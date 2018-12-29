package com.netopstec.productcollector.util;

/**
 * UrlUtil
 *
 * @author linyi
 * @date 2018/12/28 10:20
 */
public class UrlUtil {

    /**
     * from url find key and value.
     * for example: https://list.tmall.com/search_product.htm?cat=50026391, getvalueByKey("cat=", url), you can get 50026391.
     */
    public static Long getValueByKey(String key, String url) {
        if (key == null || "".equals(key)) {
            return null;
        }
        if (url == null || "".equals(url)) {
            return null;
        }
        int startIndex = url.indexOf(key);
        int endIndex = url.indexOf("&", startIndex);
        if (endIndex == -1) {
            return Long.valueOf(url.substring(startIndex + key.length()));
        }
        return Long.valueOf(url.substring(startIndex + key.length(), endIndex));
    }

}
