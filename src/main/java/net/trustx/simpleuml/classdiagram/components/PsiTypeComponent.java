 package net.trustx.simpleuml.classdiagram.components;

 import com.intellij.psi.PsiArrayType;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiClassType;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiType;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Font;
 import javax.swing.BoxLayout;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import net.trustx.simpleuml.classdiagram.util.EventHelper;
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.util.EditorHelper;
 import net.trustx.simpleuml.util.UMLUtils;




 public class PsiTypeComponent
   extends JPanel
 {
   private UMLLabel typeLabel;
   private String qualifiedClassName;

   public PsiTypeComponent(ClassDiagramComponent classDiagramComponent, PsiClassComponent psiClassComponent, PsiClass psiClass, Font font, boolean underscored, boolean registerListeners, boolean registerToolTip) {
     setOpaque(false);
     setLayout(new BoxLayout(this, 0));

     this.qualifiedClassName = psiClass.getQualifiedName();

     String niceName = UMLUtils.getNiceClassName(psiClass);

     this.typeLabel = new UMLLabel(niceName);
     this.typeLabel.setFont(font);
     this.typeLabel.setUnderscored(underscored);
     this.typeLabel.setToolTipText(this.qualifiedClassName);
     this.typeLabel.setPsiElement((PsiElement)psiClass);
     add((Component)this.typeLabel);

     if (registerListeners) {

       EventHelper.initQuickDiagramSupport(classDiagramComponent, (JComponent)psiClassComponent, this.typeLabel, underscored, this.qualifiedClassName);
       EditorHelper.initQuickSourceSupport(classDiagramComponent.getDiagramSettings().getQuickSourceLinkColor(), classDiagramComponent.getProject(), (JComponent)psiClassComponent, this.typeLabel, psiClass.getNameIdentifier(), underscored, true, false);
     }
   }












   public PsiTypeComponent(ClassDiagramComponent classDiagramComponent, JComponent parentComponent, PsiType psiType, Font font, boolean underscored, boolean registerListeners, boolean registerToolTip) {
     setOpaque(false);
     setLayout(new BoxLayout(this, 0));

     String niceName = psiType.getPresentableText();

     boolean showLink = false;
     boolean unresolveable = false;
     PsiClass psiClass = null;

     if (psiType instanceof PsiArrayType) {

       StringBuffer arraySuffix = new StringBuffer();
       PsiType arrayPsiType = psiType;
       while (arrayPsiType instanceof PsiArrayType) {

         arraySuffix.append("[]");
         arrayPsiType = ((PsiArrayType)arrayPsiType).getComponentType();
       }
       if (arrayPsiType instanceof PsiClassType) {

         psiClass = ((PsiClassType)arrayPsiType).resolve();
         if (psiClass == null)
         {
           this.qualifiedClassName = arrayPsiType.getCanonicalText();
           showLink = false;
           unresolveable = true;
         }
         else
         {
           this.qualifiedClassName = psiClass.getQualifiedName();
           niceName = UMLUtils.getNiceClassName(psiClass) + arraySuffix;
           showLink = true;
         }

       }
     } else if (psiType instanceof PsiClassType) {

       psiClass = ((PsiClassType)psiType).resolve();
       if (psiClass == null) {

         this.qualifiedClassName = psiType.getCanonicalText();
         showLink = false;
         unresolveable = true;
       }
       else {

         this.qualifiedClassName = psiClass.getQualifiedName();

         niceName = psiType.getPresentableText();
         showLink = true;
       }
     }

     this.typeLabel = new UMLLabel(niceName);
     this.typeLabel.setFont(font);
     if (unresolveable)
     {
       this.typeLabel.setForeground(Color.RED);
     }
     this.typeLabel.setUnderscored(underscored);
     this.typeLabel.setPsiElement((PsiElement)psiClass);
     add((Component)this.typeLabel);

     if (registerListeners && showLink) {

       if (registerToolTip)
       {
         this.typeLabel.setToolTipText(this.qualifiedClassName);
       }
       EventHelper.initQuickDiagramSupport(classDiagramComponent, parentComponent, this.typeLabel, underscored, this.qualifiedClassName);
       EditorHelper.initQuickSourceSupport(classDiagramComponent.getDiagramSettings().getQuickSourceLinkColor(), classDiagramComponent.getProject(), parentComponent, this.typeLabel, psiClass.getNameIdentifier(), underscored, true, false);
     }
   }











   public String getQualifiedClassName() {
     return this.qualifiedClassName;
   }
 }


