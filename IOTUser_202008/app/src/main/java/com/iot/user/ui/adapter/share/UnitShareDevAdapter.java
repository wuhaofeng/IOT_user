package com.iot.user.ui.adapter.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.model.share.UnitShareDevModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitShareDevAdapter extends RecyclerView.Adapter <UnitShareDevAdapter.MyViewHolder>{
    private Context context;
    private List<UnitShareDevModel> mDatas;
    private LayoutInflater layoutInflater;
    public UnitShareDevModel selectModel;

    public UnitShareDevAdapter(Context context, List<UnitShareDevModel> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void updateDatas(List<UnitShareDevModel>mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = layoutInflater.inflate(R.layout.item_unit_share_dev, parent, false);

        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        final UnitShareDevModel reatineInfo = mDatas.get(position);

        viewHolder.tv_dev_status.setText(reatineInfo.getDevDefaultName());
        String devNum=String.valueOf(reatineInfo.getDevNum());
        if (devNum.length()>4){
            String  devStatus=devNum.substring(1,4);
            if (devStatus.equals("192")){

            }
        }
        viewHolder.tv_dev_date.setText(devNum);
        if (selectModel==null){
            viewHolder.iv_family_select.setBackgroundResource(R.drawable.btn_circle_unselect);
        }else {
            if (devNum.equals(String.valueOf(selectModel.getDevNum()))) {
                viewHolder.iv_family_select.setBackgroundResource(R.drawable.btn_circle_select);
            } else {
                viewHolder.iv_family_select.setBackgroundResource(R.drawable.btn_circle_unselect);
            }
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectModel=reatineInfo;
                notifyDataSetChanged();
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

        @BindView(R.id.tv_dev_status)
        TextView tv_dev_status;
        @BindView(R.id.tv_dev_date)
        TextView tv_dev_date;
        @BindView(R.id.iv_family_select)
        ImageView iv_family_select;
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
