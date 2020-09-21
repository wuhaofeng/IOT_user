package com.iot.user.ui.adapter.dev;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.model.dev.UnitDevDetailAddrModel;
import com.iot.user.utils.AppValidationMgr;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitDevAddressAdapter extends RecyclerView.Adapter <UnitDevAddressAdapter.MyViewHolder>{
    private Context context;
    private List<UnitDevDetailAddrModel> mDatas;
    private UnitDevDetailAddrModel selectAddr;
    private LayoutInflater layoutInflater;

    public UnitDevAddressAdapter(Context context, List mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        if (mDatas!=null&&mDatas.size()>0){
            selectAddr=(UnitDevDetailAddrModel) mDatas.get(0);
        }
        layoutInflater = LayoutInflater.from(this.context);
    }
    public void updateDatas(List<UnitDevDetailAddrModel> mDatas,UnitDevDetailAddrModel selectAddr) {
        this.mDatas = mDatas;
        this.selectAddr=selectAddr;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = layoutInflater.inflate(R.layout.item_unit_dev_detail_addr, viewGroup, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        UnitDevDetailAddrModel devModel=mDatas.get(position);
        if (devModel.getHaddr().equals(selectAddr.getHaddr())){
            viewHolder.tv_unit_detail_addr.setBackgroundResource(R.drawable.bg_rect_blue_btn_fill);
            viewHolder.tv_unit_detail_addr.setTextColor(Color.WHITE);
        }else {
            viewHolder.tv_unit_detail_addr.setBackgroundResource(R.drawable.bg_rect_btn_blue_empty_white);
            viewHolder.tv_unit_detail_addr.setTextColor(Color.BLACK);
        }
        viewHolder.tv_unit_detail_addr.setText(AppValidationMgr.removeStringSpace(devModel.getHname(),0));
        if (TextUtils.isEmpty(AppValidationMgr.removeStringSpace(devModel.getHname(),0))){
            viewHolder.tv_unit_detail_addr.setText(""+Double.valueOf(devModel.getHaddr()).intValue());
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
        @BindView(R.id.rl_unit_detail_addr)
        RelativeLayout rl_unit_detail_addr;
        @BindView(R.id.tv_unit_detail_addr)
        TextView tv_unit_detail_addr;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
