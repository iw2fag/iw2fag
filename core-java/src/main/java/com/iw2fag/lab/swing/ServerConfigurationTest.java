package com.iw2fag.lab.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ServerConfigurationTest {

    //Font
    protected static Map<TextAttribute, Object> attributes = new HashMap<>();

    static {
        attributes.put(TextAttribute.FAMILY, "Open Sans");
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_MEDIUM);
        attributes.put(TextAttribute.SIZE, 12);
    }

    protected Font totalTitleFont = new Font("Open Sans", Font.BOLD, 18);
    protected Font smallTitleFont = Font.getFont(attributes);
    protected Font defaultTextFont = new Font("Open Sans", Font.PLAIN, 12);


    //Color
    protected final Color titleColor = new Color(0, 121, 239);
    protected final Color buttonTextColor = new Color(68, 68, 68);

    private static JPanel panel;
    private JPanel serverConfigurationPanel;
    private JPanel proxyConfigurationPanel;
    private JLabel authenticationTitleLabel;
    private JLabel entercredentialLabel;
    private JTextField credentialTextField;
    private JLabel obtainCredentialLabel;

    private JLabel proxyTitleLabel;
    private JRadioButton disableProxyRadioButton;
    private JRadioButton enableProxyRadioButton;
    private JLabel proxyServerAddressLabel;
    private JTextField proxyServerAddressTextField;
    private JLabel proxyServerPortLabel;
    private JTextField proxyServerPortTextField;
    private JLabel useProxyCredentialLabel;
    private JLabel proxyUsernameLabel;
    private JTextField proxyUsernameTextField;
    private JLabel proxyPasswordLabel;
    private JPasswordField proxyPasswordTextField;
    private JFileChooser chooser;
    private ButtonGroup radioBtnGroup;

    public static void main(String[] args) {
        ServerConfigurationTest test = new ServerConfigurationTest();
        test.configComponents();
        test.configPanelLayout();
        JFrame frame = new JFrame();
        frame.setSize(695, 565);
        frame.add(panel);
        frame.show();
    }

    public ServerConfigurationTest() {
        panel = new JPanel();
        serverConfigurationPanel = new JPanel();
        proxyConfigurationPanel = new JPanel();
        authenticationTitleLabel = new JLabel();

        entercredentialLabel = new JLabel();
        credentialTextField = new JTextField();
        obtainCredentialLabel = new JLabel();

        proxyTitleLabel = new JLabel();
        disableProxyRadioButton = new JRadioButton();
        enableProxyRadioButton = new JRadioButton();
        useProxyCredentialLabel = new JLabel();
        proxyServerAddressLabel = new JLabel();
        proxyServerAddressTextField = new JTextField();
        proxyServerPortLabel = new JLabel();
        proxyServerPortTextField = new JTextField();
        proxyUsernameLabel = new JLabel();
        proxyUsernameTextField = new JTextField();
        proxyPasswordLabel = new JLabel();
        proxyPasswordTextField = new JPasswordField();
        chooser = new JFileChooser();
        radioBtnGroup = new ButtonGroup();
        radioBtnGroup.add(disableProxyRadioButton);
        radioBtnGroup.add(enableProxyRadioButton);
    }


    ActionListener disableProxyListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            proxyServerAddressTextField.setEnabled(false);
            proxyServerPortTextField.setEnabled(false);
            proxyUsernameTextField.setEnabled(false);
            proxyPasswordTextField.setEnabled(false);
        }
    };

    ActionListener enableProxyListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            proxyServerAddressTextField.setEnabled(true);
            proxyServerPortTextField.setEnabled(true);
            proxyUsernameTextField.setEnabled(true);
            proxyPasswordTextField.setEnabled(true);
        }
    };

    public JPanel getConfiguredParentPanel() {
        return panel;
    }


    public void configPanelLayout() {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        serverConfigurationPanel.setPreferredSize(new Dimension(695, 280));
        configServerPanelLayout(serverConfigurationPanel);
        panel.add(serverConfigurationPanel, c);

        c.gridx = 0;
        c.gridy = 1;
        proxyConfigurationPanel.setPreferredSize(new Dimension(695, 285));
        configProxyPanel(proxyConfigurationPanel);
        panel.add(proxyConfigurationPanel, c);
    }

    public void configComponents() {

        //authentication title
        authenticationTitleLabel.setText("Authentication");
        authenticationTitleLabel.setPreferredSize(new Dimension(600, 25));
        authenticationTitleLabel.setForeground(Color.white);
        authenticationTitleLabel.setFont(totalTitleFont);

        //credential label
        entercredentialLabel.setText("Enter credential for Mobile Center or SRF");
        entercredentialLabel.setPreferredSize(new Dimension(600, 25));
        entercredentialLabel.setForeground(Color.white);
        entercredentialLabel.setFont(defaultTextFont);

        //credential textfield
        credentialTextField.setForeground(Color.black);
        credentialTextField.setBackground(Color.white);
        credentialTextField.setFont(defaultTextFont);
        credentialTextField.setPreferredSize(new Dimension(600, 25));

        //server address
        String obtainCredentialText =
                "<html>How to obtain credentials: <br>" +
                        "<br>" +
                        "<b>Mobile Center:</b> Contact your admin to request connector access token<br>" +
                        "<b>SRF:</b>In the Connector page under the mobile lab page, select \"+Device connector\" and generate your credentials<br>" +
                        "<br>" +
                        "<b>.</b>You can easily modify your connector form Mobile Center to SRF by entering different credentials</html>";
        obtainCredentialLabel.setText(obtainCredentialText);
        obtainCredentialLabel.setForeground(Color.white);
        obtainCredentialLabel.setFont(defaultTextFont);
        obtainCredentialLabel.setPreferredSize(new Dimension(600, 100));

        proxyTitleLabel.setText("Proxy Setting");
        proxyTitleLabel.setPreferredSize(new Dimension(600, 25));
        proxyTitleLabel.setForeground(Color.white);
        proxyTitleLabel.setFont(totalTitleFont);

        disableProxyRadioButton.setText("No proxy server is used");
        disableProxyRadioButton.setSelected(true);
        disableProxyRadioButton.setBackground(null);
        disableProxyRadioButton.setForeground(Color.white);
        disableProxyRadioButton.setFont(defaultTextFont);
        disableProxyRadioButton.setPreferredSize(new Dimension(600, 25));
        disableProxyRadioButton.addActionListener(disableProxyListener);

        enableProxyRadioButton.setText("Server address");
        enableProxyRadioButton.setSelected(false);
        enableProxyRadioButton.setBackground(null);
        enableProxyRadioButton.setForeground(Color.white);
        enableProxyRadioButton.setFont(defaultTextFont);
        enableProxyRadioButton.setPreferredSize(new Dimension(600, 25));
        enableProxyRadioButton.addActionListener(enableProxyListener);

        proxyServerAddressTextField.setPreferredSize(new Dimension(600, 25));
        proxyServerAddressTextField.setText("");
        /*if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
            bindMouseListener(proxyServerAddressTextField, showNativeTextFieldListener);
        }*/

        proxyServerPortLabel.setText("Server Port");
        proxyServerPortLabel.setPreferredSize(new Dimension(600, 25));
        proxyServerPortLabel.setForeground(Color.white);

        proxyServerPortTextField.setPreferredSize(new Dimension(600, 25));
        proxyServerPortTextField.setText("");
        /*if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
            bindMouseListener(proxyServerPortTextField, showNativeTextFieldListener);
        }*/

        useProxyCredentialLabel.setText("If needed, provide credentials for the proxy server:");
        useProxyCredentialLabel.setPreferredSize(new Dimension(600, 25));


        proxyUsernameLabel.setText("Username");
        proxyUsernameLabel.setPreferredSize(new Dimension(250, 25));
        proxyUsernameLabel.setForeground(Color.white);

        proxyUsernameTextField.setPreferredSize(new Dimension(250, 25));
        proxyUsernameTextField.setText("");
     /*   if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
            bindMouseListener(proxyUsernameTextField, showNativeTextFieldListener);
        }*/

        proxyPasswordLabel.setText("Password");
        proxyPasswordLabel.setPreferredSize(new Dimension(250, 25));
        proxyPasswordLabel.setForeground(Color.white);

        proxyPasswordTextField.setPreferredSize(new Dimension(250, 25));
        proxyPasswordTextField.setText("");
       /* if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
            bindMouseListener(proxyPasswordTextField, showNativeTextFieldListener);
        }*/
    }


    //This is only for Mac OSX
