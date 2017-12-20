package no.kreutzer.domain;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.kreutzer.utils.JsonDateSerializer;

public class GraphDAO {
	private Date date;
	private float value;
	
    @JsonSerialize(using = JsonDateSerializer.class)
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
