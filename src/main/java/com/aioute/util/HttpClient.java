package com.aioute.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.util.StringUtils;

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

    public String sendByGet(String url, String userId) throws IOException {
        if (StringUtils.hasText(userId)) {
            this.parameter.put("userId", userId);
        }
        return sendGet(url);
    }

    private String sendGet(String url) {
        try {
            // 创建一个httpclient对象
            CloseableHttpClient client = HttpClients.custom().build();
            // 创建URIBuilder
            URIBuilder uri = new URIBuilder(url);
            // 设置参数
            if (this.parameter.size() > 0) {
                Iterator<String> it = this.parameter.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    uri.addParameter(key, this.parameter.get(key));
                }
            }
            // 创建httpGet对象
            HttpGet hg = new HttpGet(uri.build());
            // 设置请求的报文头部的编码
            hg.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            // 设置期望服务端返回的编码
            hg.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
            // 请求服务
            CloseableHttpResponse response = client.execute(hg);
            // 获取响应码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // 获取返回实例entity
                HttpEntity entity = response.getEntity();
                // 通过EntityUtils的一个工具方法获取返回内容
                String resStr = EntityUtils.toString(entity, "utf-8");
                // 输出
                System.out.println("地址: " + url + "\n返回内容为: " + resStr);
                return resStr;
            } else {
                // 输出
                System.out.println("请求失败,错误码为: " + statusCode);
            }
            // 关闭response和client
            response.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}