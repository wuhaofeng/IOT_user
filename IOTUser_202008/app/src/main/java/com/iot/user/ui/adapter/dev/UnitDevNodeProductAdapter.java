package com.iot.user.ui.adapter.dev;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.ui.model.dev.UnitNodeProductModel;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitDevNodeProductAdapter extends RecyclerView.Adapter<UnitDevNodeProductAdapter.MyViewHolder> {
    private Context context;
    private List<UnitNodeProductModel> mDatas;
    private LayoutInflater layoutInflater;
    private Boolean canOperation;

    public UnitDevNodeProductAdapter(Context context, List mDatas,Boolean canOperation) {
        this.context = context;
        this.mDatas = mDatas;
        this.canOperation=canOperation;
        layoutInflater = LayoutInflater.from(this.context);
    }
    public void updateDatas(List<UnitNodeProductModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = layoutInflater.inflate(R.layout.item_dev_node_detail_body, viewGroup, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        final UnitNodeProductModel productModel=(UnitNodeProductModel)mDatas.get(position);
        ((MyViewHolder) viewHolder).tv_node_pro_title.setText(Util.getNodeProductType(productModel.getType()));
        ((MyViewHolder) viewHolder).tv_node_pro_address.setText("安装位置:"+productModel.getNodeAddr());
        ((MyViewHolder) viewHolder).tv_node_pro_id.setText("节点ID:"+Double.valueOf(productModel.getNodeID()).intValue());
        ((MyViewHolder) viewHolder).tv_node_pro_accress.setText("通道:"+Double.valueOf(productModel.getMaddr()).intValue());

        ((MyViewHolder) viewHolder).sc_dev_switch_compat.setOnCheckedChangeListener(null);/***关闭点击后的监听**/
        if (Util.getNodeProductStatus(productModel.getStatus()).equals("开启")){
            ((MyViewHolder) viewHolder).sc_dev_switch_compat.setChecked(true);
        }else {
            ((MyViewHolder) viewHolder).sc_dev_switch_compat.setChecked(false);
        }
        if (canOperation==false){
            ((MyViewHolder) viewHolder).sc_dev_switch_compat.setVisibility(View.GONE);
        }
        if (Util.getNodeProductType(productModel.getType()).equals("风机")){
            if (Util.getNodeProductStatus(productModel.getStatus()).equals("开启")){
                ((MyViewHolder) viewHolder).btn_node_prodect_open.setBackgroundResource(R.drawable.unit_node_fan_open);
            }else {
                ((MyViewHolder) viewHolder).btn_node_prodect_open.setBackgroundResource(R.drawable.unit_node_fan_close);
            }
        }else if (Util.getNodeProductType(productModel.getType()).equals("电磁阀")){
            if (Util.getNodeProductStatus(productModel.getStatus()).equals("开启")){
                ((MyViewHolder) viewHolder).btn_node_prodect_open.setBackgroundResource(R.drawable.unit_node_clicket_open);
            }else {
                ((MyViewHolder) viewHolder).btn_node_prodect_open.setBackgroundResource(R.drawable.unit_node_clicket_close);
            }
        }else {
            if (Util.getNodeProductStatus(productModel.getStatus()).equals("开启")){
                ((MyViewHolder) viewHolder).btn_node_prodect_open.setBackgroundResource(R.drawable.unit_node_lignt_open);
            }else {
                ((MyViewHolder) viewHolder).btn_node_prodect_open.setBackgroundResource(R.drawable.unit_node_light_close);
            }
        }

        ((MyViewHolder) viewHolder).sc_dev_switch_compat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {/***设备开/关**/
                        if (mOnItemLitener!=null){
                            mOnItemLitener.onDevOpenClick(b,productModel.getNodeID(),productModel.getMaddr());
                        }
                        compoundButton.setChecked(!compoundButton.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_node_prodect_open)
        Button btn_node_prodect_open;
        @BindView(R.id.tv_node_pro_title)
        TextView tv_node_pro_title;
        @BindView(R.id.tv_node_pro_accress)
        TextView tv_node_pro_accress;
        @BindView(R.id.tv_node_pro_address)
        TextView tv_node_pro_address;
        @BindView(R.id.tv_node_pro_id)
        TextView tv_node_pro_id;
        @BindView(R.id.sc_dev_switch_compat)
        SwitchCompat sc_dev_switch_compat;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (PrefUtil.getUnitOperationPermission(IOTApplication.getIntstance())<2){
                sc_dev_switch_compat.setVisibility(View.GONE);
            }else {
                sc_dev_switch_compat.setVisibility(View.VISIBLE);
            }
        }
    }
    public interface OnItemListener{
        void onDevOpenClick(Boolean isOpen,String nodeId, String productId);
    }
    private OnItemListener mOnItemLitener;

    public void setOnItemClickLitener(OnItemListener mOnItemLitener) {
        this.mOnItemLitener = mOnItemLitener;
    }
}

