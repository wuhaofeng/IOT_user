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

public class ShoppingSelectDeviceAdapter extends RecyclerView.Adapter<ShoppingSelectDeviceAdapter.MyViewHolder> {
    private Context context;
    private List<ShoppingDeviceSelectListModel> selectList;
    private Float orderSwitch=0f;
    private LayoutInflater layoutInflater;
    public ShoppingSelectDeviceAdapter(Context context,List<ShoppingDeviceSelectListModel> selectList,Float orderSwitch) {
        this.context = context;
        this.selectList=selectList;
        this.orderSwitch=orderSwitch;
        layoutInflater = LayoutInflater.from(this.context);
    }
    /***AdapterDelegate**/
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_devnum)
        TextView tv_devnum;
        @BindView(R.id.iv_dev_icon)
        ImageView iv_dev_icon;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.tv_select_year_date) TextView tv_select_year;
        @BindView(R.id.iv_button_cancel) ImageView iv_button_cancel;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.item_shopping_select_device, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        final ShoppingDeviceSelectListModel selectModel = selectList.get(position);
        viewHolder.tv_price.setText("¥"+Util.getTwoNumFloatWith(selectModel.getPrice(),true));
        viewHolder.tv_select_year.setText(selectModel.getMark());
        if (orderSwitch==0){
            viewHolder.tv_select_year.setText(Util.getShoppingSelectYear(selectModel.getRenew_year()));
        }
        viewHolder.tv_title.setText(selectModel.getDev_type()+"系列"+selectModel.getName());

        viewHolder.tv_devnum.setText("编  号："+selectModel.getProduct_id());
        viewHolder.iv_button_cancel.setOnClickListener(new View.OnClickListener() {/**删除此条数据**/
            @Override
            public void onClick(View view) {
                if (mOnItemLitener!=null){
                    mOnItemLitener.deleteItemClick(position);
                }
            }
        });
        viewHolder.tv_select_year.setOnClickListener(new View.OnClickListener() {
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
        return selectList.size();
    }

    /***use data***/

    public void updateDatas(List<ShoppingDeviceSelectListModel> mDatas) {
        this.selectList = mDatas;
        notifyDataSetChanged();
    }


    public interface OnItemListener{
        void onItemClick(View view,int position);
        void deleteItemClick(int position);
    }
    private OnItemListener mOnItemLitener;

    public void setOnItemClickLitener(OnItemListener mOnItemLitener) {
        this.mOnItemLitener = mOnItemLitener;
    }
}

