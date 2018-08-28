package com.henry.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @Title:
 * @Description:    会员信息修改登录密码 DTO
 * @author huangj
 * @date 2018/8/22
 * @version V1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPasswordReqDto {

    @ApiModelProperty(value = "会员手机号码")
    @NotEmpty(message = "会员手机号码不能为空")
    private String phone;

    @ApiModelProperty(value = "旧的登录密码")
    @NotEmpty(message = "旧的登录密码不能为空")
    private String oldLoginPassword;

    @ApiModelProperty(value = "新的登录密码")
    @NotEmpty(message = "新的登录密码不能为空")
    private String newLoginPassword;

}