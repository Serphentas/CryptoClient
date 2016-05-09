package visual.workers;

import internal.crypto.GCMCipher;
import internal.crypto.GPCrypto;
import internal.network.Authentication;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import visual.DefaultFrame;
import visual.ErrorHandler;
import visual.LoginForm;

public class LoginWorker extends SwingWorker<Boolean, Boolean> {

    private final String username;
    private final char[] loginPass;
    private final JFrame frame;

    public LoginWorker(String username, char[] password, JFrame frame) {
        this.username = username;
        this.loginPass = password;
        this.frame = frame;
    }

    @Override
    protected Boolean doInBackground() throws IOException, Exception {
        return Authentication.login(username, loginPass);
    }

    @Override
    protected void done() {
        try {
            if (get()) {
                GCMCipher.setPassword(loginPass.clone());
                GPCrypto.sanitize(loginPass, 10000);
                frame.dispose();
                DefaultFrame.main(null);
            } else {
                LoginForm.updateLoginLabel("Login failed, please check your credentials.");
            }
        } catch (InterruptedException | ExecutionException ex) {
            ErrorHandler.showError(ex);
        }
    }
}
