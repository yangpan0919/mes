package cn.tzauto.mes.conf;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfig {
    //获得上下文,用于在多线程是从容器获取对象
    public static ConfigurableApplicationContext context ;

    @Bean
    public ConfigurableApplicationContext test(ConfigurableApplicationContext  c){
        context = c;
        return c;
    }
}
