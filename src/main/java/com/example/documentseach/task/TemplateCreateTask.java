package com.example.documentseach.task;

import com.alibaba.fastjson2.JSONObject;
import com.example.documentseach.common.util.GetJSONFromTxt;
import com.example.documentseach.common.util.log.KLog;
import com.example.documentseach.common.util.log.LoggerFactory;
import com.example.documentseach.persistent.dao.es.TemplateESDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangpengkai
 */
@Configuration
public class TemplateCreateTask {
    private static final KLog LOGGER = LoggerFactory.getLog(TemplateCreateTask.class);

    @Autowired
    private TemplateESDAO templateESDAO;

    private Set<String> preCreateTemplateName;

    @PostConstruct
    public void init() {
        try {
            // 1.获取预创建模板目录下的所有name
            File file = new File("src/main/java/com/example/documentseach/default/template");
            preCreateTemplateName = Arrays.stream(file.listFiles()).map(File::getName).collect(Collectors.toSet());

            // 2.开始创建所有的初始化预创建模板
            List<String> failCreateTemplateNames = new LinkedList<>();
            for (String templateName : preCreateTemplateName) {
                JSONObject templateStructure = GetJSONFromTxt.readDslFileInJarFile(templateName);
                boolean template = templateESDAO.createTemplate(templateName,
                        templateStructure.getJSONArray("index_patterns").toList(String.class),
                        templateStructure.getJSONObject("mappings"),
                        templateStructure.getJSONObject("settings")
                );

                if (!template) failCreateTemplateNames.add(templateName);
            }
            LOGGER.warn("class=TemplateCreateTask||method=init||the fail add templates is {}", failCreateTemplateNames);
        } catch (Exception e) {
            LOGGER.error("class=TemplateCreateTask||method=init||errMsg={}", e.getMessage());
        }
    }
}
