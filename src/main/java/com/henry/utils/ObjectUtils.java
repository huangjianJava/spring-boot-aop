package com.henry.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author huangj
 * @version V1.0
 * @title: Object 工具类
 * @description:
 * @date 2018/8/28
 */
@SuppressWarnings("all")
public class ObjectUtils {

    private static Logger logger = LoggerFactory.getLogger(ObjectUtils.class);

    /**
     * map转用标识符连接的key=value形式字符串
     * @param map   map 数据源
     * @param connectorFlag 连接的标识符
     * @return
     */
    public static String mapToString(Map<String, String> map,String connectorFlag){
        Map<String, String> sortedParams = new TreeMap<>(map);
        Set<Map.Entry<String, String>> entrySet = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> param : entrySet) {
            stringBuilder.append(param.getKey())
                    .append("=")
                    .append(param.getValue())
                    .append(connectorFlag);
        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    /**
     * Object转Map(利用反射获取类里面的值和名称)
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMap(Object obj){
        try {
            Map<String, String> map = new HashMap<>();
            Class<?> clazz = obj.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                String value = (String) field.get(obj);
                map.put(fieldName, value);
            }
            return map;
        }catch (Exception ex){
            logger.error("ObjectUtils,method:objectToMap,error:" + ex.getMessage());
            return null;
        }
    }

    /**
     * 测试
     * @param map   map 数据源
     * @param connectorFlag 连接的标识符
     * @return
     */
    public static String mapToString2(Map<String, Object> map,String connectorFlag){
        Map<String, Object> sortedParams = new TreeMap<>(map);
        Set<Map.Entry<String, Object>> entrySet = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> param : entrySet) {
            stringBuilder.append(param.getKey())
                    .append("=")
                    .append(param.getValue())
                    .append(connectorFlag);
        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    /**
     * 测试
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap2(Object obj){
        try {
            Map<String, Object> map = new HashMap<>();
            Class<?> clazz = obj.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                map.put(fieldName, value);
            }
            return map;
        }catch (Exception ex){
            logger.error("ObjectUtils,method:objectToMap,error:" + ex.getMessage());
            return null;
        }
    }

}
