package com.iot.user.ui.activity.share;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitFamilyShareBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.contract.share.FamilyShareContract;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.presenter.share.FamilySharePresenter;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.KeyboardUtils;
import com.iot.user.utils.MyToast;

import butterknife.BindView;

public class UnitFamilyShareActivity extends BaseMvpActivity<FamilySharePresenter, ActivityUnitFamilyShareBinding> implements FamilyShareContract.View {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_family_share;
    }
    Toolbar mToolbar;
    EditText et_search_share;
    TextView tv_member_nickname;
    TextView tv_member_phone;
    RelativeLayout rl_share_member;
    ImageView iv_unit_family_share;
    Button unit_share_btn;

    private UnitFamilyModel familyModel;
    private String memberName="";
    private String devNum="";
    private Boolean fromDev=false;
    public static String FamilyModel="family_model";
    @Override
    public void initView() {
        mPresenter=new FamilySharePresenter();
        mPresenter.attachView(this);
      mToolbar=(Toolbar)dataBinding.toolbar;
      et_search_share=dataBinding.etSearchShare;
      tv_member_nickname=dataBinding.tvMemberNickname;
      tv_member_phone=dataBinding.tvMemberPhone;
      rl_share_member=dataBinding.rlShareMember;
      iv_unit_family_share=dataBinding.ivUnitFamilyShare;
      unit_share_btn=dataBinding.unitShareBtn;
        Intent intent=getIntent();
        if (intent!=null) {
            familyModel = (UnitFamilyModel) intent.getSerializableExtra(FamilyModel);
            memberName=intent.getStringExtra("MemberName");
            devNum=intent.getStringExtra("DevNum");
            if (devNum!=null){
                fromDev=true;
            }
        }
        initMyToolBar();
        initViews();
    }
    private void initViews(){
        rl_share_member.setVisibility(View.GONE);
        et_search_share.setText(memberName);
        iv_unit_family_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideSoftInput( UnitFamilyShareActivity.this);
            }
        });
        et_search_share.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    clickShareBtn();
                }
                return false;
            }
        });
        et_search_share.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i1==1){
                    unit_share_btn.setText("查找");
                }
                if (charSequence.toString().equals("")){
                    unit_share_btn.setText("查找");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    /***获取成员信息***/
    private void getShareMemberData() {
        String memberStr = AppValidationMgr.removeStringSpace(et_search_share.getText().toString(), 0);
        if (TextUtils.isEmpty(memberStr)) {
            MyToast.showShortToast("请输入手机号/账号");
        }
        mPresenter.postUnitMemberInfo(memberStr);
    }
    /***分享成员***/
    private void postShareMember(){
        String memberStr= AppValidationMgr.removeStringSpace(et_search_share.getText().toString(),0);
        if (TextUtils.isEmpty(memberStr)){
            MyToast.showShortToast("请输入手机号/账号");
        }
        if (fromDev==true){
            mPresenter.postUnitAddMemberList(devNum,memberStr);
        }else{
            mPresenter.postUnitFamilyShare(memberStr,familyModel.getFamily_id());
        }
    }
    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "成员共享", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "成员共享", R.drawable.gank_ic_back_night);
        }
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
      if (type.equals("member_info")){
          tv_member_nickname.setText(mPresenter.memberInfoModel.getNickName());
          tv_member_phone.setText(mPresenter.memberInfoModel.getPhone());
          unit_share_btn.setText("确认");
          rl_share_member.setVisibility(View.VISIBLE);
      }else if (type.equals("dev_share")){
          Intent intent = new Intent();
          intent.putExtra("respond", "finish");
          // 设置返回码和返回携带的数据
          setResult(Activity.RESULT_OK, intent);
          finish();
      }else if (type.equals("family_share")){
          Intent intent = new Intent();
          intent.putExtra("respond", "finish");
          // 设置返回码和返回携带的数据
          setResult(Activity.RESULT_OK, intent);
          finish();
      }
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        unit_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickShareBtn();
            }
        });
    }
    void clickShareBtn(){/**分享成员**/
        if (unit_share_btn.getText().equals("查找")){/**查找成员id**/
            getShareMemberData();
        }else {
            unit_share_btn.setText("查找");
            postShareMember();
        }
    }
}