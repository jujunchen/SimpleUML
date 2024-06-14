 package net.trustx.simpleuml.plugin.configuration;
 
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Insets;
 import javax.swing.JButton;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JTextField;
 import javax.swing.border.LineBorder;
 import javax.swing.border.TitledBorder;
 
 
 
 
 
 public class GeneralPanelUI
   extends JPanel
 {
   private JPanel pathPanel;
   private JButton showFileChooserButton;
   private JLabel defaultPathNameLabel;
   private JPanel birdViewPanel;
   private JPanel fillerPanel01;
   private JLabel defaultPathLabel;
   private JLabel birdViewDelayNameLabel;
   private JPanel fillerPanel;
   private JTextField birdViewDelayTextField;
   
   public GeneralPanelUI() {
     initComponents();
   }
 
 
 
 
 
 
 
 
 
   
   private void initComponents() {
     this.pathPanel = new JPanel();
     this.defaultPathNameLabel = new JLabel();
     this.defaultPathLabel = new JLabel();
     this.showFileChooserButton = new JButton();
     this.birdViewPanel = new JPanel();
     this.birdViewDelayNameLabel = new JLabel();
     this.birdViewDelayTextField = new JTextField();
     this.fillerPanel01 = new JPanel();
     this.fillerPanel = new JPanel();
     
     setLayout(new GridBagLayout());
     
     this.pathPanel.setLayout(new GridBagLayout());
     
     this.pathPanel.setBorder(new TitledBorder("Paths"));
     this.defaultPathNameLabel.setText("Default Diagram Path");
     GridBagConstraints gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.insets = new Insets(0, 5, 5, 5);
     this.pathPanel.add(this.defaultPathNameLabel, gridBagConstraints);
     
     this.defaultPathLabel.setBorder(new LineBorder(new Color(153, 153, 153)));
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 1;
     gridBagConstraints.insets = new Insets(0, 0, 5, 5);
     gridBagConstraints.weightx = 1.0D;
     this.pathPanel.add(this.defaultPathLabel, gridBagConstraints);
     
     this.showFileChooserButton.setText("...");
     this.showFileChooserButton.setToolTipText("null");
     this.showFileChooserButton.setMargin(new Insets(0, 0, 0, 0));
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 3;
     gridBagConstraints.insets = new Insets(0, 0, 5, 5);
     this.pathPanel.add(this.showFileChooserButton, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 2;
     gridBagConstraints.weightx = 1.0D;
     add(this.pathPanel, gridBagConstraints);
     
     this.birdViewPanel.setLayout(new GridBagLayout());
     
     this.birdViewPanel.setBorder(new TitledBorder("Birdview"));
     this.birdViewDelayNameLabel.setText("Birdview Update Delay");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.insets = new Insets(0, 5, 5, 5);
     gridBagConstraints.anchor = 17;
     this.birdViewPanel.add(this.birdViewDelayNameLabel, gridBagConstraints);
     
     this.birdViewDelayTextField.setToolTipText("null");
     this.birdViewDelayTextField.setPreferredSize(new Dimension(10, 20));
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.ipadx = 30;
     gridBagConstraints.insets = new Insets(0, 0, 5, 5);
     this.birdViewPanel.add(this.birdViewDelayTextField, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     this.birdViewPanel.add(this.fillerPanel01, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.fill = 2;
     gridBagConstraints.weightx = 1.0D;
     add(this.birdViewPanel, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 2;
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.weighty = 1.0D;
     add(this.fillerPanel, gridBagConstraints);
   }
 
 
 
 
 
 
 
   
   public JLabel getBirdViewDelayNameLabel() {
     return this.birdViewDelayNameLabel;
   }
 
 
 
 
 
 
   
   public void setBirdViewDelayNameLabel(JLabel birdViewDelayNameLabel) {
     this.birdViewDelayNameLabel = birdViewDelayNameLabel;
   }
 
 
 
 
 
 
   
   public JTextField getBirdViewDelayTextField() {
     return this.birdViewDelayTextField;
   }
 
 
 
 
 
 
   
   public void setBirdViewDelayTextField(JTextField birdViewDelayTextField) {
     this.birdViewDelayTextField = birdViewDelayTextField;
   }
 
 
 
 
 
 
   
   public JPanel getBirdViewPanel() {
     return this.birdViewPanel;
   }
 
 
 
 
 
 
   
   public void setBirdViewPanel(JPanel birdViewPanel) {
     this.birdViewPanel = birdViewPanel;
   }
 
 
 
 
 
 
   
   public JLabel getDefaultPathLabel() {
     return this.defaultPathLabel;
   }
 
 
 
 
 
 
   
   public void setDefaultPathLabel(JLabel defaultPathLabel) {
     this.defaultPathLabel = defaultPathLabel;
   }
 
 
 
 
 
 
   
   public JLabel getDefaultPathNameLabel() {
     return this.defaultPathNameLabel;
   }
 
 
 
 
 
 
   
   public void setDefaultPathNameLabel(JLabel defaultPathNameLabel) {
     this.defaultPathNameLabel = defaultPathNameLabel;
   }
 
 
 
 
 
 
   
   public JPanel getFillerPanel() {
     return this.fillerPanel;
   }
 
 
 
 
 
 
   
   public void setFillerPanel(JPanel fillerPanel) {
     this.fillerPanel = fillerPanel;
   }
 
 
 
 
 
 
   
   public JPanel getFillerPanel01() {
     return this.fillerPanel01;
   }
 
 
 
 
 
 
   
   public void setFillerPanel01(JPanel fillerPanel01) {
     this.fillerPanel01 = fillerPanel01;
   }
 
 
 
 
 
 
   
   public JPanel getPathPanel() {
     return this.pathPanel;
   }
 
 
 
 
 
 
   
   public void setPathPanel(JPanel pathPanel) {
     this.pathPanel = pathPanel;
   }
 
 
 
 
 
 
   
   public JButton getShowFileChooserButton() {
     return this.showFileChooserButton;
   }
 
 
 
 
 
 
   
   public void setShowFileChooserButton(JButton showFileChooserButton) {
     this.showFileChooserButton = showFileChooserButton;
   }
 }


