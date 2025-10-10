package guru.nicks;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Renders Freemarker templates.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FreemarkerTemplateRenderer {

    // DI
    private final Configuration configuration;

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
