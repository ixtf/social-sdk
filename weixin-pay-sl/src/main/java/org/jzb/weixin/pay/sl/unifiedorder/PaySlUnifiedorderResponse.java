package org.jzb.weixin.pay.sl.unifiedorder;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.jzb.J;
import org.jzb.weixin.pay.sl.PaySlClient;
import org.jzb.weixin.pay.sl.util.PaySlConstant;

import java.util.Map;
import java.util.Optional;

/*
 * 描述：
 *
 * @author jzb
 * @create 2017-10-24
 */
public class PaySlUnifiedorderResponse {
    private final PaySlClient client;
    private final JsonNode node;

    PaySlUnifiedorderResponse(PaySlClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    public Map<String, String> jsapiPayData() throws Exception {
        final Map<String, String> signMap = Maps.newHashMap();
        signMap.put("appId", client.getAppid());
        signMap.put("timeStamp", (System.currentTimeMillis() / 1000) + "");
        signMap.put("nonceStr", J.uuid58());
        signMap.put("package", "prepay_id=" + prepay_id());
        signMap.put("signType", "MD5");
        String paySign = client.sign(signMap, PaySlConstant.SignType.MD5);
        signMap.put("paySign", paySign);
        return ImmutableMap.copyOf(signMap);
    }

    /**
     * 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
     *
     * @return 预支付交易会话标识
     */
    public String prepay_id() {
        return node.get("prepay_id").asText();
    }

    /**
     * trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
     *
     * @return 二维码链接
     */
    public String code_url() {
        return Optional.ofNullable(node.get("code_url"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public String appid() {
        return node.get("appid").asText();
    }

    public String mch_id() {
        return node.get("mch_id").asText();
    }

    public String sub_appid() {
        return node.get("sub_appid").asText();
    }

    public String sub_mch_id() {
        return node.get("sub_mch_id").asText();
    }

    public String trade_type() {
        return node.get("trade_type").asText();
    }

    public String device_info() {
        return node.get("device_info").asText();
    }

    public String nonce_str() {
        return node.get("nonce_str").asText();
    }

    public String sign() {
        return node.get("sign").asText();
    }

    public String result_code() {
        return node.get("result_code").asText();
    }

    public String err_code() {
        return node.get("err_code").asText();
    }

    public String err_code_des() {
        return node.get("err_code_des").asText();
    }

    @Override
    public String toString() {
        return node.toString();
    }
}
