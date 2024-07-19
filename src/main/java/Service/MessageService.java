package Service;

import Model.Message;
import Model.Account;
import DAO.MessageDAO;
import DAO.AccountDAO;

import java.util.List;
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
        return null;
    }

    /**
     * Gets all messages.
     * @return all {@code Message} records, otherwise {@code null}.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }


    /**
     * Get {@code Message} given {@code message_id}.
     * @param message_id
     * @return the {@code Message}, otherwise {@code null}.
     */
    public Message getMessageById(int message_id) {
        Optional<Message> foundMessage = messageDAO.getMessageById(message_id);
        return foundMessage.isPresent() ? foundMessage.get() : null;
    }

    /**
     * Delete {@code Message} given {@code message_id}.
     * @param message_id
     * @return the deleted {@code Message}, otherwise {@code null}.
     */
    public Message deleteMessageById(int message_id) {
        Optional<Message> messageToDelete = messageDAO.getMessageById(message_id);

        if (messageToDelete.isPresent()) {
            if(messageDAO.deleteMessageById(message_id) == 1) return messageToDelete.get();
        }
        return null;
    }

    /**
     * Update {@code Message} record.
     * @param message
     * @return the updated {@code Message}, otherwise {@code null}.
     */
    public Message updateMessageTextById(Message message) {
        Optional<Message> retrievedMessage = Optional.ofNullable(this.getMessageById(message.getMessage_id()));
        String msgText = message.getMessage_text();

        if (retrievedMessage.isPresent() && !(msgText.isBlank() || msgText.length() > 255)) {
            retrievedMessage.get().setMessage_text(message.getMessage_text());
            message = retrievedMessage.get();
            if(messageDAO.updateMessageTextById(message)) return message;
        }
        return null;
    }


    /**
     * Get all {@code Message} records for given {@code account_id}.
     * @param account_id
     * @return
     */
    public List<Message> getAllMessagesByUser(int account_id) {
        return messageDAO.getAllMessagesByUser(account_id);
    }

}
