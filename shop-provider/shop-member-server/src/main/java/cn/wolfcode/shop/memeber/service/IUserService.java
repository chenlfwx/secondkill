package cn.wolfcode.shop.memeber.service;

import cn.wolfcode.shop.memeber.domain.User;
import cn.wolfcode.shop.memeber.vo.LoginVO;

public interface IUserService {

    User selectByPrimaryKey(Long id);

    String login(LoginVO loginVO);

    boolean refresh(String token);

}
