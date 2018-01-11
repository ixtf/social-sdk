package org.jzb.weixin.mp.util;

public class MpConstant {
    public static final String TOKEN_URL_TPL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=${appid}&secret=${secret}";
    public static final String AUTHORIZE_URL_TPL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&redirect_uri=${redirect_uri}&response_type=code&scope=snsapi_userinfo&state=${state}#wechat_redirect";
    public static final String AUTHORIZE_TOKEN_URL_TPL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=${appid}&secret=${secret}&code=${code}&grant_type=authorization_code";
    public static final String AUTHORIZE_UNION_INFO_URL_TPL = "https://api.weixin.qq.com/sns/userinfo?access_token=${access_token}&openid=${openid}&lang=${lang}";
    public static final String JSAPI_TICKET_URL_TPL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=${access_token}&type=jsapi";
    public static final String QRCODE_URL_TPL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=${access_token}";
    public static final String SHOW_QRCODE_URL_TPL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${ticket}";
    public static final String UNION_INFO_URL_TPL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=${access_token}&openid=${openid}&lang=${lang}";
    public static final String KF_MESSAGE_SEND_URL_TPL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=${access_token}";
    public static final String TPL_MESSAGE_SEND_URL_TPL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=${access_token}";
}