/*    private MouseListener chooseCredentialFileListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setDialogTitle("Select your credentials file:");

            int retVal = chooser.showOpenDialog(null);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                if (file.length() > 5 * 1024) {
                    JOptionPane.showMessageDialog(null, "The specified file is not valid. Choose a valid credentials file.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String credential = getCredentialFromFile(file);
                    credentialTextField.setText(credential);
                }

            }
        }
    };*/

    //This is only for Mac OSX
/*    private MouseListener showNativeTextFieldListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            //
            JTextComponent textComponent = (JTextComponent) e.getSource();
            if (!textComponent.isEnabled()) {
                return;
            }
            try {
                OSXSecureTextInputServices service = new OSXSecureTextInputServices();
                String value = service.showTextFieldAndGetInput(IAResourceBundle.getValue("Installer.JZGTextComponentUtil.secureText.title"), IAResourceBundle.getValue("Installer.JZGTextComponentUtil.secureText.messagePrompt"), IAResourceBundle.getValue("Installer.Customizer.ok"), IAResourceBundle.getValue("Installer.Customizer.cancel"), textComponent.getText(), Boolean.FALSE);
                textComponent.setText(value);
            } catch (Exception ignored) {

            }

        }
    };*/

    private void configProxyPanel(JPanel proxyConfigurationPanel) {
        proxyConfigurationPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 0, 10);
        proxyConfigurationPanel.add(proxyTitleLabel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 25, 0, 10);
        proxyConfigurationPanel.add(disableProxyRadioButton, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 25, 0, 10);
        proxyConfigurationPanel.add(enableProxyRadioButton, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 25, 0, 10);
        proxyConfigurationPanel.add(proxyServerAddressTextField, c);

        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(10, 25, 0, 10);
        proxyConfigurationPanel.add(proxyServerPortLabel, c);

        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(0, 25, 0, 10);
        proxyConfigurationPanel.add(proxyServerPortTextField, c);

        c.gridx = 0;
        c.gridy = 6;
        c.insets = new Insets(10, 25, 0, 10);
        proxyConfigurationPanel.add(useProxyCredentialLabel, c);

        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;

        c.insets = new Insets(5, 25, 0, 25);
        proxyConfigurationPanel.add(proxyUsernameLabel, c);

        c.gridx = 1;
        c.gridy = 7;
        c.insets = new Insets(5, 25, 0, 10);
        proxyConfigurationPanel.add(proxyPasswordLabel, c);

        c.gridx = 0;
        c.gridy = 8;
        c.insets = new Insets(0, 25, 20, 25);
        proxyConfigurationPanel.add(proxyUsernameTextField, c);

        c.gridx = 1;
        c.gridy = 8;
        c.insets = new Insets(0, 25, 20, 10);
        proxyConfigurationPanel.add(proxyPasswordTextField, c);

    }

    private void configServerPanelLayout(JPanel serverConfigurationPanel) {
        serverConfigurationPanel.setPreferredSize(new Dimension(695, 295));
        serverConfigurationPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        // c.weighty = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 10, 0, 10);
        serverConfigurationPanel.add(authenticationTitleLabel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 25, 0, 10);
        serverConfigurationPanel.add(entercredentialLabel, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 25, 0, 10);
        serverConfigurationPanel.add(credentialTextField, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(10, 25, 0, 10);
        serverConfigurationPanel.add(obtainCredentialLabel, c);

    }

    /*  private String getCredentialFromFile(File file) {
          try {
              return FileUtils.readFileToString(file);
          } catch (Exception e) {
              return null;
          }
      }
  */
    private static void bindMouseListener(JComponent component, MouseListener listener) {
        if (component.getMouseListeners() == null || !Arrays.asList(component.getMouseListeners()).contains(listener)) {
            component.addMouseListener(listener);
        }
    }


}
