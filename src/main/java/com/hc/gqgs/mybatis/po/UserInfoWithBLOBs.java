package com.hc.gqgs.mybatis.po;

public class UserInfoWithBLOBs extends UserInfo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_info.person_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    private String personInfo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_info.learn_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    private String learnInfo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_info.valite_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    private String valiteInfo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_info.question_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    private String questionInfo;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_info.person_info
     *
     * @return the value of user_info.person_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public String getPersonInfo() {
        return personInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_info.person_info
     *
     * @param personInfo the value for user_info.person_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public void setPersonInfo(String personInfo) {
        this.personInfo = personInfo == null ? null : personInfo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_info.learn_info
     *
     * @return the value of user_info.learn_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public String getLearnInfo() {
        return learnInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_info.learn_info
     *
     * @param learnInfo the value for user_info.learn_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public void setLearnInfo(String learnInfo) {
        this.learnInfo = learnInfo == null ? null : learnInfo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_info.valite_info
     *
     * @return the value of user_info.valite_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public String getValiteInfo() {
        return valiteInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_info.valite_info
     *
     * @param valiteInfo the value for user_info.valite_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public void setValiteInfo(String valiteInfo) {
        this.valiteInfo = valiteInfo == null ? null : valiteInfo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_info.question_info
     *
     * @return the value of user_info.question_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public String getQuestionInfo() {
        return questionInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_info.question_info
     *
     * @param questionInfo the value for user_info.question_info
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public void setQuestionInfo(String questionInfo) {
        this.questionInfo = questionInfo == null ? null : questionInfo.trim();
    }
}