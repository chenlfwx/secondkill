package cn.wolfcode.shop.memeber.frontend.controller;

import cn.wolfcode.commons.util.CookUtils;
import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.memeber.domain.User;
import cn.wolfcode.shop.memeber.service.IUserService;
import cn.wolfcode.shop.memeber.util.DBUtil;
import cn.wolfcode.shop.memeber.util.Md5Utils;
import cn.wolfcode.shop.memeber.vo.LoginVO;
import cn.wolfcode.shop.memeber.web.resolver.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@Validated LoginVO loginVO, HttpServletResponse response) {
        String token = userService.login(loginVO);
        CookUtils.add(response, CookUtils.USER_TOKEN, token, CookUtils.DEFAULT_MAX_AGE);
        return Result.success(null);
    }


    @RequestMapping("/currentUser")
    public Result<User> currentUser(@LoginUser User user) {
        System.out.println(user);
        return Result.success(user);
    }

    @RequestMapping("/initData")
    public Result<String> initData() throws Exception {
        List<User> users = initUser(500);
        insertToDb(users);
        createToken(users);
        return Result.success("");
    }

    private void createToken(List<User> users) throws Exception {
        File file = new File("D:/tokens.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            LoginVO vo = new LoginVO();
            vo.setUsername(users.get(i).getId() + "");
            vo.setPassword("111111");
            String token = userService.login(vo);
            String row = users.get(i).getId() + "," + token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
        }
        raf.close();
    }

    private void insertToDb(List<User> users) throws Exception {
        Connection conn = DBUtil.getConn();
        String sql = "insert into t_user(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            pstmt.setInt(1, user.getLoginCount());
            pstmt.setString(2, user.getNickname());
            pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            pstmt.setString(4, user.getSalt());
            pstmt.setString(5, user.getPassword());
            pstmt.setLong(6, user.getId());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
    }

    private List<User> initUser(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(13000000000L + i);
            user.setLoginCount(1);
            user.setNickname("user" + i);
            user.setRegisterDate(new Date());
            user.setSalt("1a2b3c4d");
            user.setPassword(Md5Utils.encode("111111", user.getSalt()));
            users.add(user);
        }
        return users;
    }
}
