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
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iot.user.R;
import com.iot.user.base.BaseFragment;
import com.iot.user.databinding.FragmentUnitDevNodeProductBinding;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.dev.UnitDevAddressAdapter;
import com.iot.user.ui.adapter.dev.UnitDevNodeProductAdapter;
import com.iot.user.ui.adapter.dev.UnitDevNodeSettingAdapter;
import com.iot.user.ui.contract.dev.DevNodeContract;
import com.iot.user.ui.model.dev.UnitDevDetailAddrModel;
import com.iot.user.ui.model.dev.push.UnitDevNodeProPushModel;
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
 * Use the {@link UnitDevNodeProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitDevNodeProductFragment extends BaseFragment<FragmentUnitDevNodeProductBinding>{
    public UnitDevNodeProductFragment() {
        // Required empty public constructor
    }

    public static UnitDevNodeProductFragment newInstance(String devNum, DevNodePresenter nodePresenter) {
        UnitDevNodeProductFragment fragment = new UnitDevNodeProductFragment();
        fragment.devNum=devNum;
        fragment.mPresenter=nodePresenter;
        return fragment;
    }
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_dev_node_product;
    }
    RecyclerView rv_dev_address;
    RecyclerView rv_dev_node;
    RelativeLayout rl_dev_empty;
    private String devNum="";
    private DevNodePresenter mPresenter=null;
    private UnitDevAddressAdapter itemAdapter;
    private UnitDevNodeProductAdapter nodeAdapter;
    private UnitDevDetailAddrModel selectAddr;
    @Override
    protected void initView(View view) {
        rv_dev_address=dataBinding.rvDevAddress;
        rv_dev_node=dataBinding.rvDevNode;
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
    }

    private void initItemAdapter(){
        if (mPresenter.loadDatas.size()==0){
            rl_dev_empty.setVisibility(View.VISIBLE);
        }else {
            rl_dev_empty.setVisibility(View.GONE);
        }
        if (itemAdapter == null) {
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
            mPresenter.postDevLoadList(devNum,"node_pro");
        }else{
            dealPostLoadList();
        }
    }
    /***节点列表***/
    private void initNodeItemAdapter(){/***1开启0关闭**/
        if (nodeAdapter == null) {
            if(mPresenter.nodeModeDatas!=null && getContext()!=null){
                nodeAdapter = new UnitDevNodeProductAdapter(getContext(), mPresenter.nodeModeDatas,true);
                if(rv_dev_node!=null){
                    rv_dev_node.setAdapter(nodeAdapter);
                }
                nodeAdapter.setOnItemClickLitener(new UnitDevNodeProductAdapter.OnItemListener() {
                    @Override
                    public void onDevOpenClick(Boolean isOpen, String nodeId, String productId) {
                        int open=0;
                        if (isOpen==true){
                            open=1;
                        }
                        clickOpenNodeProductBtn(nodeId,productId,open);
                    }
                });
            }
        } else {
            nodeAdapter.updateDatas(mPresenter.nodeModeDatas);
        }
    }

    private void initDevNodeInfo() {/***获取设备节点信息***/
        String hArrId= Util.getTwoNumFloatWith(selectAddr.getHaddr(),false);
        if (mPresenter.nodeModeDatas.size()==0) {
            mPresenter.getDevLoadProList(devNum, hArrId,"node_pro");
        }else{
            initNodeItemAdapter();
        }
    }

    private void clickOpenNodeProductBtn(String nodeId, String productId, final int isOpen) {/***节点设备开关**/
        nodeId = Util.getTwoNumFloatWith(nodeId, false);/**节点id***/
        String hAddrId = Util.getTwoNumFloatWith(selectAddr.getHaddr(), false);/**回路id***/
        productId = Util.getTwoNumFloatWith(productId, false);/**设备id***/
        Map proMap = new HashMap();
        proMap.put("caddr", productId);
        proMap.put("onoff", isOpen);

        Map proArr[] = {proMap};

        Map productMap = new HashMap();
        productMap.put("naddr", nodeId);
        productMap.put("channels", proArr);

        List list = new ArrayList();
        Map nodeArr[] = {productMap};
        Map map = new HashMap();
        map.put("haddr", hAddrId);
        map.put("nodes", nodeArr);
        list.add(map);
        if (isOpen == 0) {
            networkOpenLoading("关闭");
        } else {
            networkOpenLoading("开启");
        }
        mPresenter.clickOpenNodeProductBtn(devNum,list,"node_pro");
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
            dialog = null;
        }
    }

    private MaterialDialog dialog=null;
    public void networkOpenLoading(String title){
        if (dialog==null) {
            dialog = DialogUtils.showMyDialog(getContext(), "提示", "设备" + title + "正在处理,硬件等待响应中，请稍后查看....", "确定","", new DialogUtils.OnDialogClickListener() {
                @Override
                public void onConfirm() {
                    networkCloseLoading();
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
    public void notificationNodeProData(UnitDevNodeProPushModel pushModel){
        networkCloseLoading();
        initDevNodeInfo();
    }

    public void onNodeSuccess(BaseResponse bean, String type) {
        LogUtil.e("devNodeProduct+++++++"+type);
        if (type.equals("load_list")){
            dealPostLoadList();
        }else if (type.equals("node_pro_list")){
            initNodeItemAdapter();
        }else if (type.equals("close_node_pro")){
        }
    }
    private void dealPostLoadList(){/***回路列表***/
        if (mPresenter.loadDatas.size()>0){
            selectAddr=mPresenter.loadDatas.get(0);
            initDevNodeInfo();
        }
        initItemAdapter();
    }
}