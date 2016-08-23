package com.kirer.rxjavalearn;


import com.kirer.retrofit.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by xinwb on 2016/8/17.
 */
public interface ArticlesService {

    String BASE_URL = "http://api.kanzhihu.com/";

    @GET("getposts")
    Observable<HttpResult<List<Article>>> articles();
}
