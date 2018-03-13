package com.hc.gqgs.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hc.gqgs.json.ERRORDetail;
import com.hc.gqgs.json.WebResult;
import com.hc.gqgs.mybatis.mapper.*;
import com.hc.gqgs.mybatis.mapper.business.OtherMapper;
import com.hc.gqgs.mybatis.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    PersonalDetailsMapper personalDetailsMapper;
    @Autowired
    OtherMapper otherMapper;
    @Autowired
    RadioQuestionMapper radioQuestionMapper;
    @Autowired
    LearnDataMapper learnDataMapper;
    @Autowired
    WxManageMapper wxManageMapper;

    /**
     * 根据apeidcode查询数据
     */
    public JSONArray queryByApeidCode(String apeidcode) {
        JSONArray jsonArray = new JSONArray();
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andAppeidcodeEqualTo(apeidcode);
        List<UserInfoWithBLOBs> list = userInfoMapper.selectByExampleWithBLOBs(userInfoExample);
        if (list != null) {
            JSONObject json;
            for (UserInfoWithBLOBs user : list) {
                json = new JSONObject();
                json.put("bizId", user.getBizId());
                json.put("state", user.getState());
                json.put("time", JSONObject.parseObject(user.getPersonInfo()).get("time"));
                json.put("learningInfo", JSONObject.parseObject(user.getLearnInfo()));
                jsonArray.add(json);
            }
        }
        return jsonArray;
    }

    /**
     * 根据apeidcode查询业务流水号
     *
     * @param apeidcode
     * @return
     */
    public String queryBizId(String apeidcode) {
        String bizId = null;
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andAppeidcodeEqualTo(apeidcode);
        List<UserInfoWithBLOBs> list = userInfoMapper.selectByExampleWithBLOBs(userInfoExample);
        if (list.size() > 1) {
            for (UserInfoWithBLOBs user : list) {
                String state = user.getState();
                if (state.equals("1") || state.equals("2") || state.equals("3")) {
                    bizId = user.getBizId();
                    break;
                }
            }
        } else if (list.size() == 1) {
            bizId = list.get(0).getBizId();
        }
        return bizId;
    }

    public boolean insertPersonal(PersonalDetails personalDetails) {
        PersonalDetailsExample personalDetailsExample = new PersonalDetailsExample();
        personalDetailsExample.createCriteria().andAppeidcodeEqualTo(personalDetails.getAppeidcode());
        List<PersonalDetails> list = personalDetailsMapper.selectByExample(personalDetailsExample);
        if (!(list == null || list.size() < 1)) {
            return false;
        }
        personalDetailsMapper.insertSelective(personalDetails);
        return true;
    }

    /**
     * 根据apeidcode查询个人信息
     *
     * @param apeidcode
     * @return
     */
    public PersonalDetails selByApeid(String apeidcode) {
        PersonalDetailsExample personalDetailsExample = new PersonalDetailsExample();
        personalDetailsExample.createCriteria().andAppeidcodeEqualTo(apeidcode);
        List<PersonalDetails> list = personalDetailsMapper.selectByExample(personalDetailsExample);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 插入个人信息到数据库
     */
    public boolean insertPersonalInfo(UserInfoWithBLOBs userInfoWithBLOBs) {
        boolean flag = false;
        userInfoWithBLOBs.setState("1");
        int num = userInfoMapper.insertSelective(userInfoWithBLOBs);
        if (num > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 根据业务流水号查询信息
     *
     * @param bizId 业务流水号
     * @return 用户学习信息
     */
    public UserInfoWithBLOBs queryById(String bizId) {
//		System.out.println(bizId);
        UserInfoWithBLOBs userInfoWithBLOBs = userInfoMapper.selectByPrimaryKey(bizId);
        return userInfoWithBLOBs;
    }

    /**
     * 覆盖用户学习记录
     *
     * @param userInfoWithBLOBs 用户学习记录
     * @return true false
     */
    public boolean updateUserInfo(UserInfoWithBLOBs userInfoWithBLOBs) {
        boolean flag = false;
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andBizIdEqualTo(userInfoWithBLOBs.getBizId());
        int num = userInfoMapper.updateByExampleSelective(userInfoWithBLOBs, userInfoExample);
        if (num > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 根据业务流水号查询用户状态返回不同内容
     *
     * @param bizId 业务流水号
     * @return
     */
    public WebResult getAll(String bizId) {
        JSONObject jsonObject = new JSONObject();
        UserInfoWithBLOBs userInfoWithBLOBs = userInfoMapper.selectByPrimaryKey(bizId);
        if (userInfoWithBLOBs == null)
            return WebResult.error(ERRORDetail.TFC_0101007);
        String state = userInfoWithBLOBs.getState();
        jsonObject.put("state", state);
        jsonObject.put("bizId", bizId);
        switch (state) {
            case "1":
                if (userInfoWithBLOBs.getPersonInfo() == null)
                    return WebResult.error(ERRORDetail.TFC_0101014);
                JSONObject personInfo = JSONObject.parseObject(userInfoWithBLOBs.getPersonInfo());
                jsonObject.put("personInfo", personInfo);
                if (userInfoWithBLOBs.getLearnInfo() != null)
                    jsonObject.put("learningInfo", JSONObject.parseObject(userInfoWithBLOBs.getLearnInfo()));
                if (userInfoWithBLOBs.getValiteInfo() == null)
                    jsonObject.put("valiteInfo", new JSONArray());
                else
                    jsonObject.put("valiteInfo", JSONObject.parseArray(userInfoWithBLOBs.getValiteInfo()));
                jsonObject.put("video", queryLearnData());
                break;
            case "2":
                if (userInfoWithBLOBs.getPersonInfo() == null)
                    return WebResult.error(ERRORDetail.TFC_0101014);
                JSONObject personInfo2 = JSONObject.parseObject(userInfoWithBLOBs.getPersonInfo());
                jsonObject.put("personInfo", personInfo2);
                if (userInfoWithBLOBs.getLearnInfo() == null)
                    return WebResult.error(ERRORDetail.TFC_0101012);
                jsonObject.put("learningInfo", JSONObject.parseObject(userInfoWithBLOBs.getLearnInfo()));
                if (userInfoWithBLOBs.getValiteInfo() == null)
                    return WebResult.error(ERRORDetail.TFC_0101013);
                jsonObject.put("valiteInfo", JSONObject.parseArray(userInfoWithBLOBs.getValiteInfo()));
                jsonObject.put("questions", selRand());
                jsonObject.put("video", queryLearnData());
                break;
            default:
                if (userInfoWithBLOBs.getPersonInfo() == null)
                    return WebResult.error(ERRORDetail.TFC_0101014);
                JSONObject personInfo3 = JSONObject.parseObject(userInfoWithBLOBs.getPersonInfo());
                jsonObject.put("personInfo", personInfo3);
                if (userInfoWithBLOBs.getLearnInfo() == null)
                    return WebResult.error(ERRORDetail.TFC_0101012);
                jsonObject.put("learningInfo", JSONObject.parseObject(userInfoWithBLOBs.getLearnInfo()));
                if (userInfoWithBLOBs.getValiteInfo() == null)
                    return WebResult.error(ERRORDetail.TFC_0101013);
                jsonObject.put("valiteInfo", JSONObject.parseArray(userInfoWithBLOBs.getValiteInfo()));
                JSONObject questionInfo = getQuestion(bizId);
                jsonObject.put("questions", questionInfo.getJSONArray("question"));
                jsonObject.put("answers", questionInfo.getJSONObject("answer"));
                jsonObject.put("video", queryLearnData());
                break;
        }
        return WebResult.success(jsonObject);
    }

    /**
     * 更改用户学习状态
     *
     * @param bizId 业务流水号
     */
    public boolean updateState(String bizId) {
        UserInfoWithBLOBs userInfoWithBLOBs = userInfoMapper.selectByPrimaryKey(bizId);
        if (userInfoWithBLOBs.getValiteInfo() == null)
            return false;
        userInfoWithBLOBs.setState("2");
        userInfoMapper.updateByPrimaryKeySelective(userInfoWithBLOBs);
        return true;
    }

    /**
     * 更改用户学习状态
     *
     * @param userInfoWithBLOBs
     * @return 用户微信号
     */
    public String updateState(UserInfoWithBLOBs userInfoWithBLOBs) {
        String openId = "";
        int count = userInfoMapper.updateByPrimaryKeySelective(userInfoWithBLOBs);
        if (count == 0) {
            return null;
        }
        UserInfoWithBLOBs userInfo = userInfoMapper.selectByPrimaryKey(userInfoWithBLOBs.getBizId());
        JSONObject json = JSONObject.parseObject(userInfo.getPersonInfo());
        openId = json.getString("wxOpenId");
        return openId;
    }

    /**
     * 获取用户学习状态
     *
     * @param bizId 业务流水号
     * @return
     */
    public String getState(String bizId) {
        UserInfoWithBLOBs user = userInfoMapper.selectByPrimaryKey(bizId);
        String state = user.getState();
        return state;
    }

    /**
     * 从数据库随机抽取题目
     *
     * @return 题目
     */
    public Object selRand() {
        List<RadioQuestion> questions = otherMapper.selRand();
        if (null == questions || questions.isEmpty()) {
            return null;
        }
        List<JSONObject> question = new ArrayList<JSONObject>();
        JSONObject item;
        for (RadioQuestion rq : questions) {
            item = new JSONObject();
            item.put("questionId", rq.getQuestionId());
            item.put("title", rq.getTitle());
            item.put("img", rq.getImg());
            item.put("options", JSONObject.parseArray(rq.getOptions()));
            item.put("rightOptionId", rq.getRightOptionId());
            question.add(item);
        }
        return question;
    }

    /**
     * 把用户做过的题目插入数据库
     *
     * @param bizId  业务流水号
     * @param option 用户的答案
     * @return true false
     */
    public boolean insertQuestion(String bizId, String option) {
        boolean flag = false;
        UserInfoWithBLOBs user = new UserInfoWithBLOBs();
        user.setBizId(bizId);
        user.setQuestionInfo(option);
        user.setState("3");
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andBizIdEqualTo(user.getBizId());
        int count = userInfoMapper.updateByExampleSelective(user, userInfoExample);
        if (count > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 还原用户做过的题目
     *
     * @param bizId 业务流水号
     * @return
     */
    public JSONObject getQuestion(String bizId) {
        JSONObject questionAll = new JSONObject();
        UserInfoWithBLOBs user = userInfoMapper.selectByPrimaryKey(bizId);// 查询用户信息
        if (user.getQuestionInfo() == null)
            return null;
        JSONObject questionInfo = JSONObject.parseObject(user.getQuestionInfo());// {"题目id":"选项id","题目id":"选项id"}
        List<Integer> questionIds = new ArrayList<Integer>();// 存储题目id
        for (String k : questionInfo.keySet()) {
            questionIds.add(Integer.parseInt(k));
        }
        // 查找50道题目
        RadioQuestionExample radioQuestionExample = new RadioQuestionExample();
        radioQuestionExample.createCriteria().andQuestionIdIn(questionIds);
        List<RadioQuestion> questions = radioQuestionMapper.selectByExample(radioQuestionExample);
        List<JSONObject> question = new ArrayList<JSONObject>();
        JSONObject item;
        for (RadioQuestion rq : questions) {
            item = new JSONObject();
            item.put("questionId", rq.getQuestionId());
            item.put("title", rq.getTitle());
            item.put("img", rq.getImg());
            item.put("options", JSONObject.parseArray(rq.getOptions()));
            item.put("rightOptionId", rq.getRightOptionId());
            question.add(item);
        }
        questionAll.put("question", question);
        questionAll.put("answer", questionInfo);
        return questionAll;
    }

    /**
     * 抽取一道题目
     *
     * @param questions
     * @return
     */
    public Object selOne(String[] questions) {
        JSONObject json = new JSONObject();
        RadioQuestion radioQuestion = otherMapper.selOne(questions);
        json.put("questionId", radioQuestion.getQuestionId());
        json.put("title", radioQuestion.getTitle());
        json.put("img", radioQuestion.getImg());
        json.put("options", JSONObject.parseArray(radioQuestion.getOptions()));
        json.put("rightOptionId", radioQuestion.getRightOptionId());
        return json;
    }

    /**
     * 获取管理员微信号
     *
     * @return
     */
    public List<String> getOpenId() {
        WxManageExample wxManageExample = new WxManageExample();
        WxManageExample.Criteria criteria = wxManageExample.createCriteria();
        criteria.andTypeEqualTo(3);
        List<WxManage> list = wxManageMapper.selectByExample(wxManageExample);
        List<String> openId = new ArrayList<>();
        for (WxManage wxManage : list) {
            openId.add(wxManage.getWxOpenId());
        }
        return openId;
    }

    public boolean findOpenId(String openId) {
        WxManageExample wxManageExample = new WxManageExample();
        wxManageExample.createCriteria().andWxOpenIdEqualTo(openId);
        List<WxManage> list = wxManageMapper.selectByExample(wxManageExample);
        if (list == null || list.size() < 1) {
            return false;
        }
        return true;
    }

    /**
     * 补全用户信息
     *
     * @param personalDetails
     * @return
     */
    public boolean perfect(PersonalDetails personalDetails) {
        PersonalDetailsExample personalDetailsExample = new PersonalDetailsExample();
        personalDetailsExample.createCriteria().andAppeidcodeEqualTo(personalDetails.getAppeidcode());
        int count = personalDetailsMapper.updateByExampleSelective(personalDetails, personalDetailsExample);
        if (count < 1) {
            return false;
        }
        return true;
    }

    /**
     * 查询所有视频
     *
     * @return
     */
    public List<LearnData> queryLearnData() {
        List<LearnData> list = learnDataMapper.selectByExample(null);
        return list;
    }

    /**
     * 根据手机号查询用户apeidcode（测试用）
     */
    public String queryByPhone(String telphone) {
        PersonalDetailsExample personalDetailsExample = new PersonalDetailsExample();
        personalDetailsExample.createCriteria().andTelphoneEqualTo(telphone);
        List<PersonalDetails> list = personalDetailsMapper.selectByExample(personalDetailsExample);
        if (list == null || list.size() < 1) {
            return null;
        }
        String apeidcode = list.get(0).getAppeidcode();
        return apeidcode;
    }
}
