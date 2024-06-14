 package net.trustx.simpleuml.classdiagram.configuration;

 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.GridLayout;
 import javax.swing.ButtonGroup;
 import javax.swing.JCheckBox;
 import javax.swing.JPanel;
 import javax.swing.JRadioButton;
 import javax.swing.border.TitledBorder;





 public class ClassesPanelUI
   extends JPanel
 {
   private JPanel helperPanel01;
   private JPanel compartmentsPanel;
   private JCheckBox methodsExpandedBox;
   private JRadioButton implementsHideConditionalRadioButton;
   private JPanel implementsPanel;
   private JRadioButton extendsHideConditionalRadioButton;

   public ClassesPanelUI() {
     initComponents();
   }
   private JCheckBox constructorsExpandedBox; private ButtonGroup compartmentsButtonGroup; private JCheckBox showParamtersBox; private JRadioButton compartmentHideRadioButton; private JRadioButton compartmentShowRadioButton; private ButtonGroup extendsButtonGroup; private JPanel fillerPanel01; private JRadioButton extendsHideRadioButton;
   private JPanel defaultPanel;
   private JCheckBox fieldsExpandedBox;
   private JPanel fillerPanel;
   private JRadioButton extendsShowRadioButton;
   private JPanel jPanel8;
   private JPanel globalPanel;

   private void initComponents() {
     this.compartmentsButtonGroup = new ButtonGroup();
     this.extendsButtonGroup = new ButtonGroup();
     this.implementsButtonGroup = new ButtonGroup();
     this.helperPanel01 = new JPanel();
     this.defaultPanel = new JPanel();
     this.fieldsExpandedBox = new JCheckBox();
     this.constructorsExpandedBox = new JCheckBox();
     this.methodsExpandedBox = new JCheckBox();
     this.jPanel8 = new JPanel();
     this.globalPanel = new JPanel();
     this.showParamtersBox = new JCheckBox();
     this.showLongModifiersBox = new JCheckBox();
     this.fillerPanel01 = new JPanel();
     this.helperPanel02 = new JPanel();
     this.compartmentsPanel = new JPanel();
     this.compartmentShowRadioButton = new JRadioButton();
     this.compartmentHideConditionalRadioButton = new JRadioButton();
     this.compartmentHideRadioButton = new JRadioButton();
     this.extendsPanel = new JPanel();
     this.extendsShowRadioButton = new JRadioButton();
     this.extendsHideConditionalRadioButton = new JRadioButton();
     this.extendsHideRadioButton = new JRadioButton();
     this.implementsPanel = new JPanel();
     this.implementsShowRadioButton = new JRadioButton();
     this.implementsHideConditionalRadioButton = new JRadioButton();
     this.implementsHideRadioButton = new JRadioButton();
     this.fillerPanel = new JPanel();

     setLayout(new GridBagLayout());

     this.helperPanel01.setLayout(new GridLayout(1, 0));

     this.defaultPanel.setLayout(new GridBagLayout());

     this.defaultPanel.setBorder(new TitledBorder("Default"));
     this.fieldsExpandedBox.setText("Fields expanded");
     GridBagConstraints gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.defaultPanel.add(this.fieldsExpandedBox, gridBagConstraints);

     this.constructorsExpandedBox.setText("Constructors expanded");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.defaultPanel.add(this.constructorsExpandedBox, gridBagConstraints);

     this.methodsExpandedBox.setText("Methods expanded");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 2;
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.defaultPanel.add(this.methodsExpandedBox, gridBagConstraints);

     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 3;
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.weighty = 1.0D;
     this.defaultPanel.add(this.jPanel8, gridBagConstraints);

     this.helperPanel01.add(this.defaultPanel);

     this.globalPanel.setLayout(new GridBagLayout());

     this.globalPanel.setBorder(new TitledBorder("Global"));
     this.showParamtersBox.setText("Show Parameters");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.globalPanel.add(this.showParamtersBox, gridBagConstraints);

     this.showLongModifiersBox.setText("Long Modifiers");
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.globalPanel.add(this.showLongModifiersBox, gridBagConstraints);

     this.fillerPanel01.setLayout(new GridBagLayout());

     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 2;
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.weighty = 1.0D;
     this.globalPanel.add(this.fillerPanel01, gridBagConstraints);

     this.helperPanel01.add(this.globalPanel);

     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.fill = 2;
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     add(this.helperPanel01, gridBagConstraints);

     this.helperPanel02.setLayout(new GridLayout(1, 0));

     this.compartmentsPanel.setLayout(new GridBagLayout());

     this.compartmentsPanel.setBorder(new TitledBorder("Compartments"));
     this.compartmentShowRadioButton.setText("Show");
     this.compartmentsButtonGroup.add(this.compartmentShowRadioButton);
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.compartmentsPanel.add(this.compartmentShowRadioButton, gridBagConstraints);

     this.compartmentHideConditionalRadioButton.setText("Hide Empty");
     this.compartmentsButtonGroup.add(this.compartmentHideConditionalRadioButton);
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.compartmentsPanel.add(this.compartmentHideConditionalRadioButton, gridBagConstraints);

     this.compartmentHideRadioButton.setText("Hide");
     this.compartmentsButtonGroup.add(this.compartmentHideRadioButton);
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 2;
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.compartmentsPanel.add(this.compartmentHideRadioButton, gridBagConstraints);

     this.helperPanel02.add(this.compartmentsPanel);

     this.extendsPanel.setLayout(new GridBagLayout());

     this.extendsPanel.setBorder(new TitledBorder("Extends"));
     this.extendsShowRadioButton.setText("Show");
     this.extendsButtonGroup.add(this.extendsShowRadioButton);
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.extendsPanel.add(this.extendsShowRadioButton, gridBagConstraints);

     this.extendsHideConditionalRadioButton.setText("Automatic");
     this.extendsButtonGroup.add(this.extendsHideConditionalRadioButton);
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.extendsPanel.add(this.extendsHideConditionalRadioButton, gridBagConstraints);

     this.extendsHideRadioButton.setText("Hide");
     this.extendsButtonGroup.add(this.extendsHideRadioButton);
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 2;
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.extendsPanel.add(this.extendsHideRadioButton, gridBagConstraints);

     this.helperPanel02.add(this.extendsPanel);

     this.implementsPanel.setLayout(new GridBagLayout());

     this.implementsPanel.setBorder(new TitledBorder("Implements"));
     this.implementsShowRadioButton.setText("Show");
     this.implementsButtonGroup.add(this.implementsShowRadioButton);
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.implementsPanel.add(this.implementsShowRadioButton, gridBagConstraints);

     this.implementsHideConditionalRadioButton.setText("Automatic");
     this.implementsButtonGroup.add(this.implementsHideConditionalRadioButton);
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.implementsPanel.add(this.implementsHideConditionalRadioButton, gridBagConstraints);

     this.implementsHideRadioButton.setText("Hide");
     this.implementsButtonGroup.add(this.implementsHideRadioButton);
     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 2;
     gridBagConstraints.anchor = 17;
     gridBagConstraints.weightx = 1.0D;
     this.implementsPanel.add(this.implementsHideRadioButton, gridBagConstraints);

     this.helperPanel02.add(this.implementsPanel);

     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 1;
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     add(this.helperPanel02, gridBagConstraints);

     gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.gridx = 0;
     gridBagConstraints.gridy = 2;
     gridBagConstraints.fill = 1;
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.weighty = 1.0D;
     add(this.fillerPanel, gridBagConstraints);
   }


   private ButtonGroup implementsButtonGroup;
   private JRadioButton compartmentHideConditionalRadioButton;
   private JPanel helperPanel02;

   public JRadioButton getCompartmentHideConditionalRadioButton() {
     return this.compartmentHideConditionalRadioButton;
   }
   private JPanel extendsPanel;
   private JCheckBox showLongModifiersBox;
   private JRadioButton implementsShowRadioButton;
   private JRadioButton implementsHideRadioButton;

   public void setCompartmentHideConditionalRadioButton(JRadioButton compartmentHideConditionalRadioButton) {
     this.compartmentHideConditionalRadioButton = compartmentHideConditionalRadioButton;
   }





   public JRadioButton getCompartmentHideRadioButton() {
     return this.compartmentHideRadioButton;
   }





   public void setCompartmentHideRadioButton(JRadioButton compartmentHideRadioButton) {
     this.compartmentHideRadioButton = compartmentHideRadioButton;
   }





   public ButtonGroup getCompartmentsButtonGroup() {
     return this.compartmentsButtonGroup;
   }





   public void setCompartmentsButtonGroup(ButtonGroup compartmentsButtonGroup) {
     this.compartmentsButtonGroup = compartmentsButtonGroup;
   }





   public JRadioButton getCompartmentShowRadioButton() {
     return this.compartmentShowRadioButton;
   }





   public void setCompartmentShowRadioButton(JRadioButton compartmentShowRadioButton) {
     this.compartmentShowRadioButton = compartmentShowRadioButton;
   }





   public JPanel getCompartmentsPanel() {
     return this.compartmentsPanel;
   }





   public void setCompartmentsPanel(JPanel compartmentsPanel) {
     this.compartmentsPanel = compartmentsPanel;
   }





   public JCheckBox getConstructorsExpandedBox() {
     return this.constructorsExpandedBox;
   }





   public void setConstructorsExpandedBox(JCheckBox constructorsExpandedBox) {
     this.constructorsExpandedBox = constructorsExpandedBox;
   }





   public JPanel getDefaultPanel() {
     return this.defaultPanel;
   }





   public void setDefaultPanel(JPanel defaultPanel) {
     this.defaultPanel = defaultPanel;
   }





   public ButtonGroup getExtendsButtonGroup() {
     return this.extendsButtonGroup;
   }





   public void setExtendsButtonGroup(ButtonGroup extendsButtonGroup) {
     this.extendsButtonGroup = extendsButtonGroup;
   }





   public JRadioButton getExtendsHideConditionalRadioButton() {
     return this.extendsHideConditionalRadioButton;
   }





   public void setExtendsHideConditionalRadioButton(JRadioButton extendsHideConditionalRadioButton) {
     this.extendsHideConditionalRadioButton = extendsHideConditionalRadioButton;
   }





   public JRadioButton getExtendsHideRadioButton() {
     return this.extendsHideRadioButton;
   }





   public void setExtendsHideRadioButton(JRadioButton extendsHideRadioButton) {
     this.extendsHideRadioButton = extendsHideRadioButton;
   }





   public JPanel getExtendsPanel() {
     return this.extendsPanel;
   }





   public void setExtendsPanel(JPanel extendsPanel) {
     this.extendsPanel = extendsPanel;
   }





   public JRadioButton getExtendsShowRadioButton() {
     return this.extendsShowRadioButton;
   }





   public void setExtendsShowRadioButton(JRadioButton extendsShowRadioButton) {
     this.extendsShowRadioButton = extendsShowRadioButton;
   }





   public JCheckBox getFieldsExpandedBox() {
     return this.fieldsExpandedBox;
   }





   public void setFieldsExpandedBox(JCheckBox fieldsExpandedBox) {
     this.fieldsExpandedBox = fieldsExpandedBox;
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





   public JPanel getGlobalPanel() {
     return this.globalPanel;
   }





   public void setGlobalPanel(JPanel globalPanel) {
     this.globalPanel = globalPanel;
   }





   public JPanel getHelperPanel01() {
     return this.helperPanel01;
   }





   public void setHelperPanel01(JPanel helperPanel01) {
     this.helperPanel01 = helperPanel01;
   }





   public JPanel getHelperPanel02() {
     return this.helperPanel02;
   }





   public void setHelperPanel02(JPanel helperPanel02) {
     this.helperPanel02 = helperPanel02;
   }





   public ButtonGroup getImplementsButtonGroup() {
     return this.implementsButtonGroup;
   }





   public void setImplementsButtonGroup(ButtonGroup implementsButtonGroup) {
     this.implementsButtonGroup = implementsButtonGroup;
   }





   public JRadioButton getImplementsHideConditionalRadioButton() {
     return this.implementsHideConditionalRadioButton;
   }





   public void setImplementsHideConditionalRadioButton(JRadioButton implementsHideConditionalRadioButton) {
     this.implementsHideConditionalRadioButton = implementsHideConditionalRadioButton;
   }





   public JRadioButton getImplementsHideRadioButton() {
     return this.implementsHideRadioButton;
   }





   public void setImplementsHideRadioButton(JRadioButton implementsHideRadioButton) {
     this.implementsHideRadioButton = implementsHideRadioButton;
   }





   public JPanel getImplementsPanel() {
     return this.implementsPanel;
   }





   public void setImplementsPanel(JPanel implementsPanel) {
     this.implementsPanel = implementsPanel;
   }





   public JRadioButton getImplementsShowRadioButton() {
     return this.implementsShowRadioButton;
   }





   public void setImplementsShowRadioButton(JRadioButton implementsShowRadioButton) {
     this.implementsShowRadioButton = implementsShowRadioButton;
   }





   public JPanel getJPanel8() {
     return this.jPanel8;
   }





   public void setJPanel8(JPanel jPanel8) {
     this.jPanel8 = jPanel8;
   }





   public JCheckBox getMethodsExpandedBox() {
     return this.methodsExpandedBox;
   }





   public void setMethodsExpandedBox(JCheckBox methodsExpandedBox) {
     this.methodsExpandedBox = methodsExpandedBox;
   }





   public JCheckBox getShowLongModifiersBox() {
     return this.showLongModifiersBox;
   }





   public void setShowLongModifiersBox(JCheckBox showLongModifiersBox) {
     this.showLongModifiersBox = showLongModifiersBox;
   }





   public JCheckBox getShowParamtersBox() {
     return this.showParamtersBox;
   }





   public void setShowParamtersBox(JCheckBox showParamtersBox) {
     this.showParamtersBox = showParamtersBox;
   }
 }


