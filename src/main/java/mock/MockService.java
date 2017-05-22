package mock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import no.kreutzer.domain.TankDAO;

public class MockService {
	/*
	 * Create a set of events for test purposes
	 */
	public Iterable<TankDAO> getEvents() {
		ArrayList<TankDAO> list = new ArrayList<TankDAO>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -2); // two days back
		
		for (int i=0;i<1000;i++) {
			TankDAO dao = new TankDAO();
			dao.setDate(new Date(cal.getTimeInMillis()));
			dao.setLevel(getLevel(cal));
			list.add(dao);
			cal.add(Calendar.MINUTE, 1);
		}
		return list;
	}
	private int getLevel(Calendar cal) {
		float level = 70+cal.get(Calendar.HOUR_OF_DAY);//+cal.get(Calendar.MINUTE)/60;
		return (int)level;
	}
	
}
