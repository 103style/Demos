package com.lxk.slidingconflictdemo

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller

/**
 * @author https://github.com/103style
 * @date 2019/12/27 15:49
 *
 *
 * 可以竖直滑动的view
 */
class VerticalScrollerView @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    /**
     * 记录上一次触摸时间的位置
     */
    private var x1: Float = 0.toFloat()
    private var y1: Float = 0.toFloat()
    private var lastX: Float = 0.toFloat()
    private var lastY: Float = 0.toFloat()
    private var scroller: Scroller = Scroller(context)

    private var mHeight: Int = 0
    private var mContentHeight: Int = 0


    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)

    override fun generateLayoutParams(attrs: AttributeSet) = MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() = MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var contentHeight = 0
        var contentMaxWidth = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)

            val layoutParams = child.layoutParams as MarginLayoutParams
            val cWidth = layoutParams.marginEnd + layoutParams.marginStart + child.measuredWidth
            val cHeight = layoutParams.topMargin + layoutParams.bottomMargin + child.measuredHeight
            contentMaxWidth = Math.max(cWidth, contentMaxWidth)
            contentHeight += cHeight
        }
        //设置测量宽高
        mHeight = heightSize
        mContentHeight = contentHeight
        setMeasuredDimension(if (widthMode == View.MeasureSpec.EXACTLY) widthSize else contentMaxWidth,
                if (heightMode == View.MeasureSpec.EXACTLY) heightSize else contentHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var top = 0
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            val layoutParams = view.layoutParams as MarginLayoutParams
            //从上到下依次布局
            view.layout(layoutParams.leftMargin, layoutParams.topMargin + top,
                    layoutParams.leftMargin + view.measuredWidth,
                    layoutParams.topMargin + top + view.measuredHeight)
            top += view.measuredHeight + layoutParams.bottomMargin + layoutParams.topMargin
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        x1 = ev.x
        y1 = ev.y
        if (ev.action == MotionEvent.ACTION_DOWN) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        val res = super.dispatchTouchEvent(ev)
        lastX = x1
        lastY = y1
        return res
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (!scroller.isFinished) {
                scroller.abortAnimation()
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = (x1 - lastX).toInt()
                val dy = (y1 - lastY).toInt()
                //跟随手指滑动
                scrollBy(0, -dy)
                //在水平滑动距离 大于 竖直滑动时 允许 父View拦截
                if (Math.abs(dx) > Math.abs(dy) + 50) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP -> {
                val scrollY = scrollY
                if (scrollY < 0) {
                    smoothScrollBy(-scrollY)
                } else if (mContentHeight <= mHeight) {
                    smoothScrollBy(-scrollY)
                } else if (mContentHeight - scrollY < mHeight) {
                    smoothScrollBy(mContentHeight - scrollY - mHeight)
                } else {
                    //惯性滑动效果
                }
            }
            else -> {
            }
        }
        return true
    }

    /**
     * 平滑滑动
     *
     * @param dy
     */
    private fun smoothScrollBy(dy: Int) {
        scroller.startScroll(0, scrollY, 0, dy, 500)
        invalidate()
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }


}
