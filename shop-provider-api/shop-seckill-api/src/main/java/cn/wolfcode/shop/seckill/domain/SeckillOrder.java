package cn.wolfcode.shop.seckill.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Setter
@Getter
public class SeckillOrder implements Serializable {
    private static final long serialVersionUID = -1876973176367805479L;
    private Long id;
    private String orderNo;
    private Long userId;
    private Long seckillId;
}
