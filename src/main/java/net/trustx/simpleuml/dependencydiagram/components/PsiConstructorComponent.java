 package net.trustx.simpleuml.dependencydiagram.components;
 
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiMethod;
 import com.intellij.psi.PsiModifierList;
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
 
 
 

 public class PsiConstructorComponent
   extends JPanel
 {
   private UMLLabel accessLabel;
   private UMLLabel constructorNamelabel;
   private JPanel parameterBox;
   
   public PsiConstructorComponent(DependencyDiagramComponent dependencyDiagramComponent, PsiDependencyComponent psiClassComponent, PsiMethod psiMethodConstructor) {
     setOpaque(false);
     setLayout(new BoxLayout(this, 0));
     
     PsiModifierList psiModifierList = psiMethodConstructor.getModifierList();
     this.accessLabel = new UMLLabel(UMLUtils.getUMLModifierString(psiModifierList, dependencyDiagramComponent.getDiagramSettings().isLongModifier()));
     Font font = dependencyDiagramComponent.getDiagramSettings().getDiagramFont();
     add((Component)this.accessLabel);
     add(Box.createHorizontalStrut(5));
     
     boolean underscored = false;
     
     this.accessLabel.setFont(font);
     
     this.constructorNamelabel = new UMLLabel(psiMethodConstructor.getName());
     this.constructorNamelabel.setFont(font);
     this.constructorNamelabel.setUnderscored(underscored);
     this.constructorNamelabel.setPsiElement((PsiElement)psiMethodConstructor);
     add((Component)this.constructorNamelabel);
     
     EditorHelper.initQuickSourceSupport(dependencyDiagramComponent.getDiagramSettings().getQuickSourceLinkColor(), dependencyDiagramComponent.getProject(), (JComponent)psiClassComponent, this.constructorNamelabel, psiMethodConstructor.getNameIdentifier(), underscored, false, false);
 
 
 
 
 
 
 
 
     
     UMLLabel openLabel = new UMLLabel("(");
     openLabel.setFont(font);
     openLabel.setUnderscored(underscored);
     add((Component)openLabel);
     
     this.parameterBox = new JPanel();
     this.parameterBox.setLayout(new BoxLayout(this.parameterBox, 0));
     this.parameterBox.setOpaque(false);
     
     PsiParameter[] psiParameters = psiMethodConstructor.getParameterList().getParameters();
     boolean showParameters = dependencyDiagramComponent.getDiagramSettings().isShowParameters();
     
     for (int i = 0; i < psiParameters.length; i++) {
       
       PsiParameter psiParameter = psiParameters[i];
       
       UMLLabel paramNameLabel = new UMLLabel(psiParameter.getName() + ":");
       paramNameLabel.setFont(font);
       paramNameLabel.setUnderscored(underscored);
       paramNameLabel.setPsiElement((PsiElement)psiParameter);
       this.parameterBox.add((Component)paramNameLabel);
       
       PsiTypeComponent typeComponent = new PsiTypeComponent(dependencyDiagramComponent, (JComponent)psiClassComponent, psiParameter.getType(), font, underscored, true, showParameters);
       this.parameterBox.add(typeComponent);
       if (i != psiParameters.length - 1) {
         
         UMLLabel commaLabel = new UMLLabel(",");
         commaLabel.setFont(font);
         commaLabel.setUnderscored(underscored);
         this.parameterBox.add((Component)commaLabel);
         
         UMLLabel temp1Label = new UMLLabel(" ");
         temp1Label.setFont(font);
         temp1Label.setUnderscored(underscored);
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
       placeholderLabel.setUnderscored(underscored);
       placeholderLabel.setToolTipComponent(this.parameterBox);
       add((Component)placeholderLabel);
     } 
     
     UMLLabel closeLabel = new UMLLabel(")");
     closeLabel.setFont(font);
     closeLabel.setUnderscored(underscored);
     add((Component)closeLabel);
     
     add(Box.createHorizontalGlue());
   }
 }


