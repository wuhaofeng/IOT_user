package com.iot.user.ui.fragment.dev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.base.BaseFragment;
import com.iot.user.databinding.FragmentUnitDevAlertDescBinding;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.adapter.dev.UnitDevDescAdapter;
import com.iot.user.ui.model.dev.UnitDevAlertAddressModel;
import com.iot.user.ui.model.dev.UnitDevAlertNodeModel;
import com.iot.user.ui.model.message.UnitDevAlertDescModel;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UnitDevAlertDescFragment extends BaseFragment<FragmentUnitDevAlertDescBinding> {
    RecyclerView rv_dev_desc;
    TextView tv_dev_desc_title;
    RelativeLayout rl_unit_dev_alert_desc;
    private UnitDevDescAdapter itemAdapter;
    private List mDatas=new ArrayList();
    public String title="";
    public UnitDevAlertDescModel descModel;
    public UnitDevAlertDescFragment() {
    }
    public static UnitDevAlertDescFragment newInstance(String title, UnitDevAlertDescModel descModel) {
        UnitDevAlertDescFragment fragment = new UnitDevAlertDescFragment();
        fragment.title=title;
        fragment.descModel=descModel;
        return fragment;
    }
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_unit_dev_alert_desc;
    }

    @Override
    protected void initView(View view) {
        rv_dev_desc=dataBinding.rvDevDesc;
        tv_dev_desc_title=dataBinding.tvDevDescTitle;
        rl_unit_dev_alert_desc=dataBinding.rlUnitDevAlertDesc;
        initViews();
    }

    private void initViews(){
        if (descModel!=null){
            tv_dev_desc_title.setText(descModel.getTitle());
            if (descModel.getAlertAddress()==null){
            }else {
                for (int i = 0; i < descModel.getAlertAddress().size(); i++) {
                    UnitDevAlertAddressModel addressModel = descModel.getAlertAddress().get(i);
                    mDatas.add(addressModel);
                    for (int j = 0; j < addressModel.getNode().size(); j++) {
                        UnitDevAlertNodeModel nodeModel = addressModel.getNode().get(j);
                        mDatas.add(nodeModel);
                    }
                }
            }
        }else {
            tv_dev_desc_title.setText(title);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_dev_desc.setLayoutManager(linearLayoutManager);
        rv_dev_desc.setItemAnimator(new DefaultItemAnimator());
        rv_dev_desc.addItemDecoration(new GridSpacingItemDecoration(1, 2, true));
//        /***添加分割线**/
//        DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.line_list_item_decoration));
//        rv_dev_desc.addItemDecoration(divider);
        rv_dev_desc.setVerticalScrollBarEnabled(true);
        initItemAdapter();
    }

    private void initItemAdapter(){
        if (itemAdapter == null ) {
            if(mDatas!=null && mDatas.size()>0 &&getContext()!=null){
                itemAdapter = new UnitDevDescAdapter(getContext(), mDatas);
                if(rv_dev_desc!=null){
                    rv_dev_desc.setAdapter(itemAdapter);
                }
                itemAdapter.setOnItemClickLitener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
            }
        } else {
            if(mDatas!=null) {
                itemAdapter.updateDatas(mDatas);
            }
        }
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        rl_unit_dev_alert_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}
