package com.example.documentseach.rest.servlet;


import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = { "/druid/*" }, initParams = {
        // @WebInitParam(name = "allow", value = "127.0.0.1"),
        @WebInitParam(name = "loginUsername", value = "druid"),
        @WebInitParam(name = "loginPassword", value = "druid"),
        @WebInitParam(name = "resetEnable", value = "false") })
public class DruidStatViewServlet extends StatViewServlet implements Servlet {
}
