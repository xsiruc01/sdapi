package cz.sd.api.alvao.database.dao.impl;

import cz.sd.api.ServiceDeskMessage;
import cz.sd.api.alvao.database.dao.AlvaoAbstractDao;
import cz.sd.api.alvao.database.dao.IAlvaoDbTicketDao;
import cz.sd.api.alvao.ticket.AlvaoTicketCreateData;
import cz.sd.api.alvao.convertor.AlvaoPriorityConvertor;
import cz.sd.api.alvao.convertor.AlvaoStatusConvertor;
import cz.sd.api.alvao.database.AlvaoDbTicket;
import cz.sd.api.ticket.TicketType;
import cz.sd.api.ticket.TicketUpdateData;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementace DAO pro praci s tickety v Alvao
 * databazi.
 *
 * @author Pavel Sirucek (xsiruc01)
 */
public class AlvaoDbTicketDao extends AlvaoAbstractDao implements IAlvaoDbTicketDao {

    public AlvaoDbTicketDao(String dbString) {
        super(dbString);
    }
    
    @Override
    public AlvaoDbTicket getTicket(long id) {
        Connection conn = null;
        ResultSet rs = null;
        
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());
            
            // liActKindId = 7 znamena ze se ticket bude v systemu tvarit, jako
            // by byl vytvoreny pomoci formulare
            Statement stmt = conn.createStatement();
            String sql = "SELECT * from dbo.tHdTicket t " +
                         "JOIN dbo.tAct a ON a.liActHdTicketId = t.iHdTicketId " +
                         "JOIN dbo.tHdSection s ON s.iHdSectionId = t.liHdTicketHdSectionId " +
                         "WHERE t.iHdTicketId = " + id + " AND a.liActKindId = 7";

            rs = stmt.executeQuery(sql);            
            AlvaoDbTicket ticket = new AlvaoDbTicket();            
            if (rs.next()) {
                
                ticket.setDetail(rs.getString("mActNotice"));
                ticket.setSubject(rs.getString("sHdTicket"));
                ticket.setNotice(rs.getString("mHdTicketNotice"));
                ticket.setCreatorId(rs.getInt("liHdTicketUserPersonId"));
                ticket.setCreatedDate(rs.getDate("dHdTicket"));
                ticket.setDeadlineDate(rs.getDate("dHdTicketDeadline"));
                //ticket.setCategory();
                ticket.setStateNotice(rs.getString("sHdTicketStateNotice"));
                //ticket.setGroup();
                ticket.setSolver(rs.getInt("liHdTicketSolverPersonId"));
                ticket.setReceivedDate(rs.getDate("dHdTicketReceived"));
                ticket.setResolvedDate(rs.getDate("dHdTicketResolved"));
                ticket.setUserDeadlineDate(rs.getDate("dHdTicketUserDeadline"));
                ticket.setDeviceCode(rs.getString("sHdTicketDeviceCode"));
                ticket.setPriority(rs.getInt("liHdTicketPriorityId"));
                ticket.setTicketUser(rs.getString("sHdTicketUser"));
                //ticket.setUserEmail();
                //ticket.setUserPhone();
                //ticket.setUserContact();
                ticket.setSection(rs.getInt("liHdTicketHdSectionId"));
                //ticket.setUserMobile();
                //ticket.setUserOffice();
                //ticket.setUserDepartment();
                //ticket.setUserWorkPosition();
                //ticket.setUserCompany();
                ticket.setStartingActId(rs.getInt("liHdTicketStartingActId"));
                //ticket.setRoomId();
                //ticket.setBranch();
                ticket.setMessageTag(rs.getString("sHdTicketMessageTag"));
                ticket.setConfigurationItemId(rs.getLong("liHdTicketNodeId"));
                ticket.setRemovedDate(rs.getDate("dHdTicketRemoved"));
                ticket.setSlaId(rs.getInt("liHdTicketSlaId"));
                ticket.setResolvedPersonId(rs.getInt("liHdTicketSlaId"));
                //ticket.setSolverOrder();
                //ticket.setUserCloseLastAdvised();
                ticket.setEstimatedHours(rs.getInt("nHdTicketEstimatedHours"));
                //ticket.setQueueOrder();
                //ticket.setQueueDeadline();
                //ticket.setApprovalItemId();
                //ticket.setWaitingForEmail();
                ticket.setImpact(rs.getInt("Impact"));
                ticket.setUrgency(rs.getInt("Urgency"));
                //ticket.setUserEmail2();
                //ticket.setUserPhone2();
                //ticket.setFeedbackLinkId();
                //ticket.setFeedbackSolveSpeed();
                //ticket.setFeedbackProfessionality();
                //ticket.setFeedbackExpertise();
                //ticket.setFeedbackComment();
                ticket.setClosedDate(rs.getDate("closedDate"));
                ticket.setClosedByPersonId(rs.getInt("ClosedByPersonId"));
                //ticket.setFirstReactionDeadline();
                ticket.setLastActionDate(rs.getDate("LastActionDate"));
                //ticket.setWaitingActId();
                //ticket.setWaitedForUserHours();
                //ticket.setHasReaction();
                ticket.setWorkHours(rs.getInt("WorkHours"));
                //ticket.setTravelHours();
                //ticket.setTravelKm();
                ticket.setNextActionDeadline(rs.getDate("NextActionDeadline"));
                ticket.setTicketId(rs.getInt("iHdTicketId"));
                ticket.setTicketTypeId(rs.getInt("TicketTypeId"));
            }
            
