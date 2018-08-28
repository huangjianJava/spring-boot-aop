
package com.henry.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 改用BasePageDto
 **/
@ApiModel
public class BaseQueryDto{

  @ApiModelProperty(value = "分页信息", name = "分页信息")
  private PageDto pageInfo;

  public PageDto getPageInfo() {
    return pageInfo;
  }

  public void setPageInfo(PageDto pageInfo) {
    this.pageInfo = pageInfo;
  }

  @JsonIgnore 
  public Integer getPageNumberByDefault() {
    Integer iNum = 1;
    if (pageInfo != null) {
      iNum = pageInfo.getPageNum();
    }
    if (iNum <= 0) {
      iNum = 1;
    }
    return iNum;
  }

  @JsonIgnore 
  public Integer getPageSizeByDefault() {
    Integer iNum = 10;
    if (pageInfo != null) {
      iNum = pageInfo.getPageSize();
    }
    if (iNum <= 0) {
      iNum = 10;
    }
    return iNum;
  }
}
