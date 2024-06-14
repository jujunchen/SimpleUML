 package net.trustx.simpleuml.classdiagram.components;

 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiJavaCodeReferenceElement;
 import com.intellij.psi.PsiType;
 import com.intellij.psi.PsiTypeParameter;
 import com.intellij.psi.PsiTypeParameterList;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Font;
 import javax.swing.Box;
 import javax.swing.BoxLayout;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import net.trustx.simpleuml.classdiagram.configuration.ClassDiagramSettings;
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.util.EditorHelper;
 import net.trustx.simpleuml.util.UMLUtils;


 public class PsiClassTitlePanel
   extends JPanel
 {
   public PsiClassTitlePanel(ClassDiagramComponent classDiagramComponent, PsiClassComponent psiClassComponent) {
     setOpaque(false);
     setLayout(new BoxLayout(this, 0));
     UMLLabel classNameLabel = new UMLLabel();

     classNameLabel.setFont(classDiagramComponent.getDiagramSettings().getDiagramTitleFont());

     PsiClass psiClass = psiClassComponent.getPsiClass();

     UMLLabel modLabel = new UMLLabel();
     modLabel.setText(UMLUtils.getUMLModifierString(psiClass.getModifierList(), classDiagramComponent.getDiagramSettings().isLongModifier()));
     modLabel.setFont(classDiagramComponent.getDiagramSettings().getDiagramFont());

     String niceName = UMLUtils.getNiceClassName(psiClass);
     if (psiClass.hasTypeParameters())
     {

       niceName = niceName + getParameterString(psiClass);
     }

     classNameLabel.setText(niceName);

     classNameLabel.setToolTipText(psiClassComponent.getQualifiedClassName());
     classNameLabel.setPsiElement((PsiElement)psiClass);

     Font normalFont = classDiagramComponent.getDiagramSettings().getDiagramFont();

     if (psiClass.getModifierList().hasModifierProperty("abstract") || psiClass.isInterface()) {

       classNameLabel.setFont(classNameLabel.getFont().deriveFont(3));
       modLabel.setFont(classNameLabel.getFont());
       normalFont = normalFont.deriveFont(2);
       if (psiClass.isInterface())
       {
         psiClassComponent.setBackground(classDiagramComponent.getDiagramSettings().getInterfaceBackgroundColor());
       }
       else
       {
         psiClassComponent.setBackground(classDiagramComponent.getDiagramSettings().getAbstractClassBackgroundColor());
       }

     } else {

       psiClassComponent.setBackground(classDiagramComponent.getDiagramSettings().getClassBackgroundColor());
     }


     if (psiClassComponent.getColor() != null)
     {
       psiClassComponent.setBackground(psiClassComponent.getColor());
     }

     psiClassComponent.setOpaque(true);
     boolean underscored = false;
     if (psiClass.getModifierList().hasModifierProperty("static"))
     {
       underscored = true;
     }
     classNameLabel.setUnderscored(underscored);

     EditorHelper.initQuickSourceSupport(classDiagramComponent.getDiagramSettings().getQuickSourceLinkColor(), classDiagramComponent.getProject(), (JComponent)psiClassComponent, classNameLabel, psiClass.getNameIdentifier(), underscored, false, true);









     add((Component)modLabel);
     add((Component)classNameLabel);

     PsiJavaCodeReferenceElement[] elements = UMLUtils.getReferenceElements(psiClass.getExtendsList());

     ClassDiagramSettings ds = classDiagramComponent.getDiagramSettings();

     if (!psiClass.isInterface())
     {

       if (elements.length == 1) {

         UMLLabel extendsLabel = new UMLLabel(" extends ");

         PsiElement element = elements[0].resolve();

         if (element == null) {

           addErrorLabel(ds, extendsLabel, normalFont, elements);
         }
         else if (element instanceof PsiType) {

           addTypeLabel(classDiagramComponent, psiClassComponent, element, normalFont, ds, extendsLabel);
         }
         else if (element instanceof PsiClass) {

           addClassLabel(classDiagramComponent, psiClassComponent, element, normalFont, ds, extendsLabel);
         }
       }
     }






     add(Box.createHorizontalGlue());
   }





   private String getParameterString(PsiClass psiClass) {
     PsiTypeParameterList parameterList = psiClass.getTypeParameterList();
     if (parameterList == null) return "<null>";

     PsiTypeParameter[] parameters = parameterList.getTypeParameters();
     StringBuffer sb = new StringBuffer("<");
     for (int i = 0; i < parameters.length; i++) {

       PsiTypeParameter parameter = parameters[i];

       String name = parameter.getName();
       if (name != null) {

         sb.append(name);
         sb.append(", ");
       }
       else {

         int index = parameter.getIndex();
         sb.append("P");
         sb.append(index);
         sb.append(", ");
       }
     }

     sb.delete(sb.length() - 2, sb.length());
     sb.append(">");
     return sb.toString();
   }



   private void addClassLabel(ClassDiagramComponent classDiagramComponent, PsiClassComponent psiClassComponent, PsiElement element, Font normalFont, ClassDiagramSettings ds, UMLLabel extendsLabel) {
     PsiTypeComponent classComponent = new PsiTypeComponent(classDiagramComponent, psiClassComponent, (PsiClass)element, normalFont, false, true, true);

     addPsiTypeComponent(psiClassComponent, classComponent, ds, classDiagramComponent, extendsLabel, normalFont);
   }



   private void addTypeLabel(ClassDiagramComponent classDiagramComponent, PsiClassComponent psiClassComponent, PsiElement element, Font normalFont, ClassDiagramSettings ds, UMLLabel extendsLabel) {
     PsiTypeComponent classComponent = new PsiTypeComponent(classDiagramComponent, (JComponent)psiClassComponent, (PsiType)element, normalFont, false, true, true);

     addPsiTypeComponent(psiClassComponent, classComponent, ds, classDiagramComponent, extendsLabel, normalFont);
   }



   private void addPsiTypeComponent(PsiClassComponent psiClassComponent, PsiTypeComponent classComponent, ClassDiagramSettings ds, ClassDiagramComponent classDiagramComponent, UMLLabel extendsLabel, Font normalFont) {
     psiClassComponent.addExtendsClassname(classComponent.getQualifiedClassName());

     if ((ds.getExtendsBehaviour() == 4 && !classDiagramComponent.containsClass(classComponent.getQualifiedClassName())) || ds.getExtendsBehaviour() == 1) {

       extendsLabel.setFont(normalFont);
       add((Component)extendsLabel);

       add(classComponent);
     }
   }



   private void addErrorLabel(ClassDiagramSettings ds, UMLLabel extendsLabel, Font normalFont, PsiJavaCodeReferenceElement[] elements) {
     if (ds.getExtendsBehaviour() == 1 || ds.getExtendsBehaviour() == 4) {

       extendsLabel.setFont(normalFont);
       add((Component)extendsLabel);

       UMLLabel errorLabel = new UMLLabel(elements[0].getCanonicalText());
       errorLabel.setFont(normalFont);
       errorLabel.setForeground(Color.RED);
       add((Component)errorLabel);
     }
   }
 }


