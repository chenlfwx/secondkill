package cn.wolfcode.shop.goods.mapper;

import cn.wolfcode.shop.goods.domain.Goods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface GoodsMapper {

    @SelectProvider(value = GoodsMapperSelectProvider.class, method = "queryByIds")
    List<Goods> queryByIds(@Param("ids") List<Long> ids);


    class GoodsMapperSelectProvider {
        public String queryByIds(@Param("ids") List<Long> ids) {
            StringBuilder sb = new StringBuilder(50);
            sb.append("SELECT * FROM t_goods ");
            if (ids != null && !ids.isEmpty()) {
                sb.append("WHERE id IN (");
                for (int i = 0; i < ids.size(); i++) {
                    if (i != 0) {
                        sb.append(",");
                    }
                    sb.append(ids.get(i));
                }
                sb.append(")");
            }
            return sb.toString();
        }
    }
}
