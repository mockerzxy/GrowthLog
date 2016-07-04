package com.example.xueyuanzhang.growthlog.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xueyuanzhang on 16/7/3.
 */
public class Record extends ListEntry{
    private int id;
    private String text;
    private List<String> picList = new ArrayList<>();
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }
}
