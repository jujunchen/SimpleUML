 package net.trustx.simpleuml.dependencydiagram.components;

 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;


 public class DependencyDiagramComponentSettings
 {
   private HashMap settingsListenerMap;
   private boolean showLibraryDependencies;

   public DependencyDiagramComponentSettings() {
     this.settingsListenerMap = new HashMap<Object, Object>();
     initDefaultValues();
   }



   private void initDefaultValues() {
     this.showLibraryDependencies = true;
   }



   public void addSettingsListener(ComponentSettingsListener listener) {
     this.settingsListenerMap.put(listener, listener);
   }



   public void removeSettingsListener(ComponentSettingsListener listener) {
     this.settingsListenerMap.remove(listener);
   }



   public void notifyListeners() {
     Iterator<ComponentSettingsListener> iter = this.settingsListenerMap.values().iterator();
     while (iter.hasNext()) {

       ComponentSettingsListener componentSettingsListener = iter.next();
       if (componentSettingsListener != null)
       {
         componentSettingsListener.settingsChanged(this);
       }
     }
   }



   public boolean isShowLibraryDependencies() {
     return this.showLibraryDependencies;
   }



   public void setShowLibraryDependencies(boolean showLibraryDependencies) {
     this.showLibraryDependencies = showLibraryDependencies;
   }




   public void setSettingsMap(Map settingsMap) {
     try {
       this.showLibraryDependencies = getBooleanValue(settingsMap.get("showLibraryDependencies"));
     }
     catch (Throwable e) {

       e.printStackTrace();
     }
   }



   private boolean getBooleanValue(Object value) {
     if (value == null)
     {
       return false;
     }
     return "true".equalsIgnoreCase(value.toString());
   }



   public Map getSettingsMap() {
     HashMap<Object, Object> settingsMap = new HashMap<Object, Object>();
     settingsMap.put("showLibraryDependencies", Boolean.valueOf(this.showLibraryDependencies));
     return settingsMap;
   }
 }


