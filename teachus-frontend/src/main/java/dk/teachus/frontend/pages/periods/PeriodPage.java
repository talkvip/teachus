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
package dk.teachus.frontend.pages.periods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.joda.time.DateTime;

import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.impl.PeriodImpl.WeekDay;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.form.ButtonPanelElement;
import dk.teachus.frontend.components.form.CheckGroupElement;
import dk.teachus.frontend.components.form.DateElement;
import dk.teachus.frontend.components.form.DecimalFieldElement;
import dk.teachus.frontend.components.form.DropDownElement;
import dk.teachus.frontend.components.form.FormPanel;
import dk.teachus.frontend.components.form.IntegerFieldElement;
import dk.teachus.frontend.components.form.TextFieldElement;
import dk.teachus.frontend.pages.AuthenticatedBasePage;
import dk.teachus.frontend.utils.TimeChoiceRenderer;
import dk.teachus.frontend.utils.WeekDayChoiceRenderer;
import dk.teachus.frontend.utils.WeekDayChoiceRenderer.Format;

public class PeriodPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	private static class TimeModel extends Model {
		private static final long serialVersionUID = 1L;
		
		private IModel nestedModel;

		public TimeModel(IModel nestedModel) {
			this.nestedModel = nestedModel;
		}

		public Object getObject() {
			Object returnObject = null;
			
			Object object = nestedModel.getObject();
			if (object != null) {
				Date date = (Date) object;
				returnObject = new DateTime(date).getMinuteOfDay();
			}
			
			return returnObject;
		}

		public void setObject(Object object) {
			if (object != null) {
				Integer minutesOfDay = (Integer) object;
				nestedModel.setObject(new DateTime().withTime(0, 0, 0, 0).plusMinutes(minutesOfDay).toDate());
			}
		}
		
	}
	
	public PeriodPage(final Period period) {
		super(UserLevel.TEACHER, true);
		
		FormPanel form = new FormPanel("form"); //$NON-NLS-1$
		add(form);
		
		// Name
		TextFieldElement nameElement = new TextFieldElement(TeachUsSession.get().getString("General.name"), new PropertyModel(period, "name"), true);
		nameElement.add(StringValidator.maximumLength(100));
		form.addElement(nameElement); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Begin date
		form.addElement(new DateElement(TeachUsSession.get().getString("General.startDate"), new PropertyModel(period, "beginDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		// End date
		form.addElement(new DateElement(TeachUsSession.get().getString("General.endDate"), new PropertyModel(period, "endDate"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Time elements
		List<Integer> hours = new ArrayList<Integer>();
		DateTime dt = new DateTime().withTime(0, 0, 0, 0);
		int day = dt.getDayOfMonth();
		while (day == dt.getDayOfMonth()) {
			hours.add(dt.getMinuteOfDay());
			dt = dt.plusMinutes(30);
		}
		
		TimeChoiceRenderer timeChoiceRenderer = new TimeChoiceRenderer();
		
		// Start time
		form.addElement(new DropDownElement(TeachUsSession.get().getString("General.startTime"), new TimeModel(new PropertyModel(period, "startTime")), hours, timeChoiceRenderer, true)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// End time
		form.addElement(new DropDownElement(TeachUsSession.get().getString("General.endTime"), new TimeModel(new PropertyModel(period, "endTime")), hours, timeChoiceRenderer, true)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Location
		TextFieldElement locationElement = new TextFieldElement(TeachUsSession.get().getString("General.location"), new PropertyModel(period, "location"));
		locationElement.add(StringValidator.maximumLength(100));
		form.addElement(locationElement); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Price
		form.addElement(new DecimalFieldElement(TeachUsSession.get().getString("General.price"), new PropertyModel(period, "price"), 6)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Lesson duration
		form.addElement(new IntegerFieldElement(TeachUsSession.get().getString("General.lessonDuration"), new PropertyModel(period, "lessonDuration"), true, 4)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Interval Between Lesson Start
		form.addElement(new IntegerFieldElement(TeachUsSession.get().getString("General.intervalBetweenLessonStart"), new PropertyModel(period, "intervalBetweenLessonStart"), true, 4)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Week days
		form.addElement(new CheckGroupElement(TeachUsSession.get().getString("General.weekDays"), new PropertyModel(period, "weekDays"), Arrays.asList(WeekDay.values()), new WeekDayChoiceRenderer(Format.LONG), true)); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Repeat every week
		form.addElement(new IntegerFieldElement(TeachUsSession.get().getString("General.repeatEveryWeek"), new PropertyModel(period, "repeatEveryWeek"))); //$NON-NLS-1$ //$NON-NLS-2$
		
		// Buttons
		form.addElement(new ButtonPanelElement() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCancel(AjaxRequestTarget target) {
				getRequestCycle().setResponsePage(PeriodsPage.class);
			}

			@Override
			protected void onSave(AjaxRequestTarget target) {
				PeriodDAO periodDAO = TeachUsApplication.get().getPeriodDAO();

				periodDAO.save(period);				
				
				getRequestCycle().setResponsePage(PeriodsPage.class);
			}			
		});
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.periods"); //$NON-NLS-1$
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.PERIODS;
	}
	
}