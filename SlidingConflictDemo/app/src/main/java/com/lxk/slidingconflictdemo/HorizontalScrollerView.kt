package com.lxk.slidingconflictdemo

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewGroup
import android.widget.Scroller

/**
 * @author https://github.com/103style
 * @date 2019/12/23 22:20
 */
class HorizontalScrollerView @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    /**
     * 平滑滑动用
     */
    private var scroller: Scroller = Scroller(context)
    /**
     * 计算滑动速度用
     */
    private var velocityTracker: VelocityTracker = VelocityTracker.obtain()
    /**
     * 记录上一次触摸时间的位置
     */
    private var x1: Float = 0.toFloat()
    private var y1: Float = 0.toFloat()
    private var lastX: Float = 0.toFloat()
    private var lastY: Float = 0.toFloat()
    private val lastInterceptX: Float = 0.toFloat()
    private val lastInterceptY: Float = 0.toFloat()
    /**
     * 子控件的宽高和个数
     */
    private var mChildWidth: Int = 0
    private var mChildHeight: Int = 0
    private var mChildSize: Int = 0

    /**
     * 当前的子View索引变化
     */
    private var onChangeListener: OnChangeListener? = null

    /**
     * 当前的子View索引
     */
    private var childIndex = 0


    /**
     * 设置子View索引变化回调
     */
    fun setOnChangeListener(onChangeListener: OnChangeListener) {
        this.onChangeListener = onChangeListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        //默认都一样大 占满父布局
        mChildSize = childCount
        for (i in 0 until mChildSize) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
        }
        mChildWidth = widthSize
        mChildHeight = heightSize

        //设置测量宽高
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            //从左到右依次布局
            view.layout(left, 0, left + mChildWidth, mChildHeight)
            left += mChildWidth
        }
    }

    //    //外部拦截法
    //    @Override
    //    public boolean onInterceptTouchEvent(MotionEvent ev) {
    //        boolean intercept;
    //        float x = ev.getX();
    //        float y = ev.getY();
    //        switch (ev.getAction()) {
    //            case MotionEvent.ACTION_DOWN:
    //                intercept = false;
    //                if (!scroller.isFinished()) {
    //                    scroller.abortAnimation();
    //                    intercept = true;
    //                }
    //                break;
    //            case MotionEvent.ACTION_MOVE:
    //                float dx = x - lastInterceptX;
    //                float dy = y - lastInterceptY;
    //                //水平滑动距离大于竖直滑动
    //                intercept = Math.abs(dx) > Math.abs(dy) + 50;
    //                break;
    //            case MotionEvent.ACTION_UP:
    //            default:
    //                intercept = false;
    //                break;
    //        }
    //        lastX = x;
    //        lastY = y;
    //
    //        lastInterceptX = x;
    //        lastInterceptY = y;
    //
    ////        return false;
    //        return intercept;
    //    }


    // 内部拦截法
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                    return true
                }
                return false
            }
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> return true
            else -> return true
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        x1 = ev.x
        y1 = ev.y
        val res = super.dispatchTouchEvent(ev)
        lastX = x1
        lastY = y1
        return res
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        velocityTracker.addMovement(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (!scroller.isFinished) {
                scroller.abortAnimation()
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = (x1 - lastX).toInt()
                //跟随手指滑动
                scrollBy(-dx, 0)
            }
            MotionEvent.ACTION_UP -> {
                val scrollX = scrollX
                //计算1s内的速度
                velocityTracker.computeCurrentVelocity(1000)
                //获取水平的滑动速度
                val xVelocity = velocityTracker.xVelocity

                if (Math.abs(xVelocity) > 100) {
                    childIndex = if (xVelocity > 0) childIndex - 1 else childIndex + 1
                } else {
                    childIndex = (scrollX + mChildWidth / 2) / mChildWidth
                }
                childIndex = Math.max(0, Math.min(childIndex, mChildSize - 1))

                if (onChangeListener != null) {
                    onChangeListener!!.indexChange(childIndex)
                }
                //计算还需滑动到整个child的偏移
                val sx = childIndex * mChildWidth - scrollX
                //通过Scroller来平滑滑动
                smoothScrollBy(sx)

                //清除
                velocityTracker.clear()
            }
            else -> {
            }
        }
        return true
    }

    /**
     * 平滑滑动
     *
     * @param dx
     */
    private fun smoothScrollBy(dx: Int) {
        scroller.startScroll(scrollX, 0, dx, 0, 500)
        invalidate()
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }

    /**
     * 更新当前可见子View的索引
     */
    fun updateChildIndex(pos: Int) {
        if (!scroller.isFinished) {
            scroller.abortAnimation()
        }
        smoothScrollBy(pos * mChildWidth - scrollX)
        if (onChangeListener != null) {
            onChangeListener!!.indexChange(pos)
        }
    }

    interface OnChangeListener {
        /**
         * 索引变化
         *
         * @param index 索引
         */
        fun indexChange(index: Int)
    }

}
