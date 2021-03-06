package com.example.xueyuanzhang.growthlog.ui.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xueyuanzhang on 16/7/3.
 */
public class TimeUtil {
    public static String formatTimeInRough(String timeStamp){
        Date date = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
    public static String formatTimeInDetail(String timeStamp){
        Date date = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }
}
