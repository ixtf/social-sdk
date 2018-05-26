package org.jzb.weixin.work.oa;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.time.DateUtils;
import org.jzb.J;
import org.jzb.weixin.work.AgentClient;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.jzb.Constant.MAPPER;
import static org.jzb.social.core.Constant.OK_CLIENT;
import static org.jzb.social.core.Constant.OK_JSON;
import static org.jzb.weixin.work.util.WorkConstant.GET_CHECKIN_DATA_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class GetCheckinDataRequest implements Callable<GetCheckinDataResponse> {
    protected final AgentClient client;
    protected final JsonNode node;

    protected GetCheckinDataRequest(AgentClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    @Override
    public GetCheckinDataResponse call() throws Exception {
        final Request request = new Request.Builder()
                .url(J.strTpl(GET_CHECKIN_DATA_URL_TPL, ImmutableMap.of("access_token", client.access_token())))
                .post(RequestBody.create(OK_JSON, MAPPER.writeValueAsString(node)))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                return new GetCheckinDataResponse(client, node);
            }
        }
    }

    public static class Builder implements Callable<GetCheckinDataResponse> {
        private final AgentClient client;
        private final ObjectNode node = MAPPER.createObjectNode();
        private final Set<String> useridlist = Sets.newHashSet();
        private int opencheckindatatype = 3;
        private Date endtime = new Date();
        private Date starttime = DateUtils.addDays(endtime, -1);

        public Builder(AgentClient client) {
            this.client = client;
            node.put("opencheckindatatype", 3);
        }

        /**
         * 打卡类型
         * 1：上下班打卡；2：外出打卡；3：全部打卡
         *
         * @param opencheckindatatype
         * @return
         */
        public Builder opencheckindatatype(int opencheckindatatype) {
            this.opencheckindatatype = opencheckindatatype;
            if (opencheckindatatype > 3 || opencheckindatatype < 1) {
                this.opencheckindatatype = 3;
            }
            return this;
        }

        public Builder starttime(Date starttime) {
            this.starttime = starttime;
            return this;
        }

        public Builder starttime(LocalDate ld) {
            return starttime(J.date(ld));
        }

        public Builder endtime(Date endtime) {
            this.endtime = endtime;
            return this;
        }

        public Builder endtime(LocalDate ld) {
            return endtime(J.date(ld));
        }

        /**
         * 用户列表不超过100个。若用户超过100个，请分批获取
         *
         * @param users
         * @return
         */
        public Builder addUser(Stream<String> users) {
            users.forEach(useridlist::add);
            return this;
        }

        public Builder addUser(String... users) {
            return this.addUser(Arrays.stream(users));
        }

        public Builder addUser(Iterable<String> users) {
            return this.addUser(StreamSupport.stream(users.spliterator(), false));
        }

        public GetCheckinDataRequest build() {
            if (useridlist.size() > 100) {
                throw new RuntimeException("用户列表超过100个，请分批获取");
            }
            final long until = J.localDateTime(starttime).until(LocalDateTime.now(), ChronoUnit.MONTHS);
            if (until > 3) {
                throw new RuntimeException("获取记录时间跨度不超过三个月");
            }
            node.put("opencheckindatatype", opencheckindatatype);
            node.put("starttime", starttime.getTime() / 1000);
            node.put("endtime", endtime.getTime() / 1000);
            node.set("useridlist", MAPPER.convertValue(useridlist, ArrayNode.class));
            return new GetCheckinDataRequest(client, node);
        }

        @Override
        public GetCheckinDataResponse call() throws Exception {
            return this.build().call();
        }
    }
}
