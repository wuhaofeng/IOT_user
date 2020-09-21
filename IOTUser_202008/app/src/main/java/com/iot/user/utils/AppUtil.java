package com.iot.user.utils;

import com.blankj.utilcode.util.FileUtils;
import com.iot.user.utils.model.UpdateInfo;

public class AppUtil {

    /**
     * apk文件是否存在，apk文件MD5验证是否合法可安装
     * 可安装则不需要下载返回false,相反返回true
     */
    public static boolean isNeedDownload(UpdateInfo updateInfo, String apkPath){
        if (FileUtils.isFileExists(apkPath)){
            if (StringUtils.isBlank(updateInfo.getMd5())){
                return true;
            }else {
                /**equalsIgnoreCase比较的时候忽略字母大小写**/
                if (updateInfo.getMd5().equalsIgnoreCase(FileUtils.getFileMD5ToString(apkPath).toLowerCase())){
                    return false;
                }else {
                    return true;
                }
            }
        }else {
            return true;
        }
    }
}
