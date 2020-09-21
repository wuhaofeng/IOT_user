package com.iot.user.ui.view.zxing.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.iot.user.R;
import com.iot.user.app.IOTApplication;
import com.iot.user.base.BaseActivity;
import com.iot.user.constant.DevConstants;
import com.iot.user.databinding.ActivityCaptureBinding;
import com.iot.user.http.NetWorkApi;
import com.iot.user.http.model.BaseResponse;
import com.iot.user.http.net.RxUtils;
import com.iot.user.http.request.dev.CommonDevLowJsonRequest;
import com.iot.user.http.request.main.UnitDevDetailRequest;
import com.iot.user.ui.activity.dev.DevDetailGasActivity;
import com.iot.user.ui.activity.dev.UnitDevBindActivity;
import com.iot.user.ui.activity.dev.UnitDevRegisterActivity;
import com.iot.user.ui.model.main.UnitDevDetailModel;
import com.iot.user.ui.model.main.UnitFamilyModel;
import com.iot.user.ui.view.zxing.camera.CameraManager;
import com.iot.user.ui.view.zxing.decoding.CaptureActivityHandler;
import com.iot.user.ui.view.zxing.decoding.InactivityTimer;
import com.iot.user.ui.view.zxing.decoding.RGBLuminanceSource;
import com.iot.user.ui.view.zxing.view.ViewfinderView;
import com.iot.user.utils.AppValidationMgr;
import com.iot.user.utils.DialogUtils;
import com.iot.user.utils.DoubleClickUtil;
import com.iot.user.utils.PrefUtil;
import com.iot.user.utils.MyToast;
import com.iot.user.utils.ToastUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import io.reactivex.observers.DisposableObserver;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class CaptureActivity extends BaseActivity<ActivityCaptureBinding> implements Callback ,EasyPermissions.PermissionCallbacks{
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_capture;
    }
    private Button btnLight;
    private Button btnOpenImage;
    private TextView tv_right;

    private LinearLayout ll_enter;
    private EditText et_devnum;
    private Button btn_ok;

    private boolean playBeep;
    private boolean vibrate;
    private boolean hasSurface;
    private String characterSet;
    private int ifOpenLight = 0;//判断是否开启闪光灯
    private MediaPlayer mediaPlayer;
    private ViewfinderView viewfinderView;
    private CaptureActivityHandler handler;
    private Vector<BarcodeFormat> decodeFormats;
    private InactivityTimer inactivityTimer;
    private static final float BEEP_VOLUME = 0.10f;
    private ImageView back;
    public static final String INTENT_TYPE = "type";
    public static final String SCAN_DEV = "scan_dev";/**直接在主页面处理，已绑定的情况下扫描解绑**/
    public static final String SCAN_DEV_DETAIL = "scan_dev_detail";/**直接在主页面处理，已绑定的情况下扫描跳转详情页**/
    public static final String SCAN_REPAIR = "scan_repair";/**扫描后回退到前activity，用onActivityResult捕捉**/
    private String type;
    @Override
    public void initView() {
        viewfinderView =dataBinding.viewfinderView;
        btnLight =dataBinding.btnLight;
        btnOpenImage =dataBinding.btnOpenimg;
        back =dataBinding.back;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        CameraManager.init(getApplication());
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
//        setListener();
        /***权限申请***/
        methodRequiresTwoPermission();
        //获取启动模式
        type = getIntent().getStringExtra(INTENT_TYPE);
//        initToolBarRightBtn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
        if (handler!=null)
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * Handler scan result
     *
     * @param barcode 获取结果
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        // FIXME
        if (resultString.equals("")) {
            Toast.makeText(CaptureActivity.this, R.string.scan_fail_toast, Toast.LENGTH_SHORT)
                    .show();
        } else {
            if(SCAN_REPAIR.equals(type)){
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("code", resultString);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                CaptureActivity.this.finish();
            }else{
                isDevReg_V902(resultString);
            }
        }
    }

    @SuppressLint("AutoDispose")
    private void isDevReg_V902(String devNo){
        final String devNum= AppValidationMgr.removeStringSpace(devNo,0);
        CommonDevLowJsonRequest request = new CommonDevLowJsonRequest(devNum,
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),
                PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()));//String devnum, String token, String uId
        showProgressDialog();
        NetWorkApi.provideRepositoryData().IsDevReg_V902(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        int errcode=entity.getCode();
                        if(errcode == DevConstants.DEV_UNREGIST){//设备未注册
                            Intent intent = new Intent(CaptureActivity.this, UnitDevRegisterActivity.class);
                            intent.putExtra(UnitDevRegisterActivity.DEV_ID,devNum);
                            intent.putExtra(UnitDevRegisterActivity.REGIST_STATUS,errcode);
                            intent.putExtra("FragemntIsRegister",true);
                            startActivity(intent);
                            CaptureActivity.this.finish();
                        }else if(errcode == DevConstants.DEV_REGISTED_HAS_MASTER){//设备注册，且有主绑人,但未关注,跳转关注设备页面
                            Intent intent = new Intent(CaptureActivity.this, UnitDevBindActivity.class);
                            intent.putExtra(UnitDevBindActivity.DEV_ID,devNum);
                            startActivity(intent);
                            CaptureActivity.this.finish();
                        }else if(errcode == DevConstants.DEV_REGISTED_NO_MASTER){//设备注册，未有主绑人
                            Intent intent = new Intent(CaptureActivity.this, UnitDevRegisterActivity.class);
                            intent.putExtra(UnitDevRegisterActivity.DEV_ID,devNum);
                            intent.putExtra(UnitDevRegisterActivity.REGIST_STATUS,errcode);
                            intent.putExtra("FragemntIsRegister",true);
                            startActivity(intent);
                            CaptureActivity.this.finish();
                        }else if(errcode == DevConstants.DEV_REGISTED_HAS_BIND){//设备已经绑定，跳转设备详情页面
                            if (SCAN_DEV.equals(type)) {
                                getBindDevWithString(devNum);
                            }else {
                                Intent intent=new Intent(CaptureActivity.this, DevDetailGasActivity.class);
                                intent.putExtra("DevNumDetail",devNum);
                                startActivity(intent);
                                CaptureActivity.this.finish();
                            }
                        }else{
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        dissmissProgressDialog();
                        MyToast.showShortToast(e.getMessage());
                    }
                    @Override
                    public void onComplete() {
                        dissmissProgressDialog();
                    }
                });
    }

    @SuppressLint("AutoDispose")
    private void getBindDevWithString(final String devNum){/***扫描后自动解绑\取消关注****/
        UnitDevDetailRequest request=new UnitDevDetailRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum);
        NetWorkApi.provideRepositoryData().postUnitDevInfo(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
                            LinkedTreeMap map=(LinkedTreeMap)entity.getBody();
                            Gson gson=new Gson();
                            String jsonString = gson.toJson(map);
                            UnitDevDetailModel devDetailModel=gson.fromJson(jsonString, UnitDevDetailModel.class);
                            if (Double.valueOf(devDetailModel.getBind_type()).intValue()==1){/***设备主绑人***/
                                unbindDev(devNum);
                            }else{
                                unAttentionDev(devNum);
                            }
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }
    @SuppressLint("AutoDispose")
    private void unbindDev(String devNum){/***解绑并注销***/
        devNum= AppValidationMgr.removeStringSpace(devNum,0);/***去除空格***/
        UnitDevDetailRequest request=new UnitDevDetailRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum);
        NetWorkApi.provideRepositoryData().postUnitDevUnBinder(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
//                            MyToast.showShortToast("解绑成功");
                            restartCamera(0);
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("AutoDispose")
    private void unAttentionDev(String devNum){/***取消关注***/
        devNum= AppValidationMgr.removeStringSpace(devNum,0);/***去除空格***/
        UnitDevDetailRequest request=new UnitDevDetailRequest(PrefUtil.getLoginAccountUid(IOTApplication.getIntstance()),
                PrefUtil.getLoginToken(IOTApplication.getIntstance()),devNum);
        NetWorkApi.provideRepositoryData().postUnitDevUnAttention(request)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe(new DisposableObserver<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse entity) {
                        if (entity.getCode()==0) {
//                            MyToast.showShortToast("取消关注成功");
                            restartCamera(1);
                        }else {
                            MyToast.showShortToast(entity.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取带二维码的相片进行扫描
     */
    public void pickPictureFromAblum(View v) {
        Intent mIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(mIntent, 1);

    }
    /**
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent) 对相册获取的结果进行分析
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Result resultString = scanningImage1(picturePath);
                    if (resultString == null) {
                        Toast.makeText(getApplicationContext(), R.string.scan_parse_error,
                                Toast.LENGTH_LONG).show();
                    } else {

                        String resultImage = resultString.getText();
                        if (resultImage.equals("")) {

                            Toast.makeText(CaptureActivity.this, R.string.scan_fail_toast,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if(SCAN_REPAIR.equals(type)){
                                Intent resultIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("result", resultImage);
                                resultIntent.putExtras(bundle);
                                CaptureActivity.this.setResult(RESULT_OK, resultIntent);
                            }else{
                                isDevReg_V902(resultImage);
                            }

                        }

                        CaptureActivity.this.finish();
                    }

                    break;
                //当从软件设置界面，返回当前程序时候
                case AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE:
                    methodRequiresTwoPermission();
                    //执行Toast显示或者其他逻辑处理操作
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 解析QR图内容
     */
    // 解析QR图片
    private Result scanningImage1(String picturePath) {

        if (TextUtils.isEmpty(picturePath)) {
            return null;
        }

        Map<DecodeHintType, String> hints1 = new Hashtable<DecodeHintType, String>();
        hints1.put(DecodeHintType.CHARACTER_SET, "utf-8");

        // 获得待解析的图片
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result;
        try {
            result = reader.decode(bitmap1, hints1);
            return result;
        } catch (NotFoundException e) {
            Toast.makeText(CaptureActivity.this, R.string.scan_parse_error,
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (ChecksumException e) {
            Toast.makeText(CaptureActivity.this, R.string.scan_parse_error,
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (FormatException e) {
            Toast.makeText(CaptureActivity.this, R.string.scan_parse_error,
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }

    // 是否开启闪光灯
    public void IfOpenLight(View v) {
        ifOpenLight++;

        switch (ifOpenLight % 2) {
            case 0:
                //关闪光灯
                CameraManager.get().closeLight();
                btnLight.setText(getString(R.string.str_open_light));
                break;
            case 1:
                //开闪光灯
                CameraManager.get().openLight();
                btnLight.setText(getString(R.string.str_close_light));
                break;
            default:
                break;
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats,
                    characterSet);
        }
    }
    void restartCamera(int type){
        String title="解绑成功";
        if (type==1){
            title="取消关注成功";
        }
        DialogUtils.showMustConfirmDialog(this,
                title, null, "确定",
                new DialogUtils.OnDialogClickListener() {
                    @Override
                    public void onConfirm() {
                        //更新版本
                        if (handler != null) {
                            handler.restartPreviewAndDecode();
                        }
                    }
                    @Override
                    public void onCancel() {
                    }
                });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {

            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    /***设置右上角的手动输入***/
    private void initToolBarRightBtn(){
        et_devnum = findViewById(R.id.et_devnum);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultString = et_devnum.getText().toString();
                if(resultString!=null && !"".equals(resultString)){
                    isDevReg_V902(resultString);
                }else{
                    MyToast.showShortToast("请输入设备编号");
                }
            }
        });
        tv_right = findViewById(R.id.tv_right);
        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ll_enter!=null){
                    ll_enter.setVisibility(View.VISIBLE);
                }
            }
        });
        ll_enter = findViewById(R.id.ll_enter);
        if(SCAN_DEV.equals(type)){
            tv_right.setVisibility(View.VISIBLE);
        }else{
            tv_right.setVisibility(View.GONE);
        }
    }
    public void hide(View view){
        if(ll_enter!=null){
            ll_enter.setVisibility(View.GONE);
        }
    }

    /**拍照权限申请*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA};/**Manifest.permission.ACCESS_FINE_LOCATION**/
        if (EasyPermissions.hasPermissions(this, perms)) {
//            ToastUtil.showMessage("已经授权");
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "应用时需要照相机权限",
                    100, perms);
        }
    }
    /**
     * 请求权限成功。
     * 可以弹窗显示结果，也可执行具体需要的逻辑操作
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        ToastUtil.showMessage("用户授权成功");
    }
    /**
     * 请求权限失败
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        ToastUtil.showMessage("用户授权失败");
        /**
         　　* 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         　　* 这时候，需要跳转到设置界面去，让用户手动开启。
         　　*/
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this).build().show();
            DialogUtils.showMyDialog(CaptureActivity.this,
                    "提示", "您可以在设置中打开相机服务", "去设置", "取消",
                    new DialogUtils.OnDialogClickListener() {
                        @Override
                        public void onConfirm() {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", CaptureActivity.this.getPackageName(), null);
                            intent.setData(uri);
                            try {
                                CaptureActivity.this.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }
    }

}