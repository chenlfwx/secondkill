package cn.wolfcode.commons.util;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CodeMsg {

    /** 状态码 **/
    private Integer code;

    /** 消息 **/
    private String msg;

}
