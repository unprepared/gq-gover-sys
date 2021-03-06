package com.hc.gqgs.mybatis.po;

public class RadioQuestion {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column radio_question.question_id
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    private Integer questionId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column radio_question.title
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    private String title;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column radio_question.img
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    private String img;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column radio_question.options
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    private String options;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column radio_question.right_option_id
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    private Integer rightOptionId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column radio_question.question_id
     *
     * @return the value of radio_question.question_id
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column radio_question.question_id
     *
     * @param questionId the value for radio_question.question_id
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column radio_question.title
     *
     * @return the value of radio_question.title
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column radio_question.title
     *
     * @param title the value for radio_question.title
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column radio_question.img
     *
     * @return the value of radio_question.img
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public String getImg() {
        return img;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column radio_question.img
     *
     * @param img the value for radio_question.img
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public void setImg(String img) {
        this.img = img == null ? null : img.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column radio_question.options
     *
     * @return the value of radio_question.options
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public String getOptions() {
        return options;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column radio_question.options
     *
     * @param options the value for radio_question.options
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public void setOptions(String options) {
        this.options = options == null ? null : options.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column radio_question.right_option_id
     *
     * @return the value of radio_question.right_option_id
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public Integer getRightOptionId() {
        return rightOptionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column radio_question.right_option_id
     *
     * @param rightOptionId the value for radio_question.right_option_id
     *
     * @mbggenerated Sun Mar 11 17:45:24 CST 2018
     */
    public void setRightOptionId(Integer rightOptionId) {
        this.rightOptionId = rightOptionId;
    }
}