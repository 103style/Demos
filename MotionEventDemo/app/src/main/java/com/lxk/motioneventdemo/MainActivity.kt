package com.lxk.motioneventdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View

import androidx.appcompat.app.AppCompatActivity

import com.lxk.motioneventdemo.test.TestActivity

/**
 * @author https://github.com/103style
 * @date 2020/1/12 16:03
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG: String = "MainActivity"

    private var testLinearLayout: TestLinearLayout? = null
    private var testView: TestView? = null

    private val onClickListener: View.OnClickListener = View.OnClickListener {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testLinearLayout = findViewById(R.id.tll)
        testView = findViewById(R.id.tv)

        findViewById<View>(R.id.bt_reset).setOnClickListener(this)
        findViewById<View>(R.id.bt_view_down).setOnClickListener(this)
        findViewById<View>(R.id.bt_group_down).setOnClickListener(this)
        findViewById<View>(R.id.bt_intercept_all).setOnClickListener(this)
        findViewById<View>(R.id.bt_intercept_down).setOnClickListener(this)
        findViewById<View>(R.id.bt_intercept_move).setOnClickListener(this)
        findViewById<View>(R.id.bt_intercept_up).setOnClickListener(this)
        findViewById<View>(R.id.bt_view_clickable).setOnClickListener(this)
        findViewById<View>(R.id.bt_view_enable).setOnClickListener(this)
        findViewById<View>(R.id.bt_test).setOnClickListener(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.e(TAG, "dispatchTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(ev.action))
        val res = super.dispatchTouchEvent(ev)
        Log.i(TAG, "dispatchTouchEvent: return  " + res)
        return res
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e(TAG, "onTouchEvent: ev.getAction() = " + EventHandler.handlerEvent(event.action))
        val res = super.onTouchEvent(event)
        Log.i(TAG, "onTouchEvent: return  " + res)
        return res
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_reset -> {
                TestLinearLayout.setIsIntercept(false)
                TestLinearLayout.setInterceptEvent(-1024)
                testLinearLayout?.setOnClickListener(null)
                testView?.setOnClickListener(null)
                testLinearLayout?.isClickable = false
                testView?.isClickable = false
            }
            R.id.bt_view_down -> {
                testView?.isClickable = true
                testView?.setOnClickListener(onClickListener)
            }
            R.id.bt_group_down -> {
                testLinearLayout?.isClickable = true
                testLinearLayout?.setOnClickListener(onClickListener)
            }
            R.id.bt_intercept_all -> {
                testLinearLayout?.isClickable = false
                TestLinearLayout.setIsIntercept(true)
            }
            R.id.bt_intercept_down -> {
                testLinearLayout?.isClickable = false
                TestLinearLayout.setInterceptEvent(MotionEvent.ACTION_DOWN)
            }
            R.id.bt_intercept_move -> {
                testLinearLayout?.isClickable = false
                TestLinearLayout.setInterceptEvent(MotionEvent.ACTION_MOVE)
            }
            R.id.bt_intercept_up -> {
                testLinearLayout?.isClickable = false
                TestLinearLayout.setInterceptEvent(MotionEvent.ACTION_UP)
            }
            R.id.bt_view_clickable -> testView?.isClickable = true

            R.id.bt_view_enable -> testView?.isEnabled = false
            R.id.bt_test -> startActivity(Intent(this, TestActivity::class.java))
            else -> {
            }
        }
    }
}
