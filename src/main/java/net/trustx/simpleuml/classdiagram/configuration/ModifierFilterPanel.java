 package net.trustx.simpleuml.classdiagram.configuration;
 
 import java.awt.GridLayout;
 import javax.swing.BorderFactory;
 import javax.swing.JFrame;
 import javax.swing.JPanel;
 

 public class ModifierFilterPanel
   extends JPanel
 {
   private ModifierFilterBox hideFilterBox;
   private ModifierFilterBox showFilterBox;
   
   public static void main(String[] args) {
     System.out.println(25);
     JFrame jFrame = new JFrame("ModifierFilterPanel");
     jFrame.setDefaultCloseOperation(3);
     jFrame.getContentPane().setLayout(new GridLayout(0, 3));
     ModifierFilterPanel comp1 = new ModifierFilterPanel();
     comp1.setBorder(BorderFactory.createTitledBorder("Fields"));
     jFrame.getContentPane().add(comp1);
     ModifierFilterPanel comp2 = new ModifierFilterPanel();
     comp2.setBorder(BorderFactory.createTitledBorder("Constructors"));
     jFrame.getContentPane().add(comp2);
     ModifierFilterPanel comp3 = new ModifierFilterPanel();
     comp3.setBorder(BorderFactory.createTitledBorder("Methods"));
     jFrame.getContentPane().add(comp3);
     jFrame.pack();
     jFrame.setVisible(true);
   }
 
 
 
 
 
 
   
   public ModifierFilterPanel() {
     setLayout(new GridLayout(0, 1, 10, 10));
     
     this.hideFilterBox = new ModifierFilterBox("Hide:");
     add(this.hideFilterBox);
     this.showFilterBox = new ModifierFilterBox("Show:");
     add(this.showFilterBox);
   }
 
 
   
   public ModifierFilterBox getHideFilterBox() {
     return this.hideFilterBox;
   }
 
 
   
   public ModifierFilterBox getShowFilterBox() {
     return this.showFilterBox;
   }
 }


