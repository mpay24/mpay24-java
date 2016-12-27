package com.mpay24.payment.communication;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SoapAuthenticationInterceptor extends AbstractPhaseInterceptor<Message> {

    HttpServletRequest servletRequest;

    Logger log = Logger.getLogger(SoapAuthenticationInterceptor.class.getName());

    public SoapAuthenticationInterceptor(HttpServletRequest servletRequest) {
        super(Phase.PROTOCOL);
        this.servletRequest = servletRequest;
    }

    public void handleMessage(Message message) {
        String authHeader = servletRequest.getHeader("Authorization");
        log.info("Forwarding Http Basic Auth header from Original Rest Call to Soap call");
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("Authorization", Arrays.asList(authHeader));
        message.put(Message.PROTOCOL_HEADERS, headers);
    }

}
