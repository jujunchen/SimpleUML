 package net.trustx.simpleuml.classdiagram.configuration;
 
 import com.intellij.openapi.ui.DialogWrapper;
 import java.awt.BorderLayout;
 import java.awt.Component;
 import java.awt.GridLayout;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import javax.swing.BorderFactory;
 import javax.swing.Box;
 import javax.swing.ButtonGroup;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JComponent;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JRadioButton;
 import net.trustx.simpleuml.classdiagram.util.PsiComponentUtils;
 
 public class ModifierChooserDialog
   extends DialogWrapper
 {
   private int value;
   private Box contentPanel;
   private JPanel scopePanel;
   private JPanel modifierPanel;
   private JLabel valueLabel;
   private JRadioButton privateRadioButton;
   private JRadioButton packageRadioButton;
   private JRadioButton protectedRadioButton;
   private JRadioButton publicRadioButton;
   private JCheckBox finalCheckBox;
   private JCheckBox staticCheckBox;
   
   public ModifierChooserDialog(Component parent) {
     super(parent, false);
     this.value = 0;
     getContentPane().setLayout(new BorderLayout(5, 5));
     this.scopePanel = new JPanel(new GridLayout(5, 0));
     this.scopePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Scope"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
     this.modifierPanel = new JPanel(new GridLayout(5, 0));
     this.modifierPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Modifier"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
     this.contentPanel = Box.createHorizontalBox();
     
     this.valueLabel = new JLabel(" ");
     this.valueLabel.setBorder(BorderFactory.createTitledBorder("Filter"));
     getContentPane().add(this.valueLabel, "North");
     
     this.privateRadioButton = new JRadioButton("private");
     this.packageRadioButton = new JRadioButton("package");
     this.protectedRadioButton = new JRadioButton("protected");
     this.publicRadioButton = new JRadioButton("public");
     
     ButtonGroup visibilityGroup = new ButtonGroup();
     visibilityGroup.add(this.privateRadioButton);
     visibilityGroup.add(this.packageRadioButton);
     visibilityGroup.add(this.protectedRadioButton);
     visibilityGroup.add(this.publicRadioButton);
     
     this.privateRadioButton.setSelected(true);
     this.finalCheckBox = new JCheckBox("final");
     this.staticCheckBox = new JCheckBox("static");
     
     this.modifierPanel.add(this.staticCheckBox);
     this.modifierPanel.add(this.finalCheckBox);
     this.scopePanel.add(this.privateRadioButton);
     
     this.scopePanel.add(this.packageRadioButton);
     this.modifierPanel.add(Box.createHorizontalBox());
     this.scopePanel.add(this.protectedRadioButton);
     this.modifierPanel.add(Box.createHorizontalBox());
     this.scopePanel.add(this.publicRadioButton);
     this.modifierPanel.add(Box.createHorizontalBox());
     
     this.contentPanel.add(this.scopePanel);
     this.contentPanel.add(this.modifierPanel);
     getContentPane().add(this.contentPanel, "Center");
     
     JButton okButton = new JButton("Ok");
     JButton cancelButton = new JButton("Cancel");
 
     
     Box buttonBox = Box.createHorizontalBox();
     buttonBox.add(Box.createHorizontalGlue());
     buttonBox.add(okButton);
     buttonBox.add(Box.createHorizontalStrut(5));
     buttonBox.add(cancelButton);
     buttonBox.add(Box.createHorizontalGlue());
     buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
     
     getContentPane().add(buttonBox, "South");
     
     ActionListener updateActionListener = new ActionListener()
       {
         public void actionPerformed(ActionEvent e)
         {
           ModifierChooserDialog.this.updateValue();
         }
       };
     this.privateRadioButton.addActionListener(updateActionListener);
     this.protectedRadioButton.addActionListener(updateActionListener);
     this.publicRadioButton.addActionListener(updateActionListener);
     this.packageRadioButton.addActionListener(updateActionListener);
     
     this.finalCheckBox.addActionListener(updateActionListener);
     this.staticCheckBox.addActionListener(updateActionListener);
     
     updateValue();
     
     okButton.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             ModifierChooserDialog.this.dispose();
           }
         });
     
     cancelButton.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             ModifierChooserDialog.this.value = -1;
             ModifierChooserDialog.this.dispose();
           }
         });
   }
 
 
 
 
   
   private void updateValue() {
     this.value = 0;
     this.value |= this.privateRadioButton.isSelected() ? 2 : 0;
     this.value |= this.protectedRadioButton.isSelected() ? 4 : 0;
     this.value |= this.publicRadioButton.isSelected() ? 1 : 0;
     this.value |= this.packageRadioButton.isSelected() ? 4096 : 0;
     
     this.value |= this.finalCheckBox.isSelected() ? 16 : 0;
     this.value |= this.staticCheckBox.isSelected() ? 8 : 0;
     
     updateLabel();
   }
 
 
   
   private void updateLabel() {
     this.valueLabel.setText(PsiComponentUtils.convertModifierIntToString(this.value) + " ");
   }
 
 
   
   public int getModifier() {
     return this.value;
   }
 
 
   
   protected JComponent createCenterPanel() {
     return this.contentPanel;
   }
 }


