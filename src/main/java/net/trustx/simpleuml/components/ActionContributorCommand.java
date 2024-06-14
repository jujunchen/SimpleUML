 package net.trustx.simpleuml.components;
 
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import java.util.HashMap;
 import javax.swing.Icon;
 

 
 public abstract class ActionContributorCommand
   extends AnAction
 {
   public static final String EVENT_PROPERTY_NAME = "ActionContributorCommand.Event";
   
   public ActionContributorCommand() {}
   
   public ActionContributorCommand(String text) {
     super(text);
   }
 
 
   
   public ActionContributorCommand(String text, String description, Icon icon) {
     super(text, description, icon);
   }
 
 
   
   public void actionPerformed(AnActionEvent event) {
     HashMap<Object, Object> commandProperties = new HashMap<Object, Object>();
     commandProperties.put("ActionContributorCommand.Event", event);
     executeCommand(commandProperties, true, true);
   }
 
 
   
   public abstract void executeCommand(HashMap paramHashMap, boolean paramBoolean1, boolean paramBoolean2);
 
   
   public String getGroupName() {
     return null;
   }
 }


