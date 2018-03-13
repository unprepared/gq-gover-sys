package com.hc.gqgs.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hc.gqgs.mybatis.mapper.SecondVisaMapper;
import com.hc.gqgs.mybatis.mapper.WxManageMapper;
import com.hc.gqgs.mybatis.po.*;
import com.hc.gqgs.wechat.WxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hc.gqgs.json.ERRORDetail;
import com.hc.gqgs.json.WebResult;

//二次签注
@Service
public class SecondVisaService {
    Logger logger = LoggerFactory.getLogger(SecondVisaService.class);
    static final String userUrl = "http://kingeid.nat300.top/#/visarecord?";

    @Autowired
    SecondVisaMapper secondVisaMapper;
    @Autowired
    WxManageMapper wxManageMapper;

    // 接收微信公众号端的信息
    public WebResult cheeApplicationg(String sign, String openId, String jsonString, Integer xgAndAmMuch, String sCode, String photoPath,
                                      String relativePath, String idCardNo, String appeidcode) {
        photoPath = "SecondVisa/photo/" + sCode + ".png";
        SecondVisa secondVisa = new SecondVisa();
        secondVisa.setSign(sign);
        secondVisa.setAppeidcode(appeidcode);
        secondVisa.setOpenId(openId);
        secondVisa.setsCode(sCode);
        secondVisa.setData(jsonString);
        secondVisa.setPath(relativePath);
        secondVisa.setState(0);
        secondVisa.setPhotopath(photoPath);
        secondVisa.setTime(new Date());
        secondVisa.setXgandammuch(xgAndAmMuch);
        secondVisa.setIdCard(idCardNo);
        if (secondVisaMapper.insert(secondVisa) < 1) {
            return WebResult.error(ERRORDetail.SQL_0201005);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sCode", sCode);
        return WebResult.success(map);
    }

    /**
     * 判断这个身份证号码是否已经存在未审核申请
     */
    public long history(String idCardNo) {
        SecondVisaExample secondVisaExample = new SecondVisaExample();
        SecondVisaExample.Criteria criteria = secondVisaExample.createCriteria();
        criteria.andIdCardEqualTo(idCardNo);
        criteria.andStateEqualTo(0);
        return secondVisaMapper.countByExample(secondVisaExample);
    }

    /**
     * 响应APP查询记录状态
     */
//	public WebResult queryState(String sCode) {
//		SecondVisaExample secondVisaExample = new SecondVisaExample();
//		SecondVisaExample.Criteria criteria = secondVisaExample.createCriteria();
//		criteria.andSCodeEqualTo(sCode);
//		List<SecondVisa> secondVisas = secondVisaMapper.selectByExample(secondVisaExample);
//		if (secondVisas == null || secondVisas.size() == 0)
//			return WebResult.error(ERRORDetail.SQL_0201005);// 查出来的数据为空，记录不存在
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("state", secondVisas.get(0).getState());
//		map.put("remark", secondVisas.get(0).getRemark());
//		map.put("sCode", secondVisas.get(0).getsCode());
//		map.put("orderNum", secondVisas.get(0).getOrdernum());
//		map.put("xgAndAmMuch", secondVisas.get(0).getXgandammuch());
//		return WebResult.success(map);
//	}

    /**
     * 通过appeidcode码获取sCod等信息
     */
    public WebResult getinfo(String bizId) {
        SecondVisaExample secondVisaExample = new SecondVisaExample();
        SecondVisaExample.Criteria criteria = secondVisaExample.createCriteria();
        criteria.andSCodeEqualTo(bizId);
        List<SecondVisa> secondVisas = secondVisaMapper.selectByExample(secondVisaExample);
        if (secondVisas == null)
            return WebResult.error(ERRORDetail.SEC_0801005);// 查出来的数据为空，不存在申请数据
        SecondVisa secondVisa = secondVisas.get(0);
        String data = secondVisa.getData();
        JSONObject datajson = JSONObject.parseObject(data);
        logger.info("打印数据详情data：" + datajson.toJSONString());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", secondVisa.getState());//业务状态
        map.put("remark", secondVisa.getRemark());//失败理由
        map.put("bizId", secondVisa.getsCode());//业务标识
        map.put("orderNum", secondVisa.getOrdernum());//缴费订单号
        map.put("money", secondVisa.getXgandammuch());//缴费总和
        map.put("personalinfo", datajson);//所有数据的集合
        map.put("path", secondVisa.getPath());//生成的文档地址
        map.put("photopath", secondVisa.getPhotopath());//证件照所在地址
        map.put("time", secondVisa.getTime());//申请的时间
        return WebResult.success(map);
    }

    /**
     * 二次签注审核
     */
    public WebResult changeState(String remark, String bizId, Integer state, String orderNum) {
        SecondVisaExample secondVisaExample = new SecondVisaExample();
        SecondVisaExample.Criteria criteria = secondVisaExample.createCriteria();
        criteria.andSCodeEqualTo(bizId);
        criteria.andStateEqualTo(0);
        List<SecondVisa> secondVisas = secondVisaMapper.selectByExample(secondVisaExample);
        if (secondVisas == null || secondVisas.size() < 1)
            return WebResult.error(ERRORDetail.BUS_0501006);
        SecondVisa secondVisa = secondVisas.get(0);
        secondVisa.setState(state);
        secondVisa.setRemark(remark);
        secondVisa.setOrdernum(orderNum);
        if (secondVisaMapper.updateByPrimaryKey(secondVisa) < 1)
            return WebResult.error(ERRORDetail.SQL_0201005);
        String openId = secondVisa.getOpenId();
        if (1 == state)
            WxUtils.sendToUT(userUrl, openId, bizId, "二次签注"); //发送申请成功信息
        else
            WxUtils.sendToUF(userUrl, openId, remark, bizId, "二次签注");  //发送申请失败信息
        return WebResult.success();
    }

    /**
     * 通过appeidcode获取业务流水号
     *
     * @param appeidcode
     * @return sCode
     * 业务流水号
     */
    public String getsCode(String appeidcode) {
        SecondVisaExample secondVisaExample = new SecondVisaExample();
        SecondVisaExample.Criteria criteria = secondVisaExample.createCriteria();
        criteria.andAppeidcodeEqualTo(appeidcode);
        secondVisaExample.setOrderByClause("time desc");
        List<SecondVisa> secondVisas = secondVisaMapper.selectByExample(secondVisaExample);
        if (secondVisas == null || secondVisas.size() < 1)
            return null;
        return secondVisas.get(0).getsCode();
    }

    /**
     * 判断管理员是否有权限审核
     *
     * @param openId
     */
    public boolean checkManager(String openId) {
        WxManageExample wxManageExample = new WxManageExample();
        WxManageExample.Criteria criteria = wxManageExample.createCriteria();
        criteria.andWxOpenIdEqualTo(openId);
        criteria.andTypeEqualTo(2);
        if (wxManageMapper.countByExample(wxManageExample)<1)
            return false;
        return true;
    }

}
