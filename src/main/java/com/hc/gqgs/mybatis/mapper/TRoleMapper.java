package com.hc.gqgs.mybatis.mapper;

import com.hc.gqgs.mybatis.po.TRole;
import com.hc.gqgs.mybatis.po.TRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int countByExample(TRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int deleteByExample(TRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int deleteByPrimaryKey(Integer tid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int insert(TRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int insertSelective(TRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    List<TRole> selectByExample(TRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    TRole selectByPrimaryKey(Integer tid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByExampleSelective(@Param("record") TRole record, @Param("example") TRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByExample(@Param("record") TRole record, @Param("example") TRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByPrimaryKeySelective(TRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_role
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByPrimaryKey(TRole record);
}