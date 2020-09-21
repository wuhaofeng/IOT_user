package com.iot.user.ui.adapter.dev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.model.dev.UnitDevChartModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitDevChartAdapter extends RecyclerView.Adapter<UnitDevChartAdapter.MyViewHolder> {
    private Context context;
    private List<UnitDevChartModel> mDatas;
    private LayoutInflater layoutInflater;
    public UnitDevChartAdapter(Context context, List<UnitDevChartModel> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }

    /***AdapterDelegate**/
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_devnum)
        TextView tv_devnum;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.item_sinle_dev_detail_marning, parent, false);
        return new MyViewHolder(inflate);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder,final int position) {
        final UnitDevChartModel devChargeItem = mDatas.get(position);
        viewHolder.tv_devnum.setText(""+position);
        viewHolder.tv_title.setText(devChargeItem.getGasvalue()+"%LEL");
        viewHolder.tv_date.setText(devChargeItem.getDateStr());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public void updateDataWith(List<UnitDevChartModel>mDatas){
        this.mDatas=mDatas;
        notifyDataSetChanged();
    }
}

