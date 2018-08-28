package com.henry.common.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 描述
 **/

public class PageDto {
    @ApiModelProperty(value = "当前页", name = "当前页", example="1")
    private int pageNum;

    @ApiModelProperty(value = "每页的数量,不大于1000", name = "每页的数量,不大于1000", example="10")
    private int pageSize;

    public int getPageNum() {
        if (pageNum < 1) {
            return 1;
        } else {
            return pageNum;
        }
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        if (pageSize < 1) {
            return 10;
        } else if (pageSize > 1000) {
            return 1000;
        } else {
            return pageSize;
        }
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @ApiModelProperty(hidden=true)
    @JsonIgnore 
    @JSONField(serialize=false)  
    public int getBegin() {
        return pageSize * (pageNum - 1);
    }
    @JsonIgnore 
    @JSONField(serialize=false)  
    @ApiModelProperty(hidden=true)
    public int getEnd() {
        return pageNum * pageSize;
    }

}
