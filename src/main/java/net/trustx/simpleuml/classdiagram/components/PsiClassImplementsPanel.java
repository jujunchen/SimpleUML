 package net.trustx.simpleuml.classdiagram.components;

 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiJavaCodeReferenceElement;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Font;
 import javax.swing.Box;
 import javax.swing.BoxLayout;
 import javax.swing.JPanel;
 import net.trustx.simpleuml.classdiagram.configuration.ClassDiagramSettings;
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.util.UMLUtils;



 public class PsiClassImplementsPanel
   extends JPanel
 {
   public PsiClassImplementsPanel(ClassDiagramComponent classDiagramComponent, PsiClassComponent psiClassComponent) {
     ClassDiagramSettings ds = classDiagramComponent.getDiagramSettings();

     setOpaque(false);
     setLayout(new BoxLayout(this, 1));

     PsiClass psiClass = psiClassComponent.getPsiClass();

     PsiJavaCodeReferenceElement[] elements = getReferenceElements(psiClass);

     int interfaceCount = 0;

     if (elements.length > 0) {

       UMLLabel label = getExtendsImplementsLabel(psiClass);

       Font normalFont = getFont(classDiagramComponent, psiClass);

       Box firstBox = Box.createHorizontalBox();
       label.setFont(normalFont);

       firstBox.add(Box.createHorizontalStrut(20));
       firstBox.add((Component)label);
       firstBox.add(Box.createHorizontalStrut(5));

       for (int i = 0; i < elements.length; i++) {

         PsiJavaCodeReferenceElement element = elements[i];
         PsiClass extendedClass = (PsiClass)element.resolve();



         if (interfaceCount == 0) {

           if (extendedClass == null)
           {
             UMLLabel umlLabel = new UMLLabel(element.getText());
             umlLabel.setFont(normalFont);
             umlLabel.setForeground(Color.RED);
             firstBox.add((Component)umlLabel);
             firstBox.add(Box.createHorizontalGlue());
             add(firstBox);
             interfaceCount++;
           }
           else
           {
             PsiTypeComponent scc = new PsiTypeComponent(classDiagramComponent, psiClassComponent, extendedClass, normalFont, false, true, true);
             if ((ds.getImplementsBehaviour() == 4 && !classDiagramComponent.containsClass(scc.getQualifiedClassName())) || ds.getImplementsBehaviour() == 1) {

               firstBox.add(scc);
               firstBox.add(Box.createHorizontalGlue());
               add(firstBox);
               interfaceCount++;
             }

             addExtendsImplementsClassname(psiClass, psiClassComponent, scc);
           }

         }
         else {

           Box tempBox = Box.createHorizontalBox();
           if (extendedClass == null) {

             UMLLabel umlLabel = new UMLLabel(element.getText());
             umlLabel.setFont(normalFont);
             umlLabel.setForeground(Color.RED);
             tempBox.add(Box.createHorizontalStrut((label.getPreferredSize()).width + 25));
             tempBox.add((Component)umlLabel);
             tempBox.add(Box.createHorizontalGlue());
             add(tempBox);
             interfaceCount++;
           }
           else {

             PsiTypeComponent scc = new PsiTypeComponent(classDiagramComponent, psiClassComponent, extendedClass, normalFont, false, true, true);
             if ((ds.getImplementsBehaviour() == 4 && !classDiagramComponent.containsClass(scc.getQualifiedClassName())) || ds.getImplementsBehaviour() == 1) {

               tempBox.add(Box.createHorizontalStrut((label.getPreferredSize()).width + 25));
               tempBox.add(scc);
               tempBox.add(Box.createHorizontalGlue());
               add(tempBox);
               interfaceCount++;
             }

             addExtendsImplementsClassname(psiClass, psiClassComponent, scc);
           }
         }
       }
     }


     if (ds.getImplementsBehaviour() == 2) {

       removeAll();
       setOpaque(false);
       setLayout(new BoxLayout(this, 1));
     }
   }




   private void addExtendsImplementsClassname(PsiClass psiClass, PsiClassComponent psiClassComponent, PsiTypeComponent scc) {
     if (psiClass.isInterface()) {

       psiClassComponent.addExtendsClassname(scc.getQualifiedClassName());
     }
     else {

       psiClassComponent.addImplementsClassname(scc.getQualifiedClassName());
     }
   }



   private Font getFont(ClassDiagramComponent classDiagramComponent, PsiClass psiClass) {
     Font normalFont = classDiagramComponent.getDiagramSettings().getDiagramFont();
     if (psiClass.getModifierList().hasModifierProperty("abstract"))
     {
       normalFont = normalFont.deriveFont(2);
     }
     return normalFont;
   }



   private PsiJavaCodeReferenceElement[] getReferenceElements(PsiClass psiClass) {
     PsiJavaCodeReferenceElement[] elements;
     if (psiClass.isInterface()) {

       elements = UMLUtils.getReferenceElements(psiClass.getExtendsList());
     }
     else {

       elements = UMLUtils.getReferenceElements(psiClass.getImplementsList());
     }
     return elements;
   }



   private UMLLabel getExtendsImplementsLabel(PsiClass psiClass) {
     UMLLabel label;
     if (psiClass.isInterface()) {

       label = new UMLLabel("extends");
     }
     else {

       label = new UMLLabel("implements");
     }
     return label;
   }
 }


