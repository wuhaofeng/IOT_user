package com.iot.user.http.net;

import android.util.Log;

import com.iot.user.app.IOTApplication;
import com.iot.user.http.base.IotHttpService;
import com.iot.user.utils.PrefUtil;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //    public static final String DEFAULT_IP = "pub.shmsiot.top";
    public static final String DEFAULT_IP = "testiot.shmsiot.top";

    public static final String DEFAULT_PORT = "8091";
    public static final String DEFAULT_HTTP = "httpserver";
    public static final String DEFAULT_VERSION =  "v1";
    public static String IOT_BASE_URL = "";

    private Retrofit mRetrofit;
    private static RetrofitClient payInstance;
    private static volatile RetrofitClient v1Instance;
    private OkHttpClient mOkHttpClient;
    public IotHttpService v1HttpService;
    public IotHttpService payHttpService;

    private static final String KEY_STORE_TYPE_BKS = "bks";//证书类型 固定值
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";//证书类型 固定值
    private static final String KEY_STORE_PASSWORD = "123456";// 客户端证书密码
    private static final String KEY_STORE_TRUST_PASSWORD = "123456";//客户端证书库密码

    public static RetrofitClient getV1Instance(){
        if(v1Instance == null){
            synchronized (RetrofitClient.class){
                if(v1Instance == null) {
                    v1Instance = new RetrofitClient(2);
                }
            }
        }
        return v1Instance;
    }

    public synchronized static RetrofitClient getPayInstance(){
        if(payInstance == null){
            synchronized (RetrofitClient.class){
                payInstance = new RetrofitClient(1);
            }
        }
        return payInstance;
    }
    /**
     * 多语言切换时需要对header进行重新初始化
     */
    public static void restoreInstance(){
        payInstance = null;
        v1Instance =null;
    }
    private RetrofitClient(int type) {
        if (type==2){
            mOkHttpClient = getUnsafeOkHttpClient(new TokenInterceptor());/////信任所有证书
            String serverIP = PrefUtil.getServerIP(IOTApplication.getIntstance());
            String serverPort = PrefUtil.getServerPort(IOTApplication.getIntstance());
            String version = PrefUtil.getServerVersion(IOTApplication.getIntstance());
            IOT_BASE_URL =  "http://"+serverIP+":"+serverPort+"/"+ DEFAULT_HTTP+"/"+ version+"/";
            mRetrofit = new Retrofit.Builder().baseUrl(IOT_BASE_URL).addConverterFactory(
                    GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(mOkHttpClient).build();
            v1HttpService = mRetrofit.create(IotHttpService.class);
        }else {
            mOkHttpClient = getUnsafeOkHttpClient(new TokenInterceptor());/////信任所有证书
//        mOkHttpClient = getSafeOkHttpClient(new TokenInterceptor());//https双向认证

            String serverIP = PrefUtil.getServerIP(IOTApplication.getIntstance());
            String serverPort = PrefUtil.getServerPort(IOTApplication.getIntstance());
            String version = PrefUtil.getServerVersion(IOTApplication.getIntstance());
            IOT_BASE_URL = "http://" + serverIP + ":" + serverPort + "/";//+ version+"/";
            //  IOT_BASE_URL =  "http://"+serverIP+":"+serverPort+"/"+ version+"/";
            mRetrofit = new Retrofit.Builder().baseUrl(IOT_BASE_URL).addConverterFactory(
                    GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(mOkHttpClient).build();
            payHttpService = mRetrofit.create(IotHttpService.class);
        }
    }
    /**
     * 设置信任所有证书
     */
    private static OkHttpClient getUnsafeOkHttpClient(Interceptor mInterceptor) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws
                                CertificateException {
                        }
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws
                                CertificateException {
                        }
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(mInterceptor)
                    .sslSocketFactory(sslSocketFactory)
                    .addInterceptor(new ChuckInterceptor(IOTApplication.getIntstance()))/**http打印检查**/
                    .writeTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                    .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                    .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                    .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                    .build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
