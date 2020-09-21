package com.iot.user.ui.adapter.shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.model.shopping.ShoppingOrderDetailModel;
import com.iot.user.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingOrderListItemAdapter extends RecyclerView.Adapter<ShoppingOrderListItemAdapter.MyViewHolder> {
    private Context context;
    private List<ShoppingOrderDetailModel>mData;
    private LayoutInflater layoutInflater;
    public ShoppingOrderListItemAdapter(Context context, List<ShoppingOrderDetailModel> mData){
        this.context=context;
        this.mData=mData;
        this.layoutInflater=LayoutInflater.from(context);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_device_num)
        TextView tv_device_num;
        @BindView(R.id.tv_select_year_date)
        TextView tv_select_year_date;
        @BindView(R.id.tv_select_year_discount)
        TextView tv_select_year_discount;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_shopping_order_list_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        ShoppingOrderDetailModel listModel = mData.get(position);
        viewHolder.tv_title.setText(listModel.getDev_type()+"系列"+listModel.getName());
        viewHolder.tv_device_num.setText(listModel.getProduct_id());
        viewHolder.tv_select_year_date.setText(Util.getShoppingSelectYear(listModel.getRenew_year()));
        viewHolder.tv_select_year_discount.setText(listModel.getMark());
        if (listModel.getStatus().equals("0")){
            viewHolder.tv_select_year_discount.setText("无折扣");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
