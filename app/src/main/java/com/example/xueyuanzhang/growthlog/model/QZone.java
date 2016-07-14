package com.example.xueyuanzhang.growthlog.model;

/**
 * Created by 26096 on 2016/7/14.
 */
public class QZone {
    private Integer zoneID;
    private String zoneName;
    private Integer createrID;
    private String memberIDS;
    private String memberNames;
    public Integer getZoneID() {
        return zoneID;
    }
    public void setZoneID(Integer zoneID) {
        this.zoneID = zoneID;
    }
    public String getZoneName() {
        return zoneName;
    }
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
    public Integer getCreaterID() {
        return createrID;
    }
    public void setCreaterID(Integer createrID) {
        this.createrID = createrID;
    }
    public String getMemberIDS() {
        return memberIDS;
    }
    public void setMemberIDS(String memberIDS) {
        this.memberIDS = memberIDS;
    }
    public String getMemberNames() {
        return memberNames;
    }
    public void setMemberNames(String memberNames) {
        this.memberNames = memberNames;
    }

}
