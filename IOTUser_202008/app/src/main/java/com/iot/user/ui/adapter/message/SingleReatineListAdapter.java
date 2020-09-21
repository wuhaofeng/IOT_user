package com.iot.user.ui.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.ui.model.message.ReatineInfoMap;

import java.util.List;

public class SingleReatineListAdapter extends BaseAdapter {

    private static final String TAG = "OrganizationsListAdapter";
    private LayoutInflater layoutInflater;
    private List<ReatineInfoMap> mDatas ;
    private Context context;

    public SingleReatineListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    public SingleReatineListAdapter(Context context, List<ReatineInfoMap> mDatas) {
        layoutInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.context = context;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {

        if(mDatas !=null){
            return mDatas.get(position);
        }
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        View view;
        ReatineInfoMap item = mDatas.get(position);
        if (convertView == null|| convertView.getTag() == null) {
            view = layoutInflater.inflate(R.layout.reatine_list_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.tv_reatine_title = view.findViewById(R.id.tv_reatine_title);
            mViewHolder.tv_reatine_content = view.findViewById(R.id.tv_reatine_content);
            mViewHolder.ll_signal = view.findViewById(R.id.ll_signal);
            mViewHolder.iv_signal = view.findViewById(R.id.iv_signal);
            view.setTag(mViewHolder);
        } else {
            view = convertView;
            mViewHolder = (ViewHolder) view.getTag();
        }
        if(item!=null){
            if("信号强度".equals(item.getReatineTitle())){
                mViewHolder.ll_signal.setVisibility(View.VISIBLE);
                mViewHolder.tv_reatine_content.setVisibility(View.GONE);
                double strength = 10;
                try{
                    strength = Double.valueOf(item.getReatineContent());
                }catch (Exception e){
                    strength = 0;
                }
                if(strength < 4){
                    mViewHolder.iv_signal.setImageResource(R.drawable.signal0);
                }else if(strength <10){
                    mViewHolder.iv_signal.setImageResource(R.drawable.signal1);
                }else if(strength < 16){
                    mViewHolder.iv_signal.setImageResource(R.drawable.signal2);
                }else if(strength < 22){
                    mViewHolder.iv_signal.setImageResource(R.drawable.signal3);
                }else if(strength < 28){
                    mViewHolder.iv_signal.setImageResource(R.drawable.signal4);
                }else{
                    mViewHolder.iv_signal.setImageResource(R.drawable.signal5);
                }
            }else{
                mViewHolder.ll_signal.setVisibility(View.GONE);
                mViewHolder.tv_reatine_content.setVisibility(View.VISIBLE);
                mViewHolder.tv_reatine_content.setText(item.getReatineContent());
            }
            mViewHolder.tv_reatine_title.setText(item.getReatineTitle());
        }
        return view;
    }

    class ViewHolder {
        /**
         * title
         */
        TextView tv_reatine_title;
        /**
         * content
         */
        TextView tv_reatine_content;

        LinearLayout ll_signal;

        ImageView iv_signal;

    }

}