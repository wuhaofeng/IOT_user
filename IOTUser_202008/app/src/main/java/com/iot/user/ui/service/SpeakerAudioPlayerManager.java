package com.iot.user.ui.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;

import com.iot.user.app.IOTApplication;
import com.iot.user.utils.PrefUtil;

/**
 * @qingchen
 * 音频播放服务
 */
public class SpeakerAudioPlayerManager{
    private static MediaPlayer mMediaPlayer = null;

    private static Vibrator mVibrator;

    public static VoiceHintState voiceHintState = VoiceHintState.STATE_NONE;

    private static SpeakerAudioPlayerManager defaultInstance;

    public synchronized static SpeakerAudioPlayerManager getDefaultInstance(){
        if(defaultInstance == null){
            synchronized (SpeakerAudioPlayerManager.class){
                defaultInstance = new SpeakerAudioPlayerManager();
            }
        }
        return defaultInstance;
    }

    /***使用mp3进行设备报警***/
    public static ServiceConnection createConnection(){
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //Log.e("xxx", "onServiceConnected");

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }


    /**
     * 启动火警音及震动提示
     */
    public synchronized void playFireRing(){
        if(PrefUtil.getIsMusicMode(IOTApplication.getIntstance())){
            loopPlayAssetsFile("fire.mp3");
        }
        if(PrefUtil.getIsShockMode(IOTApplication.getIntstance())){
            loopShock();
        }
    }
    /**
     * 启动报警音及震动提示
     */
    public synchronized void playAlarmRing(){
        if(PrefUtil.getIsMusicMode(IOTApplication.getIntstance())){
            loopPlayAssetsFile("sound_alarm.mp3");
        }
        if(PrefUtil.getIsShockMode(IOTApplication.getIntstance())){
            loopShock();
        }
    }

    /**
     * 启动故障音及震动提示
     */
    public synchronized void playFaultRing(){
        if(PrefUtil.getIsMusicMode(IOTApplication.getIntstance())){
            loopPlayAssetsFile("sound_fault.mp3");
        }
        if(PrefUtil.getIsShockMode(IOTApplication.getIntstance())){
            loopShock();
        }
    }

    /**
     * 启动燃气报警音及震动提示
     */
    public synchronized void playGasRing(){
        if(PrefUtil.getIsMusicMode(IOTApplication.getIntstance())){
            loopPlayAssetsFile("sound_gas_alarm.mp3");
        }
        if(PrefUtil.getIsShockMode(IOTApplication.getIntstance())){
            loopShock();
        }
    }

    /**
     * 启动烟雾、燃气复合报警音及震动提示
     */
    public synchronized void playCombineRing(){
        if(PrefUtil.getIsMusicMode(IOTApplication.getIntstance())){
            loopPlayAssetsFile("sound_combine_alarm.mp3");
        }
        if(PrefUtil.getIsShockMode(IOTApplication.getIntstance())){
            loopShock();
        }
    }
    /**
     * 启动烟雾、燃气复合报警音及震动提示
     */
    public synchronized void playSimulationRing(){
        if(PrefUtil.getIsMusicMode(IOTApplication.getIntstance())){
            loopPlayAssetsFile("alarm_simulation.mp3");
        }
        if(PrefUtil.getIsShockMode(IOTApplication.getIntstance())){
            loopShock();
        }
    }

    /**
     * 启动烟雾报警及震动提示
     */
    public synchronized void playSmokeRing(){
        if(PrefUtil.getIsMusicMode(IOTApplication.getIntstance())){
            loopPlayAssetsFile("sound_smoke_alarm.mp3");
        }
        if(PrefUtil.getIsShockMode(IOTApplication.getIntstance())){
            loopShock();
        }
    }

    /**
     * 每秒循环震动，持续
     */
    public void loopShock(){
        mVibrator = (Vibrator) IOTApplication.getIntstance().getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        mVibrator.vibrate(new long[]{300, 100, 100, 1000}, 0);
    }

    public void stopShock(){
        if(mVibrator!=null){
            mVibrator.cancel();
        }
    }

    /**
     * 关闭报警音及振动
     */
    public void stopRing(){
        stopVoiceHint();
        stopShock();
    }

    public void stopMusic(){
        stopVoiceHint();
    }

    public enum VoiceHintState{
        STATE_NONE(0, "state_none"),
        STATE_HINT_KESTART_BEGIN(1, "state_hint_kestart_begin"),
        STATE_HINT_KESTART_END(2, "state_hint_kestart_end"),
        STATE_HINT_KEEND_BEGIN(3, "state_hint_keend_begin"),
        STATE_HINT_KEEND_END(4, "state_hint_keend_end"),
        STATE_HINT_KETIMEOUT_BEGIN(5, "state_hint_ketimeout_begin"),
        STATE_HINT_KETIMEOUT_END(6, "state_hint_ketimeout_end"),
        STATE_HINT_KEERROR_BEGIN(7, "state_hint_keerror_begin"),
        STATE_HINT_KEERROR_END(8, "state_hint_keerror_end"),
        STATE_HINT_BUSY(9, "state_hint_busy"),
        STATE_HINT_INCALL(10, "state_hint_incall");

        private int state;
        private String stateName;
        VoiceHintState(int state, String stateName){
            this.state = state;
            this.stateName = stateName;
        }
    }

    private void playAssetsFile(String filename, MediaPlayer.OnCompletionListener onCompletionListener){
        if(mMediaPlayer != null){
            if(mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setLooping(false);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AssetManager assetManager = IOTApplication.getIntstance().getAssets();
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filename);
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            if(onCompletionListener != null) {
                mMediaPlayer.setOnCompletionListener(onCompletionListener);
            }
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动系统媒体播放，循环播放报警音
     * @param filename
     */
    private void loopPlayAssetsFile(String filename){
        if(mMediaPlayer != null){
            if(mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setLooping(true);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AssetManager assetManager = IOTApplication.getIntstance().getAssets();
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filename);
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopVoiceHint(){
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}

