package com.kirer.ui;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kirer.model.BaseViewModel;


/**
 * Created by xinwb on 2016/8/5.
 */
public abstract class BaseActivity<T extends ViewDataBinding, E extends BaseViewModel> extends AppCompatActivity {

    public T binding;
    public E viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public abstract void init();

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
