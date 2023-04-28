package com.mudxx.common.web.response;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mudxx.common.exception.code.CommonErrorCode;
import com.mudxx.common.exception.code.IErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 通用返回对象
 *
 * @author laiwen
 */
@ApiModel(description = "通用返回对象")
public class CommonResult<T> implements Serializable {
    private static final long serialVersionUID = -7976826315846369771L;

    /**
     * @description 定义jackson对象
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @ApiModelProperty(value = "错误码")
    private String code;
    @ApiModelProperty(value = "提示信息")
    private String message;
    @ApiModelProperty(value = "返回数据")
    private T data;

    protected CommonResult() {
    }

    protected CommonResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    protected CommonResult(IErrorCode errorCode, T data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this, new JSONConfig().setIgnoreNullValue(false));
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(CommonErrorCode.SUCCESS, data);
    }

    /**
     * 成功返回结果
     */
    public static <T> CommonResult<T> success() {
        return success(null);
    }

    /**
     * 失败返回结果
     *
     * @param code    错误码
     * @param message 提示信息
     * @param data    数据
     */
    public static <T> CommonResult<T> failed(String code, String message, T data) {
        return new CommonResult<>(code, message, data);
    }

    /**
     * 失败返回结果
     *
     * @param code    错误码
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String code, String message) {
        return failed(code, message, null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   提示信息
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode, String message) {
        return failed(errorCode.getCode(), message);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<>(errorCode, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return failed(CommonErrorCode.SYSTEM_ERROR.getCode(), message);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> failed() {
        return failed(CommonErrorCode.SYSTEM_ERROR);
    }

    /**
     * 未登录返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> unauthorized(String message) {
        return failed(CommonErrorCode.UNAUTHORIZED.getCode(), message);
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResult<T> unauthorized() {
        return failed(CommonErrorCode.UNAUTHORIZED);
    }

    /**
     * 未授权返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> forbidden(String message) {
        return failed(CommonErrorCode.FORBIDDEN.getCode(), message);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResult<T> forbidden() {
        return failed(CommonErrorCode.FORBIDDEN);
    }

    /**
     * API找不到返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> notfound(String message) {
        return failed(CommonErrorCode.NOT_FOUND.getCode(), message);
    }

    /**
     * API找不到返回结果
     */
    public static <T> CommonResult<T> notfound() {
        return failed(CommonErrorCode.NOT_FOUND);
    }


    /**
     * 说明：data为null
     *
     * @param json json字符串
     * @return 返回自定义响应对象
     */
    public static <T> CommonResult<T> format(String json) {
        try {
            // json字符串中data为null，将json字符串转换成CommonResult对象
            return MAPPER.readValue(json, CommonResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 说明：data为T类型的单个对象
     *
     * @param json  json字符串
     * @param clazz T类型
     * @return 返回自定义响应对象
     */
    public static <T> CommonResult<T> formatToPojo(String json, Class<T> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(json, CommonResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(json);
            // 获取data的值
            JsonNode data = jsonNode.get("data");
            // obj用来存储data
            T obj = null;
            if (!data.isNull()) {
                // 如果data不为null
                obj = MAPPER.readValue(data.traverse(), clazz);
            }
            return new CommonResult<>(jsonNode.get("code").asText(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 说明：data为T类型对象的集合
     *
     * @param json  json字符串
     * @param clazz T类型
     * @return 返回自定义响应对象
     */
    public static <T> CommonResult<T> formatToList(String json, Class<T> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(json, CommonResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(json);
            // 获取data的值
            JsonNode data = jsonNode.get("data");
            // obj用来存储data
            T obj = null;
            if (data.isArray() && data.size() > 0) {
                // 如果data是数组格式并且长度不为0
                obj = MAPPER.readValue(data.traverse(), MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return new CommonResult<>(jsonNode.get("code").asText(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
