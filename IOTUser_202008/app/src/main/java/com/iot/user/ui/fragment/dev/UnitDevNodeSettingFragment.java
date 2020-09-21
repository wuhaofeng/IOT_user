package com.iot.user.ui.fragment.dev;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iot.user.R;
import com.iot.user.base.BaseFragment;
import com.iot.user.databinding.FragmentUnitDevNodeBinding;
import com.iot.user.databinding.FragmentUnitDevNodeSettingBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.dev.UnitDevAddressAdapter;
import com.iot.user.ui.adapter.dev.UnitDevNodeSettingAdapter;
import com.iot.user.ui.contract.dev.DevNodeContract;
import com.iot.user.ui.model.dev.UnitDevDetailAddrModel;
import com.iot.user.ui.model.dev.UnitDevDetailNodeModel;
import com.iot.user.ui.model.dev.push.UnitDevNodePushModel;
import com.iot.user.ui.model.dev.push.UnitDevRoadPushModel;
import com.iot.user.ui.presenter.dev.DevNodePresenter;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.LogUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.Util;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitDevNodeSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitDevNodeSettingFragment extends BaseFragment<FragmentUnitDevNodeSettingBinding>{
    public UnitDevNodeSettingFragment() {
        // Required empty public constructor
    }
    public static UnitDevNodeSettingFragment newInstance(String devNum, DevNodePresenter nodePresenter) {
        UnitDevNodeSettingFragment fragment = new UnitDevNodeSettingFragment();
        fragment.devNum=devNum;
        fragment.mPresenter=nodePresenter;
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_dev_node_setting;
    }
    RecyclerView rv_dev_address;
    RecyclerView rv_dev_node;
    RelativeLayout rl_dev_empty;
    Button btn_unit_dev_harr_open;

    private String devNum="";
    private DevNodePresenter mPresenter=null;
    private UnitDevAddressAdapter itemAdapter;
    private UnitDevNodeSettingAdapter nodeAdapter;
    private UnitDevDetailAddrModel selectAddr;
    @Override
    protected void initView(View view) {
        rv_dev_address=dataBinding.rvDevAddress;
        rv_dev_node=dataBinding.rvDevNode;
        btn_unit_dev_harr_open=dataBinding.btnUnitDevHarrOpen;
        rl_dev_empty=dataBinding.rlDevEmpty;
        initViews();
        initDevLoadInfo();
    }
    /***回路列表***/
    private void initViews(){
        /***    rv_dev_address.setLayoutManager(new GridLayoutManager(getContext(),3));
         rv_dev_address.setItemAnimator(new DefaultItemAnimator());
         rv_dev_address.addItemDecoration(new GridSpacingItemDecoration(3, PXTransUtils.dp2px(getContext(),10), true));
         rv_dev_address.setHorizontalScrollBarEnabled(true);   */
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());  //LinearLayoutManager中定制了可扩展的布局排列接口，子类按照接口中的规范来实现就可以定制出不同排雷方式的布局了
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_dev_address.setLayoutManager(layoutManager);
        rv_dev_address.setItemAnimator(new DefaultItemAnimator());
        rv_dev_address.setHorizontalScrollBarEnabled(true);

        initItemAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_dev_node.setLayoutManager(linearLayoutManager);
        rv_dev_node.setItemAnimator(new DefaultItemAnimator());
        rv_dev_node.addItemDecoration(new GridSpacingItemDecoration(1, 20, true));
        rv_dev_node.setVerticalScrollBarEnabled(true);
        initNodeItemAdapter();
        btn_unit_dev_harr_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertViewOpenHArrBtn();
            }
        });
    }
    private void initItemAdapter(){
        if (mPresenter.loadDatas.size()==0){
            rl_dev_empty.setVisibility(View.VISIBLE);
        }else {
            rl_dev_empty.setVisibility(View.GONE);
        }
        if (itemAdapter == null ) {
            if(mPresenter.loadDatas!=null && mPresenter.loadDatas.size()>0&&getContext()!=null){
                itemAdapter = new UnitDevAddressAdapter(getContext(), mPresenter.loadDatas);
                if(rv_dev_address!=null){
                    rv_dev_address.setAdapter(itemAdapter);
                }
                itemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        selectAddr=mPresenter.loadDatas.get(position);
                        itemAdapter.updateDatas(mPresenter.loadDatas,selectAddr);
                        initDevNodeInfo();
                    }
                });
            }
        } else {
            if(mPresenter.loadDatas!=null) {
                itemAdapter.updateDatas(mPresenter.loadDatas,selectAddr);
            }
        }
    }
    private void initDevLoadInfo(){
        if (mPresenter.loadDatas.size()==0){
            mPresenter.postDevLoadList(devNum,"node_setting");
        }else{
            dealPostLoadList();
        }
    }

    private void updateDevHarrOpenData(String status){
        selectAddr.setStatus(status);
        for (int i=0;i<mPresenter.loadDatas.size();i++){
            UnitDevDetailAddrModel addrModel=mPresenter.loadDatas.get(i);
            if (selectAddr.getHaddr().equals(addrModel.getHaddr())){
                addrModel.setStatus(status);
            }
        }
        if (Util.getDevNodeStatus(selectAddr.getStatus()).equals("开启")) {
            btn_unit_dev_harr_open.setText("屏蔽回路");
        }else {
            btn_unit_dev_harr_open.setText("开启回路");
        }
    }

    /***节点列表***/
    private void initNodeItemAdapter(){
        if (nodeAdapter == null ) {
            if(mPresenter.nodeAllDatas!=null &&getContext()!=null&&getContext()!=null){
                nodeAdapter = new UnitDevNodeSettingAdapter(getContext(), mPresenter.nodeAllDatas);
                if(rv_dev_node!=null){
                    rv_dev_node.setAdapter(nodeAdapter);
                }
                nodeAdapter.setmOnItemClickLitener(new UnitDevNodeSettingAdapter.OnClickItemListener() {
                    @Override
                    public void onItemClick(int position) {
                    }
                    @Override
                    public void onOpenClick(Boolean isOpen, int position) {
                        if (isOpen==false){/**屏蔽节点**/
                            clickOpenNodeBtn(position,false);
                        }else {/**开启节点**/
                            clickOpenNodeBtn(position,true);
                        }
                    }
                });
            }
        } else {
            nodeAdapter.updateDatas(mPresenter.nodeAllDatas);
        }
    }
    private void initDevNodeInfo() {/***获取设备节点信息***/
        String hArrId = Util.getTwoNumFloatWith(selectAddr.getHaddr(), false);
        mPresenter.getDevLoadAllNodeList(devNum,hArrId,"node_setting");
    }

    private void showAlertViewOpenHArrBtn(){
        String title="确定要开启此回路吗?";
        if (Util.getDevNodeStatus(selectAddr.getStatus()).equals("开启")) {/**屏蔽回路**/
            title="确定要屏蔽此回路吗?";
        }
        DialogUtils.showMyDialog(getContext(), "提示", title, "确定","取消", new DialogUtils.OnDialogClickListener() {
            @Override
            public void onConfirm() {
                clickOpenHAddrBtn();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void clickOpenHAddrBtn() {/***开启屏蔽回路**/
        List list = new ArrayList();
        list.add(selectAddr.getHaddr());
        showProgressDialogWitnTime(2);
        if (Util.getDevNodeStatus(selectAddr.getStatus()).equals("开启")) {/**屏蔽回路**/
             mPresenter.postUnitHArrCloseCode(devNum,list,"node_setting");
        }else{
            mPresenter.postUnitHArrOpenCode(devNum,list,"node_setting");
        }
    }
    private void clickOpenNodeBtn(int position, Boolean isOpen){/***节点屏蔽/开启**/
        final UnitDevDetailNodeModel nodeModel=mPresenter.nodeAllDatas.get(position);
        String nodeId= Util.getTwoNumFloatWith(nodeModel.getDaddr(),false);/**节点id***/
        String hAddrId=Util.getTwoNumFloatWith(selectAddr.getHaddr(),false);/**回路id***/
        List list=new ArrayList();
        String nodeArr[]={nodeId};
        Map map=new HashMap();
        map.put("haddr",hAddrId);
        map.put("naddrs",nodeArr);
        list.add(map);
        showProgressDialogWitnTime(2);
        if (isOpen==false) {
            networkOpenLoading("节点屏蔽");
            mPresenter.postUnitNodeCloseCode(devNum,list,"node_setting");
        }else {
            networkOpenLoading("节点开启");
            mPresenter.postUnitNodeOpenCode(devNum,list,"node_setting");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try{
            if(getUserVisibleHint()){
                initDevNodeInfo();
                //界面可见时
            }else {
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
        if (dialog != null) {
            dialog.dismiss();
            dialog=null;
        }
    }
    private MaterialDialog dialog=null;
    public void networkOpenLoading(String title){
        if (dialog==null) {
            dialog = DialogUtils.showMustConfirmDialog(getContext(), "提示", "设备" + title + "正在处理,硬件等待响应中，请稍后查看....", "确定", new DialogUtils.OnDialogClickListener() {
                @Override
                public void onConfirm() {
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }
    public void networkCloseLoading(){
        if (dialog!=null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void notificationRoadData(UnitDevRoadPushModel pushModel){
        networkCloseLoading();
        if (pushModel==null||pushModel.getHaddrs()==null){
            pushModel=new UnitDevRoadPushModel();
            String[] selectHarr={selectAddr.getHaddr()};
            pushModel.setOnoff(selectAddr.getStatus());
            pushModel.setHaddrs(selectHarr);
        }
        for (int i=0;i<mPresenter.loadDatas.size();i++){
            UnitDevDetailAddrModel addrModel=mPresenter.loadDatas.get(i);
            if (pushModel.getHaddrs()[0].equals(addrModel.getHaddr())){
                addrModel.setStatus(pushModel.getOnoff());
            }
        }
        if (pushModel.getHaddrs()[0].equals(selectAddr.getHaddr())){
            selectAddr.setStatus(pushModel.getOnoff());
            initDevNodeInfo();
        }else{
            if (itemAdapter!=null) {
                itemAdapter.updateDatas(mPresenter.loadDatas, selectAddr);
                initDevNodeInfo();
            }
        }
        if (Util.getDevNodeStatus(selectAddr.getStatus()).equals("开启")) {/**1开启2屏蔽**/
            btn_unit_dev_harr_open.setText("屏蔽回路");
        }else {
            btn_unit_dev_harr_open.setText("开启回路");
        }
    }

    public void notificationNodeData(UnitDevNodePushModel pushModel){
        /***暂时不主动去刷新回路**/
        LogUtil.d("关闭节点屏蔽的提示");
        networkCloseLoading();
        if (pushModel.getOnoffs().size()>0) {
            String hArrID=pushModel.getOnoffs().get(0).getHaddr();
            mPresenter.updateDevHaddrInfoNetWork(devNum,hArrID,"node_setting");
        }else{
            MyToast.showShortToast("未返回回路相关数据");
        }
    }



    public void onNodeSuccess(BaseResponse bean, String type) {
        LogUtil.e("devNodeSetting+++++++"+type);
        if (type.equals("load_list")){
            dealPostLoadList();
        }else if (type.equals("node_list_all")){
            initNodeItemAdapter();
        }else if (type.equals("close_load")){
            networkOpenLoading("回路屏蔽");
        }else if (type.equals("open_load")){
            networkOpenLoading("回路开启");
        }else if (type.equals("setting_notification")){
            dealNotificationNodeList(bean);
        }else if (type.equals("dialog")){
            DialogUtils.showMyDialog(context, "提示", bean.getMessage(),
                    "确定", "取消", new DialogUtils.OnDialogClickListener() {
                        @Override
                        public void onConfirm() {
                        }
                        @Override
                        public void onCancel() {

                        }
                    });
        }
    }

    private void dealPostLoadList(){/***回路列表***/
        if (mPresenter.loadDatas.size()>0){
            selectAddr=mPresenter.loadDatas.get(0);
            initDevNodeInfo();
            updateDevHarrOpenData(selectAddr.getStatus());
        }
        initItemAdapter();
    }

    private void dealNotificationNodeList(BaseResponse bean){/***透传回来的数据进行数据请求***/
        Map dataDic=(Map) bean.getBody();
        Map dataArr=(Map) dataDic.get("haddrStatus");
        if (selectAddr.getHaddr().equals(dataArr.get("haddr"))){
            selectAddr.setStatus(String.valueOf(dataArr.get("status")));
            updateDevHarrOpenData(selectAddr.getStatus());
        }else{
            selectAddr.setHaddr((String) dataArr.get("haddr"));
            selectAddr.setStatus(String.valueOf(dataArr.get("status")));
            updateDevHarrOpenData(selectAddr.getStatus());
        }
        initDevNodeInfo();
    }

}