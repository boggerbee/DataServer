package no.kreutzer.rest;

import no.kreutzer.db.MySQLService;
import no.kreutzer.domain.GraphDAO;
import no.kreutzer.domain.MarkerDAO;
import no.kreutzer.domain.TankDAO;
import no.kreutzer.telldus.CommandStatus;
import no.kreutzer.telldus.TelldusService;
import restx.annotations.GET;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component @RestxResource
public class TankResource {
	static final Logger log = LoggerFactory.getLogger(TankResource.class);
    private static final int FULL_DEVICE_ID = 2053144;
	private MySQLService mysql;
	private TelldusService telldus;
	
	public TankResource(@Named("MySQLService") MySQLService m, @Named("TelldusService") TelldusService t) {
		mysql = m;
		telldus = t;
	}

    @GET("/current")
    @PermitAll
    public TankDAO getCurrent() {
        return mysql.getCurrent();
    }

    @POST("/tank")
    @PermitAll
    public TankDAO postTank(TankDAO dao){
    	//log.info(dao.toString());
    	
    	mysql.storeTank(dao);
        return dao;
    }
    @POST("/controller")
    @PermitAll
    public ControllerDAO postController(ControllerDAO dao){
    	//log.info(dao.toString());
    	
    	mysql.storeController(dao);
    	if (dao.getKey().equals("tank")) {
	        try {
	            if (dao.getValue().equals("FULL")) {
	                log.info("Updated telldus device to FULL (ON): "+telldus.setDeviceStatus(FULL_DEVICE_ID, true).status);
	            } else {
                    log.info("Updated telldus device to NOT FULL (OFF): "+telldus.setDeviceStatus(FULL_DEVICE_ID, false).status);
	            }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
	}
        return dao;
    }
    @GET("/events")
    @PermitAll
    public Iterable<TankDAO> getEvents() {
        return mysql.getEvents();
    	//return (new MockService()).getEvents();
    }
    @GET("/graphlevel")
    @PermitAll
    public Iterable<GraphDAO> getGraphLevel(String id, int hrs) {
        return mysql.getGraphableLevel(id,hrs);
    }
    @GET("/graphflow")
    @PermitAll
    public Iterable<GraphDAO> getGraphFlow(String id, int hrs) {
        return mysql.getGraphableFlow(id,hrs);
    }
    @GET("/graphmarkers")
    @PermitAll
    public Iterable<MarkerDAO> getMarkers(String id, int hrs) {
        return mysql.getFillMarkers(id,hrs);
    }
    @GET("/level")
    @PermitAll
    public Iterable<GraphDAO> getLevel(String id, Timestamp s, Timestamp e) {
        return mysql.getLevel(id,s,e);
    }
    @GET("/flow")
    @PermitAll
    public Iterable<GraphDAO> getFlow(String id, Timestamp s, Timestamp e) {
        return mysql.getFlow(id,s,e);
    }
    @GET("/markers")
    @PermitAll
    public Iterable<MarkerDAO> getMarkers(String id, Timestamp s, Timestamp e) {
        return mysql.getMarkers(id,s,e); 
    }
}
