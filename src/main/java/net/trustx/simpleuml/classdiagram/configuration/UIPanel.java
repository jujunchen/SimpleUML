 package net.trustx.simpleuml.classdiagram.configuration;

 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Font;
 import java.awt.GraphicsEnvironment;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.ItemEvent;
 import java.awt.event.ItemListener;
 import javax.swing.DefaultComboBoxModel;
 import javax.swing.JButton;
 import javax.swing.JColorChooser;
 import net.trustx.simpleuml.plugin.configuration.ConfigurationUtils;



 public class UIPanel
   extends UIPanelUI
 {
   private boolean initialised = false;

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

     getTitleFontSizeComboBox().setModel(new DefaultComboBoxModel<String>(ClassDiagramSettings.FONT_SIZES));

     DefaultComboBoxModel<String> contentFontModel = new DefaultComboBoxModel<String>(fontNames);
     getContentFontNameComboBox().setModel(contentFontModel);

     getContentFontSizeComboBox().setModel(new DefaultComboBoxModel<String>(ClassDiagramSettings.FONT_SIZES));
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

     addColorButtonListener(getInterfaceColorButton());
     addColorButtonListener(getAbstractClassColorButton());
     addColorButtonListener(getClassColorButton());
     addColorButtonListener(getDiagramBackgroundColorButton());
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
     return new Font(getTitleFontNameComboBox().getSelectedItem().toString(), 1, Integer.parseInt(getTitleFontSizeComboBox().getSelectedItem().toString()));
   }




   public void applySettingsToUI(ClassDiagramSettings classDiagramSettings) {
     getAbstractClassColorButton().setBackground(classDiagramSettings.getAbstractClassBackgroundColor());
     getClassColorButton().setBackground(classDiagramSettings.getClassBackgroundColor());
     getInterfaceColorButton().setBackground(classDiagramSettings.getInterfaceBackgroundColor());
     getDiagramBackgroundColorButton().setBackground(classDiagramSettings.getDiagramBackgroundColor());


     getAntialiasingConnectorsCheckBox().setSelected(classDiagramSettings.isUseAntialiasedConnectors());


     getTitleFontNameComboBox().setSelectedItem(classDiagramSettings.getDiagramTitleFont().getName());
     getTitleFontSizeComboBox().setSelectedItem("" + classDiagramSettings.getDiagramTitleFont().getSize());
     getTitleFontSampleTextLabel().setFont(classDiagramSettings.getDiagramTitleFont());

     getContentFontNameComboBox().setSelectedItem(classDiagramSettings.getDiagramFont().getName());
     getContentFontSizeComboBox().setSelectedItem("" + classDiagramSettings.getDiagramFont().getSize());
     getContentFontSampleTextLabel().setFont(classDiagramSettings.getDiagramFont());


     getWidthTextField().setText("" + (classDiagramSettings.getMinimumFigureSize()).width);
     getHeightTextField().setText("" + (classDiagramSettings.getMinimumFigureSize()).height);

     this.initialised = true;
   }



   public String getDisplayName() {
     return "UI";
   }



   public boolean isModified(ClassDiagramSettings classDiagramSettings) {
     if (!this.initialised)
     {
       return false;
     }

     if (!classDiagramSettings.getDiagramTitleFont().equals(getTitleFontOnUI()))
     {
       return true;
     }
     if (!classDiagramSettings.getDiagramFont().equals(getContentFontOnUI()))
     {
       return true;
     }
     if (classDiagramSettings.isUseAntialiasedConnectors() != getAntialiasingConnectorsCheckBox().isSelected())
     {
       return true;
     }

     if (!classDiagramSettings.getInterfaceBackgroundColor().equals(getInterfaceColorButton().getBackground()))
     {
       return true;
     }
     if (!classDiagramSettings.getAbstractClassBackgroundColor().equals(getAbstractClassColorButton().getBackground()))
     {
       return true;
     }
     if (!classDiagramSettings.getClassBackgroundColor().equals(getClassColorButton().getBackground()))
     {
       return true;
     }
     if (!classDiagramSettings.getDiagramBackgroundColor().equals(getDiagramBackgroundColorButton().getBackground()))
     {
       return true;
     }
     return !classDiagramSettings.getMinimumFigureSize().equals(getMinimumFigureSize());
   }




   private Dimension getMinimumFigureSize() {
     Dimension dimension = ConfigurationUtils.getDimensionFromStringObject(getWidthTextField().getText() + "," + getHeightTextField().getText(), new Dimension(0, 0));
     if (dimension.width < 0)
     {
       dimension.width = 0;
     }
     if (dimension.height < 0)
     {
       dimension.height = 0;
     }
     return dimension;
   }



   public void apply(ClassDiagramSettings classDiagramSettings) {
     if (!this.initialised) {
       return;
     }


     classDiagramSettings.setDiagramTitleFont(getTitleFontOnUI());
     classDiagramSettings.setDiagramFont(getContentFontOnUI());
     classDiagramSettings.setUseAntialiasedConnectors(getAntialiasingConnectorsCheckBox().isSelected());
     classDiagramSettings.setInterfaceBackgroundColor(getInterfaceColorButton().getBackground());
     classDiagramSettings.setAbstractClassBackgroundColor(getAbstractClassColorButton().getBackground());
     classDiagramSettings.setClassBackgroundColor(getClassColorButton().getBackground());
     classDiagramSettings.setDiagramBackgroundColor(getDiagramBackgroundColorButton().getBackground());
     classDiagramSettings.setMinimumFigureSize(getMinimumFigureSize());
   }



   public void reset(ClassDiagramSettings classDiagramSettings) {
     applySettingsToUI(classDiagramSettings);
   }
 }


