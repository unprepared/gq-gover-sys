package com.hc.gqgs.mybatis.mapper.business;

import java.util.List;

import com.hc.gqgs.mybatis.po.business.LoginMenu;
import org.apache.ibatis.annotations.Param;



public interface SelectMenuMapper {
	/*
	 * 查询所拥有的
	 */
	List<LoginMenu> selectMenu(@Param("role_code") String roleCode);
	
	/*
	 * 查询所拥有的权限URL
	 */
//	Set<String> selectURL(@Param("roleCode") String roleCode);
}
