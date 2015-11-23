package com.team.xg.download;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class StringUtils {

    /**
     * 获得非空字段
     *
     * @param input
     * @return
     */
    public static String getNotNullStr(String input) {
        if (input == null) {
            return "";
        } else if (input.equals("") || input.equals("null")) {
            return "";
        } else {
            return input;
        }
    }

    /**
     * 值是否为空
     *
     * @param input
     * @return
     */
    public static boolean hasValue(String input) {
        if (getNotNullStr(input).equals("")) {
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTimeFromTimeStamp(String input) {

        try {

            if (hasValue(input) && input.length() > 3) {

                String timeStamp = input.substring(0, input.length() - 3);

                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(Long.parseLong(timeStamp) * 1000));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获得URL上面的参数
     *
     * @param url
     * @param name
     * @return
     */
    public static String getUrlParam(String url, String name) {

        try {

            if (hasValue(url) && hasValue(name)) {

                String[] paramArr = url.substring(url.indexOf("?") + 1).split(
                        "&");
                for (String string : paramArr) {
                    if (string.startsWith(name)) {
                        return string.split("=")[1];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }
}
