#@disabled
Feature: Freemarker Template Renderer

  Scenario: Rendering a template without parameters
    Given a Freemarker template named "simple.ftl" with content "Hello, World!"
    And no exception should be thrown
    When the template is rendered
    Then no exception should be thrown
    Then the rendered output should be "Hello, World!"

  Scenario: Rendering a template with multiple variables
    Given a Freemarker template named "multiple-vars.ftl" with content "User: ${user.name}, Age: ${user.age}, Email: ${user.email}"
    And no exception should be thrown
    And a template context with a user object having name "John", age 30, and email "john@example.com"
    When the template is rendered
    Then no exception should be thrown
    And the rendered output should contain "User: John"
    And the rendered output should contain "Age: 30"
    And the rendered output should contain "Email: john@example.com"

  # WARNING: '#' can't be passed from Cucumber as-is because it's treated as a comment. Therefore it's passed
  # as '\#'. This requires an explicit removal of '\' in the step definition.
  Scenario: Rendering a template with conditional logic
    Given a Freemarker template named "conditional.ftl" with content "<\#if active>Active<\#else>Inactive</\#if>"
    And no exception should be thrown
    And a template context with key "active" and value true
    When the template is rendered
    Then no exception should be thrown
    And the rendered output should be "Active"

  # WARNING: '#' can't be passed from Cucumber as-is because it's treated as a comment. Therefore it's passed
  # as '\#'. This requires an explicit removal of '\' in the step definition.
  Scenario: Rendering a template with a list
    Given a Freemarker template named "list.ftl" with content "<\#list items as item>${item}<\#sep>, </\#sep></\#list>"
    And no exception should be thrown
    And a template context with key "items" and value list containing "apple", "banana", "cherry"
    When the template is rendered
    Then no exception should be thrown
    And the rendered output should be "apple, banana, cherry"

  Scenario: Handling a non-existent template
    Given a non-existent Freemarker template named "non-existent.ftl"
    And no exception should be thrown
    When the template is rendered
    Then the exception message should contain "Template unreadable"

  Scenario: Handling a template with syntax errors
    Given a Freemarker template named "syntax-error.ftl" with content "${unclosed"
    Then the exception message should contain "Syntax error in template "

  Scenario: Handling a template with missing variables
    Given a Freemarker template named "missing-var.ftl" with content "Hello, ${missing}!"
    And no exception should be thrown
    When the template is rendered
    Then the exception message should contain "The following has evaluated to null or missing:"

  Scenario Outline: Rendering templates with different variable types
    Given a Freemarker template named "<templateName>" with content "<templateContent>"
    And no exception should be thrown
    And a template context with key "<key>" and value <value>
    When the template is rendered
    Then no exception should be thrown
    And the rendered output should be "<expectedOutput>"
    Examples:
      | templateName | templateContent    | key    | value  | expectedOutput |
      | string.ftl   | Value: ${text}     | text   | "test" | Value: test    |
      | number.ftl   | Number: ${number}  | number | 42     | Number: 42     |
      | boolean.ftl  | Is true: ${flag?c} | flag   | true   | Is true: true  |
