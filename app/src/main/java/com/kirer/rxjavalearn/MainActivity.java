package com.kirer.rxjavalearn;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.kirer.rxjavalearn.databinding.ActivityMainBinding;
import com.kirer.rxjavalearn.databinding.IDataBinding;
import com.kirer.ui.BaseActivity;
import com.kirer.widget.recyclerview.BaseAdapter;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    private BaseAdapter<Article> adapter;

    @Override
    public void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new MainViewModel(binding);
        binding.setViewModel(viewModel);
        binding.setManager(new LinearLayoutManager(this));
        adapter = new BaseAdapter<Article>() {
            @Override
            public int getLayoutId() {
                return R.layout.i_data;
            }

            @Override
            public void bind(BindingHolder holder, int position) {
                IDataBinding binding = (IDataBinding) holder.binding;
                binding.setArticle(dataList.get(position));
            }

            @Override
            public void onItemClick(View view, int position) {
                toast(adapter.getDataList().get(position).getWho());
            }
        };
        binding.setAdapter(adapter);

    }
}
