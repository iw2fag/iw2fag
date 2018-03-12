package com.iw2fag.lab.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 8/29/2017
 * Time: 9:41 AM
 */
public class PasswordPanel {

    private JPanel parentPanel;
    private JPanel userPanel;
    private JPanel userCredentialPanel;
    private JPanel superUserPanel;
    private JPanel adminUserPanel;
    private JPanel rulesPanel;
    private JLabel adminLabel;
    private JCheckBox adminPasswordCheckbox;
    private JLabel confirmAdminPwdLabel;
    private JPasswordField confirmAdminPwdTextField;
    private JPasswordField confirmDefaultUserPwdTextField;
    private JLabel confirmSuperUserPwdLabel;
    private JPasswordField confirmSuperUserPwdTextField;
    private JLabel confirmDefaultUserPwdLabel;
    private JLabel createAdminNameLabel;
    private JTextField createAdminNameTextField;
    private JLabel createAdminPwdLabel;
    private JPasswordField createAdminPwdTextField;
    private JLabel createSuperUserPwdLabel;
    private JPasswordField createSuperUserPwdTextField;
    private JLabel credentialLabel;
    private JLabel defaultUserPwdLabel;
    private JPasswordField defaultUserPwdTextField;
    private JLabel passwordLabel;
    private JLabel pwdRuleLabel;
    private JLabel superUserLabel;
    private JCheckBox superUserPwdCheckbox;
    private JLabel usernameRuleLabel;

    protected static Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();

