package cn.wolfcode.shop.seckill.mapper;

import cn.wolfcode.shop.seckill.domain.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface OrderInfoMapper {

    @Insert("insert into t_order_info " +
            "(order_no,user_id,good_id,good_img,delivery_addr_id,good_name,good_count,good_price,seckill_price,status,create_date,pay_date)" +
            " values " +
            "(#{orderNo},#{userId},#{goodId},#{goodImg},#{deliveryAddrId},#{goodName},#{goodCount},#{goodPrice},#{seckillPrice},#{status},#{createDate},#{payDate})")
    void insert(OrderInfo orderInfo);

    @Select("SELECT * FROM t_order_info WHERE order_no = #{orderNo}")
    OrderInfo selectByOrderNo(String orderNo);

    @Update("update t_order_info set status = #{status} WHERE order_no = #{orderNo}")
    int updateOrderCancelStatue(@Param("orderNo") String orderNo, @Param("status") Integer status);

    @Update("UPDATE t_order_info SET status = #{status} WHERE orderNo = #{orderNo}")
    void updatePayStatus(@Param("orderNo") String orderNo, @Param("status") Integer status);
}
