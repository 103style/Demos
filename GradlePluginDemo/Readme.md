### GradlePluginDemo

一开始遇到 `No such property: AppExtension for class: com.lxk.plugin.demo.DemoPlugin`这样的报错。

后面经过检查发现是 少了下面那个代码中的 **import语句**.
```
import com.android.build.gradle.AppExtension

class DemoPlugin extends Transform implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(this)
    }
    ....
}
```

如果 Plugin 的 groovy 代码中有没导入的包， 运行时也会提示哪些包找不到的。


再 命令行（Terminal） 输入 `gradlew build` 能看到 `DemoPlugin.groovy` 相关的日记信息


### Demo功能

通过 `ASM ByteCode Outline` 获取 [Test](https://github.com/103style/Demos/tree/master/GradlePluginDemo/app/src/main/java/com/lxk/gradleplugindemo/Test.java) 的字节码

通过 ASM 给 [GradlePluginASMTestActivity](https://github.com/103style/Demos/tree/master/GradlePluginDemo/app/src/main/java/com/lxk/gradleplugindemo/GradlePluginASMTestActivity.java) 的 `clickTest` 方法添加 耗时统计。

可以拓展为所有方法添加耗时检测。



### 参考文档
* [在AndroidStudio中自定义Gradle插件](https://blog.csdn.net/huachao1001/article/details/51810328)
* [函数插桩（Gradle + ASM）](https://juejin.im/post/5c6eaa066fb9a049fc042048)
