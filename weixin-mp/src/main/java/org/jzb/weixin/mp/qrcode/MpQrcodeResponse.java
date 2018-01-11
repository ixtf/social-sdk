package org.jzb.weixin.mp.qrcode;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import org.jzb.J;
import org.jzb.weixin.mp.MpClient;

import static org.jzb.weixin.mp.util.MpConstant.SHOW_QRCODE_URL_TPL;

public class MpQrcodeResponse {
    private final MpClient client;
    private final JsonNode node;

    public MpQrcodeResponse(MpClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    public String showqrcodeUrl() {
        final String ticket = J.urlEncode(ticket());
        return J.strTpl(SHOW_QRCODE_URL_TPL, ImmutableMap.of("ticket", ticket));
    }

    public String ticket() {
        return node.get("ticket").asText();
    }

    public int expire_seconds() {
        return node.get("expire_seconds").asInt();
    }

    public String url() {
        return node.get("url").asText();
    }
}
