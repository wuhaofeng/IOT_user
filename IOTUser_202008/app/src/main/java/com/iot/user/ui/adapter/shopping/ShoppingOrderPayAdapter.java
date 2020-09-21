package com.iot.user.ui.adapter.shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iot.user.R;
import com.iot.user.ui.model.shopping.ShoppingOrderPayModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingOrderPayAdapter extends RecyclerView.Adapter<ShoppingOrderPayAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<ShoppingOrderPayModel>mData =new ArrayList<>();
    public ShoppingOrderPayAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        for (int i=0;i<2;i++){
          if (i==0){
              ShoppingOrderPayModel aipay=new ShoppingOrderPayModel(R.drawable.alipay_logo,"支付宝支付","亿万用户的选择，更快更安全",true);
              mData.add(aipay);
          }else if(i==1){
              ShoppingOrderPayModel aipay=new ShoppingOrderPayModel(R.drawable.weixin_logo,"微信支付","数亿用户都在用，安全可托付",false);
              mData.add(aipay);
          }
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.item_shopping_order_pay, parent, false);
        return new MyViewHolder(inflate);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_pay_title)
        TextView tv_title;
        @BindView(R.id.tv_pay_content)
        TextView tv_content;
        @BindView(R.id.iv_pay_icon)
        ImageView iv_dev_icon;
        @BindView(R.id.iv_button_select_pay)
        ImageView iv_btn_select;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ShoppingOrderPayModel model=mData.get(position);
        holder.iv_dev_icon.setBackgroundDrawable(ContextCompat.getDrawable(context,model.getIcon()));
        if (model.isSelect()) {
            holder.iv_btn_select.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.btn_circle_select));
        }else {
            holder.iv_btn_select.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.btn_circle_unselect));
        }
        holder.tv_title.setText(model.getTitle());
        holder.tv_content.setText(model.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<mData.size();i++){
                    ShoppingOrderPayModel dataModel=mData.get(i);
                    if (i==position){
                        dataModel.setSelect(true);
                    }else {
                        dataModel.setSelect(false);
                    }
                }
                notifyDataSetChanged();
                if (mOnItemLitener!=null) {
                    mOnItemLitener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemListener{
        void onItemClick(View view,int position);
    }
    private OnItemListener mOnItemLitener;

    public void setOnItemClickLitener(OnItemListener mOnItemLitener) {
        this.mOnItemLitener = mOnItemLitener;
    }
}
