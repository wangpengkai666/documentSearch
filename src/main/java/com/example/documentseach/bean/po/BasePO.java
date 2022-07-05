package com.example.documentseach.bean.po;

import java.io.Serializable;
import java.util.Date;

/**
 * 做持久化PO的基础类别
 * @author wangpengkai
 */
public class BasePO implements Serializable {
    protected Date createTime;

    protected Date updateTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
