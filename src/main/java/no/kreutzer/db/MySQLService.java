package no.kreutzer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.kreutzer.domain.GraphDAO;
import no.kreutzer.domain.TankDAO;
import restx.config.ConfigLoader;
import restx.config.ConfigSupplier;
import restx.factory.Component;
import restx.factory.Module;
import restx.factory.Provides;

@Component
public class MySQLService {
	static final Logger log = LoggerFactory.getLogger(MySQLService.class);

    static String URL = "jdbc:mysql://localhost:3306/watercontrol?useSSL=false";
    private String usr;
    private String pwd;
    
    public MySQLService(@Named("db.usr") String dbUsr, @Named("db.pwd") String dbPwd) {
    	log.info("Usr="+dbUsr);
    	usr = dbUsr;
    	pwd = dbPwd;
    }

	public void Store(TankDAO dao) {
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
	
	public ArrayList<GraphDAO> getGraphableLevel() {
        Connection con = null;
        PreparedStatement pst = null;
        try {
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("SELECT DATE_FORMAT(ts,'%Y-%m-%d %H:%i:00') as ds, AVG(level) as avg_level "+
            						   "FROM TankEvent WHERE ts > DATE_SUB(NOW(),INTERVAL 24 HOUR) "+
            						   "GROUP BY ds ORDER BY ds DESC");
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
	
	public ArrayList<GraphDAO> getGraphableFlow() {
        Connection con = null;
        PreparedStatement pst = null;
        try {
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            con = DriverManager.getConnection(URL, usr, pwd);

            pst = con.prepareStatement("SELECT DATE_FORMAT(ts,'%Y-%m-%d %H:%i:00') as ds, AVG(flow) as avg_flow "+
					   "FROM TankEvent WHERE ts > DATE_SUB(NOW(),INTERVAL 24 HOUR) "+
					   "GROUP BY ds ORDER BY ds DESC");
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
            dao.setTimestamp(rs.getTimestamp("ts"));
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

}
