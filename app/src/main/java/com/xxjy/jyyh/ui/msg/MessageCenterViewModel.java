package com.xxjy.jyyh.ui.msg;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.ArticleListBean;

public class MessageCenterViewModel extends BaseViewModel<MessageCenterRepository> {

    public MessageCenterViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ArticleListBean> articlesLiveData = new MutableLiveData<>();
    public MutableLiveData<ArticleListBean> noticesLiveData = new MutableLiveData<>();
    public void getArticles( int pageNum,int pageSize){
        mRespository.getArticles( articlesLiveData,  pageNum, pageSize);
    }
    public void getNotices( int pageNum,int pageSize){
        mRespository.getNotices( noticesLiveData,  pageNum, pageSize);
    }
}
