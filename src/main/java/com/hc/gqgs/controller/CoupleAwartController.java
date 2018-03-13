package com.hc.gqgs.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hc.gqgs.config.Config;
import com.hc.gqgs.json.ERRORDetail;
import com.hc.gqgs.json.WebResult;
import com.hc.gqgs.mybatis.mapper.PoliceStationMapper;
import com.hc.gqgs.mybatis.po.PoliceStation;
import com.hc.gqgs.mybatis.po.PoliceStationExample;
import com.hc.gqgs.service.CoupleAwartService;
import com.hc.gqgs.tools.SIMeID.SIMeIDService;
import com.hc.gqgs.tools.StringUtil;
import com.hc.gqgs.tools.ToWord;
import com.hc.gqgs.wechat.DefaultWxTokenRepo;
import com.hc.gqgs.wechat.WxImgUtil;
import com.hc.gqgs.wechat.WxUtils;
import io.swagger.annotations.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//夫妻投靠
@Controller
//@ApiIgnore()
@Api(value = "/awart/CoupleAwartController", tags = { "夫妻投靠的所有接口" })
public class CoupleAwartController {
	Logger logger = LoggerFactory.getLogger(CoupleAwartController.class);
	static DefaultWxTokenRepo wxToken = DefaultWxTokenRepo.getInstance();
	//给管理员发信息的地址
	static  final  String managerUrl = "http://kingeid.nat300.top/#/coupleverify?verifyOpenId=";
	@Autowired
	CoupleAwartService coupleAwartService;
	@Autowired
	PoliceStationMapper policeStationMapper;
	@Autowired
	Config config;
	@Autowired
	SIMeIDService sIMeIDService;

