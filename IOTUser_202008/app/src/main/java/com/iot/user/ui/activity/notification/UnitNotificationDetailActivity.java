package com.iot.user.ui.activity.notification;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitNotificationDetailBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.contract.notification.UnitNotificationContract;
import com.iot.user.ui.model.notification.NoticeContentModel;
import com.iot.user.ui.presenter.notification.UnitNotificationPresenter;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.URLImageParser;
import com.iot.user.utils.URLTagHandler;

import butterknife.BindView;

public class UnitNotificationDetailActivity extends BaseMvpActivity<UnitNotificationPresenter,ActivityUnitNotificationDetailBinding> implements UnitNotificationContract.View {
    Button set_notice_read_btn;
    Toolbar mToolbar;
    TextView tv_notice_title;
    TextView tv_notice_content;
    EditText et_feedback;
    TextView tv_notice_time;
    NoticeContentModel noticeContent = null;
    public static final String NOTICE_INFO = "noticeInfo";
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_notification_detail;
    }

    @Override
    public void initView() {
        mPresenter=new UnitNotificationPresenter();
        mPresenter.attachView(this);
        noticeContent = (NoticeContentModel) getIntent().getSerializableExtra(NOTICE_INFO);
       set_notice_read_btn=dataBinding.setNoticeReadBtn;
       mToolbar=(Toolbar) dataBinding.toolbar;
       tv_notice_title=dataBinding.tvNoticeTitle;
       tv_notice_content=dataBinding.tvNoticeContent;
       tv_notice_time=dataBinding.tvNoticeTime;
       et_feedback=dataBinding.etFeedback;
        if(noticeContent!=null){
//            noticeTitle = noticeContent.getPublish_title();
            tv_notice_title.setText(noticeContent.getPublish_title());

            URLImageParser imageGetter = new URLImageParser(tv_notice_content);
            tv_notice_content.setMovementMethod(LinkMovementMethod.getInstance());
            tv_notice_content.setText(Html.fromHtml(noticeContent.getPublish_text(),imageGetter,new URLTagHandler(this)));
            tv_notice_time.setText(DateUtil.getYearDayTime2(noticeContent.getNotice_time()));
            //0 代表未读   1 已读
            if(noticeContent.getReaded() == 1){
                set_notice_read_btn.setVisibility(View.GONE);
                et_feedback.setVisibility(View.GONE);
            }else{
                set_notice_read_btn.setVisibility(View.VISIBLE);
                et_feedback.setVisibility(View.VISIBLE);
            }
        }
        initMyToolBar();
        if (noticeContent!=null){
           mPresenter.setReadNotice(noticeContent.getNotice_id());
        }
    }
    private void initMyToolBar() {
        initToolBar(mToolbar, "公告详情", R.drawable.gank_ic_back_white);
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

    @Override
    public void onSuccess(BaseResponse bean, String type) {

    }
}