package cn.wolfcode.shop.goods.feign.hystrix;

import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.goods.domain.Goods;
import cn.wolfcode.shop.goods.feign.GoodsFeignApi;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoodsFeignHystrix implements GoodsFeignApi {
    @Override
    public Result<List<Goods>> queryByIds(List<Long> ids) {
        return null;
    }
}
