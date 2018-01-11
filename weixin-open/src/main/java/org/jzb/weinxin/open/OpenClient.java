package org.jzb.weinxin.open;

import org.apache.commons.lang3.Validate;
import org.jzb.weinxin.open.authorizeCode.OpenAuthorizeCodeRequest;

import java.util.Properties;


/**
 * 描述：
 *
 * @author jzb 2017-10-25
 */
public class OpenClient {
    private final String appid;
    private final String secret;

    private OpenClient(String appid, String secret) {
        Validate.notBlank(appid);
        Validate.notBlank(secret);
        this.appid = appid;
        this.secret = secret;
    }

    public static OpenClient getInstance(Properties p) {
        final String appid = p.getProperty("appid");
        final String secret = p.getProperty("secret");
        return new OpenClient(appid, secret);
    }

    public OpenAuthorizeCodeRequest authorizeCode(String code, String state) {
        return new OpenAuthorizeCodeRequest(this, code, state);
    }

    public String getAppid() {
        return appid;
    }

    public String getSecret() {
        return secret;
    }
}
