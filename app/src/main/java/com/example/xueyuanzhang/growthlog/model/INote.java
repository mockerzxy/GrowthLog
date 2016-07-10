package com.example.xueyuanzhang.growthlog.model;

/**
 * Created by xueyuanzhang on 16/7/10.
 */
public class INote {
    private Integer noteID;
    private Integer userID;
    private String content;
    private String date;
    private String photo;
    private String video;
    private String tape;

    public Integer getNoteID() {
        return noteID;
    }
    public void setNoteID(Integer noteID) {
        this.noteID = noteID;
    }
    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getVideo() {
        return video;
    }
    public void setVideo(String video) {
        this.video = video;
    }
    public String getTape() {
        return tape;
    }
    public void setTape(String tape) {
        this.tape = tape;
    }

}