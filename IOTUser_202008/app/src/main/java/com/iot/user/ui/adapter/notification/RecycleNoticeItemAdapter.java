package com.iot.user.ui.adapter.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.adapter.base.OnItemClickListener;
import com.iot.user.ui.model.notification.NoticeContentModel;
import com.iot.user.utils.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecycleNoticeItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<NoticeContentModel> mDatas;
    private LayoutInflater layoutInflater;

    public RecycleNoticeItemAdapter(Context context, List<NoticeContentModel> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void updateDatas(List<NoticeContentModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate;
        if (viewType == 1) {
            inflate = layoutInflater.inflate(R.layout.item_notice_null, parent, false);
            return new MyNullHolder(inflate);
        }else {
            inflate = layoutInflater.inflate(R.layout.item_notice_item, parent, false);
            return new MyViewHolder(inflate);

        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        if (viewHolder.getItemViewType() != 1){
            NoticeContentModel noticeContent = mDatas.get(position);

            //图片
//            RequestOptions options = new RequestOptions();
//            options.centerCrop();
//            options.placeholder(R.drawable.notice1);
            int drawableId = R.drawable.icon_notice_normal;
//            if(noticeContent.getPublish_type() == 1){//个人公告
//                drawableId = R.drawable.notice1;
//                ((MyViewHolder)viewHolder).tv_notice_type.setText("个人");
//            }else if(noticeContent.getPublish_type() == 2){//机构公告
//                drawableId = R.drawable.notice2;
//                ((MyViewHolder)viewHolder).tv_notice_type.setText("机构");
//            }else if(noticeContent.getPublish_type() == 3){//组公告
//                drawableId = R.drawable.notice3;
//                ((MyViewHolder)viewHolder).tv_notice_type.setText("群组");
//            }else if(noticeContent.getPublish_type() == 4){//系统公告
//                drawableId = R.drawable.notice4;
//                ((MyViewHolder)viewHolder).tv_notice_type.setText("系统");
//            }else{
//                drawableId = R.drawable.notice2;
            ((MyViewHolder)viewHolder).tv_notice_type.setText("公告");
//            }

            if(noticeContent.getReaded()== 1){/**已读通知***/
                ((MyViewHolder)viewHolder).iv_read.setVisibility(View.GONE);
            }else{
                ((MyViewHolder)viewHolder).iv_read.setVisibility(View.VISIBLE);
            }
            ((MyViewHolder)viewHolder).iv_msg_item.setImageResource(drawableId);
        /*Glide.with(context)
                .load(messageEntity.getThumbnail())
                .apply(options)
                .into(viewHolder.iv_cook_show);*/
            //通知标题
            ((MyViewHolder)viewHolder).tv_title.setText(noticeContent.getPublish_title());
            ((MyViewHolder)viewHolder).tv_user.setText(noticeContent.getNickName());
            //通知内容
            ((MyViewHolder)viewHolder).tv_ingredients.setText(Html.fromHtml(noticeContent.getPublish_text()));
            //通知时间
            ((MyViewHolder)viewHolder).tv_time.setText(DateUtil.getYearDayTime2(noticeContent.getNotice_time()));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onItemClick(view, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getNodata();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_msg_item)
        ImageView iv_msg_item;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_content)
        TextView tv_ingredients;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_user)
        TextView tv_user;
        @BindView(R.id.tv_notice_type)
        TextView tv_notice_type;
        @BindView(R.id.iv_read)
        ImageView iv_read;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MyNullHolder extends RecyclerView.ViewHolder{
        public MyNullHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
