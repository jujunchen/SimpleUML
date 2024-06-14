 package net.trustx.simpleuml.classdiagram.util;
 
 import java.awt.Color;
 import java.util.HashMap;
 import java.util.Map;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 

 public class ClassDiagramHelper
 {
   private String className;
   private int posX;
   private int posY;
   private boolean fieldsExpanded;
   private boolean constructorsExpanded;
   private boolean methodsExpanded;
   private boolean pinned;
   private Color color;
   
   public ClassDiagramHelper(String className, int posX, int posY) {
     this.className = className;
     this.posX = posX;
     this.posY = posY;
   }
 
 
   
   public ClassDiagramHelper(PsiClassComponent diagramComponent) {
     this.className = diagramComponent.getQualifiedClassName();
     this.constructorsExpanded = diagramComponent.getToggleConstructorButton().isSelected();
     this.fieldsExpanded = diagramComponent.getToggleFieldButton().isSelected();
     this.methodsExpanded = diagramComponent.getToggleMethodButton().isSelected();
     this.posX = diagramComponent.getPosX();
     this.posY = diagramComponent.getPosY();
     this.pinned = diagramComponent.isPinned();
     this.color = diagramComponent.getColor();
   }
 
 
   
   public String getClassName() {
     return this.className;
   }
 
 
   
   public boolean isConstructorsExpanded() {
     return this.constructorsExpanded;
   }
 
 
   
   public boolean isFieldsExpanded() {
     return this.fieldsExpanded;
   }
 
 
   
   public boolean isMethodsExpanded() {
     return this.methodsExpanded;
   }
 
 
   
   public int getPosX() {
     return this.posX;
   }
 
 
   
   public int getPosY() {
     return this.posY;
   }
 
 
   
   public boolean isPinned() {
     return this.pinned;
   }
 
 
   
   public Color getColor() {
     return this.color;
   }
 
 
 
   
   public void setSettingsMap(Map settingsMap) {
     try {
       this.fieldsExpanded = getBooleanValue(settingsMap.get("fieldsExpanded"));
       this.constructorsExpanded = getBooleanValue(settingsMap.get("constructorsExpanded"));
       this.methodsExpanded = getBooleanValue(settingsMap.get("methodsExpanded"));
       this.pinned = getBooleanValue(settingsMap.get("pinned"));
       this.color = getColorValue(settingsMap.get("color"));
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
     
     settingsMap.put("fieldsExpanded", Boolean.valueOf(this.fieldsExpanded));
     settingsMap.put("constructorsExpanded", Boolean.valueOf(this.constructorsExpanded));
     settingsMap.put("methodsExpanded", Boolean.valueOf(this.methodsExpanded));
     settingsMap.put("pinned", Boolean.valueOf(this.pinned));
     if (this.color != null)
     {
       settingsMap.put("color", new Integer(this.color.getRGB()));
     }
     
     return settingsMap;
   }
 }


