package org.jzb.weixin.pay.sl.unifiedorder;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jzb.J;
import org.jzb.weixin.pay.sl.PaySlClient;
import org.jzb.weixin.pay.sl.util.PaySlConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import static org.jzb.social.core.Constant.*;
import static org.jzb.social.core.JSocial.generateNonceStr;
import static org.jzb.weixin.pay.sl.util.PaySlConstant.*;
import static org.jzb.weixin.pay.sl.util.PaySlUtil.mapToXml;

/*
 * 描述：
 * 统一下单
 *
 * @author jzb
 * @create 2017-10-24
 */
public class PaySlUnifiedorderRequest implements Callable<PaySlUnifiedorderResponse> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final PaySlClient client;
    private final Map<String, String> map;
    private final PaySlConstant.SignType signType;

    private PaySlUnifiedorderRequest(PaySlClient client, Map<String, String> map, SignType signType) {
        this.client = client;
        this.map = map;
        this.signType = signType;
    }

    @Override
    public PaySlUnifiedorderResponse call() throws Exception {
        final String sign = client.sign(map, signType);
        final Map<String, String> map = Maps.newHashMap(this.map);
        map.put(FIELD_SIGN, sign);
        final Request request = new Request.Builder()
                .url("https://" + DOMAIN_API + UNIFIEDORDER_URL_SUFFIX)
                .post(RequestBody.create(OK_XML, mapToXml(map)))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body()) {
                final String xmlStr = body.string();
                final JsonNode node = XML_MAPPER.readTree(xmlStr);
                final String return_code = node.get("return_code").asText();
                if (!SUCCESS.equals(return_code)) {
                    log.error("", xmlStr);
                    final String errMsg = node.get("return_msg").asText();
                    throw new RuntimeException(errMsg);
                }
                final String result_code = node.get("result_code").asText();
                if (!SUCCESS.equals(result_code)) {
                    log.error("", xmlStr);
                    final String errMsg = node.get("err_code_des").asText();
                    throw new RuntimeException(errMsg);
                }
                if (!client.isSignValid(xmlStr, signType)) {
                    log.error("", xmlStr);
                    final String errMsg = "无效的签名!";
                    throw new RuntimeException(errMsg);
                }
                return new PaySlUnifiedorderResponse(client, node);
            }
        }
    }

    public static class Builder implements Callable<PaySlUnifiedorderResponse> {
        private final PaySlClient client;
        private final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        private PaySlConstant.SignType signType = SignType.MD5;

        public Builder(PaySlClient client) {
            this.client = client;
            builder.put("appid", client.getAppid());
            builder.put("mch_id", client.getMch_id());
            builder.put(FIELD_SIGN_TYPE, MD5);
            builder.put("trade_type", "JSAPI");
            builder.put("device_info", "WEB");
            builder.put("limit_pay", "no_credit");
            builder.put("nonce_str", generateNonceStr());
        }

        public Builder signType(PaySlConstant.SignType signType) {
            switch (signType) {
                case HMACSHA256: {
                    this.signType = SignType.HMACSHA256;
                    builder.put(FIELD_SIGN_TYPE, HMACSHA256);
                    break;
                }
                default: {
                    this.signType = SignType.MD5;
                    builder.put(FIELD_SIGN_TYPE, MD5);
                    break;
                }
            }
            return this;
        }

        // 子商户号
        public Builder sub_mch_id(String sub_mch_id) {
            builder.put("sub_mch_id", sub_mch_id);
            return this;
        }

        /**
         * trade_type=JSAPI，此参数必传，用户在主商户appid下的唯一标识。
         * openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。
         * 下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
         *
         * @param openid 用户标识
         * @return
         */
        public Builder openid(String openid) {
            builder.put("openid", openid);
            return this;
        }

        /**
         * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
         *
         * @param out_trade_no 商户订单号
         * @return
         */
        public Builder out_trade_no(String out_trade_no) {
            builder.put("out_trade_no", out_trade_no);
            return this;
        }

        /**
         * 总金额
         *
         * @param total_fee 单位：分
         * @return
         */
        public Builder total_fee(int total_fee) {
            builder.put("total_fee", "" + total_fee);
            return this;
        }

        /**
         * 总金额
         *
         * @param total_fee 单位：元
         * @return
         */
        public Builder total_fee(BigDecimal total_fee) {
            total_fee = total_fee.multiply(BigDecimal.valueOf(100));
            return this.total_fee(total_fee.intValue());
        }

        // 终端IP
        public Builder spbill_create_ip(String spbill_create_ip) {
            builder.put("spbill_create_ip", spbill_create_ip);
            return this;
        }

        public Builder notify_url(String notify_url) {
            builder.put("notify_url", notify_url);
            return this;
        }

        public Builder body(String body) {
            builder.put("body", body);
            return this;
        }

        public Builder sub_appid(String sub_appid) {
            sub_appid = Optional.ofNullable(sub_appid)
                    .filter(J::nonBlank)
                    .orElse("");
            builder.put("sub_appid", sub_appid);
            return this;
        }

        public Builder detail(String detail) {
            detail = Optional.ofNullable(detail)
                    .filter(J::nonBlank)
                    .orElse("");
            builder.put("detail", detail);
            return this;
        }

        public Builder attach(String attach) {
            attach = Optional.ofNullable(attach)
                    .filter(J::nonBlank)
                    .orElse("");
            builder.put("attach", attach);
            return this;
        }

        public Builder fee_type(String fee_type) {
            fee_type = Optional.ofNullable(fee_type)
                    .filter(J::nonBlank)
                    .orElse("CNY");
            builder.put("fee_type", fee_type);
            return this;
        }

        public PaySlUnifiedorderRequest build() {
            return new PaySlUnifiedorderRequest(client, builder.build(), signType);
        }

        @Override
        public PaySlUnifiedorderResponse call() throws Exception {
            return this.build().call();
        }
    }
}
