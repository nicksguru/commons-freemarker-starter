package guru.nicks.commons.config;

import guru.nicks.commons.impl.FreemarkerTemplateServiceImpl;
import guru.nicks.commons.service.FreemarkerTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class CommonsFreemarkerAutoConfiguration {

    /**
     * Creates {@link FreemarkerTemplateService} bean if it's not already present.
     */
    @ConditionalOnMissingBean(FreemarkerTemplateService.class)
    @Bean
    public FreemarkerTemplateService freemarkerTemplateService(freemarker.template.Configuration configuration) {
        log.debug("Building {} bean", FreemarkerTemplateService.class.getSimpleName());
        return new FreemarkerTemplateServiceImpl(configuration);
    }

}
