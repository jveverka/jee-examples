package ite.examples.template.services.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

@Stateless
public class DBService {
	
	private static final Logger logger = Logger.getLogger(DBService.class.getName());

    @Resource(mappedName="java:/datasources/PostgresDS")
    private DataSource ds;

    public List<UserDTO> getUsersAction() {
        Connection conn = null;
        List<UserDTO> result = new ArrayList<>();
        try {
            conn = ds.getConnection();
            String selectSQL = "SELECT id, name FROM jpa06_user";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(selectSQL);
            while (rs.next()) {
            	Long id = rs.getLong("id");
            	String name = rs.getString("name");	
            	logger.info("found: ID=" + id + " NAME=" + name);
            	result.add(new UserDTO(id, name));
            }            
        } catch (SQLException e) {
        	logger.severe("SQLException: " + e.getMessage());
		} finally {
            if (conn != null) {
            	try {
					conn.close();
				} catch (SQLException e) {
					logger.severe("SQLException: failed to close connection: " + e.getMessage());
				}
            }
        }
        return result;
    }
}
