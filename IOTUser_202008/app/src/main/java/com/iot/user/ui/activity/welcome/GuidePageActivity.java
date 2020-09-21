package com.iot.user.ui.activity.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iot.user.R;
import com.iot.user.base.BaseActivity;
import com.iot.user.databinding.ActivityGuidePageBinding;
import com.iot.user.ui.activity.login.LoginActivity;
import com.iot.user.ui.adapter.welcome.DepthPageTransformer;
import com.iot.user.ui.adapter.welcome.GuidePagePagerAdapter;
import com.iot.user.utils.PrefUtil;

import java.util.ArrayList;
import java.util.List;

public class GuidePageActivity extends BaseActivity<ActivityGuidePageBinding> implements View.OnClickListener{

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_guide_page;
    }
    private ViewPager mIn_vp;
    private LinearLayout mIn_ll;
    private List<View> mViewList;
    private ImageView mLight_dots;
    private int mDistance;
    private ImageView mOne_dot;
    private ImageView mTwo_dot;
    private Button mBtn_next;

    private TextView tv_jump;
    private TextView tv_jump1;


    @Override
    public void initView() {
        mIn_vp = dataBinding.inViewpager;
        mIn_ll = dataBinding.inLl;
        mLight_dots = dataBinding.ivLightDots;
        mBtn_next = dataBinding.btNext;
        tv_jump=dataBinding.tvJump;
        initData();
        mIn_vp.setAdapter(new GuidePagePagerAdapter(mViewList));
        addDots();
        moveDots();
        mIn_vp.setPageTransformer(true,new DepthPageTransformer());
    }

    private void moveDots() {
        mLight_dots.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获得两个圆点之间的距离
                mDistance = mIn_ll.getChildAt(1).getLeft() - mIn_ll.getChildAt(0).getLeft();
                mLight_dots.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });
        mIn_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
                float leftMargin = mDistance * (position + positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLight_dots.getLayoutParams();
                params.leftMargin = (int) leftMargin+20;
                mLight_dots.setLayoutParams(params);
                if(position==1){
                    mBtn_next.setVisibility(View.VISIBLE);
                }
                if(position!=1&&mBtn_next.getVisibility()==View.VISIBLE){
                    mBtn_next.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //页面跳转时，设置小圆点的margin
                float leftMargin = mDistance * position;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLight_dots.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                mLight_dots.setLayoutParams(params);
                if(position==1){
                    mBtn_next.setVisibility(View.VISIBLE);
                }
                if(position!=1&&mBtn_next.getVisibility()==View.VISIBLE){
                    mBtn_next.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void addDots() {
        mOne_dot = new ImageView(this);
        mOne_dot.setImageResource(R.drawable.gray_dot);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 0, 20, 0);
        mIn_ll.addView(mOne_dot, layoutParams);
        mTwo_dot = new ImageView(this);
        mTwo_dot.setImageResource(R.drawable.gray_dot);
        mIn_ll.addView(mTwo_dot, layoutParams);
//        mThree_dot = new ImageView(this);
//        mThree_dot.setImageResource(R.drawable.gray_dot);
//        mIn_ll.addView(mThree_dot, layoutParams);
        setClickListener();

    }

    private void setClickListener() {
        mOne_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIn_vp.setCurrentItem(0);
            }
        });
        mTwo_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIn_vp.setCurrentItem(1);
            }
        });
//        mThree_dot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mIn_vp.setCurrentItem(2);
//            }
//        });
    }

    private void initData() {
        mViewList = new ArrayList<View>();
        LayoutInflater lf = getLayoutInflater().from(GuidePageActivity.this);
        View view1 =lf.inflate(R.layout.welcome_indicator1, null);
        tv_jump1 = view1.findViewById(R.id.tv_ind1);
        View view2 = lf.inflate(R.layout.welcome_indicator2, null);
//        tv_jump2 =(TextView) view2.findViewById(R.id.tv_ind2);
//        View view3 = lf.inflate(R.layout.we_indicator3, null);
        mViewList.add(view1);
        mViewList.add(view2);
//        mViewList.add(view3);
        tv_jump1.setOnClickListener(this);
//        tv_jump2.setOnClickListener(this);
    }

    @Override
    protected void initClickBtn() {
        super.initClickBtn();
        tv_jump1.setOnClickListener(this);
        mBtn_next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:
                jump();
                break;
            case R.id.tv_jump:
            case R.id.tv_ind1:
                jump();
                break;
        }
    }
    private void jump(){
            startActivity(new Intent(GuidePageActivity.this, LoginActivity.class));
            finish();
            return;
    }
}