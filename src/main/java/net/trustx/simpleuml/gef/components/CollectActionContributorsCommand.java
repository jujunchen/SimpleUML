 package net.trustx.simpleuml.gef.components;

 import java.awt.Component;
 import java.util.HashSet;
 import net.trustx.simpleuml.components.ActionContributor;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;





 class CollectActionContributorsCommand
   implements SelectableCommand
 {
   private final HashSet actionContributors;
   private Component originator;

   public CollectActionContributorsCommand(HashSet actionContributors, Component originator) {
     this.actionContributors = actionContributors;
     this.originator = originator;
   }




   public void preExecution() {}



   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof ActionContributor)
     {
       this.actionContributors.add(selectable);
     }
     return true;
   }



   public void postExecution() {
     if (this.originator instanceof ActionContributor) {

       ActionContributor contributor = (ActionContributor)this.originator;
       this.actionContributors.add(contributor);
     }
   }
 }


