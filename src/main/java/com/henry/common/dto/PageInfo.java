
package com.henry.common.dto;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 描述
 **/
@SuppressWarnings({"rawtypes"})
public class PageInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<T> list;
    private PaginationInfo paginationInfo;
    
    public PageInfo() {
    }

    /**
     * 包装Page对象
     *
     * @param list
     */
    public PageInfo(List<T> list) {
        this(list, 8);
    }

    public PageInfo(List<T> list, int navigatePages) {
        if (list instanceof Page) {
            Page page = (Page) list;
            this.list = list;
            paginationInfo = new PaginationInfo(page.getPageNum(), page.getPageSize(), page.size(), page.getTotal());
        } else if (list instanceof Collection) {
            this.list = list;
            paginationInfo = new PaginationInfo(1, list.size(), list.size(), list.size());
        }
        
        if (list instanceof Collection) {
            if (paginationInfo != null) {
                paginationInfo.setNavigatePages(navigatePages);
            }
        }
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public PaginationInfo getPaginationInfo() {
        return paginationInfo;
    }

    public void setPaginationInfo(PaginationInfo paginationInfo) {
        this.paginationInfo = paginationInfo;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
