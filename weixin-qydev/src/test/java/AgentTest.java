import org.jzb.weixin.work.AgentClient;
import org.jzb.weixin.work.WorkClient;
import org.jzb.weixin.work.msg.MsgSendResponse;

import java.util.Properties;

/**
 * 描述：
 *
 * @author jzb 2017-12-13
 */
public class AgentTest {
    final static int agentid = 0;
    final static String corpid = "wx8d1fcf0d627bf7bb";
    final static String secret = "DsAVvapcWi86kthBJYeap7EdLEVMc-UK5RJgNDEXgww";
    final static WorkClient workClient;
    final static AgentClient agentClient;

    static {
        Properties p = new Properties();
        p.setProperty("corpid", corpid);
        workClient = WorkClient.getInstance(p);

        p.setProperty("agentid", "" + agentid);
        p.setProperty("secret", secret);
        agentClient = workClient.agentClient(p);
    }

    public static void main(String[] args) throws Exception {
        final MsgSendResponse res = agentClient.msgSend()
                .text()
                .addUser("12000077")
                .content("test")
                .call();
        System.out.println(res);
    }
}
