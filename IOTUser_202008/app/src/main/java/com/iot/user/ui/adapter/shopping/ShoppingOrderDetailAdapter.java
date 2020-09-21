package com.iot.user.ui.adapter.shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.model.shopping.ShoppingOrderDetailResp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingOrderDetailAdapter extends RecyclerView.Adapter {
    private ShoppingOrderDetailResp mData;
    private Context context;
    private LayoutInflater layoutInflater;
    private TextView timerView;
    //类型标识
    public final int HEAD_finish=100000;
    public final int HEAD_wait=200000;
    public final int HEAD_cancel=300000;
    public final int BODY=400000;
    public ShoppingOrderDetailAdapter(Context context, ShoppingOrderDetailResp mData){
        this.context=context;
        this.mData=mData;
        this.layoutInflater=LayoutInflater.from(context);
    }
    private int headCount=1;
    public int ishead(int postion){
        boolean isHead=headCount!=0&&postion<headCount;
        if (isHead) {
            switch (mData.getStatus()) {
                case "2":
                    return 2;
                case "1":
                    return 1;
                default:
                    return 3;
            }
        }
        return 4;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case HEAD_finish:
                return new HeaderViewHodler(View.inflate(context,R.layout.item_shopping_order_detail_header,null));
            case HEAD_wait:
                return new HeaderWaitViewHodler(View.inflate(context,R.layout.item_shopping_order_detail_head_wait,null));
            case HEAD_cancel:
                return new HeaderCancelViewHodler(View.inflate(context,R.layout.item_shopping_order_detail_head_cancel,null));
            case BODY:
                return new BodyViewHodler(View.inflate(context, R.layout.item_shopping_order_detail_body,null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof HeaderViewHodler) {/**已完成**/
            ((HeaderViewHodler) holder).bottomConnectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {/**联系商家**/
                    if (mOnItemLitener!=null){
                        mOnItemLitener.onConnectClick();
                    }
                }
            });
        }else if(holder instanceof HeaderCancelViewHodler){/**已取消**/
        ((HeaderCancelViewHodler) holder).onDeviceListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {/**前往选择**/
                if (mOnItemLitener!=null){
                    mOnItemLitener.onDeviceListClick();
                }
            }
        });
        ((HeaderCancelViewHodler) holder).bottomConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {/**联系商家**/
                if (mOnItemLitener!=null){
                    mOnItemLitener.onConnectClick();
                }
            }
        });

        }else if(holder instanceof HeaderWaitViewHodler){/**待支付**/
            ((HeaderWaitViewHodler) holder).btn_cancel_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemLitener!=null){
                        mOnItemLitener.onCancelOrderClick(view);
                    }
                }
            });
            ((HeaderWaitViewHodler) holder).btn_pay_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemLitener!=null){
                        mOnItemLitener.onPayOrderClick();
                    }
                }
            });
            ((HeaderWaitViewHodler) holder).bottomConnectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {/**联系商家**/
                    if (mOnItemLitener!=null){
                        mOnItemLitener.onConnectClick();
                    }
                }
            });
            timerView=((HeaderWaitViewHodler) holder).tv_timer;
            ((HeaderWaitViewHodler) holder).tv_wait_time.setText("超过"+mData.getExprie_minute()+"分钟未支付");
        }
        else if(holder instanceof BodyViewHodler)
        {
            initRecycleView((BodyViewHodler)holder,position);
        }
    }

    private void initRecycleView(BodyViewHodler viewHolder,int position){
        RecyclerView recyclerView=viewHolder.recyclerView;
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
        ShoppingOrderDetailItemAdapter itemAdapter=new ShoppingOrderDetailItemAdapter(context,mData.getOrderDetail());
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutFrozen(true);/**下层可以接收到点击事件。*/
    }


    @Override
    public int getItemViewType(int position) {
        switch (ishead(position)){
            case 1:return HEAD_wait;
            case 2:return HEAD_finish;
            case 3:return HEAD_cancel;
            default:return BODY;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class HeaderViewHodler extends RecyclerView.ViewHolder{
        @BindView(R.id.btn_bottom_connect)
        Button bottomConnectBtn;
        public HeaderViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    class HeaderCancelViewHodler extends RecyclerView.ViewHolder{
        @BindView(R.id.btn_device_list)
        Button onDeviceListBtn;
        @BindView(R.id.btn_bottom_connect)
        Button bottomConnectBtn;
        public HeaderCancelViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    class HeaderWaitViewHodler extends RecyclerView.ViewHolder{
        @BindView(R.id.btn_cancel_order)
        Button btn_cancel_order;
        @BindView(R.id.btn_pay_order)
        Button btn_pay_order;
        @BindView(R.id.btn_bottom_connect)
        Button bottomConnectBtn;
        @BindView(R.id.tv_wait_timer)
        TextView tv_timer;
        @BindView(R.id.tv_wait_time)
        TextView tv_wait_time;
        public HeaderWaitViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    class BodyViewHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_shopping_order_detail) RecyclerView recyclerView;
        private Boolean isHaveItemDecoration=false;
        public BodyViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemListener{
        void onDeviceListClick();
        void onConnectClick();
        void onPayOrderClick();
        void onCancelOrderClick(View view);
    }
    private OnItemListener mOnItemLitener;

    public void setOnItemClickLitener(OnItemListener mOnItemLitener) {
        this.mOnItemLitener = mOnItemLitener;
    }

    public void updateTimerView(String timeStr){
        if (null!=timerView){
            timerView.setText(timeStr);
        }
    }

}
