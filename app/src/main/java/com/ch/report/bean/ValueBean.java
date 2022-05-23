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
     * 5：对公
     * 6：其他
     */
    private int type;

    /**
     * 名称
     */
    private String name;

    /**
     * 数量
     */
    private String count;

    /**
     * 数量单位
     */
    private String countUnit;


    /**
     * 值
     */
    private String value;


    /**
     * 数值单位
     */
    private String valueUnit;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCountUnit() {
        return countUnit;
    }

    public void setCountUnit(String countUnit) {
        this.countUnit = countUnit;
    }

    public String getValueUnit() {
        return valueUnit;
    }

    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }
}
