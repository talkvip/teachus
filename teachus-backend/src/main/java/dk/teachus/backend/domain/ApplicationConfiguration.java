/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.backend.domain;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

public interface ApplicationConfiguration extends Serializable {

	public static final String GOOGLE_ANALYTICS_WEB_PROPERTY_ID = "GOOGLE_ANALYTICS_WEB_PROPERTY_ID";
	public static final String SERVER_URL = "SERVER_URL";
	public static final String VERSION = "VERSION";
	public static final String DEFAULT_TIMEZONE = "DEFAULT_TIMEZONE";
	
	void setConfiguration(String configurationKey, String configurationValue);
	
	void setConfigurationInteger(String configurationKey, int configurationValue);
	
	String getConfiguration(String configurationKey);
	
	int getConfigurationInteger(String configurationKey);
	
	void addPropertyListener(PropertyChangeListener listener);
	
	void removePropertyListener(PropertyChangeListener listener);
	
}
