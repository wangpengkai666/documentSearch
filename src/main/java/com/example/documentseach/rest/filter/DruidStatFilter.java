package com.example.documentseach.rest.filter;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

@WebFilter(filterName = "druidWebStatFilter", urlPatterns = { "/*" }, initParams = { @WebInitParam(name = "exclusions", value = "*.js,*.jpg,*.png,*.gif,*.ico,*.css,/druid/*") })
public class DruidStatFilter extends WebStatFilter {

}
