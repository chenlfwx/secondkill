package cn.wolfcode.shop.memeber.mapper;

import cn.wolfcode.shop.memeber.domain.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("select * from t_user where id = #{id}")
    User selectByPrimaryKey(Long id);

}
