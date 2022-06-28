package com.example.documentseach.core;

import com.example.documentseach.bean.dto.ArticleDTO;

import java.util.List;

/**
 * @author wangpengkai
 */
public interface SearchDocumentService {
    /**
     * 搜索单个文档信息
     * @param articleDTO
     * @return
     */
    boolean storeDocuments(ArticleDTO articleDTO);

    /**
     * 存储多个文档信息
     * @param articleDTOs
     * @return
     */
    boolean storeDocuments(List<ArticleDTO> articleDTOs);
}
