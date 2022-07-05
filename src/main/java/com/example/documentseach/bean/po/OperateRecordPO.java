package com.example.documentseach.bean.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangpengkai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateRecordPO extends BasePO{
    /**
     * 主键
     */
    private Integer id;

    /**
     * 主题id
     */
    private Integer topicId;

    /**
     * 操作描述
     */
    private String  content;

    /**
     * 操作人  邮箱前缀
     */
    private String  operator;

    /**
     * 操作类型
     */
    private String type;
}
