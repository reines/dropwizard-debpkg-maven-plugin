public static Object validate() {
    final Class configClass = Class.forName("{{{configClass}}}");
    final java.io.File configFile = new java.io.File("{{{configFile}}}");

    final javax.validation.Validator validator = javax.validation.Validation.buildDefaultValidatorFactory().getValidator();
    final com.fasterxml.jackson.databind.ObjectMapper objectMapper = io.dropwizard.jackson.Jackson.newObjectMapper();
    final io.dropwizard.configuration.ConfigurationFactory configurationFactory = new io.dropwizard.configuration.ConfigurationFactory(configClass, validator, objectMapper, "dw");

    return configurationFactory.build(configFile);
}
