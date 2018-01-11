package org.jzb.weinxin.open.authorizeCode;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jzb.J;
import org.jzb.weinxin.open.OpenClient;

import java.io.InputStream;
import java.util.concurrent.Callable;

import static org.jzb.Constant.MAPPER;
import static org.jzb.social.core.Constant.OK_CLIENT;
import static org.jzb.weinxin.open.util.OpenConstant.AUTHORIZE_TOKEN_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class OpenAuthorizeCodeRequest implements Callable<OpenAuthorizeUnionInfoResponse> {
    private final OpenClient client;
    private final String code;
    private final String state;

    public OpenAuthorizeCodeRequest(OpenClient client, String code, String state) {
        this.client = client;
        this.code = code;
        this.state = state;
    }

    @Override
    public OpenAuthorizeUnionInfoResponse call() throws Exception {
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
                final OpenAuthorizeTokenResponse tokenRes = new OpenAuthorizeTokenResponse(client, node);
                return tokenRes.call();
            }
        }
    }
}
