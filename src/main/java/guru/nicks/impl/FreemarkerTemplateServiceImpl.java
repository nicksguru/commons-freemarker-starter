package guru.nicks.impl;

import guru.nicks.service.FreemarkerTemplateService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class FreemarkerTemplateServiceImpl implements FreemarkerTemplateService {

    // DI
    private final Configuration configuration;

    @Override
    public String render(String templateName, Map<?, ?> templateContext) {
        Template template;

        try {
            template = configuration.getTemplate(templateName);
        } catch (IOException e) {
            throw new IllegalArgumentException("Template unreadable: " + e.getMessage(), e);
        }

        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateContext);
        } catch (TemplateException | IOException e) {
            throw new IllegalArgumentException("Failed to render template: " + e.getMessage(), e);
        }
    }

}
