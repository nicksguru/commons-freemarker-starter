package guru.nicks.commons.service;

import java.util.Map;

/**
 * Renders Freemarker templates.
 */
public interface FreemarkerTemplateService {

    /**
     * Renders a template with the provided context data.
     *
     * @param templateName    the name of the template to render
     * @param templateContext data model used for substituting template variables
     * @return the rendered template as a string
     */
    String render(String templateName, Map<?, ?> templateContext);

}
