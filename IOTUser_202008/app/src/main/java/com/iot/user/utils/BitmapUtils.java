package com.iot.user.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.iot.user.app.IOTApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by maning on 16/3/9.
 * <p/>
 * 图片相关的工具类
 */
public class BitmapUtils {

    public static boolean saveBitmapToSD(Bitmap bitmap, String dir, String name, boolean isShowPhotos) {
        File path = new File(dir);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path + "/" + name);
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    fileOutputStream);
            fileOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 其次把文件插入到系统图库
        if (isShowPhotos) {
            try {
                MediaStore.Images.Media.insertImage(IOTApplication.getIntstance().getContentResolver(),
                        file.getAbsolutePath(), name, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            IOTApplication.getIntstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
        }

        return true;
    }

    public static boolean saveResToSD(Context context, int resID, String dir, String name) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

        return saveBitmapToSD(bitmap, dir, name, false);
    }

    /**
     * 生成二维码图片
     * @param content
     * @return
     */
    public static Bitmap createQrBitmapFromString(String content) {
        try {
            //二维码QR_CODE
            BarcodeFormat fomt = BarcodeFormat.QR_CODE;
            //编码转换
            String a = new String(content.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(a, fomt, 300, 300);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixel = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (bitMatrix.get(j, i)) {
                        pixel[i * width + j] = 0xff000000;
                    }
                }
            }
            Bitmap bmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bmap.setPixels(pixel, 0, width, 0, 0, width, height);
            return bmap;
        } catch (Exception e) {
            return null;
        }

    }

}
