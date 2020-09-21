package com.iot.user.http;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.iot.user.http.base.HttpDataSource;
import com.iot.user.http.base.HttpDataSourceImpl;
import com.iot.user.http.base.IotHttpService;
import com.iot.user.http.base.RespositoryData;
import com.iot.user.http.net.RetrofitClient;

public class NetWorkApi {
    private volatile static IotHttpService apiService = null;
    private volatile static HttpDataSourceImpl httpDataSource = null;

    private volatile static IotHttpService apiPayService = null;
    private volatile static HttpDataSourceImpl httpPayDataSource = null;
    public static RespositoryData provideRepositoryData() {/***当defaultUrl中带v1版本号**/
        //网络API服务
        apiService = RetrofitClient.getV1Instance().v1HttpService;
        //网络数据源
        httpDataSource = HttpDataSourceImpl.getInstance(apiService);
        //两条分支组成一个数据仓库
        return RespositoryData.getInstance(httpDataSource);
    }
    public static RespositoryData providePayRepositoryData() {/***当defaultUrl中不带v1版本号**/
        //网络API服务
        apiPayService = RetrofitClient.getPayInstance().payHttpService;
        //网络数据源
        httpPayDataSource = HttpDataSourceImpl.getPayInstance(apiPayService);
        //两条分支组成一个数据仓库
        return RespositoryData.getPayInstance(httpPayDataSource);
    }
    public static void destroyInstance() {
        RetrofitClient.restoreInstance();
        HttpDataSourceImpl.destroyInstance();
        RespositoryData.destroyInstance();
    }

}
