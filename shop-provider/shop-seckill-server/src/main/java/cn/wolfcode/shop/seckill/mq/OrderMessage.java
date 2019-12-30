package cn.wolfcode.shop.seckill.mq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage implements Serializable {
    private Long userId;
    private Long seckillId;
    private String uuid;
}
