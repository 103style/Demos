package com.lxk.surfaceviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author https://github.com/103style
 * @date 2020/1/9 10:21
 */
public class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private final SurfaceHolder mHolder;
    private Thread thread;
    /**
     * 线程运行标识符
     */
    private volatile boolean threadRunning;
    private Canvas mCanvas;
    private Paint mPaint;
    private int circleRadius = 10;
    private int[] colors = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE};
    private int colorIndex = 0;

    public TestSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.FILL);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new Thread(this);
        threadRunning = true;
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        threadRunning = false;
        mHolder.removeCallback(this);
    }


    @Override
    public void run() {
        while (threadRunning) {
            try {
                synchronized (mHolder) {
                    Thread.sleep(100);
                    drawTest();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null) {
                    //发生异常时并提交改变。
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }

    private void drawTest() {
        //获取画布对象
        mCanvas = mHolder.lockCanvas();
        if (mCanvas == null) {
            return;
        }
        mPaint.setColor(colors[colorIndex % colors.length]);

        Bitmap pic = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
        mCanvas.drawBitmap(pic, (getWidth() - pic.getWidth()) / 2,
                getHeight() - pic.getHeight(), mPaint);

        int columnCount = 5;
        int maxRadius = getWidth() / columnCount / 2;
        int lineCount = getHeight() / maxRadius / 2;
        int gap = 5;
        if (circleRadius > maxRadius - colorIndex * gap) {
            colorIndex++;
            circleRadius = 0;
        } else {
            circleRadius += 5;
        }
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < lineCount; j++) {
                mCanvas.drawCircle(maxRadius * 2 * i + maxRadius,
                        maxRadius * 2 * j + maxRadius,
                        circleRadius, mPaint);
            }
        }
        // 完成画画，把画布显示在屏幕上
        mHolder.unlockCanvasAndPost(mCanvas);
        mCanvas = null;
    }
}
