package cn.wolfcode.shop.memeber.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class LoginVO {

    @Pattern(regexp = "1[345689]\\d{9}",message = "手机号不符合")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
