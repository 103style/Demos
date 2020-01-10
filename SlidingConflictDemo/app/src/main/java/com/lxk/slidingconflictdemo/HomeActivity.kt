package com.lxk.slidingconflictdemo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.lxk.slidingconflictdemo.HorizontalScrollerView.OnChangeListener

/**
 * @author https://github.com/103style
 * @date 2019/12/11 22:15
 * 主页
 */
class HomeActivity : AppCompatActivity(), View.OnClickListener {

    /**
     * 底部三个按钮
     */
    private var tab1: AppCompatButton? = null
    private var tab2: AppCompatButton? = null
    private var tab3: AppCompatButton? = null
    /**
     * 水平滑动的 HorizontalScrollerView
     */
    private var horizontalScrollerView: HorizontalScrollerView? = null
    /**
     * 三个子 VerticalScrollerView
     */
    private var rsv1: VerticalScrollerView? = null
    private var rsv2: VerticalScrollerView? = null
    private var rsv3: VerticalScrollerView? = null

    /**
     * 测试数据的起始值和个数
     */
    private var start = 0
    private var count = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        initSetup()
    }

    private fun initView() {
        horizontalScrollerView = findViewById(R.id.tvp_test)

        rsv1 = findViewById(R.id.rsv1)
        rsv2 = findViewById(R.id.rsv2)
        rsv3 = findViewById(R.id.rsv3)

        tab1 = findViewById(R.id.tab_1)
        tab1!!.isSelected = true
        tab2 = findViewById(R.id.tab_2)
        tab3 = findViewById(R.id.tab_3)

        tab1!!.setOnClickListener(this)
        tab2!!.setOnClickListener(this)
        tab3!!.setOnClickListener(this)
    }

    private fun initSetup() {
        setupRsv(rsv1)
        setupRsv(rsv2)
        setupRsv(rsv3)
        horizontalScrollerView!!.setOnChangeListener(
                object : OnChangeListener {
                    override fun indexChange(index: Int) {
                        changeTabStatue(index)
                    }
                })
    }


    /**
     * 给 VerticalScrollerView 添加子View
     */
    private fun setupRsv(verticalScrollerView: VerticalScrollerView?) {

        addItemHorizontalScrollerView(verticalScrollerView)

        val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = 32
        for (i in start until count - 1) {
            val button = AppCompatButton(this)
            button.layoutParams = layoutParams
            button.text = i.toString()
            verticalScrollerView!!.addView(button)
        }
        updateData()
    }

    /**
     * 添加可以水平滑动的子View  ItemHorizontalScrollerView
     */
    private fun addItemHorizontalScrollerView(verticalScrollerView: VerticalScrollerView?) {
        val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val itemHorizontalScrollerView = ItemHorizontalScrollerView(this)
        itemHorizontalScrollerView.layoutParams = layoutParams
        val itemCount = 10
        val itemLP = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        for (i in 0 until itemCount) {
            val button = AppCompatButton(this)
            button.layoutParams = itemLP
            button.text = i.toString()
            button.isClickable = false
            button.isLongClickable = false
            itemHorizontalScrollerView.addView(button)
        }
        verticalScrollerView!!.addView(itemHorizontalScrollerView)
    }

    /**
     * 更新数据
     */
    private fun updateData() {
        start += 50
        count += 50
    }

    private fun changeTabStatue(position: Int) {
        tab1!!.isSelected = false
        tab2!!.isSelected = false
        tab3!!.isSelected = false
        when (position) {
            1 -> tab2!!.isSelected = true
            2 -> tab3!!.isSelected = true
            else -> tab1!!.isSelected = true
        }
    }

    override fun onClick(v: View) {
        val pos: Int
        when (v.id) {
            R.id.tab_2 -> pos = 1
            R.id.tab_3 -> pos = 2
            R.id.tab_1 -> pos = 0
            else -> pos = 0
        }
        horizontalScrollerView!!.updateChildIndex(pos)
    }

}
