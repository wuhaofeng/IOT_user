package com.iot.user.ui.adapter.shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.model.shopping.ShoppingOrderDiscountListModel;
import com.iot.user.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingSelectYearAdapter extends RecyclerView.Adapter<ShoppingSelectYearAdapter.MyViewHolder>{
    private Context context;
    private List<ShoppingOrderDiscountListModel.ShoppingOrderDiscountModel> mDatas;
    private LayoutInflater layoutInflater;
    private Float orderSwitch;
    private int selectItem;
    private float price;
    public ShoppingSelectYearAdapter(Context context, List<ShoppingOrderDiscountListModel.ShoppingOrderDiscountModel> mDatas,Float orderSwitch,float price) {
        this.context = context;
        this.mDatas = mDatas;
        this.selectItem=0;
        this.orderSwitch=orderSwitch;
        this.price=price;
        layoutInflater = LayoutInflater.from(this.context);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.tv_desc)
        TextView tv_desc;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.item_shopping_select_year, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        ShoppingOrderDiscountListModel.ShoppingOrderDiscountModel dataModel=mDatas.get(position);
        viewHolder.tv_title.setText(Util.getShoppingSelectYear(dataModel.getRenew_year()));

        viewHolder.tv_price.setText("¥"+Util.getTwoNumFloatWith(Integer.parseInt(dataModel.getRenew_year())*price,true));
        if (orderSwitch==2){
            viewHolder.tv_price.setText("¥"+Util.getTwoNumFloatWith(Integer.parseInt(dataModel.getRenew_year())*price*Float.parseFloat(dataModel.getDiscount()),true));
        }
        if (orderSwitch==0){
            viewHolder.tv_desc.setVisibility(View.GONE);
        }else {
            viewHolder.tv_desc.setVisibility(View.VISIBLE);
            viewHolder.tv_desc.setText(dataModel.getMark());
        }
        if (selectItem==position){
            viewHolder.itemView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_rect_btn_blue_empty));
        }else {
            viewHolder.itemView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_rect_btn_gray_empty));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem=position;
                notifyDataSetChanged();
                if (mOnItemLitener!=null){
                    mOnItemLitener.onItemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public interface OnItemListener{
        void onItemClick(View view,int position);
    }
    private OnItemListener mOnItemLitener;

    public void setOnItemClickLitener(OnItemListener mOnItemLitener) {
        this.mOnItemLitener = mOnItemLitener;
    }
}
