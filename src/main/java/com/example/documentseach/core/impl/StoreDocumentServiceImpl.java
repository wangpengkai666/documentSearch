package com.example.documentseach.core.impl;

import com.example.documentseach.bean.dto.ArticleDTO;
import com.example.documentseach.common.util.ESUtil;
import com.example.documentseach.core.StoreDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangpengkai
 */
@Service
public class StoreDocumentServiceImpl implements StoreDocumentService {
    @Autowired
    private ESUtil esUtil;

    @Override
    public boolean storeDocuments(ArticleDTO articleDTO) {
        return false;
    }

    @Override
    public boolean storeDocuments(List<ArticleDTO> articleDTOs) {
        return false;
    }
}
