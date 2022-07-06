package com.example.documentseach.core;

import com.example.documentseach.bean.dto.ArticleDTO;

import java.util.List;

/**
 * @author wangpengkai
 */
public interface StoreDocumentService {

    /**
     * 单个存储文档
     * @param articleDTO
     * @return
     */
    boolean storeDocuments(ArticleDTO articleDTO);

    /**
     * 存储多个文档
     * @param articleDTOs
     * @return
     */
    boolean storeDocuments(List<ArticleDTO> articleDTOs);
}
