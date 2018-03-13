package com.hc.gqgs.controller;

import com.hc.gqgs.config.Config;
import com.hc.gqgs.mybatis.mapper.CoupleAwartMapper;
import com.hc.gqgs.mybatis.mapper.PoliceStationMapper;
import com.hc.gqgs.mybatis.po.PoliceStation;
import com.hc.gqgs.mybatis.po.PoliceStationExample;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.jni.File;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@ApiIgnore
public class Test {
    org.slf4j.Logger logger = LoggerFactory.getLogger(SecondVisaController.class);
    @Autowired
    CoupleAwartMapper coupleAwartMapper;
    @Autowired
    PoliceStationMapper policeStationMapper;
    @Autowired
    Config config;
    @RequestMapping("/test1")
    @ResponseBody
    public Object test(HttpServletRequest request) throws IOException {
//        String path = "C:\\Users\\Administrator\\Desktop\\办理户口呈批表.docx";
//        String Hash = DigestUtils.md5Hex(new FileInputStream(path));
        logger.info("测试test1");
        return "test1";
    }

    @RequestMapping("/test2")
    @ResponseBody
    public Object testTwo(HttpServletRequest request) throws IOException {
        logger.info("测试test2");
        return policeStationMapper.selectByExample(null);
    }

}
