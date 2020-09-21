package com.iot.user.ui.adapter.room;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.model.main.UnitFamilyRoomModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitRoomListAdapter extends RecyclerView.Adapter<UnitRoomListAdapter.MyViewHolder> {
    private Context context;
    private List<UnitFamilyRoomModel> mDatas;
    private LayoutInflater layoutInflater;

    private Boolean isEdit=false;

    public Map<String,Boolean> selectMap=new HashMap<>();

    public UnitRoomListAdapter(Context context, List mDatas,Boolean isEdit) {
        this.context = context;
        this.mDatas = mDatas;
        this.isEdit=isEdit;
        layoutInflater = LayoutInflater.from(this.context);
    }
    public void updateDatas(List<UnitFamilyRoomModel> mDatas,Boolean isEdit) {
        this.mDatas = mDatas;
        this.isEdit=isEdit;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = layoutInflater.inflate(R.layout.item_unit_room_list, viewGroup, false);
        return new MyViewHolder(inflate);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        UnitFamilyRoomModel roomModel=mDatas.get(position);
        viewHolder.tv_room_name.setText(roomModel.getPlace_name());
        int roomNum=Double.valueOf(roomModel.getDevnumCount()).intValue();
        viewHolder.tv_dev_num.setText(roomNum+"个设备");
        if (isEdit==false){
            viewHolder.iv_unit_next.setVisibility(View.VISIBLE);
            viewHolder.iv_room_select.setVisibility(View.GONE);
        }else {
            viewHolder.iv_unit_next.setVisibility(View.GONE);
            viewHolder.iv_room_select.setVisibility(View.VISIBLE);
            if (selectMap.getOrDefault(""+position,false)==false) {
                viewHolder.iv_room_select.setBackgroundResource(R.drawable.btn_circle_unselect);
            }else {
                viewHolder.iv_room_select.setBackgroundResource(R.drawable.btn_circle_select);
            }
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (isEdit==true) {
                    if (selectMap.getOrDefault("" + position, false) == false) {
                        selectMap.put("" + position, true);
                    } else {
                        selectMap.put("" + position, false);
                    }
                    notifyDataSetChanged();
                }
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
        @BindView(R.id.iv_unit_next)
        ImageView iv_unit_next;
        @BindView(R.id.iv_room_select)
        ImageView iv_room_select;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
