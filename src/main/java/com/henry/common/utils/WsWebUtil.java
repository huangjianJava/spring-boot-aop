package com.henry.common.utils;

import com.alibaba.fastjson.JSONArray;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class WsWebUtil {

    public static <T> T resolveParamJsonToObject(HttpServletRequest request, String parameter,
                                                 Class<T> clazz) {
        return JsonUtil.fromJson(request.getParameter(parameter), clazz);
    }

    public static <T> List<T> resolveParamListJsonToObject(HttpServletRequest request,
            String parameter, Class<T> clazz) {
        List<T> resultObjList = JSONArray.parseArray(request.getParameter(parameter), clazz);
        return resultObjList;
    }


    /**
     * Response support CORS (Cross-Origin Resource Sharing).
     * 
     * @param response ServletResponse
     */
    public static void supportCORS(ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Method", "GET, POST, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers",
                "Origin, accept, X-Requested-With, Content-Type, Authorization");
        httpResponse.setContentType("text/json;charset=utf-8");
        httpResponse.setCharacterEncoding("UTF-8");
    }
}
