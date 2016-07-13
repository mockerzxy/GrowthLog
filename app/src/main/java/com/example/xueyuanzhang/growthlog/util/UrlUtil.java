package com.example.xueyuanzhang.growthlog.util;

/**
 * Created by xueyuanzhang on 16/7/12.
 */
public class UrlUtil {
    public static String getCodeFromUrl(String url){
        int positionTemp=url.indexOf('?');
        int positionStart=positionTemp+1;
        String Code=url.substring(positionStart);
        int position = Code.indexOf("=");
        position = position+1;
        String result = Code.substring(position);
        return result;
    }
}
