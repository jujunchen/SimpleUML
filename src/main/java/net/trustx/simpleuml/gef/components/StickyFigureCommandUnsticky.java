 package net.trustx.simpleuml.gef.components;
 
 import java.util.HashMap;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 
 

 public class StickyFigureCommandUnsticky
   extends ActionContributorCommand
 {
   private StickyFigureComponent stickyFigureComponent;
   
   public StickyFigureCommandUnsticky(String text, StickyFigureComponent stickyFigureComponent) {
     super(text);
     this.stickyFigureComponent = stickyFigureComponent;
   }
 
 
   
   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     this.stickyFigureComponent.setSticky(false);
   }
 
 
   
   public String getGroupName() {
     return "Figure";
   }
 }


