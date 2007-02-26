package dk.frankbille.teachus.frontend.pages.persons;

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.frankbille.teachus.domain.Admin;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.frontend.WicketSpringTestCase;

public class TestPersonPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testPupilPage() {
		final Pupil pupil = (Pupil) tester.getPerson(11l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new PupilPage(pupil);
			}
			
		});
		
		tester.assertRenderedPage(PupilPage.class);
		
		tester.assertContains(pupil.getName());
	}
	
	public void testTeacherPage() {
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		final Teacher teacher = (Teacher) tester.getPerson(2l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new TeacherPage(teacher);
			}
			
		});
		
		tester.assertRenderedPage(TeacherPage.class);
		
		tester.assertContains(teacher.getName());
	}
	
	public void testAdminPage() {
		// Log in as admin
		TesterTeachUsSession.get().setPerson(1l);
		
		final Admin admin = (Admin) tester.getPerson(1l);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new AdminPage(admin);
			}
			
		});
		
		tester.assertRenderedPage(AdminPage.class);
		
		tester.assertContains(admin.getName());
	}
}
