package com.iot.user.base;


import com.iot.user.http.model.BaseResponse;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.ObservableSubscribeProxy;

import io.reactivex.ObservableConverter;

public interface BaseView {

    /**
     * 显示加载中
     */
    void showLoading(String content);

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 数据获取失败
     * @param errMessage
     */
    void onError(String errMessage);

    /**
     * 数据请求完成
     * @param
     */
    void onComplete();

    /**
     * 绑定Android生命周期 防止RxJava内存泄漏
     *
     * @param <T>
     * @return
     */
    <T> AutoDisposeConverter<T> bindAutoDispose();

}
