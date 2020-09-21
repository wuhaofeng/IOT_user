package com.iot.user.ui.adapter.shopping;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.model.shopping.ShoppingDeviceListModel;
import com.iot.user.ui.model.shopping.ShoppingDeviceSelectListModel;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingDeviceListAdapter extends RecyclerView.Adapter<ShoppingDeviceListAdapter.MyViewHolder> {
    private Context context;
    private List<ShoppingDeviceListModel> mDatas;
    private List<ShoppingDeviceSelectListModel> selectList;
    public Float orderSwitch=0f;
    private LayoutInflater layoutInflater;
    public ShoppingDeviceListAdapter(Context context, List<ShoppingDeviceListModel> mDatas,List<ShoppingDeviceSelectListModel> selectList,Float orderSwitch) {
        this.context = context;
        this.mDatas = mDatas;
        this.selectList=selectList;
        this.orderSwitch=orderSwitch;
        layoutInflater = LayoutInflater.from(this.context);
    }
    /***AdapterDelegate**/
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_devnum)
        TextView tv_devnum;
        @BindView(R.id.iv_dev_icon)
        ImageView iv_dev_icon;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.tv_location)
        TextView tv_location;
        @BindView(R.id.tv_select_year_date) TextView tv_select_year;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.item_shopping_device_list, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder,final int position) {
        final ShoppingDeviceListModel devChargeItem = mDatas.get(position);
        if (getSelectModelWithProductId(devChargeItem.getDevNum())!=null){
            ShoppingDeviceSelectListModel selectModel=getSelectModelWithProductId(devChargeItem.getDevNum());
            viewHolder.tv_price.setText("¥"+Util.getTwoNumFloatWith(selectModel.getPrice(),true));
            viewHolder.tv_select_year.setText(selectModel.getMark());
            Log.e("测试orderSwitch", "onBindViewHolder: "+orderSwitch+selectModel.getMark());
            if (orderSwitch==0){
                viewHolder.tv_select_year.setText(Util.getShoppingSelectYear(selectModel.getRenew_year()));
            }
        }else {
            if (devChargeItem.getPrice()!=null) {
                viewHolder.tv_price.setText("¥" + Util.getTwoNumFloatWith(devChargeItem.getPrice(), true));
            }else {
                devChargeItem.setPrice("0");
                viewHolder.tv_price.setText("¥" + Util.getTwoNumFloatWith(devChargeItem.getPrice(), true));
            }
            viewHolder.tv_select_year.setText("续费一年 无优惠");
        }
        int drawableId = R.drawable.pic_gray_bg;
        if(devChargeItem.getDevType() == 101){
            drawableId = R.drawable.d101;
        }else if(devChargeItem.getDevType() == 102){
            drawableId = R.drawable.d102;
        }else if(devChargeItem.getDevType() == 181){
            drawableId = R.drawable.d181;
        }else if(devChargeItem.getDevType() == 182){
            drawableId = R.drawable.d182;
        }else if(devChargeItem.getDevType() == 201){
            drawableId = R.drawable.d201;
        }
        viewHolder.iv_dev_icon.setImageResource(drawableId);
        /**设备名称
        String devNickname = devChargeItem.getFriend_name().trim();
        if(devNickname!=null && !"".equals(devNickname)){
            viewHolder.tv_title.setText(devNickname);
        }else{
            viewHolder.tv_title.setText(devChargeItem.getName());
        }*/
        viewHolder.tv_title.setText(devChargeItem.getDevType()+"系列"+devChargeItem.getName());

        viewHolder.tv_location.setText("地  址："+devChargeItem.getAddress());
        if (devChargeItem.getExecuteTime()!=null){
            devChargeItem.setExecuteTime(Util.getTwoNumFloatWith(devChargeItem.getExecuteTime(),false));
        }
        if (devChargeItem.getExpireTime()!=null){
            devChargeItem.setExpireTime(Util.getTwoNumFloatWith(devChargeItem.getExpireTime(),false));
        }
        viewHolder.tv_date.setText("有效期："+ DateUtil.getYearTime(devChargeItem.getExecuteTime())+" 至 "+ DateUtil.getYearTime(devChargeItem.getExpireTime()));
        viewHolder.tv_devnum.setText("编  号："+devChargeItem.getDevNum());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemLitener!=null){
                    mOnItemLitener.onItemClick(view,position);
                }
            }
        });

    }

    public void updateSelectList(List<ShoppingDeviceSelectListModel> deviceSelectist){
        notifyDataSetChanged();
    }
    public ShoppingDeviceSelectListModel getSelectModelWithProductId(String productId){
        for(int i=0;i<selectList.size();i++){
            ShoppingDeviceSelectListModel dataModel=selectList.get(i);
            if (dataModel.getProduct_id().equals(productId)){
                return dataModel;
            }
        }
       return null;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

/***use data***/

    public void updateDatas(List<ShoppingDeviceListModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }


    public interface OnItemListener{
        void onItemClick(View view,int position);
    }
    private OnItemListener mOnItemLitener;

    public void setOnItemClickLitener(OnItemListener mOnItemLitener) {
        this.mOnItemLitener = mOnItemLitener;
    }
}
