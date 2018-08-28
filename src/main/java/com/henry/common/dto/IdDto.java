
package com.henry.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 描述
 **/

@ApiModel
public class IdDto {
	
	@NotNull(message="{v.id.not.empty}")
    @ApiModelProperty(value = "长整型", name = "数据Id" , required = true )
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
