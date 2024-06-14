 package net.trustx.simpleuml.classdiagram.configuration;
 
 import java.awt.Component;
 import javax.swing.DefaultListCellRenderer;
 import javax.swing.JLabel;
 import javax.swing.JList;
 import net.trustx.simpleuml.classdiagram.util.PsiComponentUtils;
 

 public class ModifierListCellRenderer
   extends DefaultListCellRenderer
 {
   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
     JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
     label.setText(PsiComponentUtils.convertModifierIntToString(Integer.parseInt(value.toString())));
     return label;
   }
 }


