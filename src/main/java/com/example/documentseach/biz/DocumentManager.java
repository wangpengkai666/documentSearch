package com.example.documentseach.biz;

import com.example.documentseach.core.SearchDocumentService;
import com.example.documentseach.core.StoreDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangpengkai
 */
@Component
public class DocumentManager {
    @Autowired
    private SearchDocumentService searchDocumentService;

    @Autowired
    private StoreDocumentService storeDocumentService;
}
