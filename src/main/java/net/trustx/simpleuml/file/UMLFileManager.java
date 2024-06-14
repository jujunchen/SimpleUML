 package net.trustx.simpleuml.file;
 
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.vfs.LocalFileSystem;
 import com.intellij.openapi.vfs.VirtualFile;
 import com.intellij.openapi.vfs.VirtualFileManager;
 import java.io.File;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Set;
 import java.util.TreeSet;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLConstants;
 

 
 public class UMLFileManager
   implements UMLConstants
 {
   private static final String DEFAULT_FILE_LOCATION_KEY = "defaultFileLocation";
   private static final String KNOWN_FILE_KEY = "knownFile";
   private TreeSet knownFilesSet;
   private String defaultFileLocationURL;
   private UMLToolWindowPlugin umlToolWindowPlugin;
   
   public UMLFileManager(UMLToolWindowPlugin umlToolWindowPlugin) {
     this.umlToolWindowPlugin = umlToolWindowPlugin;
     umlToolWindowPlugin.setUmlFileManager(this);
     initDefaultSettings();
   }
 
 
   
   public void addURL(String url) {
     this.knownFilesSet.add(url);
   }
 
 
   
   private void initDefaultSettings() {
     VirtualFile workspaceFile = this.umlToolWindowPlugin.getProject().getBaseDir();
     if (this.umlToolWindowPlugin.getProject() == null || workspaceFile == null) {
       String path = (new File(".")).getPath().replace(File.separatorChar, '/');
       this.defaultFileLocationURL = LocalFileSystem.getInstance().findFileByPath(path).getUrl();
     } else {
       VirtualFile parent = workspaceFile.getParent();
       if (parent == null)
         return; 
       this.defaultFileLocationURL = parent.getUrl();
     } 
     this.knownFilesSet = new TreeSet();
     
     addFileURLsInDefaultLocation();
   }
 
 
   
   private void addFileURLsInDefaultLocation() {
     VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(this.defaultFileLocationURL);
     if (virtualFile == null)
       return; 
     VirtualFile[] children = virtualFile.getChildren();
     for (int i = 0; i < children.length; i++) {
       
       VirtualFile child = children[i];
       if ("suml".equals(child.getExtension()))
       {
         this.knownFilesSet.add(child.getUrl());
       }
     } 
   }
 
 
   
   public void setSettingsMap(Map settingsMap) {
     String defaultFileLocationURL = (String)settingsMap.get("defaultFileLocation");
     
     if (defaultFileLocationURL == null) {
       return;
     }
     VirtualFile defaultFileLocation = VirtualFileManager.getInstance().findFileByUrl(defaultFileLocationURL);
     if (defaultFileLocation != null && defaultFileLocation.isValid() && defaultFileLocation.isDirectory())
     {
       this.defaultFileLocationURL = defaultFileLocationURL;
     }
 
     
     Set keySet = settingsMap.keySet();
     for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext(); ) {
       
       String key = iterator.next();
       if (key.startsWith("knownFile")) {
         
         String url = (String)settingsMap.get(key);
         
         VirtualFile file = VirtualFileManager.getInstance().findFileByUrl(defaultFileLocationURL);
         if (file != null && file.isValid())
         {
           this.knownFilesSet.add(url);
         }
       } 
     } 
     
     addFileURLsInDefaultLocation();
   }
 
 
   
   public Map getSettingsMap() {
     HashMap<Object, Object> settingsMap = new HashMap<Object, Object>();
     settingsMap.put("defaultFileLocation", this.defaultFileLocationURL);
     
     int i = 0;
     for (Iterator<String> iterator = this.knownFilesSet.iterator(); iterator.hasNext(); ) {
       
       String url = iterator.next();
       settingsMap.put("knownFile" + i, url);
       i++;
     } 
     
     return settingsMap;
   }
 
 
   
   public String getDefaultFileLocationURL() {
     return this.defaultFileLocationURL;
   }
 
 
   
   public String[] getKnownFileURLs() {
     return (String[])this.knownFilesSet.toArray((Object[])new String[this.knownFilesSet.size()]);
   }
 
 
   
   public Set getKnownFilesURLSet() {
     return new TreeSet(this.knownFilesSet);
   }
 
 
   
   public void setDefaultFileLocationURL(String url) {
     this.defaultFileLocationURL = url;
   }
 
 
   
   public Project getProject() {
     return this.umlToolWindowPlugin.getProject();
   }
 
 
   
   public void removeURL(String holePath) {
     this.knownFilesSet.remove(holePath);
   }
 }


