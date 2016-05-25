package cz.sd.api.alvao.database.dao.impl;

import cz.sd.api.alvao.database.AlvaoDbConfigItem;
import cz.sd.api.alvao.database.dao.AlvaoAbstractDao;
import cz.sd.api.alvao.database.dao.IAlvaoDbConfigItemDao;
import cz.sd.api.ticket.CiUpdateData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementace DAO pro praci s konfiguracnimi polozkami/assety v Alvao
 * databazi.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoDbConfigItemDao extends AlvaoAbstractDao implements IAlvaoDbConfigItemDao {

    public AlvaoDbConfigItemDao(String dbString) {
        super(dbString);
    }

    @Override
    public AlvaoDbConfigItem getCI(long id) {
        Connection conn = null;
        ResultSet rs = null;

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM vNodePropertyMix WHERE intNodeId = " + id;

            rs = stmt.executeQuery(sql);
            AlvaoDbConfigItem ci = new AlvaoDbConfigItem();
            if (rs.next()) {

                ci.setClassId(rs.getLong("lintClassId"));
                ci.setName(rs.getString("txtName"));
                ci.setNodeId(rs.getInt("intNodeId"));
                ci.setParentId(rs.getLong("lintParentId"));
                ci.setPath(rs.getString("txtPath"));
                ci.setState(rs.getInt("intState"));
            } else {
                return null; // zadna polozka
            }

            sql = "SELECT * FROM vPropertyKind WHERE lintNodeId = " + id;
            rs = stmt.executeQuery(sql);

            Map<String, String> properties = new HashMap<>();
            while (rs.next()) {
                String kind = rs.getString("txtKind");
                String value = rs.getString("txtValue");
                properties.put(kind, value);
            }
            ci.setProperties(properties);

            return ci;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlvaoDbTicketDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                // no op
            }
        }
    }

    @Override
    public boolean linkToTicket(long ciId, long ticketid) {
        Connection conn = null;
        ResultSet rs = null;

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());
            conn.setAutoCommit(false);

            PreparedStatement stmt;
            String sql = "UPDATE dbo.tHdTicket SET liHdTicketNodeId = " + ciId + " "
                    + "WHERE iHdTicketId = " + ticketid;

            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();

            conn.commit();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlvaoDbTicketDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                // no op
            }
        }
    }

    @Override
    public boolean updateCI(CiUpdateData updateData) {
        Connection conn = null;
        ResultSet rs = null;

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());
            conn.setAutoCommit(false);

            PreparedStatement stmt;
            // nastaveni detailnich vlastnosti
            Map<String, Object> map = updateData.getDetails();
            String set = "";
            String values = "";
            for (Map.Entry<String, Object> entrySet : map.entrySet()) {
                String key = entrySet.getKey();
                Object value = entrySet.getValue();

                String sql = "UPDATE tblProperty "
                        + "SET txtValue = N'" + value.toString() + "' "
                        + "WHERE intPropertyId = ("
                        + "SELECT intPropertyId FROM vPropertyKind WHERE lintNodeId = " + updateData.getCiId() + " AND txtKind = N'" + key + "'"
                        + ")";
                stmt = conn.prepareStatement(sql);
                stmt.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlvaoDbTicketDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                // no op
            }
        }
    }

}
