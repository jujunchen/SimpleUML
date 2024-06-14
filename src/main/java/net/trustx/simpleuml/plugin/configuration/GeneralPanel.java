 package net.trustx.simpleuml.plugin.configuration;

 import com.intellij.openapi.vfs.VirtualFile;
 import com.intellij.openapi.vfs.VirtualFileManager;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import net.trustx.simpleuml.file.FolderChooser;



























 public class GeneralPanel
   extends GeneralPanelUI
 {
   private boolean initialised;
   private VirtualFile defaultFileLocation;

   public GeneralPanel(final GeneralDiagramSettings generalDiagramSettings) {
     this.initialised = false;

     getShowFileChooserButton().addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             FolderChooser folderChooser = new FolderChooser(generalDiagramSettings.getUmlFileManager().getProject(), GeneralPanel.this.defaultFileLocation.getUrl());
             folderChooser.setModal(true);
             folderChooser.show();
             if (folderChooser.getExitCode() == 0) {

               GeneralPanel.this.defaultFileLocation = folderChooser.getSelectedFile();
               GeneralPanel.this.getDefaultPathLabel().setText(GeneralPanel.this.defaultFileLocation.getPresentableUrl());
             }
           }
         });
   }



   public void applySettingsToUI(GeneralDiagramSettings generalDiagramSettings) {
     this.defaultFileLocation = VirtualFileManager.getInstance().findFileByUrl(generalDiagramSettings.getUmlFileManager().getDefaultFileLocationURL());
     getDefaultPathLabel().setText(this.defaultFileLocation.getPresentableUrl());


     getBirdViewDelayTextField().setText("" + generalDiagramSettings.getBirdViewUpdateDelay());
     this.initialised = true;
   }



   public String getDisplayName() {
     return "General";
   }



   public boolean isModified(GeneralDiagramSettings generalDiagramSettings) {
     if (!this.initialised)
     {
       return false;
     }

     if (!getBirdViewDelayTextField().getText().equals("" + generalDiagramSettings.getBirdViewUpdateDelay()))
     {
       return true;
     }
     return !this.defaultFileLocation.getUrl().equals(generalDiagramSettings.getUmlFileManager().getDefaultFileLocationURL());
   }




   public void apply(GeneralDiagramSettings generalDiagramSettings) {
     if (!this.initialised) {
       return;
     }


     generalDiagramSettings.setBirdViewUpdateDelay(ConfigurationUtils.getLong(getBirdViewDelayTextField().getText(), generalDiagramSettings.getBirdViewUpdateDelay(), 100L, 1209600000L));

     generalDiagramSettings.getUmlFileManager().setDefaultFileLocationURL(this.defaultFileLocation.getUrl());
   }




   public void reset(GeneralDiagramSettings generalDiagramSettings) {
     applySettingsToUI(generalDiagramSettings);
   }
 }


