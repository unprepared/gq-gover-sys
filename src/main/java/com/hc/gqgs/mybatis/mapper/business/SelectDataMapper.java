package com.hc.gqgs.mybatis.mapper.business;

import java.util.Date;
import java.util.List;

import com.hc.gqgs.mybatis.po.business.CoupleAwartPojo;
import org.apache.ibatis.annotations.Param;


public interface SelectDataMapper {
	List<CoupleAwartPojo> selectData(@Param("policeStationName") String policeStationName, @Param("state") Integer state, @Param("start") Date start, @Param("end") Date end);

}
