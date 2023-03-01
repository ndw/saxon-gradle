ruleset {
  ruleset('rulesets/basic.xml')
  ruleset('rulesets/imports.xml')
  ruleset('rulesets/naming.xml') {
    exclude 'PropertyName'
    'ClassName' {
      regex = '^[A-Z][a-zA-Z0-9]*$'
    }
    'FieldName' {
      finalRegex = '^_?[a-z][a-zA-Z0-9]*$'
      staticFinalRegex = '^[A-Z][A-Z_0-9]*$'
    }
    'MethodName' {
      regex = '^[a-z][a-zA-Z0-9_]*$'
    }
    'VariableName' {
      finalRegex = '^_?[a-z][a-zA-Z0-9]*$'
    }
  }
  ruleset('rulesets/unused.xml')
  ruleset('rulesets/exceptions.xml')
  ruleset('rulesets/logging.xml') {
    exclude 'Println'
  }
  ruleset('rulesets/braces.xml')
  ruleset('rulesets/size.xml') {
    exclude 'CrapMetric'
  }
  ruleset('rulesets/junit.xml')
  ruleset('rulesets/dry.xml')
  ruleset('rulesets/design.xml') {
    exclude 'Instanceof'
  }
}
