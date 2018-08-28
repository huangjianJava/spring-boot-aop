
package com.henry.result;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 基础信息
 **/
public interface ResultDataConstants {
    final String MSG_CODE_SUCCESS = "成功";
    final String MSG_CODE_ERROR = "错误";
    final String MSG_CODE_AUTHFAIL = "认证失败";
    final String MSG_CODE_AUTHFAIL_LOSE = "认证失效";
    final String MSG_CODE_AUTHFAIL_KICK = "强制下线";
    
    final String MSG_CODE_VALIDFAIL = "校验参数失败";
    final String MSG_CODE_NOTPOPEDOM = "无功能权限";
    final String MSG_CODE_NOTDATAPOPEDOM = "无数据权限";

    
    final int CODE_SUCCESS = 1;
    final int CODE_ERROR = 2;
    final int CODE_AUTHFAIL = 3;
    final int CODE_VALIDFAIL = 4;
    final int CODE_UPDATEVER = 5;
    final int CODE_NOTPOPEDOM = 6;
    final int CODE_NOTDATAPOPEDOM = 7;
  
    /**
     * 1+两位CODE值+3位自定编码
     */
    final int ERROR_QUERY = 102001;
    final int ERROR_ADD = 102002;
    final int ERROR_UPDATE = 102003;
    final int ERROR_DELETE = 102004;
    final int ERROR_OTHER = 102099;

    
    //final int ERROR_TOKEN = 103001;
    final int TOKEN_LOST = 103001;
    final int TOKEN_KICK = 103002;
}