            sql = "SELECT * FROM dbo.tAct " +
                  "WHERE liActHdTicketId = " + id + " AND liActKindId = 8 ORDER BY dRecordCreated";                     
            rs = stmt.executeQuery(sql);
            
            List<ServiceDeskMessage> notes = new ArrayList<>();
            while (rs.next()) {
                String note = rs.getString("mActNotice");
                Date date = rs.getDate("dRecordCreated");
                ServiceDeskMessage newNote = new ServiceDeskMessage(note, date.toString());
                notes.add(newNote);
            }
            // serazeni
            List<ServiceDeskMessage> revertNotes = new ArrayList<>();
            for (int i = notes.size() - 1; i >= 0; i--) {
                revertNotes.add(notes.get(i));
            }            
            ticket.setNotes(revertNotes);

            return ticket;
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
    public Integer getStatusIdByStatusName(String status) {
        Connection conn = null;
        ResultSet rs = null;
        
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());
            
            Statement stmt = conn.createStatement();
            String sql = "SELECT id from dbo.TicketState WHERE TicketTypeId = 2 AND TicketState = N'" + status + "'";                     
            rs = stmt.executeQuery(sql);
            Integer statusId = null;
            if (rs.next()) {                
                statusId = rs.getInt("id");
            }

            return statusId;
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
    public boolean updateTicket(TicketUpdateData data) {
        Connection conn = null;
        ResultSet rs = null;
        
        String convertedStatus;
        if (TicketType.INCIDENT.equals(data.getType())) {
            convertedStatus = AlvaoStatusConvertor.convertToAlvaoIncidentStatus(data.getStatus(), this);
        } else if (TicketType.PROBLEM.equals(data.getType())) {
            convertedStatus = AlvaoStatusConvertor.convertToAlvaoProblemStatus(data.getStatus(), this);
        } else if (TicketType.CHANGE.equals(data.getType())) {
            convertedStatus = AlvaoStatusConvertor.convertToAlvaoChangeStatus(data.getStatus(), this);
        } else
            convertedStatus = "";    
        
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());
            conn.setAutoCommit(false);
            
            PreparedStatement stmt;
            String sql = "UPDATE dbo.tHdTicket SET ";
            if (data.getSubject() != null) {
                sql += "sHdTicket = N'" + data.getSubject() + "'#";
            }
            if (data.getStatus() != null) {
                sql += "sHdTicketStateNotice = N'" + convertedStatus + "'#";
            }
            if (data.getPriority() != null) {
                sql += "liHdTicketPriorityId = " + AlvaoPriorityConvertor.convertToAlvaoPriority(data.getPriority()) + "#";
            }
            sql += "WHERE iHdTicketId = " + data.getTicketId();
            
            
            sql = sql.replaceAll("#WHERE", " WHERE");
            sql = sql.replaceAll("#", ", ");
            
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            
            if (data.getDetail() != null) {
                sql = "UPDATE dbo.tAct SET mActNotice = N'" + data.getDetail() + "' WHERE liActHdTicketId = " + data.getTicketId() + " AND liActKindId = 1";
                stmt = conn.prepareStatement(sql);
                stmt.executeUpdate();
            }
            
