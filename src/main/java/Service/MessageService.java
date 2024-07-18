package Service;

import Model.Message;
import Model.Account;
import DAO.MessageDAO;
import DAO.AccountDAO;

import java.util.Optional;

public class MessageService {

    MessageDAO messageDAO;
    AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }
    
    /**
     * Checks validity of given {@code message} to write to database.
     * Successful if message text is NOT blank, NOT over 255 characters,
     * and is posted by an existing user - {@code account} stored in database,
     * otherwise returns null.
     * @param message
     * @return
     */
    public Message addMessage(Message message) {
        Optional<Account> existingAccount = accountDAO.getAccountById(message.getPosted_by());
        //if (existingAccount.isPresent() && !(message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) )
        if (existingAccount.isPresent()) {
            String msgText = message.getMessage_text();
            if (!(msgText.isBlank() || msgText.length() > 255)) return messageDAO.writeMessage(message);
        }
        System.out.println("after not being present=============================");
        return null;
    }
}
