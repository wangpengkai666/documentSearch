package com.example.documentseach.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangpengkai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "操作信息记录类")
public class OperateRecordDTO extends BaseDTO {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("主题id")
    private Integer topicId;

    @ApiModelProperty("操作具体内容")
    private String content;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("操作类型")
    private String type;

    @ApiModelProperty("操作搜索记录开始时间")
    private Data beginTime;

    @ApiModelProperty("操作搜索记录结束时间")
    private Data endTime;
}
