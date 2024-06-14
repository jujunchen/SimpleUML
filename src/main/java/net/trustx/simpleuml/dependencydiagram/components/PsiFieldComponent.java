 package net.trustx.simpleuml.dependencydiagram.components;

 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiField;
 import java.awt.Component;
 import java.awt.Font;
 import javax.swing.Box;
 import javax.swing.BoxLayout;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.util.EditorHelper;
 import net.trustx.simpleuml.util.UMLUtils;




 public class PsiFieldComponent
   extends JPanel
 {
   private UMLLabel accessLabel;
   private UMLLabel fieldNamelabel;
   private boolean underscored;
   private PsiTypeComponent typeComponent;

   public PsiFieldComponent(DependencyDiagramComponent dependencyDiagramComponent, PsiDependencyComponent psiClassComponent, PsiField psiField) {
     setOpaque(false);
     setLayout(new BoxLayout(this, 0));

     this.accessLabel = new UMLLabel(UMLUtils.getUMLModifierString(psiField.getModifierList(), dependencyDiagramComponent.getDiagramSettings().isLongModifier()));
     Font font = dependencyDiagramComponent.getDiagramSettings().getDiagramFont();
     add((Component)this.accessLabel);
     add(Box.createHorizontalStrut(5));

     this.underscored = false;

     if (psiField.getModifierList().hasModifierProperty("abstract"))
     {
       font = font.deriveFont(2);
     }
     if (psiField.getModifierList().hasModifierProperty("static"))
     {
       this.underscored = true;
     }

     this.accessLabel.setFont(font);

     this.fieldNamelabel = new UMLLabel(psiField.getName());
     this.fieldNamelabel.setFont(font);
     this.fieldNamelabel.setUnderscored(this.underscored);
     this.fieldNamelabel.setPsiElement((PsiElement)psiField);
     add((Component)this.fieldNamelabel);

     EditorHelper.initQuickSourceSupport(dependencyDiagramComponent.getDiagramSettings().getQuickSourceLinkColor(), dependencyDiagramComponent.getProject(), (JComponent)psiClassComponent, this.fieldNamelabel, psiField.getNameIdentifier(), this.underscored, false, false);









     UMLLabel openLabel = new UMLLabel(":");
     openLabel.setFont(font);
     openLabel.setUnderscored(false);
     add((Component)openLabel);

     this.typeComponent = new PsiTypeComponent(dependencyDiagramComponent, (JComponent)psiClassComponent, psiField.getType(), font, false, true, true);
     psiClassComponent.addFieldsClassname(this.typeComponent.getQualifiedClassName());
     add(this.typeComponent);

     add(Box.createHorizontalGlue());
   }



   public String getAccessLabelText() {
     return this.accessLabel.getText();
   }



   public String getFieldNameLabelText() {
     return this.fieldNamelabel.getText();
   }



   public String getClassName() {
     return this.typeComponent.getQualifiedClassName();
   }
 }


