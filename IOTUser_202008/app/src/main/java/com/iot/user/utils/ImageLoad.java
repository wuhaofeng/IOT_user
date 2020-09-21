package com.iot.user.utils;

import android.content.Context;
import android.widget.ImageView;

import com.iot.user.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by shucheng.qu on 2017/8/24.
 */

public class ImageLoad {

    public static void loadPlaceholder(Context context, String url, Target target) {

        Picasso picasso = new Picasso.Builder(context).loggingEnabled(true).build();
        picasso.load(url)
//                .placeholder(R.drawable.moren)
//                .error(R.drawable.moren)
                .transform(new ImageTransform())
//                .transform(new CompressTransformation())
                .into(target);
    }

}
