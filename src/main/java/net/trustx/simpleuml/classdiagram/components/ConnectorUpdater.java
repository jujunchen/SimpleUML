 package net.trustx.simpleuml.classdiagram.components;

 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.Set;
 import net.trustx.simpleuml.gef.Figure;
 import net.trustx.simpleuml.gef.anchor.FigureAnchor;
 import net.trustx.simpleuml.gef.components.DiagramPaneListener;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.connector.Connector;
 import net.trustx.simpleuml.gef.connector.ConnectorDecorator;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorExtends;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorImplements;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorInner;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorSettings;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorUses;
 import net.trustx.simpleuml.gef.connector.ConnectorDirect;



 public class ConnectorUpdater
   implements DiagramPaneListener
 {
   private ClassDiagramComponent classDiagramComponent;
   private Set connectorsToRecycle;

   public ConnectorUpdater(ClassDiagramComponent classDiagramComponent) {
     this.classDiagramComponent = classDiagramComponent;
     this.connectorsToRecycle = new HashSet();
   }



   public void figureAdded(FigureComponent figureComponent) {
     if (figureComponent instanceof PsiClassComponent) {

       PsiClassComponent psiClassComponent = (PsiClassComponent)figureComponent;
       updateConnectors(psiClassComponent);
     }
   }




   public void figureRemoved(FigureComponent figureComponent) {}




   public void connectorAdded(Connector connector) {}




   public void connectorRemoved(Connector connector) {}



   public void updateAllConnectors() {
     PsiClassComponent[] psiClassComponents = this.classDiagramComponent.getPsiClassComponents();
     for (int i = 0; i < psiClassComponents.length; i++) {

       PsiClassComponent psiClassComponent = psiClassComponents[i];
       updateConnectors(psiClassComponent);
     }
   }



   public void updateConnectors(PsiClassComponent psiClassComponent) {
     Iterator extendsIter = psiClassComponent.getExtendsClassnameIterator();
     while (extendsIter.hasNext()) {

       Object extendsClassname = extendsIter.next();

       Figure extendedComponent = (Figure)this.classDiagramComponent.getPsiClassComponentMap().get(extendsClassname);
       if (extendedComponent != null) {
         Connector connector = null;
         ConnectorDecoratorExtends connectorDecorator = new ConnectorDecoratorExtends();
         connectorDecorator.setAntialiased(this.classDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
         connectorDecorator.setFillColor(this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
         ConnectorDirect connectorDirect = new ConnectorDirect(this.classDiagramComponent, (ConnectorDecorator)connectorDecorator, new FigureAnchor((Figure)psiClassComponent), new FigureAnchor(extendedComponent));
         connectorDirect.setActionContributor(new ConnectorActionContributor(this.classDiagramComponent, (Connector)connectorDirect));

         if (this.connectorsToRecycle.contains(connectorDirect)) {

           connector = getConnector((Connector)connectorDirect);
           connector.getStartFigureAnchor().setFigure((Figure)psiClassComponent);
           connector.getEndFigureAnchor().setFigure(extendedComponent);
         }
         this.classDiagramComponent.getConnectorManager().addConnector(connector);
         connector.setVisible(this.classDiagramComponent.getComponentSettings().isPaintExtends());
       }
     }


     Iterator implementsIter = psiClassComponent.getImplementsClassnameIterator();
     while (implementsIter.hasNext()) {

       Object implementsClassname = implementsIter.next();

       Figure implementedComponent = (Figure)this.classDiagramComponent.getPsiClassComponentMap().get(implementsClassname);
       if (implementedComponent != null) {
         Connector connector = null;
         ConnectorDecoratorImplements connectorDecorator = new ConnectorDecoratorImplements();
         connectorDecorator.setAntialiased(this.classDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
         connectorDecorator.setFillColor(this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
         ConnectorDirect connectorDirect = new ConnectorDirect(this.classDiagramComponent, (ConnectorDecorator)connectorDecorator, new FigureAnchor((Figure)psiClassComponent), new FigureAnchor(implementedComponent));
         connectorDirect.setActionContributor(new ConnectorActionContributor(this.classDiagramComponent, (Connector)connectorDirect));

         if (this.connectorsToRecycle.contains(connectorDirect)) {

           connector = getConnector((Connector)connectorDirect);
           connector.getStartFigureAnchor().setFigure((Figure)psiClassComponent);
           connector.getEndFigureAnchor().setFigure(implementedComponent);
         }
         this.classDiagramComponent.getConnectorManager().addConnector(connector);
         connector.setVisible(this.classDiagramComponent.getComponentSettings().isPaintImplements());
       }
     }


     Iterator<PsiFieldComponent> fieldsIter = psiClassComponent.getFieldsList().iterator();
     while (fieldsIter.hasNext()) {

       PsiFieldComponent psiFieldComponent = fieldsIter.next();

       Figure fieldComponent = (Figure)this.classDiagramComponent.getPsiClassComponentMap().get(psiFieldComponent.getClassName());
       if (fieldComponent != null) {
         Connector connector = null;

         ConnectorDecoratorSettings decoratorSettings = new ConnectorDecoratorSettings(this.classDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors(), this.classDiagramComponent.getDiagramSettings().getDiagramFont(), this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());


         ConnectorDecoratorUses connectorDecorator = new ConnectorDecoratorUses(decoratorSettings);
         connectorDecorator.setDescription(psiFieldComponent.getAccessLabelText() + " " + psiFieldComponent.getFieldNameLabelText());

         connectorDecorator.setAntialiased(this.classDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
         connectorDecorator.setFillColor(this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
         ConnectorDirect connectorDirect = new ConnectorDirect(this.classDiagramComponent, (ConnectorDecorator)connectorDecorator, new FigureAnchor((Figure)psiClassComponent), new FigureAnchor(fieldComponent));
         connectorDirect.setActionContributor(new ConnectorActionContributor(this.classDiagramComponent, (Connector)connectorDirect));
         if (this.connectorsToRecycle.contains(connectorDirect)) {

           connector = getConnector((Connector)connectorDirect);
           connector.getStartFigureAnchor().setFigure((Figure)psiClassComponent);
           connector.getEndFigureAnchor().setFigure(fieldComponent);
         }
         this.classDiagramComponent.getConnectorManager().addConnector(connector);
         connector.setVisible(this.classDiagramComponent.getComponentSettings().isPaintUses());
       }
     }


     String[] innerClassNames = psiClassComponent.getQualifiedInnerClassNames();
     for (int i = 0; i < innerClassNames.length; i++) {

       String innerClassName = innerClassNames[i];
       Figure innerClassFigure = (Figure)this.classDiagramComponent.getPsiClassComponentMap().get(innerClassName);
       if (innerClassFigure != null) {
         Connector connector = null;
         ConnectorDecoratorInner connectorDecorator = new ConnectorDecoratorInner();
         connectorDecorator.setAntialiased(this.classDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
         connectorDecorator.setFillColor(this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
         ConnectorDirect connectorDirect = new ConnectorDirect(this.classDiagramComponent, (ConnectorDecorator)connectorDecorator, new FigureAnchor(innerClassFigure), new FigureAnchor((Figure)psiClassComponent));
         connectorDirect.setActionContributor(new ConnectorActionContributor(this.classDiagramComponent, (Connector)connectorDirect));
         if (this.connectorsToRecycle.contains(connectorDirect)) {

           connector = getConnector((Connector)connectorDirect);
           connector.getStartFigureAnchor().setFigure(innerClassFigure);
           connector.getEndFigureAnchor().setFigure((Figure)psiClassComponent);
         }
         this.classDiagramComponent.getConnectorManager().addConnector(connector);
         connector.setVisible(this.classDiagramComponent.getComponentSettings().isPaintInner());
       }
     }


     Iterator<PsiClassComponent> classesOnDiagramIter = this.classDiagramComponent.getPsiClassComponentMap().values().iterator();

     while (classesOnDiagramIter.hasNext()) {

       PsiClassComponent classOnDiagram = classesOnDiagramIter.next();

       if (classOnDiagram.containsExtendsClassname(psiClassComponent.getQualifiedClassName())) {
         Connector connector = null;
         ConnectorDecoratorExtends connectorDecorator = new ConnectorDecoratorExtends();
         connectorDecorator.setAntialiased(this.classDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
         connectorDecorator.setFillColor(this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
         ConnectorDirect connectorDirect = new ConnectorDirect(this.classDiagramComponent, (ConnectorDecorator)connectorDecorator, new FigureAnchor((Figure)classOnDiagram), new FigureAnchor((Figure)psiClassComponent));
         connectorDirect.setActionContributor(new ConnectorActionContributor(this.classDiagramComponent, (Connector)connectorDirect));
         if (this.connectorsToRecycle.contains(connectorDirect)) {

           connector = getConnector((Connector)connectorDirect);
           connector.getStartFigureAnchor().setFigure((Figure)classOnDiagram);
           connector.getEndFigureAnchor().setFigure((Figure)psiClassComponent);
         }
         this.classDiagramComponent.getConnectorManager().addConnector(connector);
         connector.setVisible(this.classDiagramComponent.getComponentSettings().isPaintExtends());

         if (this.classDiagramComponent.getDiagramSettings().getExtendsBehaviour() == 4 || this.classDiagramComponent.getDiagramSettings().getImplementsBehaviour() == 4)
         {
           classOnDiagram.rebuildComponent(false);
         }
       }

       if (classOnDiagram.containsImplementsClassname(psiClassComponent.getQualifiedClassName())) {
         Connector connector = null;
         ConnectorDecoratorImplements connectorDecorator = new ConnectorDecoratorImplements();
         connectorDecorator.setAntialiased(this.classDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
         connectorDecorator.setFillColor(this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
         ConnectorDirect connectorDirect = new ConnectorDirect(this.classDiagramComponent, (ConnectorDecorator)connectorDecorator, new FigureAnchor((Figure)classOnDiagram), new FigureAnchor((Figure)psiClassComponent));
         connectorDirect.setActionContributor(new ConnectorActionContributor(this.classDiagramComponent, (Connector)connectorDirect));
         if (this.connectorsToRecycle.contains(connectorDirect)) {

           connector = getConnector((Connector)connectorDirect);
           connector.getStartFigureAnchor().setFigure((Figure)classOnDiagram);
           connector.getEndFigureAnchor().setFigure((Figure)psiClassComponent);
         }
         this.classDiagramComponent.getConnectorManager().addConnector(connector);
         connector.setVisible(this.classDiagramComponent.getComponentSettings().isPaintImplements());

         if (this.classDiagramComponent.getDiagramSettings().getExtendsBehaviour() == 4 || this.classDiagramComponent.getDiagramSettings().getImplementsBehaviour() == 4)
         {
           classOnDiagram.rebuildComponent(false);
         }
       }

       Iterator<PsiFieldComponent> psiFieldComps = classOnDiagram.getFieldsList().iterator();
       while (psiFieldComps.hasNext()) {

         PsiFieldComponent fieldComponent = psiFieldComps.next();



         if (fieldComponent.getClassName() != null && fieldComponent.getClassName().equals(psiClassComponent.getQualifiedClassName())) {
           Connector connector = null;

           ConnectorDecoratorSettings decoratorSettings = new ConnectorDecoratorSettings(this.classDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors(), this.classDiagramComponent.getDiagramSettings().getDiagramFont(), this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());


           ConnectorDecoratorUses connectorDecorator = new ConnectorDecoratorUses(decoratorSettings);
           connectorDecorator.setDescription(fieldComponent.getAccessLabelText() + " " + fieldComponent.getFieldNameLabelText());
           connectorDecorator.setAntialiased(this.classDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
           connectorDecorator.setFillColor(this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
           ConnectorDirect connectorDirect = new ConnectorDirect(this.classDiagramComponent, (ConnectorDecorator)connectorDecorator, new FigureAnchor((Figure)classOnDiagram), new FigureAnchor((Figure)psiClassComponent));
           connectorDirect.setActionContributor(new ConnectorActionContributor(this.classDiagramComponent, (Connector)connectorDirect));

           if (this.connectorsToRecycle.contains(connectorDirect)) {

             connector = getConnector((Connector)connectorDirect);
             connector.getStartFigureAnchor().setFigure((Figure)classOnDiagram);
             connector.getEndFigureAnchor().setFigure((Figure)psiClassComponent);
           }
           this.classDiagramComponent.getConnectorManager().addConnector(connector);
           connector.setVisible(this.classDiagramComponent.getComponentSettings().isPaintUses());
         }
       }

       if (classOnDiagram.containsInnerClassname(psiClassComponent.getQualifiedClassName())) {
         Connector connector = null;
         ConnectorDecoratorInner connectorDecorator = new ConnectorDecoratorInner();
         connectorDecorator.setAntialiased(this.classDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
         connectorDecorator.setFillColor(this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
         ConnectorDirect connectorDirect = new ConnectorDirect(this.classDiagramComponent, (ConnectorDecorator)connectorDecorator, new FigureAnchor((Figure)psiClassComponent), new FigureAnchor((Figure)classOnDiagram));
         connectorDirect.setActionContributor(new ConnectorActionContributor(this.classDiagramComponent, (Connector)connectorDirect));

         if (this.connectorsToRecycle.contains(connectorDirect)) {

           connector = getConnector((Connector)connectorDirect);
           connector.getStartFigureAnchor().setFigure((Figure)psiClassComponent);
           connector.getEndFigureAnchor().setFigure((Figure)classOnDiagram);
         }
         this.classDiagramComponent.getConnectorManager().addConnector(connector);
         connector.setVisible(this.classDiagramComponent.getComponentSettings().isPaintInner());
       }
     }


     this.connectorsToRecycle.clear();
   }



   private Connector getConnector(Connector cd) {
     for (Iterator<Connector> iterator = this.connectorsToRecycle.iterator(); iterator.hasNext(); ) {

       Connector connector = iterator.next();
       if (connector.equals(cd))
       {
         return connector;
       }
     }
     return null;
   }
 }