	@PostMapping(value = "/awart/coupleawartcontroller")
	@ResponseBody
	@ApiOperation(value = "夫妻投靠数据提交", httpMethod = "POST", produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "data", value = "所有数据都在data里面，数据项描述例子：{info:{name:名字,police:派出所名称,sex:申请人性别,idCard:申请人身份证号码,telphone:申请人电话号码,domicile:申请人户籍所在地(江西省抚州市宜黄县棠阴镇君山村),relative:申请人与被申请人的关系,"
					+ "reName:被申请人名字,reSex:被申请人性别,reIdCard:被申请人身份证号码,birth:被申请人出生日期(1995年05月28日),openId:每个微信用户的唯一标识,redomicile:被申请人户籍所在地(江西省抚州市宜黄县棠阴镇君山村)},mediaIdArr:[前4张为双方户口本相关照片,第5张结婚证照片,6-9是双方身份证照片,第10张是现场照片](数组类型,存放了所有照片的mediaId，即每张图片的唯一标识)}", required = true, paramType = "query") })
	@ApiResponses({@ApiResponse(code = 200, message = "{\"errCode\":\"00\",\"errMsg\":\"0000000(成功)\",\"data\":{\"cCode\":cCode}}")})
	public Object coupleAwartController(HttpServletRequest request,String data)
			throws IllegalStateException, IOException {
		HttpSession session = request.getSession();
		if(session == null)
			return WebResult.error(ERRORDetail.GQ_0103004);
		String appeidcode = (String) session.getAttribute("user");
		String SIMeIDphone = (String) session.getAttribute("telphone");
		logger.info("打印SIMeIDphone:"+SIMeIDphone);
		if(SIMeIDphone == null || "".equals(SIMeIDphone))
			return WebResult.error(ERRORDetail.GQ_0103004);
		if(appeidcode==null || "".equals(appeidcode))
			return WebResult.error(ERRORDetail.GQ_0103004);
		if (StringUtil.isEmptyOrNull(data))
			WebResult.error(ERRORDetail.COU_0701002);
		// 生成的表存放根目录，从resources配置文件中读取出来的
		String path = config.getPath() + "CoupleAwart\\";
		// 存在就不创建，不存在则创建文件夹
		File file = new File(path, "CoupleAwart");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		// 图片文件夹名，生成的文档名
		String cCode = UUID.randomUUID().toString().replace("-", "");
		// 生成每次存放上传的图片文件
		String photopath = path + "photo\\";
		File photofile = new File(photopath, "photo");
		if (!photofile.getParentFile().exists()) {
			photofile.getParentFile().mkdirs();
		}
		// 生成存放图片的文件夹
		photopath = photopath + cCode + "\\";
		File filePath = new File(photopath, cCode);
		if (!filePath.getParentFile().exists()) {
			filePath.getParentFile().mkdirs();
		}
		logger.info("打印图片存储路径photopath：" + photopath);

		// 获取个人基本信息
		logger.info("夫妻投靠所有数据："+data);
		JSONObject dataJson = JSONObject.parseObject(data);
		if (dataJson == null || dataJson.isEmpty())
			WebResult.error(ERRORDetail.COU_0701002);

		// 申请人姓名
		String name = dataJson.getString("name");
		if (StringUtil.isEmptyOrNull(name)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}
		// 派出所名称
		String policeStation = dataJson.getString("police");
		if (StringUtil.isEmptyOrNull(policeStation)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}
		// 申请人性别
		String sex = dataJson.getString("sex");
		if (StringUtil.isEmptyOrNull(sex)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}
		// 申请人身份证号码
		String idCard = dataJson.getString("idCard");
		if (StringUtil.isEmptyOrNull(idCard)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}
		// 申请人电话号码
		String telphone = dataJson.getString("telphone");
		if (StringUtil.isEmptyOrNull(telphone)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}
		// 申请人与被申请人关系
		String relative = dataJson.getString("relative");
		if (StringUtil.isEmptyOrNull(relative)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}
		// 被申请人名字
		String reName = dataJson.getString("reName");
		if (StringUtil.isEmptyOrNull(reName)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}
		// 被申请人性别
		String reSex = dataJson.getString("reSex");
		if (StringUtil.isEmptyOrNull(reSex)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}
		// 被申请人身份证号码
		String reIdCard = dataJson.getString("reIdCard");
		if (StringUtil.isEmptyOrNull(reIdCard)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}
		// 被申请人出生日期
		String birth = dataJson.getString("birth");
		if (StringUtil.isEmptyOrNull(birth)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}
		// 声请人户籍所在地
		String domicile = dataJson.getString("domicile");
		if (StringUtil.isEmptyOrNull(domicile)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}

		// 被声请人户籍所在地
		String redomicile = dataJson.getString("redomicile");
		if (StringUtil.isEmptyOrNull(redomicile)) {
			WebResult.error(ERRORDetail.COU_0701002);
		}
		//每个微信用户的唯一标识openId
        String openId = dataJson.getString("openId");
        if (StringUtil.isEmptyOrNull(openId)) {
            WebResult.error(ERRORDetail.COU_0701002);
        }
		// 获得图片ID的数组
		JSONArray mediaIdArr = dataJson.getJSONArray("mediaIdArr");
		if (mediaIdArr == null || mediaIdArr.size() < 10)
			WebResult.error(ERRORDetail.COU_0701003);
		logger.info("打印接受的jsonArray：" + mediaIdArr.toJSONString().length());
		logger.info("打印jsonArray的大小：" + mediaIdArr.size());
		String mediaId = null;
		int length = mediaIdArr.size();
		String[] photo = new String[length];
		logger.info("打印photo的大小：" + photo.length);
		String token = wxToken.getAccessToken();
		for (int i = 0; i < length; i++) {
			if (mediaIdArr.getString(i) == null)
				WebResult.error(ERRORDetail.COU_0701003);
			mediaId = mediaIdArr.getString(i);
//			photo[i] = Base64Image.GenerateImage(newString, photopath + i + ".png");
			logger.info("打印photo[" + i + "]:" + photo[i]);
			String photoPath = photopath + i + ".png";
			WebResult result =  WxImgUtil.saveImageToDisk(token,mediaId,photoPath);
			if(!"00".equals(result.getErrCode())){
//				result.setErrMsg("第"+ i+1 + "张图片存储失败");
				return result;
			}
			photo[i] = photoPath;
		}
		// 图片路径按模块存入数据库
		// 申请本人现场照片
		String picturePath = "CoupleAwart/photo/" + cCode + "/" + 9 + ".png";
		// 户口本证件照
		String hkphotoPath = "";
		for (int i = 0; i <= 3; i++) {
			hkphotoPath += "CoupleAwart/photo/" + cCode + "/" + i + ".png" + ";";
		}
		hkphotoPath = hkphotoPath.substring(0, hkphotoPath.length() - 1);
		logger.info("打印hkphotoPath：" + hkphotoPath);
		// 身份证证件照
		String idCardPath = "";
		for (int i = 5; i <= 8; i++) {
			idCardPath += "CoupleAwart/photo/" + cCode + "/" + i + ".png" + ";";
		}
		idCardPath = idCardPath.substring(0, idCardPath.length() - 1);
		logger.info("打印idCardPath：" + idCardPath);
		// 结婚照证件照
		String mcPicture3 = "CoupleAwart/photo/" + cCode + "/4.png";
		logger.info("打印mcPicture3：" + mcPicture3);

		logger.info("打印photo[]：" + photo.toString());
		Map<String, Object> datas = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("hkPicture1", ToWord.toPicture(photo[0]));
				put("hkPicture2", ToWord.toPicture(photo[1]));
				put("hkPicture3", ToWord.toPicture(photo[2]));
				put("hkPicture4", ToWord.toPicture(photo[3]));
				put("idCardPicture1", ToWord.toPicture(photo[5]));
				put("idCardPicture2", ToWord.toPicture(photo[6]));
				put("idCardPicture3", ToWord.toPicture(photo[7]));
				put("idCardPicture4", ToWord.toPicture(photo[8]));
				put("Picture", ToWord.toPicture(photo[9]));
				put("mcPicture1", ToWord.toPicture(photo[4]));
			}
		};
		// 材料word文档相对路径
		String relativePath1 = "CoupleAwart/" + cCode + "_1" + ".docx";
		logger.info("打印相对路径relativePath：" + relativePath1);
		// 生成材料word文档的绝对路径
		String path1 = path + cCode + "_1" + ".docx";
		// String path1 = path + "\\" + "CoupleAwart" + "\\" + cCode + "_材料" +
		// ".docx";
		logger.info("打印绝对路径path：" + path1);
		// 模板文档的路径
