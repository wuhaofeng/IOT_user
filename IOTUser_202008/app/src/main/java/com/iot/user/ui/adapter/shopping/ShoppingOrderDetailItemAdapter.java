package com.iot.user.ui.adapter.shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.model.shopping.ShoppingOrderDetailModel;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingOrderDetailItemAdapter extends RecyclerView.Adapter <ShoppingOrderDetailItemAdapter.MyViewHolder>{
    private Context context;
    private List<ShoppingOrderDetailModel> mData;
    private LayoutInflater layoutInflater;
    public ShoppingOrderDetailItemAdapter(Context context, List<ShoppingOrderDetailModel> mData){
        this.context=context;
        this.mData=mData;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_shopping_order_submit, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        ShoppingOrderDetailModel listModel = mData.get(position);
        viewHolder.tv_price.setText("¥" + listModel.getTotal_fee());
        viewHolder.tv_select_year.setText(listModel.getMark());
        if (listModel.getStatus().equals("0")) {
            viewHolder.tv_select_year.setText(Util.getShoppingSelectYear(listModel.getRenew_year()));
        }
        viewHolder.tv_title.setText(listModel.getDev_type() + "系列" + listModel.getName());
        viewHolder.tv_devnum.setText("编号：" + listModel.getProduct_id());
        viewHolder.tv_location.setText("地址：" + listModel.getAddress());
        viewHolder.tv_date.setText(DateUtil.getYearTime(Long.parseLong(listModel.getExecuteTime())) + " 至 " + DateUtil.getYearTime(Long.parseLong(listModel.getExpireTime())));
    }

        @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
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
}
