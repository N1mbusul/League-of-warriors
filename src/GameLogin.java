import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameLogin extends JFrame implements ActionListener{

    // Singleton Pattern
    private static GameLogin joc = null;
    public static GameLogin getInstance(){
            if (joc == null) {
                joc = new GameLogin();
            }
        return joc;
    }

    private ArrayList<Account> accounts;

    Container container = getContentPane();
    JLabel userLabel = new JLabel("Username");
    JLabel passwordLabel = new JLabel("Password");
    JTextField userField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset All");
    JCheckBox showPassword = new JCheckBox("Show Password");

    private final char defaultEchoChar;

    private GameLogin(){
        try {
            accounts = JsonInput.deserializeAccounts();
            if (accounts == null || accounts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No accounts available. Exiting.");
                System.exit(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading accounts: " + e.getMessage());
            System.exit(1);
        }

        // pt tickbox de show password
        defaultEchoChar = passwordField.getEchoChar();

        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

        //container.setBackground(Color.GREEN);

        // pentru a nu putea da copy-paste in casute
//        userField.setTransferHandler(null);
//        passwordField.setTransferHandler(null);
    }

    public void setLayoutManager(){
        container.setLayout(null);
    }

    public void setLocationAndSize(){
        userLabel.setBounds(50,150,100,30);
        passwordLabel.setBounds(50,220,100,30);
        userField.setBounds(150,150,150,30);
        passwordField.setBounds(150,220,150,30);
        showPassword.setBounds(150,250,150,30);
        //showPassword.setBackground(Color.RED);
        loginButton.setBounds(50,300,100,30);
        //loginButton.setBackground(Color.BLUE);
        resetButton.setBounds(200,300,100,30);
        //resetButton.setBackground(Color.CYAN);
    }

    public void addComponentsToContainer(){
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
    }

    public void addActionEvent(){
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
    }

    private Account authenticate(String email, String password) {
        for (Account account : accounts) {
            Credentials credentials = account.getInformation().getCredentials();
            if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == loginButton){
            String userTxt;
            String passTxt;
            userTxt = userField.getText();
            passTxt = new String(passwordField.getPassword());

            Account currentAccount = authenticate(userTxt, passTxt);

            if (currentAccount != null) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                dispose();
                // urmeaza character selection
                GameCharacterSelection characterSelection = GameCharacterSelection.getInstance(currentAccount);
                characterSelection.run();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        }

        if(e.getSource() == resetButton){
            userField.setText("");
            passwordField.setText("");
        }

        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar(defaultEchoChar);
            }
        }
    }

    public void run() {
        setTitle("Login Board");
        setVisible(true);
        setBounds(10, 10, 370, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        userField.setText("");
        passwordField.setText("");
    }

}


