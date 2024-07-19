package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

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

    /**
     * Retrieve all {@code Message} records from database.
     * @return all {@code Message} records, otherwise {@code null}.
     */
    public List<Message> getAllMessages() {
        List<Message> allMessages = new ArrayList<>();

        try {
            // 1. get connection -> 2. prepare DQL -> execute query
            Connection conn = ConnectionUtil.getConnection();
            String query = "SELECT * FROM message";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Message message = new Message();
                message.setMessage_id(rs.getInt("message_id"));
                message.setMessage_text(rs.getString("message_text"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                allMessages.add(message);
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception occurred: " + e.toString());
        }
        return allMessages;
    }


    /**
     * given {@code message_id} return {@code Message} from database.
     * @param message_id
     * @return the {@code Message}, otherwise {@code null}.
     */
    public Optional<Message> getMessageById(int message_id) {
        try {
            // 1. get connection -> 2. prepare DQL -> 3. execute query
            Connection conn = ConnectionUtil.getConnection();
            String query = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, message_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.first()) {
                Message message = new Message();
                message.setMessage_id(rs.getInt("message_id"));
                message.setMessage_text(rs.getString("message_text"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                return Optional.of(message);
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception occurred: " + e.toString());
        }
        return Optional.empty();
    }

    /**
     * Given {@code message_id} delete {@code Message} from database.
     * @param message_id
     * @return number of deleted {@code records}, otherwise 0.
     */
    public int deleteMessageById(int message_id) {
        try {
            // 1. get connection -> 2. prepare DML -> execute update
            Connection conn = ConnectionUtil.getConnection();
            String query = "Delete FROM message WHERE message_id = ?";
            PreparedStatement pstmt= conn.prepareStatement(query);
            pstmt.setInt(1, message_id);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("SQL Exception occurred: " + e.toString());
        }
        return 0;
    }

}
