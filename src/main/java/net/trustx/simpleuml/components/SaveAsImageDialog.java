 package net.trustx.simpleuml.components;
 
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.DialogWrapper;
 import com.intellij.openapi.ui.Messages;
 import java.awt.BorderLayout;
 import java.awt.GridLayout;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.io.File;
 import java.io.IOException;
 import java.util.TreeSet;
 import javax.imageio.ImageIO;
 import javax.swing.Action;
 import javax.swing.Box;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JComboBox;
 import javax.swing.JComponent;
 import javax.swing.JFileChooser;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JSpinner;
 import javax.swing.JTextField;
 import javax.swing.SpinnerNumberModel;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 
 
 public class SaveAsImageDialog
   extends DialogWrapper
 {
   private JPanel centerPanel;
   private JCheckBox antialiasingCheckBox;
   private JCheckBox textAntialiasingCheckBox;
   private JCheckBox cropImageCheckBox;
   private DiagramPreviewPanel diagramPreviewPanel;
   private File selectedFile;
   private JComboBox typeComboBox;
   private String previousTypeEnding;
   private JTextField pathField;
   private Project project;
   private JSpinner scaleSpinner;
   
   public SaveAsImageDialog(Project project, Previewable diagramComponent) {
     super(project, false);
     this.project = project;
     getContentPane().setLayout(new BorderLayout());
     this.centerPanel = new JPanel(new BorderLayout());
     this.diagramPreviewPanel = new DiagramPreviewPanel(diagramComponent);
     this.centerPanel.add(this.diagramPreviewPanel, "Center");
     
     getContentPane().add(this.centerPanel, "Center");
 
     
     initScaleSpinner();
     initAntialiasingCheckBox();
     initTextAntialiasingCheckBox();
     initCropImageCheckBox();
     
     JPanel controlPanel = new JPanel(new GridLayout(0, 1));
     controlPanel.add(this.antialiasingCheckBox);
     controlPanel.add(this.textAntialiasingCheckBox);
     controlPanel.add(this.cropImageCheckBox);
     
     JPanel southPanel = new JPanel(new BorderLayout(5, 5));
 
 
     
     Box typeBox = Box.createHorizontalBox();
     JLabel typeLabel = new JLabel("Image Type");
     
     String[] names = ImageIO.getWriterFormatNames();
     TreeSet<String> set = new TreeSet();
     for (int i = 0; i < names.length; i++) {
       
       String name = names[i];
       set.add(name.toLowerCase());
     } 
     
     initTypeComboBox(set);
     
     typeBox.add(typeLabel);
     typeBox.add(Box.createHorizontalStrut(5));
     typeBox.add(this.typeComboBox);
 
     
     typeBox.add(Box.createHorizontalStrut(10));
     typeBox.add(new JLabel("Scale Percentage"));
     typeBox.add(Box.createHorizontalStrut(5));
     typeBox.add(this.scaleSpinner);
     
     JPanel helperPanel = new JPanel(new BorderLayout());
     helperPanel.add(typeBox, "West");
     
     southPanel.add(helperPanel, "Center");
 
 
     
     JPanel pathPanel = initPathPanel(project);
     
     southPanel.add(pathPanel, "South");
     
     JPanel tempPanel = new JPanel(new BorderLayout(5, 5));
     tempPanel.add(controlPanel, "Center");
     tempPanel.add(southPanel, "South");
     
     this.centerPanel.add(tempPanel, "South");
     
     init();
   }
 
 
   
   private JPanel initPathPanel(final Project project) {
     this.pathField = new JTextField("");
 
     
     JButton pathButton = new JButton("...");
     pathButton.setMargin(null);
     JPanel pathBox = new JPanel(new BorderLayout(5, 5));
     pathBox.add(new JLabel("File:"), "West");
     pathBox.add(this.pathField, "Center");
     pathBox.add(pathButton, "East");
     pathButton.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             String previousPath = (String)DiagramSettingsWorkspace.getDiagramSettingsWorkspace(project).get("SaveAsImageDialog.path");
             File parentFile = null;
             if (previousPath != null)
             {
               parentFile = (new File(previousPath)).getParentFile();
             }
             
             if (parentFile == null)
             {
               parentFile = new File(".");
             }
             
             JFileChooser fileChooser = new JFileChooser(parentFile);
             int success = fileChooser.showSaveDialog(SaveAsImageDialog.this.centerPanel);
             if (success == 0) {
               
               SaveAsImageDialog.this.selectedFile = fileChooser.getSelectedFile();
               
               try {
                 String path = SaveAsImageDialog.this.selectedFile.getCanonicalPath();
                 SaveAsImageDialog.this.previousTypeEnding = "." + SaveAsImageDialog.this.typeComboBox.getSelectedItem();
                 if (!path.endsWith(SaveAsImageDialog.this.previousTypeEnding))
                 {
                   path = path.concat(SaveAsImageDialog.this.previousTypeEnding);
                 }
                 SaveAsImageDialog.this.pathField.setText(path);
               }
               catch (IOException e1) {}
             } 
           }
         });
 
     
     return pathBox;
   }
 
 
   
   private void initTypeComboBox(TreeSet set) {
     this.typeComboBox = new JComboBox(set.toArray());
     this.typeComboBox.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             if (SaveAsImageDialog.this.previousTypeEnding != null) {
               
               if (SaveAsImageDialog.this.pathField.getText().endsWith(SaveAsImageDialog.this.previousTypeEnding))
               {
                 String text = SaveAsImageDialog.this.pathField.getText();
                 text = text.substring(0, text.length() - SaveAsImageDialog.this.previousTypeEnding.length());
                 SaveAsImageDialog.this.previousTypeEnding = "." + SaveAsImageDialog.this.typeComboBox.getSelectedItem();
                 text = text.concat(SaveAsImageDialog.this.previousTypeEnding);
                 SaveAsImageDialog.this.pathField.setText(text);
               }
             
             } else {
               
               SaveAsImageDialog.this.previousTypeEnding = "." + SaveAsImageDialog.this.typeComboBox.getSelectedItem();
             } 
           }
         });
   }
 
 
   
   private void initCropImageCheckBox() {
     this.cropImageCheckBox = new JCheckBox("Crop Image");
     this.cropImageCheckBox.setSelected(true);
     this.cropImageCheckBox.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             SaveAsImageDialog.this.diagramPreviewPanel.setCropImage(SaveAsImageDialog.this.cropImageCheckBox.isSelected());
           }
         });
   }
 
 
   
   private void initTextAntialiasingCheckBox() {
     this.textAntialiasingCheckBox = new JCheckBox("Text Antialiasing");
     this.textAntialiasingCheckBox.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             SaveAsImageDialog.this.diagramPreviewPanel.setTextAntialiasing(SaveAsImageDialog.this.textAntialiasingCheckBox.isSelected());
           }
         });
   }
 
 
   
   private void initAntialiasingCheckBox() {
     this.antialiasingCheckBox = new JCheckBox("Antialiasing");
     this.antialiasingCheckBox.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             SaveAsImageDialog.this.diagramPreviewPanel.setAntialiasing(SaveAsImageDialog.this.antialiasingCheckBox.isSelected());
           }
         });
   }
 
 
   
   private void initScaleSpinner() {
     final SpinnerNumberModel snm = new SpinnerNumberModel(new Integer(50), new Integer(1), new Integer(400), new Integer(1));
     this.scaleSpinner = new JSpinner(snm);
     
     this.scaleSpinner.addChangeListener(new ChangeListener()
         {
           public void stateChanged(ChangeEvent e)
           {
             SaveAsImageDialog.this.diagramPreviewPanel.setScaleFactor(snm.getNumber().intValue() / 100.0D);
           }
         });
   }
 
 
   
   public void setImagePath(String path) {
     this.pathField.setText(path);
   }
 
 
   
   protected JComponent createCenterPanel() {
     return this.centerPanel;
   }
 
 
   
   public String getTitle() {
     return "Save as Image";
   }
 
 
   
   public File getSelectedFile() {
     return new File(this.pathField.getText());
   }
 
 
   
   public String getSelectedImageType() {
     return this.typeComboBox.getSelectedItem().toString();
   }
 
 
   
   public void setSelectedImageType(String type) {
     this.typeComboBox.setSelectedItem(type);
   }
 
 
   
   public DiagramPreviewPanel getDiagramPreviewPanel() {
     return this.diagramPreviewPanel;
   }
 
 
   
   public JComboBox getTypeComboBox() {
     return this.typeComboBox;
   }
 
 
   
   protected void doOKAction() {
     if ("".equals(this.pathField.getText().trim())) {
       
       Messages.showMessageDialog(this.project, "Path is not valid", "ERROR", Messages.getErrorIcon());
       return;
     } 
     if (!this.pathField.getText().endsWith("." + this.typeComboBox.getSelectedItem()))
     {
       this.pathField.setText(this.pathField.getText() + "." + this.typeComboBox.getSelectedItem());
     }
 
     
     try {
       File file = new File(this.pathField.getText());
       if (!file.createNewFile() && !file.exists()) {
         
         Messages.showMessageDialog(this.project, "Path is not valid", "ERROR", Messages.getErrorIcon());
         
         return;
       } 
     } catch (IOException e) {
       
       Messages.showMessageDialog(this.project, "Path is not valid", "ERROR", Messages.getErrorIcon());
       
       return;
     } 
     super.doOKAction();
   }
 
 
   
   protected Action[] createActions() {
     setOKButtonText("Ok");
     setCancelButtonText("Cancel");
     return new Action[] { getOKAction(), getCancelAction() };
   }
 }


