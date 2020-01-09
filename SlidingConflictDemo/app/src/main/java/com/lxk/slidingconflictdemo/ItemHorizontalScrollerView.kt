package com.lxk.slidingconflictdemo

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Scroller

/**
 * @author https://github.com/103style
 * @date 2019/12/29 20:15
 *
 *
 * 竖直滑动的View里面添加的水平滑动的View
 */
class ItemHorizontalScrollerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    /**
     * 平滑滑动用
     */
    private var scroller: Scroller = Scroller(context)
    /**
     * 最后一次事件点击的位置
     */
    private var lastX: Float = 0.toFloat()
    private val lastY: Float = 0.toFloat()
    /**
     * 布局的宽度 和  内容的宽度
     */
    private var mWidth: Int = 0
    private var mContentWidth: Int = 0

    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)

    override fun generateLayoutParams(attrs: AttributeSet) = MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() = MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        mWidth = widthSize
        mContentWidth = 0
        var contentMaxHeight = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)

            val layoutParams = child.layoutParams as MarginLayoutParams
            val cWidth = layoutParams.marginEnd + layoutParams.marginStart + child.measuredWidth
            val cHeight = layoutParams.topMargin + layoutParams.bottomMargin + child.measuredHeight
            contentMaxHeight = Math.max(cHeight, contentMaxHeight)
            mContentWidth += cWidth
        }
        setMeasuredDimension(if (widthMode == MeasureSpec.EXACTLY) widthSize else mContentWidth,
                if (heightMode == MeasureSpec.EXACTLY) heightSize else contentMaxHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            val layoutParams = view.layoutParams as MarginLayoutParams
            //从上到下依次布局
            view.layout(left + layoutParams.leftMargin, layoutParams.topMargin,
                    left + layoutParams.leftMargin + view.measuredWidth,
                    layoutParams.topMargin + view.measuredHeight)
            left += view.width + layoutParams.leftMargin + layoutParams.rightMargin
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val scrollX = scrollX
        var used = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }
                used = true
            }
            MotionEvent.ACTION_MOVE -> {
                var dx = (x - lastX).toInt()
                if (scrollX <= 0 && dx > 0) {
                    //在最左边并且左滑时
                    if (scrollX == 0) {
                        dx = 0
                    } else {
                        dx += scrollX
                    }
                } else if (scrollX + mWidth >= mContentWidth && dx < 0) {
                    //在最右边并且右滑时
                    if (scrollX + mWidth >= mContentWidth) {
                        dx = 0
                    } else {
                        dx += scrollX + mWidth - mContentWidth
                    }
                } else {
                    used = true
                }

                //跟随手指滑动
                scrollBy(-dx, 0)

                //在不需要在左滑和右滑的时候 事件交给父控件处理
                if (dx == 0 && !used) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP -> if (scrollX < 0) {
                smoothScrollBy(-scrollX)
            } else if (mContentWidth <= mWidth) {
                smoothScrollBy(-scrollX)
            } else if (mContentWidth - scrollX < mWidth) {
                smoothScrollBy(mContentWidth - scrollX - mWidth)
            } else {
                //惯性滑动效果
            }
            else -> {
            }
        }
        lastX = x
        return used
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
}
