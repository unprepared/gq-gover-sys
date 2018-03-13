package com.hc.gqgs.controller;

import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.data.PictureRenderData;
import com.hc.gqgs.config.Config;
import com.hc.gqgs.json.ERRORDetail;
import com.hc.gqgs.json.WebResult;
import com.hc.gqgs.service.CoupleAwartService;
import com.hc.gqgs.service.SecondVisaService;
import com.hc.gqgs.tools.SIMeID.SIMeIDService;
import com.hc.gqgs.tools.StringUtil;
import com.hc.gqgs.tools.ToWord;
import com.hc.gqgs.wechat.DefaultWxTokenRepo;
import com.hc.gqgs.wechat.WxImgUtil;
import com.hc.gqgs.wechat.WxUtils;
import io.swagger.annotations.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//二次签注
@Controller
//@ApiIgnore()
@Api(value = "SecondVisaController", tags = {"二次签注的所有接口"})
public class SecondVisaController {
    org.slf4j.Logger logger = LoggerFactory.getLogger(SecondVisaController.class);
    static DefaultWxTokenRepo wxToken = DefaultWxTokenRepo.getInstance();
    //发送信息给管理员的地址
    static  final  String managerUrl = "http://kingeid.nat300.top/#/visaverify?verifyOpenId=";

    @Autowired
    SecondVisaService secondVisaService;
    @Autowired
    Config config;
    @Autowired
    CoupleAwartService coupleAwartService;
    @Autowired
    SIMeIDService sIMeIDService;

