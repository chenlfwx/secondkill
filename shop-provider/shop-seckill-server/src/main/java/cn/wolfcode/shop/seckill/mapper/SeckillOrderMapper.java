package cn.wolfcode.shop.seckill.mapper;

import cn.wolfcode.shop.seckill.domain.SeckillOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SeckillOrderMapper {

    @Insert("INSERT INTO t_seckill_order(user_id,order_no,seckill_id) VALUES(#{userId},#{orderNo},#{seckillId})")
    void insert(SeckillOrder order);

    @Select("SELECT * FROM t_seckill_order WHERE user_id = #{userId} AND seckill_id = #{seckillId}")
    SeckillOrder findByUserIdAndSeckillId(@Param("userId") Long userId, @Param("seckillId") Long seckillId);
}
