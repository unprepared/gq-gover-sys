package com.hc.gqgs.json;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "分页信息")
public class Pagination {

	@ApiModelProperty(required = true, name = "pageCurrent", value = "当前选择页", dataType = "int")
	private Integer pageCurrent;
	@ApiModelProperty(required = true, name = "pageSize", value = "每页条数", dataType = "int")
	private Integer pageSize;
	
	
	public Integer getPageCurrent() {
		return pageCurrent;
	}


	public void setPageCurrent(Integer pageCurrent) {
		this.pageCurrent = pageCurrent;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	@Override
	public String toString() {
		return "Pagination [pageCurrent=" + pageCurrent + ", pageSize=" + pageSize + "]";
	}

	
	
}
