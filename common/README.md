*****************************************   注意  ******************************************
<meta-data
            android:name="APPLICATION_ID"
            android:value="${applicationId}"/>

********************************************************************************************

1、打印日志
   Logger.d(userName)

   Logger.init("MainActivity")         // 修改打印的tag值
   Logger.t("App").i(userName)         // 给当前打印的换一个单独的tag名
   Logger.log(DEBUG, "tag", "message", throwable) // 打印自定义级别、tag、信息等格式日志

   Logger.init("MainActivity")
        .logLevel(LogLevel.FULL) //  显示全部日志，LogLevel.NONE不显示日志，默认是Full
        .methodCount(5)         //  方法栈打印的个数，默认是2
        .methodOffset(0)        //  设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
        .hideThreadInfo()      //  隐藏线程信息
        .logAdapter(new AndroidLogAdapter());// 自定义一个打印适配器，这里适配了Android的Log打印
2、toast


3、打印日志到本地
    //初始化 isDebug 为true才会执行
    LogSD.setIsDebug(isDebug)
    LogSD.setRootDirDefault(SDPathUtils.getSDCardPublicDir("xiaoyingying/log"))
    //
    LogSD.w("打印的信息"，"filename")

4、图片加载
    ImageLoader.loader().load(this, "http://chuantu.biz/t6/345/1532056593x-1404817629.jpg", ivIcon,
                    centerCrop = true,placeholder = R.drawable.ic_launcher)

3、Preference
    SharedPreferences 保存信息
    eg:
    var isShowGuide: Boolean by Preference(this, ConstantsKey.KEY_IS_SHOW_GUIDE, false)
    取值 isShowGuide
    设置值 isShowGuide=true

    //注意 对象UserInfo 及里面的对象都要实现Serializable
     var mUserInfoBean: UserInfo by Preference<UserInfo>(this, "userinfo", UserInfo())

4、SDPathUtils 获取目录
    //SD目录
    SDPathUtils.getSDCardPublicDir("xiaoyingying/log")
    //缓存目录
    SDPathUtils.getSDCardPrivateCacheDir(context,"cacheFile")


5、路由
  官网： https://github.com/alibaba/ARouter
  举个栗子
  https://www.jianshu.com/p/7cb2cc9b726a

  本项目
  在 base模块 router.RoutePath 定义路径
  const val LOGIN_LOG = "/login/log"

  @Route(path = RoutePath.LOGIN_LOG)
  class LoginActivity : BaseActivity() {
     @Autowired
     @JvmField var username:String?=null

     @Autowired(name = "usertype")   // 可以通过name来映射URL中的不同参数
     @JvmField var type=0
     override fun onCreate(savedInstanceState: Bundle?) {
             super.onCreate(savedInstanceState ?: Bundle())
             ARouter.getInstance().inject(this)
             //...
     }
  }

  //extras = ConstantsInterceptorType.LOG  需要登录验证
  @Route(path = RoutePath.MY_USERINFO, extras = ConstantsInterceptorType.LOG)


5、EncryptUtils 加密解密
    //加密
    val dataEncrypt = EncryptUtils.instance.encrypt("啦啦啦123")
    //解密
    val dataDecrypt = EncryptUtils.instance.decrypt(dataEncrypt)




6、CommonAdapter使用
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


7、网络请求

LoginService里面：
//地址带有 https: 后 不会用 baseurl 了
@POST("https://www.dudulifo.com/user/phonelogin")
@FormUrlEncoded
fun login(@FieldMap map: HashMap<String, String>): Observable<BaseBean<UserInfo>>

RequestManager.instanceApi
                    .getLanguageType()
                    .compose(composeLife(LifeCycleEvent.DESTROY, lifecycleSubject))
                    .compose(composeCache(true,"testa",true))
                    .subscribeExtApi({
                        Logger.d("test data" + GsonUtil().toJson(it))
                    })

RxBus使用
    接收方：
        RxBus.toFlowable().subscribe(t ->
            if(t is User){

            }
        }).apply{
        //销毁时取消监听
          addSubscription(this)
        }

     发送方：
     RxBus.post(new User("张三"))
