package com.iot.user.ui.adapter.dev;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.model.main.UnitFamilyDevModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitDevListAdapter extends RecyclerView.Adapter<UnitDevListAdapter.MyViewHolder> {
    private Context context;
    private List<UnitFamilyDevModel> mDatas;
    private LayoutInflater layoutInflater;
    private Boolean isFromAlert;
    public UnitDevListAdapter(Context context, List mDatas,Boolean isFromAlert) {
        this.context = context;
        this.mDatas = mDatas;
        this.isFromAlert=isFromAlert;
        layoutInflater = LayoutInflater.from(this.context);
    }
    public void updateDatas(List<UnitFamilyDevModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = layoutInflater.inflate(R.layout.item_unit_dev_list, viewGroup, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        UnitFamilyDevModel devModel=mDatas.get(position);
        if (devModel.getDevType().equals("192")){
            viewHolder.iv_dev_icon.setBackgroundResource(R.drawable.d192);
        }else  if (devModel.getDevType().equals("182")) {
            viewHolder.iv_dev_icon.setBackgroundResource(R.drawable.d182);
        }
        viewHolder.tv_dev_place.setText(devModel.getPlaceName());
        viewHolder.tv_dev_name.setText(devModel.getNickname());
        viewHolder.tv_dev_num.setText(devModel.getDevNum());
        int devStatus=0;
        if (devModel.getDevStatus()!=null&&!TextUtils.isEmpty(String.valueOf(devModel.getDevStatus()))){
            devStatus=Double.valueOf(devModel.getDevStatus()).intValue();
        }
        if (devStatus==1){//警告
            viewHolder.tv_dev_status.setText("警告");
            viewHolder.tv_dev_status.setTextColor(context.getResources().getColor(R.color.type_warning));
            viewHolder.iv_dev_status_img.setBackgroundResource(R.drawable.unit_warning);
        }else  if (devStatus>1) {//报警
            viewHolder.tv_dev_status.setText("报警");
            viewHolder.tv_dev_status.setTextColor(context.getResources().getColor(R.color.type_alert));
            viewHolder.iv_dev_status_img.setBackgroundResource(R.drawable.unit_alert);
        }else  if (Double.valueOf(devModel.getIsOnline())==2) {//离线
            viewHolder.tv_dev_status.setText("离线");
            viewHolder.tv_dev_status.setTextColor(context.getResources().getColor(R.color.type_offline));
            viewHolder.iv_dev_status_img.setBackgroundResource(R.drawable.unit_offline);
        }else {
            viewHolder.tv_dev_status.setText("在线");
            viewHolder.tv_dev_status.setTextColor(context.getResources().getColor(R.color.type_online));
            viewHolder.iv_dev_status_img.setBackgroundResource(R.drawable.unit_online);
        }
        if (Integer.valueOf(devModel.getBindType())==3){
            viewHolder.tv_dev_place.setText("共享");
        }else if (Integer.valueOf(devModel.getBindType())==2){
            viewHolder.tv_dev_place.setText("关注");
        }
        if (isFromAlert==true){
            viewHolder.tv_dev_mute.setVisibility(View.VISIBLE);
            if (Double.valueOf(devModel.getMute()).intValue()==0){
                viewHolder.tv_dev_mute.setText("未操作");
            }else if (Double.valueOf(devModel.getMute()).intValue()==1){
                viewHolder.tv_dev_mute.setText("已知道");
            }else{
                viewHolder.tv_dev_mute.setText("已消音");
            }
        }else{
           viewHolder.tv_dev_mute.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_dev_icon)
        ImageView iv_dev_icon;
        @BindView(R.id.tv_dev_name)
        TextView tv_dev_name;
        @BindView(R.id.tv_dev_num)
        TextView tv_dev_num;
        @BindView(R.id.tv_dev_place)
        TextView tv_dev_place;
        @BindView(R.id.tv_dev_mute)
        TextView tv_dev_mute;
        @BindView(R.id.tv_dev_status)
        TextView tv_dev_status;
        @BindView(R.id.tv_dev_status_img)
        ImageView iv_dev_status_img;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
