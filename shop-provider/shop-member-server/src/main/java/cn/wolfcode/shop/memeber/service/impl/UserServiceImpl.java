package cn.wolfcode.shop.memeber.service.impl;

import cn.wolfcode.commons.util.BusinessException;
import cn.wolfcode.shop.memeber.domain.User;
import cn.wolfcode.shop.memeber.mapper.UserMapper;
import cn.wolfcode.shop.memeber.redis.MemberServerKeyPrefix;
import cn.wolfcode.shop.memeber.result.MemberServerCodeMsg;
import cn.wolfcode.shop.memeber.service.IUserService;
import cn.wolfcode.shop.memeber.util.Md5Utils;
import cn.wolfcode.shop.memeber.vo.LoginVO;
import cn.wolfcode.shop.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public User selectByPrimaryKey(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public String login(LoginVO loginVO) {
        if (!StringUtils.hasLength(loginVO.getUsername())) {
            throw new BusinessException(MemberServerCodeMsg.OP_ERROR);
        }
        if (!StringUtils.hasLength(loginVO.getPassword())) {
            throw new BusinessException(MemberServerCodeMsg.OP_ERROR);
        }
        User user = selectByPrimaryKey(Long.parseLong(loginVO.getUsername()));
        if (user == null) {
            throw new BusinessException(MemberServerCodeMsg.LOGIN_ERROR);
        }
        // 比较密码
        String encodePassword = Md5Utils.encode(loginVO.getPassword(), user.getSalt());
        if (!encodePassword.equals(user.getPassword())) {
            throw new BusinessException(MemberServerCodeMsg.LOGIN_ERROR);
        }
        return createToken(user);
    }

    @Override
    public boolean refresh(String token) {
        // 判断是不是用户伪造的Token
        MemberServerKeyPrefix memberServerKeyPrefix = MemberServerKeyPrefix.USER_TOKEN;
        if (redisService.existKey(memberServerKeyPrefix, token)) {
            redisService.expire(memberServerKeyPrefix, token, memberServerKeyPrefix.getExpireSeconds());
            return true;
        }
        return false;
    }


    private String createToken(User user) {
        Objects.requireNonNull(user);
        String token = UUID.randomUUID().toString().replace("-", "");
        redisService.set(MemberServerKeyPrefix.USER_TOKEN, token, user);
        return token;
    }
}
