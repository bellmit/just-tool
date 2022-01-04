ClassLoader
====================================================
BootStrapClassLoader：负责加载JAVA核心包，JVM自带。
ExtClassLoader：负责加载扩展包路径（java.ext）。
AppClassLoader：负责加载ClassPath目录的jar包。

CustomClassLoader:自定义加载逻辑。

加载逻辑，由上之下；Class的加载采用双亲委派（extend, parent）

同一个ClassLoader只会存在一个相同的Class对象。
不同的ClassLoader加载相同的Class对象，他们不能相互转换。

加载->校验->准备->解析->初始化...销毁...


defineClass:接受二进制属性的Class文件对象转换为JAVA的Class对象