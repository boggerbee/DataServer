package no.kreutzer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.TimeZone;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.kreutzer.domain.GraphDAO;
import no.kreutzer.domain.MarkerDAO;
import no.kreutzer.domain.TankDAO;
import no.kreutzer.rest.ControllerDAO;
import restx.factory.Component;

@Component
public class MySQLService {
	static final Logger log = LoggerFactory.getLogger(MySQLService.class);

    static String URL = "jdbc:mysql://localhost:3306/watercontrol?useSSL=false";
    private String usr;
    private String pwd;
    private Calendar cal = Calendar.getInstance();
    private int offsetFromUTC = 7200000; //BUG some problem with timezones..
    
    public MySQLService(@Named("db.usr") String dbUsr, @Named("db.pwd") String dbPwd) {
    	log.info("Usr="+dbUsr);
    	usr = dbUsr;
    	pwd = dbPwd;
    }

	public void storeTank(TankDAO dao) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("INSERT INTO TankEvent(id,level,flow,state,pumpState,valveState,switchState) VALUES(?,?,?,?,?,?,?)");
            pst.setString(1, dao.getId());
            pst.setFloat(2, dao.getLevel());
            pst.setFloat(3, dao.getFlow());
            pst.setString(4, dao.getState());
            pst.setString(5, dao.getPumpState());
            pst.setString(6, dao.getValveState());
            pst.setString(7, dao.getSwitchState());
            pst.executeUpdate();

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }
	public Iterable<TankDAO> getEvents() {
        Connection con = null;
        PreparedStatement pst = null;
        try {
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("SELECT ts,id,level,flow,state,pumpState,valveState,switchState FROM TankEvent");
            ResultSet rs = pst.executeQuery();
            ArrayList<TankDAO> list = makeList(rs);            

    		return list;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return null;
	}
	
	public TankDAO getCurrent() {
        Connection con = null;
        PreparedStatement pst = null;
        try {
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("SELECT ts,id,level,flow,state,pumpState,valveState,switchState FROM TankEvent "+
            						   "ORDER BY ts DESC LIMIT 5");
            ResultSet rs = pst.executeQuery();
            ArrayList<TankDAO> list = makeList(rs);            
    		return list.get(0);
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return null;
	}	
	
	public ArrayList<GraphDAO> getGraphableLevel(String id, int hrs) {
        Connection con = null;
        PreparedStatement pst = null;
        if (hrs == 0) hrs = 24;
        if (id == null) id = "Almedalen25"; 
        try {
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("SELECT DATE_FORMAT(MIN(ts),'%Y-%m-%d %H:%i:00') AS ds,AVG(level) AS avg_level "+
				"FROM TankEvent WHERE ts > DATE_SUB(NOW(),INTERVAL "+hrs+" HOUR) "+
            	"AND id = '"+id+"' GROUP BY ROUND(UNIX_TIMESTAMP(ts) / 600)"); // 600 is every 10th minute
            ResultSet rs = pst.executeQuery();
            ArrayList<GraphDAO> list = new ArrayList<GraphDAO>();
            while (rs.next()) {
            	GraphDAO dao = new GraphDAO();
            	float value = (rs.getFloat("avg_level"))/100;
                dao.setDate(rs.getTimestamp("ds"));
                dao.setValue(value);
                list.add(dao);
            }            
    		return list;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return null;		
	}
	
	
	public ArrayList<GraphDAO> getGraphableFlow(String id, int hrs) {
        Connection con = null;
        PreparedStatement pst = null;
        if (hrs == 0) hrs = 24;
        if (id == null) id = "Almedalen25"; 
        try {
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("SELECT DATE_FORMAT(MIN(ts),'%Y-%m-%d %H:%i:00') AS ds,AVG(flow) AS avg_flow "+
				"FROM TankEvent WHERE ts > DATE_SUB(NOW(),INTERVAL "+hrs+" HOUR) "+
				"AND id = '"+id+"' GROUP BY ROUND(UNIX_TIMESTAMP(ts) / 600)"); // 600 is every 10th minute
            
            ResultSet rs = pst.executeQuery();
            ArrayList<GraphDAO> list = new ArrayList<GraphDAO>();
            while (rs.next()) {
            	GraphDAO dao = new GraphDAO();
            	float value = rs.getFloat("avg_flow");
                dao.setDate(rs.getTimestamp("ds"));
                dao.setValue(value);
                list.add(dao);
            }            
    		return list;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return null;		
	}
	
	private ArrayList<TankDAO> makeList(ResultSet rs) throws SQLException {
        ArrayList<TankDAO> list = new ArrayList<TankDAO>();
        
        while (rs.next()) {
            TankDAO dao = new TankDAO();
            cal.setTime(rs.getTimestamp("ts"));
            cal.add(Calendar.MILLISECOND, offsetFromUTC);
            dao.setDate(cal.getTime());
            dao.setId(rs.getString("id"));
            dao.setLevel(rs.getFloat("level"));
            dao.setFlow(rs.getFloat("flow"));
            dao.setState(rs.getString("state"));
            dao.setPumpState(rs.getString("pumpState"));
            dao.setValveState(rs.getString("valveState"));
            dao.setSwitchState(rs.getString("switchState"));
            list.add(dao);
        }
     
        return list;
	}
	
	public void storeController(ControllerDAO dao) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("INSERT INTO ControllerEvent(id,mode,flow,_key,_value) VALUES(?,?,?,?,?)");
            pst.setString(1, dao.getId());
            pst.setString(2, dao.getMode());
            pst.setLong(3, dao.getFlow());
            pst.setString(4, dao.getKey());
            pst.setString(5, dao.getValue());
            pst.executeUpdate();

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        }	}

	public Iterable<MarkerDAO> getFillMarkers(String id, int hrs) {
        Connection con = null;
        PreparedStatement pst = null;
        if (hrs == 0) hrs = 24;
        if (id == null) id = "Almedalen25";         
        try {
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("select ts,_value from ControllerEvent where _key='tank' "+
            "and ts>DATE_SUB(NOW(),INTERVAL "+hrs+" HOUR) and id = '"+id+"'");
            ResultSet rs = pst.executeQuery();
            ArrayList<MarkerDAO> list = new ArrayList<MarkerDAO>();
            while (rs.next()) {
            	MarkerDAO dao = new MarkerDAO();
                dao.setDate(rs.getTimestamp("ds"));
                dao.setLabel(rs.getString("_value"));
                list.add(dao);
            }            
    		return list;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return null;	
	}
	
    public ArrayList<GraphDAO> getLevel(String id, Timestamp start,Timestamp end) {
        Connection con = null;
        PreparedStatement pst = null;
        if (id == null) id = "Almedalen25"; 
        log.info("getLevel start time: "+start+" end time: "+end);
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("SELECT DATE_FORMAT(MIN(ts),'%Y-%m-%d %H:%i:00') AS ds,AVG(level) AS avg_level "+
                "FROM TankEvent WHERE ts > ? AND ts < ? "+
                "AND id = ? GROUP BY ROUND(UNIX_TIMESTAMP(ts) / 100)"); // 600 is every 10th minute, 100 is every minute
            pst.setTimestamp(1, start);
            pst.setTimestamp(2, end);
            pst.setString(3, id);
            
            ResultSet rs = pst.executeQuery();
            ArrayList<GraphDAO> list = new ArrayList<GraphDAO>();
            while (rs.next()) {
                GraphDAO dao = new GraphDAO();
                float value = (rs.getFloat("avg_level"))/100;
                dao.setDate(rs.getTimestamp("ds"));
                dao.setValue(value);
                list.add(dao);
            }    
            if (list.size() != 0) {
                log.info("First event: "+list.get(0).getDate());
            }
            return list;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return null;        
    }
    
    public ArrayList<GraphDAO> getFlow(String id, Timestamp start,Timestamp end) {
        Connection con = null;
        PreparedStatement pst = null;
        if (id == null) id = "Almedalen25"; 
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("SELECT DATE_FORMAT(MIN(ts),'%Y-%m-%d %H:%i:00') AS ds,AVG(flow) AS avg_flow "+
                "FROM TankEvent WHERE ts > ? AND ts < ? "+
                "AND id = ? GROUP BY ROUND(UNIX_TIMESTAMP(ts) / 100)"); // 600 is every 10th minute, 100 is every minute
            pst.setTimestamp(1, start);
            pst.setTimestamp(2, end);
            pst.setString(3, id);
            
            ResultSet rs = pst.executeQuery();
            ArrayList<GraphDAO> list = new ArrayList<GraphDAO>();
            while (rs.next()) {
                GraphDAO dao = new GraphDAO();
                dao.setDate(rs.getTimestamp("ds"));
                dao.setValue(rs.getFloat("avg_flow"));
                list.add(dao);
            }            
            return list;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return null;        
    }   

    public Iterable<MarkerDAO> getMarkers(String id, Timestamp start,Timestamp end) {
        Connection con = null;
        PreparedStatement pst = null;
        if (id == null) id = "Almedalen25";         
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("SELECT ts,_value FROM ControllerEvent WHERE _key='tank' AND ts > ? AND ts < ? AND id = ?");
            pst.setTimestamp(1, start);
            pst.setTimestamp(2, end);
            pst.setString(3, id);
            
            ResultSet rs = pst.executeQuery();
            ArrayList<MarkerDAO> list = new ArrayList<MarkerDAO>();
            while (rs.next()) {
                MarkerDAO dao = new MarkerDAO();
                dao.setDate(rs.getTimestamp("ts"));
                dao.setLabel(rs.getString("_value"));
                list.add(dao);
            }            
            return list;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return null;    
    }
}
