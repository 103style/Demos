package com.lxk.viewdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button btAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAnim = findViewById(R.id.bt_anim);

//        final TestScroller scroller = findViewById(R.id.tv);
//        scroller.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scroller.smoothScrollTo(200, 200);
////                scroller.computeScroll();
//            }
//        });


        btAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btAnim.scrollTo(-100, -100);
//                changeLayoutParams();
//                show();
//                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translation);
//                btAnim.startAnimation(animation);
            }
        });
//
//        btAnim.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                show();
//                ObjectAnimator objectAnimator = ObjectAnimator
//                        .ofFloat(btAnim, "translationX", 0, 400)
//                        .setDuration(2000);
////                objectAnimator.addListener(new AnimatorListenerAdapter() {
////                    @Override
////                    public void onAnimationEnd(Animator animation) {
////                        super.onAnimationEnd(animation);
////
////                    }
////                });
//                objectAnimator.start();
//                return true;
//            }
//        });

    }


    private void changeLayoutParams() {
        LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) btAnim.getLayoutParams();
        llp.width += 100;
        llp.height += 100;
        llp.setMarginStart(llp.getMarginStart() + 50);
        llp.topMargin += 50;
//        btAnim.setLayoutParams(llp);
        btAnim.requestLayout();
    }


    private void show() {
        Toast.makeText(this, "start anim", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "btAnim.getLeft() = " + btAnim.getLeft());
        Log.e(TAG, "btAnim.getTop() = " + btAnim.getTop());
        Log.e(TAG, "btAnim.getRight() = " + btAnim.getRight());
        Log.e(TAG, "btAnim.getBottom() = " + btAnim.getBottom());
        Log.e(TAG, "btAnim.getWidth() = " + btAnim.getWidth());
        Log.e(TAG, "btAnim.getHeight() = " + btAnim.getHeight());
        Log.e(TAG, "btAnim.getX() = " + btAnim.getX());
        Log.e(TAG, "btAnim.getY() = " + btAnim.getY());
        Log.e(TAG, "btAnim.getTranslationX() = " + btAnim.getTranslationX());
        Log.e(TAG, "btAnim.getTranslationY() = " + btAnim.getTranslationY());
    }


}
