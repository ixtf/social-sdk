package org.jzb.weixin.mp.msg_tpl;

import com.fasterxml.jackson.databind.JsonNode;
import org.jzb.weixin.mp.MpClient;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class MpMsgTplResponse {
    protected final MpClient client;
    protected final JsonNode node;

    public MpMsgTplResponse(MpClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    public boolean isSuccessed() {
        return errcode() == 0;
    }

    public int errcode() {
        return node.get("errcode").asInt();
    }

    public String errmsg() {
        return node.get("errcode").asText();
    }

    public long msgid() {
        return node.get("errcode").asLong();
    }
}
