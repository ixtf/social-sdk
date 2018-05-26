package org.jzb.weixin.work.util;

public class WorkConstant {
    public static final String TOKEN_URL_TPL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=${corpid}&corpsecret=${corpsecret}";
    public static final String MESSAGE_SEND_URL_TPL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=${access_token}";
    public static final String GET_CHECKIN_DATA_URL_TPL = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token=${access_token}";
    public static final String USER_SIMPLE_LIST_URL_TPL = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=${access_token}&department_id=${department_id}&fetch_child=${fetch_child}";
    public static final String USER_LIST_URL_TPL = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=${access_token}&department_id=${department_id}&fetch_child=${fetch_child}";
    public static final String USER_GET_URL_TPL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=${access_token}&userid=${userid}";
    public static final String TAG_GET_URL_TPL = "https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token=${access_token}&tagid=${tagid}";
    public static final String TAG_LIST_URL_TPL = "https://qyapi.weixin.qq.com/cgi-bin/tag/list?access_token=${access_token}";
    public static final String AGENT_GET_URL_TPL = "https://qyapi.weixin.qq.com/cgi-bin/agent/get?access_token=${access_token}&agentid=${agentid}";
}
