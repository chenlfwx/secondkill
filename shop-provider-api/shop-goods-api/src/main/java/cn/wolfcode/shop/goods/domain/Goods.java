package cn.wolfcode.shop.goods.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class Goods implements Serializable {

    private static final long serialVersionUID = 6445176263003466722L;

    private Long id;
    private String goodName;
    private String goodTitle;
    private String goodImg;
    private String goodDetail;
    private BigDecimal goodPrice;
    private Integer goodStock;
}
