package dk.teachus.frontend.pages.settings;

import dk.teachus.frontend.WicketSpringTestCase;
import dk.teachus.frontend.pages.settings.TeacherSettingsPage;

public class TestTeacherSettingsPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		tester.startPage(TeacherSettingsPage.class);
		
		tester.assertRenderedPage(TeacherSettingsPage.class);
	}
	
}