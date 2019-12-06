package com.lxk.annotationdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2019/11/29 13:53
 * <p>
 * recycler view adapter 的基类
 */
public abstract class BaseRecyclerAdapter<Item, Holder extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<Holder> {

    Context mContext;
    private List<Item> mRecyclerItems;


    BaseRecyclerAdapter(Context context) {
        this(context, null);
    }

    BaseRecyclerAdapter(Context context, List<Item> items) {
        this.mContext = context;
        this.mRecyclerItems = items;
    }

    /***
     * 加载item布局
     * @param layoutRes 布局item
     * @param parent 父容器
     * @return item布局
     */
    View inflate(int layoutRes, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(layoutRes, parent, false);
    }

    @Override
    @NonNull
    final public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(parent, viewType);
    }

    protected abstract Holder getViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindData(@NonNull Holder holder, int position);

    @Override
    final public void onBindViewHolder(@NonNull Holder holder, int position) {
        onBindData(holder, position);
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    /**
     * 获取数据集合
     */
    public List<Item> getItems() {
        if (mRecyclerItems == null) {
            mRecyclerItems = new ArrayList<>();
        }
        return mRecyclerItems;
    }

    /**
     * 设置数据集合
     */
    public void setItems(List<Item> mRecyclerItems) {
        this.mRecyclerItems = mRecyclerItems;
    }

    /**
     * 获取莫一个位置的数据
     */
    public Item getItem(int index) {
        if (index >= 0 && index < getItemCount()) {
            return getItems().get(index);
        }
        return null;
    }

    /**
     * 添加数据
     */
    public void addItem(Item item) {
        if (mRecyclerItems == null) {
            mRecyclerItems = new ArrayList<>();
        }
        this.mRecyclerItems.add(item);
    }

    /**
     * 添加数据
     */
    public void setItem(int index, Item item) {
        if (mRecyclerItems == null) {
            mRecyclerItems = new ArrayList<>();
        }
        this.mRecyclerItems.set(index, item);
    }

    /**
     * 添加数据
     */
    public void addItem(int index, Item item) {
        if (mRecyclerItems == null) {
            mRecyclerItems = new ArrayList<>();
        }
        this.mRecyclerItems.add(index, item);
    }

    /**
     * 添加数据集合
     */
    public void addItems(List<Item> items) {
        if (mRecyclerItems == null) {
            mRecyclerItems = new ArrayList<>();
        }
        this.mRecyclerItems.addAll(items);
    }

    /**
     * 添加数据集合
     */
    public void addItems(int index, List<Item> items) {
        if (mRecyclerItems == null) {
            mRecyclerItems = new ArrayList<>();
        }
        this.mRecyclerItems.addAll(index, items);
    }

    /**
     * 清除item数据
     */
    public void clean() {
        mRecyclerItems = new ArrayList<>();
    }
}
