package com.lxk.slidingconflictdemo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
     * 水平滑动的测试viewpager
     */
    private TestViewPager testViewPager;
    /**
     * 三个子RecyclerView
     */
    private RecyclerView rv1, rv2, rv3;

    /**
     * 测试数据的起始值和个数
     */
    private int start = 0, count = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initSetup();
    }

    private void initView() {
        testViewPager = findViewById(R.id.tvp_test);

        rv1 = findViewById(R.id.rv1);
        rv2 = findViewById(R.id.rv2);
        rv3 = findViewById(R.id.rv3);

        tab1 = findViewById(R.id.tab_1);
        tab1.setSelected(true);
        tab2 = findViewById(R.id.tab_2);
        tab3 = findViewById(R.id.tab_3);

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
    }

    private void initSetup() {
        setupRv(rv1);
        setupRv(rv2);
        setupRv(rv3);
        testViewPager.setOnChangeListener(
                new TestViewPager.OnChangeListener() {
                    @Override
                    public void indexChange(int index) {
                        changeTabStatue(index);
                    }
                });
    }

    private void setupRv(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i = start; i < count; i++) {
            list.add(String.valueOf(i));
        }
        recyclerView.setAdapter(new RecyclerViewAdapter(this, list));
        updateData();
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
        testViewPager.updateChildIndex(pos);
    }

}
