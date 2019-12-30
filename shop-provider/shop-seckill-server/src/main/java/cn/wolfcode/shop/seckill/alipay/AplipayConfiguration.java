package cn.wolfcode.shop.seckill.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AlipayProperties.class)
public class AplipayConfiguration {

    private final AlipayProperties alipayProperties;

    public AplipayConfiguration(AlipayProperties alipayProperties) {
        this.alipayProperties = alipayProperties;
    }

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(alipayProperties.getGatewayUrl(),
                alipayProperties.getAppId(), alipayProperties.getMerchantPrivateKey(),
                "json", alipayProperties.getCharset(), alipayProperties.getAlipayPublicKey(), alipayProperties.getSignType());
    }
}
