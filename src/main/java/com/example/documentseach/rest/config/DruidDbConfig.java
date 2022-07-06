package com.example.documentseach.rest.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author wangpengkai
 */
@Configuration
@MapperScan(value = "com.example.documentseach.persistent.dao.mysql", sqlSessionFactoryRef = "wpkSqlSessionFactory")
public class DruidDbConfig {

    @Bean("wpkDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setBanner(false);
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setIdType(IdType.AUTO);
        globalConfig.setDbConfig(dbConfig);
        return globalConfig;
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {

        PaginationInterceptor page = new PaginationInterceptor();
        page.setDbType(DbType.getDbType("mysql"));
        return page;
    }

    /**
     * 配置SqlSessionFactory.
     *
     * @param dataSource dataSource
     * @return SqlSessionFactory
     * @throws Exception Exception
     */
    @Bean("wpkSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("wpkDataSource") DataSource dataSource) throws Exception {
        // 将SqlSessionFactoryBean 替换为 MybatisSqlSessionFactoryBean， 否则mybatis-plus 提示 Invalid bound statement (not found)
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        // 设置mybatis的xml所在位置
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mybatis/*.xml"));
        bean.setGlobalConfig(globalConfig());
        MybatisConfiguration mc = new MybatisConfiguration();
        mc.setLogImpl(org.apache.ibatis.logging.slf4j.Slf4jImpl.class);
        bean.setConfiguration(mc);

        // 添加分页插件，不加这个，分页不生效
        bean.setPlugins(paginationInterceptor());

        return bean.getObject();
    }
}
