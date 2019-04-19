package com.oyf.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Create Time: 2019年04月17日 20:09
 * Create Author: 欧阳飞
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订单分页参数实体类")
public class PageParam {

    @ApiModelProperty(value = "从第几页开始，默认0",dataType = "Integer",example = "0")
    private Integer page = 0;

    @ApiModelProperty(value = "每页显示条数，默认10",dataType = "Integer",example = "10")
    private Integer size = 10;

    @NotBlank(message = "必须输入用户的微信openid")
    @ApiModelProperty(value = "买家微信openid",dataType = "String")
    private String openId;


}
