package org.jzb.weixin.work.oa;

import com.fasterxml.jackson.databind.JsonNode;
import org.jzb.J;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 描述：
 *
 * @author jzb 2018-04-29
 */
public class CheckinData {
    private final JsonNode node;

    public CheckinData(JsonNode node) {
        this.node = node;
    }

    public String userid() {
        return Optional.ofNullable(node)
                .map(it -> it.get("userid"))
                .map(JsonNode::asText)
                .orElseThrow(() -> new NullPointerException());
    }

    /**
     * @return 打卡规则名称
     */
    public String groupname() {
        return Optional.ofNullable(node)
                .map(it -> it.get("groupname"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public String checkin_type() {
        return Optional.ofNullable(node)
                .map(it -> it.get("checkin_type"))
                .map(JsonNode::asText)
                .orElseThrow(() -> new NullPointerException());
    }

    /**
     * @return 打卡时间 Unix时间戳
     */
    public Date checkin_time() {
        return Optional.ofNullable(node)
                .map(it -> it.get("checkin_time"))
                .map(JsonNode::asLong)
                .map(it -> it * 1000)
                .map(Date::new)
                .orElseThrow(() -> new NullPointerException());
    }

    public String location_title() {
        return Optional.ofNullable(node)
                .map(it -> it.get("location_title"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public String location_detail() {
        return Optional.ofNullable(node)
                .map(it -> it.get("location_detail"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public String wifiname() {
        return Optional.ofNullable(node)
                .map(it -> it.get("wifiname"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    /**
     * @return 打卡的MAC地址/bssid
     */
    public String wifimac() {
        return Optional.ofNullable(node)
                .map(it -> it.get("wifimac"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    /**
     * @return 打卡的附件media_id，可使用media/get获取附件
     */
    public Stream<String> mediaids() {
        return Optional.ofNullable(node)
                .map(it -> it.get("mediaids"))
                .map(it -> StreamSupport.stream(it.spliterator(), false)
                        .map(JsonNode::asText)
                )
                .orElse(Stream.empty());
    }

    public String notes() {
        return Optional.ofNullable(node)
                .map(it -> it.get("notes"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    /**
     * @return 异常类型，字符串，包括：时间异常，地点异常，未打卡，wifi异常，非常用设备。如果有多个异常，以分号间隔
     */
    public Stream<String> exception_type() {
        return Optional.ofNullable(node)
                .map(it -> it.get("exception_type"))
                .map(JsonNode::asText)
                .filter(J::nonBlank)
                .map(it -> it.split("\\;"))
                .map(Arrays::stream)
                .orElse(Stream.empty());
    }

    /**
     * @return 是否打卡
     */
    public boolean hasCheckin() {
        return !exception_type()
                .filter(it -> it.equals("未打卡"))
                .findFirst()
                .isPresent();
    }
}
