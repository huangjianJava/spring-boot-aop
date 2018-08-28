/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: BarCodeUtil
 * Author:   temp
 * Date:     2018/5/15 15:42
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.henry.common.utils;

import java.util.Calendar;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author temp
 * @create 2018/5/15
 * @since 1.0.0
 */
public class BarCodeUtil {
    /**
     * 获取商品编码<br>
     * 编码规则:年份最后两位+前缀5位数(商品二级分类编码)+后缀五位数(当前二级分类下累计发布的个数)
     *
     * @param prefix
     *            前缀部分(商品二级分类编码),长度为首位不为0的5位阿拉伯数字
     *            目前 前缀部分 商品一级分类编码,长度为首位不为0的2位阿拉伯数字
     * @param suffix
     *            后缀部分(当前二级分类下商品累计发布的个数),长度为首位不为0的5位阿拉伯数字
     *            目前 后缀部分(当前spu商品ID),长度为7位阿拉伯数字
     * @return
     * @throws Exception
     */
    public static String buildBarCode(String prefix, String suffix) {
        // 检测参数合法性
        //checkParams(prefix, suffix);
        StringBuffer sb = new StringBuffer();
        /*sb.append(getYearStamp());*/
        sb.append(prefix);
        sb.append(getLenCompletion(suffix));
        return sb.toString();
    }

    /**
     * 获取维基商品编码<br>
     * 编码规则:年份最后两位+前缀7位数(商品编码)+后缀三位数(当前sup商品下得计数累加)
     *
     * @param prefix
     *
     *            目前 前缀部分 商品编码,长度为首位不为0的9位阿拉伯数字
     * @param suffix
     *            目前 后缀部分(当前spu商品下得wik数累加),长度为4位阿拉伯数字
     * @return
     * @throws Exception
     */
    public static String buildWikCode(String prefix, String suffix) {
        // 检测参数合法性
        //checkParams(prefix, suffix);
        StringBuffer sb = new StringBuffer();
        sb.append(getYearStamp());
        sb.append(prefix);
        sb.append(suffix);
        return sb.toString();
    }

    /**
     * 检测参数是否合法
     *
     * @param prefix
     * @param suffix
     * @throws Exception
     *//*
	public static void checkParams(String prefix, String suffix)
			throws Exception {
		if (StringUtils.isEmpty(prefix)) {
			throw new Exception("参数【prefix】不能为空");
		}
		if (StringUtils.isEmpty(suffix)) {
			throw new Exception("参数【suffix】不能为空");
		}
		if (prefix.startsWith("0")) {
			throw new Exception("参数【prefix】异常,首位不能为0,prefix = " + prefix);
		}
		if (suffix.startsWith("0")) {
			throw new Exception("参数【suffix】异常,首位不能为0,suffix = " + suffix);
		}
		if (prefix.length() != 5) {
			throw new Exception("参数【prefix】长度只能为5位,prefix = " + prefix);
		}
		if (suffix.length() > 5) {
			throw new Exception("参数【suffix】长度不能大于为5位,suffix = " + suffix);
		}
	}*/

    /**
     * 获取当前年份的最后两位
     *
     * @return
     */
    public static String getYearStamp() {
        Calendar now = Calendar.getInstance();
        return (now.get(Calendar.YEAR) + "").substring(2, 4);
    }

    /**
     * 字符串长度补全(最多5位)
     *
     * @param params
     *            待补全的字符串
     * @return
     */
    public static String getLenCompletion(String params) {
        switch (params.length()) {
            case 1:
                params = "000000" + params;
                break;
            case 2:
                params = "00000" + params;
                break;
            case 3:
                params = "0000" + params;
                break;
            case 4:
                params = "000" + params;
                break;
            case 5:
                params = "00" + params;
                break;
            case 6:
                params = "0" + params;
                break;
            default:
                break;
        }
        return params;
    }

    public static void main(String[] args) {
        try {
            System.out.print(buildBarCode("10", "1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}