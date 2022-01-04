package com.liugs.tool.test;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TestObj
 * @Description
 * @Author liugs
 * @Date 2021/7/8 14:58:04
 */
public class TestObj implements Serializable {
    private String fieldOne;
    private String fieldTwo;
    private String fieldThree;

    public String getFieldOne() {
        return fieldOne;
    }

    public void setFieldOne(String fieldOne) {
        this.fieldOne = fieldOne;
    }

    public String getFieldTwo() {
        return fieldTwo;
    }

    public void setFieldTwo(String fieldTwo) {
        this.fieldTwo = fieldTwo;
    }

    public String getFieldThree() {
        return fieldThree;
    }

    public void setFieldThree(String fieldThree) {
        this.fieldThree = fieldThree;
    }
}
