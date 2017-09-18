package com.aioute.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpClient {

    private Map<String, String> parameter = new HashMap<String, String>();
    private HttpServletResponse response;
    private HttpServletRequest request;
    private String type = "";

    public HttpClient(HttpServletRequest req, HttpServletResponse res) {
        this.request = req;
        this.response = res;
        setParameterMap(req.getParameterMap());
    }

    public HttpClient(HttpServletRequest req, HttpServletResponse res, String type) {
        if (this.type.equalsIgnoreCase("GET")) {
            this.type = "GET";
        }
        if (this.type.equalsIgnoreCase("POST")) {
            this.type = "POST";
        }
        this.request = req;
        this.response = res;
        setParameterMap(req.getParameterMap());
    }

    public void setParameter(String key, String value) {
        this.parameter.put(key, value);
    }

    private void setParameterMap(Map<String, String[]> map) {
        if (map != null) {
            for (String key : map.keySet()) {
                this.parameter.put(key, map.get(key)[0]);
            }
        }
    }

    public void send(String url) {
        try {
//            if ((this.type.length() == 0 && this.request.getMethod().equalsIgnoreCase("GET")) || this.type.equals("GET")) {
//                sendByGet(url);
//            } else {
//                sendByPost(url);
//            }
            sendByGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendByGet(String url) throws IOException {
        if (this.parameter.size() > 0) {
            StringBuffer sb = new StringBuffer();
            Iterator<String> it = this.parameter.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                sb.append(key).append("=").append(this.parameter.get(key));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            response.sendRedirect(url + "?" + sb.toString());
        } else {
            response.sendRedirect(url);
        }
    }

}