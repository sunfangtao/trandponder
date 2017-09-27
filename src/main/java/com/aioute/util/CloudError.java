package com.aioute.util;

/**
 * 平台上的错误信息
 */
public class CloudError {

    /**
     * 返回结果
     */
    public enum ResultEnum {

        SUCCESS("success"),
        FAIL("fail"),;

        private String value;

        ResultEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Json返回的类型
     */
    public enum ReasonEnum {
        REPEAT("RepeatException"),
        NODATA("NoDataException"),
        SQLEXCEPTION("SqlException"),
        SERVEREXCEPTION("ServerException"),
        NORMAL("Normal"),
        PERMISSION("Permission"),
        NOTLOGIN("NoLogin"),
        PASSWORDERROR("PasswordErrorException"),
        NOACCOUNT("UnkonwAccountException"),
        NOREQUIREPARAMS("NoRequireParams"),
        IOException("IOException");
        private String value;

        ReasonEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 排序的类型
     */
    public enum SortEnum {
        DISTANCE("distance"),
        PRICE("price"),
        CREATE_TIME("createTime"),
        PRICE_DESC("priceDesc"),
        PRICE_ASC("priceAsc");
        private String value;

        SortEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
