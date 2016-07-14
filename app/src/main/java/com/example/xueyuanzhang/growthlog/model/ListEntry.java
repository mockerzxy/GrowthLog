package com.example.xueyuanzhang.growthlog.model;

/**
 * Created by xueyuanzhang on 16/7/3.
 */
public abstract class ListEntry implements Comparable<ListEntry>{
    public double score;
    abstract String getTime();

    @Override
    public int compareTo(ListEntry another) {
        if(this.score>another.score){
            return 1;
        }else if(this.score<another.score){
            return -1;
        }else{
            return 0;
        }
    }
}
