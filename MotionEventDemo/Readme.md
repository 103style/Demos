《Android开发艺术探索》 学习记录

`base on Android-29`  

[验证和分析Android的事件分发机制 -- 验证部分的记录](https://juejin.im/post/5dee7424518825127a03663a)


---

### 目录
* 实例验证
* 验证结果的小结

---


### 示例验证
**这里大家主要关注打印的日志信息，看下事件分发的过程即可。**

[Demo源码github地址](https://github.com/103style/Demos/tree/master/MotionEventDemo)

我们分别创建监听上面三个方法的 ViewGroup 和 View，然后在测试 Activity 中重写上面的方法，源码链接如下：
[TestLinearLayout.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/TestLinearLayout.java)、[TestView.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/TestView.java)、[EventHandler.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/EventHandler.java)、[MainActivity.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/MainActivity.java)、[activity_main.xml](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/res/layout/activity_main.xml)

**reset** 点击之后的处理逻辑。
```
TestLinearLayout.setIsIntercept(false);
TestLinearLayout.setInterceptEvent(-1024);
testLinearLayout.setOnClickListener(null);
testView.setOnClickListener(null);
testLinearLayout.setClickable(false);
testView.setClickable(false);
```

---

我们通过以下测试方法来验证：
`除了默认，其他没有设置点击事件时，都设置为不点击，即setClickable(fasle)`
> * 默认什么都不做
> * View 设置点击事件
> * ViewGroup 设置点击事件
> * View 和 ViewGroup 都设置点击事件
> * ViewGroup 仅拦截全部事件，即 `onInterceptTouchEvent` 返回 `true`
> * ViewGroup 拦截全部事件，并设置 ViewGroup 的点击事件
> * ViewGroup 拦截全部事件，并设置 View 的点击事件
> * ViewGroup 拦截全部事件，并设置 View 和 ViewGroup 的点击事件
> * ViewGroup 仅拦截 DOWN 事件
> * ViewGroup 拦截 DOWN 事件，并设置 ViewGroup 的点击事件
> * ViewGroup 拦截 DOWN 事件，并设置 View 的点击事件
> * ViewGroup 拦截 DOWN 事件，并设置 View 和 ViewGroup 的点击事件
> * ViewGroup 仅拦截 MOVE 事件
> * ViewGroup 拦截 MOVE 事件，并设置 ViewGroup 的点击事件
> * ViewGroup 拦截 MOVE 事件，并设置 View 的点击事件
> * ViewGroup 拦截 MOVE 事件，并设置 View 和 ViewGroup 的点击事件
> * ViewGroup 仅拦截 UP 事件
> * ViewGroup 拦截 UP 事件，并设置 ViewGroup 的点击事件
> * ViewGroup 拦截 UP 事件，并设置 View 的点击事件
> * ViewGroup 拦截 UP 事件，并设置 View 和 ViewGroup 的点击事件
> * 仅设置 View 为可点击的
> * 仅设置 View 的 enable 为 false
> * 设置 View 的 enable 为 false 并且可点击 

---

##### 默认状态
运行程序，我们点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN
MainActivity: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
MainActivity: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
MainActivity: onTouchEvent: ev.getAction() = ACTION_UP
```
>`tips`: 多次表示 下面那些事件依次执行 循环多次.

从以上日志我们可以看到 DOWN 事件的传递路径为从 `MainActivity.dispatchTouchEvent` → `TestLinearLayout.dispatchTouchEvent` → `TestLinearLayout.onInterceptTouchEvent` → `TestView.dispatchTouchEvent` → `TestView.onTouchEvent` → `TestLinearLayout.onTouchEvent` → `MainActivity.onTouchEvent`, 而 MOVE 和  UP 事件就只发生在 MainActivity.
因为 View 默认 clickable 和 longClickable 都为 `false`，所以没有消耗 DOWN 事件。

满足上述第 1、2、4、6、7、8、11 点。

---

##### 仅View设置点击事件
我们点击示例中的 **View setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_MOVE, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestView: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_UP, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestView: onTouchEvent: ev.getAction() = ACTION_UP
```
因为 TestView 消耗了 DOWN 事件，后面的每个事件都传递到了 TestView，我们可以看到 DOWN、MOVE、UP 每个事件都是从 MainActivity → TestLinearLayout → TestView，TestView 并没有处理 MOVE 所以就 消失 了。

满足上述第 1、2、4、5、6、7、11 点。

---

##### 仅ViewGroup设置点击事件
我们依次点击示例中的 **Reset** 和 **ViewGroup setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_UP
```
我们可以看到 DOWN 事件是从 MainActivity → TestLinearLayout → TestView → TestLinearLayout，而MOVE、UP事件就只从 MainActivity → TestLinearLayout.
因为 TestView 没有消耗 DOWN 事件，然后传给TestLinearLayout，TestLinearLayout消耗了 DOWN 事件，所以后面的每个事件都只传递到了 TestLinearLayout。

满足上述第 1、2、4、5、6、7、8、11 点。

---

##### View和ViewGroup都设置点击事件
我们依次点击示例中的 **Reset** 、**View setOnClick** 和 **ViewGroup setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN

多个
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_MOVE, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestView: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_UP, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestView: onTouchEvent: ev.getAction() = ACTION_UP
```
这里和 “仅View设置点击事件” 一致，因为 TestView 消耗了 DOWN 事件，TestLinearLayout 就无法再消耗 DOWN 事件了。

---

##### ViewGroup设置拦截全部事件 `(仅拦截，没消耗)`
我们依次点击示例中的 **Reset** 和 **ViewGroup Intercept All** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = true
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN
MainActivity: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
MainActivity: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
MainActivity: onTouchEvent: ev.getAction() = ACTION_UP
```
我们可以看到 DOWN 事件传递路径为 MainActivity → TestLinearLayout，因为DOWN 事件被拦截了，所以 TestView 收不到事件，而MOVE、UP事件就只在 MainActivity.

满足上述第 1、3、4、5、6、7、11 点。

---

##### ViewGroup设置拦截全部事件，并设置ViewGroup的点击事件.
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept All** 和 **ViewGroup setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept 
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_UP
```
这里我们可以看到 DOWN 事件传递路径为 MainActivity → TestLinearLayout，因为TestLinearLayout消耗了 DOWN 事件，所以MOVE、UP事件也传到了 TestLinearLayout.

满足上述第 1、2、3、4、5、6、7、8、11 点。

---

##### ViewGroup设置拦截全部事件，并设置View的点击事件.
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept All** 和 **View setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = true
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN
MainActivity: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
MainActivity: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
MainActivity: onTouchEvent: ev.getAction() = ACTION_UP
```
和 “ViewGroup仅设置拦截全部事件” 一致，TestLinearLayout 拦截了事件，所以 TestView 收不到事件，而TestLinearLayout 没有消耗 DOWN 事件，后续的事件也没有继续传递到他。


---

##### ViewGroup设置拦截全部事件，并设置View和ViewGroup的点击事件.
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept All**、 **View setOnClick** 和 **ViewGroup setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = true
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_UP
```
和 “ViewGroup设置拦截全部事件，并设置ViewGroup的点击事件” 一致，TestLinearLayout 拦截了事件，所以 TestView 收不到事件，而TestLinearLayout 消耗了 DOWN 事件，后续的事件也都继续传递到他。

---

##### 仅ViewGroup拦截DOWN事件
我们依次点击示例中的 **Reset** 和 **ViewGroup Intercept down**按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestLinearLayout: intercept event = ACTION_DOWN
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN
MainActivity: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
MainActivity: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
MainActivity: onTouchEvent: ev.getAction() = ACTION_UP
```
和 “ViewGroup仅设置拦截全部事件” 一致，TestLinearLayout 拦截了事件，所以 TestView 收不到事件，而TestLinearLayout 没有消耗 DOWN 事件，后续的事件也没有继续传递到他。

---

##### ViewGroup拦截DOWN事件，并设置ViewGroup的点击事件
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept down** 和 **ViewGroup setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestLinearLayout: intercept event = ACTION_DOWN
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_UP
```
和 “ViewGroup设置拦截全部事件，并设置ViewGroup的点击事件” 一致，TestLinearLayout 拦截了事件，所以 TestView 收不到事件，而TestLinearLayout 消耗了 DOWN 事件，后续的事件也都继续传递到他。

---

##### ViewGroup拦截DOWN事件，并设置View的点击事件
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept down** 和 **View setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestLinearLayout: intercept event = ACTION_DOWN
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN
MainActivity: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
MainActivity: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
MainActivity: onTouchEvent: ev.getAction() = ACTION_UP
```
和 “ViewGroup设置拦截全部事件” 一致，TestLinearLayout 拦截了事件，所以 TestView 收不到事件，而TestLinearLayout 没有消耗 DOWN 事件，后续的事件也没有继续传递到他。

---

##### ViewGroup拦截DOWN事件，并设置View和ViewGroup的点击事件
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept down**、**View setOnClick**  和 **ViewGroup setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestLinearLayout: intercept event = ACTION_DOWN
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_UP
```
和 “ViewGroup拦截DOWN事件，并设置ViewGroup的点击事件” 一致，TestLinearLayout 拦截了事件，所以 TestView 收不到事件，而TestLinearLayout 消耗了 DOWN 事件，后续的事件也都继续传递到他。

---

##### 仅ViewGroup拦截MOVE事件
我们依次点击示例中的 **Reset** 和 **ViewGroup Intercept move** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN
MainActivity: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
MainActivity: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
MainActivity: onTouchEvent: ev.getAction() = ACTION_UP
```
和 “默认情况” 一致，因为 TestLinearLayout 和 TestView 都没消耗 DOWN 事件，所以后续事件都不能传递到它们。

---

##### ViewGroup拦截MOVE事件，并设置ViewGroup的点击事件
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept move** 和 **ViewGroup setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_UP
```
和 “设置ViewGroup的点击事件” 的点击事件一致，此时事件已经由 TestLinearLayout 处理，所以也没回调 onInterceptTouchEvent 方法。

---

##### ViewGroup拦截MOVE事件，并设置View的点击事件
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept move** 和 **View setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_MOVE, isIntercept = false
TestLinearLayout: intercept event = ACTION_MOVE
TestView: dispatchTouchEvent: ev.getAction() = ACTION_CANCEL
TestView: onTouchEvent: ev.getAction() = ACTION_CANCEL

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_MOVE
MainActivity: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_UP
MainActivity: onTouchEvent: ev.getAction() = ACTION_UP
```
可以看到 TestView 消耗了 DOWN 事件， 但是 MOVE 事件被 TestLinearLayout 拦截了，然后后续的 MOVE 和 UP 事件就只能传到 TestLinearLayout 了。
**而且在拦截到MOVE事件到的时候，会触发TestView 的 ACTION_CANCEL 事件**

满足上述第 1、2、3、4、5、6、7、8、11 点。

---

##### ViewGroup拦截Move事件，并设置View和ViewGroup的点击事件
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept move**、**ViewGroup setOnClick**  和 **View setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_MOVE, isIntercept = false
TestLinearLayout: intercept event = ACTION_MOVE
TestView: dispatchTouchEvent: ev.getAction() = ACTION_CANCEL
TestView: onTouchEvent: ev.getAction() = ACTION_CANCEL

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_UP
```
和 “ViewGroup拦截MOVE事件，并设置View的点击事件” 一致。因为 DOWN 事件 的先传个 TestView.

---

##### 仅ViewGroup拦截UP事件
我们依次点击示例中的 **Reset**  和 **ViewGroup Intercept up** 按钮，然后点击 **TestView** 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN
MainActivity: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
MainActivity: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
MainActivity: onTouchEvent: ev.getAction() = ACTION_UP
```
和 “默认” 一致，因为 TestLinearLayout 也没有消耗 DOWN 事件，所以就收不到 MOVE 和 UP 事件。

---

##### ViewGroup拦截UP事件，并设置ViewGroup的点击事件
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept up** 和 **ViewGroup setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_UP
```
这里因为 TestLinearLayout 消耗了 DOWN 事件， 因为事件已经是它在处理了，也没有调用 `onInterceptTouchEvent` 方法来拦截 UP 事件。

满足上述第 1、3、4、5、6、7、11 点。

---

##### ViewGroup拦截 UP 事件，并设置View的点击事件
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept up** 和 **View setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_MOVE, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestView: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_UP, isIntercept = false
TestLinearLayout: intercept event = ACTION_UP
TestView: dispatchTouchEvent: ev.getAction() = ACTION_CANCEL
TestView: onTouchEvent: ev.getAction() = ACTION_CANCEL
```
这里因为 TestView 消耗了 DOWN 事件，而 TestLinearLayout 只拦截 UP 事件，所以前面的 DOWN 和 MOVE 一次传递到 TestView，然后在 UP 事件的时候，被 TestLinearLayout 拦截，然后触发了 TestView 的 ACTION_CANCEL 事件。

满足上述第 1、2、3、4、5、6、7、8、11 点。
 
---

##### ViewGroup拦截 UP 事件，并设置View和 ViewGroup 的点击事件
我们依次点击示例中的 **Reset** 、**ViewGroup Intercept up** 、**View setOnClick** 和 **ViewGroup setOnClick** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN

多次
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_MOVE, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_MOVE
TestView: onTouchEvent: ev.getAction() = ACTION_MOVE

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_UP, isIntercept = false
TestLinearLayout: intercept event = ACTION_UP
TestView: dispatchTouchEvent: ev.getAction() = ACTION_CANCEL
TestView: onTouchEvent: ev.getAction() = ACTION_CANCEL
```
同 “ViewGroup拦截 UP 事件，并设置View的点击事件” 一致

----

##### 仅设置 View 为可点击的 
我们依次点击示例中的 **Reset** 和 **set view Clickable true** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_UP, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestView: onTouchEvent: ev.getAction() = ACTION_UP
```
同 “仅设置View的点击事件” 一致，主要验证第 8 点。

---

##### 仅设置 View 的 enable 为 false
我们依次点击示例中的 **Reset** 、和 **set view enable false** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onTouchEvent: ev.getAction() = ACTION_DOWN
MainActivity: onTouchEvent: ev.getAction() = ACTION_DOWN

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
MainActivity: onTouchEvent: ev.getAction() = ACTION_UP
```
同 “默认情况” 一致，主要验证第 9 点。

---

##### 设置 View 的 enable 为 false 并且可点击 
我们依次点击示例中的 **Reset** 、**set view Clickable true** 和 **set view enable false** 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
```
MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_DOWN, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_DOWN
TestView: onTouchEvent: ev.getAction() = ACTION_DOWN

MainActivity: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestLinearLayout: onInterceptTouchEvent: ev.getAction() = ACTION_UP, isIntercept = false
TestView: dispatchTouchEvent: ev.getAction() = ACTION_UP
TestView: onTouchEvent: ev.getAction() = ACTION_UP
```
同 “仅设置View的点击事件” 一致，主要验证第 8、9 点。

---

###  对日志信息的小结
这里先简单的小结下，通过上面的日志我们可以看到：
* 默认情况下事件的传递顺序是 `Activity.dispatchTouchEvent` → `ViewGroup.dispatchTouchEvent` → `ViewGroup.onInterceptTouchEvent` → `View.dispatchTouchEvent` → `View.onTouchEvent` → `ViewGroup.onTouchEvent` → `Activity.onTouchEvent`， 然后后续的的事件都只走 `Activity.dispatchTouchEvent` → `Activity.onTouchEvent`了，不会再继续放下传了。
就好像老板接到一个任务，然后交给你的上级，你的上级然后交给你，你想了很久发现解决不了，就告诉上级解决不了，然后你的上级又想了很久，发现也解决不了，就反馈给老板，然后老板就只能自己处理这个任务了，后续类似的任务也不会给你的上级，从而你也就不会有类似的任务了。

* 只要中间的 ViewGroup 和 View 谁处理了 down 事件， 后续的事件除了上层拦截之外都会传到处理了 down 事件的位置上。
还是上面的例子，如果完成那个任务，那么后续类似的任务老板都会接下来交给你的上级，你的上级也能处理的话，他也会先交给你，如果你能处理，就让你处理，如果你处理不了，后续类似的任务也不会分配给你了，他就自己处理了。

* 如果  View 谁处理了 down 事件，但是后续的事件被 ViewGroup 拦截了，则后续的事件都会交给 ViewGroup 去处理。
还是上面的例子，老板接到一个任务，然后交给你的上级，你的上级然后交给你，然后你完成了第一部分，然后你要开始休长假了，所以后续的任务你的上级就会自己处理，不会在你休假期间打扰你了。

* 然后 View 的 enable 状态并不会影响其接收事件。
就像你的上级叫你去开会，但是你在开另一个会，并不会因为你在开另外一个会而不通知你。

---

如果有描述错误的，请提醒我，感谢！

以上

如果觉得不错的话，请帮忙点个赞呗。

---

扫描下面的二维码，关注我的公众号 **Android1024**， 点关注，不迷路。
![Android1024](https://upload-images.jianshu.io/upload_images/1709375-84aaffe67e21a7e9.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

`
