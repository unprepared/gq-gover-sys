package com.hc.gqgs.json;


/**
 * 平台错误描述定义，可根据索引查询具体错误描述。
 */
public enum ERRORDetail {

    //系统错误
    GQ_0000000("0000000", "成功"),
    GQ_0401001("0401001", "服务异常"),
    GQ_0103002("0103002","无权访问"),
    SQL_0201005("0201005", "数据库错误"),
    GQ_0103004("4300000","登录失效，请重新登陆"),
	//夫妻投靠错误结果
	COU_0701001("0701001","派出所所查为空"),
	COU_0701002("0701002","必要参数为空"),
	COU_0701003("0701003","图片少了"),
    COU_0701004("0701004","不存在申请记录"),
    COU_0701006("0701005","管理员没有权限"),

	//二次签注错误结果
	SEC_0801001("0801001","必要参数为空"),
	SEC_0801002("0801002","已提交申请，并且未处理"),
	SEC_0801003("0801003","查询唯一标识，身份证号码为空"),
	SEC_0801004("0801004","查询唯一标识为空"),
    SEC_0801005("0801005","不存在申请记录"),
    SEC_0801006("0801005","管理员没有权限"),

    //业务逻辑错误结果（所属Business）
    BUS_0501001("0501001","pagination或filter数据为空"),
    BUS_0501002("0501002","必要参数为空"),
    BUS_0501003("0501003","申请记录不存在"),
    BUS_0501004("0501004","改变申请状态必要参数为空或错误"),
    BUS_0501006("0501006","申请记录不存在或已处理"),

    //交警违规参数错误【参数为空】
    TFC_0101001("0101001", "参数为空"),
    TFC_0101002("0101002", "身份证号码为空"),
    TFC_0101003("0101003", "校验信息为空"),
    TFC_0101004("0101004", "学习信息为空"),
    TFC_0101005("0101005", "客户端时间为空"),
    TFC_0101006("0101006", "时间不合法"),
    TFC_0101007("0101007", "用户不存在"),
    TFC_0101008("0101008", "业务流水号不存在"),
    TFC_0101009("0101009", "插入数据库失败"),
    TFC_0101010("0101010", "更新数据库失败"),
    TFC_0101011("0101011", "图片下载失败"),

    TFC_0101012("0101012", "找不到学习记录"),
    TFC_0101013("0101013", "找不到校验记录"),
    TFC_0101014("0101014", "找不到个人信息"),
    TFC_0101015("0101015", "找不到任何debug信息"),
    TFC_0101016("0101016", "msisdn或用户未注册"),
    TFC_0101017("0101017", "抱歉，你没有权限"),

    EID_0601001("0601001","SIMeID服务异常"),
	
	GQ_6666666("0500000","异常,没有找到合适的结果描述");


    private String index;
    private String meaning;

    ERRORDetail(String index, String meaning) {
        this.index = index;
        this.setMeaning(meaning);
    }

    public String getIndex() {
        return this.index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
    
    public String getResultCode(){
    	return this.index.substring(0,2);
    }
    
    public String getDetails(){
    	return this.index+"("+this.meaning+")";
    }

    public static ERRORDetail getEnum(String index) {
        for (ERRORDetail st : ERRORDetail.values()) {
            if (st.index.equals(index)) {
                return st;
            }
        }
        return GQ_6666666;
    }
    
    

}
