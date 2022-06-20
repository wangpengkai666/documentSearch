package com.example.documentseach.core.impl;

import com.example.documentseach.common.util.ESUtil;
import com.example.documentseach.core.SearchDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangpengkai
 */
@Service
public class SearchDocumentServiceImpl implements SearchDocumentService {
    @Autowired
    private ESUtil esUtil;
}
