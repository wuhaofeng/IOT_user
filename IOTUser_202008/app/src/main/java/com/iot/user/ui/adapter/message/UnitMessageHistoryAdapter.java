package com.iot.user.ui.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.model.message.UnitMessageModel;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitMessageHistoryAdapter extends RecyclerView.Adapter <UnitMessageHistoryAdapter.MyViewHolder>{
    private Context mContext;
    private List<UnitMessageModel> mDatas;
    private LayoutInflater layoutInflater;

    public UnitMessageHistoryAdapter(Context context, List<UnitMessageModel> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void updateDatas(List<UnitMessageModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = layoutInflater.inflate(R.layout.item_unit_message_history, parent, false);

        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        UnitMessageModel reatineInfo = mDatas.get(position);

        int drawableId = R.drawable.user;
        String statusStr= Util.getDevStatusStatus(reatineInfo.getInfoType());
        viewHolder.tv_dev_status.setText(statusStr);
        if(statusStr.equals("设备故障")){
            viewHolder.tv_dev_status.setTextColor(mContext.getResources().getColor(R.color.type_02));
        }else if(statusStr.equals("设备报警")){
            viewHolder.tv_dev_status.setTextColor(mContext.getResources().getColor(R.color.red));
        }else{
            viewHolder.tv_dev_status.setTextColor(mContext.getResources().getColor(R.color.textColor));
        }

        if(statusStr.equals("设备故障")){
            drawableId = R.drawable.unit_message_history_warn;
        }else if(statusStr.equals("设备报警")){
            drawableId = R.drawable.unit_message_history_alert;
        }else{
            drawableId = R.drawable.unit_message_history_normal;
        }
        viewHolder.iv_dev_status.setImageResource(drawableId);
        viewHolder.tv_dev_num.setText("编号:"+reatineInfo.getDevNum());
        viewHolder.tv_dev_address.setText("位置:"+reatineInfo.getFinalAddress());
        //设备更新时间
        viewHolder.tv_dev_date.setText("时间:"+ DateUtil.getYearDayTime(reatineInfo.getAt()));

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

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_dev_status)
        ImageView iv_dev_status;
        @BindView(R.id.tv_dev_status)
        TextView tv_dev_status;
        @BindView(R.id.tv_dev_num)
        TextView tv_dev_num;
        @BindView(R.id.tv_dev_address)
        TextView tv_dev_address;
        @BindView(R.id.tv_dev_date)
        TextView tv_dev_date;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
