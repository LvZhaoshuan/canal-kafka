package com.yibao.canaldemo.canal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author houxiurong
 * @date 2019-07-26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableBean implements Serializable {
    /**
     * 数据库
     */
    private String database;
    /**
     * 表
     */
    private String table;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 行数据
     */
    private List<Map> data;

    /**
     * 更新时-行旧数据
     */
    private List<Map> old;

    /**
     * 是否是ddl语句
     */
    private Boolean isDdl;

    /**
     * 操作时间
     */
    private Long es;

    /**
     * 操作时间
     */
    private Long ts;

    private String sql;

    /**
     * 表主键
     */
    private String[] pkName;

    /**
     * 表字段语句
     */
    private Map mysqlType;

    /**
     * 表字段类型
     */
    private Map sqlType;
}
