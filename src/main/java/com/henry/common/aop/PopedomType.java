
package com.henry.common.aop;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 描述
 **/

public enum PopedomType {

    /**
     * 不做权限拦截
     */
    None(0),

    /**
     * 查询
     */
    View(10),

    /**
     * 增加
     */
    Insert(11),

    /**
     * 编辑
     */
    Update(12),

    /**
     * 删除
     */
    Delete(13),

    /**
     * 导入
     */
    Import(14),

    /**
     * 导出
     */
    Export(15),

    /**
     * 打印
     */
    Print(16),

    /**
     * 审核
     */
    Audit(17),

    /**
     * 特殊
     */
    Special(18),

    /**
     * 重置密码
     */
    ResetPwd(19)

    ;

    
    private int flag;
    
    PopedomType(int flag) {
      this.flag = flag;
    }
    
    public int getFlag() {
      return flag;
    }
}
