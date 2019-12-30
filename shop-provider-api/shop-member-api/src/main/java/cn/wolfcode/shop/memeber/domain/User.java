package cn.wolfcode.shop.memeber.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1639563449819006547L;

    private Long id;
    private String nickname;//昵称
    @JsonIgnore
    private String password;//密码
    @JsonIgnore
    private String salt;//盐
    private String head;//头像
    private Date registerDate;//注册时间
    private Date lastLoginDate;//最后登录时间
    private Integer loginCount;//登录次数
}
