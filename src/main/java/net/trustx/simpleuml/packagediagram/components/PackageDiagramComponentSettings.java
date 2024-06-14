 package net.trustx.simpleuml.packagediagram.components;

 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;





 public class PackageDiagramComponentSettings
 {
   private HashMap settingsListenerMap;

   public PackageDiagramComponentSettings() {
     this.settingsListenerMap = new HashMap<Object, Object>();
     initDefaultValues();
   }




   private void initDefaultValues() {}



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




   public void setSettingsMap(Map settingsMap) {}



   public Map getSettingsMap() {
     HashMap<Object, Object> settingsMap = new HashMap<Object, Object>();
     return settingsMap;
   }
 }


