package com.example.xueyuanzhang.growthlog.ui.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xueyuanzhang on 16/7/11.
 */
public class StringUtil {

    public static List<Integer> getId(String member){
        List<Integer> list = new ArrayList<>();
        for(String id:member.split(";")){
            list.add(Integer.valueOf(id));
        }
        return list;
    }
}
