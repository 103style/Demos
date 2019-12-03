### 三个主要相关方法以及一些结论的介绍
点击事件的分发主要由以下这三个很重要的方法来完成的：
* `public boolean dispatchTouchEvent(MotionEvent ev)`
    用来进行事件的分发。**如果事件传到当前View, 这个方法一定被调用**，返回结果受当前view的 onTouchEvent 和 下级view的 dispatchTouchEvent 影响，表示是否消耗事件。

* `public boolean onInterceptTouchEvent(MotionEvent ev)`
    ViewGroup才有的方法，用于判断是否拦截某个事件。 如果当前方法拦截了某个事件，同一个事件序列中，此方法不会再回调。 返回结果表示是否拦截当前事件。
   
* `public boolean onTouchEvent(MotionEvent event)`
    在 dispatchTouchEvent 中调用，用来处理点击事件，返回结果表示是否消耗当前事件，如果不消耗，则在同一事件序列中，当前View无法再次接收到事件。

这三个方法之间关系的伪代码如下：
```
public boolean dispatchTouchEvent(MotionEvent ev) {
    boolean consume = false;
    if (onInterceptTouchEvent(ev)) {
        consume = onTouchEvent(ev);
    } else {
        consume = child.dispatchTouchEvent(ev);
    }
    return consume;
}
```

这里我们先列出一些结论，然后下一步我们来验证它们：
> 1、同一个事件序列是指从手指接触屏幕那一刻起，到离开屏幕结束，是以 `down` 事件开始， 中间有数量不定的 `move` 事件，最终以 `up` 事件结束。
> 2、正常情况下，一个事件序列只能被一个 `View` 拦截且消耗。这条原因可参考第三点。
> 3、某个 `View` 一旦决定拦截，那么这个事件序列都只能由他来处理（前提是事件能传到它），并且它的 `onInterceptTouchEvent` 不会在被调用。这点也很好理解，就是说一个 `View` 决定拦截一个事件后，那么系统会把这个事件序列内的其他事件都交给他来处理，因此就不会再去调用 `onInterceptTouchEvent` 来询问是否拦截了。
> 4、某个`View` 一旦开始处理事件，如果他不消耗 `ACTION_DOWN` 事件，那么同一序列的其他事件都不会交给它处理，并且事件将重新交给它的父元素去处理，即父元素的 `onTouchEvent` 会被调用。就像一件重要的事你没处理好，短期内上级就不敢再把重要的事情交给你做了。
> 5、如果 `View` 不消耗除 `ACTION_DOWN` 以外的事件，那么这个点击事件就会消失，此时父元素的 `onTouchEvent` 不会被调用，并且当前 `View` 能收到后续的事件，最终这些事件都交给 `Activity` 处理。
> 6、`ViewGroup` 默认不拦截任何事件。`ViewGroup` 源码中 `onInterceptTouchEvent` 默认返回 `false`。
> 7、`View` 没有 `onInterceptTouchEvent` 方法，一旦事件传给他，它的 `onTouchEvent` 就会被调用。
> 8、`View` 的 `onTouchEvent` 默认都会消耗事件（返回 `true`），除非它是不可点击的(`clickable` 和 `longClickable` 都为 `false`)。`View` 的 `longClickable` 属性默认为 `false`；`clickable` 分情况，比如 `Button` 默认为 `true`,`TextView` 默认为 `false`。
> 9、`View` 的 `enable` 属性不影响 `onTouchEvent` 的默认返回值，哪怕 `View` 是 `disable` 状态的，只要`clickable` 和 `longClickable` 其中一个为 `true`, 那么 `onTouchEvent` 就返回 `true`.
> 10、`onClick` 会触发的前提是当前 `View` 是可以点击的，并且收到了 `down` 和 `up` 事件。 
> 11、事件的传递过程是 **由外向内** 的，即事件总是先传递给父元素，然后再由父元素分发给子 `View`，通过 `requestDisallowInterceptTouchEvent(boolean)` 方法可以干预父元素的分发过程，**但是 ACTION_DOWN 除外**。 

