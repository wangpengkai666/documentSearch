package com.example.documentseach.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "文章主体信息")
public class ArticleDTO {

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("文章的作者")
    private String author;

    @ApiModelProperty("文章的标题")
    private String title;

    @ApiModelProperty("文章的时间戳")
    private Date timestamp;
}
