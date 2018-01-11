package org.jzb.weixin.mp;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jzb.J;
import org.jzb.social.core.AbstractExpireToken;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.jzb.Constant.MAPPER;
import static org.jzb.social.core.Constant.OK_CLIENT;
import static org.jzb.weixin.mp.util.MpConstant.TOKEN_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-24
 */
class MpAccessToken extends AbstractExpireToken<String> {
    private final String appid;
    private final String secret;
    private int expires_in;

    MpAccessToken(String appid, String secret) {
        this.appid = appid;
        this.secret = secret;
    }

    protected void fetch() throws Exception {
        final ImmutableMap<String, String> map = ImmutableMap.of("appid", appid, "secret", secret);
        final Request request = new Request.Builder()
                .url(J.strTpl(TOKEN_URL_TPL, map))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                final Integer errcode = Optional.ofNullable(node.get("errcode"))
                        .map(JsonNode::asInt)
                        .orElse(0);
                if (errcode > 0) {
                    throw new Exception(node.toString());
                }
                final String access_token = node.get("access_token").asText();
                setT(access_token);
                final long expiresInMillis = getCreateInMillis() + TimeUnit.SECONDS.toMillis(expires_in - 30 * 60);
                setExpiresInMillis(expiresInMillis);
                expires_in = node.get("expires_in").asInt();
            }
        }
    }
}
