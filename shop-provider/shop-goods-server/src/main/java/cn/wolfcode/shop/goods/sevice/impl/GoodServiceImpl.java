package cn.wolfcode.shop.goods.sevice.impl;

import cn.wolfcode.shop.goods.domain.Goods;
import cn.wolfcode.shop.goods.mapper.GoodsMapper;
import cn.wolfcode.shop.goods.sevice.IGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class GoodServiceImpl implements IGoodService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<Goods> queryByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return goodsMapper.queryByIds(ids);
    }
}
