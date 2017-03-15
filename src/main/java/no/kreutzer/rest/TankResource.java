package no.kreutzer.rest;

import no.kreutzer.db.MySQLService;
import no.kreutzer.domain.GraphDAO;
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
    	
    	mysql.Store(dao);
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
    public Iterable<GraphDAO> getGraphLevel() {
        return mysql.getGraphableLevel();
    }
    @GET("/graphflow")
    @PermitAll
    public Iterable<GraphDAO> getGraphFlow() {
        return mysql.getGraphableFlow();
    }
}
