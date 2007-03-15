package dk.teachus.frontend.pages.persons;

import java.util.ArrayList;
import java.util.List;

import dk.teachus.dao.PersonDAO;
import dk.teachus.domain.Admin;
import dk.teachus.domain.Person;
import dk.teachus.domain.Pupil;
import dk.teachus.domain.Teacher;
import dk.teachus.domain.impl.PupilImpl;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.UserLevel;
import dk.teachus.frontend.components.FunctionsColumn.FunctionItem;
import dk.teachus.frontend.pages.calendar.PupilCalendarPage;

public class PupilsPage extends PersonsPage<Pupil> {
	private static final long serialVersionUID = 1L;
	
	public PupilsPage() {
		super(UserLevel.TEACHER);
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.pupils"); //$NON-NLS-1$
	}

	@Override
	protected List<Pupil> getPersons() {
		List<Pupil> persons = null;
		
		PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
		Person person = TeachUsSession.get().getPerson();
		if (person instanceof Teacher) {
			persons = personDAO.getPupils((Teacher) person);
		} else if (person instanceof Admin) {
			persons = personDAO.getPersons(Pupil.class);
		}
			
		return persons;
	}

	@Override
	protected Pupil getNewPerson() {
		Person person = TeachUsSession.get().getPerson();
		Teacher teacher = (Teacher) person;
		Pupil pupil = new PupilImpl();
		pupil.setTeacher(teacher);
		return pupil;
	}

	@Override
	protected String getNewPersonLabel() {
		return TeachUsSession.get().getString("PupilsPage.newPupil"); //$NON-NLS-1$
	}

	@Override
	protected boolean showNewPersonLink() {
		boolean showNewPersonLink = false;
		
		Person person = TeachUsSession.get().getPerson();
		if (person instanceof Teacher) {
			showNewPersonLink = true;
		}
		
		return showNewPersonLink;
	}
	
	@Override
	protected PersonPage getPersonPage(Pupil person) {
		return new PupilPage(person);
	}

	@Override
	protected List<FunctionItem> getFunctions() {
		List<FunctionItem> functions = new ArrayList<FunctionItem>();

		functions.add(new PersonFunctionItem(TeachUsSession.get().getString("General.calendar")) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(Pupil pupil) {
				getRequestCycle().setResponsePage(new PupilCalendarPage(pupil));
			}
		});
		
		functions.add(new PersonFunctionItem(TeachUsSession.get().getString("General.delete")) { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(Pupil person) {
				PersonDAO personDAO = TeachUsApplication.get().getPersonDAO();
				personDAO.setInactive(person.getId());
				
				getRequestCycle().setResponsePage(PupilsPage.class);
			}
			
			@Override
			public String getClickConfirmText(Pupil person) {
				String deleteText = TeachUsSession.get().getString("PupilPage.deleteConfirm"); //$NON-NLS-1$
				deleteText = deleteText.replace("{personname}", person.getName()); //$NON-NLS-1$
				return deleteText;
			}
		});

		return functions;
	}

	@Override
	protected AuthenticatedPageCategory getPageCategory() {
		return AuthenticatedPageCategory.PUPILS;
	}
	
}