//		String realPath = request.getServletContext().getRealPath("WEB-INF/WordTemplate/共青城市商品房夫妻投靠材料表.docx");
		String realPath = config.getTemplatePath() + "共青城市商品房夫妻投靠材料表.docx";
		logger.info("模板文档地址:" + realPath);
		logger.info("生成文档地址:" + path1);
		ToWord.getInstance().toWord(datas, realPath, path1);

		// 生成个人信息Word
        logger.info("打印户籍地址："+domicile);
        logger.info("打印户籍地址："+redomicile);
		Map<String, Object> datas2 = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("name", name);
				put("policeStation", policeStation);
				put("sex", sex);
				put("idCard", idCard);
				put("telphone", telphone);
				put("relative", relative);
				put("reName", reName);
				put("reSex", reSex);
				put("reIdCard", reIdCard);
				put("birth", birth);
                put("domicile", domicile);
				put("domicile1", domicile);
				put("redomicile", redomicile);
			}
		};
		// 个人信息word文档相对路径
		String relativePath2 = "CoupleAwart/" + cCode + "_2" + ".docx";
		logger.info("打印相对路径relativePath2：" + relativePath2);
		// 生成个人信息word文档的绝对路径
		String path2 = path + cCode + "_2" + ".docx";
		// String path2 = path + "\\" + "CoupleAwart" + "\\" + cCode + "_个人信息" +
		// ".docx";
		logger.info("打印绝对路径path：" + path2);
		// 模板文档的路径
