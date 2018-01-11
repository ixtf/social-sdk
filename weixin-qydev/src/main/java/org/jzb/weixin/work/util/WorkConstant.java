package org.jzb.weixin.work.util;

public class WorkConstant {
    public static final String TOKEN_URL_TPL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=${corpid}&corpsecret=${corpsecret}";
    public static final String MESSAGE_SEND_URL_TPL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=${access_token}";
}
