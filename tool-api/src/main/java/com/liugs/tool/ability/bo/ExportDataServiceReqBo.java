package com.liugs.tool.ability.bo;

import java.io.Serializable;

/**
 * @ClassName ExportDataServiceReqBo
 * @Description data export request param
 * @Author liugs
 * @Date 2021/2/27 18:05:18
 */
public class ExportDataServiceReqBo implements Serializable {

    private static final long serialVersionUID = 1285017360389478746L;

    private String columns;

    private String tableName;

    private String whereConditions;


    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWhereConditions() {
        return whereConditions;
    }

    public void setWhereConditions(String whereConditions) {
        this.whereConditions = whereConditions;
    }
}
