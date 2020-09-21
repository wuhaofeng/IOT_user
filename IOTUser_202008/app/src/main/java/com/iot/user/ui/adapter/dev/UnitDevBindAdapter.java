package com.iot.user.ui.adapter.dev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.ui.model.dev.UnitDevBindModel;

import java.util.List;

public class UnitDevBindAdapter extends BaseAdapter {

    private static final String TAG = "OrganizationsListAdapter";
    private LayoutInflater layoutInflater;
    private List<UnitDevBindModel> mDatas ;
    private Context context;

    public UnitDevBindAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    public UnitDevBindAdapter(Context context, List<UnitDevBindModel> mDatas) {
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
        UnitDevBindModel item = mDatas.get(position);
        if (convertView == null|| convertView.getTag() == null) {
            view = layoutInflater.inflate(R.layout.reatine_list_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.tv_reatine_title = view.findViewById(R.id.tv_reatine_title);
            mViewHolder.tv_reatine_content = view.findViewById(R.id.tv_reatine_content);
            view.setTag(mViewHolder);
        } else {
            view = convertView;
            mViewHolder = (ViewHolder) view.getTag();
        }
        if(item!=null){
            mViewHolder.tv_reatine_title.setText(item.getTitle());
            mViewHolder.tv_reatine_content.setText(item.getContext());
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


    }

}