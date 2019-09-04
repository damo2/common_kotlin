### 注意 
> App 模块AndroidManifest 添加

    <meta-data
             android:name="APPLICATION_ID"
             android:value="${applicationId}"/>

通过 applicationIdExt获取applicationId

###### 打印日志
  [logger](https://github.com/orhanobut/logger)
    
    //添加日志保存本地 DiskLogAdapter 需要存储权限
    Logger.addLogAdapter(DiskLogAdapter())
    //使用
    logd("开始")
    loge("异常")


​           
###### 吐司
    toastInfo("信息")

###### json解析
    //写法1
    val userBean = userJsonStr.gsonFromJsonExt<UserBean>()
    val jsonStr = userBean.toJsonExt()
    
    //写法2
    MessageBean<ImageBean> message = GsonConvert.jsonToBean(jsonString, MessageBean.class, ImageBean.class);
    var  bean :List<Bean>?  = GsonConvert.jsonToBeanList(jsonStr, Bean::class.java)
    var  bean :BaseBean<SelectTypeBeam>?  = GsonConvert.fromJsonToBeanDataList(jsonStr, BaseBean::class.java, SelectTypeBeam::class.java)

###### 图片加载
    ImageLoader.loader().load(this, "http://chuantu.biz/t6/345/1532056593x-1404817629.jpg", ivIcon,
                    centerCrop = true,placeholder = R.drawable.ic_launcher)

###### SharedPreferences 保存信息
 eg:
    1.PreferenceNonNull  取值不能为空
    var isShowGuide: Boolean by PreferenceNonNull(context, ConstantsKey.KEY_IS_SHOW_GUIDE, false)
    //取值 isShowGuide
    //设置值 isShowGuide=true
    
    2.Preference 取值可为空
    //注意 对象UserInfo 及里面的对象都要实现Serializable
     var mUserInfoBean: UserInfo? by Preference<UserInfo>(context, "userinfo", UserInfo::class.java)

###### SDPathUtils 获取目录
    //SD目录
    SDPathUtils.getSDCardPublicDir("xiaoyingying/log")
    //缓存目录
    SDPathUtils.getSDCardPrivateCacheDir(context,"cacheFile")

###### EncryptUtils 加密解密
    //加密
    val dataEncrypt = EncryptUtils.instance.encrypt("啦啦啦123")
    //解密
    val dataDecrypt = EncryptUtils.instance.decrypt(dataEncrypt)


###### CommonAdapter使用
    //kotlin
    val adapter = CommonAdapter(this, R.layout.$itemLayoutId$, $data$, holderConvert = { holder, data, position, payloads ->
    
    })
    
    //java
    private EmptyWrapper mAdapterWrapper;
    CommonAdapter<String> mAdapter = new CommonAdapter<String>(getApplicationContext(), R.layout.item, mRedPacketThemeList) {
                        @Override
                        protected void convert(ViewHolder holder, String str, int position) {
                            holder.getView(R.id.tv_name).setText(str);
                        }
                    };
             FullyGridLayoutManager mLayoutManager = new FullyGridLayoutManager(mContext, 2);
             //设置是否能滚动
             // mLayoutManager.setScrollEnabled(false);
             mRecyclerView.setLayoutManager(mLayoutManager);
             mRecyclerView.addItemDecoration(new GridBaseItemDecoration(mContext, 2, 16, 16, false));
             //添加加载更多
             LoadMoreWrapper mFootWrapper = new LoadMoreWrapper(mAdapter);
             View view = new View(this);
             RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtils.dp2pxFI(mContext, 10));
             view.setLayoutParams(params);
             mFootWrapper.setLoadMoreView(view);
             //添加空白页
             mAdapterWrapper = new EmptyWrapper(mFootWrapper);
             TextView tv = new TextView(mActivity);
             tv.setText("请下拉刷新重试");
             mAdapterWrapper.setEmptyView(tv);
             mRecyclerView.setAdapter(mAdapterWrapper);


### 网络请求

##### 接口
    LoginService里面：
    //地址带有 https: 后 不会用 baseurl 了
    @POST("https://www.dudulifo.com/user/phonelogin")
    @FormUrlEncoded
    fun login(@FieldMap map: HashMap<String, String>): Observable<BaseBean<UserInfo>>

##### 初始化
    object ApiManager {
        var apiService by NotNullSingle<ApiService>()
        //application 初始化
        fun initApiService() {
            apiService = RequestApiManager.instance.apply {
                initRetrofit({ clientBuilder ->
                    //添加拦截器
                }, { retrofitBuilder ->
                }, ConstApi.BaseUrl)
            }.createService(ApiService::class.java)
        }
    }

