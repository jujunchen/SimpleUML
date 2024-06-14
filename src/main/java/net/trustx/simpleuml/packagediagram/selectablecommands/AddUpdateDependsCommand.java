 package net.trustx.simpleuml.packagediagram.selectablecommands;

 import com.intellij.psi.JavaPsiFacade;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiDirectory;
 import com.intellij.psi.PsiElementVisitor;
 import com.intellij.psi.PsiJavaFile;
 import com.intellij.psi.PsiPackage;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.Map;

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
 import net.trustx.simpleuml.packagediagram.components.ConnectorActionContributor;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponent;
 import net.trustx.simpleuml.packagediagram.components.PsiPackageComponent;
 import net.trustx.simpleuml.packagediagram.configuration.PackageDiagramSettings;
 import net.trustx.simpleuml.util.FindReferenceVisitor;





 public class AddUpdateDependsCommand
   implements SelectableCommand
 {
   private PackageDiagramComponent packageDiagramComponent;

   public AddUpdateDependsCommand(PackageDiagramComponent packageDiagramComponent) {
     this.packageDiagramComponent = packageDiagramComponent;
   }




   public void preExecution() {}



   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof PsiPackageComponent) {

       PsiPackageComponent psiPackageComponent = (PsiPackageComponent)selectable;
       return executeCommand(psiPackageComponent);
     }
     return true;
   }



   public boolean executeCommand(PsiPackageComponent psiPackageComponent) {
     this.packageDiagramComponent.getConnectorManager().removeConnectors((FigureComponent)psiPackageComponent, 2, 16);

     Map<String, PsiPackage> psiPackagesToAdd = new HashMap<>();

     PsiDirectory[] psiDirectories = psiPackageComponent.getPsiPackage().getDirectories();

     for (int i = 0; i < psiDirectories.length; i++) {

       PsiDirectory psiDirectory = psiDirectories[i];

       FindReferenceVisitor frv = new FindReferenceVisitor();
       psiDirectory.accept((PsiElementVisitor)frv);



       HashSet hashSet = frv.getList();

       for (Iterator<PsiClass> iterator1 = hashSet.iterator(); iterator1.hasNext(); ) {

         PsiClass depClass = iterator1.next();
         PsiJavaFile psiJavaFile = (PsiJavaFile)depClass.getContainingFile();
         String packageName = psiJavaFile.getPackageName();
         PsiPackage depPackage = JavaPsiFacade.getInstance(this.packageDiagramComponent.getProject()).findPackage(packageName);
         if (depPackage != null && !psiPackagesToAdd.containsKey(depPackage.getQualifiedName()))
         {

           psiPackagesToAdd.put(depPackage.getQualifiedName(), depPackage);
         }
       }
     }


     PackageDiagramSettings ds = this.packageDiagramComponent.getDiagramSettings();
     for (Iterator<PsiPackage> iterator = psiPackagesToAdd.values().iterator(); iterator.hasNext(); ) {

       PsiPackage psiPackage = iterator.next();

       PsiPackageComponent refPackageComponent = this.packageDiagramComponent.getPsiPackageComponent(psiPackage.getQualifiedName());
       if (refPackageComponent != null && !refPackageComponent.getPackageName().equals(psiPackageComponent.getPackageName())) {

         ConnectorDecoratorDepends connectorDecorator = new ConnectorDecoratorDepends();
         ConnectorDirect connectorDirect = new ConnectorDirect((DiagramPane)this.packageDiagramComponent, (ConnectorDecorator)connectorDecorator, new FigureAnchor((Figure)psiPackageComponent), new FigureAnchor((Figure)refPackageComponent));

         connectorDirect.setActionContributor((ActionContributor)new ConnectorActionContributor(this.packageDiagramComponent, (Connector)connectorDirect));


         connectorDecorator.setAntialiased(ds.isUseAntialiasedConnectors());
         connectorDecorator.setFillColor(ds.getDiagramBackgroundColor());

         connectorDirect.setVisible(true);

         this.packageDiagramComponent.getConnectorManager().addConnector((Connector)connectorDirect);
       }
     }



     return true;
   }



   public void postExecution() {
     this.packageDiagramComponent.getConnectorManager().validateConnectors();
     this.packageDiagramComponent.layoutContainer();
     this.packageDiagramComponent.repaint();
   }
 }


