package com.xxjy.jyyh.jscalljava.jsbean;

import com.blankj.utilcode.util.GsonUtils;

public class ToolBarStateBean {
    private String toolBarBgColor;
    private String toolBarTitleColor;

    public static ToolBarStateBean parseFromString(String beanJson) {
        try {
            return GsonUtils.fromJson(beanJson, ToolBarStateBean.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String getToolBarBgColor() {
        return toolBarBgColor;
    }

    public void setToolBarBgColor(String toolBarBgColor) {
        this.toolBarBgColor = toolBarBgColor;
    }

    public String getToolBarTitleColor() {
        return toolBarTitleColor;
    }

    public void setToolBarTitleColor(String toolBarTitleColor) {
        this.toolBarTitleColor = toolBarTitleColor;
    }
}