##### 请求
    //请求
    ApiManager.apiService
                    .update("1.0")
                    .composeLife(getLifecycleSubject(), LifeCycleEvent.DESTROY)//结束时取消订阅
                    .composeDefault()//统一处理异常，请求后台异常throw ApiException ，异常信息为后台给的异常内容
                    .subscribeExtApi({
                        //成功返回
                        toastInfo(it.toString())
                    }, { e ->
                        //异常，不传默认toast ApiError的异常信息，添加此处理了 isToast 无效。
                        if (e is ApiException) {
                            //可根据后台错误码处理不同异常
                            logd("${e.code}")
                        }
                        toastInfo("更新失败")
                    }, {
                        //请求完成
                    }, isShowLoad = true,// 是否显示进度框
                            context = activity,//isShowLoad 为true时必传
                            isToast = true//是否toast异常，处理了异常时无效
                    )
    
     //文件下载
     RequestFileManager.downloadFile(
                         "http://wangru.oss-cn-qingdao.aliyuncs.com/test/erp-v1.0.0-20190404.apk",
                         StorageUtils.getPublicStoragePath("test/erp.apk"),
                         { file -> Toast.makeText(applicationContext, "下载成功${file.name}", Toast.LENGTH_SHORT).show() },
                         { e -> Toast.makeText(applicationContext, "下载失败${e.message}", Toast.LENGTH_SHORT).show() },
                         { totalLength, contentLength, done ->
                             Logger.d("totalLength=$totalLength contentLength=$contentLength")
                         });
      //文件上传
      RequestFileManager.uploadFileByKey(
                          "http://www.wxjishu.com:9999/file/upload",
                          "file",
                          File(StorageUtils.getPublicStoragePath("test/wanban.apk")),
                          { str -> Logger.d("上传结果=$str") },
                          { e -> Logger.d("异常=$e") },
                          { progress,total -> Logger.d("up=$progress total=$total") }
                  )

### RxBus使用
###### 接收方：
    RxBus.toFlowable().subscribe { event ->
            if (event is DownApkEvent) {
            //todo same thing
            }
        }.apply {
            //todo 结束时取消监听
        }

###### 发送方：
    RxBus.post(new User("张三"))


### 弱引用
    private var activity: Activity? by Weak()

### 只能赋值一次，且不能为空
    var context by NotNullSingle<Context>()


​    

<br><br><br>

#### 多个判空
    val num1: Int? = null
    val num2: Int? = 2
    ifNotNull(num1, num2) { num1a, num2a ->
        //都不为空才执行
        print(num1a + num2a)
    }

#### 防止快速点击
    tvAnko.setOnClickExtNoFast {
        startActivity<AnkoActivity>()
    }

#### 限制最多输入字数
    edtInput.limitLengthExt(5,{ toastInfo("最多输入字数为5")})

#### 单例
    //没有参数 （或者使用 lazy 委托）
    class Manager {
        companion object : SingleHolder<Manager>(::Manager)
    }
    //使用
    Manager.getInstance()
    
    //一个参数
    class Manager(context: Context) {
        companion object : SingleHolder1<Manager, Context>(::Manager)
        init {
            //使用context 初始化
        }
    }
    //使用
    Manager.getInstance(context)

##### 注解

```
@KeepNotProguard
class User(var name:String)//添加注解不混淆
```

##### 日期
        val date = Date()       //现在时间
    
        val beforeDay = date - 1    //前一天
        val lastDay = date + 1      //后一天
    
        val beforeHour = date - hour(1)     //前一个小时
        val lastHour = date + month(1)       //后一个月
        val beforeHour = date - minute(1)     //前一分钟
        
        val isBefore = beforeDay < date     //比较大小
        
        date++      //本月的最后一天
        date--      //本月的第一天
        
        if(date > beforeDay){}   //比较日期

<br><br><br>
### **自定义view**
<img src="https://github.com/damo2/common/blob/master/file/ic0.jpg?raw=true" width="150">  

##### 点击倒计时
    <com.app.common.widget.CountdownButton
        android:id="@+id/btnGetCode"
        android:layout_width="110dp"
        android:layout_height="30dp"
        android:padding="0dp"
        android:textSize="12sp"
        android:textColor="#FFFFFF"
        android:text="获取验证码"
        app:cb_downFormat="%s 秒"
        app:cb_endTxt="重新获取"
        app:cb_totalTime="30000"
        app:cb_bg="@drawable/getcode_button"
        app:cb_isExitTiming="true"
        />
            
    <!--getcode_button-->
    <?xml version="1.0" encoding="utf-8"?>
    <selector xmlns:android="http://schemas.android.com/apk/res/android">
        <item android:state_enabled="true">
            <shape android:shape="rectangle">
                <solid android:color="#ff80cbc4" />
                <corners android:radius="50dp" />
            </shape>
        </item>
        <item android:state_enabled="false">
            <shape android:shape="rectangle">
                <solid android:color="@color/common_textgray" />
                <corners android:radius="50dp" />
            </shape>
        </item>
    </selector>

