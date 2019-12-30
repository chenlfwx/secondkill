package cn.wolfcode.shop.memeber.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by wolfcode-lanxw
 */
public class MD5Util {
    public static final String salt = "1a2b3c4d";
    /**
     * 明文变成表单密文
     *
     * @return
     */
    public static String inputPassToFormPass(String password){
        String str = "" + salt.charAt(0)+salt.charAt(2)+password+salt.charAt(4)+salt.charAt(5);
        return DigestUtils.md5Hex(str);
    }
    public static String formPassToDbPass(String password,String salt){
        String str = "" + salt.charAt(0)+salt.charAt(2)+password+salt.charAt(4)+salt.charAt(5);
        return DigestUtils.md5Hex(str);
    }

    public static void main(String[] args) {
        String inputPass = "111111";
        String dbPass = formPassToDbPass(inputPassToFormPass(inputPass), salt);
        System.out.println(dbPass);
    }
}
