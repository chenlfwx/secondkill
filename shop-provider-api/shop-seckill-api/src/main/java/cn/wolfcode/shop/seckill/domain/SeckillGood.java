package cn.wolfcode.shop.seckill.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@ToString
public class SeckillGood implements Serializable {

    private static final long serialVersionUID = 3118062664476303733L;

    private Long id;
    private Long goodId;
    private BigDecimal seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
