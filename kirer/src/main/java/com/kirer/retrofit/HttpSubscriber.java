package com.kirer.retrofit;

import android.widget.Toast;

import com.kirer.Kirer;
import com.kirer.utils.L;

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
            Toast.makeText(Kirer.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        onFailed(e);
    }

    @Override
    public void onNext(HttpResult<T> t) {
        L.d("Http onNext --> error : " + t.error + " count : " + t.count + " result : " + t.results);
        if (!t.error) {
            onSuccess(t.results);
        } else
            onFailed(new Throwable("error=" + t.error));
    }

    public abstract void onSuccess(T t);

    public abstract void onFailed(Throwable e);
}
