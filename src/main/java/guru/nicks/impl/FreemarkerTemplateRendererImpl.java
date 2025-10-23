package guru.nicks.impl;

import guru.nicks.service.FreemarkerTemplateRenderer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;

@ConditionalOnMissingBean(FreemarkerTemplateRenderer.class)
@Service
@RequiredArgsConstructor
public class FreemarkerTemplateRendererImpl implements FreemarkerTemplateRenderer {

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
