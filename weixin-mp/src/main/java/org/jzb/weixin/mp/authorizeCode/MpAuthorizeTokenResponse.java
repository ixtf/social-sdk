package org.jzb.weixin.mp.authorizeCode;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jzb.J;
import org.jzb.weixin.mp.MpClient;

import java.io.InputStream;
import java.util.concurrent.Callable;

import static org.jzb.Constant.MAPPER;
import static org.jzb.social.core.Constant.OK_CLIENT;
import static org.jzb.weixin.mp.util.MpConstant.AUTHORIZE_UNION_INFO_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class MpAuthorizeTokenResponse implements Callable<MpAuthorizeUnionInfoResponse> {
    private final MpClient client;
    private final JsonNode node;

    public MpAuthorizeTokenResponse(MpClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    @Override
    public MpAuthorizeUnionInfoResponse call() throws Exception {
        final Request request = new Request.Builder()
                .url(J.strTpl(AUTHORIZE_UNION_INFO_URL_TPL, ImmutableMap.of("access_token", access_token(), "openid", openid(), "lang", "zh_CN")))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                return new MpAuthorizeUnionInfoResponse(client, node);
            }
        }
    }

    private String openid() {
        return node.get("openid").asText();
    }

    private String scope() {
        return node.get("scope").asText();
    }

    private String access_token() {
        return node.get("access_token").asText();
    }

    private int expires_in() {
        return node.get("expires_in").asInt();
    }

    private String refresh_token() {
        return node.get("refresh_token").asText();
    }
}
