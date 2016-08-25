package com.kirer.rxjavalearn;

import android.databinding.ObservableBoolean;

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
    }


    public final KListView.LoadingListener listener = new KListView.LoadingListener() {
        @Override
        public void onRefresh() {
            refreshing.set(true);
            page = 1;
            getDataList(page);
        }

        @Override
        public void onLoadMore() {
            loadingMore.set(true);
            getDataList(++page);
        }
    };

    private void getDataList(final int page) {
        ServiceFactory
                .getInstance()
                .createService(ArticlesService.class)
                .articles(10, page)
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