##### 圆角view
RoundButtonView、RoundFrameLayout、RoundLinearLayout、RoundRelativeLayout、RoundTextView 相同设置

    //圆角button
    <com.app.common.widget.round.RoundButtonView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:minHeight="0dp"
            android:padding="8dp"
            android:text="圆角"
            android:textColor="#888888"
            app:backgroundColor="#D1C4E9"
            app:backgroundPressColor="#9575CD"
            app:borderColor="#81D4FA"
            app:borderPressColor="#0277BD"
            app:borderSize="0.5dp"
            app:isRippleEnable="false"
            app:radius="4dp"
            app:textPressColor="#FFFFFF"
            />
     //圆形图片
    <com.app.common.widget.round.RoundImageView
            android:layout_width="50dp"
            android:layout_height="50dp"
            android:src="@drawable/ic_test1"
            app:borderColor="#81D4FA"
            app:borderSize="8dp"
            />


​            
​            
​         
​            
### 扩展属性和方法   
[代码](/common/src/main/java/com/app/common/extensions)   
[工具类](/common/src/main/java/com/app/common/utils)  
###### Activity 扩展属性和方法
 属性和方法  | 描述  
 ---- | ----- 
 Activity.screenWidthExt  | 屏幕宽 
 Activity.screenHeightExt  | 屏幕高 
 Activity.statusBarHeightExt  | 状态栏高度 
 Activity.navigationBarHeightExt  | 导航栏高度
 Activity.navigationBarWidthExt  | 横屏状态下导航栏的宽度
 Activity.activityRootExt  | 获取activity的根view
 Activity.hasNavBarExt  | 是否有导航栏
 Activity.statusHind()   | 隐藏状态栏
 Activity.statusTranslucent() | 沉浸式状态栏
 Activity.viewIsVisibleExt(view) | view是否在屏幕中可见


###### Context 扩展属性和方法
 属性和方法  | 描述  
 ---- | ----- 
 Context.versionCodeExt  | 版本号 
 Context.versionNameExt  | 版本名 
 Context.appNameExt  | 项目名 
 Context.applicationIdExt  | applicationId
 Context.androidIDExt  | 设备唯一id
 Context.screenWidthExt  | 屏幕宽
 Context.screenHeightExt  | 屏幕高
 Context.isAppRunningForegroundExt()   | 是否运行在前台
 Context.checkDeviceHasNavigationBarExt() | 是否有虚拟键盘
 Context.getUriFromFileExt(file,applicationId) | 文件生成Uri
 Context.getActivityExt() | 获取Activity
 Context.getAppIconDrawableExt(packageName) | 获取App图标
 Context.dp2px(dp) | dp转px
 Context.sp2px(sp) | sp转px
 Context.getColorExt(id[,theme]) | 资源id获取颜色
 Context.getColorStateListExt(id[,theme]) | 资源id获取颜色
 Context.getNetWorkTypeNameExt() | 获取当前的网络类型(WIFI,2G,3G,4G)
 Context.is4GExt() | 判断网络是否是4G
 Context.isWifiExt() | 判断wifi是否连接


###### String 扩展属性和方法
 属性和方法  | 描述  
 ---- | ----- 
 String.equalsExt(str,isIgnoerNull)  | 是否相等
 String.isEmailExt()  | 是否是邮箱
 String.isPasswordHalfAngleExt()  | 是否是密码半角（所有英文字符英文符号）
 String.setColorExt(color)  | String 设置颜色
 String.getNumExt(default)  | 获取String里面的数字
 String.toIntExt(default)  | String转int失败

###### View 扩展属性和方法
 属性和方法  | 描述  
 ---- | ----- 
 View.widthExt()  | view 宽
 View.heightExt()  | view 高
 View.setOnClickExtNoFast(time,block: (view: View) -> Unit)  | View.防止快速点击
 View.setMarginExt(left,top,right,bottom)  | 设置 Margin
 View.setPaddingExt(left,top,right,bottom)  | 设置 Padding
 View.setWidthHeightExt(width,height)  | 设置宽高
 View.getActivityExt() | 获取 activity
 View.toBitmapExt() | view转成bitmap

###### EditText 扩展属性和方法
 属性和方法  | 描述  
 ---- | ----- 
 EditText.inhibitInputSpaceExt()  | 禁止输入空格 
 EditText.limitLengthExt(length,outCallback)  | 限制输入长度
 EditText.setOnlyDecimalExt(isShow)  | 只能输入数字和小数点，小数点只能1个且小数点后最多只能2位 
 EditText.setPwdShowOrHindExt()  | 设置密码显示或隐藏

###### File 扩展属性和方法
 属性和方法  | 描述  
 ---- | ----- 
 File.headerExt  | 获取文件头
 File.metadataExt  | 文件类型
 File.isImageExt  | 是否是图片
 File.sizeExt  | 获取文件的大小
 String.filenameExt  | 文件名字带后缀
 String.filenameNoExtensionExt  | 文件名字不带后缀
 String.fileExtensionExt  | 文件后缀
 File.appendExt(txt,isCreate)  | 向文件中追加文本 
 File.getVideoInfoExt((duration: Int, width: Int, height: Int) -> Unit) | 获取视频信息           

## 参考及使用
    圆角view https://github.com/H07000223/FlycoRoundView
    通用adapter https://github.com/hongyangAndroid/baseAdapter
    字体对齐的TextView  https://github.com/androiddevelop/AlignTextView
    日志 https://github.com/orhanobut/logger