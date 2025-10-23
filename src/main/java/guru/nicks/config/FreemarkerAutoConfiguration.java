package guru.nicks.config;

import guru.nicks.impl.FreemarkerTemplateServiceImpl;
import guru.nicks.service.FreemarkerTemplateService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class FreemarkerAutoConfiguration {

    @ConditionalOnMissingBean(FreemarkerTemplateService.class)
    @Bean
    public FreemarkerTemplateService freemarkerTemplateService(freemarker.template.Configuration configuration) {
        return new FreemarkerTemplateServiceImpl(configuration);
    }

}
