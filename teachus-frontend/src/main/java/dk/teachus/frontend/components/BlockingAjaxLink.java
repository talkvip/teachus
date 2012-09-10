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
package dk.teachus.frontend.components;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.model.IModel;

public abstract class BlockingAjaxLink<T> extends AjaxFallbackLink<T> {
	private static final long serialVersionUID = 1L;

	public BlockingAjaxLink(String id) {
		super(id);		
	}
	
	public BlockingAjaxLink(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new AjaxCallDecorator() {
			private static final long serialVersionUID = 1L;

			@Override
			public CharSequence decorateScript(Component c, CharSequence script) {
				StringBuilder b = new StringBuilder();
				
				b.append("if (this.blockLink == null) {");
				b.append("this.blockLink = true;");
				b.append(script);
				b.append("} else { return false; }");
				
				return b;
			}
		};
	}
}
