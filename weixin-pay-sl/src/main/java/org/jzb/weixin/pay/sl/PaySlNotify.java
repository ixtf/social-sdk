package org.jzb.weixin.pay.sl;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static org.jzb.social.core.Constant.XML_MAPPER;
import static org.jzb.weixin.pay.sl.util.PaySlConstant.SUCCESS;

/**
 * 描述：
 *
 * @author jzb 2017-10-29
 */
public class PaySlNotify implements Serializable {
    private final String xmlStr;
    private final JsonNode node;

    public PaySlNotify(String xmlStr) throws IOException {
        this.xmlStr = xmlStr;
        this.node = XML_MAPPER.readTree(xmlStr);
    }

    public boolean isSuccessed() {
        return Objects.equals(return_code(), SUCCESS) && Objects.equals(result_code(), SUCCESS);
    }

    public String errMsg() {
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(return_msg())
                .ifPresent(sb::append);
        Optional.ofNullable(err_code_des())
                .ifPresent(sb::append);
        return sb.toString();
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

    //微信支付订单号
    public String transaction_id() {
        return node.get("transaction_id").asText();
    }

    //商户订单号
    public String out_trade_no() {
        return node.get("out_trade_no").asText();
    }

    public Date time_end() throws ParseException {
        final String time_end = node.get("time_end").asText();
        return DateUtils.parseDate(time_end, "yyyyMMddHHmmss");
    }

    public String openid() {
        return node.get("openid").asText();
    }

    public String return_code() {
        return node.get("return_code").asText();
    }

    public String return_msg() {
        return node.get("return_msg").asText();
    }

    public String result_code() {
        return node.get("result_code").asText();
    }

    public String err_code() {
        return node.get("err_code").asText();
    }

    public String err_code_des() {
        return node.get("err_code").asText();
    }

    //付款银行
    public String bank_type() {
        return node.get("bank_type").asText();
    }

    public String fee_type() {
        return node.get("fee_type").asText();
    }

    //订单总金额，单位为元
    public double total_fee() {
        return node.get("total_fee").asDouble() / 100;
    }

    public String cash_fee_type() {
        return node.get("cash_fee_type").asText();
    }

    //现金支付金额订单现金支付金额，单位为元
    public double cash_fee() {
        return node.get("cash_fee").asDouble() / 100;
    }

    //应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
    public double settlement_total_fee() {
        return node.get("settlement_total_fee").asDouble() / 100;
    }

    //代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额
    public double coupon_fee() {
        return node.get("coupon_fee").asDouble() / 100;
    }

    //代金券或立减优惠使用数量
    public int coupon_count() {
        return node.get("coupon_fee").asInt();
    }

    //用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
    public String is_subscribe() {
        return node.get("is_subscribe").asText();
    }

    public String sub_openid() {
        return node.get("sub_openid").asText();
    }

    public String sub_is_subscribe() {
        return node.get("sub_is_subscribe").asText();
    }

    public String trade_type() {
        return node.get("trade_type").asText();
    }

    public String device_info() {
        return node.get("device_info").asText();
    }
}
