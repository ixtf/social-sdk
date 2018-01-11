package org.jzb.weinxin.open.authorizeCode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jzb.weinxin.open.OpenClient;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class OpenAuthorizeUnionInfoResponse {
    private final OpenClient client;
    private final JsonNode node;

    public OpenAuthorizeUnionInfoResponse(OpenClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    public String unionid() {
        return node.get("unionid").asText();
    }

    public String openid() {
        return node.get("openid").asText();
    }

    public String nickname() {
        return node.get("nicknamel").asText();
    }

    public int sex() {
        return node.get("sex").asInt();
    }

    public String province() {
        return node.get("province").asText();
    }

    public String city() {
        return node.get("city").asText();
    }

    public String country() {
        return node.get("country").asText();
    }

    public String headimgurl() {
        return node.get("headimgurl").asText();
    }

    public String[] privilege() {
        final ArrayNode nodes = (ArrayNode) node.get("privilege");
        String[] privilege = new String[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            privilege[i] = nodes.get(i).asText();
        }
        return privilege;
    }
}
