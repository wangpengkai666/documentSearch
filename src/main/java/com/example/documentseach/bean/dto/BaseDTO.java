package com.example.documentseach.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangpengkai
 */
@Data
@ApiModel("DTO基础类")
public abstract class BaseDTO implements Serializable {
    @ApiModelProperty("词条创建时间")
    protected Date createTime;

    @ApiModelProperty("词条更新时间")
    protected Date updateTime;

    private static final long serialVersionUID = 7861489615519826338L;
}
