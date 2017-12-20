package no.kreutzer.domain;

import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.kreutzer.utils.JsonDateSerializer;

public class MarkerDAO {
	private Date date;
	private String label;

    @JsonSerialize(using = JsonDateSerializer.class)
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
