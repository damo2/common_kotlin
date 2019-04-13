package com.app.common.widget.wheelview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LoopWheelViewTwo extends View {

    public static final int FONT_COLOR = 0xff909090;
    public static final int CURRENT_ITEM_FONT_COLOR = 0xff646464;
    public static final int FONT_SIZE = 50;
    public static final int CURRENT_ITEM_FONT_SIZE = 80;
    public static final int PADDING = 10;
    public static final int SHOW_COUNT = 3;
    public static final int SELECT = 0;
    private int itemHeight;
    //需要显示的行数
    private int showCount = SHOW_COUNT;
    //当前默认选择的位置
    private int select = SELECT;
    //字体颜色、大小、补白
    private int fontColor = FONT_COLOR;
    private int fontSize = FONT_SIZE;
    //文本列表
    private List<String> lists;
    //选中项的辅助文本，可为空
    private String selectTip;
    //每一项Item和选中项
    private List<WheelItem> wheelItems = new ArrayList<>();
    private WheelSelect wheelSelect = null;
    //手点击的Y坐标
    private float mTouchY;
    //监听器
    private OnWheelViewItemSelectListener listener;

    public LoopWheelViewTwo setLoop(boolean loop) {
        isLoop = loop;
        return this;
    }

    private boolean isLoop = true;

    public LoopWheelViewTwo(Context context) {
        super(context);
    }

    public LoopWheelViewTwo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoopWheelViewTwo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置选中项的复制文本，可以不设置
     */
    public LoopWheelViewTwo selectTip(String selectTip) {
        this.selectTip = selectTip;
        return this;
    }

    /**
     * 设置文本列表，必须且必须在build方法之前设置
     */
    public LoopWheelViewTwo lists(List<String> lists) {
        this.lists = lists;
        return this;
    }

    /**
     * 设置显示行数，不设置的话默认为3
     */
    public LoopWheelViewTwo showCount(int showCount) {
        if (showCount % 2 == 0) {
            throw new IllegalStateException("the showCount must be odd");
        }
        this.showCount = showCount;
        return this;
    }

    /**
     * 设置默认选中的文本的索引，不设置默认为0
     */
    public LoopWheelViewTwo select(int select) {
        this.select = select;
        return this;
    }

    /**
     * 最后调用的方法，判断是否有必要函数没有被调用
     */
    public LoopWheelViewTwo build() {
        if (lists == null) {
            throw new IllegalStateException("this method must invoke after the method [lists]");
        }
        return this;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //得到总体宽度
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        // 得到每一个Item的高度
        @SuppressLint("DrawAllocation") Paint mPaint = new Paint();
        mPaint.setTextSize(fontSize);
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        itemHeight = (int) (metrics.bottom - metrics.top) + 2 * PADDING;
        //初始化每一个WheelItem
        initWheelItems(width, itemHeight);
        //初始化WheelSelect
        int rightPadding = 0;
        if (selectTip != null) {
            if (selectTip.equals("月") || selectTip.equals("日")) {
                rightPadding = (int) (width * 0.2);
            } else {
                rightPadding = (int) (width * 0.05);
            }
        }
        wheelSelect = new WheelSelect(showCount / 2 * itemHeight, width, itemHeight, selectTip,
        fontColor, fontSize, rightPadding
        );
        //得到所有的高度
        int height = itemHeight * showCount;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    /**
     * 创建显示个数+2个WheelItem
     */
    private void initWheelItems(int width, int itemHeight) {
        wheelItems.clear();
        int extraItemCount;
        if (isLoop) {
            extraItemCount = 2;
        } else {
            extraItemCount = 0;
        }
        for (int i = 0; i < showCount + extraItemCount; i++) {
            int startY;
            int stringIndex;
            if (isLoop) {
                startY = itemHeight * (i - 1);
                stringIndex = select - showCount / 2 - 1 + i;
            } else {
                startY = itemHeight * i;
                stringIndex = i;
            }
            if (stringIndex < 0) {
                stringIndex = lists.size() + stringIndex;
            }
            if (stringIndex >= lists.size()) {
                stringIndex = stringIndex - lists.size();
            }
            String text = lists.get(stringIndex);
            WheelItem item = new WheelItem(startY, width, itemHeight, fontColor, fontSize,
            text);
            wheelItems.add(item);
            if (text.equals(lists.get(select))) {
                item.setFontSize(CURRENT_ITEM_FONT_SIZE);
                item.setFontColor(CURRENT_ITEM_FONT_COLOR);
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = event.getY() - mTouchY;
                mTouchY = event.getY();
                //-200是上滑坐标界限，超出-200之后的滑动事件不作处理
                if (mTouchY > -200) {
                    handleMove(dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                handleUp();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 处理移动操作
     */
    private void handleMove(float dy) {
        //调整坐标
        for (WheelItem item : wheelItems) {
            item.adjust(dy);
            //如果startY在selectItem的中点上面，则将该项的字体改大
            if (item.getStartY() > wheelSelect.getStartY() && item.getStartY() < (
            wheelSelect.getStartY() + itemHeight / 2)) {
                item.setFontSize(CURRENT_ITEM_FONT_SIZE);
                item.setFontColor(CURRENT_ITEM_FONT_COLOR);
            } else {
                //单次点击的情况下dy的值是0 不做处理
                if (dy != 0) {
                    item.setFontSize(FONT_SIZE);
                    item.setFontColor(FONT_COLOR);
                }
            }
        }
        if (!isLoop) {
            WheelItem firstItem = wheelItems.get(0);
            WheelItem lastItem = wheelItems.get(showCount - 1);
            if ((firstItem.getStartY() - wheelSelect.getStartY()) > 50 || (wheelSelect.getStartY() - lastItem.getStartY()) > 50) {
                return;
            }
        }
        invalidate();
        //调整
        adjust();
    }

    /**
     * 处理抬起操作
     */
    private void handleUp() {
        int index = -1;
        //得到应该选择的那一项
        for (int i = 0; i < wheelItems.size(); i++) {
            WheelItem item = wheelItems.get(i);
            //如果startY在selectItem的中点上面，则将该项作为选择项
            if (item.getStartY() > wheelSelect.getStartY() && item.getStartY() < (
            wheelSelect.getStartY() + itemHeight / 2)) {
                index = i;
                break;
            }
            //如果startY在selectItem的中点下面，则将上一项作为选择项
            if (item.getStartY() >= (wheelSelect.getStartY() + itemHeight / 2)
            && item.getStartY() < (wheelSelect.getStartY() + itemHeight)) {
                index = i - 1;
                break;
            }
        }
        if (isLoop) {
            //如果没找到或者其他因素，直接返回
            if (index == -1) {
                return;
            }
        } else {
            WheelItem firstItem = wheelItems.get(0);
            WheelItem lastItem = wheelItems.get(showCount - 1);
            if ((wheelSelect.getStartY() - lastItem.getStartY() > 1)) {
                index = showCount - 1;
            } else if ((firstItem.getStartY() - wheelSelect.getStartY()) > 1) {
                index = 0;
            } else {
                return;
            }
        }
        //得到偏移的位移
        float dy = wheelSelect.getStartY() - wheelItems.get(index).getStartY();
        //调整坐标
        for (WheelItem item : wheelItems) {
            item.adjust(dy);
        }
        invalidate();
        // 调整
        adjust();
        //设置选择项
        int stringIndex = lists.indexOf(wheelItems.get(index).getText());
        if (stringIndex != -1) {
            select = stringIndex;
            if (listener != null) {
                listener.onItemSelect(select);
            }
        }
        WheelItem selectedItem = wheelItems.get(index);
        // 调整index 偏移量86时代表上方错位 258代表下方错位
        if (selectedItem.getStartY() == 86) {
            index += 1;
            select += 1;
        } else if (selectedItem.getStartY() == 258) {
            index -= 1;
            select -= 1;
        }
        for (int i = 0; i < wheelItems.size(); i++) {
            WheelItem wheelItem = wheelItems.get(i);
            if (i == index) {
                wheelItem.setFontSize(CURRENT_ITEM_FONT_SIZE);
                wheelItem.setFontColor(CURRENT_ITEM_FONT_COLOR);
            } else {
                wheelItem.setFontSize(FONT_SIZE);
                wheelItem.setFontColor(FONT_COLOR);
            }
        }
        invalidate();
    }

    /**
     * 调整Item移动和循环显示
     */
    private void adjust() {
//        if (isLoop) {
            //如果向下滑动超出半个Item的高度，则调整容器
            if (wheelItems.get(0).getStartY() >= -itemHeight / 2) {
                //移除最后一个Item重用
                WheelItem item = wheelItems.remove(wheelItems.size() - 1);
                //设置起点Y坐标
                item.setStartY(wheelItems.get(0).getStartY() - itemHeight);
                //得到文本在容器中的索引
                int index = lists.indexOf(wheelItems.get(0).getText());
                if (index == -1) {
                    return;
                }
                index -= 1;
                if (index < 0) {
                    index = lists.size() + index;
                }
                //设置文本
                item.setText(lists.get(index));
                //添加到最开始
                wheelItems.add(0, item);
                invalidate();
                return;
            }
            //如果向上滑超出半个Item的高度，则调整容器
            if (wheelItems.get(0).getStartY() <= (-itemHeight / 2 - itemHeight)) {
                //移除第一个Item重用
                WheelItem item = wheelItems.remove(0);
                //设置起点Y坐标
                item.setStartY(wheelItems.get(wheelItems.size() - 1).getStartY() + itemHeight);
                //得到文本在容器中的索引
                int index = lists.indexOf(wheelItems.get(wheelItems.size() - 1).getText());
                if (index == -1) {
                    return;
                }
                index += 1;
                if (index >= lists.size()) {
                    index = 0;
                }
                //设置文本
                item.setText(lists.get(index));
                //添加到最后面
                wheelItems.add(item);
                invalidate();
            }
//        }
    }

    /**
     * 得到当前的选择项
     */
    public int getSelectItem() {
        return select;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制每一项Item
        for (WheelItem item : wheelItems) {
            item.onDraw(canvas);
        }
        //绘制阴影
        if (wheelSelect != null) {
            wheelSelect.onDraw(canvas);
        }
    }

    /**
     * 设置监听器
     */
    public LoopWheelViewTwo listener(OnWheelViewItemSelectListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnWheelViewItemSelectListener {
        void onItemSelect(int index);
    }

    class WheelItem {
        // 起点Y坐标、宽度、高度
        private float startY;
        private int width;
        private int height;
        //四点坐标
        private RectF rect = new RectF();

        void setFontColor(int fontColor) {
            this.fontColor = fontColor;
        }

        //字体大小、颜色
        private int fontColor;

        private int fontSize;
        private String text;

        private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        WheelItem(float startY, int width, int height, int fontColor, int fontSize, String text) {
            this.startY = startY;
            this.width = width;
            this.height = height;
            this.fontColor = fontColor;
            this.fontSize = fontSize;
            this.text = text;
            adjust(0);
        }

        /**
         * 根据Y坐标的变化值，调整四点坐标值
         */
        void adjust(float dy) {
            startY += dy;
            rect.left = 0;
            rect.top = startY;
            rect.right = width;
            rect.bottom = startY + height;
        }

        float getStartY() {
            return startY;
        }

        /**
         * 直接设置Y坐标属性，调整四点坐标属性
         */
        void setStartY(float startY) {
            this.startY = startY;
            rect.left = 0;
            rect.top = startY;
            rect.right = width;
            rect.bottom = startY + height;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        void onDraw(Canvas mCanvas) {
            //设置钢笔属性
            mPaint.setTextSize(fontSize);
            mPaint.setColor(fontColor);
            //得到字体的宽度
            int textWidth = (int) mPaint.measureText(text);
            //drawText的绘制起点是左下角,y轴起点为baseLine
            Paint.FontMetrics metrics = mPaint.getFontMetrics();
            int baseLine = (int) (rect.centerY() + (metrics.bottom - metrics.top) / 2 - metrics.bottom);
            //居中绘制
            mCanvas.drawText(text, rect.centerX() - textWidth / 2, baseLine, mPaint);
        }

        void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }
    }

    class WheelSelect {
        //黑框背景颜色
        private final int COLOR_BACKGROUND = Color.parseColor("#77777777");
        //黑框的Y坐标起点、宽度、高度
        private int startY;
        //四点坐标
        private Rect rect = new Rect();
        //需要选择文本的颜色、大小、补白
        private String selectText;
        private int fontColor;
        private int fontSize;
        private int padding;
        private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        WheelSelect(int startY, int width, int height, String selectText, int fontColor, int fontSize,
                    int padding) {
            this.startY = startY;
            this.selectText = selectText;
            this.fontColor = fontColor;
            this.fontSize = fontSize;
            this.padding = padding;
            rect.left = 0;
            rect.top = startY;
            rect.right = width;
            rect.bottom = startY + height;
        }

        int getStartY() {
            return startY;
        }

        void onDraw(Canvas mCanvas) {
            //绘制背景
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(COLOR_BACKGROUND);
            mPaint.setStrokeWidth(1);
            mCanvas.drawLine(0, rect.top, rect.right, rect.top, mPaint);
            mCanvas.drawLine(0, rect.bottom, rect.right, rect.bottom, mPaint);
            //绘制提醒文字
            if (selectText != null) {
                //设置钢笔属性
                mPaint.setTextSize(fontSize);
                mPaint.setColor(fontColor);
                //得到字体的宽度
                int textWidth = (int) mPaint.measureText(selectText);
                //drawText的绘制起点是左下角,y轴起点为baseLine
                Paint.FontMetrics metrics = mPaint.getFontMetrics();
                int baseLine = (int) (rect.centerY() + (metrics.bottom - metrics.top) / 2 - metrics.bottom);
                //在靠右边绘制文本
                mCanvas.drawText(selectText, rect.right - padding - textWidth, baseLine, mPaint);
            }
        }
    }
}
