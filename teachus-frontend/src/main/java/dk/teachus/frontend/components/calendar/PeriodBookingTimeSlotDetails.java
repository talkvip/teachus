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
package dk.teachus.frontend.components.calendar;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.frontend.TeachUsSession;

public class PeriodBookingTimeSlotDetails extends Panel {
	private static final long serialVersionUID = 1L;

	public PeriodBookingTimeSlotDetails(String id, PupilBooking pupilBooking) {
		super(id);
		
		add(new Label("name", new PropertyModel<String>(pupilBooking, "pupil.name"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("telephoneLabel", TeachUsSession.get().getString("General.phoneNumber"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("telephone", new PropertyModel<String>(pupilBooking, "pupil.phoneNumber"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("emailLabel", TeachUsSession.get().getString("General.email"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new Label("email", new PropertyModel<String>(pupilBooking, "pupil.email"))); //$NON-NLS-1$ //$NON-NLS-2$
		add(new MultiLineLabel("notes", new PropertyModel<String>(pupilBooking, "pupil.notes"))); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}