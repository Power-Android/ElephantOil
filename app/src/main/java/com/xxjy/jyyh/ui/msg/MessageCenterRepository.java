package com.xxjy.jyyh.ui.msg;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.ArticleListBean;

import java.util.List;

import rxhttp.RxHttp;

public class MessageCenterRepository extends BaseRepository {
    public void getArticles(MutableLiveData<ArticleListBean> articlesLiveData, int pageNum, int pageSize ){
        addDisposable(RxHttp.postForm(ApiService.GET_ARTICLES)
                .add("type","notice")
                .add("pageNum",pageNum)
                .add("pageSize",pageSize)
                .asResponse(ArticleListBean.class)
                .subscribe(data -> articlesLiveData.postValue(data))
        );
    }
    public void getNotices(MutableLiveData<ArticleListBean> noticesLiveData, int pageNum,int pageSize ){
        addDisposable(RxHttp.postForm(ApiService.GET_NOTICES)
                .add("pageNum",pageNum)
                .add("pageSize",pageSize)
                .asResponse(ArticleListBean.class)
                .subscribe(data -> noticesLiveData.postValue(data))
        );
    }
}
