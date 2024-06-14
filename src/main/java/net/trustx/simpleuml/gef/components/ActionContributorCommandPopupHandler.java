 package net.trustx.simpleuml.gef.components;

 import com.intellij.openapi.actionSystem.ActionGroup;
 import com.intellij.openapi.actionSystem.ActionManager;
 import com.intellij.openapi.actionSystem.ActionPopupMenu;
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DefaultActionGroup;
 import java.awt.Point;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.LinkedHashMap;
 import java.util.TreeMap;
 import javax.swing.JPopupMenu;
 import javax.swing.SwingUtilities;
 import net.trustx.simpleuml.components.ActionContributor;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.gef.connector.Connector;



 public class ActionContributorCommandPopupHandler
   implements MouseListener
 {
   public static final String LOCATION_PROPERTY_NAME = "ActionContributorCommandPopupHandler.Location";
   private DiagramPane diagramPane;
   private Point clickLocation;

   public ActionContributorCommandPopupHandler(DiagramPane diagramPane) {
     diagramPane.addMouseListener(this);
     this.diagramPane = diagramPane;
   }



   public ActionContributorCommandPopupHandler(DiagramPane diagramPane, FigureComponent figureComponent) {
     figureComponent.addMouseListener(this);
     this.diagramPane = diagramPane;
   }



   public void mouseClicked(MouseEvent e) {
     showPopupMenu(e);
   }



   public void mousePressed(MouseEvent e) {
     showPopupMenu(e);
   }



   public void mouseReleased(MouseEvent e) {
     showPopupMenu(e);
   }




   public void mouseEntered(MouseEvent e) {}




   public void mouseExited(MouseEvent e) {}



   private void showPopupMenu(MouseEvent e) {
     if (e.isPopupTrigger()) {

       this.clickLocation = e.getPoint();

       ActionContributor[] contributors = collectContributors(e);
       Collection commands = getCommandsFromContributors(contributors);
       DefaultActionGroup actionGroup = createActionGroupForCommands(commands);
       ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu("Popup", (ActionGroup)actionGroup);

       JPopupMenu popupMenu = actionPopupMenu.getComponent();
       popupMenu.show(e.getComponent(), e.getX(), e.getY());
     }
   }



   private DefaultActionGroup createActionGroupForCommands(Collection commands) {
     DefaultActionGroup actionGroup = new DefaultActionGroup();

     TreeMap<Object, Object> actionGroupMap = new TreeMap<Object, Object>();
     actionGroupMap.put("", actionGroup);

     for (Iterator<ActionCommand> iterator = commands.iterator(); iterator.hasNext(); ) {

       ActionCommand command = iterator.next();

       String id = "";
       String parentId = "";
       for (int i = 0; i < (command.getGroupNames()).length; i++) {

         id = id + command.getGroupNames()[i];

         if (!actionGroupMap.containsKey(id)) {

           DefaultActionGroup defaultActionGroup = (DefaultActionGroup)actionGroupMap.get(parentId);
           DefaultActionGroup newDefaultActionGroup = new DefaultActionGroup(command.getGroupNames()[i], true);
           defaultActionGroup.add((AnAction)newDefaultActionGroup);
           actionGroupMap.put(id, newDefaultActionGroup);
         }

         parentId = parentId + command.getGroupNames()[i];
       }

       if (!actionGroupMap.containsKey(id + command.getAction().getTemplatePresentation().getText())) {

         DefaultActionGroup defaultActionGroup = (DefaultActionGroup)actionGroupMap.get(parentId);
         defaultActionGroup.add(command.getAction());
         actionGroupMap.put(id + command.getAction().getTemplatePresentation().getText(), command.getAction());
       }
     }


     if (actionGroup.getChildrenCount() == 1)
     {
       return (DefaultActionGroup)actionGroup.getChildren(null)[0];
     }

     return actionGroup;
   }



   private Collection getCommandsFromContributors(ActionContributor[] contributors) {
     LinkedHashMap<Object, Object> commands = new LinkedHashMap<Object, Object>();
     for (int i = 0; i < contributors.length; i++) {

       ActionContributor contributor = contributors[i];
       ActionContributorCommandInfo[] actionContributorCommandInfos = contributor.getActionContributorCommandInfos();
       for (int j = 0; j < actionContributorCommandInfos.length; j++) {

         ActionContributorCommandInfo command = actionContributorCommandInfos[j];
         if ("IDEAActionGroup".equals(command.getType())) {

           ActionGroup ideaActionGroup = (ActionGroup)ActionManager.getInstance().getAction(command.getActionGroupID());
           ActionCommand actionCommand = new ActionCommand(command, contributors, (AnAction)ideaActionGroup);
           if (!commands.containsKey(ideaActionGroup.getTemplatePresentation().getText()))
           {
             commands.put(ideaActionGroup.getTemplatePresentation().getText(), actionCommand);
           }
         }
         else if ("SimpleUMLCommand".equals(command.getType())) {

           ActionCommand action = new ActionCommand(command, contributors, null);
           if (!commands.containsKey(action.getTemplatePresentation().getText()))
           {
             commands.put(action.getTemplatePresentation().getText(), action);
           }
         }
         else {

           throw new RuntimeException("type unknown");
         }
       }
     }
     return commands.values();
   }



   private ActionContributor[] collectContributors(MouseEvent e) {
     HashSet<Connector> actionContributors = new HashSet();

     Point connectorPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this.diagramPane);
     Connector connector = this.diagramPane.getConnectorManager().getConnectorForLocation(connectorPoint.x, connectorPoint.y);
     if (connector != null)
     {
       actionContributors.add(connector);
     }


     if (this.diagramPane.getSelectionManager().size() > 0) {

       this.diagramPane.getSelectionManager().executeCommandOnSelection(new CollectActionContributorsCommand(actionContributors, e.getComponent()));
     }
     else if (e.getComponent() instanceof ActionContributor) {

       actionContributors.add(e.getComponent());
     }

     return (ActionContributor[])actionContributors.toArray((Object[])new ActionContributor[actionContributors.size()]);
   }


   private class ActionCommand
     extends AnAction
   {
     private final ActionContributorCommandInfo command;

     private final ActionContributor[] contributors;

     private AnAction action;

     public ActionCommand(ActionContributorCommandInfo command, ActionContributor[] contributors, AnAction action) {
       super(command.getActionName());
       this.command = command;
       this.contributors = contributors;
       this.action = action;
     }



     public void actionPerformed(AnActionEvent event) {
       HashMap<Object, Object> commandProperties = new HashMap<Object, Object>();
       commandProperties.put("ActionContributorCommandPopupHandler.Location", ActionContributorCommandPopupHandler.this.clickLocation);
       commandProperties.put("ActionContributorCommand.Event", event);

       for (int k = 0; k < this.contributors.length; k++) {

         ActionContributor contributor = this.contributors[k];
         ActionContributorCommand actionContributorCommand = contributor.getActionContributorCommand(this.command);
         if (actionContributorCommand != null)
         {
           actionContributorCommand.executeCommand(commandProperties, (k == 0), (k == this.contributors.length - 1));
         }
       }
     }





     public String[] getGroupNames() {
       return this.command.getGroupNames();
     }



     public AnAction getAction() {
       if (this.action != null)
       {
         return this.action;
       }


       return this;
     }
   }
 }


