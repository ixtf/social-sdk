package org.jzb.weixin.pay.sl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author jzb 2017-10-29
 */
@JacksonXmlRootElement(localName = "xml")
public class PaySlNotifyResult implements Serializable {
    @JacksonXmlCData
    private String return_code;
    @JacksonXmlCData
    private String return_msg;

    public boolean isSuccessed() {
        return "SUCCESS".equals(return_code);
    }

    public void successed(boolean b) {
        setReturn_code(b ? "SUCCESS" : "FAIL");
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }
}
