package com.iot.user.ui.adapter.dev;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.ui.model.dev.UnitDevDetailNodeModel;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitDevNodeSettingAdapter extends RecyclerView.Adapter <UnitDevNodeSettingAdapter.MyViewHolder>{
    private Context context;
    private List<UnitDevDetailNodeModel> mDatas;
    private LayoutInflater layoutInflater;

    public UnitDevNodeSettingAdapter(Context context, List mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }
    public void updateDatas(List<UnitDevDetailNodeModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = layoutInflater.inflate(R.layout.item_unit_dev_detail_node_setting, viewGroup, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        UnitDevDetailNodeModel devModel=mDatas.get(position);
        viewHolder.tv_dev_node_address.setText("安装位置:"+devModel.getDname());
        viewHolder.tv_dev_node_name.setText("节点ID:"+Double.valueOf(devModel.getDaddr()).intValue());
        viewHolder.tv_dev_node_type.setText("节点类型:"+ Util.getDevNodeType(devModel.getType()));
        if (Util.getDevNodeType(devModel.getType()).equals("联动模块")){
           viewHolder.iv_node_dev.setBackgroundResource(R.drawable.unit_node_product_head);
        }else {
            Double conStatus=Double.parseDouble(devModel.getCondStatus());
            if (conStatus==0){
                viewHolder.iv_node_dev.setBackgroundResource(R.drawable.unit_node_product_green);
            }else if(conStatus==1){
                viewHolder.iv_node_dev.setBackgroundResource(R.drawable.unit_node_product_red);
            }else {
                viewHolder.iv_node_dev.setBackgroundResource(R.drawable.unit_node_product_yellow);
            }
        }
        viewHolder.tv_dev_node_status.setText("节点状态:"+Util.getDevNodeStatus(devModel.getStatus()));
        viewHolder.sc_switch_compat.setOnCheckedChangeListener (null);
        if (Util.getDevNodeStatus(devModel.getStatus()).equals("开启")){
            viewHolder.sc_switch_compat.setChecked(true);
            viewHolder.tv_dev_node_status.setTextColor(context.getResources().getColor(R.color.google_green));
        }else {
            viewHolder.sc_switch_compat.setChecked(false);
            viewHolder.tv_dev_node_status.setTextColor(context.getResources().getColor(R.color.black));
        }
        viewHolder.sc_switch_compat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onOpenClick(b,position);
                }
                compoundButton.setChecked(!compoundButton.isChecked());
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public interface OnClickItemListener{
        void onItemClick(int position);
        void onOpenClick(Boolean isOpen, int position);
    }

    private OnClickItemListener mOnItemClickLitener;

    public void setmOnItemClickLitener(OnClickItemListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_dev_node_name)
        TextView tv_dev_node_name;
        @BindView(R.id.tv_dev_node_type)
        TextView tv_dev_node_type;
        @BindView(R.id.tv_dev_node_status)
        TextView tv_dev_node_status;
        @BindView(R.id.tv_dev_node_address)
        TextView tv_dev_node_address;
        @BindView(R.id.iv_node_dev)
        ImageView iv_node_dev;
        @BindView(R.id.sc_switch_compat)
        SwitchCompat sc_switch_compat;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (PrefUtil.getUnitOperationPermission(IOTApplication.getIntstance())<2){
                sc_switch_compat.setVisibility(View.GONE);
            }else {
                sc_switch_compat.setVisibility(View.VISIBLE);
            }
        }
    }
}
