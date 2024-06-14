 package net.trustx.simpleuml.packagediagram.util;

 import java.awt.Color;
 import java.util.HashMap;
 import java.util.Map;
 import net.trustx.simpleuml.packagediagram.components.PsiPackageComponent;




 public class PackageDiagramHelper
 {
   private String packageName;
   private int posX;
   private int posY;
   private int width;
   private int height;
   private boolean pinned;
   private Color color;
   private boolean showClasses;

   public PackageDiagramHelper(String packageName, int posX, int posY, int width, int height) {
     this.packageName = packageName;
     this.posX = posX;
     this.posY = posY;
     this.width = width;
     this.height = height;
   }



   public PackageDiagramHelper(PsiPackageComponent psiPackageComponent) {
     this.packageName = psiPackageComponent.getPackageName();
     this.posX = psiPackageComponent.getPosX();
     this.posY = psiPackageComponent.getPosY();
     this.width = psiPackageComponent.getWidth();
     this.height = psiPackageComponent.getHeight();
     this.pinned = psiPackageComponent.isPinned();
     this.color = psiPackageComponent.getColor();
     this.showClasses = psiPackageComponent.isShowClasses();
   }



   public String getPackageName() {
     return this.packageName;
   }



   public int getPosX() {
     return this.posX;
   }



   public int getPosY() {
     return this.posY;
   }



   public int getWidth() {
     return this.width;
   }



   public int getHeight() {
     return this.height;
   }



   public boolean isPinned() {
     return this.pinned;
   }



   public Color getColor() {
     return this.color;
   }



   public boolean isShowClasses() {
     return this.showClasses;
   }



   public void setShowClasses(boolean showClasses) {
     this.showClasses = showClasses;
   }




   public void setSettingsMap(Map settingsMap) {
     try {
       this.pinned = getBooleanValue(settingsMap.get("pinned"));
       this.color = getColorValue(settingsMap.get("color"));
       this.showClasses = getBooleanValue(settingsMap.get("showclasses"));
     }
     catch (Throwable e) {

       e.printStackTrace();
     }
   }



   private Color getColorValue(Object value) {
     if (value == null)
     {
       return null;
     }
     if ("null".equals(value))
     {
       return null;
     }

     return new Color(Integer.parseInt(value.toString()));
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

     settingsMap.put("pinned", Boolean.valueOf(this.pinned));
     settingsMap.put("showclasses", Boolean.valueOf(this.showClasses));
     if (this.color != null)
     {
       settingsMap.put("color", this.color.getRGB());
     }

     return settingsMap;
   }
 }


