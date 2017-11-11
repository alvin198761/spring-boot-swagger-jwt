package org.alvin.swagger.jwt.system.bean;

/**
 * swagger 响应对象
 */
public class SwaggerResponse<T> {

    private boolean success;
    private String msg;
    private T data;

    public SwaggerResponse(T data) {
        this.data = data;
        this.success = true;
        this.data = null;
    }

    public SwaggerResponse(boolean success, String msg) {
        this.msg = msg;
        this.success = false;
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
