package com.iot.user.ui.adapter.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.model.main.UnitFamilyRoomModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitHomeRoomAdapter extends RecyclerView.Adapter<UnitHomeRoomAdapter.MyViewHolder> {
    private Context context;
    private List<UnitFamilyRoomModel> mDatas;
    private LayoutInflater layoutInflater;

    public UnitHomeRoomAdapter(Context context, List mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }
    public void updateDatas(List<UnitFamilyRoomModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = layoutInflater.inflate(R.layout.item_unit_home_room, viewGroup, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        UnitFamilyRoomModel roomModel=mDatas.get(position);
        viewHolder.tv_room_name.setText(roomModel.getPlace_name());
        int roomNum=Double.valueOf(roomModel.getDevnumCount()).intValue();
        viewHolder.tv_dev_num.setText(roomNum+"个设备");
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

        @BindView(R.id.tv_room_name)
        TextView tv_room_name;
        @BindView(R.id.tv_dev_num)
        TextView tv_dev_num;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

