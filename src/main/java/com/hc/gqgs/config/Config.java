package com.hc.gqgs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:application.yml"}, encoding = "utf-8")
public class Config {

    //获取Word存放的地址
    @Value("${resources.path}")
    private String path;
    public String getPath() {
        return path;
    }
    //去模板文件的地址
    @Value("${resources.templatePath}")
    private String templatePath;
    public String getTemplatePath() {
        return templatePath;
    }
    //获取交警数据
    @Value("${resources.valitePath}")
    private String valitePath;
    public String getValitePath(){
        return valitePath;
    }

    //获取SIMeID的一些配置
    @Value("${SIMeID.SERVER_URL}")
    private String SERVER_URL;
    public String getSERVER_URL(){
        return SERVER_URL;
    }
    @Value("${SIMeID.SERVER_PORT}")
    private int SERVER_PORT;
    public int getSERVER_PORT(){
        return SERVER_PORT;
    }
    @Value("${SIMeID.SERVER_CONTEXT}")
    private String SERVER_CONTEXT;
    public String getSERVER_CONTEXT(){
        return SERVER_CONTEXT;
    }

    //开启eID的钥匙
    @Value("${resources.eIDON}")
    private boolean eIDON;
    public boolean getEIDON(){
        return eIDON;
    }
}
