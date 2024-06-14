 package net.trustx.simpleuml.gef.components;

 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Rectangle;
 import java.awt.event.ActionEvent;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.awt.event.WindowAdapter;
 import java.awt.event.WindowEvent;
 import java.util.Collection;
 import java.util.Iterator;
 import javax.swing.AbstractAction;
 import javax.swing.Action;
 import javax.swing.JFrame;
 import javax.swing.JScrollPane;
 import javax.swing.KeyStroke;
 import net.trustx.simpleuml.gef.connector.ConnectorAntialiasingCommand;
 import net.trustx.simpleuml.gef.connector.ConnectorCommand;
 import net.trustx.simpleuml.gef.connector.ConnectorVisibilityCommand;



 public class Tester2
 {
   public static void main(String[] args) {
     JFrame frame = new JFrame("Tester2");

     final DiagramPane diagramPane = new DiagramPane();
     diagramPane.setAllowedContentBounds(new Rectangle(0, 0, 2147483647, 2147483647));
     diagramPane.setBackground(Color.LIGHT_GRAY.brighter());
     diagramPane.setOpaque(false);

     diagramPane.addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e)
           {
             diagramPane.getSelectionManager().clear();
             diagramPane.requestFocusInWindow();
           }
         });

     JScrollPane scrollPane = new JScrollPane(diagramPane);
     frame.getContentPane().add(scrollPane, "Center");


     DiagramPaneDragHandler diagramPaneDragHandler = new DiagramPaneDragHandler();
     diagramPaneDragHandler.install(diagramPane, scrollPane);

     new DiagramPaneMarqueeHandler(diagramPane);


     DiagramPane diagramPane2 = new DiagramPane();
     diagramPane2.setOpaque(false);


     diagramPane2.getConnectorManager().executeCommandOnConnectors((ConnectorCommand)new ConnectorVisibilityCommand(diagramPane2, true));
     diagramPane2.getConnectorManager().executeCommandOnConnectors((ConnectorCommand)new ConnectorAntialiasingCommand(diagramPane2, true));


     diagramPane2.getConnectorManager().addConnectorManager(diagramPane.getConnectorManager());
     Collection fC = diagramPane2.getFigureComponents();
     for (Iterator<FigureComponent> iterator = fC.iterator(); iterator.hasNext(); ) {

       FigureComponent figureComponent123 = iterator.next();
       System.out.println("figureComponent123 = " + figureComponent123);
     }


     diagramPane.getConnectorManager().executeCommandOnConnectors((ConnectorCommand)new ConnectorVisibilityCommand(diagramPane, true));
     diagramPane.getConnectorManager().executeCommandOnConnectors((ConnectorCommand)new ConnectorAntialiasingCommand(diagramPane, false));





     final HTMLFigureComponent textComponent3 = new HTMLFigureComponent(diagramPane);
     textComponent3.setTitleText("HTML");

     textComponent3.getTitleLabel().addMouseListener(new MouseAdapter()
         {
           public void mouseEntered(MouseEvent e)
           {
             textComponent3.getTitleLabel().setBackground(Color.RED);
           }



           public void mouseExited(MouseEvent e) {
             textComponent3.getTitleLabel().setBackground(Color.BLACK);
           }
         });

     FigureComponentDragHandler dragHandler3 = new FigureComponentDragHandler(diagramPane, scrollPane);
     dragHandler3.install(textComponent3);


     FigureComponentSelectionHandler selectionHandler3 = new FigureComponentSelectionHandler(diagramPane);
     selectionHandler3.install(textComponent3);

     diagramPane.addFigureComponent(textComponent3, true);
     textComponent3.setPreferredSize(new Dimension(50, 50));

     textComponent3.setSelectionManager(diagramPane.getSelectionManager());
     textComponent3.setSelected(false);

     new ActionContributorCommandPopupHandler(diagramPane, textComponent3);

     Action printComponentsAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           Collection components = diagramPane.getFigureComponents();
           for (Iterator<FigureComponent> iterator = components.iterator(); iterator.hasNext(); ) {

             FigureComponent component = iterator.next();
             System.out.println("component = " + component);
           }
         }
       };
     diagramPane.getInputMap().put(KeyStroke.getKeyStroke(79, 704), "printComponentsAction");
     diagramPane.getActionMap().put("printComponentsAction", printComponentsAction);


     frame.addWindowListener(new WindowAdapter()
         {
           public void windowClosing(WindowEvent e)
           {
             diagramPane.disposeUIResources();
             System.exit(0);
           }
         });

     frame.setSize(300, 300);
     frame.setVisible(true);
   }
 }


