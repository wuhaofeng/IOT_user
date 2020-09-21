package com.iot.user.ui.adapter.shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.model.shopping.ShoppingDeviceSelectListModel;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingOrderSubmitAdapter extends RecyclerView.Adapter<ShoppingOrderSubmitAdapter.MyViewHolder> {
    private Context context;
    private List<ShoppingDeviceSelectListModel> selectList;
    private Float orderSwitch=0f;
    private LayoutInflater layoutInflater;
    public ShoppingOrderSubmitAdapter(Context context, List<ShoppingDeviceSelectListModel> selectList, Float orderSwitch) {
        this.context = context;
        this.selectList=selectList;
        this.orderSwitch=orderSwitch;
        layoutInflater = LayoutInflater.from(this.context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.item_shopping_order_submit, parent, false);
        return new MyViewHolder(inflate);
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
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        final ShoppingDeviceSelectListModel selectModel = selectList.get(position);
        viewHolder.tv_price.setText("¥"+Util.getTwoNumFloatWith(selectModel.getPrice(),true));
        viewHolder.tv_select_year.setText(selectModel.getMark());
        if (orderSwitch==0){
            viewHolder.tv_select_year.setText(Util.getShoppingSelectYear(selectModel.getRenew_year()));
        }
        viewHolder.tv_title.setText(selectModel.getDev_type()+"系列"+selectModel.getName());
        viewHolder.tv_devnum.setText("编号："+selectModel.getProduct_id());
        viewHolder.tv_location.setText("地址："+selectModel.getAddress());
        viewHolder.tv_date.setText(DateUtil.getYearTime(Long.parseLong(selectModel.getExecuteTime()))+" 至 "+ DateUtil.getYearTime(Long.parseLong(selectModel.getExpireTime())));
    }

    @Override
    public int getItemCount() {
        return selectList.size();
    }
}
