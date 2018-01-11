package org.jzb.weixin.work;

import org.jzb.social.core.AbstractExpireToken;
import org.jzb.weixin.work.msg.MsgSendRequest;
import org.jzb.weixin.work.util.AesException;
import org.jzb.weixin.work.util.WXBizMsgCrypt;

/**
 * 描述： 企业微信
 *
 * @author jzb 2017-12-13
 */
public class AgentClient {
    private final WorkClient workClient;
    private final int agentid;
    private final String secret;
    private final WXBizMsgCrypt wxbmc;
    private final AbstractExpireToken<String> accessToken;

    private AgentClient(WorkClient workClient, int agentid, String secret, WXBizMsgCrypt wxbmc) {
        this.workClient = workClient;
        this.agentid = agentid;
        this.secret = secret;
        this.wxbmc = wxbmc;
        accessToken = new AgentAccessToken(workClient.corpid, secret);
    }

    AgentClient(WorkClient workClient, int agentid, String secret) {
        this(workClient, agentid, secret, null);
    }

    AgentClient(WorkClient workClient, int agentid, String secret, String token, String encodingaeskey) throws AesException {
        this(workClient, agentid, secret, new WXBizMsgCrypt(token, encodingaeskey, workClient.corpid));
    }

    public String access_token() throws Exception {
        return accessToken.get();
    }

    public MsgSendRequest.Builder msgSend() {
        return new MsgSendRequest.Builder(this);
    }

    public int getAgentid() {
        return agentid;
    }
}
