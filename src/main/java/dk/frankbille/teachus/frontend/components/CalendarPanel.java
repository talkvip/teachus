package dk.frankbille.teachus.frontend.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormatter;

import wicket.behavior.SimpleAttributeModifier;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.Panel;
import wicket.markup.repeater.RepeatingView;
import dk.frankbille.teachus.domain.DatePeriod;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Periods;
import dk.frankbille.teachus.frontend.utils.Formatters;
import dk.frankbille.teachus.frontend.utils.Icons;

public abstract class CalendarPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	public CalendarPanel(String wicketId, DateMidnight pageDate, Periods periods) {
		super(wicketId);
		
		DateMidnight weekDate = pageDate.withDayOfWeek(DateTimeConstants.MONDAY);
		final DateMidnight firstWeekDate = weekDate;
		List<DatePeriod> dates = new ArrayList<DatePeriod>();
		List<DatePeriod> weekDates = periods.generateDatesForWeek(weekDate.toDate());
		do {
			dates.addAll(weekDates);
			weekDate = weekDate.plusWeeks(1);
			weekDates = periods.generateDatesForWeek(weekDate.toDate());
		} while(dates.size()+weekDates.size() <= 7);
		
		
		// Weeks
		RepeatingView weeks = new RepeatingView("weeks"); //$NON-NLS-1$
		add(weeks);
		
		int w = -1;
		RepeatingView days = null;
		DateTimeFormatter formatWeekOfYear = Formatters.getFormatWeekOfYear();
		Label weekHeader = null;
		int daysInWeek = 0;
		for (DatePeriod datePeriod : dates) {
			Date date = datePeriod.getDate();
			DateMidnight dm = new DateMidnight(date);
			
			if (dm.getWeekOfWeekyear() != w) {
				if (weekHeader != null) {
					weekHeader.add(new SimpleAttributeModifier("colspan", ""+daysInWeek)); //$NON-NLS-1$ //$NON-NLS-2$
				}
				daysInWeek = 0;
				
				WebMarkupContainer week = new WebMarkupContainer(weeks.newChildId());
				weeks.add(week);
				
				weekHeader = new Label("weekHeader", formatWeekOfYear.print(dm)); //$NON-NLS-1$
				week.add(weekHeader);
				
				days = new RepeatingView("days"); //$NON-NLS-1$
				week.add(days);
				
				w = dm.getWeekOfWeekyear();
			}
			
			WebMarkupContainer day = new WebMarkupContainer(days.newChildId());
			days.add(day);
			
			// Periods
			RepeatingView periodsView = new RepeatingView("periods"); //$NON-NLS-1$
			day.add(periodsView);
			
			for (Period period : datePeriod.getPeriods()) {
				WebMarkupContainer periodCell = new WebMarkupContainer(periodsView.newChildId());
				periodsView.add(periodCell);
				
				periodCell.add(createPeriodDateComponent("period", period, date));
			}
			
			daysInWeek++;
		}
		
		if (weekHeader != null) {
			weekHeader.add(new SimpleAttributeModifier("colspan", ""+daysInWeek)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
				
		// Navigation
		Link backLink = createBackLink("backLink", firstWeekDate.minusWeeks(periods.calculateNumberOfWeeks(firstWeekDate.minusDays(1).toDate(), 7)));
		backLink.add(new Image("backIcon", Icons.LEFT)); //$NON-NLS-1$ //$NON-NLS-2$
		add(backLink);
		
		Link forwardLink = createForwardLink("forwardLink", weekDate);
		forwardLink.add(new Image("forwardIcon", Icons.RIGHT)); //$NON-NLS-1$ //$NON-NLS-2$
		add(forwardLink);
	}
	
	protected abstract Link createBackLink(String wicketId, DateMidnight previousWeekDate);
	
	protected abstract Link createForwardLink(String wicketId, DateMidnight nextWeekDate);
	
	protected abstract PeriodDateComponent createPeriodDateComponent(String wicketId, Period period, Date date);

}