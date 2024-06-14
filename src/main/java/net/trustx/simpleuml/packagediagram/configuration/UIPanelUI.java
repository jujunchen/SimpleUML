 package net.trustx.simpleuml.packagediagram.configuration;
 
 import java.awt.Dimension;
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Insets;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JComboBox;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JSpinner;
 import javax.swing.border.TitledBorder;
 
 public class UIPanelUI
   extends JPanel {
   private JCheckBox antialiasingConnectorsCheckBox;
   private JPanel colorsPanel;
   private JPanel connectorsPanel;
   private JLabel contentFontLabel;
   private JComboBox contentFontNameComboBox;
   private JLabel contentFontSampleTextLabel;
   private JComboBox contentFontSizeComboBox;
   private JLabel contentFontSizeLabel;
   private JButton diagramBackgroundColorButton;
   private JPanel fillerPanel;
   private JPanel fillerPanel02;
   
   public UIPanelUI() {
     initComponents();
   }
 
   
   private JPanel fontsPanel;
   private JButton packageColorButton;
   private JLabel packageNameCompressionLevelLabel;
   private JLabel packageNameCompressionLevelSampleLabel;
   private JSpinner packageNameCompressionLevelSpinner;
   
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
     this.packageColorButton = new JButton();
     this.diagramBackgroundColorButton = new JButton();
     this.fillerPanel = new JPanel();
     this.sizePanel = new JPanel();
     this.packageNameCompressionLevelLabel = new JLabel();
     this.fillerPanel02 = new JPanel();
     this.packageNameCompressionLevelSpinner = new JSpinner();
     this.packageNameCompressionLevelSampleLabel = new JLabel();
     
     setLayout(new GridBagLayout());
     
     this.fontsPanel.setLayout(new GridBagLayout());
     
     this.fontsPanel.setBorder(new TitledBorder("Fonts"));
     this.titleFontLabel.setText("Title");
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
     
     this.contentFontLabel.setText("Text");
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
     this.packageColorButton.setText("Package background");
     this.packageColorButton.setPreferredSize(new Dimension(10, 26));
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 2;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.insets = new Insets(0, 5, 5, 5);
     this.colorsPanel.add(this.packageColorButton, gridBagConstraints);
     
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
     
     this.sizePanel.setBorder(new TitledBorder("Package Name"));
     this.packageNameCompressionLevelLabel.setText("Compression Level");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.anchor = 17;
     gridBagConstraints.insets = new Insets(0, 5, 5, 20);
     this.sizePanel.add(this.packageNameCompressionLevelLabel, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 3;
     gridBagConstraints.gridy = 0;
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.insets = new Insets(0, 0, 5, 5);
     this.sizePanel.add(this.fillerPanel02, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 1;
     gridBagConstraints.gridy = 0;
     gridBagConstraints.insets = new Insets(0, 5, 5, 20);
     this.sizePanel.add(this.packageNameCompressionLevelSpinner, gridBagConstraints);
     
     this.packageNameCompressionLevelSampleLabel.setText("net.trustx.simpleuml.test");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.insets = new Insets(0, 5, 5, 20);
     this.sizePanel.add(this.packageNameCompressionLevelSampleLabel, gridBagConstraints);
     
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 3;
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     add(this.sizePanel, gridBagConstraints);
   }
 
   
   private JPanel sizePanel;
   private JLabel titleFontLabel;
   private JComboBox titleFontNameComboBox;
   
   public JCheckBox getAntialiasingConnectorsCheckBox() {
     return this.antialiasingConnectorsCheckBox;
   }
   
   private JLabel titleFontSampleTextLabel;
   private JComboBox titleFontSizeComboBox;
   private JLabel titleFontSizeLabel;
   
   public void setAntialiasingConnectorsCheckBox(JCheckBox antialiasingConnectorsCheckBox) {
     this.antialiasingConnectorsCheckBox = antialiasingConnectorsCheckBox;
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
 
 
 
 
   
   public JPanel getFillerPanel02() {
     return this.fillerPanel02;
   }
 
 
 
 
   
   public void setFillerPanel02(JPanel fillerPanel02) {
     this.fillerPanel02 = fillerPanel02;
   }
 
 
 
 
   
   public JPanel getFontsPanel() {
     return this.fontsPanel;
   }
 
 
 
 
   
   public void setFontsPanel(JPanel fontsPanel) {
     this.fontsPanel = fontsPanel;
   }
 
 
 
 
   
   public JButton getPackageColorButton() {
     return this.packageColorButton;
   }
 
 
 
 
   
   public void setPackageColorButton(JButton packageColorButton) {
     this.packageColorButton = packageColorButton;
   }
 
 
 
 
   
   public JLabel getPackageNameCompressionLevelLabel() {
     return this.packageNameCompressionLevelLabel;
   }
 
 
 
 
   
   public void setPackageNameCompressionLevelLabel(JLabel packageNameCompressionLevelLabel) {
     this.packageNameCompressionLevelLabel = packageNameCompressionLevelLabel;
   }
 
 
 
 
   
   public JLabel getPackageNameCompressionLevelSampleLabel() {
     return this.packageNameCompressionLevelSampleLabel;
   }
 
 
 
 
   
   public void setPackageNameCompressionLevelSampleLabel(JLabel packageNameCompressionLevelSampleLabel) {
     this.packageNameCompressionLevelSampleLabel = packageNameCompressionLevelSampleLabel;
   }
 
 
 
 
   
   public JSpinner getPackageNameCompressionLevelSpinner() {
     return this.packageNameCompressionLevelSpinner;
   }
 
 
 
 
   
   public void setPackageNameCompressionLevelSpinner(JSpinner packageNameCompressionLevelSpinner) {
     this.packageNameCompressionLevelSpinner = packageNameCompressionLevelSpinner;
   }
 
 
 
 
   
   public JPanel getSizePanel() {
     return this.sizePanel;
   }
 
 
 
 
   
   public void setSizePanel(JPanel sizePanel) {
     this.sizePanel = sizePanel;
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
 }


