package com.liugs.tool.config;

import com.liugs.tool.ability.bo.ExportDataServiceReqBo;
import com.liugs.tool.util.WriteFile;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName AsyncExport
 * @Description
 * @Author liugs
 * @Date 2021/2/25 14:14:59
 */
@Component
public class AsyncExport {

    @Autowired
    private DataSource dataSource;

    private Logger log = LoggerFactory.getLogger(this.getClass());
    //模式名
    private static final String SELECT = "SELECT";
    private static final String FROM = "FROM";
    private static final String INSERT = "INSERT INTO";
    private static final String VALUES = "VALUES";
    private static final String COMMA = ",";
    private static String TABLE_NAME;
    private static List<String> TABLE_INSERT_LIST = new LinkedList<>();
    private StringBuffer LINE_INSERT_SQL = new StringBuffer();

    private Integer FILE_NUMBER = 1;

    @Value("${export.data.path}")
    private String FILE_PATH_PREFIX;
    @Value("${export.data.fetch.size:100}")
    private int FETCH_SIZE;
    @Value("${export.data.cache.max:10000}")
    private Long cacheMax;
    @Value("${export.data.max.record:100000}")
    private Long maxRecord;

    @Async("exportAsyncExecutor")
    public String asyncExport(ExportDataServiceReqBo reqBo) {
        TABLE_NAME = reqBo.getTableName();
        //拼接查询语句
        String sql = sortQuerySql(reqBo);
        String filePath = FILE_PATH_PREFIX + File.separator + reqBo.getTableName() + File.separator + reqBo.getTableName() + ".txt";
        try {
            File targetFile = new File(FILE_PATH_PREFIX + File.separator + reqBo.getTableName());
            if (!targetFile.exists()) {
                FileUtils.forceMkdir(targetFile);
            }
            log.info("本次数据的目录为：{}", targetFile.getPath());
        } catch (Exception e) {
            log.error("根据表名称生产目录异常：" + e.getMessage());
        }

        //执行查询
        try {
            executeSql(sql, filePath);
        } catch (Exception e) {
            log.error("执行查询异常，" + e.getMessage());
            e.printStackTrace();
            return "执行查询异常，" + e.getMessage();
        }
        return "SUCCESS";
    }

    /**
     * 描述 组装查询语句
     * @return java.lang.String
     * @author liugs
     * @date 2021/2/24 16:43:02
     */
    private String sortQuerySql(ExportDataServiceReqBo reqBo) {
        StringBuffer sb= new StringBuffer();
        sb.append(SELECT).append(" ").append(reqBo.getColumns()).append(" ").append(FROM).append(" ").append(TABLE_NAME);
        if (!StringUtils.isEmpty(reqBo.getWhereConditions())) {
            sb.append("WHERE ").append(reqBo.getWhereConditions());
        }
        log.info("拼接得到的SQL语句为：" + sb);
        return sb.toString();
    }

    /**
     * 描述 执行SQL
     * @param sql
     * @param filePath
     * @return void
     * @author liugs
     * @date 2021/2/25 13:19:12
     */
    private void executeSql(String sql, String filePath) throws Exception {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        if (null == statement) {
            log.error("获取声明失败");
            return;
        }
        log.info("====获取声明成功");

        //FETCH_SIZE 的大小视具体情况而定
        statement.setFetchSize(FETCH_SIZE);

        log.info(String.valueOf(statement.getFetchSize()));

        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();

        long begin = System.currentTimeMillis();

        //计数器
        int j = 0;

        int columnCount = metaData.getColumnCount();

        log.info("开始循环");
        while (resultSet.next()) {
            j++;
            StringBuffer columnName = new StringBuffer();
            StringBuffer columnValue = new StringBuffer();
            for (int i = 1; i <= columnCount; i ++) {
                //获取每一列的值，字段名称，字段类型
                String value = resultSet.getString(i);
                String name = metaData.getColumnName(i);
                int type = metaData.getColumnType(i);

                boolean isNumber = Types.SMALLINT == type || Types.INTEGER == type || Types.BIGINT == type || Types.FLOAT == type
                        || Types.DOUBLE == type || Types.NUMERIC == type || Types.DECIMAL == type;

                if (i == columnCount) {
                    columnName.append(name);
                    //字符串类型
                    if (Types.CHAR == type || Types.VARCHAR == type || Types.LONGVARCHAR == type) {
                        if (value == null) {
                            columnValue.append("null");
                        } else {
                            columnValue.append("'").append(value.replaceAll("'", "")).append("'");
                        }
                        //数字类型
                    } else if (isNumber) {
                        if (value == null) {
                            columnValue.append("null");
                        } else {
                            columnValue.append(value);
                        }
                        //时间类型
                    } else if (Types.DATE == type || Types.TIME == type || Types.TIMESTAMP == type) {
                        if (value == null) {
                            columnValue.append("null");
                        } else {
                            columnValue.append("timestamp'").append(value).append("'");
                        }
                        //其他类型
                    } else {
                        if (value == null) {
                            columnValue.append("null");
                        } else {
                            columnValue.append(value);
                        }
                    }
                    //其他列
                } else {
                    columnName.append(name).append(COMMA);
                    //字符串类型
                    if (Types.CHAR == type || Types.VARCHAR == type || Types.LONGVARCHAR == type) {
                        if (value == null) {
                            columnValue.append("null,");
                        } else {
                            columnValue.append("'").append(value.replaceAll("'", "")).append("',");
                        }
                        //数字类型
                    } else if (isNumber) {
                        if (value == null) {
                            columnValue.append("null,");
                        } else {
                            columnValue.append(value).append(COMMA);
                        }
                        //时间类型
                    } else if (Types.DATE == type || Types.TIME == type || Types.TIMESTAMP == type) {
                        if (value == null) {
                            columnValue.append("null,");
                        } else {
                            columnValue.append("timestamp'").append(value).append("',");
                        }
                        //其他类型
                    } else {
                        if (value == null) {
                            columnValue.append("null,");
                        } else {
                            columnValue.append(value).append(COMMA);
                        }
                    }
                }
            }
            //组装插入语句并写入到文件
            assembleInsertSql(columnName, columnValue, resultSet.isLast(), j, filePath);
        }

        long end = System.currentTimeMillis();
        log.info("总耗时:{}，获取到了{}条数据",(end - begin), j);
    }

    /**
     * 描述 组装插入语句
     * @param columnName
     * @param columnValue
     * @param isLast
     * @param j
     * @return void
     * @author liugs
     * @date 2021/2/25 9:37:26
     */
    private void assembleInsertSql(StringBuffer columnName, StringBuffer columnValue, Boolean isLast, int j, String filePath) {
        LINE_INSERT_SQL.append(INSERT).append(" ").append(TABLE_NAME).append(" (").append(columnName).append(") ").append(VALUES).append(" (").append(columnValue).append(");").append(System.lineSeparator());
        //将本条语句加入集合
        TABLE_INSERT_LIST.add(LINE_INSERT_SQL.toString());
        //清空行数据
        LINE_INSERT_SQL.setLength(0);

        //根据设定的值，每到多少条写一次
        if (j % cacheMax == 0){
            log.info("已写入：" + j + "条数据");
            //写数据
            WriteFile.writeFile(TABLE_INSERT_LIST, filePath, j, maxRecord);
            //清空list
            TABLE_INSERT_LIST.clear();
        } else if (isLast && j % cacheMax != 0) {
            log.info("最后一次写入，已写入：" + j + "条数据");
            //写数据
            WriteFile.writeFile(TABLE_INSERT_LIST, filePath);
            FILE_NUMBER = 1;
        }
    }

}
