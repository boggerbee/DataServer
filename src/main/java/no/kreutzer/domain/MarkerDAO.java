package no.kreutzer.domain;

import java.util.Date;

public class MarkerDAO {
	private Date date;
	private String label;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String value) {
		this.label = value;
	}
}
