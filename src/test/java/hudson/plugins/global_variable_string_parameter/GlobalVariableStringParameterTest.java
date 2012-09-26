package hudson.plugins.global_variable_string_parameter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import hudson.EnvVars;
import hudson.model.StringParameterValue;
import hudson.slaves.EnvironmentVariablesNodeProperty;
import java.io.IOException;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class GlobalVariableStringParameterTest extends HudsonTestCase {
	private void addVariable(String key, String value) throws IOException{
		EnvironmentVariablesNodeProperty prop = new EnvironmentVariablesNodeProperty();
	    EnvVars envVars = prop.getEnvVars();
	    envVars.put(key, value);
	    super.hudson.getGlobalNodeProperties().add(prop);
	}
	
	@Test
	public void testNoReplacement() throws IOException {
		addVariable("globalKey","globalValue");
		
		String name = "test";
		String value = "testValue";
		String description = "Test Description";
		GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name,value,description);
		
		//Verify that default and created values remain unchanged.
		assertFalse(GlobalVariableStringParameterDefinition.globalVarExists(value));
		assertThat(td.getDefaultParameterValue().value, is(value));
		assertThat(((StringParameterValue) td.createValue(value)).value, is(value));
	}
	
	@Test
	public void testSimpleReplacement() throws IOException {		
		addVariable("globalKey","globalValue");
		
		String name = "test";
		String value = "$globalKey";
		String description = "Test Description";
		GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name,value,description);
		
		//Verify that default and created values are changed.
		assertTrue(GlobalVariableStringParameterDefinition.globalVarExists(value));
		assertThat(td.getDefaultParameterValue().value, is("globalValue"));
		assertThat(((StringParameterValue) td.createValue(value)).value, is("globalValue"));
	}
	
	@Test
	public void testSimpleBracesReplacement() throws IOException {		
		addVariable("globalKey","globalValue");
		
		String name = "test";
		String value = "${globalKey}";
		String description = "Test Description";
		GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name,value,description);
		
		//Verify that default and created values are changed.
		assertTrue(GlobalVariableStringParameterDefinition.globalVarExists(value));
		assertThat(td.getDefaultParameterValue().value, is("globalValue"));
		assertThat(((StringParameterValue) td.createValue(value)).value, is("globalValue"));
	}
	
	@Test
	public void testSpacesBracesReplacement() throws IOException {		
		addVariable("global Key","global Value");
		
		String name = "test";
		String value = "${global Key}";
		String description = "Test Description";
		GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name,value,description);
		
		//Verify that default and created values are changed.
		assertTrue(GlobalVariableStringParameterDefinition.globalVarExists(value));
		assertThat(td.getDefaultParameterValue().value, is("global Value"));
		assertThat(((StringParameterValue) td.createValue(value)).value, is("global Value"));
	}
	
	@Test
	public void testMissingVariable() throws IOException {		
		addVariable("globalKey","globalValue");
		
		String name = "test";
		String value = "${wrong Global Key}";
		String description = "Test Description";
		GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name,value,description);
		
		//Verify that default and created values remain unchanged.		
		assertFalse(GlobalVariableStringParameterDefinition.globalVarExists(value));
		assertThat(td.getDefaultParameterValue().value, is(value));
		assertThat(((StringParameterValue) td.createValue(value)).value, is(value));
	}	
	
	@Test
	public void testBlankVariable() throws IOException {		
		addVariable("globalKey","globalValue");
		
		String name = "test";
		String value = "${}";
		String description = "Test Description";
		GlobalVariableStringParameterDefinition td = new GlobalVariableStringParameterDefinition(name,value,description);
		
		//Verify that default and created values remain unchanged.
		assertFalse(GlobalVariableStringParameterDefinition.globalVarExists(value));
		assertThat(td.getDefaultParameterValue().value, is(value));
		assertThat(((StringParameterValue) td.createValue(value)).value, is(value));
	}
}
