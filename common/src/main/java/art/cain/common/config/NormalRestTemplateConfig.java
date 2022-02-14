package art.cain.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NormalRestTemplateConfig {

    private static final int CONNECT_TIMEOUT = 100 * 1000;
    private static final int READ_TIMEOUT = 100 * 1000;
//    private static final int CONNECT_TIMEOUT = 10 * 1000;
//    private static final int READ_TIMEOUT = 10 * 1000;

    @Bean(name = "normalRestTemplate")
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(READ_TIMEOUT);//单位为ms
        factory.setConnectTimeout(CONNECT_TIMEOUT);//单位为ms
        return factory;
    }
}