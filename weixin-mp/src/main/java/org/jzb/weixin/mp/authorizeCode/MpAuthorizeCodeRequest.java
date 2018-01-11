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
import static org.jzb.weixin.mp.util.MpConstant.AUTHORIZE_TOKEN_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class MpAuthorizeCodeRequest implements Callable<MpAuthorizeUnionInfoResponse> {
    private final MpClient client;
    private final String code;
    private final String state;

    public MpAuthorizeCodeRequest(MpClient client, String code, String state) {
        this.client = client;
        this.code = code;
        this.state = state;
    }

    @Override
    public MpAuthorizeUnionInfoResponse call() throws Exception {
        final Request request = new Request.Builder()
                .url(J.strTpl(AUTHORIZE_TOKEN_URL_TPL, ImmutableMap.of("appid", client.getAppid(), "secret", client.getSecret(), "code", code, "state", state)))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                final MpAuthorizeTokenResponse tokenRes = new MpAuthorizeTokenResponse(client, node);
                return tokenRes.call();
            }
        }
    }
}
