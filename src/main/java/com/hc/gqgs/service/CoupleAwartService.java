package com.hc.gqgs.service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hc.gqgs.json.ERRORDetail;
import com.hc.gqgs.json.WebResult;
import com.hc.gqgs.mybatis.mapper.CoupleAwartMapper;
import com.hc.gqgs.mybatis.mapper.PoliceStationMapper;
import com.hc.gqgs.mybatis.mapper.SecondVisaMapper;
import com.hc.gqgs.mybatis.mapper.WxManageMapper;
import com.hc.gqgs.mybatis.mapper.business.SelectDataMapper;
import com.hc.gqgs.mybatis.po.*;
import com.hc.gqgs.wechat.WxUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//夫妻投靠
@Service
public class CoupleAwartService {
    org.slf4j.Logger logger = LoggerFactory.getLogger(CoupleAwartService.class);
    static final String userURl = "http://kingeid.nat300.top/#/couplerecord?";
    @Autowired
    CoupleAwartMapper coupleAwartMapper;
    @Autowired
    SelectDataMapper selectDataMapper;
    @Autowired
    SecondVisaMapper secondVisaMapper;
    @Autowired
    PoliceStationMapper policeStationMapper;
    @Autowired
    WxManageMapper wxManageMapper;

    public WebResult coupleAwartService(String sign, String openId, String appeidcode, String cCode, String data, String relativePath, String relativePath2,
                                        String hkphotoPath, String idCardPath, String mcPicture3, String picturePath, Integer policeStationId) {
        CoupleAwart coupleAwart = new CoupleAwart();
        coupleAwart.setAppeidcode(appeidcode);
        coupleAwart.setSign(sign);
        coupleAwart.setOpenId(openId);
        coupleAwart.setcCode(cCode);
        coupleAwart.setPath(relativePath);
        coupleAwart.setInfoPath(relativePath2);
        coupleAwart.setHkPhotoPath(hkphotoPath);
        coupleAwart.setIdcardPath(idCardPath);
        coupleAwart.setMcPicture3(mcPicture3);
        coupleAwart.setTime(new Date());
        coupleAwart.setState(0);
        coupleAwart.setData(data);
        coupleAwart.setPicturePath(picturePath);
        coupleAwart.setPoliceStationId(policeStationId);

        if (coupleAwartMapper.insert(coupleAwart) < 1)
            return WebResult.error(ERRORDetail.SQL_0201005);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cCode", cCode);
        return WebResult.success(map);
    }

    /**
     * 获取派出所名字
     *
     * @return
     */
    public WebResult policeStation() {
        PoliceStationExample policeStationExample = new PoliceStationExample();
        List<PoliceStation> policeStations = policeStationMapper.selectByExample(policeStationExample);
        if (policeStations == null || policeStations.size() < 1)
            return WebResult.error(ERRORDetail.COU_0701001);
        List<String> policeName = new ArrayList<>();
        for (PoliceStation policeStation : policeStations) {
            policeName.add(policeStation.getPoliceStation());
        }
        Map<String, Object> data = new HashMap<String, Object>();
        logger.info("警察局名称：" + policeName);
        data.put("policeStation", policeName);
        return WebResult.success(data);
    }

    /**
     * 改变申请记录的状态，审核
     */
    public WebResult awartCheck(String bizId, Integer state, String remark) {
        CoupleAwartExample coupleAwartExample = new CoupleAwartExample();
        CoupleAwartExample.Criteria criteria = coupleAwartExample.createCriteria();
        criteria.andCCodeEqualTo(bizId);
        List<CoupleAwart> coupleAwarts = coupleAwartMapper.selectByExample(coupleAwartExample);
        if (coupleAwarts == null)
            return WebResult.error(ERRORDetail.BUS_0501003);
        coupleAwarts.get(0).setState(state);
        coupleAwarts.get(0).setRemark(remark);
        if (coupleAwartMapper.updateByPrimaryKey(coupleAwarts.get(0)) < 1)
            return WebResult.error(ERRORDetail.SQL_0201005);
        String openId = coupleAwarts.get(0).getOpenId();
        if (1 == state)
            WxUtils.sendToUT(userURl, openId, bizId, "夫妻投靠"); //发送申请成功信息
        else
            WxUtils.sendToUF(userURl, openId, "材料有误", bizId, "夫妻投靠");  //发送申请失败信息
        return WebResult.success();
    }

