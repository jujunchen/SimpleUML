 package net.trustx.simpleuml.dependencydiagram.components;

 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiMethod;
 import com.intellij.psi.PsiParameter;
 import java.awt.Component;
 import java.awt.Dimension;
 import java.awt.Font;
 import javax.swing.Box;
 import javax.swing.BoxLayout;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.util.EditorHelper;
 import net.trustx.simpleuml.util.UMLUtils;




 public class PsiMethodComponent
   extends JPanel
 {
   private UMLLabel accessLabel;
   private UMLLabel methodNamelabel;
   private boolean underscored;
   private JPanel parameterBox;

   public PsiMethodComponent(PsiDependencyComponent psiClassComponent, DependencyDiagramComponent dependencyDiagramComponent, PsiMethod psiMethod) {
     setOpaque(false);
     setLayout(new BoxLayout(this, 0));

     this.accessLabel = new UMLLabel(UMLUtils.getUMLModifierString(psiMethod.getModifierList(), dependencyDiagramComponent.getDiagramSettings().isLongModifier()));

     Font font = dependencyDiagramComponent.getDiagramSettings().getDiagramFont();
     add((Component)this.accessLabel);
     add(Box.createHorizontalStrut(5));

     this.underscored = false;

     if (psiMethod.getModifierList().hasModifierProperty("abstract"))
     {
       font = font.deriveFont(2);
     }
     if (psiMethod.getModifierList().hasModifierProperty("static"))
     {
       this.underscored = true;
     }

     this.accessLabel.setFont(font);

     this.methodNamelabel = new UMLLabel(psiMethod.getName());
     this.methodNamelabel.setFont(font);
     this.methodNamelabel.setUnderscored(this.underscored);
     this.methodNamelabel.setPsiElement((PsiElement)psiMethod);
     add((Component)this.methodNamelabel);

     EditorHelper.initQuickSourceSupport(dependencyDiagramComponent.getDiagramSettings().getQuickSourceLinkColor(), dependencyDiagramComponent.getProject(), (JComponent)psiClassComponent, this.methodNamelabel, psiMethod.getNameIdentifier(), this.underscored, false, false);










     UMLLabel openLabel = new UMLLabel("(");
     openLabel.setFont(font);
     openLabel.setUnderscored(this.underscored);
     add((Component)openLabel);

     this.parameterBox = new JPanel();
     this.parameterBox.setLayout(new BoxLayout(this.parameterBox, 0));
     this.parameterBox.setOpaque(false);

     PsiParameter[] psiParameters = psiMethod.getParameterList().getParameters();
     boolean showParameters = dependencyDiagramComponent.getDiagramSettings().isShowParameters();

     for (int i = 0; i < psiParameters.length; i++) {

       PsiParameter psiParameter = psiParameters[i];

       UMLLabel paramNameLabel = new UMLLabel(psiParameter.getName() + ":");
       paramNameLabel.setFont(font);
       paramNameLabel.setUnderscored(this.underscored);
       paramNameLabel.setPsiElement((PsiElement)psiParameter);
       this.parameterBox.add((Component)paramNameLabel);
       EditorHelper.initQuickSourceSupport(dependencyDiagramComponent.getDiagramSettings().getQuickSourceLinkColor(), dependencyDiagramComponent.getProject(), (JComponent)psiClassComponent, paramNameLabel, psiParameter.getNameIdentifier(), this.underscored, false, false);









       PsiTypeComponent typeComponent = new PsiTypeComponent(dependencyDiagramComponent, (JComponent)psiClassComponent, psiParameter.getType(), font, this.underscored, true, showParameters);
       this.parameterBox.add(typeComponent);
       if (i != psiParameters.length - 1) {

         UMLLabel commaLabel = new UMLLabel(",");
         commaLabel.setFont(font);
         commaLabel.setUnderscored(this.underscored);
         this.parameterBox.add((Component)commaLabel);

         UMLLabel temp1Label = new UMLLabel(" ");
         temp1Label.setFont(font);
         temp1Label.setUnderscored(this.underscored);
         temp1Label.setMinimumSize(new Dimension(5, (temp1Label.getMinimumSize()).height));
         temp1Label.setPreferredSize(new Dimension(5, (temp1Label.getPreferredSize()).height));
         temp1Label.setMaximumSize(new Dimension(5, 32767));
         this.parameterBox.add((Component)temp1Label);
       }
     }

     if (showParameters) {

       add(this.parameterBox);
     }
     else if (psiParameters.length > 0) {

       UMLLabel placeholderLabel = new UMLLabel("...");
       placeholderLabel.setFont(font);
       placeholderLabel.setUnderscored(this.underscored);
       placeholderLabel.setToolTipComponent(this.parameterBox);
       add((Component)placeholderLabel);
     }

     UMLLabel closeLabel = new UMLLabel(")");
     closeLabel.setFont(font);
     closeLabel.setUnderscored(this.underscored);
     add((Component)closeLabel);

     UMLLabel delimLabel = new UMLLabel(":");
     delimLabel.setFont(font);
     delimLabel.setUnderscored(false);
     add((Component)delimLabel);

     PsiTypeComponent psiTypeComponent = new PsiTypeComponent(dependencyDiagramComponent, (JComponent)psiClassComponent, psiMethod.getReturnType(), font, false, true, true);
     add(psiTypeComponent);

     add(Box.createHorizontalGlue());
   }
 }


