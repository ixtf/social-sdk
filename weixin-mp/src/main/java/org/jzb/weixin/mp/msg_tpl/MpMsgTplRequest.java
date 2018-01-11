package org.jzb.weixin.mp.msg_tpl;

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
import static org.jzb.weixin.mp.util.MpConstant.TPL_MESSAGE_SEND_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class MpMsgTplRequest implements Callable<MpMsgTplResponse> {
    private final MpClient client;
    private final JsonNode node;

    protected MpMsgTplRequest(MpClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    @Override
    public MpMsgTplResponse call() throws Exception {
        final Request request = new Request.Builder()
                .url(J.strTpl(TPL_MESSAGE_SEND_URL_TPL, ImmutableMap.of("access_token", client.access_token())))
                .post(RequestBody.create(OK_JSON, MAPPER.writeValueAsString(node)))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                return new MpMsgTplResponse(client, node);
            }
        }
    }

    public static class Builder implements Callable<MpMsgTplResponse> {
        private final MpClient client;
        private final ObjectNode node = MAPPER.createObjectNode();
        private final ObjectNode dataNode = MAPPER.createObjectNode();

        public Builder(MpClient client) {
            this.client = client;
            this.node.set("data", dataNode);
        }

        public Builder touser(String touser) {
            node.put("touser", touser);
            return this;
        }

        public Builder template_id(String template_id) {
            node.put("template_id", template_id);
            return this;
        }

        public Builder url(String url) {
            node.put("url", url);
            return this;
        }

        public Builder data(String key, String value) {
            ObjectNode keyNode = MAPPER.createObjectNode()
                    .put("value", value);
            dataNode.set(key, keyNode);
            return this;
        }

        public Builder data(String key, String value, String color) {
            ObjectNode keyNode = MAPPER.createObjectNode()
                    .put("value", value)
                    .put("color", color);
            dataNode.set(key, keyNode);
            return this;
        }

        public MpMsgTplRequest build() {
            return new MpMsgTplRequest(client, node);
        }

        @Override
        public MpMsgTplResponse call() throws Exception {
            return this.build().call();
        }
    }
}
