package com.kirer.widget.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinwb on 2016/8/23.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.BindingHolder> {

    protected List<T> dataList;

    public BaseAdapter() {
    }

    public BaseAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addDataList(List<T> dataList) {
        if (this.dataList == null) {
            this.dataList = new ArrayList<>();
        }
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), null));
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public abstract int getLayoutId();

    public abstract void bind(BindingHolder holder, int position);

    public class BindingHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

        public T binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}