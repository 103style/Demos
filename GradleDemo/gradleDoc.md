[TOC]

### 获取构建信息

* 查看项目列表 `gradle -q projects`
   ```
   //build.gradle
   //添加项目添加描述信息
   description = 'root build file'
    
   buildscript {}
   ```

* 获取任务列表 `gradle -q tasks`, 获取所有的默认任务以及每个任务的描述.
  获取任务更多信息 `gradle -q tasks --all` ， `--all` 会列出项目中所有任务以及任务之间的依赖关系.
  ```
  //更改任务报告内容
  taskname {
      description = 'Builds the distribution'
      group = 'build'
  }
  ```

* 获取任务具体信息 `gradle help --task taskName`  输出包含了任务的路径、类型以及描述信息等.

* 查看某一个 `task` 的详情 `gradle -q api:dependencies`

* 过滤依赖信息  `gradle -q api:dependencies --configuration testCompile`

* ~~查看特定依赖 `gradle dependencyInsight`~~

* 获取项目属性列表 `gradle properties`

* 获取构建日志 `gradlew --profile --offline --rerun-tasks taskname`.
  e.g. `gradlew --profile --offline --rerun-tasks assembleDebug`
  `--profile`：启用性能剖析，
  `--offline`：禁止 Gradle 提取在线依赖项
  `--rerun-tasks`：强制 Gradle 重新运行所有任务并忽略任何任务优化。

---

### Gradle 构建语言
##### 项目API
```
//build.gralde
println name
println project.name
```
添加以上代码，通过 `gradle -q check` 可以查看输出
```
E:\Demos\GradleDemo> gradlew -q check
GradleDemo
GradleDemo
```
如您所见，两个 println 语句都输出了相同的属性，第一个输出使用的是自动委托 (`auto-delegation`), 因为当前属性并没有在构建脚本中定义. 另一个语句使用了项目一个属性，这个属性在任何构建脚本中都可用，它的返回值是被关联的工程对象. 只有当您定义了一个属性或者一个方法, 它的名字和工程对象的某个成员的名字相同时, 你应该使用项目属性.

