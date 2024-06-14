 package net.trustx.simpleuml.gef.components;

 import java.util.HashMap;
 import net.trustx.simpleuml.components.ActionContributorCommand;




 public class StickyFigureCommandSticky
   extends ActionContributorCommand
 {
   private StickyFigureComponent stickyFigureComponent;

   public StickyFigureCommandSticky(String text, StickyFigureComponent stickyFigureComponent) {
     super(text);
     this.stickyFigureComponent = stickyFigureComponent;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     this.stickyFigureComponent.setSticky(true);
   }



   public String getGroupName() {
     return "Figure";
   }
 }


