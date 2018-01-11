package org.jzb.weixin.work.msg;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jzb.J;
import org.jzb.weixin.work.AgentClient;

import java.io.InputStream;
import java.util.concurrent.Callable;

import static org.jzb.Constant.MAPPER;
import static org.jzb.social.core.Constant.OK_CLIENT;
import static org.jzb.social.core.Constant.OK_JSON;
import static org.jzb.weixin.work.util.WorkConstant.MESSAGE_SEND_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public abstract class MsgSendRequest implements Callable<MsgSendResponse> {
    protected final AgentClient client;
    protected final JsonNode node;

    protected MsgSendRequest(AgentClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    @Override
    public MsgSendResponse call() throws Exception {
        final Request request = new Request.Builder()
                .url(J.strTpl(MESSAGE_SEND_URL_TPL, ImmutableMap.of("access_token", client.access_token())))
                .post(RequestBody.create(OK_JSON, MAPPER.writeValueAsString(node)))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                return new MsgSendResponse(client, node);
            }
        }
    }

    public static class Builder {
        protected final AgentClient client;

        public Builder(AgentClient client) {
            this.client = client;
        }

        public MsgSendRequestText.Builder text() {
            return new MsgSendRequestText.Builder(client);
        }
    }
}
