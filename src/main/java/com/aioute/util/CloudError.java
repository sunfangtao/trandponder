package com.aioute.util;

/**
 * 平台上的错误信息
 */
public class CloudError {

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

    /**
     * 收藏的类型
     */
    public enum FavouriteEnum {
        NEW_CAR("newCar"),
        SELL_CAR("sellCar"),
        PLAT_USER("user");
        private String value;

        FavouriteEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
