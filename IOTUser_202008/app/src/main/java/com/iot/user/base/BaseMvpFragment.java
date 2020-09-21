package com.iot.user.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;

import com.iot.user.http.model.BaseResponse;
import com.iot.user.utils.MyToast;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.ObservableSubscribeProxy;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.ObservableConverter;


public abstract class BaseMvpFragment<T extends BasePresenter,V extends ViewDataBinding> extends BaseFragment implements BaseView {

    protected T mPresenter;
    protected V dataBinding;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (dataBinding != null) {
            dataBinding.unbind();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false);
        initView(dataBinding.getRoot());
        initClickBtn();
        return dataBinding.getRoot();
    }

    protected void initClickBtn(){

    }



    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    /**
     * 初始化视图
     *
     * @param view
     */
    protected abstract void initView(View view);

    @Override
    public void showLoading(String content) {
        if (content==null){
            showProgressDialog();
        }else{
            showProgressDialog(content);
        }
    }

    @Override
    public void hideLoading() {
        dissmissProgressDialog();
    }

    @Override
    public void onError(String errMessage) {
        MyToast.showShortToast(errMessage);
    }

    @Override
    public void onComplete() {

    }

    /**
     * 绑定生命周期 防止MVP内存泄漏
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }
}
