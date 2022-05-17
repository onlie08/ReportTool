package com.ch.report.bean;

import java.io.Serializable;

public class ValueBean implements Serializable {
    /**
     *类型
     * 1：存款
     * 2：发卡
     * 3：基保理
     * 4：信用卡
     * 5：网金
     */
    private int type;

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 备注
     */
    private String info;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
