数据库缓存

项目build.gradle 添加

    classpath "io.objectbox:objectbox-gradle-plugin:2.3.1"

application 初始化

    ObjectBoxInit.build(ctx);

使用

    data class UserBean(var name: String, var age: Int)

    var userBean by Dao<UserBean>(UserBean::class.java,"user")

保存(直接赋值保存)

    userBean = UserBean("张三",Random().nextInt(100))

取值(直接调用就是取值)

    Log.d(userBean.toJsonExt())

