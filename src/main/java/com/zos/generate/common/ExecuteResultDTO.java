package com.zos.generate.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "公共响应信息")
public class ExecuteResultDTO {
	@ApiModelProperty(value = "响应消息", example = "成功: success, 失败: exception msg")
	private String msg;
	@ApiModelProperty(value = "响应数据", example = "")
	private Object obj;
	@ApiModelProperty(value = "响应结果", example = "成功: 1, 失败: 0")
	private Integer code;
}
