package cn.wolfcode.shop.goods.feign;

import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.goods.domain.Goods;
import cn.wolfcode.shop.goods.sevice.IGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GoodsFeignClient implements GoodsFeignApi {

    @Autowired
    private IGoodService goodService;

    @Override
    public Result<List<Goods>> queryByIds(@RequestParam("ids") List<Long> ids) {
        return Result.success(goodService.queryByIds(ids));
    }
}
