
package com.henry.common.utils;

import com.github.pagehelper.Page;
import com.henry.common.dto.PageInfo;
import com.henry.common.dto.PaginationInfo;


import java.util.Collection;
import java.util.List;

public class PageUtil {
    public static <T> PaginationInfo getPaginationInfo(List<T> list) {
        PaginationInfo paginationInfo = null;
        if (list != null) {
            if (list instanceof Page) {
                @SuppressWarnings("rawtypes")
                Page page = (Page) list;
                paginationInfo = new PaginationInfo(page.getPageNum(), page.getPageSize(),
                        page.size(), page.getTotal());
            } else if (list instanceof Collection) {
                paginationInfo = new PaginationInfo(1, list.size(), list.size(), list.size());
            }
            
        }
        
        if (list instanceof Collection) {
            if (paginationInfo != null) {
                paginationInfo.setNavigatePages(8);
            }
        }
        return paginationInfo;
    }


    /**
     * 
     * 转换分页列表至另一个Dto列表
     * 
     * @return
     */
    public static <T, V> PageInfo<V> CopyPageList(List<T> list, Class<V> clazz) {
        if (list == null) {
            return null;
        }
        PageInfo<V> resultObj = new PageInfo<V>(CopyDataUtil.copyList(list, clazz));
        resultObj.setPaginationInfo(getPaginationInfo(list));
        return resultObj;
    }

    /**
     * 
     * 根据分页信息与列表得到一个新的分页数据list
     * 
     * @return
     */
    public static <T> PageInfo<T> getPageList(List<T> list, PaginationInfo paginationInfo) {
        PageInfo<T> resultObj = new PageInfo<T>(list);
        resultObj.setPaginationInfo(paginationInfo);
        return resultObj;
    }

    /**
     * 
     * 根据分页信息与列表得到一个新的分页数据list
     * 
     * @return
     */
    public static <T, V> PageInfo<T> getPageList(List<T> list, Page<?> pageList) {
        PaginationInfo paginationInfo = null;
        if (pageList != null) {
            paginationInfo = getPaginationInfo(pageList);
        }
        PageInfo<T> resultObj = new PageInfo<T>(list);
        resultObj.setPaginationInfo(paginationInfo);
        return resultObj;
    }
}
