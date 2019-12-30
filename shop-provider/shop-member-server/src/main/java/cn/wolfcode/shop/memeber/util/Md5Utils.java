package cn.wolfcode.shop.memeber.util;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Utils {

    private Md5Utils() {
    }

    public static String encode(String password, String salt) {
        String md5String = "" + salt.charAt(0) + salt.charAt(2) + password + salt.charAt(5) + salt.charAt(4);
        return DigestUtils.md5Hex(md5String);
    }
}
