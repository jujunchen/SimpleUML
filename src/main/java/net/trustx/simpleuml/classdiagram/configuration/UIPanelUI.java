 package net.trustx.simpleuml.classdiagram.configuration;
 
 import java.awt.Dimension;
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Insets;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JComboBox;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JTextField;
 import javax.swing.border.TitledBorder;
 

 
 
 public class UIPanelUI
   extends JPanel
 {
   private JLabel heightLabel;
   private JTextField widthTextField;
   private JLabel contentFontSizeLabel;
   private JLabel titleFontLabel;
   private JLabel minimumClassSizeLabel;
   private JTextField heightTextField;
   
   public UIPanelUI() {
     initComponents();
   }
   private JButton abstractClassColorButton; private JPanel colorsPanel; private JButton classColorButton; private JButton interfaceColorButton; private JLabel titleFontSampleTextLabel; private JLabel contentFontSampleTextLabel; private JComboBox titleFontSizeComboBox;
   private JPanel fillerPanel;
   private JCheckBox antialiasingConnectorsCheckBox;
   private JLabel titleFontSizeLabel;
   private JComboBox contentFontNameComboBox;
   private JPanel sizePanel;
   private JPanel fillerPanel02;
   
   private void initComponents() {
     this.fontsPanel = new JPanel();
     this.titleFontLabel = new JLabel();
     this.titleFontNameComboBox = new JComboBox();
     this.titleFontSizeLabel = new JLabel();
     this.titleFontSizeComboBox = new JComboBox();
     this.titleFontSampleTextLabel = new JLabel();
     this.contentFontLabel = new JLabel();
     this.contentFontNameComboBox = new JComboBox();
     this.contentFontSizeLabel = new JLabel();
     this.contentFontSizeComboBox = new JComboBox();
     this.contentFontSampleTextLabel = new JLabel();
     this.connectorsPanel = new JPanel();
     this.antialiasingConnectorsCheckBox = new JCheckBox();
     this.colorsPanel = new JPanel();
     this.interfaceColorButton = new JButton();
     this.abstractClassColorButton = new JButton();
     this.classColorButton = new JButton();
     this.diagramBackgroundColorButton = new JButton();
     this.fillerPanel = new JPanel();
     this.sizePanel = new JPanel();
     this.minimumClassSizeLabel = new JLabel();
     this.widthTextField = new JTextField();
     this.withLabel = new JLabel();
     this.heightLabel = new JLabel();
     this.heightTextField = new JTextField();
     this.fillerPanel02 = new JPanel();
     
     setLayout(new GridBagLayout());
     
     this.fontsPanel.setLayout(new GridBagLayout());
     
     this.fontsPanel.setBorder(new TitledBorder("Fonts"));
     this.titleFontLabel.setText("Classname");
     GridBagConstraints gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.anchor = 13;
     gridBagConstraints.insets = new Insets(0, 5, 5, 5);
     this.fontsPanel.add(this.titleFontLabel, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 2;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.insets = new Insets(0, 0, 5, 5);
     this.fontsPanel.add(this.titleFontNameComboBox, gridBagConstraints);
     
     this.titleFontSizeLabel.setText("Size");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.insets = new Insets(0, 5, 5, 5);
     this.fontsPanel.add(this.titleFontSizeLabel, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.insets = new Insets(0, 0, 5, 5);
     this.fontsPanel.add(this.titleFontSizeComboBox, gridBagConstraints);
     
     this.titleFontSampleTextLabel.setText("Sample Text");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.insets = new Insets(0, 5, 5, 5);
     this.fontsPanel.add(this.titleFontSampleTextLabel, gridBagConstraints);
     
     this.contentFontLabel.setText("Members");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.anchor = 13;
     gridBagConstraints.insets = new Insets(0, 5, 0, 5);
     this.fontsPanel.add(this.contentFontLabel, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 1;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.fill = 2;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.insets = new Insets(0, 0, 0, 5);
     this.fontsPanel.add(this.contentFontNameComboBox, gridBagConstraints);
     
     this.contentFontSizeLabel.setText("Size");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 2;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.insets = new Insets(0, 5, 0, 5);
     this.fontsPanel.add(this.contentFontSizeLabel, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 3;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.insets = new Insets(0, 0, 0, 5);
     this.fontsPanel.add(this.contentFontSizeComboBox, gridBagConstraints);
     
     this.contentFontSampleTextLabel.setText("Sample Text");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 4;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.insets = new Insets(0, 5, 0, 5);
     this.fontsPanel.add(this.contentFontSampleTextLabel, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     add(this.fontsPanel, gridBagConstraints);
     
     this.connectorsPanel.setLayout(new GridBagLayout());
     
     this.connectorsPanel.setBorder(new TitledBorder("Connectors"));
     this.antialiasingConnectorsCheckBox.setText("use antialiasing");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.connectorsPanel.add(this.antialiasingConnectorsCheckBox, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     add(this.connectorsPanel, gridBagConstraints);
     
     this.colorsPanel.setLayout(new GridBagLayout());
     
     this.colorsPanel.setBorder(new TitledBorder("Colors"));
     this.interfaceColorButton.setText("Interface");
     this.interfaceColorButton.setPreferredSize(new Dimension(10, 26));
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 2;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.insets = new Insets(0, 5, 5, 0);
     this.colorsPanel.add(this.interfaceColorButton, gridBagConstraints);
     
     this.abstractClassColorButton.setText("Abstract class");
     this.abstractClassColorButton.setPreferredSize(new Dimension(10, 26));
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 2;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.insets = new Insets(0, 5, 5, 0);
     this.colorsPanel.add(this.abstractClassColorButton, gridBagConstraints);
     
     this.classColorButton.setText("Class");
     this.classColorButton.setPreferredSize(new Dimension(10, 26));
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 2;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.insets = new Insets(0, 5, 5, 5);
     this.colorsPanel.add(this.classColorButton, gridBagConstraints);
     
     this.diagramBackgroundColorButton.setText("Diagram background");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.gridwidth = 0;
     gridBagConstraints.fill = 2;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.insets = new Insets(0, 5, 0, 5);
     this.colorsPanel.add(this.diagramBackgroundColorButton, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 2;
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     add(this.colorsPanel, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 4;
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.weighty = 1.0D;
     add(this.fillerPanel, gridBagConstraints);
     
     this.sizePanel.setLayout(new GridBagLayout());
     
     this.sizePanel.setBorder(new TitledBorder("Size"));
     this.minimumClassSizeLabel.setText("Minimum Class Size");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.insets = new Insets(0, 5, 5, 20);
     gridBagConstraints.anchor = 17;
     this.sizePanel.add(this.minimumClassSizeLabel, gridBagConstraints);
     
     this.widthTextField.setPreferredSize(new Dimension(10, 20));
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 2;
     gridBagConstraints.gridy = 0;
     gridBagConstraints.fill = 2;
     gridBagConstraints.ipadx = 30;
     gridBagConstraints.insets = new Insets(0, 0, 5, 10);
     this.sizePanel.add(this.widthTextField, gridBagConstraints);
     
     this.withLabel.setText(" Width");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 1;
     gridBagConstraints.gridy = 0;
     gridBagConstraints.insets = new Insets(0, 0, 5, 5);
     this.sizePanel.add(this.withLabel, gridBagConstraints);
     
     this.heightLabel.setText("Height");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 3;
     gridBagConstraints.gridy = 0;
     gridBagConstraints.insets = new Insets(0, 0, 5, 5);
     this.sizePanel.add(this.heightLabel, gridBagConstraints);
     
     this.heightTextField.setPreferredSize(new Dimension(10, 20));
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 4;
     gridBagConstraints.gridy = 0;
     gridBagConstraints.fill = 2;
     gridBagConstraints.ipadx = 30;
     gridBagConstraints.insets = new Insets(0, 0, 5, 5);
     this.sizePanel.add(this.heightTextField, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 1;
     gridBagConstraints.insets = new Insets(0, 0, 5, 5);
     gridBagConstraints.weightx = 1.0D;
     this.sizePanel.add(this.fillerPanel02, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 3;
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     add(this.sizePanel, gridBagConstraints);
   }
 
   
   private JComboBox titleFontNameComboBox;
   private JComboBox contentFontSizeComboBox;
   private JLabel contentFontLabel;
   
   public JButton getAbstractClassColorButton() {
     return this.abstractClassColorButton;
   }
   private JPanel fontsPanel;
   private JLabel withLabel;
   private JPanel connectorsPanel;
   private JButton diagramBackgroundColorButton;
   
   public void setAbstractClassColorButton(JButton abstractClassColorButton) {
     this.abstractClassColorButton = abstractClassColorButton;
   }
 
 
 
 
   
   public JCheckBox getAntialiasingConnectorsCheckBox() {
     return this.antialiasingConnectorsCheckBox;
   }
 
 
 
 
   
   public void setAntialiasingConnectorsCheckBox(JCheckBox antialiasingConnectorsCheckBox) {
     this.antialiasingConnectorsCheckBox = antialiasingConnectorsCheckBox;
   }
 
 
 
 
   
   public JButton getClassColorButton() {
     return this.classColorButton;
   }
 
 
 
 
   
   public void setClassColorButton(JButton classColorButton) {
     this.classColorButton = classColorButton;
   }
 
 
 
 
   
   public JPanel getColorsPanel() {
     return this.colorsPanel;
   }
 
 
 
 
   
   public void setColorsPanel(JPanel colorsPanel) {
     this.colorsPanel = colorsPanel;
   }
 
 
 
 
   
   public JPanel getConnectorsPanel() {
     return this.connectorsPanel;
   }
 
 
 
 
   
   public void setConnectorsPanel(JPanel connectorsPanel) {
     this.connectorsPanel = connectorsPanel;
   }
 
 
 
 
   
   public JLabel getContentFontLabel() {
     return this.contentFontLabel;
   }
 
 
 
 
   
   public void setContentFontLabel(JLabel contentFontLabel) {
     this.contentFontLabel = contentFontLabel;
   }
 
 
 
 
   
   public JComboBox getContentFontNameComboBox() {
     return this.contentFontNameComboBox;
   }
 
 
 
 
   
   public void setContentFontNameComboBox(JComboBox contentFontNameComboBox) {
     this.contentFontNameComboBox = contentFontNameComboBox;
   }
 
 
 
 
   
   public JLabel getContentFontSampleTextLabel() {
     return this.contentFontSampleTextLabel;
   }
 
 
 
 
   
   public void setContentFontSampleTextLabel(JLabel contentFontSampleTextLabel) {
     this.contentFontSampleTextLabel = contentFontSampleTextLabel;
   }
 
 
 
 
   
   public JComboBox getContentFontSizeComboBox() {
     return this.contentFontSizeComboBox;
   }
 
 
 
 
   
   public void setContentFontSizeComboBox(JComboBox contentFontSizeComboBox) {
     this.contentFontSizeComboBox = contentFontSizeComboBox;
   }
 
 
 
 
   
   public JLabel getContentFontSizeLabel() {
     return this.contentFontSizeLabel;
   }
 
 
 
 
   
   public void setContentFontSizeLabel(JLabel contentFontSizeLabel) {
     this.contentFontSizeLabel = contentFontSizeLabel;
   }
 
 
 
 
   
   public JButton getDiagramBackgroundColorButton() {
     return this.diagramBackgroundColorButton;
   }
 
 
 
 
   
   public void setDiagramBackgroundColorButton(JButton diagramBackgroundColorButton) {
     this.diagramBackgroundColorButton = diagramBackgroundColorButton;
   }
 
 
 
 
   
   public JPanel getFillerPanel() {
     return this.fillerPanel;
   }
 
 
 
 
   
   public void setFillerPanel(JPanel fillerPanel) {
     this.fillerPanel = fillerPanel;
   }
 
 
 
 
   
   public JPanel getFontsPanel() {
     return this.fontsPanel;
   }
 
 
 
 
   
   public void setFontsPanel(JPanel fontsPanel) {
     this.fontsPanel = fontsPanel;
   }
 
 
 
 
   
   public JButton getInterfaceColorButton() {
     return this.interfaceColorButton;
   }
 
 
 
 
   
   public void setInterfaceColorButton(JButton interfaceColorButton) {
     this.interfaceColorButton = interfaceColorButton;
   }
 
 
 
 
   
   public JLabel getTitleFontLabel() {
     return this.titleFontLabel;
   }
 
 
 
 
   
   public void setTitleFontLabel(JLabel titleFontLabel) {
     this.titleFontLabel = titleFontLabel;
   }
 
 
 
 
   
   public JComboBox getTitleFontNameComboBox() {
     return this.titleFontNameComboBox;
   }
 
 
 
 
   
   public void setTitleFontNameComboBox(JComboBox titleFontNameComboBox) {
     this.titleFontNameComboBox = titleFontNameComboBox;
   }
 
 
 
 
   
   public JLabel getTitleFontSampleTextLabel() {
     return this.titleFontSampleTextLabel;
   }
 
 
 
 
   
   public void setTitleFontSampleTextLabel(JLabel titleFontSampleTextLabel) {
     this.titleFontSampleTextLabel = titleFontSampleTextLabel;
   }
 
 
 
 
   
   public JComboBox getTitleFontSizeComboBox() {
     return this.titleFontSizeComboBox;
   }
 
 
 
 
   
   public void setTitleFontSizeComboBox(JComboBox titleFontSizeComboBox) {
     this.titleFontSizeComboBox = titleFontSizeComboBox;
   }
 
 
 
 
   
   public JLabel getTitleFontSizeLabel() {
     return this.titleFontSizeLabel;
   }
 
 
 
 
   
   public void setTitleFontSizeLabel(JLabel titleFontSizeLabel) {
     this.titleFontSizeLabel = titleFontSizeLabel;
   }
 
 
 
 
   
   public JPanel getFillerPanel02() {
     return this.fillerPanel02;
   }
 
 
 
 
   
   public void setFillerPanel02(JPanel fillerPanel02) {
     this.fillerPanel02 = fillerPanel02;
   }
 
 
 
 
   
   public JLabel getHeightLabel() {
     return this.heightLabel;
   }
 
 
 
 
   
   public void setHeightLabel(JLabel heightLabel) {
     this.heightLabel = heightLabel;
   }
 
 
 
 
   
   public JTextField getHeightTextField() {
     return this.heightTextField;
   }
 
 
 
 
   
   public void setHeightTextField(JTextField heightTextField) {
     this.heightTextField = heightTextField;
   }
 
 
 
 
   
   public JLabel getMinimumClassSizeLabel() {
     return this.minimumClassSizeLabel;
   }
 
 
 
 
   
   public void setMinimumClassSizeLabel(JLabel minimumClassSizeLabel) {
     this.minimumClassSizeLabel = minimumClassSizeLabel;
   }
 
 
 
 
   
   public JPanel getSizePanel() {
     return this.sizePanel;
   }
 
 
 
 
   
   public void setSizePanel(JPanel sizePanel) {
     this.sizePanel = sizePanel;
   }
 
 
 
 
   
   public JTextField getWidthTextField() {
     return this.widthTextField;
   }
 
 
 
 
   
   public void setWidthTextField(JTextField widthTextField) {
     this.widthTextField = widthTextField;
   }
 
 
 
 
   
   public JLabel getWithLabel() {
     return this.withLabel;
   }
 
 
 
 
   
   public void setWithLabel(JLabel withLabel) {
     this.withLabel = withLabel;
   }
 }