            if (data.getSubject()!= null) {
                sql = "UPDATE dbo.tAct SET sAct = N'" + data.getSubject() + "' WHERE liActHdTicketId = " + data.getTicketId() + " AND liActKindId = 1";
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

    @Override
    public boolean addNote(long id, String note) {
        Connection conn = null;
        ResultSet rs = null;
        
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());
            conn.setAutoCommit(false);
            
            PreparedStatement stmt;
            String sql = "INSERT INTO dbo.tAct (liActKindId, sAct, mActNotice, liActHdTicketId, dAct, bNoCharge) "
                    + "VALUES (8, N'Service Desk Api notice', N'" + note + "', " + id + ", GETDATE(), 0)";
            
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
    public Integer getTicketType(long id) {
        Connection conn = null;
        ResultSet rs = null;
        
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());
            
            Statement stmt = conn.createStatement();
            String sql = "SELECT s.TicketTypeId FROM tHdTicket t " +
                         "JOIN tHdSection s ON t.liHdTicketHdSectionId = s.iHdSectionId " +
                         "WHERE t.iHdTicketId = " + id;                     
            rs = stmt.executeQuery(sql);
            Integer typeId = null;
            if (rs.next()) {                
                typeId = rs.getInt("TicketTypeId");
            }

            return typeId;
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
    public List<Long> getRelatedIncidents(long problemId) {
        Connection conn = null;
        ResultSet rs = null;
        
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());
            
            Statement stmt = conn.createStatement();
            String sql = "SELECT EndHdTicketId FROM dbo.TicketRelation " +
                         "WHERE BeginHdTicketId = " + problemId + " AND TicketRelationTypeId = 1";                     
            rs = stmt.executeQuery(sql);
            
            List<Long> ids = new ArrayList<>();
            
            while (rs.next()) {                
                ids.add(rs.getLong("EndHdTicketId"));
            }

            return ids;
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
    public boolean linkIncidentWithProblem(long problemId, long incidentId) {
        Connection conn = null;
        ResultSet rs = null;
        
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());
            conn.setAutoCommit(false);
            
            PreparedStatement stmt;
            String sql = "INSERT INTO dbo.TicketRelation (BeginHdTicketId, EndHdTicketId, TicketRelationTypeId)\n" +
                         "VALUES (" + problemId + ", " + incidentId + ", 1)";
            
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
    public boolean unlinkIncidentWithProblem(long problemId, long incidentId) {
        Connection conn = null;
        ResultSet rs = null;
        
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());
            conn.setAutoCommit(false);
            
            PreparedStatement stmt;
            String sql = "DELETE FROM dbo.TicketRelation " +
                    "WHERE BeginHdTicketId = " + problemId + " AND " +
                    "EndHdTicketId = " + incidentId + " AND " +
                    "TicketRelationTypeId = 1";
            
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
    public Long createTicket(AlvaoTicketCreateData createData) {
        Connection conn = null;
        ResultSet rs = null;
        Long ticketId = null;
        
        TicketType type = createData.getType();
        String alvaoStatus = null;
        Integer alvaoPriority = AlvaoPriorityConvertor.convertToAlvaoPriority(createData.getPriority());
        
        switch (type) {
            case INCIDENT:
                alvaoStatus = AlvaoStatusConvertor.convertToAlvaoIncidentStatus(createData.getStatus(), this);
                break;
            case PROBLEM:
                alvaoStatus = AlvaoStatusConvertor.convertToAlvaoProblemStatus(createData.getStatus(), this);
                break;
            case CHANGE:
                alvaoStatus = AlvaoStatusConvertor.convertToAlvaoChangeStatus(createData.getStatus(), this);
                break;
        }
        
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(this.getConnectionString());
            conn.setAutoCommit(false);
            // uzivatel
            String userId;
            String userName;
            if (createData.getRequester() != null && createData.getRequester().getId() != null) {
                userId = createData.getRequester().getId().toString();
            } else {
                userId = "1";
            }
            if (createData.getRequester() != null && createData.getRequester().getName() != null) {                
                userName = createData.getRequester().getName();
            } else {
                userName = "Unknown";
            }
            // technik
            String techId;
            String techName;
            if (createData.getTechnician() != null && createData.getTechnician().getId() != null) {
                techId = createData.getTechnician().getId().toString();
            } else {
                techId = "1";
            }
            if (createData.getTechnician() != null && createData.getTechnician().getName() != null) {                
                techName = createData.getTechnician().getName();
            } else {
                techName = "Unknown";
            }
            
            
            PreparedStatement stmt;
            // vlozeni ticketu
            String sql = "INSERT INTO tHdTicket ("
                    + "iHdTicketId, "
                    + "sHdTicket, "
                    + "dHdTicket, "
                    + "liHdTicketUserPersonId, "
                    + "sHdTicketStateNotice, "
                    + "liHdTicketPriorityId, "
                    + "sHdTicketUser, "
                    + "liHdTicketHdSectionId, "
                    + "liHdTicketSlaId, "
                    + "Impact, "
                    + "Urgency,"
                    + "liHdTicketSolverPersonId)"
                    + "SELECT iHdTicketId = max(tc.liHdTicketId) + 1, "
                    + "sHdTicket = '" + createData.getSubject() + "', "
                    + "dHdTicket = GETDATE(), "
                    + "liHdTicketUserPersonId = " + userId + ", "
                    + "sHdTicketStateNotice = N'" + alvaoStatus + "', "
                    + "liHdTicketPriorityId = " + alvaoPriority + ", "
                    + "sHdTicketUser = N'" + userName + "', "
                    + "liHdTicketHdSectionId = (SELECT iHdSectionId FROM tHdSection WHERE sHdSection = N'" + createData.getService() + "'), "
                    + "liHdTicketSlaId = (SELECT iSlaId FROM tSla WHERE sSla = N'" + createData.getSla() + "'), "
                    + "Impact = 1, "
                    + "Urgency = 4, "
                    + "liHdTicketSolverPersonId = " + techId + " "
                    + "FROM tHdTicketCust tc";
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            // ziskame id vlozeneho ticketu
            sql = "SELECT max(liHdTicketId) as ticketId FROM tHdTicketCust";
            Statement st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                ticketId = rs.getLong("ticketId");
            }
            if (ticketId == null) {
                throw new SQLException("Nepodarilo se vytvorit novy tiket v ALVAO databazi");
            }
            // ziskani textoveho id ticketu
            sql = "SELECT * FROM HdSectionMessageTag WHERE id = (SELECT iHdSectionId FROM tHdSection WHERE sHdSection = N'" + createData.getService() + "')";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            String prefix = "", suffix = "";
            if (rs.next()) {
                prefix = rs.getString("Prefix");
                suffix = rs.getString("Suffix");
            }
            String textId = prefix + ticketId + suffix;
            // vlozeni textoveho id ticketu
            sql = "UPDATE tHdTicket SET sHdTicketMessageTag = N'" + textId + "' WHERE iHdTicketId = " + ticketId;
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();            
            // vlozeni detailu/zpravy ticketu
            sql = "INSERT INTO tAct (liActKindId, dAct, sAct, mActNotice, liActFromPersonId, liActHdTicketId, sActFrom, CreatedByPersonId, ActHtml, bNoCharge) "
                    + "VALUES (7, GETDATE(), N'" + createData.getSubject()+ "', N'" + createData.getDetail() + "', 1, " + ticketId + ", '_system', 1, N'" + createData.getDetail() + "', 0)";
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            
            if (createData.getConfigurationItem() != null) {
                sql = "UPDATE dbo.tHdTicket SET liHdTicketNodeId = " + createData.getConfigurationItem() + " "
                    + "WHERE iHdTicketId = " + ticketId;            
                stmt = conn.prepareStatement(sql);
                stmt.executeUpdate();
            }
            
            conn.commit();
        } catch (ClassNotFoundException | SQLException ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                // no op
            }
            Logger.getLogger(AlvaoDbTicketDao.class.getName()).log(Level.SEVERE, null, ex);
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
        return ticketId;
    }
    
}
