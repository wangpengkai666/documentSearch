package com.example.documentseach.rest.controller.v1;

import com.example.documentseach.biz.DocumentManager;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangpengkai
 */
@RestController("searchDocumentController")
@RequestMapping("/search")
@Api(tags = "文档搜索")
public class SearchDocumentController {

    @Autowired
    private DocumentManager documentManager;
}
