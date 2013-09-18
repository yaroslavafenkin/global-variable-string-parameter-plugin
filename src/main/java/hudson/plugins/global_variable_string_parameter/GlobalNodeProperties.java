/*
 * The MIT License
 *
 * Copyright (c) 2013, Patrick McKeown
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

import hudson.model.Hudson;
import hudson.slaves.EnvironmentVariablesNodeProperty;
import hudson.slaves.NodeProperty;

import java.util.HashMap;
import java.util.Map;

public class GlobalNodeProperties {

    public static Map<String, String> getProperties() {
        Map<String, String> globalNodeProperties = new HashMap<String, String>();
        // Get latest global properties by looking for all 
        // instances of EnvironmentVariablesNodeProperty in the global node properties
        for (NodeProperty<?> nodeProperty : Hudson.getInstance()
                .getGlobalNodeProperties()) {
            if (nodeProperty instanceof EnvironmentVariablesNodeProperty) {
                globalNodeProperties.putAll(((EnvironmentVariablesNodeProperty) nodeProperty).getEnvVars());
            }
        }
        return globalNodeProperties;
    }

    public static String getValue(String key) {
        if (key == null) {
            return null;
        } else {
            return getProperties().get(key);
        }
    }
}
