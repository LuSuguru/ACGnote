package com.example.lenovo.myfirstapp.model;

/**
 * Created by lenovo on 2016/8/21.
 */
public class MenuItem {
    public MenuItem(String text, boolean isSelected, int icon, int iconSelected, String tag) {
        this.text = text;
        this.isSelected = isSelected;
        this.icon = icon;
        this.iconSelected = iconSelected;
        this.tag = tag;
    }

    public boolean isSelected;
    public String text;
    public int icon;
    public int iconSelected;
    public String tag;
}
