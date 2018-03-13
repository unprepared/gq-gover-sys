package com.hc.gqgs.mybatis.mapper;

import com.hc.gqgs.mybatis.po.UserInfo;
import com.hc.gqgs.mybatis.po.UserInfoExample;
import com.hc.gqgs.mybatis.po.UserInfoWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int countByExample(UserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int deleteByExample(UserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int deleteByPrimaryKey(String bizId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int insert(UserInfoWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int insertSelective(UserInfoWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    List<UserInfoWithBLOBs> selectByExampleWithBLOBs(UserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    List<UserInfo> selectByExample(UserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    UserInfoWithBLOBs selectByPrimaryKey(String bizId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByExampleSelective(@Param("record") UserInfoWithBLOBs record, @Param("example") UserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByExampleWithBLOBs(@Param("record") UserInfoWithBLOBs record, @Param("example") UserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByExample(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByPrimaryKeySelective(UserInfoWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByPrimaryKeyWithBLOBs(UserInfoWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByPrimaryKey(UserInfo record);
}