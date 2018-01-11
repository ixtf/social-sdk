package org.jzb.weixin.work;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * 描述： 企业微信
 *
 * @author jzb 2017-12-13
 */
public class WorkClient {
    private static final Map<String, WorkClient> WORK_CLIENT_MAPMAP = Maps.newConcurrentMap();
    final String corpid;
    private final Map<Integer, AgentClient> AGENT_CLIENT_MAP = Maps.newConcurrentMap();

    private WorkClient(String corpid) {
        Validate.notBlank(corpid);
        this.corpid = corpid;
    }

    public static WorkClient getInstance(Properties p) {
        final String corpid = p.getProperty("corpid");
        return WORK_CLIENT_MAPMAP.compute(corpid, (k, v) ->
                Optional.ofNullable(v)
                        .orElse(new WorkClient(corpid))
        );
    }

    public AgentClient agentClient(Properties p) {
        final int agentid = Integer.parseInt(p.getProperty("agentid"));
        final String secret = p.getProperty("secret");
        return AGENT_CLIENT_MAP.compute(agentid, (k, v) ->
                Optional.ofNullable(v)
                        .orElse(new AgentClient(this, agentid, secret))
        );
    }
}
