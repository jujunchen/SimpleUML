 package net.trustx.simpleuml.gef.components;

 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Font;
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
 import javax.swing.JButton;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JScrollPane;
 import javax.swing.JTextArea;
 import javax.swing.KeyStroke;
 import net.trustx.simpleuml.gef.anchor.FigureAnchor;
 import net.trustx.simpleuml.gef.connector.Connector;
 import net.trustx.simpleuml.gef.connector.ConnectorAntialiasingCommand;
 import net.trustx.simpleuml.gef.connector.ConnectorCommand;
 import net.trustx.simpleuml.gef.connector.ConnectorDecorator;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorContains;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorExtends;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorImplements;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorSettings;
 import net.trustx.simpleuml.gef.connector.ConnectorDirect;
 import net.trustx.simpleuml.gef.connector.ConnectorVisibilityCommand;


 public class Tester
 {
   public static void main(String[] args) {
     JFrame frame = new JFrame("Tester");


     final DiagramPane diagramPane = new DiagramPane();
     diagramPane.setAllowedContentBounds(new Rectangle(0, 0, 2147483647, 2147483647));
     diagramPane.setBackground(Color.LIGHT_GRAY.brighter());
     diagramPane.setOpaque(true);

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




     DiagramPaneMarqueeHandler marqueeHandler = new DiagramPaneMarqueeHandler(diagramPane);


     FigureComponentSelectionHandler figureSelectionHandler = new FigureComponentSelectionHandler(diagramPane);
     FigureComponentDragHandler figureComponentDragHandler = new FigureComponentDragHandler(diagramPane, scrollPane);


     FigureComponentResizeHandler resizeHandler = new FigureComponentResizeHandler(diagramPane);

     FigureComponent figureComponent = initFigure1(diagramPane);

     resizeHandler.install(figureComponent);

     figureSelectionHandler.install(figureComponent);
     figureComponentDragHandler.install(figureComponent);


     FigureComponent figureComponent2 = initFigure2(diagramPane);

     FigureComponent figureComponent3 = initFigure3(diagramPane, figureComponent2);

     FigureComponent figureComponent4 = new FigureComponent(diagramPane);
     figureComponent4.getContentPane().setLayout(new BorderLayout());
     figureComponent4.getContentPane().add(new JLabel("figure4"), "North");
     figureComponent4.setSelectionManager(diagramPane.getSelectionManager());
     DiagramPane diagramPane2 = new DiagramPane();
     diagramPane2.setOpaque(false);


     resizeHandler.install(figureComponent4);






     figureComponent4.getContentPane().setOpaque(false);
     figureComponent4.getContentPane().add(diagramPane2, "Center");
     figureComponent4.getContentPane().setPreferredSize(new Dimension(400, 500));

     figureComponent4.setPreferredLayer(-100);




     FigureComponentSelectionHandler selectionHandler2 = new FigureComponentSelectionHandler(diagramPane2);
     FigureComponentDragHandler dragHandler2 = new FigureComponentDragHandler(diagramPane2, null);

     FigureComponent figureComponent5 = new FigureComponent(diagramPane2);
     figureComponent5.setSelectionManager(diagramPane2.getSelectionManager());
     figureComponent5.getContentPane().add(new JLabel("asdads5"));


     selectionHandler2.install(figureComponent5);
     dragHandler2.install(figureComponent5);
     diagramPane2.addFigureComponent(figureComponent5, true);

     FigureComponent figureComponent6 = new FigureComponent(diagramPane2);
     figureComponent6.add(new JLabel("blabla6"));
     selectionHandler2.install(figureComponent6);
     dragHandler2.install(figureComponent6);
     diagramPane2.addFigureComponent(figureComponent6, true);

     ConnectorDecoratorSettings decoratorSettings = new ConnectorDecoratorSettings(true, new Font("monospaced", 0, 12), Color.WHITE);
     ConnectorDirect connector4 = new ConnectorDirect(diagramPane2, (ConnectorDecorator)new ConnectorDecoratorContains(decoratorSettings), new FigureAnchor(figureComponent5), new FigureAnchor(figureComponent6));
     diagramPane2.addConnector((Connector)connector4);
     diagramPane2.getConnectorManager().executeCommandOnConnectors((ConnectorCommand)new ConnectorVisibilityCommand(diagramPane2, true));
     diagramPane2.getConnectorManager().executeCommandOnConnectors((ConnectorCommand)new ConnectorAntialiasingCommand(diagramPane2, true));




     figureSelectionHandler.install(figureComponent4);
     figureComponentDragHandler.install(figureComponent4);


     figureSelectionHandler.install(figureComponent2);
     figureComponentDragHandler.install(figureComponent2);


     diagramPane.addFigureComponent(figureComponent, true);
     diagramPane.addFigureComponent(figureComponent2, true);
     diagramPane.addFigureComponent(figureComponent4, true);

     diagramPane2.getConnectorManager().addConnectorManager(diagramPane.getConnectorManager());
     Collection fC = diagramPane2.getFigureComponents();
     for (Iterator<FigureComponent> iterator = fC.iterator(); iterator.hasNext();)
     {
//       figureComponent123 = iterator.next();
     }

     figureComponent4.addChildren((FigureComponent[])fC.toArray((Object[])new FigureComponent[fC.size()]));

     ConnectorDirect connector = new ConnectorDirect(diagramPane, (ConnectorDecorator)new ConnectorDecoratorExtends(), new FigureAnchor(figureComponent), new FigureAnchor(figureComponent2));
     diagramPane.addConnector((Connector)connector);

     ConnectorDirect connector2 = new ConnectorDirect(diagramPane, (ConnectorDecorator)new ConnectorDecoratorImplements(), new FigureAnchor(figureComponent2), new FigureAnchor(figureComponent));
     diagramPane.addConnector((Connector)connector2);

     ConnectorDirect connector3 = new ConnectorDirect(diagramPane, (ConnectorDecorator)new ConnectorDecoratorImplements(), new FigureAnchor(figureComponent), new FigureAnchor(figureComponent3));
     diagramPane.addConnector((Connector)connector3);

     ConnectorDirect connector7 = new ConnectorDirect(diagramPane, (ConnectorDecorator)new ConnectorDecoratorImplements(), new FigureAnchor(figureComponent), new FigureAnchor(figureComponent5));
     diagramPane.addConnector((Connector)connector7);


     diagramPane.getConnectorManager().executeCommandOnConnectors((ConnectorCommand)new ConnectorVisibilityCommand(diagramPane, true));
     diagramPane.getConnectorManager().executeCommandOnConnectors((ConnectorCommand)new ConnectorAntialiasingCommand(diagramPane, false));


     FigureComponent stickyFigureComponent = new StickyFigureComponent(diagramPane);
     stickyFigureComponent.setPreferredSize(new Dimension(200, 50));
     stickyFigureComponent.setSelectionManager(diagramPane.getSelectionManager());
     resizeHandler.install(stickyFigureComponent);
     figureComponentDragHandler.install(stickyFigureComponent);
     figureSelectionHandler.install(stickyFigureComponent);
     diagramPane.addFigureComponent(stickyFigureComponent, true);






     TextAreaFigureComponent textComponent = new TextAreaFigureComponent(diagramPane);

     FigureComponentDragHandler dragHandler = new FigureComponentDragHandler(diagramPane, scrollPane);
     dragHandler.install(textComponent);

     resizeHandler = new FigureComponentResizeHandler(diagramPane);
     resizeHandler.install(textComponent);

     FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(diagramPane);
     selectionHandler.install(textComponent);

     diagramPane.addFigureComponent(textComponent, true);
     textComponent.setPreferredSize(new Dimension(50, 50));

     textComponent.setSelectionManager(diagramPane.getSelectionManager());
     textComponent.setSelected(false);



     HTMLFigureComponent textComponent3 = new HTMLFigureComponent(diagramPane);
     textComponent3.setTitleText("HTML");

     FigureComponentDragHandler dragHandler3 = new FigureComponentDragHandler(diagramPane, scrollPane);
     dragHandler3.install(textComponent3);

     resizeHandler = new FigureComponentResizeHandler(diagramPane);
     resizeHandler.install(textComponent3);

     FigureComponentSelectionHandler selectionHandler3 = new FigureComponentSelectionHandler(diagramPane);
     selectionHandler3.install(textComponent3);

     diagramPane.addFigureComponent(textComponent3, true);
     textComponent3.setPreferredSize(new Dimension(50, 50));

     textComponent3.setSelectionManager(diagramPane.getSelectionManager());
     textComponent3.setSelected(false);

     ActionContributorCommandPopupHandler popupHandler = new ActionContributorCommandPopupHandler(diagramPane, textComponent3);

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





   private static FigureComponent initFigure3(DiagramPane diagramPane, FigureComponent figureComponent2) {
     FigureComponent figureComponent3 = new FigureComponent(diagramPane);
     figureComponent3.add(new JLabel("asdasda"));
     figureComponent2.add(figureComponent3, "Center");
     figureComponent3.setSelectionManager(diagramPane.getSelectionManager());
     return figureComponent3;
   }



   private static FigureComponent initFigure2(DiagramPane diagramPane) {
     FigureComponent figureComponent2 = new FigureComponent(diagramPane);
     figureComponent2.setLayout(new BorderLayout());
     figureComponent2.add(new JLabel("TestFigure2"), "North");
     figureComponent2.add(new JButton("press"), "South");

     figureComponent2.setOpaque(false);

     figureComponent2.setPosX(100);
     figureComponent2.setPosY(100);

     figureComponent2.setSelectionManager(diagramPane.getSelectionManager());
     return figureComponent2;
   }



   private static FigureComponent initFigure1(DiagramPane diagramPane) {
     FigureComponent figureComponent = new FigureComponent(diagramPane);
     figureComponent.getContentPane().setLayout(new BorderLayout());
     figureComponent.getContentPane().add(new JLabel("TestFigure1"), "North");
     JTextArea textArea = new JTextArea("asdasdasdasdasdasdasd\nasd\nasd\nasd\nasd\nasd\nasd\nasd\nasd\nasd\nasd\nasd");
     textArea.setOpaque(false);
     figureComponent.getContentPane().setOpaque(false);
     figureComponent.getContentPane().add(new JScrollPane(textArea), "Center");

     figureComponent.setPosX(50);
     figureComponent.setPosY(50);

     figureComponent.setSelectionManager(diagramPane.getSelectionManager());

     return figureComponent;
   }
 }


