package com.lxk.annotationdemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author https://github.com/103style
 * @date 2019/11/29 13:57
 */
public class TestAdapter extends BaseRecyclerAdapter<String, TestAdapter.ViewHolder> {

    public TestAdapter(Context context) {
        super(context);
    }

    @Override
    ViewHolder onCreateView(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(R.layout.activity_main, parent));
    }

    @Override
    void onBindView(ViewHolder holder, int index) {
        holder.bindData(index, getItem(index));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindData(int index, String item) {

        }
    }

}