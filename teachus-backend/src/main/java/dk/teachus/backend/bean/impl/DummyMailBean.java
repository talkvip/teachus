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
package dk.teachus.backend.bean.impl;

import java.util.List;

import javax.mail.internet.InternetAddress;

import org.springframework.mail.javamail.JavaMailSender;

import dk.teachus.backend.MailException;
import dk.teachus.backend.bean.MailBean;
import dk.teachus.backend.bean.VelocityBean;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;

/**
 * Mail implementation which doesn't send mails.
 */
public class DummyMailBean implements MailBean {
	private static final long serialVersionUID = 1L;
	
	public DummyMailBean(JavaMailSender mailSender, VelocityBean velocityBean) {
	}

	public void sendNewBookingsMail(Teacher teacher, List<PupilBooking> pupilBookings, ApplicationConfiguration configuration) {
	}
	
	public void sendMail(InternetAddress sender, InternetAddress recipient, String subject, String body) throws MailException {
	}

}
