package com.lxk.slidingconflictdemo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 22:15
 * 主页
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 底部三个按钮
     */
    private AppCompatButton tab1, tab2, tab3;
    /**
     * 水平滑动的 HorizontalScrollerView
     */
    private HorizontalScrollerView horizontalScrollerView;
    /**
     * 三个子 VerticalScrollerView
     */
    private VerticalScrollerView rsv1, rsv2, rsv3;

    /**
     * 测试数据的起始值和个数
     */
    private int start = 0, count = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initSetup();
    }

    private void initView() {
        horizontalScrollerView = findViewById(R.id.tvp_test);

        rsv1 = findViewById(R.id.rsv1);
        rsv2 = findViewById(R.id.rsv2);
        rsv3 = findViewById(R.id.rsv3);

        tab1 = findViewById(R.id.tab_1);
        tab1.setSelected(true);
        tab2 = findViewById(R.id.tab_2);
        tab3 = findViewById(R.id.tab_3);

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
    }

    private void initSetup() {
        setupRsv(rsv1);
        setupRsv(rsv2);
        setupRsv(rsv3);
        horizontalScrollerView.setOnChangeListener(this::changeTabStatue);
    }

    /**
     * 给 VerticalScrollerView 添加子View
     */
    private void setupRsv(VerticalScrollerView verticalScrollerView) {

        addItemHorizontalScrollerView(verticalScrollerView);

        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = 32;
        for (int i = start; i < count - 1; i++) {
            AppCompatButton button = new AppCompatButton(this);
            button.setLayoutParams(layoutParams);
            button.setText(String.valueOf(i));
            verticalScrollerView.addView(button);
        }
        updateData();
    }

    /**
     * 添加可以水平滑动的子View  ItemHorizontalScrollerView
     */
    private void addItemHorizontalScrollerView(VerticalScrollerView verticalScrollerView) {
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ItemHorizontalScrollerView itemHorizontalScrollerView = new ItemHorizontalScrollerView(this);
        itemHorizontalScrollerView.setLayoutParams(layoutParams);
        int itemCount = 10;
        ViewGroup.MarginLayoutParams itemLP = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < itemCount; i++) {
            AppCompatButton button = new AppCompatButton(this);
            button.setLayoutParams(itemLP);
            button.setText(String.valueOf(i));
            button.setClickable(false);
            button.setLongClickable(false);
            itemHorizontalScrollerView.addView(button);
        }
        verticalScrollerView.addView(itemHorizontalScrollerView);
    }

    /**
     * 更新数据
     */
    private void updateData() {
        start += 50;
        count += 50;
    }

    private void changeTabStatue(int position) {
        tab1.setSelected(false);
        tab2.setSelected(false);
        tab3.setSelected(false);
        switch (position) {
            case 1:
                tab2.setSelected(true);
                break;
            case 2:
                tab3.setSelected(true);
                break;
            default:
                tab1.setSelected(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int pos;
        switch (v.getId()) {
            case R.id.tab_2:
                pos = 1;
                break;
            case R.id.tab_3:
                pos = 2;
                break;
            case R.id.tab_1:
            default:
                pos = 0;
                break;
        }
        horizontalScrollerView.updateChildIndex(pos);
    }

}
