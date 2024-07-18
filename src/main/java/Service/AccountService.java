package Service;

import java.util.Optional;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    
    AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    /**
     * Checks validity of account, if valid then writes, else returns null.
     * @param account
     * @return
     */
    public Account addAccount(Account account) {
        Optional<Account> optionalAccount = accountDAO.getAccountByUsername(account.getUsername());
        
        if (!optionalAccount.isPresent() && !(account.getUsername().isBlank() || account.getPassword().length() < 4)) {
            System.out.println("not in db and valid: " + "\n" + account);
            return accountDAO.writeAccount(account);
        } else return null;
    }

    /**
     * Given {@code account} verifies it exists and matches
     * the account stored in the database.
     * @param username
     * @return
     */
    public Account verifyLogin(Account account) {
        Optional<Account> existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        
        if (existingAccount.isPresent()) {
            Account retrievedAccount = existingAccount.get();
            if (account.getPassword().equals(retrievedAccount.getPassword())) return retrievedAccount;
        }
        return null; // does not exist
    }
}