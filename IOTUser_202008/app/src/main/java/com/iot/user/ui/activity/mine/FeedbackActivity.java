package com.iot.user.ui.activity.mine;

import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cb.ratingbar.CBRatingBar;
import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.mine.FeedbackJsonRequest;
import com.iot.user.skin.SkinManager;
import com.iot.user.utils.PrefUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class FeedbackActivity extends BaseActivity {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_feedback;
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_feedback)
    EditText et_feedback;
    @BindView(R.id.feetback_tv)
    TextView feetback_tv;
    @BindView(R.id.feedback_btn)
    Button feedback_btn;
    @BindView(R.id.rating_bar)
    CBRatingBar rating_bar;
    private int score = 5;
    @Override
    public void initView() {
        ButterKnife.bind(this);
        initMyToolBar();
        rating_bar.setStarSize(60) //大小
                .setStarCount(5) //数量
                .setStarSpace(25) //间距
                .setShowStroke(false) //是否显示边框
                .setStarFillColor(Color.GRAY) //填充的背景颜色
                .setStarCoverColor(Color.RED) //填充的进度颜色
                .setStarMaxProgress(120) //最大进度
                .setStarProgress(120) //当前显示的进度
                .setUseGradient(false) //是否使用渐变填充（如果使用则coverColor无效）
                .setStartColor(Color.parseColor("#000000")) //渐变的起点颜色
                .setEndColor(Color.parseColor("#ffffff")) //渐变的终点颜色
                .setCanTouch(true) //是否可以点击
                .setCoverDir(CBRatingBar.CoverDir.leftToRight) //设置进度覆盖的方向
                .setOnStarTouchListener(new CBRatingBar.OnStarTouchListener() { //点击监听
                    @Override
                    public void onStarTouch(int touchCount) {
                        score = touchCount;
//                        Toast.makeText(FeedbackActivity.this, "点击第" + touchCount + "个星星", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "用户反馈", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "用户反馈", R.drawable.gank_ic_back_night);
        }
        et_feedback.addTextChangedListener(textWatcher);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发送反馈信息
     */
    @SuppressLint("AutoDispose")
    @OnClick(R.id.feedback_btn)
    public void btn_feedback() {
        FeedbackJsonRequest feedbackJsonRequest = new FeedbackJsonRequest(PrefUtil.getLoginAccountUid(FeedbackActivity.this),
                PrefUtil.getLoginToken(FeedbackActivity.this),
                String.valueOf(score),et_feedback.getText().toString());
        NetWorkApi.provideRepositoryData().feedback(feedbackJsonRequest)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                    }
                    @Override
                    public void onError(Throwable e) {
                        dissmissProgressDialog();
                    }
                    @Override
                    public void onComplete() {
                        dissmissProgressDialog();
                    }
                });
        Toast.makeText(FeedbackActivity.this,"非常感谢您提出宝贵建议",Toast.LENGTH_SHORT).show();
        FeedbackActivity.this.finish();
    }

    /**
     * 监听输入字数
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String fb_context = et_feedback.getText().toString();
            int len = fb_context.length();
            feetback_tv.setText(String.format(getString(R.string.feedback_content_you_can_enter),(200-len)));//"您还可以输入" "字"

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}