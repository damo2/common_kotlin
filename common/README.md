### 注意 
> App 模块AndroidManifest 添加

    <meta-data
             android:name="APPLICATION_ID"
             android:value="${applicationId}"/>
      
通过 getApplicationIdExt()获取applicationId

### 打印日志
  [官网](https://github.com/orhanobut/logger)
  
    Logger.d(userName)
    Logger.init("MainActivity")
           .logLevel(LogLevel.FULL) //  显示全部日志，LogLevel.NONE不显示日志，默认是Full
           .methodCount(5)         //  方法栈打印的个数，默认是2
           .methodOffset(0)        //  设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
           .hideThreadInfo()      //  隐藏线程信息
           .logAdapter(new AndroidLogAdapter());// 自定义一个打印适配器，这里适配了Android的Log打印
### 吐司
    toastInfo("信息")

### json解析
    //写法1
    val user = jsonStr.gsonFromJsonExt<User>()
    val jsonStr = user.toJsonExt()
    //写法2
    MessageBean<ImageBean> message = GsonConvert.jsonToBean(jsonString, MessageBean.class, ImageBean.class);
    var  bean :List<Bean>?  = GsonConvert.jsonToBeanList(jsonStr, Bean::class.java)
    var  bean:BaseBean<SelectTypeBeam>?  = GsonConvert.fromJsonToBeanDataList(jsonStr, BaseBean::class.java, SelectTypeBeam::class.java)


### 打印日志到本地
    //初始化 isDebug 为true才会执行
    LogSD.setIsDebug(isDebug)
    LogSD.setRootDirDefault(SDPathUtils.getSDCardPublicDir("xiaoyingying/log"))
    //
    LogSD.w("打印的信息"，"filename")

### 图片加载
    ImageLoader.loader().load(this, "http://chuantu.biz/t6/345/1532056593x-1404817629.jpg", ivIcon,
                    centerCrop = true,placeholder = R.drawable.ic_launcher)

### SharedPreferences 保存信息
 eg:

    var isShowGuide: Boolean by Preference(this, ConstantsKey.KEY_IS_SHOW_GUIDE, false)
    //取值 isShowGuide
    //设置值 isShowGuide=true

    //注意 对象UserInfo 及里面的对象都要实现Serializable
     var mUserInfoBean: UserInfo by Preference<UserInfo>(this, "userinfo", UserInfo())

### SDPathUtils 获取目录
    //SD目录
    SDPathUtils.getSDCardPublicDir("xiaoyingying/log")
    //缓存目录
    SDPathUtils.getSDCardPrivateCacheDir(context,"cacheFile")

### EncryptUtils 加密解密
    //加密
    val dataEncrypt = EncryptUtils.instance.encrypt("啦啦啦123")
    //解密
    val dataDecrypt = EncryptUtils.instance.decrypt(dataEncrypt)




### CommonAdapter使用
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

    LoginService里面：
    //地址带有 https: 后 不会用 baseurl 了
    @POST("https://www.dudulifo.com/user/phonelogin")
    @FormUrlEncoded
    fun login(@FieldMap map: HashMap<String, String>): Observable<BaseBean<UserInfo>>

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

    ApiManager.apiService
              .getLanguageType()
              .compose(composeLife(LifeCycleEvent.DESTROY, lifecycleSubject))
              .compose(composeCommonBean())//.compose(composeCache(true,"testa",true))
              .subscribeExtApi({
                Logger.d("test data" + GsonUtil().toJson(it))
              })

### RxBus使用
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


### 弱引用
    private var activity: Activity? by Weak()

### 只能赋值一次，且不能为空
    var context by NotNullSingle<Context>()
