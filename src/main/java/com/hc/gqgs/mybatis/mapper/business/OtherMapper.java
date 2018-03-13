package com.hc.gqgs.mybatis.mapper.business;

import com.hc.gqgs.mybatis.po.RadioQuestion;

import java.util.List;

public interface OtherMapper {

	List<RadioQuestion> selRand();

	RadioQuestion selOne(String[] questionIds);
	
}
