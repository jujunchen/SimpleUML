 package net.trustx.simpleuml.components;

 import java.util.Arrays;




 public class ActionContributorCommandInfo
 {
   public static final String TYPE_IDEA_ACTIONGROUP = "IDEAActionGroup";
   public static final String TYPE_SIMPLEUML_COMMAND = "SimpleUMLCommand";
   private String actionName;
   private String[] groupNames;
   private String type;
   private String actionGroupID;

   public ActionContributorCommandInfo(String actionName, String[] groupNames, String type, String actionGroupID) {
     this.actionName = actionName;
     this.groupNames = groupNames;
     this.type = type;
     this.actionGroupID = actionGroupID;
   }



   public ActionContributorCommandInfo(String actionName, String[] groupNames) {
     this.actionName = actionName;
     this.groupNames = groupNames;
     this.type = "SimpleUMLCommand";
     this.actionGroupID = null;
   }



   public String getActionName() {
     return this.actionName;
   }



   public String[] getGroupNames() {
     return this.groupNames;
   }



   public boolean equals(Object o) {
     if (this == o)
       return true;
     if (!(o instanceof ActionContributorCommandInfo)) {
       return false;
     }
     ActionContributorCommandInfo info = (ActionContributorCommandInfo)o;

     if (!this.actionName.equals(info.actionName))
       return false;
     return Arrays.equals((Object[])this.groupNames, (Object[])info.groupNames);
   }




   public int hashCode() {
     int result = this.actionName.hashCode();

     for (int i = 0; i < this.groupNames.length; i++) {

       String name = this.groupNames[i];
       result = 29 * result + name.hashCode();
     }
     return result;
   }



   public String getType() {
     return this.type;
   }



   public String getActionGroupID() {
     return this.actionGroupID;
   }
 }


