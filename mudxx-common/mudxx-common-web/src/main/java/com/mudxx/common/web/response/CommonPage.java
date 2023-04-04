package com.mudxx.common.web.response;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据封装类
 *
 * @author laiwen
 */
@ApiModel(description = "通用分页返回对象")
public class CommonPage<T> implements Serializable {
    private static final long serialVersionUID = -6201040376900598868L;
    @ApiModelProperty(value = "当前页")
    private Integer pageNum;
    @ApiModelProperty(value = "分页大小")
    private Integer pageSize;
    @ApiModelProperty(value = "分页总数")
    private Integer totalPage;
    @ApiModelProperty(value = "总数")
    private Long total;
    @ApiModelProperty(value = "当前页结果集")
    private List<T> list;

    /**
     * 将PageHelper分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(List<T> list) {
        CommonPage<T> result = new CommonPage<T>();
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        result.setTotalPage(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getList());
        return result;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this, new JSONConfig().setIgnoreNullValue(false));
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
