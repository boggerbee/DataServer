package no.kreutzer.domain;

import java.util.Date;

public class GraphDAO {
	private Date date;
	private float value;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
}
