package dk.frankbille.teachus.frontend.pages;

import java.util.List;

import wicket.ResourceReference;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.model.Model;
import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.frontend.TeachUsApplication;
import dk.frankbille.teachus.frontend.TeachUsSession;
import dk.frankbille.teachus.frontend.UserLevel;
import dk.frankbille.teachus.frontend.components.LinkPropertyColumn;
import dk.frankbille.teachus.frontend.components.ListPanel;
import dk.frankbille.teachus.frontend.components.RendererPropertyColumn;
import dk.frankbille.teachus.frontend.pages.persons.PupilPage;
import dk.frankbille.teachus.frontend.utils.CurrencyChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.DateChoiceRenderer;
import dk.frankbille.teachus.frontend.utils.Resources;
import dk.frankbille.teachus.frontend.utils.TimeChoiceRenderer;

public class AgendaPage extends AuthenticatedBasePage {
	private static final long serialVersionUID = 1L;

	public AgendaPage() {
		super(UserLevel.TEACHER, true);
		
		Teacher teacher = (Teacher) TeachUsSession.get().getPerson();
		
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		List<PupilBooking> bookings = bookingDAO.getFutureBookingsForTeacher(teacher);
		
		IColumn[] columns = new IColumn[] {
				new LinkPropertyColumn(new Model(TeachUsSession.get().getString("General.pupil")), "pupil.name") {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onClick(Object rowModelObject) {
						PupilBooking booking = (PupilBooking) rowModelObject;
						getRequestCycle().setResponsePage(new PupilPage(booking.getPupil()));
					}
				},
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.date")), "date", new DateChoiceRenderer()),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.time")), "date", new TimeChoiceRenderer()),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.phoneNumber")), "pupil.phoneNumber"),
				new RendererPropertyColumn(new Model(TeachUsSession.get().getString("General.price")), "period.price", new CurrencyChoiceRenderer())
		};
		
		add(new ListPanel("list", columns, bookings));
	}

	@Override
	protected ResourceReference getPageIcon() {
		return Resources.AGENDA;
	}

	@Override
	protected String getPageLabel() {
		return TeachUsSession.get().getString("General.agenda"); //$NON-NLS-1$
	}

}
