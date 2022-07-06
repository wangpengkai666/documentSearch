package com.example.documentseach.rest.controller.v1;

import com.example.documentseach.biz.DocumentManager;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangpengkai
 */
@RestController("storeDocumentController")
@RequestMapping("/store")
@Api(tags = "文档存储")
public class StoreDocumentController {

    @Autowired
    private DocumentManager documentManager;
}
