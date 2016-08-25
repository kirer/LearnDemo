package com.kirer.rxjavalearn;


import com.kirer.rxjavalearn.databinding.IDataBinding;
import com.kirer.widget.recyclerview.BaseAdapter;


/**
 * Created by xinwb on 2016/8/22.
 */
public class MyAdapter extends BaseAdapter<Article> {


    @Override
    public int getLayoutId() {
        return R.layout.i_data;
    }

    @Override
    public void bind(BindingHolder holder, int position) {
        IDataBinding binding = (IDataBinding) holder.binding;
        binding.setArticle(dataList.get(position));
    }

}
