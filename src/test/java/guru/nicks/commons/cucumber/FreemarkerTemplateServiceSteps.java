package guru.nicks.commons.cucumber;

import guru.nicks.commons.cucumber.world.TextWorld;
import guru.nicks.commons.impl.FreemarkerTemplateServiceImpl;
import guru.nicks.commons.service.FreemarkerTemplateService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Step definitions for testing {@link FreemarkerTemplateService}.
 */
@RequiredArgsConstructor
public class FreemarkerTemplateServiceSteps {

    // DI
    private final TextWorld textWorld;

    @Mock
    private Configuration configuration;
    private AutoCloseable closeableMocks;

    private FreemarkerTemplateService templateRenderer;
    private String templateName;
    private Map<String, Object> templateContext;
    private String renderedOutput;

    @Before
    public void beforeEachScenario() {
        closeableMocks = MockitoAnnotations.openMocks(this);
        templateRenderer = new FreemarkerTemplateServiceImpl(configuration);
        templateContext = new HashMap<>();
    }

    @After
    public void afterEachScenario() throws Exception {
        closeableMocks.close();
    }

    @DataTableType
    public UserEntry createUserEntry(Map<String, String> entry) {
        return UserEntry.builder()
                .name(entry.get("name"))
                .age(Integer.parseInt(entry.get("age")))
                .email(entry.get("email"))
                .build();
    }

    @Given("a Freemarker template named {string} with content {string}")
    public void aFreemarkerTemplateNamedWithContent(String name, String content) throws IOException {
        templateName = name;

        // WARNING: '#' can't be passed from Cucumber as-is because it's treated as a comment. Therefore it's passed
        // as '\#'. This requires an explicit removal of '\'.
        Template realTemplate;
        try {
            realTemplate = new Template(name, new StringReader(content.replace("\\", "")),
                    new Configuration(Configuration.VERSION_2_3_31));
        } catch (Exception e) {
            textWorld.setLastException(e);
            return;
        }

        // mock the configuration to return our template
        when(configuration.getTemplate(name))
                .thenReturn(realTemplate);
    }

    @Given("a non-existent Freemarker template named {string}")
    public void aNonExistentFreemarkerTemplateNamed(String name) throws IOException {
        templateName = name;

        // mock the configuration to throw an IOException when the template is requested
        when(configuration.getTemplate(name))
                .thenThrow(new IOException("Template not found"));
    }

    @Given("a template context with key {string} and value {string}")
    public void aTemplateContextWithKeyAndStringValue(String key, String value) {
        templateContext.put(key, value);
    }

    @Given("a template context with key {string} and value {int}")
    public void aTemplateContextWithKeyAndIntValue(String key, Integer value) {
        templateContext.put(key, value);
    }

    @Given("a template context with key {string} and value {booleanValue}")
    public void aTemplateContextWithKeyAndBooleanValue(String key, Boolean value) {
        templateContext.put(key, value);
    }

    @Given("a template context with key {string} and value list containing {string}, {string}, {string}")
    public void aTemplateContextWithKeyAndValueListContaining(String key, String item1, String item2, String item3) {
        List<String> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);
        templateContext.put(key, items);
    }

    @Given("a template context with a user object having name {string}, age {int}, and email {string}")
    public void aTemplateContextWithAUserObjectHaving(String name, int age, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("age", age);
        user.put("email", email);
        templateContext.put("user", user);
    }

    @When("the template is rendered")
    public void theTemplateIsRendered() {
        try {
            renderedOutput = templateRenderer.render(templateName, templateContext);
        } catch (Exception e) {
            textWorld.setLastException(e);
        }
    }

    @Then("the rendered output should be {string}")
    public void theRenderedOutputShouldBe(String expected) {
        assertThat(renderedOutput)
                .as("renderedOutput")
                .isEqualTo(expected);
    }

    @Then("the rendered output should contain {string}")
    public void theRenderedOutputShouldContain(String expected) {
        assertThat(renderedOutput)
                .as("renderedOutput")
                .contains(expected);
    }

    /**
     * Data class for user entries in data tables.
     */
    @Value
    @Builder
    public static class UserEntry {

        String name;
        int age;
        String email;

    }

}
