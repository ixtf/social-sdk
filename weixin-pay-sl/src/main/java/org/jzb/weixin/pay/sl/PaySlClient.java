package org.jzb.weixin.pay.sl;

import org.jzb.weixin.pay.sl.unifiedorder.PaySlUnifiedorderRequest;
import org.jzb.weixin.pay.sl.util.PaySlConstant;
import org.jzb.weixin.pay.sl.util.PaySlUtil;

import java.util.Map;
import java.util.Properties;

import static org.jzb.weixin.pay.sl.util.PaySlConstant.FIELD_SIGN;
import static org.jzb.weixin.pay.sl.util.PaySlUtil.generateSignature;
import static org.jzb.weixin.pay.sl.util.PaySlUtil.xmlToMap;

/*
 * 描述：
 * 微信支付 境内服务商
 *
 * @author jzb
 * @create 2017-10-24
 */
public final class PaySlClient {
    // 微信分配的公众账号ID
    private final String appid;
    // 微信支付分配的商户号
    private final String mch_id;
    // 秘钥
    private final String key;

    private PaySlClient(String appid, String mch_id, String key) {
        this.appid = appid;
        this.mch_id = mch_id;
        this.key = key;
    }

    public static PaySlClient getInstance(Properties p) {
        final String appid = p.getProperty("appid");
        final String mch_id = p.getProperty("mch_id");
        final String key = p.getProperty("key");
        return new PaySlClient(appid, mch_id, key);
    }

    public PaySlUnifiedorderRequest.Builder unifiedorder() {
        return new PaySlUnifiedorderRequest.Builder(this);
    }

    public String sign(Map<String, String> data, PaySlConstant.SignType signType) throws Exception {
        return generateSignature(data, key, signType);
    }

    /**
     * 判断签名是否正确
     *
     * @param xmlStr XML格式数据
     * @return 签名是否正确
     * @throws Exception
     */
    public boolean isSignValid(String xmlStr, PaySlConstant.SignType signType) throws Exception {
        Map<String, String> data = xmlToMap(xmlStr);
        if (!data.containsKey(FIELD_SIGN)) {
            return false;
        }
        String sign = data.get(FIELD_SIGN);
        return generateSignature(data, key, signType).equals(sign);
    }

    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data Map类型数据
     * @return 含有sign字段的XML
     */
    public final String mapToXml(final Map<String, String> data, PaySlConstant.SignType signType) throws Exception {
        String sign = generateSignature(data, key, signType);
        data.put(FIELD_SIGN, sign);
        return PaySlUtil.mapToXml(data);
    }

    public String getAppid() {
        return appid;
    }

    public String getMch_id() {
        return mch_id;
    }
}
