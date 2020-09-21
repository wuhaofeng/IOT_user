package com.iot.user.ui.adapter.shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.model.shopping.ShoppingOrderListModel;
import com.iot.user.utils.DateUtil;
import com.iot.user.utils.Util;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingOrderListAdapter extends RecyclerView.Adapter<ShoppingOrderListAdapter.MyViewHolder> {
    private Context context;
    private List<ShoppingOrderListModel>mData;
    private LayoutInflater layoutInflater;
    public ShoppingOrderListAdapter(Context context,List<ShoppingOrderListModel>mData){
        this.context=context;
        this.mData=mData;
        this.layoutInflater=LayoutInflater.from(context);
    }

     class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title_num)
        TextView tv_title;
        @BindView(R.id.tv_status)
        TextView tv_status;
        @BindView(R.id.tv_create_time_num)
        TextView tv_create_time;
        @BindView(R.id.tv_product_num)
        TextView tv_product_num;
        @BindView(R.id.tv_product_price)
        TextView tv_product_price;
        @BindView(R.id.rv_shopping_order_list)
        RecyclerView rv_shopping_order_list;
        private Boolean isHaveItemDecoration=false;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View view=layoutInflater.inflate(R.layout.item_shopping_order_list, parent, false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        final ShoppingOrderListModel listModel = mData.get(position);
        viewHolder.tv_title.setText(listModel.getTrade_no());
        viewHolder.tv_status.setText(Util.getOrderListStatus(listModel.getStatus()));
        viewHolder.tv_create_time.setText(DateUtil.getYearTime(Long.parseLong(listModel.getCreate_time())));
        viewHolder.tv_product_num.setText("共"+listModel.getOrderDetailList().size()+"件");
        viewHolder.tv_product_price.setText("¥"+listModel.getTotal_fee());
        initRecycleView(viewHolder,position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemLitener!=null){
                    mOnItemLitener.onItemClick(view,position);
                }
            }
        });
    }
    private void initRecycleView(MyViewHolder viewHolder,int position){
        RecyclerView recyclerView=viewHolder.rv_shopping_order_list;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        if (viewHolder.isHaveItemDecoration==false) {
            DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.line_list_item_decoration));
            recyclerView.addItemDecoration(divider);
            viewHolder.isHaveItemDecoration=true;
        }
        ShoppingOrderListItemAdapter itemAdapter=new ShoppingOrderListItemAdapter(context,mData.get(position).getOrderDetailList());
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutFrozen(true);/**下层可以接收到点击事件。*/
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public interface OnItemListener{
        void onItemClick(View view,int position);
    }
    private OnItemListener mOnItemLitener;

    public void setOnItemClickLitener(OnItemListener mOnItemLitener) {
        this.mOnItemLitener = mOnItemLitener;
    }
}
