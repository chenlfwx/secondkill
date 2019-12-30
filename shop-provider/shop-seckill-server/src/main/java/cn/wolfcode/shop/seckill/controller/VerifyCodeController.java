package cn.wolfcode.shop.seckill.controller;


import cn.wolfcode.shop.redis.RedisService;
import cn.wolfcode.shop.seckill.redis.SeckillServerKeyPrefix;
import cn.wolfcode.shop.seckill.util.VerifyCodeImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("verifyCode")
public class VerifyCodeController {

    @Autowired
    private RedisService redisService;

    @RequestMapping("/getVerifyCode")
    public void getVerifyCode(String uuid, HttpServletResponse response) throws IOException {
        String code = VerifyCodeImgUtil.generateVerifyCode();
        // 将计算结果放在Redis中
        redisService.set(SeckillServerKeyPrefix.SECKILL_VERIFYCODE, uuid, VerifyCodeImgUtil.calc(code));
        BufferedImage bufferedImage = VerifyCodeImgUtil.createVerifyCodeImg(code);
        ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
    }

}
