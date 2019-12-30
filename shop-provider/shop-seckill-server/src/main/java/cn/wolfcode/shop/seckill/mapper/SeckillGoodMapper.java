package cn.wolfcode.shop.seckill.mapper;

import cn.wolfcode.shop.seckill.domain.SeckillGood;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SeckillGoodMapper {

    @Select("SELECT * FROM t_seckill_goods")
    List<SeckillGood> query();

    @Select("SELECT * FROM t_seckill_goods WHERE id = #{id}")
    SeckillGood selectByPrimaryKey(Long seckillId);


    @Update("UPDATE t_seckill_goods SET stock_count = stock_count - 1 WHERE id = #{seckillId} AND stock_count > 0")
    int updateStock(Long seckillId);

    @Select("SELECT stock_count FROM t_seckill_goods WHERE id = #{seckillId}")
    int getGoodStock(Long seckillId);

    @Update("UPDATE t_seckill_goods SET stock_count = stock_count + 1 WHERE id = #{seckillId}")
    void incrStock(Long seckillId);
}
