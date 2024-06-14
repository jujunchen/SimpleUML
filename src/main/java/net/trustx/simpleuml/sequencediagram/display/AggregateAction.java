 package net.trustx.simpleuml.sequencediagram.display;
 
 import java.awt.MenuItem;
 import java.awt.Point;
 import java.awt.PopupMenu;
 import java.awt.event.MouseEvent;
 import javax.swing.JViewport;
 import net.trustx.simpleuml.sequencediagram.components.DiagramSelectionListener;
 
 
 
 
 
 
 
 
 
 public class AggregateAction
   extends DiagramSelectionListener
 {
   private PopupMenu rightClickMenu = new PopupMenu("Link Actions");
 
   
   public AggregateAction(Display d, JViewport view) {
     super(d, view);
     view.add(this.rightClickMenu);
   }
 
   
   public void addAction(AggregateActionListener l) {
     MenuItem item = new MenuItem(l.getName());
     if (this.rightClickMenu.getItemCount() > 0)
       this.rightClickMenu.addSeparator(); 
     this.rightClickMenu.add(item);
     item.addActionListener(l);
     l.setPointer(this);
   }
 
   
   public void mouseClicked(MouseEvent e) {
     Point p = getRelativePosition(e);
     if (e.getButton() != 1 && this.disp.getLinkAt(p) != null) {
       
       super.mouseClicked(e);
       this.rightClickMenu.show(this.view, (int)p.getX(), (int)p.getY());
     } 
   }
 }


