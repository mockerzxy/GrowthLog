package com.example.xueyuanzhang.growthlog.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xueyuanzhang on 16/7/3.
 */
public class PicPathUtil {
    public static List<String> split(String wholePath){
        List<String> picList = new ArrayList<>();
        for(String path:wholePath.split(",")){
            picList.add(path);
        }
        return picList;
    }
}
