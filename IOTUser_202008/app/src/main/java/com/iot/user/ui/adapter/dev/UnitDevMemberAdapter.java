package com.iot.user.ui.adapter.dev;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.model.dev.UnitDevMemberModel;
import com.iot.user.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitDevMemberAdapter extends RecyclerView.Adapter<UnitDevMemberAdapter.MyViewHolder> {
    private Context context;
    private List<UnitDevMemberModel> mDatas;
    private LayoutInflater layoutInflater;
    private List<String> choosen = new ArrayList<>();
    /***0的时候只展示类型1管理员2设备共享3家庭共享*
     *  1展示选择的框
     * **/
    private int mode = MODE_NORMAL;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_DELETE = 1;
    public int getMode() {
        return mode;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }

    public UnitDevMemberAdapter(Context context, List<UnitDevMemberModel> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void updateDatas(List<UnitDevMemberModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public List<String> getChoosen() {
        return choosen;
    }

    public void setChoosen(List<String> choosen) {
        this.choosen = choosen;
    }

    // 用于记录每个RadioButton的状态，并保证只可选一个
    HashMap<String, Boolean> states = new HashMap<String, Boolean>();

    /**
     * 在执行玩转移，删除操作后执行清除，使adapter回归初始状态
     * **/
    public void clearState(){
        if(states!=null){
            states.clear();
        }
        mode = MODE_NORMAL;
        if(choosen!=null){
            choosen.clear();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = layoutInflater.inflate(R.layout.item_dev_user_item, parent, false);

        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
        final UnitDevMemberModel devContactInfo = mDatas.get(position);
        viewHolder.tv_name.setText(devContactInfo.getNickname());
        viewHolder.tv_phone.setText(devContactInfo.getPhone());

        final String userId= Util.getTwoNumFloatWith(devContactInfo.getUserId(),false);
        final int bindType=Double.valueOf(devContactInfo.getBindType()).intValue();

        if(bindType==1){
            viewHolder.tv_isadmin.setVisibility(View.VISIBLE);
            viewHolder.cb_user.setChecked(false);
            viewHolder.cb_user.setVisibility(View.GONE);
        }else if (bindType==3){
            viewHolder.tv_isadmin.setVisibility(View.VISIBLE);
            viewHolder.tv_isadmin.setText("家庭共享");
            viewHolder.tv_isadmin.setTextColor(Color.LTGRAY);
            viewHolder.cb_user.setVisibility(View.GONE);
        }else {
            if (mode == MODE_NORMAL) {
                viewHolder.tv_isadmin.setVisibility(View.VISIBLE);
                viewHolder.tv_isadmin.setText("设备共享");
                viewHolder.tv_isadmin.setTextColor(Color.BLACK);
                viewHolder.cb_user.setVisibility(View.GONE);
            }else if(mode == MODE_DELETE) {
                viewHolder.tv_isadmin.setVisibility(View.GONE);
                viewHolder.cb_user.setVisibility(View.VISIBLE);
            }
        }
        boolean state = false;
        if(states.containsKey(userId)){
            state = states.get(userId);
        }
        final boolean currentState = state;
        viewHolder.cb_user.setChecked(state);
        viewHolder.cb_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!currentState){
                    states.put(userId, true);
                    if(!choosen.contains(userId)){
                        choosen.add(userId);
                    }
                }else{
                    states.put(userId, false);
                    choosen.remove(userId);
                }
                notifyDataSetChanged();
            }
        });
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_phone)
        TextView tv_phone;
        @BindView(R.id.tv_isadmin)
        TextView tv_isadmin;
        @BindView(R.id.cb_user)
        CheckBox cb_user;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