##### 标准项目属性
![标准项目属性](https://upload-images.jianshu.io/upload_images/1709375-7e1c18414665a6c5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 脚本API
声明变量： 在 Gradle 构建脚本中有两种类型的变量可以声明：局部变量 (`local`) 和 扩展属性 (`extra`) .

**局部变量** 使用关键字 `def` 来声明，其只在声明它的地方可见 . 
局部变量是 Groovy 语言的一个基本特性.
```
def dest = "dest"
task copy(type: Copy) {
      form "source"
      into dest
}
```

**扩展属性**，在 Gradle 领域模型中所有被增强的对象能够拥有自己定义的属性. 这包括但不仅限于 `projects`,`tasks`, 还有 `source sets`。
 Project 对象可以添加，读取，更改扩展的属性. 另外，使用 ext 扩展块可以一次添加多个属性。
```
ext {
    author= "103style,top"
    emailNotification = "build@master.org"
}
task printProperties << {
    println springVersion
    println emailNotification
}
```
`gradle printProperties`
```
E:\Demos\GradleDemo> gradlew printProperties
103style.top
wtimexiaoke@gmail.com
```

##### Groovy 基础
属性存取器
```
// 使用 getter 方法
println project.buildDir
println getProject().getBuildDir()

// 使用 setter 方法
project.buildDir = 'target'
getProject().setBuildDir('target')
```

---

### 深入了解 Tasks
**操作符 `<<` 在Gradle 4.x中被弃用，并且在Gradle 5.0 被移除了。**

**定义 tasks**
```
task('taskname1'){
    println 'taskname1'
}
task(copy, type: Copy) {
    from(file('srcDir'))
    into(buildDir)
}

// 使用 strings 来定义任务的名字
task('hello2') {
    println "hello2"
}
task('copy1', type: Copy) {
    from(file('srcDir'))
    into(buildDir)
}

// 另外一种语法形式
tasks.create(name: 'taskname2'){
    println 'taskname2'
}
tasks.create(name: 'copy2', type: Copy) {
    from(file('srcDir'))
    into(buildDir)
}
```

##### 定位 tasks
```
// 通过属性获取 tasks
task taskname4
println taskname4.name
println project.taskname4.name

//通过 tasks collection 获取 tasks
task taskname5
println tasks.taskname5.name
println tasks['taskname5'].name
```

##### 配置 tasks
```
// 创建一个 copy task
task myCopy1(type: Copy)

//配置一个任务 - 不同的方法
Copy myCopy = task(myCopy2, type: Copy)
myCopy.from 'resources'
myCopy.into 'target'
myCopy.include('**/*.txt', '**/*.xml', '**/*.properties')

//配置一个任务 - 通过闭包 closure
task myCopy3(type: Copy)
myCopy3 {
   from 'resources'
   into 'target'
   include('**/*.txt', '**/*.xml', '**/*.properties')
}

//通过定义一个任务
task copy4(type: Copy) {
   from 'resources'
   into 'target'
   include('**/*.txt', '**/*.xml', '**/*.properties')
}
```

##### 给 task 加入依赖
```
task taskName1 {
    println 'taskName1'
}
task taskName2(dependsOn: 'taskName1'){
    println 'taskName2'
}
task taskName3(dependsOn: ':app:lint'){
    println 'taskName3'
}
//通过任务对象加入依赖
taskName3.dependsOn taskName1 
```

##### 给 task 加入描述
```
task copy(type: Copy) {
   description 'Copies the resource directory to the target directory.'
   from 'resources'
   into 'target'
   include('**/*.txt', '**/*.xml', '**/*.properties')
}
```


##### Task 规则
```
tasks.addRule("Pattern: ping<ID>") { String taskName ->
    if (taskName.startsWith("ping")) {
        task(taskName)  {
            println "Pinging: " + (taskName - 'ping')
        }
    }
}

// 基于规则的任务依赖
task groupPing {
    dependsOn pingServer1, pingServer2
}
```
```
E:\Demos\GradleDemo> gradlew -q pingServer1
Pinging: Server1

E:\Demos\GradleDemo> gradlew -q groupPing
Pinging: Server1
Pinging: Server2
```

---

### 文件操作
##### 定位文件
```
// 使用一个相对路径
File configFile = file('src/config.xml')

// 使用一个绝对路径
configFile = file(configFile.absolutePath)
println configFile

// 使用一个项目路径的文件对象
configFile = file(new File('src/config.xml'));
println configFile
```
```
E:\Demos\GradleDemo\src\config.xml
E:\Demos\GradleDemo\src\config.xml
```

##### 文件集合
```
FileCollection collection = files(
        'src/file.txt',
        new File('src/file1.txt'),
        ['src/file2.txt', 'src/file3.txt',]
)
// 对文件集合进行迭代
collection.each { File file ->
    println file.name
}

// 转换文件集合为其他类型
Set set = collection.files
Set set2 = collection as Set
List list = collection as List
String path = collection.asPath
//File file = files.singleFile
//File file2 = files as File

// 增加和减少文件集合
def union = collection + files('src/file3.txt')
println(union.size())
def different = collection - files('src/file3.txt')
println(different.size())


//列出文件夹下的 文件
task list {
    File srcDir

    // 使用闭合创建一个文件集合
    def collection = files { srcDir.listFiles() }
    
    srcDir = file('app')
    println "Contents of $srcDir.name"
    collection.collect { it.name }.sort().each { println it }

    srcDir = file('build')
    println "Contents of $srcDir.name"
    collection.collect { relativePath(it) }.sort().each { println it }
}
```

##### 文件树
```
//以一个基准目录创建一个文件树
FileTree tree = fileTree(dir: 'app/src/main')

// 使用路径创建一个树
//tree = fileTree('src').include('**/*.java')
// 使用闭合创建一个数
//tree = fileTree('src') { include '**/*.java'}
// 使用map创建一个树
//tree = fileTree(dir: 'src', include: '**/*.java')
//tree = fileTree(dir: 'src', includes: ['**/*.java', '**/*.xml'])
//tree = fileTree(dir: 'src', include: '**/*.java', exclude: '**/*test*/**')

// 添加包含和排除规则
tree.include '**/*.java'
tree.include '**/*.xml'
tree.exclude '**/Abstract*'

//遍历文件树
tree.each { File file ->
    println file.name
}
// 过滤文件树
FileTree filtered = tree.matching {
    include '**/*.xml'
}
// 合并文件树A
FileTree sum = tree + fileTree(dir: 'src/test')
// 访问文件数的元素
tree.visit { element -> println "$element.name => $element.size" }
```

##### 指定一组输入文件
```
compile {
    //使用一个 File 对象设置源目录
    source = file('src/main/java')
    //使用一个字符路径设置源目录
    source = 'src/main/java'
    // 使用一个集合设置多个源目录
    source = ['src/main/java', '../shared/java']

    // 使用 FileCollection 或者 FileTree 设置源目录
    source = fileTree(dir: 'src/main/java').matching { include 'org/gradle/api/**' }

    // 使用一个闭合设置源目录
    source = {
        // Use the contents of each zip file in the src dir
        file('src').listFiles().findAll { it.name.endsWith('.zip') }.collect { zipTree(it) }
    }

    //指定文件
    // 使用字符路径添加源目录
    source 'src/main/java', 'src/main/groovy'
    // 使用 File 对象添加源目录
    source file('../shared/java')
    // 使用闭合添加源目录
    source { file('src/test/').listFiles() }
}
```

##### 复制文件
```
task copyTask(type: Copy) {
    exclude '**/build' //过滤文件
    exclude '**/libs' //过滤文件
    exclude '**/res' //过滤文件
    from 'app'//源目录 或者 源文件
    into 'test'//目标目录
//    // 使用一个闭合映射文件名
//    def i = 0;
//    rename { String fileName ->
//        fileName = i++ + fileName.substring(fileName.lastIndexOf('.'));
//    }
    // 使用正则表达式映射文件名
    rename '(.)(.+)\\.(.+)',  '$1.$3'
    //去除复制文件内容中的空行
    filter { String line ->
        line.length() == 0 ? null : line
    }
}
```

##### 同步任务
同步任务 ( Sync ) 任务继承自复制任务 ( Copy ) , 当它执行时,它会复制源文件到目标目录中,然后从目标目录中的删除所有非复制的文件。
```
//将test目录清空 并将app目录的内容复制到test目录下
task sync(type: Sync) {
    from 'app'
    into "test"
}
```