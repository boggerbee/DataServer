package no.kreutzer.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TankDAO {
	static final Logger log = LoggerFactory.getLogger(TankDAO.class);
    Date ts;
	@NotNull
    String id;
	float level;
	float flow;
	String state;
    String valveState;
    String pumpState;
    String switchState;
    
    public String getSwitchState() {
		return switchState;
	}
	public void setSwitchState(String ss) {
		this.switchState = ss;
	}
	public Date getDate() {
		return ts;
	}
	public void setDate(Date dt) {
		this.ts = dt;
	}
	public float getFlow() {
		return flow;
	}
	public void setFlow(float flow) {
		this.flow = flow;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValveState() {
		return valveState;
	}
	public void setValveState(String valveState) {
		this.valveState = valveState;
	}
	public String getPumpState() {
		return pumpState;
	}
	public void setPumpState(String pumpState) {
		this.pumpState = pumpState;
	}
	public float getLevel() { 
    	return level; 
    }
    public void setLevel(float l) {
    	level=l; 
    }
    public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "["+id+"]"+" Level:"+String.valueOf(level)+" Flow:"+String.valueOf(flow)+" State:"+state+" Valve:"+valveState+" Pump:"+pumpState;
	}
	

}
