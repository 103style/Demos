### 示例验证
[Demo源码github地址](https://github.com/103style/Demos/tree/master/MotionEventDemo)

我们分别创建监听上面三个方法的 ViewGroup 和 View， 然后在测试Activity中重写上面的方法，如下：
[TestLinearLayout.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/TestLinearLayout.java)、[TestView.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/TestView.java)、[EventHandler.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/EventHandler.java)、[MainActivity.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/MainActivity.java)、[activity_main.xml](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/res/layout/activity_main.xml)

---

我们通过以下测试方法来验证：
> * 默认什么都不做
> * View 设置点击事件
> * ViewGroup 设置点击事件
> * View 和 ViewGroup 都设置点击事件
> * ViewGroup 拦截全部事件，即 `onInterceptTouchEvent` 返回 `true`
> * ViewGroup 拦截全部事件，并设置 ViewGroup 的点击事件
> * ViewGroup 拦截全部事件，并设置 View 的点击事件
> * ViewGroup 拦截全部事件，并设置 View 和 ViewGroup 的点击事件
> * ViewGroup 拦截 DOWN 事件
> * ViewGroup 拦截 DOWN 事件，并设置 ViewGroup 的点击事件
> * ViewGroup 拦截 DOWN 事件，并设置 View 的点击事件
> * ViewGroup 拦截 DOWN 事件，并设置 View 和 ViewGroup 的点击事件
> * ViewGroup 拦截 MOVE 事件
> * ViewGroup 拦截 MOVE 事件，并设置 ViewGroup 的点击事件
> * ViewGroup 拦截 MOVE 事件，并设置 View 的点击事件
> * ViewGroup 拦截 MOVE 事件，并设置 View 和 ViewGroup 的点击事件
> * ViewGroup 拦截 UP 事件
> * ViewGroup 拦截 UP 事件，并设置 ViewGroup 的点击事件
> * ViewGroup 拦截 UP 事件，并设置 View 的点击事件
> * ViewGroup 拦截 UP 事件，并设置 View 和 ViewGroup 的点击事件



---

##### 默认什么都不做的事件记录
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

从以上日志我们可以看到 `DOWN` 事件的传递路径为从 `MainActivity.dispatchTouchEvent` → `TestLinearLayout.dispatchTouchEvent` → `TestLinearLayout.onInterceptTouchEvent` → `TestView.dispatchTouchEvent` → `TestView.onTouchEvent` → `TestLinearLayout.onTouchEvent` → `MainActivity.onTouchEvent`, 而 MOVE 和  UP 事件就只发生在 `MainActivity`.

---

##### 仅View设置点击事件
我们点击示例中的 `View setOnClick`按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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
`tips`: 多次表示 下面那些事件回循环出现多次

我们可以看到 `DOWN`、`MOVE`、`UP` 每个事件都是从 `MainActivity` → `TestLinearLayout` → `TestView` .

---

##### 仅ViewGroup设置点击事件
我们依次点击示例中的 `Reset` 和 `ViewGroup setOnClick`按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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
我们可以看到 `DOWN` 事件是从 `MainActivity` → `TestLinearLayout` → `TestView` → `TestLinearLayout`，而`MOVE`、`UP`事件就只从 `MainActivity` → `TestLinearLayout`.

---

##### View和ViewGroup都设置点击事件
我们依次点击示例中的 `Reset` 、`View setOnClick` 和 `ViewGroup setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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
---

##### ViewGroup设置拦截全部事件
我们依次点击示例中的 `Reset` 和 `ViewGroup Intercept All`按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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
我们可以看到 `DOWN` 事件传递路径为 `MainActivity` → `TestLinearLayout`，而`MOVE`、`UP`事件就只在 `MainActivity`.

---

##### ViewGroup设置拦截全部事件，并设置ViewGroup的点击事件.
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept All` 和 `ViewGroup setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### ViewGroup设置拦截全部事件，并设置View的点击事件.
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept All` 和 `View setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### ViewGroup设置拦截全部事件，并设置View和ViewGroup的点击事件.
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept All`、 `View setOnClick` 和 `ViewGroup setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### 仅ViewGroup拦截DOWN事件
我们依次点击示例中的 `Reset` 和 `ViewGroup Intercept down`按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### 仅ViewGroup拦截DOWN事件，并设置ViewGroup的点击事件
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept down` 和 `ViewGroup setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### 仅ViewGroup拦截DOWN事件，并设置View的点击事件
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept down` 和 `View setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### 仅ViewGroup拦截DOWN事件，并设置View和ViewGroup的点击事件
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept down`、View setOnClick`  和 `ViewGroup setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### 仅ViewGroup拦截MOVE事件
我们依次点击示例中的 `Reset` 和 `ViewGroup Intercept move`按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### 仅ViewGroup拦截MOVE事件，并设置ViewGroup的点击事件
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept move` 和 `ViewGroup setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### 仅ViewGroup拦截MOVE事件，并设置View的点击事件
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept move` 和 `View setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### 仅ViewGroup拦截Move事件，并设置View和ViewGroup的点击事件
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept move`、`ViewGroup setOnClick`  和 `View setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### 仅ViewGroup拦截UP事件
我们依次点击示例中的 `Reset`  和 `ViewGroup Intercept up`按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### 仅ViewGroup拦截UP事件，并设置ViewGroup的点击事件
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept up` 和 `ViewGroup setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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


---

##### 仅ViewGroup拦截 UP 事件，并设置View的点击事件
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept up` 和 `View setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

---

##### 仅ViewGroup拦截 UP 事件，并设置View和 ViewGroup 的点击事件
我们依次点击示例中的 `Reset` 、`ViewGroup Intercept up` 、`View setOnClick` 和 `ViewGroup setOnClick` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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

