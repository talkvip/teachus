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
package dk.teachus.backend.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.test.annotation.AbstractAnnotationAwareTransactionalTests;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.dao.PeriodDAO;
import dk.teachus.backend.dao.PersonDAO;
import dk.teachus.backend.dao.StatisticsDAO;
import dk.teachus.backend.database.StaticDataImport;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherBooking;
import dk.teachus.backend.domain.impl.PupilImpl;
import dk.teachus.backend.domain.impl.TeacherImpl;

public abstract class SpringTestCase extends AbstractAnnotationAwareTransactionalTests implements Serializable {

	public SpringTestCase() {
		setDefaultRollback(false);	
	}
	
	protected Long createPupilBooking(long periodId, long pupilId, DateTime dateTime, Date createDate) {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		PeriodDAO periodDAO = getPeriodDAO();
		
		Period period = periodDAO.get(periodId);
		endTransaction();
		
		Pupil pupil = (Pupil) personDAO.getPerson(pupilId);
		endTransaction();
		
		PupilBooking pupilBooking = bookingDAO.createPupilBookingObject();
		pupilBooking.setPeriod(period);
		pupilBooking.setPupil(pupil);
		pupilBooking.setTeacher(pupil.getTeacher());
		pupilBooking.setPaid(false);
		pupilBooking.setNotificationSent(false);
		pupilBooking.setCreateDate(createDate);
		pupilBooking.setDate(dateTime.toDate());
		
		bookingDAO.book(pupilBooking);
		endTransaction();
		
		return pupilBooking.getId();
	}
	
	protected Long createTeacherBooking(long periodId, long teacherId, DateTime date) {
		BookingDAO bookingDAO = getBookingDAO();
		PersonDAO personDAO = getPersonDAO();
		PeriodDAO periodDAO = getPeriodDAO();
		
		Period period = periodDAO.get(periodId);
		endTransaction();
		
		Teacher teacher = (Teacher) personDAO.getPerson(teacherId);
		endTransaction();
		
		TeacherBooking teacherBooking = bookingDAO.createTeacherBookingObject();
		teacherBooking.setCreateDate(new DateTime().minusHours(3).toDate());
		teacherBooking.setDate(date.toDate());
		teacherBooking.setPeriod(period);
		teacherBooking.setTeacher(teacher);
		
		bookingDAO.book(teacherBooking);
		endTransaction();
		
		return teacherBooking.getId();
	}

	public BookingDAO getBookingDAO() {
		return (BookingDAO) applicationContext.getBean("bookingDao");
	}

	@Override
	protected String[] getConfigLocations() {
		List<String> configLocations = new ArrayList<String>();
		
		configLocations.add("/dk/teachus/backend/applicationContext.xml");
		configLocations.add("/dk/teachus/backend/test/applicationContext-test.xml");
		
		addConfigLocations(configLocations);
		
		return configLocations.toArray(new String[configLocations.size()]);
	}
	
	protected void addConfigLocations(List<String> configLocations) {
	}

	public PeriodDAO getPeriodDAO() {
		return (PeriodDAO) applicationContext.getBean("periodDao");
	}

	public PersonDAO getPersonDAO() {
		return (PersonDAO) applicationContext.getBean("personDao");
	}

	public StatisticsDAO getStatisticsDAO() {
		return (StatisticsDAO) applicationContext.getBean("statisticsDao");
	}

	public SessionFactory getSessionFactory() {
		return (SessionFactory) applicationContext.getBean("sessionFactory");
	}

	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
		new StaticDataImport(sessionFactory.openSession().connection());
	}

	protected Pupil createPupil(Teacher teacher, int pupilNumber) {
		Pupil pupil = new PupilImpl();
		pupil.setName("Test pupil "+pupilNumber);
		pupil.setActive(true);
		pupil.setEmail("pupil"+pupilNumber+"@teachus.dk");
		pupil.setUsername("pupil"+pupilNumber);
		pupil.setTeacher(teacher);
		getPersonDAO().save(pupil);
		endTransaction();
		return pupil;
	}

	protected Teacher createTeacher() {
		Teacher teacher = new TeacherImpl();
		teacher.setName("Test name");
		teacher.setActive(true);
		teacher.setEmail("test@teachus.dk");
		teacher.setUsername("test");
		getPersonDAO().save(teacher);
		endTransaction();
		return teacher;
	}
	
	protected Object loadObject(Class objectClass, Serializable objectId) {
		Object object = null;
		
		org.hibernate.Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		object = session.get(objectClass, objectId);
		
		session.getTransaction().commit();
		session.close();
		
		return object;
	}

	protected Teacher inactivateTeacher() {
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		teacher.setActive(false);
		
		getPersonDAO().save(teacher);
		endTransaction();
		return teacher;
	}

	protected Teacher getTeacher() {
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		return teacher;
	}
	
}