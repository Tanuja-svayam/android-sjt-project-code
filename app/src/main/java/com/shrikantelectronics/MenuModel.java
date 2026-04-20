package com.shrikantelectronics;

/**
 * Created by anupamchugh on 22/12/17.
 */

public class MenuModel {

    public String menuName, url;
    public boolean hasChildren, isGroup;

    public MenuModel(String menuName, String isGroup, String hasChildren, String url) {

        this.menuName = menuName;
        this.url = url;
        if(isGroup =="Y")
        {
            this.isGroup = true;
        }

        if(isGroup =="N")
        {
            this.isGroup = false;
        }

        if(hasChildren =="Y")
        {
            this.hasChildren = true;
        }

        if(hasChildren =="N")
        {
            this.hasChildren = false;
        }

    }
}