    // 接收客户端发过来的数据生成相应材料
    @RequestMapping(value = "/visa/secondvisacontroller", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "二次签注审数据提交", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "所有数据都在data里面，数据项描述例子：{openId:每个微信用户的唯一标识,name:名字,surname:拼音姓,spellName:拼音名,sex:性别,nation:民族,birthplace:出生地(江西省抚州市宜黄县棠阴镇),domicile:户口所在地(江西省抚州市宜黄县棠阴镇),idCardNo:身份证号码,brithtime:出生年月(String类型1995年05月28日),"
                    + "telphone:电话号码,uName:紧急联系人,uTelphone:紧急联系号码,postMethods:收件方式(int 1,2;方式1自取，收件人、收件号码，收件地址,邮编可空,键一定要有，值为空；方式2、邮寄回去,收件人、收件号码，收件地址邮编不可空),recipient:收件人,rTelphone:收件人号码,rAddress:收件地址,postcode:邮编,"
                    + "money:应收总金额,mediaId:图片ID,visaTypeHk:香港签证类型(只能为0,1,2,3，分别代表没有选、三月一次、一年一次、一年两次),visaTypeMacao:澳门签注类型(只能为0,1,2,分别代表没有选、三月一次、一年一次)}", required = true, paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "{\"errCode\":\"00\",\"errMsg\":\"0000000(成功)\",\"data\":{\"sCode\":sCode}}")})
    public Object secondVisaController(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        String SIMeIDphone = (String) session.getAttribute("telphone");
        if(SIMeIDphone == null || "".equals(SIMeIDphone))
            return WebResult.error(ERRORDetail.GQ_0103004);
        // 生成的表存放地址，从resources配置文件中读取出来的
        String path = config.getPath() + "SecondVisa\\";
        // 存在就不创建，不存在则创建文件夹
        File file = new File(path, "CoupleAwart");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // 图片名，生成的文档名
        String sCode = UUID.randomUUID().toString().replace("-", "");
        // 图片存放地址
        String photopath = path + "photo\\";
        File photofile = new File(photopath, "photo");
        if (!photofile.getParentFile().exists()) {
            photofile.getParentFile().mkdirs();
        }

        String dataStr = request.getParameter("data");
        logger.info("二次签注所有数据："+dataStr);
        JSONObject dataJSON = JSONObject.parseObject(dataStr);
        //每个微信用户的唯一标识openId
        String openId = dataJSON.getString("openId");
        if (StringUtil.isEmptyOrNull(openId)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 姓名
        String name = dataJSON.getString("name");
        if (StringUtil.isEmptyOrNull(name)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 拼音姓
        String surname = dataJSON.getString("surname");
        if (StringUtil.isEmptyOrNull(surname)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 拼音名
        String spellName = dataJSON.getString("spellName");
        if (StringUtil.isEmptyOrNull(spellName)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 性别
        String sex = dataJSON.getString("sex");
        if (StringUtil.isEmptyOrNull(sex)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 民族
        String nation = dataJSON.getString("nation");
        if (StringUtil.isEmptyOrNull(sex)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 出生地
        String birthplace = dataJSON.getString("birthplace");
        if (StringUtil.isEmptyOrNull(birthplace)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 户口所在地
        String domicile = dataJSON.getString("domicile");
        if (StringUtil.isEmptyOrNull(domicile)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 身份证号码
        String idCardNo = dataJSON.getString("idCardNo");
        if (StringUtil.isEmptyOrNull(idCardNo)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 判断这个身份证号码是否已经提交了有效的未审核的申请
        if (secondVisaService.history(idCardNo) > 0)
            return WebResult.error(ERRORDetail.SEC_0801002);
        // 身份证号码存数据表命名
        String[] mm = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
                "18"};
        // 身份证号码存入数组
        String[] r = new String[idCardNo.length()];
        for (int i = 0; i < idCardNo.length(); i++) {
            r[i] = idCardNo.substring(i, i + 1);
        }
        // 出生年月
        String birthtime = dataJSON.getString("birthtime");
        if (StringUtil.isEmptyOrNull(birthtime)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 电话号码
        String telphone = dataJSON.getString("telphone");
        if (StringUtil.isEmptyOrNull(telphone)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 紧急联系人
        String uName = dataJSON.getString("uName");
        if (StringUtil.isEmptyOrNull(uName)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 紧急联系人号码
        String uTelphone = dataJSON.getString("uTelphone");
        if (StringUtil.isEmptyOrNull(uTelphone)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }

        // 回寄证件方式1、自取，2、邮寄回去
        String postMethods = dataJSON.getString("postMethods");
        if (StringUtil.isEmptyOrNull(postMethods)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        // 收件人号码
        String rTelphone;
        // 收件地址
        String rAddress;
        // 收件人名字
        String recipient;
        // 邮编
        String postcode;
        // 生成表所需字段
        String postMethodsStr;
        // 空心框
        char a = (char) 9633;
        // 实心框
        char b = (char) 9632;
        logger.info("打印postMethods：" + postMethods);
        if ("1".equals(postMethods)) {
            rTelphone = dataJSON.getString("rTelphone");
            rAddress = dataJSON.getString("rAddress");
            recipient = dataJSON.getString("recipient");
            postcode = dataJSON.getString("postcode");
            postMethodsStr = b + "前往公安机关出入境管理部门领取" + "    " + a + "邮寄送达  ";
        } else {
            // 收件人号码
            rTelphone = dataJSON.getString("rTelphone");
            if (StringUtil.isEmptyOrNull(rTelphone)) {
                return WebResult.error(ERRORDetail.SEC_0801001);
            }
            // 收件地址
            rAddress = dataJSON.getString("rAddress");
            if (StringUtil.isEmptyOrNull(rAddress)) {
                return WebResult.error(ERRORDetail.SEC_0801001);
            }
            // 收件人
            recipient = dataJSON.getString("recipient");
            if (StringUtil.isEmptyOrNull(recipient)) {
                return WebResult.error(ERRORDetail.SEC_0801001);
            }
            // 邮编
            postcode = dataJSON.getString("postcode");
            if (StringUtil.isEmptyOrNull(postcode)) {
                return WebResult.error(ERRORDetail.SEC_0801001);
            }
            postMethodsStr = a + "前往公安机关出入境管理部门领取" + "    " + b + "邮寄送达  ";
        }

        // 应收的总金额
        Integer xgAndAmMuch = dataJSON.getInteger("money");
        if (StringUtil.isEmptyOrNull(xgAndAmMuch)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }

        // 签证类型
        Integer visaTypeHk = dataJSON.getInteger("visaTypeHk");//香港
        Integer visaTypeMacao = dataJSON.getInteger("visaTypeMacao");//澳门
        if (StringUtil.isEmptyOrNull(visaTypeHk) && StringUtil.isEmptyOrNull(visaTypeMacao)) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        String visaTypeStringHk;
        String visaTypeStringMc;
        switch (visaTypeHk) {
            case 0:
                visaTypeStringHk = a + "3月一次" + "\n" + a + "1年一次" + "\n" + a + "1年两次";
                break;
            case 1:
                visaTypeStringHk = b + "3月一次" + "\n" + a + "1年一次" + "\n" + a + "1年两次";
                break;
            case 2:
                visaTypeStringHk = a + "3月一次" + "\n" + b + "1年一次" + "\n" + a + "1年两次";
                break;
            default:
                visaTypeStringHk = a + "3月一次" + "\n" + a + "1年一次" + "\n" + b + "1年两次";
                break;
        }
        switch (visaTypeMacao) {
            case 0:
                visaTypeStringMc = a + "3月一次" + "\n" + a + "1年一次" + "\n";
                break;
            case 1:
                visaTypeStringMc = b + "3月一次" + "\n" + a + "1年一次" + "\n";
                break;
            default:
                visaTypeStringMc = a + "3月一次" + "\n" + b + "1年一次" + "\n";
                break;
        }

        // 证件照
        String mediaId = dataJSON.getString("mediaId");
        String token = wxToken.getAccessToken();
        if (mediaId == null) {
            return WebResult.error(ERRORDetail.SEC_0801001);
        }
        String photoPath = photopath + sCode + ".png";
        WebResult result = WxImgUtil.saveImageToDisk(token, mediaId, photoPath);
        if (!"00".equals(result.getErrCode()))
            return result;
        logger.info("打印result：" + result);
        logger.info("证件照地址photoPath：" + photoPath);
        // 生成一个json字段存储数据库
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("surname", surname);
        jsonObject.put("spellName", spellName);
        jsonObject.put("sex", sex);
        jsonObject.put("nation", nation);
        jsonObject.put("birthplace", birthplace);
        jsonObject.put("domicile", domicile);
        jsonObject.put("idCardNo", idCardNo);
        jsonObject.put("birthtime", birthtime);
        jsonObject.put("telphone", telphone);
        jsonObject.put("uName", uName);
        jsonObject.put("uTelphone", uTelphone);
        jsonObject.put("rTelphone", rTelphone);
        jsonObject.put("rAddress", rAddress);
        jsonObject.put("recipient", recipient);
        jsonObject.put("postcode", postcode);
        jsonObject.put("visaTypeHk",visaTypeHk);
        jsonObject.put("visaTypeMacao",visaTypeMacao);
        jsonObject.put("postMethods", postMethods);
        logger.info("所有数据的json段" + jsonObject.toJSONString());
        Map<String, Object> datas = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("name", name);
                put("surname", surname);
                put("spellName", spellName);
                put("sex", sex);
                put("nation", nation);
                put("birthplace", birthplace);
                put("domicile", domicile);
                for (int i = 0; i < 18; i++) {
                    put(mm[i], r[i]);
                }
                put("birthtime", birthtime);
                put("telphone", telphone);
                put("uName", uName);
                put("uTelphone", uTelphone);
                put("rTelphone", rTelphone);
                put("rAddress", rAddress);
                put("recipient", recipient);
                put("postcode", postcode);
                put("visaTypeStringMc", visaTypeStringMc);
                put("visaTypeStringHk", visaTypeStringHk);
                put("postMethods", postMethodsStr);
                put("truePhoto", new PictureRenderData(142, 240, photoPath));
            }
        };
        // word文档相对路径
        String relativePath = "SecondVisa/" + sCode + ".docx";
        logger.info(" Word文档的相对路径relativePath：" + relativePath);
        // 绝对路径
        path = path + sCode + ".docx";
        logger.info(" Word文档的绝对路径path：" + path);
//		String realPath = request.getServletContext().getRealPath("WEB-INF/WordTemplate/中国公民出入境证件申请表.docx");
        String realPath =config.getTemplatePath()+"中国公民出入境证件申请表.docx";
        logger.info("文档模板地址realPath：" + realPath);
        ToWord.getInstance().toWord(datas, realPath, path);

        //获取签名值，即文档的hash值
        String sign = "";
        if(config.getEIDON()) {
            String hashValue = DigestUtils.md5Hex(new FileInputStream(path));
            logger.info("夫妻投靠生成文档的哈希值：" + hashValue);
            //然后对哈希值进行签名

            JSONObject resultjson = sIMeIDService.smsDirect(SIMeIDphone, hashValue);
            logger.info("SIMeID签名返回结果：" + resultjson);
            if (resultjson == null || "".equals(resultjson))
                return WebResult.error(ERRORDetail.EID_0601001);
            sign = resultjson.getString("eid_sign");
            logger.info("SIMeID签名值signal：" + sign);
            if (sign == null || "".equals(sign))
                return WebResult.error(ERRORDetail.EID_0601001);
        }
        String jsonString = jsonObject.toJSONString();
        WebResult data = secondVisaService.cheeApplicationg(sign,openId,jsonString, xgAndAmMuch, sCode, photoPath, relativePath,
                idCardNo, appeidcode);
        logger.info("返回数据" + JSONObject.toJSONString(data));
        //获取管理员的openId
        List<String> openIdArr = coupleAwartService.getOpenIdArr(2,null);
        //发送推送
        for (String openID:openIdArr)
            WxUtils.sendToM(managerUrl,name, sCode,openID,"二次签注");
        return data;
    }

//	/**
//	 * 响应APP查询记录状态
//	 */
//	@RequestMapping(value = "/secondVisa/queryState" ,method = RequestMethod.PUT)
//	@ResponseBody
//	@ApiOperation(value = "二次签注响应APP查询记录状态", httpMethod = "PUT", produces = "application/json")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "sCode", value = "二次签注唯一标识", required = true, paramType = "query") })
//	@ApiResponses({@ApiResponse(code = 200, message = "{\"errCode\": \"00\",\"errMsg\": \"0000000(成功)\",\"data\": {}}")})
//	public Object queryState(HttpServletRequest request,String sCode) {
//		HttpSession session = request.getSession();
//		if(session == null)
//			return WebResult.error(ERRORDetail.GQ_0103003);
//		String appeidcode = (String) session.getAttribute("user");
//		if(appeidcode==null || "".equals(appeidcode))
//			return WebResult.error(ERRORDetail.GQ_0103004);
//		if (sCode == null || sCode.length() == 0)
//			return WebResult.error(ERRORDetail.SEC_0801003);
//		logger.info("接收到的sCode：" + sCode);
//		Object object = secondVisaService.queryState(sCode);
//		logger.info("object：" + object);
//		return object;
//	}

    /**
     * 通过appeidcode或业务流水号获取申请记录信息
     */
    @RequestMapping(value = "/visa/getinfo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取二次签注申请记录数据", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "bizId", value = "申请记录唯一标识", required = false, paramType = "query") })
    @ApiResponses({@ApiResponse(code = 200, message = "{\"errCode\":\"00\",\"errMsg\":\"0000000(成功)\",\"data\":{\"personalinfo\"(申请记录详细信息):{\"birthplace\":\"均村\",\"nation\":\"汉族\",\"sex\":\"男\",\"idCardNo\":\"360732199605283659\",\"postcode\":\"330065\",\"spellName\":\"jian hui \",\"postMethods\":\"2\",\"uTelphone\":\"18770099843\",\"visaType\":1,\"birthtime\":\"1996年04月27日\",\"rAddress\":\"江西省南昌市江西警察学院\",\"uName\":\"陈首斌\",\"surname\":\"chen\",\"telphone\":\"15570146381\",\"name\":\"陈剑辉\",\"recipient\":\"陈剑辉\",\"hkPlace\":\"兴国\",\"rTelphone\":\"15570146381\"},\"path\"（申请word文档地址）:\"SecondVisa/b966da459a1244c0974ee4470216c76c.docx\",\"bizId\"（唯一标识）:\"b966da459a1244c0974ee4470216c76c\",\"money\"（总需缴纳金额）:15,\"photopath\"（现场照片地址）:\"SecondVisa/photo/b966da459a1244c0974ee4470216c76c.png\",\"orderNum\"（缴费订单号）:\"123456789012345\",\"remark\"（申请失败理由）:null,\"state\"（状态）:1,\"time\"（申请时间）:\"2018-03-10T03:59:56.000+0000\"}}")})
    public Object getinfo(HttpServletRequest request,String bizId) {
        logger.info("打印业务流水号bizId："+bizId);
        HttpSession session = request.getSession();
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        logger.info("接收到的idCard：" + appeidcode);
        if(bizId == null)
            bizId = secondVisaService.getsCode(appeidcode);
        if(bizId == null)
            return WebResult.error(ERRORDetail.SEC_0801005);//不存在申请记录
//        String appeidcode = "CGzWLrYopSYndqrsw1ljI+8v5NPgyvSAGOpip0PJmKMxMDAw";
        Object object = secondVisaService.getinfo(bizId);
        logger.info("object：" + JSONObject.toJSONString(object));
        return object;
    }

    /**
     * 微信端管理员审核
     */
    @RequestMapping(value = "/visa/visacheck",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "二次签注审核", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({ @ApiImplicitParam(name = "remark", value = "审核不通多原因", required = false, paramType = "query"),
            @ApiImplicitParam(name = "bizId", value = "申请记录唯一标识", required = true, paramType = "query"),
            @ApiImplicitParam(name = "state", value = "改变申请状态，只可为1成功，2失败", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "orderNum", value = "支付订单号", required = false, paramType = "query", dataType = "Integer") })
    public Object changeState(HttpServletRequest request,String remark, String bizId, Integer state, String orderNum) {
        HttpSession session = request.getSession();
        //判断登录是否有效
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        String openId = (String)session.getAttribute("openId");
        if (openId == null || "".equals(openId))
            return WebResult.error(ERRORDetail.GQ_0103004);
        //判断必要参数是否合法
        if (!(state == 1 || state == 2) || bizId == null)
            return WebResult.error(ERRORDetail.BUS_0501002);
        if (state == 1 && orderNum == null)
            return WebResult.error(ERRORDetail.BUS_0501002);
        logger.info("接收到的appeidcode：" + appeidcode);
        logger.info("接收到的bizId:"+bizId);
        logger.info("接收到的openId:"+openId);
        if(bizId == null)
            bizId = secondVisaService.getsCode(appeidcode);
        logger.info("接收到的bizId:"+bizId);
        if(bizId == null)
            return WebResult.error(ERRORDetail.COU_0701004);
//		String appeidcode = "CGzWLrYopSYndqrsw1ljI+8v5NPgyvSAGOpip0PJmKMxMDAw";
        //判断管理员是否有权限审核这个业务
        if(!secondVisaService.checkManager(openId))
            return WebResult.error(ERRORDetail.SEC_0801006);
        return secondVisaService.changeState(remark, bizId, state,orderNum);
    }
}
