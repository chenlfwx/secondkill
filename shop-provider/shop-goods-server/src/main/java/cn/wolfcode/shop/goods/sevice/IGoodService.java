package cn.wolfcode.shop.goods.sevice;

import cn.wolfcode.shop.goods.domain.Goods;

import java.util.List;

public interface IGoodService {

    List<Goods> queryByIds(List<Long> ids);
}
