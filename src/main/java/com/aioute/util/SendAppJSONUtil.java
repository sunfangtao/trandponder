package com.aioute.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

public class SendAppJSONUtil {

    private static class Json {
        public Object array;
        public Object info;
    }

    private static class PageJson {
        public int page;
        public int pageSize;
        public int count;
        public Object array;
        public Object info;
    }

    private static class ReturnJson {
        public String result;
        public String type;
        public String message;
        public Object data;
    }

    /**
     * 列表查询返回数据，带有页码（如果请求中带有页码的话）
     *
     * @param page
     * @param pageSize
     * @param object
     * @return
     */
    public static String getPageJsonString(int page, int pageSize, int count, Object object) {

        PageJson data = new PageJson();
        if (page >= 0 && pageSize > 0 && count > 0) {
            data.page = page;
            data.pageSize = pageSize;
            data.count = count;
        }
        data.array = object;

        ReturnJson returnJson = new ReturnJson();
        returnJson.result = CloudError.ResultEnum.SUCCESS.getValue();
        returnJson.type = CloudError.ReasonEnum.NORMAL.getValue();
        returnJson.message = "";
        returnJson.data = data;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("result", CloudError.ResultEnum.SUCCESS.getValue());
        jsonObject.addProperty("type", CloudError.ReasonEnum.NORMAL.getValue());
        jsonObject.addProperty("message", "");

        return new Gson().toJson(returnJson);
    }

    /**
     * 正常返回的Json
     *
     * @param object 用户查询的数据
     * @return
     */
    public static String getNormalString(Object object) {
        ReturnJson returnJson = new ReturnJson();
        returnJson.result = CloudError.ResultEnum.SUCCESS.getValue();
        returnJson.type = CloudError.ReasonEnum.NORMAL.getValue();
        returnJson.message = "";

        if (object != null) {
            Json json = new Json();
            if (object instanceof Map || object instanceof List) {
                json.array = object;
            } else {
                json.info = object;
            }
            returnJson.data = json;
        }
        return new Gson().toJson(returnJson);
    }

    /**
     * 必要参数缺失返回的json
     *
     * @param message 提示用户的内容
     * @return
     */
    public static String getRequireParamsMissingObject(String message) {
        ReturnJson returnJson = new ReturnJson();
        returnJson.result = CloudError.ResultEnum.FAIL.getValue();
        returnJson.type = CloudError.ReasonEnum.NOREQUIREPARAMS.getValue();
        returnJson.message = (message == null ? "缺少参数" : message);
        returnJson.data = "{}";

        return new Gson().toJson(returnJson);
    }

    /**
     * 查询结果为空返回的json
     *
     * @return
     */
    public static String getNullResultObject() {
        ReturnJson returnJson = new ReturnJson();
        returnJson.result = CloudError.ResultEnum.FAIL.getValue();
        returnJson.type = CloudError.ReasonEnum.NODATA.getValue();
        returnJson.message = "没有查询到结果";
        returnJson.data = "{}";

        return new Gson().toJson(returnJson);
    }

    /**
     * 一般失败返回的json
     *
     * @param type
     * @param message
     * @return
     */
    public static String getFailResultObject(String type, String message) {
        ReturnJson returnJson = new ReturnJson();
        returnJson.result = CloudError.ResultEnum.FAIL.getValue();
        returnJson.type = type;
        returnJson.message = (message == null ? "没有错误提示" : message);
        returnJson.data = "{}";

        return new Gson().toJson(returnJson);
    }

    /**
     * 服务器异常返回的json
     *
     * @return
     */
    public static String getServerExceptionResultObject() {
        ReturnJson returnJson = new ReturnJson();
        returnJson.result = CloudError.ResultEnum.FAIL.getValue();
        returnJson.type = CloudError.ReasonEnum.SERVEREXCEPTION.getValue();
        returnJson.message = "服务器异常";
        returnJson.data = "{}";

        return new Gson().toJson(returnJson);
    }
}
