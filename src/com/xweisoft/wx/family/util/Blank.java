package com.xweisoft.wx.family.util;

/**
 * @类名:Blank
 * @功能描述:
 * @作者:XuanKe'Huang
 * @时间:2014-11-10 下午5:11:01
 * @Copyright 2014
 */
public class Blank
{
    private String content;
    
    private int mode;
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + mode;
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Blank other = (Blank) obj;
        if (content == null)
        {
            if (other.content != null)
                return false;
        }
        else if (!content.equals(other.content))
            return false;
        if (mode != other.mode)
            return false;
        return true;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public int getMode()
    {
        return mode;
    }
    
    public void setMode(int mode)
    {
        this.mode = mode;
    }
    
    @Override
    public String toString()
    {
        return "Blank [content=" + content + ", mode=" + mode + "]";
    }
    
    public Blank(String content, int mode)
    {
        super();
        this.content = content;
        this.mode = mode;
    }
    
    public Blank()
    {
        super();
        // TODO Auto-generated constructor stub
    }
}