    /**
     * 夫妻投靠公众号查询业务状态
     */
    public WebResult getResult(String bizId) {
        CoupleAwartExample coupleAwartExample = new CoupleAwartExample();
        CoupleAwartExample.Criteria criteria = coupleAwartExample.createCriteria();
        criteria.andCCodeEqualTo(bizId);
        List<CoupleAwart> coupleAwarts = coupleAwartMapper.selectByExample(coupleAwartExample);
        if (coupleAwarts == null)
            return WebResult.error(ERRORDetail.BUS_0501003);
        CoupleAwart coupleAwart = coupleAwarts.get(0);
        JSONObject datajson = JSONObject.parseObject(coupleAwart.getData());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", coupleAwart.getState());
        map.put("bizId", coupleAwart.getcCode());
        map.put("personalinfo", datajson);
        map.put("domicilephoto", coupleAwart.getHkPhotoPath());
        map.put("idcardphoto", coupleAwart.getIdcardPath());
        map.put("marryphoto", coupleAwart.getMcPicture3());
        map.put("currentphoto", coupleAwart.getPicturePath());
        map.put("photowordpath", coupleAwart.getPath());
        map.put("perinfowordpath", coupleAwart.getInfoPath());
        map.put("time", coupleAwart.getTime());
        map.put("remark", coupleAwart.getRemark());
        return WebResult.success(map);
    }

    /**
     * 判断管理员是否有权限审核
     *
     * @param bizId  业务流水号
     * @param openId
     */
    public boolean checkManager(String bizId, String openId) {
        WxManageExample wxManageExample = new WxManageExample();
        WxManageExample.Criteria criteria = wxManageExample.createCriteria();
        criteria.andWxOpenIdEqualTo(openId);
        criteria.andTypeEqualTo(1);
        List<WxManage> wxManages = wxManageMapper.selectByExample(wxManageExample);
        if (wxManages == null || wxManages.size() < 1)
            return false;
        CoupleAwartExample coupleAwartExample = new CoupleAwartExample();
        CoupleAwartExample.Criteria criteria1 = coupleAwartExample.createCriteria();
        criteria1.andCCodeEqualTo(bizId);
        List<CoupleAwart> coupleAwarts = coupleAwartMapper.selectByExample(coupleAwartExample);
        if(coupleAwarts == null|| coupleAwarts.size()<1)
            return false;
        Integer policstation = coupleAwarts.get(0).getPoliceStationId();
        for(WxManage wxManage:wxManages)
            if (policstation == wxManage.getPolicstation())
                return true;
        return false;
    }

    /**
     * 通过appeidcode获取业务流水号
     *
     * @param appeidcode
     * @return cCode
     * 业务流水号
     */
    public String getBizId(String appeidcode) {
        CoupleAwartExample coupleAwartExample = new CoupleAwartExample();
        CoupleAwartExample.Criteria criteria = coupleAwartExample.createCriteria();
        criteria.andAppeidcodeEqualTo(appeidcode);
        coupleAwartExample.setOrderByClause("time desc");
        List<CoupleAwart> coupleAwarts = coupleAwartMapper.selectByExample(coupleAwartExample);
        if (coupleAwarts == null || coupleAwarts.size() < 1)
            return null;
        return coupleAwarts.get(0).getcCode();

    }

    /**
     * 查询管理员的openId
     *
     * @param busType       业务类型1是夫妻投靠，2是二次签注
     * @param policestation 所属警察局的标识即主键id
     */
    public List<String> getOpenIdArr(Integer busType, Integer policestation) {
        WxManageExample wxManageExample = new WxManageExample();
        WxManageExample.Criteria criteria = wxManageExample.createCriteria();
        criteria.andTypeEqualTo(busType);
        if (policestation != null)
            criteria.andPolicstationEqualTo(policestation);
        List<WxManage> wxManages = wxManageMapper.selectByExample(wxManageExample);
        List<String> openId = new ArrayList<>();
        for (WxManage wxManage : wxManages)
            openId.add(wxManage.getWxOpenId());
        return openId;
    }

}
