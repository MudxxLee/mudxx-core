package com.mudxx.common.api;

import com.alibaba.fastjson.TypeReference;

import java.io.Serializable;

/**
 * @author laiw
 * @date 2023/10/19 16:04
 */
public class ApiReturnType<T> implements Serializable {
    private static final long serialVersionUID = -4410310318978826397L;

    public enum Type {
        /**
         * 返回类型枚举
         */
        Class,
        TypeReference,
    }

    private Type type;

    private Class<T> typeClass;

    private TypeReference<T> typeReference;

    public ApiReturnType(Class<T> typeClass) {
        this.type = Type.Class;
        this.typeClass = typeClass;
    }

    public ApiReturnType(TypeReference<T> typeReference) {
        this.type = Type.TypeReference;
        this.typeReference = typeReference;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Class<T> getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    public TypeReference<T> getTypeReference() {
        return typeReference;
    }

    public void setTypeReference(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }
}


