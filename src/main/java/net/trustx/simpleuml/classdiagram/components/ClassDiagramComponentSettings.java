 package net.trustx.simpleuml.classdiagram.components;

 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;



 public class ClassDiagramComponentSettings
 {
   private boolean paintExtends;
   private boolean paintImplements;
   private boolean paintUses;
   private boolean paintInner;
   private boolean paintDepends;
   private boolean paintContains;
   private boolean layoutOnChanges;
   private HashMap settingsListenerMap;

   public ClassDiagramComponentSettings() {
     this.settingsListenerMap = new HashMap<Object, Object>();
     initDefaultValues();
   }



   private void initDefaultValues() {
     this.paintExtends = true;
     this.paintImplements = true;
     this.paintUses = false;
     this.paintInner = true;
     this.layoutOnChanges = false;
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



   public boolean isPaintExtends() {
     return this.paintExtends;
   }



   public void setPaintExtends(boolean paintExtends) {
     this.paintExtends = paintExtends;
     notifyListeners();
   }



   public boolean isPaintImplements() {
     return this.paintImplements;
   }



   public void setPaintImplements(boolean paintImplements) {
     this.paintImplements = paintImplements;
     notifyListeners();
   }



   public boolean isPaintUses() {
     return this.paintUses;
   }



   public void setPaintUses(boolean paintUses) {
     this.paintUses = paintUses;
     notifyListeners();
   }



   public boolean isPaintInner() {
     return this.paintInner;
   }



   public void setPaintInner(boolean paintInner) {
     this.paintInner = paintInner;
     notifyListeners();
   }



   public boolean isPaintDepends() {
     return this.paintDepends;
   }



   public void setPaintContains(boolean paintContains) {
     this.paintContains = paintContains;
     notifyListeners();
   }



   public void setPaintDepends(boolean paintDepends) {
     this.paintDepends = paintDepends;
     notifyListeners();
   }



   public boolean isPaintContains() {
     return this.paintContains;
   }



   public boolean isLayoutOnChanges() {
     return this.layoutOnChanges;
   }



   public void setLayoutOnChanges(boolean layoutOnChanges) {
     this.layoutOnChanges = layoutOnChanges;
   }




   public void setSettingsMap(Map settingsMap) {
     try {
       this.paintExtends = getBooleanValue(settingsMap.get("paintExtends"));
       this.paintImplements = getBooleanValue(settingsMap.get("paintImplements"));
       this.paintUses = getBooleanValue(settingsMap.get("paintUses"));
       this.paintInner = getBooleanValue(settingsMap.get("paintInner"));
       this.paintDepends = getBooleanValue(settingsMap.get("paintDepends"));

       this.layoutOnChanges = getBooleanValue(settingsMap.get("layoutOnChanges"));
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

     settingsMap.put("paintExtends", Boolean.valueOf(this.paintExtends));
     settingsMap.put("paintImplements", Boolean.valueOf(this.paintImplements));
     settingsMap.put("paintUses", Boolean.valueOf(this.paintUses));
     settingsMap.put("paintInner", Boolean.valueOf(this.paintInner));
     settingsMap.put("paintDepends", Boolean.valueOf(this.paintDepends));

     settingsMap.put("layoutOnChanges", Boolean.valueOf(this.layoutOnChanges));

     return settingsMap;
   }
 }


