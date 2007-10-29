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
package dk.teachus.frontend.components.form;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;

public class SelectPupilsPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	private CheckGroup selectGroup;
	
	public SelectPupilsPanel(String id, IModel model) {
		super(id, model);

		selectGroup = new CheckGroup("selectGroup", getModel());
		selectGroup.setRenderBodyOnly(false);
		add(selectGroup);
		
		// Header
		selectGroup.add(new CheckGroupSelector("selectAll"));
		selectGroup.add(new Label("name", TeachUsSession.get().getString("General.pupil")));
		
		// Check list model
		IModel pupilsModel = new LoadableDetachableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
				List<Pupil> pupils = loadPupils();
				return pupils;
			}
		};
		
		selectGroup.add(new ListView("pupils", pupilsModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem item) {
				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject() {
						return (item.getIndex()+1) % 2 == 0 ? "even" : "odd";
					}					
				}));
				
				Check check = new Check("select", item.getModel());
				item.add(check);
				FormComponentLabel label = new FormComponentLabel("label", check);
				item.add(label);
				label.add(new Label("name", new PropertyModel(item.getModel(), "name")).setRenderBodyOnly(true));
			}
		});
	}
	
	public CheckGroup getInputComponent() {
		return selectGroup;
	}

	private List<Pupil> loadPupils() {
		List<Pupil> pupils;
		Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
		
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		pupils = personDAO.getPupils(teacher);
		return pupils;
	}
	
}
