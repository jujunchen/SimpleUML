 package net.trustx.simpleuml.packagediagram.configuration;

 import java.awt.Color;
 import java.awt.Font;
 import java.awt.GraphicsEnvironment;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.ItemEvent;
 import java.awt.event.ItemListener;
 import javax.swing.DefaultComboBoxModel;
 import javax.swing.JButton;
 import javax.swing.JColorChooser;
 import javax.swing.SpinnerNumberModel;
 import javax.swing.border.Border;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 import net.trustx.simpleuml.util.UMLUtils;




 public class UIPanel
   extends UIPanelUI
 {
   private boolean initialised = false;
   private SpinnerNumberModel spinnerNumberModel;

   public UIPanel() {
     String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

     DefaultComboBoxModel<String> titleFontModel = new DefaultComboBoxModel<String>(fontNames);
     getTitleFontNameComboBox().setModel(titleFontModel);
     getTitleFontNameComboBox().addItemListener(new ItemListener()
         {
           public void itemStateChanged(ItemEvent e)
           {
             Font font = UIPanel.this.getTitleFontOnUI();
             UIPanel.this.getTitleFontSampleTextLabel().setFont(font);
           }
         });
     getTitleFontSizeComboBox().addItemListener(new ItemListener()
         {
           public void itemStateChanged(ItemEvent e)
           {
             Font font = UIPanel.this.getTitleFontOnUI();
             UIPanel.this.getTitleFontSampleTextLabel().setFont(font);
           }
         });

     getTitleFontSizeComboBox().setModel(new DefaultComboBoxModel<String>(PackageDiagramSettings.FONT_SIZES));

     DefaultComboBoxModel<String> contentFontModel = new DefaultComboBoxModel<String>(fontNames);
     getContentFontNameComboBox().setModel(contentFontModel);

     getContentFontSizeComboBox().setModel(new DefaultComboBoxModel<String>(PackageDiagramSettings.FONT_SIZES));
     getContentFontNameComboBox().addItemListener(new ItemListener()
         {
           public void itemStateChanged(ItemEvent e)
           {
             Font font = UIPanel.this.getContentFontOnUI();
             UIPanel.this.getContentFontSampleTextLabel().setFont(font);
           }
         });
     getContentFontSizeComboBox().addItemListener(new ItemListener()
         {
           public void itemStateChanged(ItemEvent e)
           {
             Font font = UIPanel.this.getContentFontOnUI();
             UIPanel.this.getContentFontSampleTextLabel().setFont(font);
           }
         });

     addColorButtonListener(getPackageColorButton());
     addColorButtonListener(getDiagramBackgroundColorButton());

     getPackageNameCompressionLevelSpinner().setBorder((Border)null);
     this.spinnerNumberModel = new SpinnerNumberModel(new Integer(0), new Integer(0), new Integer(99), new Integer(1));
     getPackageNameCompressionLevelSpinner().setModel(this.spinnerNumberModel);
     getPackageNameCompressionLevelSpinner().addChangeListener(new ChangeListener()
         {
           public void stateChanged(ChangeEvent e)
           {
             String cPN = UMLUtils.getCompressedPackageName("net.trustx.simpleuml.test", UIPanel.this.spinnerNumberModel.getNumber().intValue());
             UIPanel.this.getPackageNameCompressionLevelSampleLabel().setText(cPN);
           }
         });
   }



   private void addColorButtonListener(final JButton button) {
     button.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             Color color = JColorChooser.showDialog(null, button.getText(), button.getBackground());
             if (color == null) {
               return;
             }

             button.setBackground(color);
           }
         });
   }



   private Font getContentFontOnUI() {
     return new Font(getContentFontNameComboBox().getSelectedItem().toString(), 0, Integer.parseInt(getContentFontSizeComboBox().getSelectedItem().toString()));
   }



   private Font getTitleFontOnUI() {
     return new Font(getTitleFontNameComboBox().getSelectedItem().toString(), 0, Integer.parseInt(getTitleFontSizeComboBox().getSelectedItem().toString()));
   }




   public void applySettingsToUI(PackageDiagramSettings packageDiagramSettings) {
     getPackageColorButton().setBackground(packageDiagramSettings.getPackageBackgroundColor());
     getDiagramBackgroundColorButton().setBackground(packageDiagramSettings.getDiagramBackgroundColor());


     getAntialiasingConnectorsCheckBox().setSelected(packageDiagramSettings.isUseAntialiasedConnectors());


     getTitleFontNameComboBox().setSelectedItem(packageDiagramSettings.getDiagramTitleFont().getName());
     getTitleFontSizeComboBox().setSelectedItem("" + packageDiagramSettings.getDiagramTitleFont().getSize());
     getTitleFontSampleTextLabel().setFont(packageDiagramSettings.getDiagramTitleFont());

     getContentFontNameComboBox().setSelectedItem(packageDiagramSettings.getDiagramFont().getName());
     getContentFontSizeComboBox().setSelectedItem("" + packageDiagramSettings.getDiagramFont().getSize());
     getContentFontSampleTextLabel().setFont(packageDiagramSettings.getDiagramFont());


     getPackageNameCompressionLevelSpinnerModel().setValue(new Integer(packageDiagramSettings.getPackageNameCompressionLevel()));

     this.initialised = true;
   }



   public SpinnerNumberModel getPackageNameCompressionLevelSpinnerModel() {
     return this.spinnerNumberModel;
   }



   public String getDisplayName() {
     return "UI";
   }



   public boolean isModified(PackageDiagramSettings packageDiagramSettings) {
     if (!this.initialised)
     {
       return false;
     }

     if (!packageDiagramSettings.getDiagramTitleFont().equals(getTitleFontOnUI()))
     {
       return true;
     }
     if (!packageDiagramSettings.getDiagramFont().equals(getContentFontOnUI()))
     {
       return true;
     }
     if (packageDiagramSettings.isUseAntialiasedConnectors() != getAntialiasingConnectorsCheckBox().isSelected())
     {
       return true;
     }


     if (!packageDiagramSettings.getPackageBackgroundColor().equals(getPackageColorButton().getBackground()))
     {
       return true;
     }
     if (!packageDiagramSettings.getDiagramBackgroundColor().equals(getDiagramBackgroundColorButton().getBackground()))
     {
       return true;
     }
     return (packageDiagramSettings.getPackageNameCompressionLevel() != getPackageNameCompressionLevelSpinnerModel().getNumber().intValue());
   }




   public void apply(PackageDiagramSettings packageDiagramSettings) {
     if (!this.initialised) {
       return;
     }


     packageDiagramSettings.setDiagramTitleFont(getTitleFontOnUI());
     packageDiagramSettings.setDiagramFont(getContentFontOnUI());
     packageDiagramSettings.setUseAntialiasedConnectors(getAntialiasingConnectorsCheckBox().isSelected());
     packageDiagramSettings.setPackageBackgroundColor(getPackageColorButton().getBackground());
     packageDiagramSettings.setDiagramBackgroundColor(getDiagramBackgroundColorButton().getBackground());
     packageDiagramSettings.setPackageNameCompressionLevel(getPackageNameCompressionLevelSpinnerModel().getNumber().intValue());
   }



   public void reset(PackageDiagramSettings packageDiagramSettings) {
     applySettingsToUI(packageDiagramSettings);
   }
 }


