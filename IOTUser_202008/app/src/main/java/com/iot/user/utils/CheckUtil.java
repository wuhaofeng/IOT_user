package com.iot.user.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("\\d{15,16}");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

}
