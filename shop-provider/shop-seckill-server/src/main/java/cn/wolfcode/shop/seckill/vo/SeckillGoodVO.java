package cn.wolfcode.shop.seckill.vo;

import cn.wolfcode.shop.seckill.domain.SeckillGood;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;


@Setter
@Getter
@ToString
public class SeckillGoodVO extends SeckillGood {
    private static final long serialVersionUID = 5466834133723780002L;

    private String goodName;
    private String goodTitle;
    private String goodImg;
    private String goodDetail;
    private BigDecimal goodPrice;
}
