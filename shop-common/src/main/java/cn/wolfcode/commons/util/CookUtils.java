package cn.wolfcode.commons.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookUtils {

    private CookUtils() {
    }

    public static final String USER_TOKEN = "userToken";

    public static final int DEFAULT_MAX_AGE = 1800;

    /**
     * 添加指定的值到Cookie中
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param maxAge 以秒为单位
     */
    public static void add(HttpServletResponse response, String cookieName, String cookieValue, int maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }


    public static String getCookie(HttpServletRequest request, String cookieName) {
        if (StringUtils.isEmpty(cookieName)) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
