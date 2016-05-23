package visual.workers;

import internal.Settings;
import internal.crypto.DefaultCipher;
import internal.crypto.GPCrypto;
import internal.network.Authentication;
import internal.network.Control;
import internal.network.IO;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
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
                GPCrypto.sanitize(loginPass);
                boolean success = false;

                JPasswordField passField = new JPasswordField();
                if (Settings.isNew()) {
                    // warning user about password strength aka entropy
                    String msg1 = "It appears it is your first time using the client. Before continuing, you must"
                            + " set an encryption password."
                            + "\nAs it will be used to secure your files, It is extremely important that you choose "
                            + "one which is resistant against brute-force attacks."
                            + "\n\nIt is wise to use as many different characters as possible. We also enforce a "
                            + "minimum length of 20 characters, with no upper bound."
                            + "\n\nPlease keep in mind that the security of your files depends on it. As such, "
                            + "we strongly recommend you to use the KeePassX\nsoftware to generate a random "
                            + "password and store it in a database.";
                    JOptionPane.showConfirmDialog(null, msg1, "Disclaimer",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    // getting the password
                    JPasswordField confirmField = new JPasswordField();
                    Object[] content = {
                        "Password", passField,
                        "Confirm", confirmField
                    };
                    boolean done = false;

                    while (!done) {
                        int returnVal = JOptionPane.showOptionDialog(null, content, "Set encryption password",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                new String[]{"Set password"}, "Set password");

                        if (returnVal == JOptionPane.CLOSED_OPTION) {
                            IO.disconnect();
                            Control.disconnect();
                            LoginForm.updateLoginLabel("");
                            done = true;
                        } else if (passField.getPassword().length < 20) {
                            JOptionPane.showMessageDialog(null, "Password must be at least 20 characters long.",
                                    "Set encryption password", JOptionPane.WARNING_MESSAGE);
                        } else if (Arrays.equals(passField.getPassword(), confirmField.getPassword())) {
                            // asking for confirmation before setting the password
                            String msg2 = "Are you sure you want to use this password ?\n\nNo recovery is possible. "
                                    + "Losing it means losing all your files !";
                            int confirm = JOptionPane.showConfirmDialog(null, msg2, "Disclaimer",
                                    JOptionPane.YES_NO_CANCEL_OPTION);
                            switch (confirm) {
                                case JOptionPane.YES_OPTION:
                                    DefaultCipher.setEncryptionPassword(passField.getPassword().clone());
                                    GPCrypto.sanitize(passField.getPassword());
                                    success = true;
                                    done = true;
                                    break;
                                case JOptionPane.CANCEL_OPTION:
                                case JOptionPane.CLOSED_OPTION:
                                    IO.disconnect();
                                    Control.disconnect();
                                    LoginForm.updateLoginLabel("");
                                    done = true;
                                    break;
                                case JOptionPane.NO_OPTION:
                                    passField.setText("");
                                    confirmField.setText("");
                                    break;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Passwords do not match", "Set encryption "
                                    + "password", JOptionPane.ERROR_MESSAGE);
                            passField.setText("");
                        }
                    }
                } else {
                    Settings.setIsNew(false);
                    Object[] content = {
                        "Password", passField
                    };

                    boolean done = false;
                    while (!done) {
                        int returnVal = JOptionPane.showOptionDialog(null, content, "Verify encryption password",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                new String[]{"Verify"}, "Verify");

                        if (returnVal == JOptionPane.CLOSED_OPTION) {
                            IO.disconnect();
                            Control.disconnect();
                            LoginForm.updateLoginLabel("");
                            done = true;
                        } else if (passField.getPassword().length < 20) {
                            JOptionPane.showMessageDialog(null, "Password must be at least 20 characters long",
                                    "Verify encryption password", JOptionPane.ERROR_MESSAGE);
                        } else {
                            DefaultCipher.setEncryptionPassword(passField.getPassword().clone());
                            GPCrypto.sanitize(passField.getPassword());
                            success = true;
                            done = true;
                        }
                    }
                }

                if (success) {
                    frame.dispose();
                    DefaultFrame.main(null);
                }
            } else {
                LoginForm.updateLoginLabel("Login failed, please check your credentials.");
            }
        } catch (InterruptedException | ExecutionException | IOException ex) {
            ErrorHandler.showError(ex);
        }
    }
}
