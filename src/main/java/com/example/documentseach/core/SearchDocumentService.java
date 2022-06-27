package com.example.documentseach.core;

import com.example.documentseach.bean.dto.ArticleDTO;

/**
 * @author wangpengkai
 */
public interface SearchDocumentService {
    boolean storeDocuments(ArticleDTO articleDTO);
}
