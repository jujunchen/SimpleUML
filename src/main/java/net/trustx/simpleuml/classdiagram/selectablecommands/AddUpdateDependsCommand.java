 package net.trustx.simpleuml.classdiagram.selectablecommands;
 
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElementVisitor;
 import java.util.HashSet;
 import java.util.Iterator;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.ConnectorActionContributor;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.classdiagram.configuration.ClassDiagramSettings;
 import net.trustx.simpleuml.components.ActionContributor;
 import net.trustx.simpleuml.gef.Figure;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.gef.anchor.FigureAnchor;
 import net.trustx.simpleuml.gef.components.DiagramPane;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.connector.Connector;
 import net.trustx.simpleuml.gef.connector.ConnectorDecorator;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorDepends;
 import net.trustx.simpleuml.gef.connector.ConnectorDirect;
 import net.trustx.simpleuml.util.FindReferenceVisitor;
 

 public class AddUpdateDependsCommand
   implements SelectableCommand
 {
   private ClassDiagramComponent classDiagramComponent;
   
   public AddUpdateDependsCommand(ClassDiagramComponent classDiagramComponent) {
     this.classDiagramComponent = classDiagramComponent;
   }
 
 
 
   
   public void preExecution() {}
 
 
   
   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof PsiClassComponent) {
       
       PsiClassComponent psiClassComponent = (PsiClassComponent)selectable;
       return executeCommand(psiClassComponent);
     } 
     return true;
   }
 
 
   
   public boolean executeCommand(PsiClassComponent psiClassComponent) {
     this.classDiagramComponent.getConnectorManager().removeConnectors((FigureComponent)psiClassComponent, 2, 16);
     
     PsiClass psiClass = psiClassComponent.getPsiClass();
     if (psiClass == null)
     {
       return true;
     }
     
     FindReferenceVisitor frv = new FindReferenceVisitor();
     psiClass.accept((PsiElementVisitor)frv);
 
 
     
     HashSet hashSet = frv.getList();
     ClassDiagramSettings ds = this.classDiagramComponent.getDiagramSettings();
     
     for (Iterator<PsiClass> psiIterator = hashSet.iterator(); psiIterator.hasNext(); ) {
       
       PsiClass refClass = psiIterator.next();
       String qname = refClass.getQualifiedName();
       PsiClassComponent refClassComponent = this.classDiagramComponent.getPsiClassComponent(qname);
       if (refClassComponent != null && qname != null && !qname.equals(psiClass.getQualifiedName())) {
         
         ConnectorDecoratorDepends connectorDecorator = new ConnectorDecoratorDepends();
         ConnectorDirect connectorDirect = new ConnectorDirect((DiagramPane)this.classDiagramComponent, (ConnectorDecorator)connectorDecorator, new FigureAnchor((Figure)psiClassComponent), new FigureAnchor((Figure)refClassComponent));
         connectorDirect.setActionContributor((ActionContributor)new ConnectorActionContributor(this.classDiagramComponent, (Connector)connectorDirect));
         
         connectorDecorator.setAntialiased(ds.isUseAntialiasedConnectors());
         connectorDecorator.setFillColor(ds.getDiagramBackgroundColor());
         
         connectorDirect.setVisible(this.classDiagramComponent.getComponentSettings().isPaintDepends());
         
         this.classDiagramComponent.getConnectorManager().addConnector((Connector)connectorDirect);
       } 
     } 
     return true;
   }
 
 
   
   public void postExecution() {
     this.classDiagramComponent.getConnectorManager().validateConnectors();
     this.classDiagramComponent.layoutContainer();
     this.classDiagramComponent.repaint();
   }
 }


