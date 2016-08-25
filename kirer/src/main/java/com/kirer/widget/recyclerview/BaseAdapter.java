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

    public List<T> getDataList() {
        if (this.dataList == null) {
            this.dataList = new ArrayList<>();
        }

        return this.dataList;
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
        return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false));
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, position);
                    return;
                }
                onItemClick(view, position);
            }
        });
        bind(holder, position);
    }

    public void onItemClick(View view, int position) {

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public abstract int getLayoutId();

    public abstract void bind(BindingHolder holder, int position);

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class BindingHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

        public T binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}