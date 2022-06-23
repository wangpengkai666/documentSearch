package com.example.documentseach.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @author wangpengkai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private String content;

    private String author;

    private String title;

    private Date timestamp;
}
