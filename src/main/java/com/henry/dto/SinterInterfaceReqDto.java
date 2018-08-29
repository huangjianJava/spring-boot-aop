package com.henry.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author huangj
 * @version V1.0
 * @Title:  Sinter 对外接口统一入参 dto
 * @Description:
 * @date 2018/8/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SinterInterfaceReqDto {

    @ApiModelProperty(value = "用户ID")
    @NotEmpty(message = "用户ID不能为空")
    private String businessID;

    @ApiModelProperty(value = "请求/返回数据类型")
    @NotEmpty(message = "请求/返回数据类型不能为空")
    private String dataType;

    @ApiModelProperty(value = "时间戳")
    @NotEmpty(message = "时间戳不能为空")
    private String timestamp;

    @ApiModelProperty(value = "请求内容")
    @NotEmpty(message = "请求内容不能为空")
    private String requestData;

    @ApiModelProperty(value = "数据内容签名")
    @NotEmpty(message = "数据内容签名不能为空")
    private String dataSign;

}