package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageDAO {
    
    /**
     * Writes given {@code Message} to database.
     * @param message
     * @return written {@code Message}, otherwise {@code null}.
     */
    public Message writeMessage(Message message) {
        try {
            // 1. get connection -> prepare DML -> executeUpdate
            Connection conn = ConnectionUtil.getConnection();
            String query = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";
            PreparedStatement psmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            psmt.setInt(1, message.getPosted_by());
            psmt.setString(2, message.getMessage_text());
            psmt.setLong(3, message.getTime_posted_epoch());
            psmt.executeUpdate();

            // 2. retrieve key : implies successful persistence
            ResultSet rs = psmt.getGeneratedKeys();
            if (rs.first()) {
                Message retrievedMessage = new Message();
                retrievedMessage.setMessage_id(rs.getInt(1));
                retrievedMessage.setMessage_text(message.getMessage_text());
                retrievedMessage.setPosted_by(message.getPosted_by());
                retrievedMessage.setTime_posted_epoch(message.getTime_posted_epoch());
                return retrievedMessage;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occured: " + e.toString());
        }
        return null;
    }
}
