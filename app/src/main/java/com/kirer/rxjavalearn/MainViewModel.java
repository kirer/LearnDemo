package com.kirer.rxjavalearn;

import android.databinding.ObservableBoolean;
import android.view.View;

import com.kirer.model.BaseViewModel;
import com.kirer.retrofit.HttpResult;
import com.kirer.retrofit.HttpSubscriber;
import com.kirer.retrofit.ServiceFactory;
import com.kirer.retrofit.TransformUtils;
import com.kirer.rxjavalearn.databinding.ActivityMainBinding;
import com.kirer.utils.L;
import com.kirer.widget.recyclerview.KListView;

import java.util.List;

/**
 * Created by xinwb on 2016/8/23.
 */
public class MainViewModel extends BaseViewModel<ActivityMainBinding> {

    private int page = 1;
    public final ObservableBoolean refreshing = new ObservableBoolean();
    public final ObservableBoolean loadingMore = new ObservableBoolean();

    public MainViewModel(ActivityMainBinding binding) {
        super(binding);

        binding.stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshing.set(false);
            }
        });
    }


    public final KListView.LoadingListener listener = new KListView.LoadingListener() {
        @Override
        public void onRefresh() {
            page = 1;
            getDataList(page);
        }

        @Override
        public void onLoadMore() {
            getDataList(++page);
        }
    };

    private void getDataList(final int page) {
        ServiceFactory
                .getInstance()
                .createService(ArticlesService.class)
                .articles()
                .compose(TransformUtils.<HttpResult<List<Article>>>defaultSchedulers())
                .subscribe(new HttpSubscriber<List<Article>>() {
                    @Override
                    public void onSuccess(List<Article> articles) {
                        if (page == 1) {
                            refreshing.set(false);
                            binding.getAdapter().setDataList(articles);
                        } else {
                            loadingMore.set(false);
                            binding.getAdapter().addDataList(articles);
                        }
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        refreshing.set(false);
                        loadingMore.set(false);
                        toast(e.getMessage());
                    }
                });
    }


}
