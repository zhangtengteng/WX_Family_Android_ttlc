package com.xweisoft.wx.family.logic.model;

import java.io.Serializable;

import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

public class MailItem implements Serializable{
	 /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @SerializedName("id")
    private String id = "";
    
    /**
     * 图标url
     */
    @SerializedName("icon")
    private String icon = "";
    
    /**
     * 通讯名字
     */
    @SerializedName("name")
    private String name = "";
    
    /**
     * 通讯内容
     */
    @SerializedName("content")
    private String Content = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
}
