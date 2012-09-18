/*
 * The MIT License
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., Kohsuke Kawaguchi, Luca Domenico Milanesio, Tom Huybrechts
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.global_variable_string_parameter;

import hudson.Extension;
import hudson.model.ParameterValue;
import hudson.model.Hudson;
import hudson.model.StringParameterDefinition;
import hudson.model.StringParameterValue;
import hudson.slaves.NodeProperty;
import hudson.slaves.EnvironmentVariablesNodeProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * String based parameter that supports substituting global variables.
 * 
 * @author Patrick McKeown
 * @since 1.0
 * @see {@link StringParameterDefinition}
 */
public class GlobalVariableStringParameterDefinition extends StringParameterDefinition {

	@DataBoundConstructor
	public GlobalVariableStringParameterDefinition(String name, String defaultValue, String description) {
		super(name, defaultValue, description);
	}

	@Override
	public ParameterValue createValue(String value) {
		return new StringParameterValue(super.getName(), replaceGlobalEnvVars(value), super.getDescription());
	}

	@Override
	public ParameterValue createValue(StaplerRequest req, JSONObject jo) {
		jo.put("value", replaceGlobalEnvVars(jo.getString("value")));
		return (StringParameterValue) req.bindJSON(StringParameterValue.class, jo);
	}

	@Override
	public StringParameterValue getDefaultParameterValue() {
		return new StringParameterValue(super.getName(), replaceGlobalEnvVars(super.getDefaultValue()), super.getDescription());
	}

	@Extension
	public static class DescriptorImpl extends ParameterDescriptor {
		@Override
		public String getDisplayName() {
			return "Global Variable String Parameter";
		}
	}

	private String replaceGlobalEnvVars(String str) {
		Map map = new HashMap<String, String>();
		// get global properties
		for (NodeProperty nodeProperty : Hudson.getInstance().getGlobalNodeProperties()) {
			if (nodeProperty instanceof EnvironmentVariablesNodeProperty) {
				map.putAll(((EnvironmentVariablesNodeProperty) nodeProperty).getEnvVars());
			}
		}

		// search for variables of the from ${VARNAME}
		Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
		Matcher m = pattern.matcher(str);
		while (m.find()) {
			String s = m.group();
			// strip the variable indicator
			String varName = s.replaceAll("[${}]", "");
			// find the value of the variable
			String varVal = (String) map.get(varName);

			// replace ${var} with the expanded environment variable
			if (varVal != null) {
				str = str.replace(s, varVal);
			}
		}
		return str;
	}
}
