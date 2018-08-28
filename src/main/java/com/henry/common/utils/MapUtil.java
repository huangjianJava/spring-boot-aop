package com.henry.common.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    public static Map<String, Object> getNewHashMap(Map<String, Object> mapObj, String nameList) {
        Map<String, Object> newMap = new HashMap<String, Object>();
        String[] nameArray = nameList.split(",");
        if (nameArray != null) {
            for (int i = 0; i < nameArray.length; i++) {
                String sName = nameArray[i];
                if (!StringUtil.isEmptyOrNull(sName)) {
                    if (mapObj.containsKey(sName)) {
                        newMap.put(sName, mapObj.get(sName));
                    }
                }
            }
        }
        return newMap;
    }

    public static Map<String, Object> stringToMap(String agr) {
        Map<String, Object> re = new HashMap<String, Object>();
        String[] kvs = agr.split(";");
        for (String kv : kvs) {
            if (!kv.contains(":=")) {
                return null;
            }
            String[] k_v = kv.split(":=");
            re.put(k_v[0], k_v[1]);
        }
        return re;
    }

    /**
     * 获取map指定键的字符串,没有返回缺省值
     */
    public static String getMapStringValue(Map<String, Object> parameter, String keyStr,
            String defValue) {
        if (parameter != null) {
            if (parameter.containsKey(keyStr) && (parameter.get(keyStr) != null)) {
                return parameter.get(keyStr).toString();
            } else {
                return defValue;
            }
        } else {
            return defValue;
        }
    }

    public static Object getMapValue(Map<String, Object> parameter, String keyStr,
            Object defValue) {
        if (parameter != null) {
            return parameter.containsKey(keyStr) ? parameter.get(keyStr) : defValue;
        } else {
            return defValue;
        }
    }

    public static String getMapStringValueByArray(Map<String, Object> parameter, String keyStr,
            String defValue) {
        if (parameter != null) {
            if (parameter.containsKey(keyStr)) {
                @SuppressWarnings("unchecked")
                ArrayList<Object> arys = (ArrayList<Object>) parameter.get(keyStr);
                if ((arys != null) && (arys.size() > 0)) {
                    String values = "";
                    for (int i = 0; i < arys.size(); i++) {
                        if (i < arys.size() - 1) {
                            values += arys.get(i) + ",";
                        } else {
                            values += arys.get(i);
                        }
                    }
                    return values;
                }
            }
        }
        return defValue;
    }

    public static Date getMapDateValue(Map<String, Object> parameter, String keyStr) {
        if (parameter != null) {
            if (parameter.containsKey(keyStr)) {
                String dateStr = getMapStringValue(parameter, keyStr, "");
                if (StringUtil.isEmptyOrNull(dateStr)) {
                    return null;
                } else {
                    try {
                        return DateUtil.parseDayDateByAuto(dateStr);
                    } catch (Exception ex) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取map指定键的值,没有返回缺省值
     */
    public static Integer getMapStringValue(Map<String, Object> parameter, String keyStr,
            Integer defValue) {
        if (parameter != null) {
            try {
                String str = parameter.containsKey(keyStr) ? parameter.get(keyStr).toString()
                        : defValue.toString();
                return Integer.parseInt(str);
            } catch (Exception ex) {
                return defValue;
            }
        } else {
            return defValue;
        }
    }

    /**
     * 获取map指定键的值,没有返回缺省值
     */
    public static Long getMapStringValue(Map<String, Object> parameter, String keyStr,
            Long defValue) {
        if (parameter != null) {
            try {
                String str = parameter.containsKey(keyStr) ? parameter.get(keyStr).toString()
                        : defValue.toString();
                return Long.parseLong(str);
            } catch (Exception ex) {
                return defValue;
            }
        } else {
            return defValue;
        }
    }


    /**
     * 获取map指定键的值,没有返回缺省值
     */
    public static Float getMapStringValue(Map<String, Object> parameter, String keyStr,
            Float defValue) {
        if (parameter != null) {
            try {
                String str = parameter.containsKey(keyStr) ? parameter.get(keyStr).toString()
                        : defValue.toString();
                return Float.parseFloat(str);
            } catch (Exception ex) {
                return defValue;
            }
        } else {
            return defValue;
        }
    }
}
