package com.hc.gqgs.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hc.gqgs.config.Config;
import com.hc.gqgs.json.ERRORDetail;
import com.hc.gqgs.json.WebResult;
import com.hc.gqgs.mybatis.po.PersonalDetails;
import com.hc.gqgs.mybatis.po.UserInfoWithBLOBs;
import com.hc.gqgs.service.UserService;
import com.hc.gqgs.tools.SIMeID.SIMeIDService;
import com.hc.gqgs.wechat.DefaultWxTokenRepo;
import com.hc.gqgs.wechat.WxImgUtil;
import com.hc.gqgs.wechat.WxUtils;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Api(value = "UserController", tags = {"违章学习的所有接口"})
public class UserController extends BaseController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    static DefaultWxTokenRepo wxToken = DefaultWxTokenRepo.getInstance();
    static final String userUrl = "http://kingeid.nat300.top/#/trafficmain?bizId=";
    static final String managerUrl = "http://kingeid.nat300.top/#/verify?verifyOpenId=";

    @Autowired
    UserService userService;
    @Autowired
    Config config;
    @Autowired
    SIMeIDService siMeIDService;

    @PostMapping(value = "/login")
    @ResponseBody
    @ApiOperation(value = "登录", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "telphone", value = "手机号", required = true, paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "返回用户个人信息")})
    public WebResult login(HttpServletRequest request, String telphone, String openId) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("user") == null || session.getAttribute("telphone") == null || session.getAttribute("openId") == null) {
            if (telphone == null || telphone.length() < 1) {
                return WebResult.error(ERRORDetail.TFC_0101001);
            }
            String appeidcode = null;
            if (config.getEIDON()) {
                JSONObject json = siMeIDService.smsDirect(telphone, "abc");
                logger.info("------------->>" + json);
                if (!"00".equals(json.getString("result"))) {
                    return WebResult.error(ERRORDetail.TFC_0101016);
                }
                PersonalDetails personalDetails = new PersonalDetails();
                String userInfo = json.getString("user_info");
                appeidcode = JSONObject.parseObject(userInfo).getString("appeidcode");
                personalDetails.setAppeidcode(appeidcode);
                personalDetails.setTelphone(telphone);
                userService.insertPersonal(personalDetails);
            }else {
                appeidcode = userService.queryByPhone(telphone);//测试
            }
            if (appeidcode == null) {
                return WebResult.error(ERRORDetail.BUS_0501002);
            }
            if (openId == null || "".equals(openId)) {
                return WebResult.error(ERRORDetail.BUS_0501002);
            }
            session.setAttribute("user", appeidcode);
            session.setAttribute("telphone", telphone);
            session.setAttribute("openId", openId);
            return WebResult.success(userService.selByApeid(appeidcode));

        }
        String appeidcode = (String) session.getAttribute("user");
        telphone = (String) session.getAttribute("telphone");
        openId = (String) session.getAttribute("openId");
        session.setAttribute("user", appeidcode);//每次登陆都更新session有效期
        session.setAttribute("telphone", telphone);
        session.setAttribute("openId", openId);
        return WebResult.success(userService.selByApeid(appeidcode));
    }

    @GetMapping(value = "/logout")
    @ResponseBody
    @ApiOperation(value = "注销登录", httpMethod = "GET", produces = "application/json")
    @ApiResponses({@ApiResponse(code = 200, message = "0000000(成功)")})
    public WebResult login(HttpSession session) {
        session.invalidate();
        return WebResult.success();
    }

    @PostMapping(value = "/fillinfo")
    @ResponseBody
    @ApiOperation(value = "补全用户信息", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sex", value = "性别", required = true, paramType = "query"),
            @ApiImplicitParam(name = "birthtime", value = "出生日期", required = true, paramType = "query"),
            @ApiImplicitParam(name = "nation", value = "民族", required = true, paramType = "query"),
            @ApiImplicitParam(name = "domicile", value = "户籍地址", required = true, paramType = "query"),
            @ApiImplicitParam(name = "currentAddress", value = "现住地址", required = true, paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "0000000(成功)")})
    public WebResult perfect(HttpSession session, String sex, Date birthtime, String nation, String domicile, String currentAddress) {
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setAppeidcode(appeidcode);
        personalDetails.setSex(sex);
        personalDetails.setBirthtime(birthtime);
        personalDetails.setNation(nation);
        personalDetails.setDomicile(domicile);
        personalDetails.setCurrentAddress(currentAddress);
        boolean flag = userService.perfect(personalDetails);
        if (!flag) {
            return WebResult.error(ERRORDetail.TFC_0101010);
        }
        return WebResult.success();
    }

    @PostMapping(value = "/illegal/info")
    @ResponseBody
    @ApiOperation(value = "提交个人业务信息", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wxOpenId", value = "用户微信号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "carType", value = "准驾车型代号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "姓名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "idCard", value = "身份证号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "archivesId", value = "档案编号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "frontSide", value = "身份证正面照片的mediaId", required = true, paramType = "query"),
            @ApiImplicitParam(name = "backSide", value = "身份证背面照片的mediaId", required = true, paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "返回业务流水号")})
    public WebResult videoAll(HttpSession session, String wxOpenId, String carType, String name, String idCard, String archivesId, String frontSide, String backSide) {
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        if (null == wxOpenId || "".equals(wxOpenId)) {
            logger.info("数据为空！");
            return WebResult.error(ERRORDetail.TFC_0101001);
        }
        UserInfoWithBLOBs userInfoWithBLOBs = new UserInfoWithBLOBs();
        String apeidcode = (String) session.getAttribute("user");
        userInfoWithBLOBs.setAppeidcode(apeidcode);
        String bizId = UUID.randomUUID().toString().replaceAll("-", "");
        userInfoWithBLOBs.setBizId(bizId);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String timer = df.format(new Date());
        // 构建正面图片的路径
        String relativePath = "valiteImg/" + bizId + "/" + timer + ".jpg";
        String filePath = config.getValitePath() + relativePath;
        WebResult result = WxImgUtil.saveImageToDisk(wxToken.getAccessToken(), frontSide, filePath);
        if (!result.getErrCode().equals("00")) {
            return WebResult.error(ERRORDetail.TFC_0101011);
        }
        // 构建反面图片的路径
        String relativePathT = "valiteImg/" + bizId + "/" + timer + "1.jpg";
        String filePathT = config.getValitePath() + relativePathT;
        WebResult resultT = WxImgUtil.saveImageToDisk(wxToken.getAccessToken(), backSide, filePathT);
        if (!resultT.getErrCode().equals("00")) {
            return WebResult.error(ERRORDetail.TFC_0101011);
        }
        // 开始学习的时间
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        JSONObject personInfo = new JSONObject();
        personInfo.put("wxOpenId", wxOpenId);
        personInfo.put("carType", carType);
        personInfo.put("idCard", idCard);
        personInfo.put("time", time);
        personInfo.put("archivesId", archivesId);
        personInfo.put("frontSide", "Traffic/" + relativePath);
        personInfo.put("backSide", "Traffic/" + relativePathT);
        personInfo.put("mediaId1", frontSide);
        personInfo.put("mediaId2", backSide);
        personInfo.put("userName", name);
        userInfoWithBLOBs.setPersonInfo(personInfo.toJSONString());
        boolean flagTow = userService.insertPersonalInfo(userInfoWithBLOBs);
        if (!flagTow) {
            return WebResult.error(ERRORDetail.TFC_0101009);
        }
        return WebResult.success(bizId);
    }

    @PostMapping(value = "/illegal/sync")
    @ResponseBody
    @ApiOperation(value = "同步学习信息", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "{bizId:业务流水号,learningInfo(学习记录,任意格式,当客户端需要时原样返回):{},valiteInfo(校验记录):[{isValite:是否校验成功(true|false),learningMillSeconds:学习时长(例:59750),mediaId:校验图片的相对路径,time:开始校验的时间(例:1519718918391)}]}", required = true, paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "返回用户校验时上传图片的相对路径")})
    public WebResult updateValite(HttpSession session,String data) {
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        if (null == data || data.length() < 1) {
            return WebResult.error(ERRORDetail.TFC_0101001);
        }
        boolean flag = false;
        JSONObject json = JSONObject.parseObject(data);
        String bizId = json.getString("bizId");
        String learnInfo = json.getString("learningInfo");
        if (null == learnInfo || learnInfo.length() < 1) {
            return WebResult.error(ERRORDetail.TFC_0101004);
        }
        String valite = json.getString("valiteInfo");
        if (null == valite || valite.length() < 1) {
            return WebResult.error(ERRORDetail.TFC_0101003);
        }
        JSONObject jsonObject = JSONObject.parseObject(valite);
        String relativePath = "";
        if (jsonObject.getString("isValite").equals("true")) {
            // 获取图片base64
            String token = wxToken.getAccessToken();
            String mediaId = jsonObject.getString("mediaId");
            Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String timer = df.format(day);
            // 构建图片的路径
            relativePath = "valiteImg/" + bizId + "/" + timer + ".jpg";
            String filePath = config.getValitePath() + relativePath;
            WebResult result = WxImgUtil.saveImageToDisk(token, mediaId, filePath);
            if (!result.getErrCode().equals("00"))
                return WebResult.error(ERRORDetail.TFC_0101011);
            jsonObject.put("relativePath", "Traffic/" + relativePath);
        }
        UserInfoWithBLOBs userInfoWithBLOBs = userService.queryById(bizId);
        if (userInfoWithBLOBs == null)
            return WebResult.error(ERRORDetail.TFC_0101007);
        String newValite = userInfoWithBLOBs.getValiteInfo();
        if (null == newValite || newValite.length() <= 0) {
            JSONArray valiteInfo = new JSONArray();
            valiteInfo.add(jsonObject);
            userInfoWithBLOBs.setValiteInfo(valiteInfo.toString());
            userInfoWithBLOBs.setLearnInfo(learnInfo);
            // 更新数据库
            flag = userService.updateUserInfo(userInfoWithBLOBs);
        } else {
            JSONArray valiteInfoJson = JSONArray.parseArray(newValite);
            JSONObject va = (JSONObject) valiteInfoJson.get(valiteInfoJson.size() - 1);
            String time = va.getString("time");
            String clientTime = jsonObject.getString("time");
            if (null == clientTime || clientTime.length() < 1) {
                return WebResult.error(ERRORDetail.TFC_0101005);
            }
            long newTime = Long.parseLong(time);
            long newClientTime = Long.parseLong(clientTime);
            if (newTime >= newClientTime) {
                return WebResult.error(ERRORDetail.TFC_0101006);
            }
            // 将验证信息valiteInfo添加进去
            valiteInfoJson.add(jsonObject);
            userInfoWithBLOBs.setValiteInfo(valiteInfoJson.toString());
            userInfoWithBLOBs.setLearnInfo(learnInfo);
            // 更新数据库
            flag = userService.updateUserInfo(userInfoWithBLOBs);

        }
        if (!flag) {
            return WebResult.error(ERRORDetail.TFC_0101010);
        }
        return WebResult.success("Traffic/" + relativePath);
    }

    @GetMapping(value = "/illegal/history")
    @ResponseBody
    @ApiOperation(value = "找回学习记录", httpMethod = "GET", produces = "application/json")
    @ApiResponses({@ApiResponse(code = 200, message = "返回业务流水号、状态、学习记录及开始学习时间")})
    public WebResult backInfo(HttpSession session) {
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        if (null == appeidcode || appeidcode.length() < 1) {
            return WebResult.error(ERRORDetail.TFC_0101001);
        }
        JSONArray json = userService.queryByApeidCode(appeidcode);
        return WebResult.success(json);
    }

    @PostMapping(value = "/illegal/answer")
    @ResponseBody
    @ApiOperation(value = "提交做题信息", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "{bizId:业务流水号,option(用户答题记录):{1(题目id):1(选项id),110:335,112:340,116:348,119:353}", required = true, paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "0000000(成功)")})
    public WebResult upQuest(HttpSession session,String data) {
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        if (data == null || "".equals(data))
            return WebResult.error(ERRORDetail.TFC_0101001);
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONObject json = jsonObject.getJSONObject("option");
        String bizId = jsonObject.getString("bizId");
        String option = json.toJSONString();
        userService.insertQuestion(bizId, option);
        UserInfoWithBLOBs user = userService.queryById(bizId);
        String userName = JSONObject.parseObject(user.getPersonInfo()).getString("userName");
        List<String> openId = userService.getOpenId();
        for (String wxOpenId : openId) {
            WxUtils.sendToM(managerUrl, userName, bizId, wxOpenId, "交通违章");
        }
        return WebResult.success();
    }

    @GetMapping(value = "/illegal")
    @ApiOperation(value = "获取业务状态以及业务信息", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "bizId", value = "业务流水号", required = true, paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "返回用户个人信息、业务状态(state)、学习记录、校验记录、题目(questions:[{img:题目带的图片(例:images/image105.jpeg),questionId:题目id(例:233),options(选项):[{optionId:选项id(例:701),option:选项内容(例:对)},{}],title:题目内容(例:在这种情况的铁路道口要加速通过。)},{}])(状态为2)、做题记录(状态为3)以及用户答题记录(状态为3)")})
    @ResponseBody
    public WebResult finishVideo(HttpSession session, String bizId) {
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        if (bizId != null) {
            return userService.getAll(bizId);
        }
        String apeidcode = (String) session.getAttribute("user");
        if (apeidcode == null) {
            return WebResult.error(ERRORDetail.TFC_0101001);
        }
        String bizIdT = userService.queryBizId(apeidcode);
        if (bizIdT == null) {
            return WebResult.error(ERRORDetail.TFC_0101001);
        }
        return userService.getAll(bizIdT);
    }

    @PostMapping(value = "/illegal/complete")
    @ResponseBody
    @ApiOperation(value = "完成视频学习", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "{bizId:业务流水号,learningInfo(学习记录):{1(视频id):[{end:结束观看视频时间(例:1519718958621),start:开始观看视频时间(例:1519718951871)}]}}", required = true, paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "0000000(成功)")})
    public WebResult syn(HttpServletRequest request,String data) {
        HttpSession session = request.getSession();
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        if (null == data || data.length() < 1) {
            logger.info("数据为空！");
            return WebResult.error(ERRORDetail.TFC_0101001);
        }
        JSONObject jsonObject = JSONObject.parseObject(data);
        String bizId = jsonObject.getString("bizId");
        String learnInfo = jsonObject.getString("learningInfo");
        if (learnInfo == null)
            return WebResult.error(ERRORDetail.TFC_0101001);
        UserInfoWithBLOBs userInfo = new UserInfoWithBLOBs();
        userInfo.setLearnInfo(learnInfo);
        userInfo.setBizId(bizId);
        userService.updateUserInfo(userInfo);
        boolean isTrue = userService.updateState(bizId);
        if (!isTrue)
            return WebResult.error(ERRORDetail.TFC_0101013);
        return WebResult.success();
    }

    @PostMapping(value = "/illegal/verify")
    @ResponseBody
    @ApiOperation(value = "违章学习审核", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizId", value = "业务流水号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "state", value = "审核结果（true|false）", required = true, paramType = "query"),
            @ApiImplicitParam(name = "failMsg", value = "审核未通过原因", required = false, paramType = "query"),
            @ApiImplicitParam(name = "openId", value = "审核人微信号", required = true, paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "0000000(成功)")})
    public WebResult check(HttpServletRequest request,String bizId, String state, String failMsg, String openId) {
        HttpSession session = request.getSession();
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        if (bizId == null || "".equals(bizId))
            return WebResult.error(ERRORDetail.TFC_0101001);
        if (state == null || "".equals(state))
            return WebResult.error(ERRORDetail.TFC_0101001);
        if (openId == null || "".equals(openId))
            return WebResult.error(ERRORDetail.TFC_0101001);
        boolean flag = userService.findOpenId(openId);
        if (!flag) {
            return WebResult.error(ERRORDetail.TFC_0101017);
        }
        UserInfoWithBLOBs userInfoWithBLOBs = new UserInfoWithBLOBs();
        userInfoWithBLOBs.setBizId(bizId);
        if (state.equals("true")) {
            state = "4";
        } else {
            state = "5";
            UserInfoWithBLOBs user = userService.queryById(bizId);
            if (user == null) {
                return WebResult.error(ERRORDetail.TFC_0101008);
            }
            JSONObject personInfo = JSONObject.parseObject(user.getPersonInfo());
            personInfo.put("failMsg", failMsg);
            userInfoWithBLOBs.setPersonInfo(JSONObject.toJSONString(personInfo));
        }
        userInfoWithBLOBs.setState(state);
        String userOpenId = userService.updateState(userInfoWithBLOBs);
        if (userOpenId == null || "".equals(userOpenId)) {
            return WebResult.error(ERRORDetail.TFC_0101008);
        }
        if (state.equals("4"))
            WxUtils.sendToUT(userUrl, userOpenId, bizId, "违章学习");
        else
            WxUtils.sendToUF(userUrl, userOpenId, failMsg, bizId, "违章学习");
        return WebResult.success();
    }

    @GetMapping(value = "/illegal/question")
    @ResponseBody
    @ApiOperation(value = "在线测试", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "questionId", value = "用户做过的题目id(例:[1,5,12,6])", required = true, paramType = "query")})
    @ApiResponses({@ApiResponse(code = 200, message = "返回一道题目(示例:{img:题目带的图片(例:images/image105.jpeg),questionId:题目id(例:233),options(选项):[{optionId:选项id(例:701),option:选项内容(例:对)}],rightOptionId:答案的id})")})
    public WebResult exam(HttpServletRequest request,String questionId) {
        HttpSession session = request.getSession();
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        JSONArray json = JSONArray.parseArray(questionId);
        if (json.size() == 0) {
            return WebResult.success(userService.selOne(new String[]{"0"}));
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < json.size(); i++) {
            list.add(json.getString(i));
        }
        String[] question = new String[list.size()];
        list.toArray(question);
        return WebResult.success(userService.selOne(question));
    }

    @PostMapping(value = "/debug")
    @ResponseBody
    @ApiIgnore
    public WebResult getDebug(HttpServletRequest request,String bizId, String debugData) throws IOException {
        HttpSession session = request.getSession();
        if (session == null)
            return WebResult.error(ERRORDetail.GQ_0103004);
        String appeidcode = (String) session.getAttribute("user");
        if (appeidcode == null || "".equals(appeidcode))
            return WebResult.error(ERRORDetail.GQ_0103004);
        if (bizId == null)
            return WebResult.error(ERRORDetail.TFC_0101007);
        if (debugData == null)
            return WebResult.error(ERRORDetail.TFC_0101015);
        String path = "D:/illegal/" + bizId + "/" + new Date().getTime() + ".txt";
        File file = new File(path);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        file.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(debugData);
        logger.info("debug文件已保存");
        bw.flush();
        bw.close();
        return WebResult.success(true);
    }

}
