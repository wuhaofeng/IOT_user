package com.iot.user.ui.activity.dev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseMvpActivity;
import com.iot.user.databinding.ActivityUnitDevMemberBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.skin.SkinManager;
import com.iot.user.ui.activity.share.UnitFamilyShareActivity;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.dev.UnitDevMemberAdapter;
import com.iot.user.ui.contract.dev.DevMemberContract;
import com.iot.user.ui.model.dev.UnitDevMemberModel;
import com.iot.user.ui.presenter.dev.DevMemberPresenter;
import com.iot.user.ui.view.dev.InputDialog;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.MyToast;

import java.util.List;

import butterknife.BindView;

import static com.iot.user.ui.adapter.dev.UnitDevMemberAdapter.MODE_DELETE;

public class UnitDevMemberActivity extends BaseMvpActivity<DevMemberPresenter, ActivityUnitDevMemberBinding> implements DevMemberContract.View {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_unit_dev_member;
    }
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    private String devNum="";
    TextView tv_popwin;
    Button btn_share_submit;
    RelativeLayout ll_operate;
    private static int MemberShare=1001;
    private Boolean isFromDev=false;
    private Boolean isOwner=false;
    private UnitDevMemberAdapter itemAdapter;
    @Override
    public void initView() {
        mPresenter=new  DevMemberPresenter();
        mPresenter.attachView(this);
      mToolbar=(Toolbar) dataBinding.toolbar;
      mRecyclerView=dataBinding.swipeTarget;
      tv_popwin=dataBinding.tvPopwin;
      btn_share_submit=dataBinding.btnShareSubmit;
      ll_operate=dataBinding.llOperate;
        Intent intent=getIntent();
        if (intent!=null) {
            devNum = intent.getStringExtra("DevNum");
            isFromDev=intent.getBooleanExtra("isFromDev",false);
            isOwner=intent.getBooleanExtra("isOwner",false);
        }
        if (isFromDev==false){
            ll_operate.setVisibility(View.GONE);
            btn_share_submit.setVisibility(View.GONE);
        }
        initMyToolBar();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUnitDevShareList();
    }
    private void getUnitDevShareList(){
        mPresenter.postUnitDevMemberList(devNum);
    }
    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setVerticalScrollBarEnabled(true);
    }

    private void initAdapter() {
        clearState();
        if (itemAdapter == null ) {
            if(mPresenter.mDatas!=null && mPresenter.mDatas.size()>0){
                itemAdapter = new UnitDevMemberAdapter(this, mPresenter.mDatas);
                mRecyclerView.setAdapter(itemAdapter);
                itemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        UnitDevMemberModel memberModel=mPresenter.mDatas.get(position);
                        if(!DoubleClickUtil.isFastDoubleClick(R.id.toolbar)){
                            callPhone(memberModel.getPhone());
                        }
                    }
                });
            }
        } else {
            if(mPresenter.mDatas!=null){
                itemAdapter.updateDatas(mPresenter.mDatas);
            }
        }
    }
    private void callPhone(final String phone){
        if (TextUtils.isEmpty(phone)){ //电话号码为空
            return;
        }
        DialogUtils.showMyDialog(this, "提示", "确定要打电话给"+phone+"吗?",
                "确定", "取消", new DialogUtils.OnDialogClickListener() {
                    @Override
                    public void onConfirm() {
                        if(ContextCompat.checkSelfPermission(UnitDevMemberActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(UnitDevMemberActivity.this,
                                    new String[]{
                                            Manifest.permission.CALL_PHONE
                                    }, 99);
                        }else{
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:"+phone));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancel() {

                    }
                });
    }
    private void clearState(){
        if(btn_share_submit!=null){
            btn_share_submit.setVisibility(View.VISIBLE);
        }
        if(itemAdapter!=null){
            itemAdapter.clearState();
            itemAdapter.notifyDataSetChanged();
        }
        if (isFromDev==false){
            if(btn_share_submit!=null){
                btn_share_submit.setVisibility(View.GONE);
            }
            if (ll_operate!=null){
                ll_operate.setVisibility(View.GONE);
            }
        }
    }

    private void initMyToolBar() {
        int currentSkinType = SkinManager.getCurrentSkinType(this);
        if (SkinManager.THEME_DAY == currentSkinType) {
            initToolBar(mToolbar, "设备联系人", R.drawable.gank_ic_back_white);
        } else {
            initToolBar(mToolbar, "设备联系人", R.drawable.gank_ic_back_night);
        }
        if (isFromDev==true){
            initToolBarRightBtn(mToolbar,"删除联系人",0,1);
        }else {
            if (isOwner==true){
                initToolBarRightBtn(mToolbar,"更多操作",0,1);
            }

        }
    }

    @Override
    protected void clickRightBtn(View button) {
        super.clickRightBtn(button);
        if (isFromDev==true){/**当页面来自设备分享的时候，底部需要一个跳转ShareController的按钮***/
            if (mPresenter.mDatas.size()==1){
                DialogUtils.showMyDialog(UnitDevMemberActivity.this, "提示",
                        "亲，您还没有共享设备，无法删除！",
                        "确定", null,null);
                return;
            }
            if (mPresenter.findCanDeleteMemberNumber()==0){
                DialogUtils.showMyDialog(UnitDevMemberActivity.this, "提示",
                        "亲，只有通过设备共享添加的成员才能直接删除，家庭共享的成员请前往家庭管理页面进行删除！",
                        "确定", null,null);
                return;
            }
            if(itemAdapter!=null){
                itemAdapter.setMode(MODE_DELETE);
                itemAdapter.notifyDataSetChanged();
            }
            if(btn_share_submit!=null){
                btn_share_submit.setVisibility(View.GONE);
            }
        }else{
            showMorePopupWindow();
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

    private PopupWindow moreMenuPw;
    private void showMorePopupWindow(){
        View menuView = LayoutInflater.from(mContext).inflate(R.layout.popuwindow_useroperate, null);
        LinearLayout pop_add_user = menuView.findViewById(R.id.pop_add_user);
        LinearLayout pop_remove = menuView.findViewById(R.id.pop_remove);
        LinearLayout pop_transform = menuView.findViewById(R.id.pop_transform);
        pop_transform.setVisibility(View.GONE);


        moreMenuPw = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        moreMenuPw.setTouchable(true);
        moreMenuPw.setOutsideTouchable(true);
        /*
         * 新增关注用户
         */
        pop_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DoubleClickUtil.isFastDoubleClick(R.id.toolbar)){
                    moreMenuPw.dismiss();
                    // TODO: 2019/9/13 新增关注用户
                    final InputDialog inputDialog = new InputDialog(UnitDevMemberActivity.this,"添加关注用户","","请输入手机号");
                    inputDialog.setCancelListerner(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inputDialog.dismiss();
                        }
                    });
                    inputDialog.setConfirmListerner(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String account = inputDialog.getContent();
                            if(account == null || !AppValidationMgr.isPhone(account)){
                                MyToast.showShortToast("请输入正确的手机号");
                            }else{
                                inputDialog.dismiss();
                                clickAddMemberBtn(account);
                            }
                        }
                    });
                    try {
                        inputDialog.show();
                    }catch (Exception e){

                    }
                }
            }
        });
        /*
         * 删除关注用户
         */
        pop_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!DoubleClickUtil.isFastDoubleClick(R.id.toolbar)){
                    moreMenuPw.dismiss();
                    if (mPresenter.mDatas.size()==1){
                        DialogUtils.showMyDialog(UnitDevMemberActivity.this, "提示",
                                "亲，您还没有共享设备，无法删除！",
                                "确定", null,null);
                        return;
                    }
                    if (mPresenter.findCanDeleteMemberNumber()==0){
                        DialogUtils.showMyDialog(UnitDevMemberActivity.this, "提示",
                                "亲，只有通过设备共享添加的成员才能直接删除，家庭共享的成员请前往家庭管理页面进行删除！",
                                "确定", null,null);
                        return;
                    }
                    // TODO: 2019/9/13 删除关注用户
                    if(itemAdapter!=null){
                        itemAdapter.setMode(MODE_DELETE);
                        itemAdapter.notifyDataSetChanged();
                    }
                    if(btn_share_submit!=null){
                        btn_share_submit.setVisibility(View.GONE);
                    }
                    if(ll_operate!=null){
                        ll_operate.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        moreMenuPw.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));

        moreMenuPw.showAsDropDown(tv_popwin,-400,0);

    }



    @Override
    public void onSuccess(BaseResponse bean, String type) {
        if (type.equals("member_list")){
            initAdapter();
        }else if (type.equals("member_add")){
            clearState();
            getUnitDevShareList();
        }else if (type.equals("member_delete")){
            clearState();
            getUnitDevShareList();
        }
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        dataBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteBtn();
            }
        });
        dataBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearState();
            }
        });
        btn_share_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSubmitBtn();
            }
        });
    }
    void clickDeleteBtn() {/***删除联系人**/
        if (itemAdapter != null && itemAdapter.getChoosen() != null && itemAdapter.getChoosen().size() > 0) {
            List<String> choosen = itemAdapter.getChoosen();
            mPresenter.UnitMemberRemoveRequest(devNum,choosen);
        }
    }
    void clickSubmitBtn(){/***跳转设备共享页面***/
        Intent intent = new Intent(UnitDevMemberActivity.this, UnitFamilyShareActivity.class);
        intent.putExtra("MemberName", "");
        intent.putExtra("DevNum", devNum);
        startActivityForResult(intent, MemberShare);
    }
    void clickAddMemberBtn(String memberName){
        mPresenter.postUnitAddMemberList(devNum,memberName);
    }
}