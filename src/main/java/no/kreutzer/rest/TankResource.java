package no.kreutzer.rest;

import no.kreutzer.db.MySQLService;
import no.kreutzer.domain.GraphDAO;
import no.kreutzer.domain.MarkerDAO;
import no.kreutzer.domain.TankDAO;
import restx.annotations.GET;
import restx.annotations.POST;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component @RestxResource
public class TankResource {
	static final Logger log = LoggerFactory.getLogger(TankResource.class);
	private MySQLService mysql;
	
	public TankResource(@Named("MySQLService") MySQLService m) {
		mysql = m;
	}

    @GET("/current")
    @PermitAll
    public TankDAO getCurrent() {
        return mysql.getCurrent();
    }

    @POST("/tank")
    @PermitAll
    public TankDAO postTank(TankDAO dao){
    	log.info(dao.toString());
    	
    	mysql.storeTank(dao);
        return dao;
    }
    @POST("/controller")
    @PermitAll
    public ControllerDAO postController(ControllerDAO dao){
    	log.info(dao.toString());
    	
    	mysql.storeController(dao);
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
    public Iterable<GraphDAO> getGraphLevel(int hrs) {
        return mysql.getGraphableLevel(hrs);
    }
    @GET("/graphflow")
    @PermitAll
    public Iterable<GraphDAO> getGraphFlow(int hrs) {
        return mysql.getGraphableFlow(hrs);
    }
    @GET("/graphmarkers")
    @PermitAll
    public Iterable<MarkerDAO> getMarkers() {
        return mysql.getFillMarkers();
    }
}