//		String realPath2 = request.getServletContext().getRealPath("WEB-INF/WordTemplate/办理户口呈批表.docx");
		String realPath2 = config.getTemplatePath() + "办理户口呈批表.docx";
		logger.info("个人信息模板文档地址:" + realPath2);
		logger.info("个人信息生成文档地址:" + path2);
		// 生成Word
		ToWord.getInstance().toWord(datas2, realPath2, path2);

		// 生成一个JSON存入数据库
		JSONObject jsonObject = new JSONObject();
		for (Map.Entry<String, Object> entry : datas2.entrySet()) {
			jsonObject.put(entry.getKey(), entry.getValue());
		}

		//获取签名值，即文档的hash值
		String sign = "";
		if(config.getEIDON()) {
			String hashValue = DigestUtils.md5Hex(new FileInputStream(path2));
			logger.info("夫妻投靠生成文档的哈希值：" + hashValue);
			//然后对哈希值进行签名
			JSONObject resultjson = sIMeIDService.smsDirect(SIMeIDphone, hashValue);
			if (resultjson == null || "".equals(resultjson))
				return WebResult.error(ERRORDetail.EID_0601001);
			logger.info("SIMeID签名返回结果：" + resultjson);
			sign = resultjson.getString("eid_sign");
			if (sign == null || "".equals(sign))
				return WebResult.error(ERRORDetail.EID_0601001);
			logger.info("SIMeID签名值signal：" + sign);
		}
		/*
		 * jsonObject.put("name", name); jsonObject.put("policeStation",
		 * policeStation); jsonObject.put("sex", sex); jsonObject.put("idCard",
		 * idCard); jsonObject.put("telphone", telphone);
		 * jsonObject.put("relative", relative); jsonObject.put("reName",
		 * reName); jsonObject.put("reIdCard", reIdCard);
		 * jsonObject.put("birth", birth); jsonObject.put("domicile",
		 * domicile); jsonObject.put("redomicile", redomicile);
		 */
		String jsonString = JSONObject.toJSONString(jsonObject);
		logger.info("打印JSONString字符串:" + jsonString);
		logger.info("打印policeStation字符串:" + policeStation);
		PoliceStationExample policeStationExample = new PoliceStationExample();
		policeStationExample.createCriteria().andPoliceStationEqualTo(policeStation.replaceAll(" ", ""));
		List<PoliceStation> policeStations = policeStationMapper.selectByExample(policeStationExample);
		logger.info(JSONObject.toJSONString(policeStations));

		Object data1 = coupleAwartService.coupleAwartService(sign,openId,appeidcode,cCode, jsonString, relativePath1, relativePath2,
				hkphotoPath, idCardPath, mcPicture3, picturePath,
				policeStations.size() > 0 ? policeStations.get(0).getTid() : null);
		logger.info("返回数据data1:" + data1);
        List<String> openIdArr = coupleAwartService.getOpenIdArr(1,policeStations.size() > 0 ? policeStations.get(0).getTid() : null);
        for (String openID:openIdArr)
            WxUtils.sendToM(managerUrl,name, cCode,openID,"夫妻投靠");
		return data1;
	}

	/**
	 * 改变申请记录的状态，审核
	 */
	@PostMapping(value = "/awart/awartcheck")
	@ResponseBody
	@ApiOperation(value = "夫妻投靠审核", httpMethod = "POST", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "bizId", value = "申请记录唯一标识", required = true, paramType = "query"),
			@ApiImplicitParam(name = "state", value = "改变申请状态，只可为1成功，2失败", required = true, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "remark", value = "审核失败说明", required = true, paramType = "query", dataType = "Integer")})
	public Object awartCheck(String bizId, Integer state,String remark) {
		if (!(state == 1 || state == 2) || bizId == null)
			return WebResult.error(ERRORDetail.BUS_0501004);
		return coupleAwartService.awartCheck(bizId, state,remark);
	}

	/**
	 * 夫妻投靠公众号查询申请记录数据
	 *@param bizId
	 * 业务流水号 可为空
	 */
	@GetMapping(value = "/awart/getresult")
	@ResponseBody
	@ApiOperation(value = "获取夫妻投靠申请记录数据", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "bizId", value = "业务流水号，申请记录唯一标识", required = false, paramType = "query")})
	@ApiResponses({@ApiResponse(code = 200, message = "{\"errCode\":\"00\",\"errMsg\":\"0000000(成功)\",\"data\":{\"personalinfo\"(申请记录信息):{\"reName\":\"路人乙\",\"idCard\":\"360732199605283659\",\"reSex\":\"女\",\"sex\":\"男\",\"redomicile\":\"兴国\"," +
			"\"policeStation\":\"茶山派出所\",\"reIdCard\":\"360732199602013657\",\"telphone\":\"15570146381\",\"birth\":\"1918年01月01日\",\"name\":\"陈剑辉\",\"domicile\":\"兴国\",\"domicile1\":\"兴国\",\"relative\":\"夫妻关系\"}," +
			"\"domicilephoto\"（户口本照片地址）:\"CoupleAwart/photo/4db9c0601043411e8f90d500922554ac/0.png;CoupleAwart/photo/4db9c0601043411e8f90d500922554ac/1.png;CoupleAwart/photo/4db9c0601043411e8f90d500922554ac/2.png;CoupleAwart/photo/4db9c0601043411e8f90d500922554ac/3.png\"," +
			"\"currentphoto\"（现场确认照片地址）:\"CoupleAwart/photo/4db9c0601043411e8f90d500922554ac/9.png\",\"bizId\"（业务流水号）:\"4db9c0601043411e8f90d500922554ac\",\"marryphoto\"（结婚证照片地址）:\"CoupleAwart/photo/4db9c0601043411e8f90d500922554ac/4.png\",\"photowordpath\"（照片信息word的地址）:\"CoupleAwart/4db9c0601043411e8f90d500922554ac_1.docx\",\"state\"（状态）:0," +
			"\"idcardphoto\"（身份证照片地址）:\"CoupleAwart/photo/4db9c0601043411e8f90d500922554ac/5.png;CoupleAwart/photo/4db9c0601043411e8f90d500922554ac/6.png;CoupleAwart/photo/4db9c0601043411e8f90d500922554ac/7.png;CoupleAwart/photo/4db9c0601043411e8f90d500922554ac/8.png\"," +
			"\"time\"（申请时间）:\"2018-03-09T13:29:17.000+0000\",\"perinfowordpath\"（申请表word地址）:\"CoupleAwart/4db9c0601043411e8f90d500922554ac_2.docx\"}}}")})
	public Object getResult(HttpServletRequest request,String bizId) {
		HttpSession session = request.getSession();
		if (session == null)
			return WebResult.error(ERRORDetail.GQ_0103004);
		String appeidcode = (String) session.getAttribute("user");
		if (appeidcode == null || "".equals(appeidcode))
			return WebResult.error(ERRORDetail.GQ_0103004);
		String openId = (String)session.getAttribute("openId");
		if (openId == null || "".equals(openId))
			return WebResult.error(ERRORDetail.GQ_0103004);
		logger.info("接收到的appeidcode：" + appeidcode);
		logger.info("接收到的bizId:"+bizId);
		logger.info("接收到的openId:"+openId);
		if(bizId == null)
			bizId = coupleAwartService.getBizId(appeidcode);
		logger.info("接收到的bizId:"+bizId);
		if(bizId == null)
			return WebResult.error(ERRORDetail.COU_0701004);
//		String appeidcode = "CGzWLrYopSYndqrsw1ljI+8v5NPgyvSAGOpip0PJmKMxMDAw";
		//判断管理员是否有权限审核这个业务
		if(!coupleAwartService.checkManager(bizId,openId))
			return WebResult.error(ERRORDetail.COU_0701006);
		Object object = coupleAwartService.getResult(bizId);
		logger.info("object：" + JSONObject.toJSONString(object));
		return object;
	}

	// 查询警察局的名称
	@GetMapping(value = "/awart/policestation")
	@ResponseBody
	@ApiOperation(value = "查询所有派出所名称", httpMethod = "GET", produces = "application/json")
	@ApiResponses({@ApiResponse(code = 200, message = "{\"errCode\":\"00\",\"errMsg\":\"0000000(成功)\",\"data\":{\"policeStation\":[\"茶山派出所\",\"苏家垱派出所\",\"金湖派出所\",\"江益派出所\",\"甘露派出所\"]}}")})
	public Object policeStation() {
		logger.info("查询警察局名称");
		return coupleAwartService.policeStation();
	}
}
