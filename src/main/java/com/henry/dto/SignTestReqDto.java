package com.henry.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

/**
 * @Title:
 * @Description:
 * @author huangj
 * @date 2018/8/29
 * @version V1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignTestReqDto {

    @ApiModelProperty(value = "姓名")
    @NotEmpty(message = "姓名不能为空")
    private String name;

    @ApiModelProperty("密码修改")
    @Valid
    private ModifyPasswordReqDto modifyPasswordReqDto;

}