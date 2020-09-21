package com.iot.user.base;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.iot.user.http.model.BaseResponse;
import com.iot.user.utils.MyToast;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.ObservableSubscribeProxy;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.ObservableConverter;

public abstract class BaseMvpActivity<T extends BasePresenter,V extends ViewDataBinding> extends BaseActivity  implements BaseView {

    protected T mPresenter;
    protected V dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        bundle.putString("Activity","BaseMvpActivity");
        super.onCreate(bundle);
        initViewDataBinding(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if(dataBinding != null){
            dataBinding.unbind();
        }
        super.onDestroy();
    }

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

    /***dataBinding***/
    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        dataBinding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState));
        initView();
        initClickBtn();
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化视图
     */
    public abstract void initView();

    protected void initClickBtn(){

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
