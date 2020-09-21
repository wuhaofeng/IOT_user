package com.iot.user.utils;

/**
 *@name 防双击工具类
 *@author qingchen
 * @version 1.0
 */
public class DoubleClickUtil {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒

    private static final int MIN_CLICK_DELAY_TIME = 1000;

    private static long lastClickTime;

    /**
     * 判断是否可以执行点击事件，基于两次点击小于1s则判定为双击，不执行点击事件
     * @return
     */
    public static boolean canClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    private static long DIFF = 1000;
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }
    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     * @param diff
     * @return
     */
    private static int lastButtonId = -1;
    private static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }


    private static int count = 0;
    public static void clearCount(){
        count = 0;
    }
    public static int getClickTimes(){
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD > 1000) {
            count = 1;
        }else{
            count++;
        }
        lastClickTime = time;
        return count;
    }

}
