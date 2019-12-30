package cn.wolfcode.shop.goods.feign;

import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.goods.domain.Goods;
import cn.wolfcode.shop.goods.feign.hystrix.GoodsFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "goods-server",fallback = GoodsFeignHystrix.class)
public interface GoodsFeignApi {
    @RequestMapping("/queryByIds")
    Result<List<Goods>> queryByIds(@RequestParam("ids") List<Long> ids);
}