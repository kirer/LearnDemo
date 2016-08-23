package com.kirer.retrofit;

import android.text.TextUtils;
import android.util.Log;


import com.kirer.utils.L;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by xinwb on 2016/8/22.
 */
public abstract class HttpSubscriber<T> extends Subscriber<HttpResult<T>> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            Log.e("HttpSubscriber", "onError --> http exception " + e.getMessage());
            // ToastUtils.getInstance().showToast(e.getMessage());
        }
        onFailed(e);
    }

    @Override
    public void onNext(HttpResult<T> t) {
        if (TextUtils.isEmpty(t.error)) {
            onSuccess(t.posts);
        } else
            onFailed(new Throwable("error=" + t.error));
    }

    public abstract void onSuccess(T t);

    public abstract void onFailed(Throwable e);
}
