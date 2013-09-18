package hudson.plugins.global_variable_string_parameter;

import hudson.EnvVars;
import hudson.model.StringParameterValue;
import hudson.slaves.EnvironmentVariablesNodeProperty;
import java.io.IOException;
import jenkins.model.Jenkins;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class GlobalVariableStringParameterTest {

    @Rule
    public JenkinsRule j = new JenkinsRule() {
    };

    private void addVariable(String key, String value) throws IOException {
        EnvironmentVariablesNodeProperty prop = new EnvironmentVariablesNodeProperty();
        EnvVars envVars = prop.getEnvVars();
        envVars.put(key, value);
        Jenkins.getInstance().getGlobalNodeProperties().add(prop);
    }

    @Test
    public void testNoReplacement() throws IOException {
        addVariable("globalKey", "globalValue");

        String name = "test";
        String value = "testValue";
        String description = "Test Description";
        GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name, value, description);

        //Verify that default and created values remain unchanged.
        assertFalse(GlobalVariableStringParameterDefinition.globalVarExists(value));
        assertThat(td.getDefaultParameterValue().value, is(value));
        assertThat(((StringParameterValue) td.createValue(value)).value, is(value));
    }

    @Test
    public void testSimpleReplacement() throws IOException {
        addVariable("globalKey", "globalValue");

        String name = "test";
        String value = "$globalKey";
        String description = "Test Description";
        GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name, value, description);

        //Verify that default and created values are changed.
        assertTrue(GlobalVariableStringParameterDefinition.globalVarExists(value));
        assertThat(td.getDefaultParameterValue().value, is("globalValue"));
        assertThat(((StringParameterValue) td.createValue(value)).value, is("globalValue"));
    }

    @Test
    public void testSimpleBracesReplacement() throws IOException {
        addVariable("globalKey", "globalValue");

        String name = "test";
        String value = "${globalKey}";
        String description = "Test Description";
        GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name, value, description);

        //Verify that default and created values are changed.
        assertTrue(GlobalVariableStringParameterDefinition.globalVarExists(value));
        assertThat(td.getDefaultParameterValue().value, is("globalValue"));
        assertThat(((StringParameterValue) td.createValue(value)).value, is("globalValue"));
    }

    @Test
    public void testSpacesBracesReplacement() throws IOException {
        addVariable("global Key", "global Value");

        String name = "test";
        String value = "${global Key}";
        String description = "Test Description";
        GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name, value, description);

        //Verify that default and created values are changed.
        assertTrue(GlobalVariableStringParameterDefinition.globalVarExists(value));
        assertThat(td.getDefaultParameterValue().value, is("global Value"));
        assertThat(((StringParameterValue) td.createValue(value)).value, is("global Value"));
    }

    @Test
    public void testMissingVariable() throws IOException {
        addVariable("globalKey", "globalValue");

        String name = "test";
        String value = "${wrong Global Key}";
        String description = "Test Description";
        GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name, value, description);

        //Verify that default and created values remain unchanged.		
        assertFalse(GlobalVariableStringParameterDefinition.globalVarExists(value));
        assertThat(td.getDefaultParameterValue().value, is(value));
        assertThat(((StringParameterValue) td.createValue(value)).value, is(value));
    }

    @Test
    public void testBlankVariable() throws IOException {
        addVariable("globalKey", "globalValue");

        String name = "test";
        String value = "${}";
        String description = "Test Description";
        GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name, value, description);

        //Verify that default and created values remain unchanged.
        assertFalse(GlobalVariableStringParameterDefinition.globalVarExists(value));
        assertThat(td.getDefaultParameterValue().value, is(value));
        assertThat(((StringParameterValue) td.createValue(value)).value, is(value));
    }

    @Test
    public void testConcurrentReplacement() throws IOException {
        addVariable("globalKey", "globalValue");

        // Generate 1000 random global variables
        for (int x = 0; x < 1000; x++) {
            addVariable(Integer.toString(x), Integer.toString(x));
        }

        final String name = "test";
        final String value = "$globalKey";
        final String description = "Test Description";
        final GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name, value, description);

        Runnable r = new Runnable() {
            public void run() {
                //Verify that default and created values are changed.
                assertTrue(GlobalVariableStringParameterDefinition.globalVarExists(value));
                assertThat(td.getDefaultParameterValue().value, is("globalValue"));
                assertThat(((StringParameterValue) td.createValue(value)).value, is("globalValue"));
            }
        };

        for (int x = 0; x < 10; x++) {
            // Run 10 substitutions simulataneously
            r.run();
        }

    }
}