---

### 示例验证
[Demo源码github地址](https://github.com/103style/Demos/tree/master/MotionEventDemo)

我们分别创建监听上面三个方法的 `ViewGroup` 和 `View`，然后在测试 `Activity` 中重写上面的方法，源码链接如下：
[TestLinearLayout.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/TestLinearLayout.java)、[TestView.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/TestView.java)、[EventHandler.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/EventHandler.java)、[MainActivity.java](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/java/com/lxk/motioneventdemo/MainActivity.java)、[activity_main.xml](https://github.com/103style/Demos/blob/master/MotionEventDemo/app/src/main/res/layout/activity_main.xml)

`reset` 点击之后的处理逻辑。
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

从以上日志我们可以看到 `DOWN` 事件的传递路径为从 `MainActivity.dispatchTouchEvent` → `TestLinearLayout.dispatchTouchEvent` → `TestLinearLayout.onInterceptTouchEvent` → `TestView.dispatchTouchEvent` → `TestView.onTouchEvent` → `TestLinearLayout.onTouchEvent` → `MainActivity.onTouchEvent`, 而 MOVE 和  UP 事件就只发生在 `MainActivity`.
因为 View 默认 `clickable` 和 `longClickable` 都为 `false`，所以没有消耗 `DOWN` 事件。

满足上述第 1、2、4、6、7、8、11 点。

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
因为 `TestView` 消耗了 `DOWN` 事件，后面的每个事件都传递到了 `TestView`，我们可以看到 `DOWN`、`MOVE`、`UP` 每个事件都是从 `MainActivity` → `TestLinearLayout` → `TestView`，`TestView` 并没有处理`MOVE` 所以就 `消失` 了。

满足上述第 1、2、4、5、6、7、11 点。

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
因为 `TestView` 没有消耗 `DOWN` 事件，然后传给`TestLinearLayout`，`TestLinearLayout`消耗了 DOWN事件，所以后面的每个事件都只传递到了 `TestLinearLayout`。

满足上述第 1、2、4、5、6、7、8、11 点。

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
这里和 “仅View设置点击事件” 一致，因为 TestView 消耗了 DOWN 事件，TestLinearLayout 就无法再消耗 DOWN 事件了。

---

##### ViewGroup设置拦截全部事件 `(仅拦截，没消耗)`
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
我们可以看到 `DOWN` 事件传递路径为 `MainActivity` → `TestLinearLayout`，因为DOWN事件被拦截了，所以 `TestView` 收不到事件，而`MOVE`、`UP`事件就只在 `MainActivity`.

满足上述第 1、3、4、5、6、7、11 点。

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
这里我们可以看到 `DOWN` 事件传递路径为 `MainActivity` → `TestLinearLayout`，因为`TestLinearLayout`消耗了 `DOWN` 事件，所以`MOVE`、`UP`事件也传到了 `TestLinearLayout`.

满足上述第 1、2、3、4、5、6、7、8、11 点。

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
和 “ViewGroup仅设置拦截全部事件” 一致，`TestLinearLayout` 拦截了事件，所以 `TestView` 收不到事件，而`TestLinearLayout` 没有消耗 `DOWN` 事件，后续的事件也没有继续传递到他。


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
和 “ViewGroup设置拦截全部事件，并设置ViewGroup的点击事件” 一致，`TestLinearLayout` 拦截了事件，所以 `TestView` 收不到事件，而`TestLinearLayout` 消耗了 `DOWN` 事件，后续的事件也都继续传递到他。

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
和 “ViewGroup仅设置拦截全部事件” 一致，`TestLinearLayout` 拦截了事件，所以 `TestView` 收不到事件，而`TestLinearLayout` 没有消耗 `DOWN` 事件，后续的事件也没有继续传递到他。

---

##### ViewGroup拦截DOWN事件，并设置ViewGroup的点击事件
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
和 “ViewGroup设置拦截全部事件，并设置ViewGroup的点击事件” 一致，`TestLinearLayout` 拦截了事件，所以 `TestView` 收不到事件，而`TestLinearLayout` 消耗了 `DOWN` 事件，后续的事件也都继续传递到他。

---

##### ViewGroup拦截DOWN事件，并设置View的点击事件
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
和 “ViewGroup设置拦截全部事件” 一致，`TestLinearLayout` 拦截了事件，所以 `TestView` 收不到事件，而`TestLinearLayout` 没有消耗 `DOWN` 事件，后续的事件也没有继续传递到他。

---

##### ViewGroup拦截DOWN事件，并设置View和ViewGroup的点击事件
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
和 “ViewGroup拦截DOWN事件，并设置ViewGroup的点击事件” 一致，`TestLinearLayout` 拦截了事件，所以 `TestView` 收不到事件，而`TestLinearLayout` 消耗了 `DOWN` 事件，后续的事件也都继续传递到他。

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
和 “默认情况” 一致，因为 `TestLinearLayout` 和 `TestView` 都没消耗 `DOWN` 事件，所以后续事件都不能传递到它们。

---

##### ViewGroup拦截MOVE事件，并设置ViewGroup的点击事件
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
和 “设置ViewGroup的点击事件” 的点击事件一致，此时事件已经由 `TestLinearLayout` 处理，所以也没回调 `onInterceptTouchEvent` 方法。

---

##### ViewGroup拦截MOVE事件，并设置View的点击事件
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
可以看到 `TestView` 消耗了 `DOWN` 事件， 但是 `MOVE` 事件被 `TestLinearLayout` 拦截了，然后后续的 `MOVE` 和 `UP` 事件就只能传到 `TestLinearLayout` 了。
**而且在拦截到MOVE事件到的时候，会触发TestView 的 `ACTION_CANCEL` 事件**

满足上述第 1、2、3、4、5、6、7、8、11 点。

---

##### ViewGroup拦截Move事件，并设置View和ViewGroup的点击事件
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
和 “ViewGroup拦截MOVE事件，并设置View的点击事件” 一致。因为 `DOWN` 事件 的先传个 `TestView`.

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
和 “默认” 一致，因为 `TestLinearLayout` 也没有消耗 `DOWN` 事件，所以就收不到 `MOVE` 和 `UP` 事件。

---

##### ViewGroup拦截UP事件，并设置ViewGroup的点击事件
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
这里因为 `TestLinearLayout` 消耗了 `DOWN` 事件， 因为事件已经是它在处理了，也没有调用 `onInterceptTouchEvent` 方法来拦截 UP 事件。

满足上述第 1、3、4、5、6、7、11 点。

---

##### ViewGroup拦截 UP 事件，并设置View的点击事件
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
这里因为 `TestView` 消耗了 `DOWN` 事件，而 `TestLinearLayout` 只拦截 `UP` 事件，所以前面的 `DOWN` 和 `MOVE` 一次传递到 `TestView`，然后在 `UP` 事件的时候，被 `TestLinearLayout` 拦截，然后触发了 `TestView` 的 `ACTION_CANCEL` 事件。

满足上述第 1、2、3、4、5、6、7、8、11 点。
 
---

##### ViewGroup拦截 UP 事件，并设置View和 ViewGroup 的点击事件
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
同 “ViewGroup拦截 UP 事件，并设置View的点击事件” 一致

----

##### 仅设置 View 为可点击的 
我们依次点击示例中的 `Reset` 和 `set view Clickable true` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
`鼠标单击就没有move事件了`
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
我们依次点击示例中的 `Reset` 、和 `set view enable false` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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
我们依次点击示例中的 `Reset` 、`set view Clickable true` 和 `set view enable false` 按钮，然后点击 TestView 所在的区域，可以看到打印的日志如下：
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
