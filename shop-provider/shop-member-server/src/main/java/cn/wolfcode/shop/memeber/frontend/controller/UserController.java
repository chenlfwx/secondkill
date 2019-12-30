package cn.wolfcode.shop.memeber.frontend.controller;

import cn.wolfcode.shop.memeber.domain.User;
import cn.wolfcode.shop.memeber.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.selectByPrimaryKey(id);
    }

}
