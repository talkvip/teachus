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

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import dk.teachus.backend.bean.MailBean;
import dk.teachus.backend.bean.VelocityBean;
import dk.teachus.backend.domain.ApplicationConfiguration;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.test.SpringTestCase;

public class TestMailBeanImpl extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testSendWelcomeMail() throws Exception {
		MailBean mailBean = getMailBean();
		
		Pupil pupil = (Pupil) getPersonDAO().getPerson(3L);
		endTransaction();
		
		Method createWelcomeMailMethod = MailBeanImpl.class.getDeclaredMethod("createWelcomeMail", new Class[] {Pupil.class, String.class, ApplicationConfiguration.class});
		createWelcomeMailMethod.setAccessible(true);
		MimeMessagePreparator preparator = (MimeMessagePreparator) createWelcomeMailMethod.invoke(mailBean, new Object[] {pupil, "No intro message", createDummyConfiguration()});
		preparator.prepare(new JavaMailSenderImpl().createMimeMessage());
	}
	
	public void testSendNewBookingsMail() throws Exception {
		MailBean mailBean = getMailBean();
		
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		List<PupilBooking> pupilBookings = getBookingDAO().getUnpaidBookings(teacher);
		endTransaction();
		
		mailBean.sendNewBookingsMail(teacher, pupilBookings, createDummyConfiguration());
	}

	private MailBean getMailBean() throws NoSuchFieldException, IllegalAccessException {
		// Replace mailsender with a dummy
		JavaMailSender mailSender = new JavaMailSender() {
			private JavaMailSender wrappedSender = new JavaMailSenderImpl();

			public MimeMessage createMimeMessage() {
				return wrappedSender.createMimeMessage();
			}

			public MimeMessage createMimeMessage(InputStream arg0) throws MailException {
				return wrappedSender.createMimeMessage(arg0);
			}

			public void send(MimeMessage arg0) throws MailException {				
			}

			public void send(MimeMessage[] arg0) throws MailException {				
			}

			public void send(MimeMessagePreparator arg0) throws MailException {		
				try {
					arg0.prepare(createMimeMessage());
				} catch (Exception e) {
					throw new MailException("", e) {
						private static final long serialVersionUID = 1L;
					};
				}
			}

			public void send(MimeMessagePreparator[] arg0) throws MailException {				
			}

			public void send(SimpleMailMessage arg0) throws MailException {				
			}

			public void send(SimpleMailMessage[] arg0) throws MailException {				
			}			
		};

		VelocityBean velocityBean = (VelocityBean) applicationContext.getBean("velocityBean");
		return new MailBeanImpl(mailSender, velocityBean);
	}
	
}
