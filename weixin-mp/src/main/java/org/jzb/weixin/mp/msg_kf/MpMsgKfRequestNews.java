package org.jzb.weixin.mp.msg_kf;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jzb.weixin.mp.MpClient;

import java.util.concurrent.Callable;

import static org.jzb.Constant.MAPPER;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class MpMsgKfRequestNews extends MpMsgKfRequest {

    protected MpMsgKfRequestNews(MpClient client, ObjectNode node, ObjectNode newsNode) {
        super(client, node);
        node.put("msgtype", "news");
        node.set("news", newsNode);
    }

    public static class Builder extends MpMsgKfRequest.Builder implements Callable<MpMsgKfResponse> {
        private final ObjectNode newsNode = MAPPER.createObjectNode();
        private final ArrayNode articlesNode = MAPPER.createArrayNode();

        protected Builder(MpClient client, ObjectNode node) {
            super(client, node);
            newsNode.set("articles", articlesNode);
        }

        public ArticleBuilder newArticle() {
            return new ArticleBuilder(this);
        }

        public MpMsgKfRequestNews build() {
            return new MpMsgKfRequestNews(client, node, newsNode);
        }

        @Override
        public MpMsgKfResponse call() throws Exception {
            return this.build().call();
        }
    }

    public static class ArticleBuilder {
        private final Builder parent;
        private final ObjectNode node = MAPPER.createObjectNode();

        private ArticleBuilder(Builder parent) {
            this.parent = parent;
        }

        public ArticleBuilder title(String title) {
            node.put("title", title);
            return this;
        }

        public ArticleBuilder description(String description) {
            node.put("description", description);
            return this;
        }

        public ArticleBuilder url(String url) {
            node.put("url", url);
            return this;
        }

        public ArticleBuilder picurl(String picurl) {
            node.put("picurl", picurl);
            return this;
        }

        public Builder addArticle() {
            parent.articlesNode.add(node);
            return parent;
        }
    }
}
