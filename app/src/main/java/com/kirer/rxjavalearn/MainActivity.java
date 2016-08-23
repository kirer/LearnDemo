package com.kirer.rxjavalearn;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.kirer.rxjavalearn.databinding.ActivityMainBinding;
import com.kirer.ui.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    public void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new MainViewModel(binding);
        binding.setViewModel(viewModel);
        binding.setManager(new LinearLayoutManager(this));
        binding.setAdapter(new MyAdapter());
    }
}
