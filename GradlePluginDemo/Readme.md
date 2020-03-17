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