    static {
        attributes.put(TextAttribute.FAMILY, "Open Sans");
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_MEDIUM);
        attributes.put(TextAttribute.SIZE, 12);
    }

    protected Font totalTitleFont = new Font("Open Sans", Font.BOLD, 18);
    protected Font smallTitleFont = Font.getFont(attributes);
    protected Font defaultTextFont = new Font("Open Sans", Font.PLAIN, 12);
    protected Font boldTitleFont = new Font("Open Sans", Font.BOLD, 12);


    //Color
    protected final Color titleColor = new Color(0, 204, 153);
    protected final Color btnEnableColor = new Color(0, 204, 153);
    protected final Color btnDisableColor = new Color(232, 232, 232);
    protected final Color fontDisableColor = new Color(158, 158, 158);
    protected final Color fontEnableColor = new Color(68, 68, 68);
    private JFrame frame;

    public PasswordPanel() {
        frame = new JFrame();
        parentPanel = new JPanel();
        userCredentialPanel = new JPanel();
        superUserPanel = new JPanel();
        adminUserPanel = new JPanel();
        userPanel = new JPanel();
        rulesPanel = new JPanel();
        credentialLabel = new JLabel();
        passwordLabel = new JLabel();
        confirmDefaultUserPwdLabel = new JLabel();
        defaultUserPwdLabel = new JLabel();
        confirmAdminPwdTextField = new JPasswordField();
        defaultUserPwdTextField = new JPasswordField();
        adminLabel = new JLabel();
        superUserPwdCheckbox = new JCheckBox();
        createSuperUserPwdLabel = new JLabel();
        confirmSuperUserPwdLabel = new JLabel();
        confirmSuperUserPwdTextField = new JPasswordField();
        createSuperUserPwdTextField = new JPasswordField();
        superUserLabel = new JLabel();
        createAdminNameLabel = new JLabel();
        createAdminNameTextField = new JTextField();
        adminPasswordCheckbox = new JCheckBox();
        confirmAdminPwdLabel = new JLabel();
        createAdminPwdLabel = new JLabel();
        createAdminPwdTextField = new JPasswordField();
        confirmDefaultUserPwdTextField = new JPasswordField();
        usernameRuleLabel = new JLabel();
        pwdRuleLabel = new JLabel();
    }

    public void configComponents() {

        //Label
        credentialLabel.setText("Credentials");
        credentialLabel.setFont(totalTitleFont);
        credentialLabel.setForeground(titleColor);
        credentialLabel.setPreferredSize(new Dimension(420, 25));
        //Label
        passwordLabel.setText("<html>The installation creates three accounts. Configure their credentials here.<br><b>Default user: admin@default.com </b></html>");
        passwordLabel.setForeground(Color.white);
        passwordLabel.setFont(defaultTextFont);
        passwordLabel.setPreferredSize(new Dimension(420, 50));

        //Label
        defaultUserPwdLabel.setText("Create default user password");
        defaultUserPwdLabel.setForeground(Color.white);
        defaultUserPwdLabel.setFont(smallTitleFont);
        defaultUserPwdLabel.setPreferredSize(new Dimension(200, 20));

        //Label
        confirmDefaultUserPwdLabel.setText("Confirm password");
        confirmDefaultUserPwdLabel.setForeground(Color.white);
        confirmDefaultUserPwdLabel.setFont(smallTitleFont);
        confirmDefaultUserPwdLabel.setPreferredSize(new Dimension(200, 20));

        //TextField
        defaultUserPwdTextField.setForeground(Color.black);
        defaultUserPwdTextField.setBackground(Color.white);
        defaultUserPwdTextField.setPreferredSize(new Dimension(200, 20));

        //TextField
        confirmDefaultUserPwdTextField.setForeground(Color.black);
        confirmDefaultUserPwdTextField.setBackground(Color.white);
        confirmDefaultUserPwdTextField.setPreferredSize(new Dimension(200, 20));

        //Label
        superUserLabel.setText("<html><b>Database superuser: </b>Can override all access restrictions within the <br>database. Use this role only when needed.</html>");
        superUserLabel.setForeground(Color.white);
        superUserLabel.setFont(defaultTextFont);
        superUserLabel.setPreferredSize(new Dimension(420, 40));

        //Checkbox
        superUserPwdCheckbox.setText("Use the same password as the default user");
        superUserPwdCheckbox.setForeground(Color.white);
        superUserPwdCheckbox.setBackground(null);
        superUserPwdCheckbox.setFont(defaultTextFont);
        superUserPwdCheckbox.setPreferredSize(new Dimension(420, 25));


        //Label
        createSuperUserPwdLabel.setText("Create superuser password");
        createSuperUserPwdLabel.setForeground(Color.white);
        createSuperUserPwdLabel.setFont(smallTitleFont);
        createSuperUserPwdLabel.setPreferredSize(new Dimension(200, 20));

        //Label
        confirmSuperUserPwdLabel.setText("Confirm superuser password");
        confirmSuperUserPwdLabel.setForeground(Color.white);
        confirmSuperUserPwdLabel.setFont(smallTitleFont);
        confirmSuperUserPwdLabel.setPreferredSize(new Dimension(200, 20));

        //TextField
        createSuperUserPwdTextField.setForeground(Color.black);
        createSuperUserPwdTextField.setBackground(Color.white);
        createSuperUserPwdTextField.setPreferredSize(new Dimension(200, 20));

        //TextField
        confirmSuperUserPwdTextField.setForeground(Color.black);
        confirmSuperUserPwdTextField.setBackground(Color.white);
        confirmSuperUserPwdTextField.setPreferredSize(new Dimension(200, 20));

        //Label
        adminLabel.setText("<html><b>Database Administrator: </b>Can modify the Mobile Center Server <br> database schema.</html>");
        adminLabel.setForeground(Color.white);
        adminLabel.setFont(defaultTextFont);
        adminLabel.setPreferredSize(new Dimension(420, 40));

        //Label
        createAdminNameLabel.setText("Create DB admin username");
        createAdminNameLabel.setForeground(Color.white);
        createAdminNameLabel.setFont(smallTitleFont);
        createAdminNameLabel.setPreferredSize(new Dimension(200, 20));

        //TextField
        createAdminNameTextField.setForeground(Color.black);
        createAdminNameTextField.setBackground(Color.white);
        createAdminNameTextField.setPreferredSize(new Dimension(200, 20));

        //Checkbox
        adminPasswordCheckbox.setText("Use the same password as the default user ");
        adminPasswordCheckbox.setForeground(Color.white);
        adminPasswordCheckbox.setBackground(null);
        adminPasswordCheckbox.setFont(defaultTextFont);
        adminPasswordCheckbox.setPreferredSize(new Dimension(420, 25));


        //Label
        createAdminPwdLabel.setText("Create DB admin password");
        createAdminPwdLabel.setForeground(Color.white);
        createAdminPwdLabel.setFont(smallTitleFont);
        createAdminPwdLabel.setPreferredSize(new Dimension(200, 20));

        //Label
        confirmAdminPwdLabel.setText("Confirm DB admin password");
        confirmAdminPwdLabel.setForeground(Color.white);
        confirmAdminPwdLabel.setFont(smallTitleFont);
        confirmAdminPwdLabel.setPreferredSize(new Dimension(200, 20));

        //TextField
        createAdminPwdTextField.setForeground(Color.black);
        createAdminPwdTextField.setBackground(Color.white);
        createAdminPwdTextField.setPreferredSize(new Dimension(200, 20));

        //TextField
        confirmAdminPwdTextField.setForeground(Color.black);
        confirmAdminPwdTextField.setBackground(Color.white);
        confirmAdminPwdTextField.setPreferredSize(new Dimension(200, 20));


        //Label
        pwdRuleLabel.setText("<html><b>Password rules:</b><br><b>.</b> At least 6 characters <br><b>.</b> Both lower and upper case <br><b>.</b> At least one digit<br><b>.</b> Only letters,numbers,'_','@' </html>");
        pwdRuleLabel.setForeground(titleColor);
        pwdRuleLabel.setFont(defaultTextFont);
        pwdRuleLabel.setPreferredSize(new Dimension(200, 120));


        //Label
        usernameRuleLabel.setText("<html><b>Username rules:</b><br> <b>.</b> Only letters, numbers or '_'</html>");
        usernameRuleLabel.setForeground(titleColor);
        usernameRuleLabel.setFont(defaultTextFont);
        usernameRuleLabel.setPreferredSize(new Dimension(200, 60));

        //initial
    }

    public static void main(String[] args) {
        PasswordPanel passwordPanelTest = new PasswordPanel();
        passwordPanelTest.configComponents();
        passwordPanelTest.configPanelLayout();
        passwordPanelTest.add2Frame();
    }

    private void add2Frame() {
        frame.add(parentPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }

    public void configPanelLayout() {

        parentPanel.setLayout(new GridBagLayout());
        parentPanel.setPreferredSize(new Dimension(695, 565));
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        userPanel.setPreferredSize(new Dimension(465, 565));
        configUserPanel(userPanel);
        parentPanel.add(userPanel, c);

        c.gridx = 1;
        c.gridy = 0;
        rulesPanel.setPreferredSize(new Dimension(230, 565));
        configRulePanel(rulesPanel);
        parentPanel.add(rulesPanel, c);

    }

    private void configRulePanel(JPanel rulesPanel) {
        rulesPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(110, 5, 0, 0);
        rulesPanel.add(pwdRuleLabel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(200, 5, 120, 0);
        rulesPanel.add(usernameRuleLabel, c);
    }

    private void configUserPanel(JPanel userPanel) {
        userPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        userCredentialPanel.setPreferredSize(new Dimension(465, 175));
        configUserCredentialPanel(userCredentialPanel);
        userPanel.add(userCredentialPanel, c);

        c.gridx = 0;
        c.gridy = 1;
        superUserPanel.setPreferredSize(new Dimension(465, 170));
        configSuperUserPanel(superUserPanel);
        userPanel.add(superUserPanel, c);

        c.gridx = 0;
        c.gridy = 2;
        configAdminUserPanel(adminUserPanel);
        adminUserPanel.setBackground(Color.DARK_GRAY);
        adminUserPanel.setPreferredSize(new Dimension(465, 220));
        userPanel.add(adminUserPanel, c);

    }

    private void configAdminUserPanel(JPanel adminUserPanel) {
        adminUserPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 0, 10);
        adminUserPanel.add(adminLabel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 10, 0, 10);
        adminUserPanel.add(createAdminNameLabel, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(0, 10, 0, 10);
        adminUserPanel.add(createAdminNameTextField, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.insets = new Insets(5, 10, 0, 10);
        adminUserPanel.add(adminPasswordCheckbox, c);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.insets = new Insets(0, 10, 0, 10);
        adminUserPanel.add(createAdminPwdLabel, c);

        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(0, 10, 0, 10);
        adminUserPanel.add(confirmAdminPwdLabel, c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(0, 10, 30, 10);
        adminUserPanel.add(createAdminPwdTextField, c);

        c.gridx = 1;
        c.gridy = 5;
        c.insets = new Insets(0, 10, 30, 10);
        adminUserPanel.add(confirmAdminPwdTextField, c);

    }

    private void configSuperUserPanel(JPanel superUserPanel) {
        superUserPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(20, 10, 0, 10);
        superUserPanel.add(superUserLabel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 10, 0, 10);
        superUserPanel.add(superUserPwdCheckbox, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(0, 10, 0, 10);
        superUserPanel.add(createSuperUserPwdLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0, 10, 0, 10);
        superUserPanel.add(confirmSuperUserPwdLabel, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 10, 30, 10);
        superUserPanel.add(createSuperUserPwdTextField, c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(0, 10, 30, 10);
        superUserPanel.add(confirmSuperUserPwdTextField, c);

    }

    private void configUserCredentialPanel(JPanel userCredentialPanel) {
        userCredentialPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 0, 10);
        userCredentialPanel.add(credentialLabel, c);


        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 10, 0, 10);
        userCredentialPanel.add(passwordLabel, c);


        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(5, 10, 0, 10);
        userCredentialPanel.add(defaultUserPwdLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(5, 10, 0, 10);
        userCredentialPanel.add(confirmDefaultUserPwdLabel, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 10, 10, 10);
        userCredentialPanel.add(defaultUserPwdTextField, c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(0, 10, 10, 10);
        userCredentialPanel.add(confirmDefaultUserPwdTextField, c);

    }


}
