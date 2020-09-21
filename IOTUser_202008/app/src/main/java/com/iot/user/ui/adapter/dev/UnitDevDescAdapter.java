package com.iot.user.ui.adapter.dev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.model.dev.UnitDevAlertAddressModel;
import com.iot.user.ui.model.dev.UnitDevAlertNodeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitDevDescAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List mDatas;
    private LayoutInflater layoutInflater;

    private static int AddressType=1;
    private static int NodeType=2;
    public UnitDevDescAdapter(Context context, List mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }
    public void updateDatas(List mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        if (type==AddressType){
            View inflate = layoutInflater.inflate(R.layout.item_unit_dev_desc_addr, viewGroup, false);
            return new MyViewHolder(inflate);
        }else {
            View inflate = layoutInflater.inflate(R.layout.item_unit_dev_desc_node, viewGroup, false);
            return new MyViewHolder2(inflate);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof MyViewHolder){
            UnitDevAlertAddressModel addressModel=(UnitDevAlertAddressModel)mDatas.get(position);
            ((MyViewHolder) viewHolder).tv_dev_desc_addr.setText(addressModel.getAddrName());
        }else if(viewHolder instanceof MyViewHolder2){
            UnitDevAlertNodeModel nodeModel=(UnitDevAlertNodeModel)mDatas.get(position);
            ((MyViewHolder2) viewHolder).tv_dev_desc_node_detail.setText(nodeModel.getAlarmdesc()+nodeModel.getFaultdesc());
            ((MyViewHolder2) viewHolder).tv_dev_desc_node_title.setText(nodeModel.getNodeName());
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

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position) instanceof UnitDevAlertAddressModel){
            return AddressType;
        }else {
            return NodeType;
        }
    }

    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_dev_desc_addr)
        TextView tv_dev_desc_addr;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    class MyViewHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_dev_desc_node_title)
        TextView tv_dev_desc_node_title;
        @BindView(R.id.tv_dev_desc_node_detail)
        TextView tv_dev_desc_node_detail;
        public MyViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
