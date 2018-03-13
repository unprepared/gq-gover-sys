package com.hc.gqgs.mybatis.mapper;

import com.hc.gqgs.mybatis.po.WxManage;
import com.hc.gqgs.mybatis.po.WxManageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxManageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_manage
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int countByExample(WxManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_manage
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int deleteByExample(WxManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_manage
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_manage
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int insert(WxManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_manage
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int insertSelective(WxManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_manage
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    List<WxManage> selectByExample(WxManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_manage
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    WxManage selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_manage
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByExampleSelective(@Param("record") WxManage record, @Param("example") WxManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_manage
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByExample(@Param("record") WxManage record, @Param("example") WxManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_manage
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByPrimaryKeySelective(WxManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_manage
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    int updateByPrimaryKey(WxManage record);
}