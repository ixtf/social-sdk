package org.jzb.weixin.mp.msg_kf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jzb.J;
import org.jzb.weixin.mp.MpClient;

import java.io.InputStream;
import java.util.concurrent.Callable;

import static org.jzb.Constant.MAPPER;
import static org.jzb.social.core.Constant.OK_CLIENT;
import static org.jzb.social.core.Constant.OK_JSON;
import static org.jzb.weixin.mp.util.MpConstant.KF_MESSAGE_SEND_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public abstract class MpMsgKfRequest implements Callable<MpMsgKfResponse> {
    protected final MpClient client;
    protected final JsonNode node;

    protected MpMsgKfRequest(MpClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    @Override
    public MpMsgKfResponse call() throws Exception {
        final Request request = new Request.Builder()
                .url(J.strTpl(KF_MESSAGE_SEND_URL_TPL, ImmutableMap.of("access_token", client.access_token())))
                .post(RequestBody.create(OK_JSON, MAPPER.writeValueAsString(node)))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                return new MpMsgKfResponse(client, node);
            }
        }
    }

    public static class Builder {
        protected final MpClient client;
        protected final ObjectNode node;

        protected Builder(MpClient client, ObjectNode node) {
            this.client = client;
            this.node = node;
        }

        public Builder(MpClient client, String touser) {
            this(client, MAPPER.createObjectNode().put("touser", touser));
        }

        public MpMsgKfRequestText.Builder text() {
            return new MpMsgKfRequestText.Builder(client, node);
        }

        public MpMsgKfRequestNews.Builder news() {
            return new MpMsgKfRequestNews.Builder(client, node);
        }
    }
}
