package com.henry.result;

/**
 * @Title: TODO
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Steven Liu 刘洋
 * @date 2018年5月30日 上午11:50:49
 * @version V1.0
 **/
public class ResultDataUtil {

    /**
     * 检测返回结果状态码是否成功
     * 
     * @param resultData
     * @return true 状态码成功
     */
    public static <T> Boolean checkCodeSuccess(ResultData<T> resultData) {
        if (resultData == null || resultData.getCode() != ResultDataConstants.CODE_SUCCESS) {
            return false;
        }
        return true;
    }

    /**
     * 1. 检测返回结果状态码是否成功 2. 检测返回结果data不空
     * 
     * @param resultData
     * @return true 状态码成功, data不空
     */
    public static <T> Boolean checkCodeAndData(ResultData<T> resultData) {
        if (checkCodeSuccess(resultData)) {
            return resultData.getData() == null ? false : true;
        }
        return false;
    }

}
