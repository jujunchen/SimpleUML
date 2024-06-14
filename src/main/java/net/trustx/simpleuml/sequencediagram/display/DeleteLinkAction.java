 package net.trustx.simpleuml.sequencediagram.display;
 
 import java.awt.event.ActionEvent;
 import net.trustx.simpleuml.sequencediagram.components.DiagramSelectionListener;
 import net.trustx.simpleuml.sequencediagram.model.Link;
 import net.trustx.simpleuml.sequencediagram.model.Model;
 
 
 
 
 
 
 
 
 
 public class DeleteLinkAction
   extends DiagramSelectionListener
   implements AggregateActionListener
 {
   private static final String DELETE_LINK_NAME = "Delete Link";
   private DiagramSelectionListener aggregateListener;
   private Display disp;
   private Model model;
   
   public DeleteLinkAction(Display d, Model m) {
     this.disp = d;
     this.model = m;
   }
 
   
   public String getName() {
     return "Delete Link";
   }
 
   
   public void setPointer(DiagramSelectionListener l) {
     this.aggregateListener = l;
   }
 
   
   public void actionPerformed(ActionEvent e) {
     DisplayLink dl = this.disp.getLinkAt(this.aggregateListener.getCurrentRelativePosition());
     Link link = this.model.getLink(dl.getSeq());
     link.delete();
     this.model.expunge();
   }
 }


