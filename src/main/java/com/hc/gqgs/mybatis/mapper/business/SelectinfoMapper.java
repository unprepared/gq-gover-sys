package com.hc.gqgs.mybatis.mapper.business;

import java.util.Date;
import java.util.List;

import com.hc.gqgs.mybatis.po.business.SecondVisaPojo;
import org.apache.ibatis.annotations.Param;


public interface SelectinfoMapper {
	
	List<SecondVisaPojo> selectinfo(@Param("state") Integer state, @Param("start") Date start, @Param("end") Date end);
}
