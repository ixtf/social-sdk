package org.jzb.weixin.mp.unionInfo;

import com.fasterxml.jackson.databind.JsonNode;
import org.jzb.weixin.mp.MpClient;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class MpUnionInfoResponse {
    private final MpClient client;
    private final JsonNode node;

    public MpUnionInfoResponse(MpClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    public boolean isSubscribed() {
        return subscribe() == 1;
    }

    // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
    public int subscribe() {
        return node.get("subscribe").asInt();
    }

    public String openid() {
        return node.get("openid").asText();
    }

    // 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
    public String unionid() {
        return node.get("unionid").asText();
    }

    public String nickname() {
        return node.get("nickname").asText();
    }

    public String headimgurl() {
        return node.get("headimgurl").asText();
    }

    // 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    public long subscribe_time() {
        return node.get("subscribe_time").asLong();
    }

    // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    public int sex() {
        return node.get("sex").asInt();
    }

    public String city() {
        return node.get("city").asText();
    }

    public String country() {
        return node.get("country").asText();
    }

    public String province() {
        return node.get("province").asText();
    }

    public String language() {
        return node.get("language").asText();
    }

}
