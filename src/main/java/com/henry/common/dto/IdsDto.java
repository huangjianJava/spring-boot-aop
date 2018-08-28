
package com.henry.common.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 描述
 **/

public class IdsDto {
    @NotBlank(message="{v.id.not.empty}")
    @ApiModelProperty(value = "批量删除拼接的字符串（逗号分隔）", name = "批量删除拼接的字符串（逗号分隔）",required = true )
    private String ids;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
