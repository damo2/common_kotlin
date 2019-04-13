
项目build.gradle 添加
classpath "io.objectbox:objectbox-gradle-plugin:2.3.1"

application 初始化
ObjectBoxInit.build(ctx);

使用
var user by Dao<User>(User::class.java, "user" [,defaultUser])

保存(直接赋值保存)
user = mUser

取值(直接调用就是取值)
user

