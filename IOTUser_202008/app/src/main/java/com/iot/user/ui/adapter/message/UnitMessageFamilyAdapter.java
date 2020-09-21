package com.iot.user.ui.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.model.message.UnitMessageFamilyModel;
import com.iot.user.utils.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitMessageFamilyAdapter extends RecyclerView.Adapter <UnitMessageFamilyAdapter.MyViewHolder>{

    private Context context;
    private List<UnitMessageFamilyModel> mDatas;
    private LayoutInflater layoutInflater;
    private int fragmentStatus;

    public UnitMessageFamilyAdapter(Context context, List<UnitMessageFamilyModel> mDatas,int fragmentStatus) {
        this.context = context;
        this.mDatas = mDatas;
        this.fragmentStatus=fragmentStatus;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void updateDatas(List<UnitMessageFamilyModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = layoutInflater.inflate(R.layout.item_unit_message_family, parent, false);

        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        final UnitMessageFamilyModel reatineInfo = mDatas.get(position);

        viewHolder.tv_dev_status.setText(reatineInfo.getNews());
        viewHolder.iv_dev_status.setImageResource(R.drawable.unit_message_home);
        if (fragmentStatus!=1){
            viewHolder.iv_dev_status.setImageResource(R.drawable.unit_message_dev);
        }
        //设备更新时间
        viewHolder.tv_dev_date.setText("时间:"+ DateUtil.getYearDayTime(reatineInfo.getCreate_time()));

        if (Double.valueOf(reatineInfo.getNews_status())==1){
            if (Double.valueOf(reatineInfo.getType())==1){
                viewHolder.btn_arrow.setText("待接受");
                viewHolder.btn_arrow.setEnabled(false);
                viewHolder.btn_arrow.setTextColor(context.getResources().getColor(R.color.textLightColor));
                viewHolder.btn_arrow.setBackgroundResource(R.color.white);
            }else{
                viewHolder.btn_arrow.setText("同意");
                viewHolder.btn_arrow.setEnabled(true);
                viewHolder.btn_arrow.setTextColor(context.getResources().getColor(R.color.google_blue));
                viewHolder.btn_arrow.setBackgroundResource(R.drawable.bg_rect_btn_blue_empty_white);
            }
        }else if (Double.valueOf(reatineInfo.getNews_status())==2){
            viewHolder.btn_arrow.setText("已同意");
            viewHolder.btn_arrow.setEnabled(false);
            viewHolder.btn_arrow.setTextColor(context.getResources().getColor(R.color.textLightColor));
            viewHolder.btn_arrow.setBackgroundResource(R.color.white);
        }else {
            viewHolder.btn_arrow.setText("已失效");
            viewHolder.btn_arrow.setEnabled(false);
            viewHolder.btn_arrow.setTextColor(context.getResources().getColor(R.color.textLightColor));
            viewHolder.btn_arrow.setBackgroundResource(R.color.white);
        }
        viewHolder.btn_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Double.valueOf(reatineInfo.getNews_status()).intValue()==1) {
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onItemClick(view, position);
                    }
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onItemLongClick(view, position);
                }
                return false;
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
        @BindView(R.id.tv_dev_date)
        TextView tv_dev_date;
        @BindView(R.id.btn_arrow)
        Button btn_arrow;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickItemListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    private OnClickItemListener mOnItemClickLitener;

    public void setmOnItemClickLitener(OnClickItemListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}

