package org.jzb.weinxin.open.util;

public class OpenConstant {
    public static final String AUTHORIZE_TOKEN_URL_TPL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=${appid}&secret=${secret}&code=${code}&grant_type=authorization_code";
    public static final String AUTHORIZE_UNION_INFO_URL_TPL = "https://api.weixin.qq.com/sns/userinfo?access_token=${access_token}&openid=${openid}&lang=${lang}";
}
