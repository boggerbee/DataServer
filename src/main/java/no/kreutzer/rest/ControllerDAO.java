package no.kreutzer.rest;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerDAO {
	static final Logger log = LoggerFactory.getLogger(ControllerDAO.class);
    Date ts;
	@NotNull
    String id;
	String key;
	String mode;
	long flow;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public long getFlow() {
		return flow;
	}
	public void setFlow(long flow) {
		this.flow = flow;
	}
	String value;
	public Date getDate() {
		return ts;
	}
	public void setDate(Date ts) {
		this.ts = ts;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
