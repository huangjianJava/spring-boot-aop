package com.henry.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 基础信息
 **/

@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "代码: 1,成功;2,错误;3,授权失败;4,验证失败;5,版本过期;6,无功能权限;7,无数据权限", name = "代码")
    private int code;

    @ApiModelProperty(value = "子代码", name = "子代码")
    private int codeEx;

    @ApiModelProperty(value = "返回描述", name = "返回描述")
    private String message;

    @ApiModelProperty(value = "开发错误描述", name = "开发错误描述")
    private String devMessage;

    @ApiModelProperty(value = "数据对象", name = "数据对象")
    private T data;
    
    @ApiModelProperty(value = "数据对象", name = "兼容旧格式")
    private T result;

    public static <E> ResultData<E> createErrorResult(E data) {
        return createErrorResult(data, ResultDataConstants.MSG_CODE_ERROR);
    }

    public static <E> ResultData<E> createErrorResult(E data, int codeEx) {
        return createErrorResult(data, codeEx, null);
    }

    public static <E> ResultData<E> createErrorResult(E data, int codeEx, String message) {
        ResultData<E> result = new ResultData<E>(ResultDataConstants.CODE_ERROR,
                ResultDataConstants.MSG_CODE_ERROR, data);
        result.setCodeEx(codeEx);
        if (message != null) {
            result.setMessage(message);
        }
        return result;
    }


    public static <E> ResultData<E> createErrorResult(E data, String message) {
        return createErrorResult(data, message, "");
    }

    public static <E> ResultData<E> createErrorResult(E data, String message, String devMessage) {
        if ((message == null) || (message.length() < 1)) {
            message = ResultDataConstants.MSG_CODE_ERROR;
        }
        ResultData<E> result =
                new ResultData<E>(ResultDataConstants.CODE_ERROR, message, data);
        result.setDevMessage(devMessage);
        return result;
    }

    public static ResultData<String> createAuthFailResult() {
        ResultData<String> resultData = new ResultData<String>(ResultDataConstants.CODE_AUTHFAIL,
                ResultDataConstants.MSG_CODE_AUTHFAIL, "");
        return resultData;
    }

    public static ResultData<String> createTokenLoseResult() {
        ResultData<String> resultData = new ResultData<String>(ResultDataConstants.CODE_AUTHFAIL,
                ResultDataConstants.MSG_CODE_AUTHFAIL_LOSE, "");
        resultData.setCodeEx(ResultDataConstants.TOKEN_LOST);
        return resultData;
    }
    
    public static ResultData<String> createTokenKickResult() {
        ResultData<String> resultData = new ResultData<String>(ResultDataConstants.CODE_AUTHFAIL,
                ResultDataConstants.MSG_CODE_AUTHFAIL_KICK, "");
        resultData.setCodeEx(ResultDataConstants.TOKEN_KICK);
        return resultData;
    }

    public static ResultData<String> createPopedomFailResult() {
        return new ResultData<String>(ResultDataConstants.CODE_NOTPOPEDOM,
                ResultDataConstants.MSG_CODE_NOTPOPEDOM, "");
    }

    public static ResultData<String> createDataPopedomFailResult() {
        return createDataPopedomFailResult(ResultDataConstants.MSG_CODE_NOTDATAPOPEDOM);
    }

    public static ResultData<String> createDataPopedomFailResult(String message) {
        return new ResultData<String>(ResultDataConstants.CODE_NOTDATAPOPEDOM,
                ResultDataConstants.MSG_CODE_NOTDATAPOPEDOM, message);
    }


    public static ResultData<String> createPopedomFailResult(String message) {
        return new ResultData<String>(ResultDataConstants.CODE_NOTPOPEDOM,
                ResultDataConstants.MSG_CODE_NOTPOPEDOM, message);
    }


    public static <E> ResultData<E> createSuccessResult(E data) {
        return new ResultData<E>(ResultDataConstants.CODE_SUCCESS,
                ResultDataConstants.MSG_CODE_SUCCESS, data);
    }
    
    public static <E> ResultData<E> createSuccessResult(E data, String msg) {
        ResultData<E> result = new ResultData<E>(ResultDataConstants.CODE_SUCCESS,
                ResultDataConstants.MSG_CODE_SUCCESS, data);
        if (msg != null) {
            result.setMessage(msg);
        }
        return result;
    }

    public static <E> ResultData<E> createQuerySuccessResult(E data) {
        return new ResultData<E>(ResultDataConstants.CODE_SUCCESS,
                ResultDataConstants.MSG_CODE_SUCCESS, data);
    }

    public static <E> ResultData<E> createValidationError(E data, String msgString,
            String devString) {
        return new ResultData<E>(ResultDataConstants.CODE_VALIDFAIL,
                ResultDataConstants.MSG_CODE_VALIDFAIL, data);
    }

    public static <E> ResultData<E> createAddResult(Boolean flag, E data) {
        if (flag) {
            return createSuccessResult(data);
        } else {
            return createErrorResult(data, ResultDataConstants.ERROR_ADD);
        }
    }

    public static <E> ResultData<E> createAddResult(Boolean flag, E data, String msg) {
        if (flag) {
            return createSuccessResult(data, msg);
        } else {
            return createAddError(data, msg);
        }
    }
    public static <E> ResultData<E> createAddError(E data, String msg) {
        return createErrorResult(data, ResultDataConstants.ERROR_ADD, msg);
    }


    public static <E> ResultData<E> createUpdateResult(Boolean flag, E data) {
        if (flag) {
            return createSuccessResult(data);
        } else {
            return createUpdateError(data, null);
        }
    }
    public static <E> ResultData<E> createUpdateError(E data, String msg) {
        return createErrorResult(data, ResultDataConstants.ERROR_UPDATE, msg);
    }


    public static <E> ResultData<E> createDeleteResult(Boolean flag) {
        E data = null;
        if (flag) {
            return createSuccessResult(data);
        } else {
            return createDeleteError(data, null);
        }
    } 
    public static <E> ResultData<E> createDeleteError(E data, String msg) {
        return createErrorResult(data, ResultDataConstants.ERROR_DELETE, msg);
    }


    public ResultData() {}

    public ResultData(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCodeEx() {
        return codeEx;
    }

    public void setCodeEx(int codeEx) {
        this.codeEx = codeEx;
    }

    public String getDevMessage() {
        return devMessage;
    }

    public void setDevMessage(String devMessage) {
        this.devMessage = devMessage;
    }
    
    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResultData [code=" + code + ", codeEx=" + codeEx + ", message=" + message
                + ", devMessage=" + devMessage + ", data=" + data + ", result=" + result + "]";
    }
}
