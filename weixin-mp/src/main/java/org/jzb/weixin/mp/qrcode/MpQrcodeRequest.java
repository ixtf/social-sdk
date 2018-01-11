package org.jzb.weixin.mp.qrcode;

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
import static org.jzb.weixin.mp.util.MpConstant.QRCODE_URL_TPL;

/**
 * 描述：
 * {
 * expire_seconds:  604800
 * action_name: QR_SCENE
 * action_info: {
 * scene:{
 * scene_id: 123
 * }
 * }
 * }
 *
 * @author jzb 2017-10-28
 */
public class MpQrcodeRequest implements Callable<MpQrcodeResponse> {
    private final MpClient client;
    private final JsonNode node;

    public MpQrcodeRequest(MpClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    @Override
    public MpQrcodeResponse call() throws Exception {
        final Request request = new Request.Builder()
                .url(J.strTpl(QRCODE_URL_TPL, ImmutableMap.of("access_token", client.access_token())))
                .post(RequestBody.create(OK_JSON, MAPPER.writeValueAsString(node)))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                return new MpQrcodeResponse(client, node);
            }
        }
    }

    public static class Builder implements Callable<MpQrcodeResponse> {
        private final MpClient client;
        private final ObjectNode node;
        private final ObjectNode scene;

        public Builder(MpClient client) {
            this.client = client;
            node = MAPPER.createObjectNode();
            ObjectNode action_info = MAPPER.createObjectNode();
            node.set("action_info", action_info);
            scene = MAPPER.createObjectNode();
            action_info.set("scene", scene);
        }

        /**
         * 该二维码有效时间，以秒为单位。
         * 最大不超过2592000（即30天），
         * 此字段如果不填，则默认有效期为30秒。
         */
        public Builder expire_seconds(int expire_seconds) {
            node.put("expire_seconds", expire_seconds);
            return this;
        }

        public Builder scene_id(int scene_id) {
            scene.put("scene_id", scene_id);
            return this;
        }

        public Builder scene_str(String scene_str) {
            scene.put("scene_str", scene_str);
            return this;
        }

        /**
         * 二维码类型，
         * QR_SCENE为临时的整型参数值，
         * QR_STR_SCENE为临时的字符串参数值，
         * QR_LIMIT_SCENE为永久的整型参数值，
         * QR_LIMIT_STR_SCENE为永久的字符串参数值
         */
        public Builder action_name(String action_name) {
            node.put("action_name", action_name);
            return this;
        }

        public MpQrcodeRequest build() {
            return new MpQrcodeRequest(client, node);
        }

        @Override
        public MpQrcodeResponse call() throws Exception {
            return this.build().call();
        }
    }
}
