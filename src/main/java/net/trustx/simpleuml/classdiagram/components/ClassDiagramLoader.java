 package net.trustx.simpleuml.classdiagram.components;

 import com.intellij.openapi.progress.ProgressIndicator;
 import com.intellij.openapi.progress.ProgressManager;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.EventQueue;
 import java.lang.reflect.InvocationTargetException;
 import java.net.URLDecoder;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import net.trustx.simpleuml.classdiagram.configuration.ClassDiagramSettings;
 import net.trustx.simpleuml.classdiagram.util.ClassDiagramHelper;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.components.DiagramLoader;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentDragHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentResizeHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentSelectionHandler;
 import net.trustx.simpleuml.gef.components.HTMLFigureComponent;
 import net.trustx.simpleuml.gef.components.StickyFigureComponent;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLUtils;
 import org.jdom.*;


 public class ClassDiagramLoader
   implements DiagramLoader
 {
   private static ClassDiagramLoader instance = new ClassDiagramLoader();



   public static DiagramLoader getInstance() {
     return instance;
   }



   public DiagramComponent loadFromDocument(Project project, Document document, String folderURL, String name) {
     ArrayList exceptionList = new ArrayList();

     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);

     if (umlToolWindowPlugin.isDiagramComponentOnUI(folderURL, name)) {

       Messages.showMessageDialog(project, "Diagram is already open", "Information", Messages.getInformationIcon());
       return null;
     }

     ClassDiagramComponent classDiagramComponent = new ClassDiagramComponent((ClassDiagramSettings)umlToolWindowPlugin.getConfigurationFactory().getDiagramSettings("Classdiagram"), new ClassDiagramComponentSettings(), project);
     ClassDiagramComponentPanel classDiagramComponentPanel = new ClassDiagramComponentPanel(classDiagramComponent, folderURL, name);
     boolean success = umlToolWindowPlugin.addDiagramComponent(classDiagramComponentPanel);

     if (!success) {

       Messages.showMessageDialog("There already exists a diagram called \"" + name + "\"", "Error", Messages.getErrorIcon());
       return null;
     }

     ArrayList helperList = getClassDiagramHelperList(classDiagramComponentPanel, document, exceptionList);

     classDiagramComponent.setVisible(false);

     ProgressIndicator progressWindow = ProgressManager.getInstance().getProgressIndicator();

     addClassesToDiagram(project, "Loading Classdiagram...", helperList, classDiagramComponent, exceptionList);
     addStickyComponentsToDiagram(classDiagramComponent, document);
     addTextComponentsToDiagram(classDiagramComponent, document);


     return classDiagramComponentPanel;
   }





   private void addTextComponentsToDiagram(ClassDiagramComponent classDiagramComponent, Document document) {
     try {
       Element e = document.getRootElement();

       Element stickyGroup = e.getChild("textcomponents");

       List stickyComponentsList = stickyGroup.getChildren("textcomponent");
       for (Iterator<Element> iterator = stickyComponentsList.iterator(); iterator.hasNext(); )
       {
         Element ce = iterator.next();

         int x = ce.getAttribute("x").getIntValue();
         int y = ce.getAttribute("y").getIntValue();

         int width = ce.getAttribute("width").getIntValue();
         int height = ce.getAttribute("height").getIntValue();

         Attribute typeAttribute = ce.getAttribute("type");

         String type = "plain";
         if (typeAttribute != null)
         {
           type = typeAttribute.getValue();
         }

         HTMLFigureComponent htmlFigureComponent = new HTMLFigureComponent(classDiagramComponent);

         htmlFigureComponent.getTitleLabel().setFont(classDiagramComponent.getDiagramSettings().getDiagramTitleFont());
         htmlFigureComponent.getTextArea().setFont(classDiagramComponent.getDiagramSettings().getDiagramFont());
         htmlFigureComponent.getTitleTextField().setFont(classDiagramComponent.getDiagramSettings().getDiagramTitleFont());
         htmlFigureComponent.setType(type);

         FigureComponentDragHandler dragHandler = new FigureComponentDragHandler(classDiagramComponent, classDiagramComponent.getClassDiagramComponentPanel().getScrollPane());
         dragHandler.install((FigureComponent)htmlFigureComponent);

         FigureComponentResizeHandler resizeHandler = new FigureComponentResizeHandler(classDiagramComponent);
         resizeHandler.install((FigureComponent)htmlFigureComponent);

         FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(classDiagramComponent);
         selectionHandler.install((FigureComponent)htmlFigureComponent);

         classDiagramComponent.addFigureComponent((FigureComponent)htmlFigureComponent, true);

         htmlFigureComponent.setPosX(x);
         htmlFigureComponent.setPosY(y);

         htmlFigureComponent.setPreferredSize(new Dimension(width, height));

         List cl = ce.getChildren("option");
         for (Iterator<Element> iterator2 = cl.iterator(); iterator2.hasNext(); ) {

           Element coe = iterator2.next();
           String oName = coe.getAttribute("name").getValue();
           String oValue = coe.getAttribute("value").getValue();

           if ("pinned".equals(oName)) {

             htmlFigureComponent.setPinned("true".equalsIgnoreCase(oValue)); continue;
           }
           if ("color".equals(oName))
           {
             htmlFigureComponent.setColor(new Color(Integer.parseInt(oValue)));
           }
         }

         Element titleElement = ce.getChild("title");
         List<Content> titleList = titleElement.getContent();
         if (titleList.size() > 0 && titleList.get(0) instanceof CDATA) {

           String title = URLDecoder.decode(((CDATA)titleList.get(0)).getText(), "UTF-8");
           htmlFigureComponent.setTitleText(title);
         }
         else {

           htmlFigureComponent.setTitleText("");
         }

         Element contentElement = ce.getChild("content");
         List<Content> contentList = contentElement.getContent();
         if (contentList.size() > 0 && contentList.get(0) instanceof CDATA) {

           String content = URLDecoder.decode(((CDATA)contentList.get(0)).getText(), "UTF-8");
           htmlFigureComponent.setContentText(content);
         }

         htmlFigureComponent.setSelectionManager(classDiagramComponent.getSelectionManager());
         htmlFigureComponent.setSelected(false);

         htmlFigureComponent.setSelectionManager(classDiagramComponent.getSelectionManager());
         htmlFigureComponent.setSelected(false);
       }

     } catch (Exception e) {

       Messages.showMessageDialog(e.getMessage(), "Error", Messages.getErrorIcon());
       e.printStackTrace();
     }
   }





   private void addStickyComponentsToDiagram(ClassDiagramComponent classDiagramComponent, Document document) {
     try {
       Element e = document.getRootElement();

       Element stickyGroup = e.getChild("stickycomponents");

       List stickyComponentsList = stickyGroup.getChildren("stickycomponent");
       for (Iterator<Element> iterator = stickyComponentsList.iterator(); iterator.hasNext(); )
       {
         Element ce = iterator.next();

         int x = ce.getAttribute("x").getIntValue();
         int y = ce.getAttribute("y").getIntValue();

         int width = ce.getAttribute("width").getIntValue();
         int height = ce.getAttribute("height").getIntValue();

         StickyFigureComponent stickyComponent = new StickyFigureComponent(classDiagramComponent);

         FigureComponentDragHandler dragHandler = new FigureComponentDragHandler(classDiagramComponent, classDiagramComponent.getClassDiagramComponentPanel().getScrollPane());
         dragHandler.install((FigureComponent)stickyComponent);

         FigureComponentResizeHandler resizeHandler = new FigureComponentResizeHandler(classDiagramComponent);
         resizeHandler.install((FigureComponent)stickyComponent);

         FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(classDiagramComponent);
         selectionHandler.install((FigureComponent)stickyComponent);

         classDiagramComponent.addFigureComponent((FigureComponent)stickyComponent, true);

         stickyComponent.setPosX(x);
         stickyComponent.setPosY(y);

         stickyComponent.setPreferredSize(new Dimension(width, height));

         List cl = ce.getChildren("option");
         for (Iterator<Element> iterator2 = cl.iterator(); iterator2.hasNext(); ) {

           Element coe = iterator2.next();
           String oName = coe.getAttribute("name").getValue();
           String oValue = coe.getAttribute("value").getValue();

           if ("sticky".equals(oName)) {

             stickyComponent.setSticky("true".equalsIgnoreCase(oValue)); continue;
           }
           if ("pinned".equals(oName)) {

             stickyComponent.setPinned("true".equalsIgnoreCase(oValue)); continue;
           }
           if ("color".equals(oName))
           {
             stickyComponent.setColor(new Color(Integer.parseInt(oValue)));
           }
         }

         stickyComponent.setSelectionManager(classDiagramComponent.getSelectionManager());
         stickyComponent.setSelected(false);
       }

     }
     catch (Exception e) {

       Messages.showMessageDialog(e.getMessage(), "Error", Messages.getErrorIcon());
       e.printStackTrace();
     }
   }







   private void addClassesToDiagram(Project project, String progressWindowTitle, final ArrayList helperList, final ClassDiagramComponent classDiagramComponent, final ArrayList exceptionList) {
     ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable()
         {
           public void run() {
             ProgressIndicator progressWindow = ProgressManager.getInstance().getProgressIndicator();
             for (int i = 0; i < helperList.size(); i++) {

               final ClassDiagramHelper helper = (ClassDiagramHelper) helperList.get(i);
               progressWindow.setText("Loading " + helper.getClassName());
               progressWindow.setFraction(i / helperList.size());


               try {
                 final int itemp = i;
                 EventQueue.invokeAndWait(new Runnable()
                     {

                       public void run()
                       {
                         try {
                           if (!classDiagramComponent.containsHighlightClass(helper.getClassName()))
                           {
                             PsiClassComponent psiClassComponent = new PsiClassComponent(classDiagramComponent, helper.getClassName());

                             FigureComponentDragHandler dragHandler = new FigureComponentDragHandler(classDiagramComponent, classDiagramComponent.getClassDiagramComponentPanel().getScrollPane());
                             dragHandler.install(psiClassComponent);

                             psiClassComponent.getToggleFieldButton().setSelected(helper.isFieldsExpanded());
                             psiClassComponent.getToggleConstructorButton().setSelected(helper.isConstructorsExpanded());
                             psiClassComponent.getToggleMethodButton().setSelected(helper.isMethodsExpanded());
                             psiClassComponent.setPinned(helper.isPinned());
                             psiClassComponent.setColor(helper.getColor());
                             psiClassComponent.rebuildComponent(false);

                             classDiagramComponent.addFigureComponent(psiClassComponent, (itemp + 1 == helperList.size()));

                             psiClassComponent.setPosX(helper.getPosX());
                             psiClassComponent.setPosY(helper.getPosY());
                           }

                         } catch (Throwable throwable) {

                           exceptionList.add(throwable);
                           throwable.printStackTrace();
                         }

                       }
                     });
               } catch (InterruptedException e) {

                 e.printStackTrace();
               }
               catch (InvocationTargetException e) {

                 e.printStackTrace();
               }
             }

             EventQueue.invokeLater(new Runnable()
                 {
                   public void run()
                   {
                     classDiagramComponent.getConnectorManager().validateConnectors();
                     classDiagramComponent.setVisible(true);
                   }
                 });

             progressWindow.stop();

             if (exceptionList.size() > 0) {

               final StringBuffer sb = new StringBuffer();

               for (Iterator<Throwable> iterator = exceptionList.iterator(); iterator.hasNext(); ) {

                 Throwable throwable = iterator.next();
                 sb.append(throwable.getMessage());

                 sb.append('\n');
                 throwable.printStackTrace(System.out);
               }

               EventQueue.invokeLater(new Runnable()
                   {
                     public void run()
                     {
                       Throwable[] throwables = (Throwable[])exceptionList.toArray((Object[])new Throwable[exceptionList.size()]);
                       UMLUtils.showInternalErrorMessageDialog(classDiagramComponent.getProject(), "Error", "Error while loading diagram:\n" + sb.toString(), throwables);
                     }
                   });
             }
           }
         }, progressWindowTitle, false, project);
   }





   private ArrayList getClassDiagramHelperList(ClassDiagramComponentPanel classDiagramComponentPanel, Document document, ArrayList<Throwable> exceptionList) {
     ArrayList<ClassDiagramHelper> helperList = new ArrayList();

     try {
       Element e = document.getRootElement();

       fillSettings(e, classDiagramComponentPanel.getClassDiagramComponent());


       Element classGroup = e.getChild("classes");

       List classesList = classGroup.getChildren("class");
       for (Iterator<Element> iterator = classesList.iterator(); iterator.hasNext(); )
       {
         Element ce = iterator.next();
         String classname = ce.getAttribute("name").getValue();
         int x = ce.getAttribute("x").getIntValue();
         int y = ce.getAttribute("y").getIntValue();

         List cl = ce.getChildren("option");
         Map<Object, Object> classSM = new HashMap<Object, Object>();
         for (Iterator<Element> iterator2 = cl.iterator(); iterator2.hasNext(); ) {

           Element coe = iterator2.next();
           String oName = coe.getAttribute("name").getValue();
           String oValue = coe.getAttribute("value").getValue();
           classSM.put(oName, oValue);
         }

         ClassDiagramHelper cdh = new ClassDiagramHelper(classname, x, y);
         helperList.add(cdh);
         cdh.setSettingsMap(classSM);
       }

     }
     catch (Throwable throwable) {

       exceptionList.add(throwable);
     }

     return helperList;
   }



   private void fillSettings(Element root, ClassDiagramComponent classDiagramComponent) {
     Element settings = root.getChild("settings");
     Map<Object, Object> settingsMap = new HashMap<Object, Object>();
     List<Element> entries = settings.getChildren("option");
     for (int i = 0; i < entries.size(); i++) {

       Element entry = entries.get(i);
       String name = entry.getAttribute("name").getValue();
       String value = entry.getAttribute("value").getValue();
       settingsMap.put(name, value);
     }
     classDiagramComponent.getComponentSettings().setSettingsMap(settingsMap);
   }
 }


