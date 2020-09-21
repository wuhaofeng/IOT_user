package com.iot.user.ui.adapter.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.model.main.UnitFamilyDevModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitHomeDevAdapter  extends RecyclerView.Adapter<UnitHomeDevAdapter.MyViewHolder>{
        private Context context;
        private List<UnitFamilyDevModel> mDatas;
        private LayoutInflater layoutInflater;

        private Boolean haveListSize=false;
        public UnitHomeDevAdapter(Context context, List mDatas) {
            this.context = context;
            this.mDatas = mDatas;
            layoutInflater = LayoutInflater.from(this.context);
        }
        public void updateDatas(List<UnitFamilyDevModel> mDatas) {
            this.mDatas = mDatas;
            notifyDataSetChanged();
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View inflate = layoutInflater.inflate(R.layout.item_unit_home_dev, viewGroup, false);
            return new MyViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
            UnitFamilyDevModel devModel=mDatas.get(position);
            if (devModel.getDevType().equals("192")){
                viewHolder.iv_dev_icon.setBackgroundResource(R.drawable.d192);
            }else  if (devModel.getDevType().equals("182")) {
                viewHolder.iv_dev_icon.setBackgroundResource(R.drawable.d182);
            }else{
                viewHolder.iv_dev_icon.setBackgroundResource(R.drawable.d182);
            }
            viewHolder.tv_dev_place.setText(devModel.getPlaceName());
            viewHolder.tv_dev_name.setText(devModel.getFriendName());
            viewHolder.tv_dev_num.setText("设备编号:"+devModel.getDevNum());
            viewHolder.tv_dev_signal.setText("信号强度:"+devModel.getCsq());
            viewHolder.tv_dev_address.setText("安装位置:"+devModel.getAddress());
            int devStatus=0;
            if (!TextUtils.isEmpty(String.valueOf(devModel.getDevStatus()))){
                if (String.valueOf(devModel.getDevStatus()).equals("null")){
                    devStatus=0;
                }else {
                    Double devSta = Double.valueOf(String.valueOf(devModel.getDevStatus()));
                    devStatus = devSta.intValue();
                }
            }
            if (devStatus==1){//警告
                viewHolder.tv_dev_status.setText("警告");
                viewHolder.tv_dev_status.setTextColor(context.getResources().getColor(R.color.type_warning));
                viewHolder.iv_dev_status_img.setBackgroundResource(R.drawable.unit_warning);
                viewHolder.unit_home_room_bg.setBackgroundResource(R.drawable.bg_rect_btn_yellow_empty_white);
            }else  if (devStatus>1) {//报警
                viewHolder.tv_dev_status.setText("报警");
                viewHolder.tv_dev_status.setTextColor(context.getResources().getColor(R.color.type_alert));
                viewHolder.iv_dev_status_img.setBackgroundResource(R.drawable.unit_alert);
                viewHolder.unit_home_room_bg.setBackgroundResource(R.drawable.bg_rect_btn_red_empty_white);
            }else  if (Double.valueOf(devModel.getIsOnline())==2) {//离线
                viewHolder.tv_dev_status.setText("离线");
                viewHolder.tv_dev_status.setTextColor(context.getResources().getColor(R.color.type_offline));
                viewHolder.iv_dev_status_img.setBackgroundResource(R.drawable.unit_offline);
                viewHolder.unit_home_room_bg.setBackgroundResource(R.drawable.bg_rect_btn_gray_empty_white);
            }else {
                viewHolder.tv_dev_status.setText("在线");
                viewHolder.tv_dev_status.setTextColor(context.getResources().getColor(R.color.type_online));
                viewHolder.iv_dev_status_img.setBackgroundResource(R.drawable.unit_online);
                viewHolder.unit_home_room_bg.setBackgroundResource(R.drawable.bg_rect_btn_green_empty_white);
            }
            if (Integer.valueOf(devModel.getBindType())==3){
                viewHolder.tv_dev_place.setText("共享");
            }else if (Integer.valueOf(devModel.getBindType())==2){
                viewHolder.tv_dev_place.setText("关注");
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
            @BindView(R.id.unit_home_room_bg)
            RelativeLayout unit_home_room_bg;
            @BindView(R.id.iv_dev_icon)
            ImageView iv_dev_icon;
            @BindView(R.id.tv_dev_name)
            TextView tv_dev_name;
            @BindView(R.id.tv_dev_num)
            TextView tv_dev_num;
            @BindView(R.id.tv_dev_address)
            TextView tv_dev_address;
            @BindView(R.id.tv_dev_signal)
            TextView tv_dev_signal;
            @BindView(R.id.tv_dev_place)
            TextView tv_dev_place;

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
