package org.alvin.swagger.jwt.system.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * swagger 响应对象
 */
@ApiModel(value = "手机响应结果", description = "响应结果实体")
public class SwaggerResponse<T> {

    @ApiModelProperty(value = "是否成功", notes = "true = 成功，false=不成功", readOnly = true)
    private boolean success;
    @ApiModelProperty(value = "失败原因", notes = "失败内容", readOnly = true)
    private String msg;

    @ApiModelProperty(value = "成功响应结果实体", notes = "数据", readOnly = true)
    private T data;

    public SwaggerResponse() {
    }

    public SwaggerResponse(T data) {
        this.data = data;
        this.success = true;
        this.data = null;
    }

    public SwaggerResponse(boolean success, String msg) {
        this.msg = msg;
        this.success = success;
        this.data = null;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
