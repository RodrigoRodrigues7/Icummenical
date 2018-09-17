package com.example.android.icummenical.Classes;

public class cardItem {

    private int background;
    private String itemTitle;
    private int profilePhoto;

    public cardItem() {
    }

    public cardItem(int background, String itemTitle, int profilePhoto) {
        this.background = background;
        this.itemTitle = itemTitle;
        this.profilePhoto = profilePhoto;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public int getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

}
