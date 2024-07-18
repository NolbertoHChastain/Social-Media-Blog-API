package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import Model.Account;
import Service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;

    public SocialMediaController() {
        this.accountService = new AccountService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);   // User Story starting point : Register
        app.post("/login", this::loginHandler);         // user story starting point : Login

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * This handler is used to POST a new Account. 
     * Converts JSON body request to Account object, if AccountService returns null, then account creation
     * was unsuccessful, and returns 400 - client error, else returns newly created account. 
     */
    private void registerHandler(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null) {
            context.json(addedAccount);
            context.status(HttpStatus.OK); // succeeded - 200
        } 
        else context.status(HttpStatus.BAD_REQUEST); // client error - 400
    }

    /**
     * handler handles login process, if given {@code account} exists
     * user logins sucessfully, else unauthorized.
     * @param context
     */
    public void loginHandler(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account existingAccount = accountService.verifyLogin(account);
        
        if (existingAccount != null) {
            context.json(existingAccount); // with id
            context.status(HttpStatus.OK);
        } // if NOT null : valid
        else context.status(HttpStatus.UNAUTHORIZED); // unauthorized - 401
    }


}