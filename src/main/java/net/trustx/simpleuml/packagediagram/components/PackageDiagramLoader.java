 package net.trustx.simpleuml.packagediagram.components;
 
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
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.components.DiagramLoader;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentDragHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentResizeHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentSelectionHandler;
 import net.trustx.simpleuml.gef.components.HTMLFigureComponent;
 import net.trustx.simpleuml.gef.components.StickyFigureComponent;
 import net.trustx.simpleuml.packagediagram.configuration.PackageDiagramSettings;
 import net.trustx.simpleuml.packagediagram.util.PackageDiagramHelper;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import org.jdom.*;


 public class PackageDiagramLoader
   implements DiagramLoader
 {
   private static PackageDiagramLoader instance = new PackageDiagramLoader();
 
 
   
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
     
     PackageDiagramComponent packageDiagramComponent = new PackageDiagramComponent((PackageDiagramSettings)umlToolWindowPlugin.getConfigurationFactory().getDiagramSettings("Packagediagram"), new PackageDiagramComponentSettings(), project);
     PackageDiagramComponentPanel packageDiagramComponentPanel = new PackageDiagramComponentPanel(packageDiagramComponent, folderURL, name);
     boolean success = umlToolWindowPlugin.addDiagramComponent(packageDiagramComponentPanel);
     
     if (!success) {
       
       Messages.showMessageDialog("There already exists a diagram called \"" + name + "\"", "Error", Messages.getErrorIcon());
       return null;
     } 
     
     ArrayList helperList = getPackageDiagramHelperList(packageDiagramComponentPanel, document, exceptionList);
     
     packageDiagramComponent.setVisible(false);
     
     addPackagesToDiagram(project, "Loading Packagediagram...", helperList, packageDiagramComponent, exceptionList);
     addStickyComponentsToDiagram(packageDiagramComponent, document);
     addTextComponentsToDiagram(packageDiagramComponent, document);
 
     
     return packageDiagramComponentPanel;
   }
 
 
 
 
   
   private void addTextComponentsToDiagram(PackageDiagramComponent packageDiagramComponent, Document document) {
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
         
         HTMLFigureComponent htmlFigureComponent = new HTMLFigureComponent(packageDiagramComponent);
         
         htmlFigureComponent.getTitleLabel().setFont(packageDiagramComponent.getDiagramSettings().getDiagramTitleFont());
         htmlFigureComponent.getTextArea().setFont(packageDiagramComponent.getDiagramSettings().getDiagramFont());
         htmlFigureComponent.getTitleTextField().setFont(packageDiagramComponent.getDiagramSettings().getDiagramTitleFont());
         htmlFigureComponent.setType(type);
         
         FigureComponentDragHandler dragHandler = new FigureComponentDragHandler(packageDiagramComponent, packageDiagramComponent.getPackageDiagramComponentPanel().getScrollPane());
         dragHandler.install((FigureComponent)htmlFigureComponent);
         
         FigureComponentResizeHandler resizeHandler = new FigureComponentResizeHandler(packageDiagramComponent);
         resizeHandler.install((FigureComponent)htmlFigureComponent);
         
         FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(packageDiagramComponent);
         selectionHandler.install((FigureComponent)htmlFigureComponent);
         
         packageDiagramComponent.addFigureComponent((FigureComponent)htmlFigureComponent, true);
         
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
         
         htmlFigureComponent.setSelectionManager(packageDiagramComponent.getSelectionManager());
         htmlFigureComponent.setSelected(false);
         
         htmlFigureComponent.setSelectionManager(packageDiagramComponent.getSelectionManager());
         htmlFigureComponent.setSelected(false);
       }
     
     } catch (Exception e) {
       
       Messages.showMessageDialog(e.getMessage(), "Error", Messages.getErrorIcon());
       e.printStackTrace();
     } 
   }
 
 
 
 
   
   private void addStickyComponentsToDiagram(PackageDiagramComponent packageDiagramComponent, Document document) {
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
         
         StickyFigureComponent stickyComponent = new StickyFigureComponent(packageDiagramComponent);
         
         FigureComponentDragHandler dragHandler = new FigureComponentDragHandler(packageDiagramComponent, packageDiagramComponent.getPackageDiagramComponentPanel().getScrollPane());
         dragHandler.install((FigureComponent)stickyComponent);
         
         FigureComponentResizeHandler resizeHandler = new FigureComponentResizeHandler(packageDiagramComponent);
         resizeHandler.install((FigureComponent)stickyComponent);
         
         FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(packageDiagramComponent);
         selectionHandler.install((FigureComponent)stickyComponent);
         
         packageDiagramComponent.addFigureComponent((FigureComponent)stickyComponent, true);
         
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
         
         stickyComponent.setSelectionManager(packageDiagramComponent.getSelectionManager());
         stickyComponent.setSelected(false);
       }
     
     }
     catch (Exception e) {
       
       Messages.showMessageDialog(e.getMessage(), "Error", Messages.getErrorIcon());
       e.printStackTrace();
     } 
   }
 
 
 
 
 
 
   
   private void addPackagesToDiagram(Project project, String progressWindowTitle, final ArrayList helperList, final PackageDiagramComponent packageDiagramComponent, final ArrayList exceptionList) {
     ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable()
         {
           public void run()
           {
             ProgressIndicator progressWindow = ProgressManager.getInstance().getProgressIndicator();
             for (int i = 0; i < helperList.size(); i++) {
               
               final PackageDiagramHelper helper = (PackageDiagramHelper) helperList.get(i);
               progressWindow.setText("Loading " + helper.getPackageName());
               progressWindow.setFraction(i / helperList.size());
 
               
               try {
                 final int itemp = i;
                 EventQueue.invokeAndWait(new Runnable()
                     {
                       
                       public void run()
                       {
                         try {
                           if (!packageDiagramComponent.containsHighlightPackage(helper.getPackageName()))
                           {
                             PsiPackageComponent psiPackageComponent = new PsiPackageComponent(packageDiagramComponent, helper.getPackageName(), helper.isShowClasses());
                             
                             FigureComponentDragHandler dragHandler = new FigureComponentDragHandler(packageDiagramComponent, packageDiagramComponent.getPackageDiagramComponentPanel().getScrollPane());
                             dragHandler.install(psiPackageComponent);
                             
                             FigureComponentResizeHandler resizeHandler = new FigureComponentResizeHandler(packageDiagramComponent);
                             resizeHandler.install(psiPackageComponent);
 
                             
                             psiPackageComponent.setPinned(helper.isPinned());
                             psiPackageComponent.setColor(helper.getColor());
                             
                             packageDiagramComponent.addFigureComponent(psiPackageComponent, (itemp + 1 == helperList.size()));
                             
                             psiPackageComponent.setPosX(helper.getPosX());
                             psiPackageComponent.setPosY(helper.getPosY());
                             
                             psiPackageComponent.setPreferredSize(new Dimension(helper.getWidth(), helper.getHeight()));
                           }
                         
                         } catch (Throwable throwable) {
                           
                           exceptionList.add(throwable.getMessage());
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
                     packageDiagramComponent.getConnectorManager().validateConnectors();
                     packageDiagramComponent.setVisible(true);
                   }
                 });
             
             progressWindow.stop();
             
             if (exceptionList.size() > 0) {
               
               StringBuffer sb = new StringBuffer();
               for (Iterator<String> iterator = exceptionList.iterator(); iterator.hasNext(); ) {
                 
                 String s = iterator.next();
                 sb.append(s);
                 sb.append('\n');
               } 
               Messages.showMessageDialog(sb.toString(), "Error", Messages.getErrorIcon());
             } 
           }
         }, progressWindowTitle, false, project);
   }
 
 
   
   private ArrayList getPackageDiagramHelperList(PackageDiagramComponentPanel packageDiagramComponentPanel, Document document, ArrayList<String> exceptionList) {
     ArrayList<PackageDiagramHelper> helperList = new ArrayList();
     
     try {
       Element e = document.getRootElement();
       
       fillSettings(e, packageDiagramComponentPanel.getPackageDiagramComponent());
 
       
       Element classGroup = e.getChild("packages");
       
       List classesList = classGroup.getChildren("package");
       for (Iterator<Element> iterator = classesList.iterator(); iterator.hasNext(); )
       {
         Element ce = iterator.next();
         String packagename = ce.getAttribute("name").getValue();
         int x = ce.getAttribute("x").getIntValue();
         int y = ce.getAttribute("y").getIntValue();
         int width = ce.getAttribute("width").getIntValue();
         int height = ce.getAttribute("height").getIntValue();
         
         List cl = ce.getChildren("option");
         Map<Object, Object> classSM = new HashMap<Object, Object>();
         for (Iterator<Element> iterator2 = cl.iterator(); iterator2.hasNext(); ) {
           
           Element coe = iterator2.next();
           String oName = coe.getAttribute("name").getValue();
           String oValue = coe.getAttribute("value").getValue();
           classSM.put(oName, oValue);
         } 
         
         PackageDiagramHelper cdh = new PackageDiagramHelper(packagename, x, y, width, height);
         helperList.add(cdh);
         cdh.setSettingsMap(classSM);
       }
     
     }
     catch (Throwable throwable) {
       
       exceptionList.add("File has bad format");
     } 
     
     return helperList;
   }
 
 
   
   private void fillSettings(Element root, PackageDiagramComponent packageDiagramComponent) {
     Element settings = root.getChild("settings");
     Map<Object, Object> settingsMap = new HashMap<Object, Object>();
     List<Element> entries = settings.getChildren("option");
     for (int i = 0; i < entries.size(); i++) {
       
       Element entry = entries.get(i);
       String name = entry.getAttribute("name").getValue();
       String value = entry.getAttribute("value").getValue();
       settingsMap.put(name, value);
     } 
     packageDiagramComponent.getComponentSettings().setSettingsMap(settingsMap);
   }
 }


