package com.example.documentseach.core.impl;

import com.example.documentseach.common.util.ESUtil;
import com.example.documentseach.core.StoreDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangpengkai
 */
@Service
public class StoreDocumentServiceImpl implements StoreDocumentService {
    @Autowired
    private ESUtil esUtil;
}
