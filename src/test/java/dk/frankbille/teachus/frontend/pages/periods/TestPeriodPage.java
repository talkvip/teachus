package dk.frankbille.teachus.frontend.pages.periods;

import wicket.Page;
import wicket.util.tester.ITestPageSource;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.frontend.WicketSpringTestCase;

public class TestPeriodPage extends WicketSpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testRender() {
		final Period period = tester.getTeachUsApplication().getPeriodDAO().get(1L);
		
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new PeriodPage(period);
			}
		});
		
		tester.assertRenderedPage(PeriodPage.class);
		
		tester.assertContains(period.getName());
	}
	
}
