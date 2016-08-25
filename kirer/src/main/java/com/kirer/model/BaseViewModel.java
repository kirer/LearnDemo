package com.kirer.model;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.widget.Toast;

/**
 * Created by xinwb on 2016/8/10.
 */
public abstract class  BaseViewModel<T extends ViewDataBinding> {
    protected T binding;
    protected Context mContext;

    public BaseViewModel(T binding) {
        this.binding = binding;
        this.mContext = binding.getRoot().getContext();
    }

    protected void toast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

}
