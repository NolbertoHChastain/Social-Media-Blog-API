package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.util.Optional;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {

    /**
     * Write given {@code Author} to database.
     * @param account
     * @return
     */
    public Account writeAccount(Account account) {
        try {
            Connection conn = ConnectionUtil.getConnection();                                           // 1. get connection
            String query = "INSERT INTO account(username, password) VALUES(?, ?)";                      // 2. create SQL query
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);    // 3. send query to db (prepare)
            pstmt.setString(1, account.getUsername());                                   // 4. setup values
            pstmt.setString(2, account.getPassword());
            pstmt.executeUpdate();                                                                      // 5. execute DML

            // ensure persistence
            Account writtenAccount = null;
            ResultSet result = pstmt.getGeneratedKeys();                                                // contains single column - starts at [1]
            if (result.first()) {
                writtenAccount = new Account();
                writtenAccount.setAccount_id(result.getInt(1));
                writtenAccount.setUsername(account.getUsername());                                      // retrieve directly from source used in successful query
                writtenAccount.setPassword(account.getPassword());
                return writtenAccount;
            } // record was written successfully - sufficient enough to retrieve key only
            return writtenAccount;
        } catch (SQLException e) {
            return null;
        }
    }
    
    /**
     * if present retrieves account given user, else returns {@code null}.
     * @param username
     * @return
     */
    public Optional<Account> getAccountByUsername(String username) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String query = "SELECT * FROM account WHERE username = ?";
            PreparedStatement psmt = connection.prepareStatement(query);
            psmt.setString(1, username);
            ResultSet result = psmt.executeQuery();
            
            if (result.first()) {
                Account account = new Account();
                account.setAccount_id(result.getInt("account_id"));
                account.setUsername(result.getString("username"));
                account.setPassword(result.getString("password"));
                return Optional.of(account);
            }
        } catch (SQLException e) {
            System.out.println("SQLException found: " + e.toString());
        }
        return Optional.empty();
    }

}
