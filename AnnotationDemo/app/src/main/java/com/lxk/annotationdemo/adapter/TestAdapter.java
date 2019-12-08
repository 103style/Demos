package com.lxk.annotationdemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lxk.annotationdemo.R;

import static com.lxk.annotationdemo.adapter.TestAdapter.TestViewHolder;

/**
 * @author https://github.com/103style
 * @date 2019/11/29 13:57
 */
public class TestAdapter extends BaseRecyclerAdapter<String, TestViewHolder> {

    public TestAdapter(Context context) {
        super(context);
    }

    @Override
    protected TestViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(inflate(R.layout.item_main, parent));
    }

    @Override
    protected void onBindData(@NonNull TestViewHolder testViewHolder, int position) {
        testViewHolder.tvShow.setText(getItem(position));
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShow;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShow = itemView.findViewById(R.id.tv_show);
        }
    }


}