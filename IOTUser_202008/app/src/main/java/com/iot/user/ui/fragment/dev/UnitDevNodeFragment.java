package com.iot.user.ui.fragment.dev;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.base.BaseFragment;
import com.iot.user.databinding.FragmentUnitDevNodeBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.activity.dev.UnitDevChartActivity;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.dev.UnitDevAddressAdapter;
import com.iot.user.ui.adapter.dev.UnitDevNodeAdapter;
import com.iot.user.ui.adapter.dev.UnitDevNodeProductAdapter;
import com.iot.user.ui.contract.dev.DevNodeContract;
import com.iot.user.ui.model.dev.UnitDevDetailAddrModel;
import com.iot.user.ui.model.dev.UnitDevDetailNodeModel;
import com.iot.user.ui.presenter.dev.DevNodePresenter;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.LogUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.Util;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.uber.autodispose.AutoDisposeConverter;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnitDevNodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitDevNodeFragment extends BaseFragment<FragmentUnitDevNodeBinding>{
    public UnitDevNodeFragment() {
    }
    public static UnitDevNodeFragment newInstance(String devNum,DevNodePresenter nodePresenter) {
        UnitDevNodeFragment fragment = new UnitDevNodeFragment();
        fragment.devNum=devNum;
        fragment.mPresenter=nodePresenter;
        return fragment;
    }
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_dev_node;
    }
    RecyclerView rv_dev_address;
    RecyclerView rv_dev_node;
    RecyclerView rv_dev_node_mode;
    RelativeLayout rl_dev_empty;
    TextView tv_node_num;
    TextView tv_address_num;
    TextView tv_node_mode_num;
    private String devNum="";
    private DevNodePresenter mPresenter=null;
    private UnitDevAddressAdapter itemAdapter;
    private UnitDevNodeAdapter nodeAdapter;
    private UnitDevNodeProductAdapter nodeModeAdapter;
    private UnitDevDetailAddrModel selectAddr;
    @Override
    protected void initView(View view) {
        rv_dev_address=dataBinding.rvDevAddress;
        rv_dev_node=dataBinding.rvDevNode;
        rv_dev_node_mode=dataBinding.rvDevNodeMode;
        rl_dev_empty=dataBinding.rlDevEmpty;
        tv_node_num=dataBinding.tvNodeNum;
        tv_address_num=dataBinding.tvAddressNum;
        tv_node_mode_num=dataBinding.tvNodeModeNum;
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
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_dev_node_mode.setLayoutManager(linearLayoutManager2);
        rv_dev_node_mode.setItemAnimator(new DefaultItemAnimator());
        rv_dev_node_mode.addItemDecoration(new GridSpacingItemDecoration(1, 20, true));
        rv_dev_node_mode.setVerticalScrollBarEnabled(true);
        initNodeModeItemAdapter();
    }
    private void initItemAdapter(){
        if (mPresenter.loadDatas.size()==0){
            rl_dev_empty.setVisibility(View.VISIBLE);
        }else {
            rl_dev_empty.setVisibility(View.GONE);
        }
        if (itemAdapter == null) {
            if (getContext()!=null) {
                itemAdapter = new UnitDevAddressAdapter(getContext(), mPresenter.loadDatas);
                if (rv_dev_address != null) {
                    rv_dev_address.setAdapter(itemAdapter);
                }
                itemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        selectAddr = mPresenter.loadDatas.get(position);
                        itemAdapter.updateDatas(mPresenter.loadDatas, selectAddr);
                        initDevNodeInfo();
                    }
                });
            }
        } else {
            itemAdapter.updateDatas(mPresenter.loadDatas,selectAddr);
        }
    }

   private void initDevLoadInfo(){
        if (mPresenter.loadDatas.size()==0){
           mPresenter.postDevLoadList(devNum,"load_node");
        }else{
            dealPostLoadList();
        }
   }

    /***节点列表***/
    private void initNodeItemAdapter(){
        if (nodeAdapter == null ) {
            if(mPresenter.nodeDatas!=null && getContext()!=null){
                nodeAdapter = new UnitDevNodeAdapter(getContext(), mPresenter.nodeDatas);
                if(rv_dev_node!=null){
                    rv_dev_node.setAdapter(nodeAdapter);
                }
                nodeAdapter.setmOnItemClickLitener(new UnitDevNodeAdapter.OnClickItemListener() {
                    @Override
                    public void onItemClick(int position) {
                        UnitDevDetailNodeModel nodeModel=mPresenter.nodeDatas.get(position);
                        Intent intent=new Intent(getContext(), UnitDevChartActivity.class);
                        intent.putExtra("DevNumDetail",devNum);
                        intent.putExtra("DevNodeDetail",nodeModel.getDaddr());
                        intent.putExtra("DevAddrDetail",selectAddr.getHaddr());
                        startActivity(intent);
                    }
                });
            }
        } else {
            nodeAdapter.updateDatas(mPresenter.nodeDatas);
        }
    }
    private void initDevNodeInfo() {/***获取设备节点信息***/
        if (selectAddr == null || selectAddr.getDevHid() == null) {
            return;
        }
        String hArrId = Util.getTwoNumFloatWith(selectAddr.getHaddr(), false);
        mPresenter.getDevLoadNodeList(devNum,hArrId,"load_node");
    }

    /***节点联动模块列表***/
    private void initNodeModeItemAdapter(){
        if (nodeModeAdapter == null) {
            if (getContext()!=null) {
                nodeModeAdapter = new UnitDevNodeProductAdapter(getContext(), mPresenter.nodeModeDatas, false);
                if (rv_dev_node_mode != null) {
                    rv_dev_node_mode.setAdapter(nodeModeAdapter);
                }
            }
        } else {
            nodeModeAdapter.updateDatas(mPresenter.nodeModeDatas);
        }
    }

    private void initDevNodeModeInfo() {/***获取联动模块信息***/
        if (selectAddr == null || selectAddr.getDevHid() == null) {
            return;
        }
        String hArrId = Util.getTwoNumFloatWith(selectAddr.getHaddr(), false);
        mPresenter.getDevLoadProList(devNum,hArrId,"load_node");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try{
            if(getUserVisibleHint()){
                initDevNodeInfo();
                addNodeDataHeart();
                //界面可见时
            }else {
                removeNodeDataHeart();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /***添加数据心跳**/
    public void addNodeDataHeart(){
        removeNodeDataHeart();
        handler.postDelayed(runnable, 10000);
    }
    public void removeNodeDataHeart(){
        if (handler!=null&&runnable!=null) {
            handler.removeCallbacks(runnable);
        }
    }
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            initDevNodeInfo();
            addNodeDataHeart();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeNodeDataHeart();
    }



    public void onNodeSuccess(BaseResponse bean, String type) {
        LogUtil.e("devNode+++++++"+type);
        if (type.equals("load_list")){
            dealPostLoadList();
        }else if (type.equals("node_list")){
            dealPostNodeList();
        }else if (type.equals("node_pro_list")){
            dealPostNodeProList();
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
    private void dealPostLoadList(){
        if (mPresenter.loadDatas.size()>0){
            selectAddr=mPresenter.loadDatas.get(0);
            initDevNodeInfo();
            tv_address_num.setText("总数"+mPresenter.loadDatas.size());
        }else{
            tv_address_num.setText("总数0");
        }
        initItemAdapter();
    }

    private void dealPostNodeList(){
        initDevNodeModeInfo();
        initNodeItemAdapter();
        if (mPresenter.nodeDatas.size()>0){
            tv_node_num.setText("总数"+mPresenter.nodeDatas.size());
        }else{
            tv_node_num.setText("总数0");
        }
    }
    private void dealPostNodeProList(){
        if (tv_node_mode_num==null){
            return;
        }
        if (mPresenter.nodeModeDatas!=null&&mPresenter.nodeModeDatas.size()>0){
            tv_node_mode_num.setText("总数"+mPresenter.nodeModeDatas.size());
        }else{
            tv_node_mode_num.setText("总数0");
        }
        initNodeModeItemAdapter();
    }

}