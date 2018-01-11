package sl;

import org.jzb.J;
import org.jzb.weixin.pay.sl.PaySlClient;
import org.jzb.weixin.pay.sl.PaySlNotifyResult;
import org.jzb.weixin.pay.sl.unifiedorder.PaySlUnifiedorderRequest;
import org.jzb.weixin.pay.sl.unifiedorder.PaySlUnifiedorderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.jzb.social.core.Constant.XML_MAPPER;

public class PaySlTest {
    private static Logger log = LoggerFactory.getLogger(PaySlTest.class);
    private static Properties p = new Properties();
    private static PaySlClient client = PaySlClient.getInstance(p);

    static {
        try {
            p.load(new FileInputStream("/home/jzb/data/japp-execution-wjh/pay_sl_weixin.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        PaySlNotifyResult test = new PaySlNotifyResult();
        test.setReturn_code("SUCCESS");
        System.out.println(XML_MAPPER.writeValueAsString(test));


        PaySlUnifiedorderRequest request = client.unifiedorder()
                .sub_mch_id("1490476912")
                // jzb
                .openid("oohBQwjH5QPqyNVFzf54MTMSBV0Y")
                .spbill_create_ip("202.101.160.126")
                // zyt
                // .openid("oohBQwnqvCd_cZHeTW5uXQxzw6Ek")
                // .spbill_create_ip("101.226.69.112")
                .total_fee(1)
                .notify_url("http://test")
                .body("test-body")
                .detail("test-detail")
                .attach("test-attach")
                .out_trade_no(J.uuid58())
                .build();
        final PaySlUnifiedorderResponse response = request.call();
        System.out.println(response);
    }
}
