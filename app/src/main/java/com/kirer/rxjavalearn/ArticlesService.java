package com.kirer.rxjavalearn;


import com.kirer.retrofit.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by xinwb on 2016/8/17.
 */
public interface ArticlesService {

    String BASE_URL = "http://gank.io/api/";

    @GET("data/all/{page_size}/{page}")
    Observable<HttpResult<List<Article>>> articles(
            @Path("page_size") int page_size,
            @Path("page") int page);
}
