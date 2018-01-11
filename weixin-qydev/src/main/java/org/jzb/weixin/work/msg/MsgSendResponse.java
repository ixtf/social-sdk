package org.jzb.weixin.work.msg;

import com.fasterxml.jackson.databind.JsonNode;
import org.jzb.weixin.work.AgentClient;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class MsgSendResponse {
    protected final AgentClient client;
    protected final JsonNode node;

    public MsgSendResponse(AgentClient client, JsonNode node) {
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
