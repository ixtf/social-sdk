package org.jzb.weixin.mp;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;
import org.jzb.J;
import org.jzb.social.core.AbstractExpireToken;
import org.jzb.weixin.mp.authorizeCode.MpAuthorizeCodeRequest;
import org.jzb.weixin.mp.msg_kf.MpMsgKfRequest;
import org.jzb.weixin.mp.msg_tpl.MpMsgTplRequest;
import org.jzb.weixin.mp.qrcode.MpQrcodeRequest;
import org.jzb.weixin.mp.unionInfo.MpUnionInfoRequest;
import org.jzb.weixin.mp.util.WXBizMsgCrypt;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static org.jzb.weixin.mp.util.MpConstant.AUTHORIZE_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-24
 */
public class MpClient {
    private static final Map<String, MpClient> MAP = Maps.newConcurrentMap();
    private final String appid;
    private final String secret;
    private final WXBizMsgCrypt mpBizMsgCrypt;
    private final AbstractExpireToken<String> accessToken;
    private final MpJsapi jsapi;

    private MpClient(String appid, String token, String secret, String encodingaeskey, AbstractExpireToken<String> accessToken) {
        Validate.notBlank(appid);
        Validate.notBlank(secret);
        Validate.notBlank(token);
        Validate.notBlank(encodingaeskey);
        this.appid = appid;
        this.secret = secret;
        this.accessToken = accessToken;
        this.jsapi = new MpJsapi(this);
        try {
            this.mpBizMsgCrypt = new WXBizMsgCrypt(token, encodingaeskey, appid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MpClient getInstance(Properties p) {
        final String appid = p.getProperty("appid");
        return MAP.compute(appid, (k, v) -> Optional.ofNullable(v)
                .orElseGet(() -> {
                    final String token = p.getProperty("token");
                    final String secret = p.getProperty("secret");
                    final String encodingaeskey = p.getProperty("encodingaeskey");
                    return new MpClient(appid, token, secret, encodingaeskey, new MpAccessToken(appid, secret));
                }));
    }

    public static MpClient getInstance(Properties p, AbstractExpireToken<String> accessToken) {
        final String appid = p.getProperty("appid");
        return MAP.compute(appid, (k, v) -> Optional.ofNullable(v)
                .orElseGet(() -> {
                    final String token = p.getProperty("token");
                    final String secret = p.getProperty("secret");
                    final String encodingaeskey = p.getProperty("encodingaeskey");
                    return new MpClient(appid, token, secret, encodingaeskey, accessToken);
                }));
    }

    public Map<String, String> jsConfig(String url) throws Exception {
        return jsapi.config(url);
    }

    public String decryptMsg(String msgSignature, String timeStamp, String nonce, String postData) throws Exception {
        return mpBizMsgCrypt.decryptMsg(msgSignature, timeStamp, nonce, postData);
    }

    public MpMsgKfRequest.Builder msgKf(String openid) {
        return new MpMsgKfRequest.Builder(this, openid);
    }

    public MpMsgTplRequest.Builder msgTpl(String openid) {
        return new MpMsgTplRequest.Builder(this).touser(openid);
    }

    public MpQrcodeRequest.Builder qrcode() {
        return new MpQrcodeRequest.Builder(this);
    }

    public MpUnionInfoRequest.Builder unionInfo(String openid) {
        return new MpUnionInfoRequest.Builder(this).openid(openid);
    }


    public String verifyUrl(String msgSignature, String timeStamp, String nonce, String echoStr) throws Exception {
        return mpBizMsgCrypt.verifyUrl(msgSignature, timeStamp, nonce, echoStr);
    }

    /**
     * 微信网页授权,构造跳转链接
     * 尤其注意：由于授权操作安全等级较高，所以在发起授权请求时，微信会对授权链接做正则强匹配校验，如果链接的参数顺序不对，授权页面将无法正常访问
     */
    public String authorizeUrl(String redirect_uri, String state) {
        Validate.notBlank(redirect_uri);
        redirect_uri = J.urlEncode(redirect_uri);
        state = J.defaultString(state, "123");
        final ImmutableMap<String, String> map = ImmutableMap.of("appid", appid, "redirect_uri", redirect_uri, "state", state);
        return J.strTpl(AUTHORIZE_URL_TPL, map);
    }

    public MpAuthorizeCodeRequest authorizeCode(String code, String state) {
        return new MpAuthorizeCodeRequest(this, code, state);
    }

    public String access_token() throws Exception {
        return accessToken.get();
    }

    public String getAppid() {
        return appid;
    }

    public String getSecret() {
        return secret;
    }
}
