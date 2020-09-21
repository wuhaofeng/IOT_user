package com.iot.user.ui.adapter.dev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.model.dev.UnitPublicRoomModel;
import com.iot.user.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitProductAdpater extends RecyclerView.Adapter<UnitProductAdpater.MyViewHolder> {
    private Context context;
    private List<UnitPublicRoomModel> mDatas;
    private LayoutInflater layoutInflater;

    public UnitProductAdpater(Context context, List mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }
    public void updateDatas(List<UnitPublicRoomModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = layoutInflater.inflate(R.layout.item_unit_add_product, viewGroup, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
            UnitPublicRoomModel devModel = mDatas.get(position);
            viewHolder.iv_add_room_icon.setBackgroundResource(Integer.valueOf(devModel.getPlace_url()));
            viewHolder.tv_add_room_name.setText(devModel.getPlace_name());
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
        @BindView(R.id.iv_add_room_icon)
        ImageView iv_add_room_icon;
        @BindView(R.id.tv_add_room_name)
        TextView tv_add_room_name;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            iv_add_room_icon.post(new Runnable() {/***放入post线程中防止获取不到width数据***/
            @Override
            public void run() {
                int width=iv_add_room_icon.getMeasuredWidth();
                Util.setWidthHeightWithRatio(iv_add_room_icon,width,1,1);
            }
            });
        }
    }
}
