package com.lxk.slidingconflictdemo.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lxk.slidingconflictdemo.R;
import com.lxk.slidingconflictdemo.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 22:15
 * 主页
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener,
        ItemFragment.OnListFragmentInteractionListener {

    private ViewPager viewPager;
    private AppCompatButton tab1, tab2, tab3;
    private List<ItemFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initSetup();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        tab1 = findViewById(R.id.tab_1);
        tab1.setSelected(true);
        tab2 = findViewById(R.id.tab_2);
        tab3 = findViewById(R.id.tab_3);

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
    }

    private void initSetup() {
        fragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            fragments.add(new ItemFragment());
        }

        viewPager.setAdapter(
                new FragmentPagerAdapter(getSupportFragmentManager(),
                        FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                    @NonNull
                    @Override
                    public Fragment getItem(int position) {
                        return fragments.get(position);
                    }

                    @Override
                    public int getCount() {
                        return fragments.size();
                    }
                });

        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        changeTabStatue(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
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
        viewPager.setCurrentItem(pos);
    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }


}
