 package net.trustx.simpleuml.dependencydiagram.components;
 
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
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.dependencydiagram.configuration.DependencyDiagramSettings;
 import net.trustx.simpleuml.util.EditorHelper;
 import net.trustx.simpleuml.util.UMLUtils;
 
 

 
 public class PsiClassTitlePanel
   extends JPanel
 {
   public PsiClassTitlePanel(DependencyDiagramComponent dependencyDiagramComponent, PsiDependencyComponent psiClassComponent) {
     setOpaque(false);
     setLayout(new BoxLayout(this, 0));
     UMLLabel classNameLabel = new UMLLabel();
     
     classNameLabel.setFont(dependencyDiagramComponent.getDiagramSettings().getDiagramTitleFont());
     
     PsiClass psiClass = psiClassComponent.getPsiClass();
     
     UMLLabel modLabel = new UMLLabel();
     modLabel.setText(UMLUtils.getUMLModifierString(psiClass.getModifierList(), dependencyDiagramComponent.getDiagramSettings().isLongModifier()));
     modLabel.setFont(dependencyDiagramComponent.getDiagramSettings().getDiagramFont());
     
     String niceName = UMLUtils.getNiceClassName(psiClass);
     if (psiClass.hasTypeParameters())
     {
       niceName = niceName + getParameterString(psiClass);
     }
     
     classNameLabel.setText(niceName);
     
     classNameLabel.setToolTipText(psiClassComponent.getQualifiedClassName());
     classNameLabel.setPsiElement((PsiElement)psiClass);
     
     Font normalFont = dependencyDiagramComponent.getDiagramSettings().getDiagramFont();
     
     if (psiClass.getModifierList().hasModifierProperty("abstract") || psiClass.isInterface()) {
       
       classNameLabel.setFont(classNameLabel.getFont().deriveFont(3));
       modLabel.setFont(classNameLabel.getFont());
       normalFont = normalFont.deriveFont(2);
       if (psiClass.isInterface())
       {
         psiClassComponent.setBackground(dependencyDiagramComponent.getDiagramSettings().getInterfaceBackgroundColor());
       }
       else
       {
         psiClassComponent.setBackground(dependencyDiagramComponent.getDiagramSettings().getAbstractClassBackgroundColor());
       }
     
     } else {
       
       psiClassComponent.setBackground(dependencyDiagramComponent.getDiagramSettings().getClassBackgroundColor());
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
     
     EditorHelper.initQuickSourceSupport(dependencyDiagramComponent.getDiagramSettings().getQuickSourceLinkColor(), dependencyDiagramComponent.getProject(), (JComponent)psiClassComponent, classNameLabel, psiClass.getNameIdentifier(), underscored, false, true);
 
 
 
 
 
 
 
     
     add((Component)modLabel);
     add((Component)classNameLabel);
     
     PsiJavaCodeReferenceElement[] elements = UMLUtils.getReferenceElements(psiClass.getExtendsList());
     
     DependencyDiagramSettings ds = dependencyDiagramComponent.getDiagramSettings();
     
     if (!psiClass.isInterface())
     {
       
       if (elements.length == 1) {
         
         UMLLabel extendsLabel = new UMLLabel(" extends ");
         
         PsiElement element = elements[0].resolve();
         
         if (element == null) {
           
           addErrorLabel(ds, extendsLabel, normalFont, elements);
         }
         else if (element instanceof PsiType) {
           
           addTypeLabel(dependencyDiagramComponent, psiClassComponent, element, normalFont, ds, extendsLabel);
         }
         else if (element instanceof PsiClass) {
           
           addClassLabel(dependencyDiagramComponent, psiClassComponent, element, normalFont, ds, extendsLabel);
         } 
       } 
     }
 
 
 
 
 
     
     add(Box.createHorizontalGlue());
   }
 
 
   
   private String getParameterString(PsiClass psiClass) {
     PsiTypeParameterList list = psiClass.getTypeParameterList();
     if (list == null) return "<null>"; 
     PsiTypeParameter[] parameters = list.getTypeParameters();
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
 
 
   
   private void addClassLabel(DependencyDiagramComponent dependencyDiagramComponent, PsiDependencyComponent psiClassComponent, PsiElement element, Font normalFont, DependencyDiagramSettings ds, UMLLabel extendsLabel) {
     PsiTypeComponent classComponent = new PsiTypeComponent(dependencyDiagramComponent, psiClassComponent, (PsiClass)element, normalFont, false, true, true);
     
     addPsiTypeComponent(psiClassComponent, classComponent, ds, extendsLabel, normalFont);
   }
 
 
   
   private void addTypeLabel(DependencyDiagramComponent dependencyDiagramComponent, PsiDependencyComponent psiClassComponent, PsiElement element, Font normalFont, DependencyDiagramSettings ds, UMLLabel extendsLabel) {
     PsiTypeComponent classComponent = new PsiTypeComponent(dependencyDiagramComponent, (JComponent)psiClassComponent, (PsiType)element, normalFont, false, true, true);
     
     addPsiTypeComponent(psiClassComponent, classComponent, ds, extendsLabel, normalFont);
   }
 
 
   
   private void addPsiTypeComponent(PsiDependencyComponent psiClassComponent, PsiTypeComponent classComponent, DependencyDiagramSettings ds, UMLLabel extendsLabel, Font normalFont) {
     psiClassComponent.addExtendsClassname(classComponent.getQualifiedClassName());
     
     if (ds.getExtendsBehaviour() == 1) {
       
       extendsLabel.setFont(normalFont);
       add((Component)extendsLabel);
       
       add(classComponent);
     } 
   }
 
 
   
   private void addErrorLabel(DependencyDiagramSettings ds, UMLLabel extendsLabel, Font normalFont, PsiJavaCodeReferenceElement[] elements) {
     if (ds.getExtendsBehaviour() == 1 || ds.getExtendsBehaviour() == 0) {
       
       extendsLabel.setFont(normalFont);
       add((Component)extendsLabel);
       
       UMLLabel errorLabel = new UMLLabel(elements[0].getCanonicalText());
       errorLabel.setFont(normalFont);
       errorLabel.setForeground(Color.RED);
       add((Component)errorLabel);
     } 
   }
 }


