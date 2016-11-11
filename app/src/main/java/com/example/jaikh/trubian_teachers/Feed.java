package com.example.jaikh.trubian_teachers;

/**
 * Created by girish on 11/6/2016.
 */

public class Feed {

    private String title;
    private String branch;
    private String desc;
    private String image;
    private String by;
    private long on;

    public Feed(){


    }

    public Feed(String title, String branch, String desc, String image,String by, long on) {
        this.title = title;
        this.branch = branch;
        this.desc = desc;
        this.image = image;
        this.by = by;
        this.on = on;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public long getOn() {
        return on;
    }

    public void setOn(long on) {
        this.on = on;
    }


}
