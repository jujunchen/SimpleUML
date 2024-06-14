 package net.trustx.simpleuml.file;

 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.DialogWrapper;
 import com.intellij.openapi.vfs.VirtualFile;
 import com.intellij.openapi.vfs.VirtualFileManager;
 import java.awt.BorderLayout;
 import java.awt.Dimension;
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Insets;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import javax.swing.Action;
 import javax.swing.BorderFactory;
 import javax.swing.JButton;
 import javax.swing.JComboBox;
 import javax.swing.JComponent;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JTextField;
 import net.trustx.simpleuml.plugin.DiagramFactory;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLConstants;
 import net.trustx.simpleuml.util.UMLUtils;





 public class UMLLocationChooser
   extends DialogWrapper
   implements UMLConstants
 {
   private JPanel centerPanel;
   private VirtualFile folderFile;
   private JComboBox typeComboBox;
   private JLabel folderLabel;
   private JButton chooseFolderButton;
   private JTextField diagramNameTextField;

   public UMLLocationChooser(final Project project, String folderURL, String name, String selectedType, boolean canChangeDiagramType) {
     super(project, false);

     getContentPane().setLayout(new BorderLayout());
     this.centerPanel = new JPanel(new BorderLayout());
     getContentPane().add(this.centerPanel, "Center");

     if (folderURL == null)
     {
       folderURL = UMLToolWindowPlugin.getUMLToolWindowPlugin(project).getUMLFileManager().getDefaultFileLocationURL();
     }
     this.folderFile = VirtualFileManager.getInstance().findFileByUrl(folderURL);


     GridBagLayout gridBagLayout = new GridBagLayout();
     GridBagConstraints gbc = new GridBagConstraints();
     this.centerPanel.setLayout(gridBagLayout);

     String[] types = DiagramFactory.getSupportedDiagramTypes();

     gbc.insets = new Insets(0, 5, 5, 5);

     gbc.fill = 2;
     gbc.gridx = 0;
     gbc.gridy = 0;
     gbc.weightx = 0.0D;
     gbc.gridwidth = 1;
     JLabel diagramTypeLabel = new JLabel("Diagram Type:", 4);
     gridBagLayout.setConstraints(diagramTypeLabel, gbc);
     this.centerPanel.add(diagramTypeLabel);

     gbc.fill = 2;
     gbc.weightx = 1.0D;
     gbc.gridwidth = 2;
     gbc.gridx = 1;
     gbc.gridy = 0;
     this.typeComboBox = new JComboBox<String>(types);
     if (selectedType != null)
     {
       this.typeComboBox.setSelectedItem(selectedType);
     }
     this.typeComboBox.setEnabled(canChangeDiagramType);
     gridBagLayout.setConstraints(this.typeComboBox, gbc);
     this.centerPanel.add(this.typeComboBox);


     gbc.fill = 2;
     gbc.weightx = 0.0D;
     gbc.weighty = 0.0D;
     gbc.gridwidth = 1;
     gbc.gridx = 0;
     gbc.gridy = 1;
     JLabel titleFontLabel = new JLabel("Folder:", 4);
     gridBagLayout.setConstraints(titleFontLabel, gbc);
     this.centerPanel.add(titleFontLabel);

     gbc.fill = 2;
     gbc.weightx = 1.0D;
     gbc.gridwidth = 1;
     gbc.gridx = 1;
     gbc.gridy = 1;
     this.folderLabel = new JLabel(this.folderFile.getPresentableUrl());
     this.folderLabel.setBorder(BorderFactory.createEtchedBorder());
     this.folderLabel.setPreferredSize(new Dimension(300, (this.folderLabel.getPreferredSize()).height + 2));
     gridBagLayout.setConstraints(this.folderLabel, gbc);
     this.centerPanel.add(this.folderLabel);

     gbc.fill = 2;
     gbc.weightx = 0.0D;
     gbc.gridwidth = 1;
     gbc.gridx = 2;
     gbc.gridy = 1;
     this.chooseFolderButton = new JButton("...");
     this.chooseFolderButton.setMargin(new Insets(0, 1, 0, 1));
     gridBagLayout.setConstraints(this.chooseFolderButton, gbc);
     this.centerPanel.add(this.chooseFolderButton);


     gbc.fill = 2;
     gbc.weightx = 0.0D;
     gbc.gridwidth = 1;
     gbc.gridx = 0;
     gbc.gridy = 2;
     JLabel contentFontLabel = new JLabel("Diagram Name:", 4);
     gridBagLayout.setConstraints(contentFontLabel, gbc);
     this.centerPanel.add(contentFontLabel);

     gbc.fill = 2;
     gbc.weightx = 1.0D;
     gbc.gridwidth = 2;
     gbc.gridx = 1;
     gbc.gridy = 2;
     this.diagramNameTextField = new JTextField("");
     if (name != null) {

       name = UMLUtils.stripFileType(name);
       this.diagramNameTextField.setText(name);
     }
     gridBagLayout.setConstraints(this.diagramNameTextField, gbc);
     this.centerPanel.add(this.diagramNameTextField);

     final String tempFolderURL = folderURL;
     this.chooseFolderButton.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             FolderChooser folderChooser = new FolderChooser(project, tempFolderURL);
             folderChooser.setModal(true);
             folderChooser.show();
             if (folderChooser.getExitCode() == 0) {

               UMLLocationChooser.this.folderFile = folderChooser.getSelectedFile();
               UMLLocationChooser.this.folderLabel.setText(UMLLocationChooser.this.folderFile.getPresentableUrl());
             }
           }
         });

     init();

     setTitle("Choose a Name and Location");
   }



   public JComponent getPreferredFocusedComponent() {
     return this.diagramNameTextField;
   }



   private boolean testName() {
     return FileUtils.isValidDiagramName(this.diagramNameTextField.getText(), true);
   }



   public String getFolderURL() {
     return this.folderFile.getUrl();
   }



   public String getDiagramName() {
     String diagramName = this.diagramNameTextField.getText();
     if (diagramName.endsWith(".suml"))
     {
       return diagramName;
     }


     return diagramName + ".suml";
   }




   public String getDiagramType() {
     return this.typeComboBox.getSelectedItem().toString();
   }



   protected JComponent createCenterPanel() {
     return this.centerPanel;
   }



   protected void doOKAction() {
     if (!testName()) {
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


