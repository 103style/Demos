package com.lxk.slidingconflictdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 22:35
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final int TYPE_HEADER = 1;
    private final int TYPE_ITEM = 2;
    private final List<String> mValues;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, List<String> items) {
        mValues = items;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_item_list, parent, false);
            return new HeadHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_show, parent, false);
            return new ItemHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 5 == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder instanceof HeadHolder) {
            HeadHolder headHolder = (HeadHolder) holder;
            headHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            headHolder.recyclerView.setAdapter(new MyItemRecyclerViewAdapter(DummyContent.ITEMS));
        } else if (holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.mContentView.setText(mValues.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ItemHolder extends ViewHolder {
        public TextView mContentView;

        public ItemHolder(View view) {
            super(view);
            mContentView = view.findViewById(R.id.content);
        }
    }

    public class HeadHolder extends ViewHolder {
        public RecyclerView recyclerView;

        public HeadHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.list);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
