 package net.trustx.simpleuml.classdiagram.components;

 import java.io.UnsupportedEncodingException;
 import java.net.URLEncoder;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Set;
 import net.trustx.simpleuml.classdiagram.util.ClassDiagramHelper;
 import net.trustx.simpleuml.components.DiagramSaver;
 import net.trustx.simpleuml.gef.anchor.Anchor;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.HTMLFigureComponent;
 import net.trustx.simpleuml.gef.components.StickyFigureComponent;
 import net.trustx.simpleuml.gef.connector.Connector;
 import net.trustx.simpleuml.gef.connector.ConnectorDecorator;
 import org.jdom.CDATA;
 import org.jdom.Content;
 import org.jdom.Document;
 import org.jdom.Element;



 public class ClassDiagramSaver
   implements DiagramSaver
 {
   private ClassDiagramComponentPanel classDiagramComponentPanel;

   public ClassDiagramSaver(ClassDiagramComponentPanel classDiagramComponentPanel) {
     this.classDiagramComponentPanel = classDiagramComponentPanel;
   }



   public Document saveToDocument() {
     Element root = new Element("Classdiagram");

     Element settingsElement = new Element("settings");
     root.addContent(settingsElement);



     Map map = this.classDiagramComponentPanel.getComponentSettings().getSettingsMap();
     Iterator<String> keyIterator = map.keySet().iterator();
     while (keyIterator.hasNext()) {

       String key = keyIterator.next();

       Element entryElement = new Element("option");
       entryElement = entryElement.setAttribute("name", key);
       entryElement = entryElement.setAttribute("value", map.get(key).toString());
       settingsElement.addContent(entryElement);
     }



     PsiClassComponent[] psiClassComponents = this.classDiagramComponentPanel.getClassDiagramComponent().getPsiClassComponents();

     Element classGroup = new Element("classes");
     for (int j = 0; j < psiClassComponents.length; j++) {

       PsiClassComponent psiClassComponent = psiClassComponents[j];
       ClassDiagramHelper helper = new ClassDiagramHelper(psiClassComponent);

       Element classElement = new Element("class");
       classElement.setAttribute("name", helper.getClassName());
       classElement.setAttribute("x", "" + helper.getPosX());
       classElement.setAttribute("y", "" + helper.getPosY());

       Map settingsMap = helper.getSettingsMap();
       Set keys = settingsMap.keySet();
       for (Iterator keyIter = keys.iterator(); keyIter.hasNext(); ) {

         Object key = keyIter.next();
         Element element = new Element("option");
         element.setAttribute("name", key.toString());
         element.setAttribute("value", settingsMap.get(key).toString());
         classElement.addContent(element);
       }

       classGroup.addContent(classElement);
     }
     root.addContent(classGroup);



     Element stickyGroup = new Element("stickycomponents");
     Collection figureComponents = this.classDiagramComponentPanel.getClassDiagramComponent().getFigureComponents();
     for (Iterator<FigureComponent> iterator = figureComponents.iterator(); iterator.hasNext(); ) {

       FigureComponent figureComponent = iterator.next();
       if (figureComponent instanceof StickyFigureComponent) {

         StickyFigureComponent stickyFigureComponent = (StickyFigureComponent)figureComponent;
         Element stickyElement = new Element("stickycomponent");
         stickyElement.setAttribute("key", "" + stickyFigureComponent.getKey());
         stickyElement.setAttribute("x", "" + stickyFigureComponent.getPosX());
         stickyElement.setAttribute("y", "" + stickyFigureComponent.getPosY());
         stickyElement.setAttribute("width", "" + stickyFigureComponent.getComponentWidth());
         stickyElement.setAttribute("height", "" + stickyFigureComponent.getComponentHeight());


         Element pinnedOptionElement = new Element("option");
         pinnedOptionElement.setAttribute("name", "pinned");
         pinnedOptionElement.setAttribute("value", String.valueOf(stickyFigureComponent.isPinned()));
         stickyElement.addContent(pinnedOptionElement);


         Element stickyOptionElement = new Element("option");
         stickyOptionElement.setAttribute("name", "sticky");
         stickyOptionElement.setAttribute("value", String.valueOf(stickyFigureComponent.isSticky()));
         stickyElement.addContent(stickyOptionElement);


         if (stickyFigureComponent.getColor() != null) {

           Element colorOptionElement = new Element("option");
           colorOptionElement.setAttribute("name", "color");
           colorOptionElement.setAttribute("value", String.valueOf(stickyFigureComponent.getColor().getRGB()));
           stickyElement.addContent(colorOptionElement);
         }

         stickyGroup.addContent(stickyElement);
       }
     }
     root.addContent(stickyGroup);


     Element textGroup = new Element("textcomponents");
     for (Iterator<FigureComponent> iterator1 = figureComponents.iterator(); iterator1.hasNext(); ) {

       FigureComponent figureComponent = iterator1.next();
       if (figureComponent instanceof HTMLFigureComponent) {

         HTMLFigureComponent htmlFigureComponent = (HTMLFigureComponent)figureComponent;
         Element textElement = new Element("textcomponent");
         textElement.setAttribute("key", "" + htmlFigureComponent.getKey());
         textElement.setAttribute("x", "" + htmlFigureComponent.getPosX());
         textElement.setAttribute("y", "" + htmlFigureComponent.getPosY());
         textElement.setAttribute("width", "" + htmlFigureComponent.getComponentWidth());
         textElement.setAttribute("height", "" + htmlFigureComponent.getComponentHeight());
         textElement.setAttribute("type", "" + htmlFigureComponent.getType());


         Element pinnedOptionElement = new Element("option");
         pinnedOptionElement.setAttribute("name", "pinned");
         pinnedOptionElement.setAttribute("value", String.valueOf(htmlFigureComponent.isPinned()));
         textElement.addContent(pinnedOptionElement);


         if (htmlFigureComponent.getColor() != null) {

           Element colorOptionElement = new Element("option");
           colorOptionElement.setAttribute("name", "color");
           colorOptionElement.setAttribute("value", String.valueOf(htmlFigureComponent.getColor().getRGB()));
           textElement.addContent(colorOptionElement);
         }


         try {
           Element titleElement = new Element("title");
           titleElement.addContent((Content)new CDATA(URLEncoder.encode(htmlFigureComponent.getTitleText(), "UTF-8")));
           textElement.addContent(titleElement);

           Element contentElement = new Element("content");
           contentElement.addContent((Content)new CDATA(URLEncoder.encode(htmlFigureComponent.getContentText(), "UTF-8")));
           textElement.addContent(contentElement);
         }
         catch (UnsupportedEncodingException e) {

           e.printStackTrace();
         }

         textGroup.addContent(textElement);
       }
     }
     root.addContent(textGroup);











     Element connectorGroup = new Element("connectors");
     Set connectors = this.classDiagramComponentPanel.getClassDiagramComponent().getConnectorManager().getConnectors();
     for (Iterator<Connector> iterator2 = connectors.iterator(); iterator2.hasNext(); ) {

       Connector connector = iterator2.next();
       Element connectorElement = new Element("connector");
       connectorElement.setAttribute("from", connector.getStartFigureAnchor().getFigure().getKey());
       connectorElement.setAttribute("to", connector.getEndFigureAnchor().getFigure().getKey());

       ArrayList al = connector.getAnchorList();
       for (Iterator<Anchor> iteratorAnchor = al.iterator(); iteratorAnchor.hasNext(); ) {

         Anchor anchor = iteratorAnchor.next();
         Element anchorElement = new Element("anchor");
         anchorElement.setAttribute("constraint", "" + anchor.getConstraint());
         anchorElement.setAttribute("type", "" + anchor.getType());
         anchorElement.setAttribute("x", "" + (anchor.getLocation()).x);
         anchorElement.setAttribute("y", "" + (anchor.getLocation()).y);
         connectorElement.addContent(anchorElement);
       }

       ConnectorDecorator decorator = connector.getConnectorDecorator();
       Element decoratorElement = new Element("decorator");
       decoratorElement.setAttribute("type", "" + decorator.getType());
       decoratorElement.setAttribute("description", decorator.getDescription());
       connectorElement.addContent(decoratorElement);

       connectorGroup.addContent(connectorElement);
     }
     root.addContent(connectorGroup);

     return new Document(root);
   }
 }


