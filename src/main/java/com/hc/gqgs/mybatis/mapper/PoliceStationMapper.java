package com.hc.gqgs.mybatis.mapper;

import com.hc.gqgs.mybatis.po.PoliceStation;
import com.hc.gqgs.mybatis.po.PoliceStationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PoliceStationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table police_station
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int countByExample(PoliceStationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table police_station
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int deleteByExample(PoliceStationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table police_station
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int deleteByPrimaryKey(Integer tid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table police_station
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int insert(PoliceStation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table police_station
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int insertSelective(PoliceStation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table police_station
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    List<PoliceStation> selectByExample(PoliceStationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table police_station
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    PoliceStation selectByPrimaryKey(Integer tid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table police_station
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByExampleSelective(@Param("record") PoliceStation record, @Param("example") PoliceStationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table police_station
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByExample(@Param("record") PoliceStation record, @Param("example") PoliceStationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table police_station
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByPrimaryKeySelective(PoliceStation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table police_station
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByPrimaryKey(PoliceStation record);
}