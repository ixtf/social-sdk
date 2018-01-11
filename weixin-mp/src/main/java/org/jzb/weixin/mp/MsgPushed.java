package org.jzb.weixin.mp;

import com.google.common.base.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */

public class MsgPushed implements Serializable {
    private final String _xmlText;
    private final String ToUserName;
    private final String FromUserName;
    private final long CreateTime;
    private final String MsgType;
    private final String Event;
    private final String Content;
    private final String EventKey;
    private final String Ticket;
    private final String Status;
    private final double Latitude;
    private final double Longitude;
    private final double Precision;
    private final double Location_X;
    private final double Location_Y;
    private final int Scale;
    private final String Label;
    private final long MsgId;
    private final int AgentID;
    private final String ScanType;
    private final String ScanResult;

    public MsgPushed(String xmlText) throws ParserConfigurationException, IOException, SAXException {
        this._xmlText = xmlText;
        try (StringReader sr = new StringReader(xmlText)) {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = db.parse(new InputSource(sr));
            Element root = document.getDocumentElement();

            ToUserName = stringV(root, "ToUserName");
            FromUserName = stringV(root, "FromUserName");
            MsgType = stringV(root, "MsgType");
            Event = stringV(root, "Event");
            Content = stringV(root, "Content");
            EventKey = stringV(root, "EventKey");
            Ticket = stringV(root, "Ticket");
            Label = stringV(root, "Label");
            Status = stringV(root, "Status");
            CreateTime = longV(root, "CreateTime");
            Latitude = doubleV(root, "Latitude");
            Longitude = doubleV(root, "Longitude");
            Precision = doubleV(root, "Precision");
            Location_X = doubleV(root, "Location_X");
            Location_Y = doubleV(root, "Location_Y");
            Scale = intV(root, "Scale");
            MsgId = longV(root, "MsgId");
            AgentID = intV(root, "AgentID");
            ScanType = stringV(root, "ScanType");
            ScanResult = stringV(root, "ScanResult");
        }
    }

    private String stringV(Element root, String tagName) {
        Node node = root.getElementsByTagName(tagName).item(0);
        return node == null ? null : node.getTextContent();
    }

    private int intV(Element root, String tagName) {
        Node node = root.getElementsByTagName(tagName).item(0);
        return node == null ? 0 : Integer.valueOf(node.getTextContent());
    }

    private long longV(Element root, String tagName) {
        Node node = root.getElementsByTagName(tagName).item(0);
        return node == null ? 0 : Long.valueOf(node.getTextContent());
    }

    private double doubleV(Element root, String tagName) {
        Node node = root.getElementsByTagName(tagName).item(0);
        return node == null ? 0 : Double.valueOf(node.getTextContent());
    }

    private boolean isEvt(String s) {
        return Objects.equal("event", MsgType) && Objects.equal(s, Event);
    }

    public boolean isEvt_subscribe() {
        return isEvt("subscribe");
    }

    public boolean isEvt_unsubscribe() {
        return isEvt("unsubscribe");
    }

    public boolean isEvt_SCAN() {
        return isEvt("SCAN");
    }

    /*
     * <xml><ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[FromUser]]></FromUserName>
     * <CreateTime>1408090606</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[scancode_waitmsg]]></Event>
     * <EventKey><![CDATA[6]]></EventKey>
     * <ScanCodeInfo>
     * <ScanType><![CDATA[qrcode]]></ScanType>
     * <ScanResult><![CDATA[2]]></ScanResult>
     * </ScanCodeInfo>
     * <AgentID>1</AgentID>
     * </xml>
     */
    public boolean isEvt_scancode_waitmsg() {
        return isEvt("scancode_waitmsg");
    }

    /*
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[FromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[CLICK]]></Event>
     * <EventKey><![CDATA[EVENTKEY]]></EventKey>
     * </xml>
     */
    public boolean isEvt_CLICK() {
        return isEvt("CLICK");
    }

    /*
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>123456789</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[LOCATION]]></Event>
     * <Latitude>23.137466</Latitude>
     * <Longitude>113.352425</Longitude>
     * <Precision>119.385040</Precision>
     * </xml>
     */
    public boolean isEvt_LOCATION() {
        return isEvt("LOCATION");
    }

    public boolean isEvt_TEMPLATESENDJOBFINISH() {
        return isEvt("TEMPLATESENDJOBFINISH");
    }

    /*
     * <xml>
     * <ToUserName><![CDATA[toUser]]></ToUserName>
     * <FromUserName><![CDATA[fromUser]]></FromUserName>
     * <CreateTime>1348831860</CreateTime>
     * <MsgType><![CDATA[text]]></MsgType>
     * <Content><![CDATA[this is a test]]></Content>
     * <MsgId>1234567890123456</MsgId>
     * </xml>
     */
    public boolean isRcv_text() {
        return Objects.equal("text", MsgType);
    }

    public boolean isRcv_location() {
        return Objects.equal("location", MsgType);
    }

    public String getToUserName() {
        return ToUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public String getEvent() {
        return Event;
    }

    public String getContent() {
        return Content;
    }

    public String getEventKey() {
        return EventKey;
    }

    public String getTicket() {
        return Ticket;
    }

    public String getStatus() {
        return Status;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public double getPrecision() {
        return Precision;
    }

    public double getLocation_X() {
        return Location_X;
    }

    public double getLocation_Y() {
        return Location_Y;
    }

    public int getScale() {
        return Scale;
    }

    public String getLabel() {
        return Label;
    }

    public long getMsgId() {
        return MsgId;
    }

    public String getScanType() {
        return ScanType;
    }

    public String getScanResult() {
        return ScanResult;
    }

    @Override
    public String toString() {
        return _xmlText;
    }
}
