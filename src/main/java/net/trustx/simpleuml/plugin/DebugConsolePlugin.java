 package net.trustx.simpleuml.plugin;

 import com.intellij.openapi.actionSystem.ActionGroup;
 import com.intellij.openapi.actionSystem.ActionManager;
 import com.intellij.openapi.actionSystem.ActionToolbar;
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DefaultActionGroup;
 import com.intellij.openapi.components.ProjectComponent;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.wm.ToolWindow;
 import com.intellij.openapi.wm.ToolWindowAnchor;
 import com.intellij.openapi.wm.ToolWindowManager;
 import com.intellij.ui.content.Content;
 import java.awt.BorderLayout;
 import java.awt.EventQueue;
 import java.awt.Font;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.io.PrintStream;
 import javax.swing.Icon;
 import javax.swing.ImageIcon;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.JTextArea;

















 public class DebugConsolePlugin
   extends OutputStream
   implements ProjectComponent
 {
   public static final String TOOL_WINDOW_ID = "DC";
   private Project myProject;
   private JTextArea textArea;
   private PrintStream defaultOut;
   private PrintStream defaultErr;

   public DebugConsolePlugin(Project project) {
     this.myProject = project;
   }



   public void projectOpened() {
     initToolWindow();
   }



   private void initToolWindow() {
     ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(this.myProject);

     ToolWindow toolWindow = toolWindowManager.getToolWindow("DC");
     if (toolWindow != null) {

       toolWindow.activate(null);

       return;
     }
     JPanel panel = new JPanel(new BorderLayout());
     this.textArea = new JTextArea();

     this.textArea.setFont(new Font("Monospaced", 0, 12));
     panel.add(new JScrollPane(this.textArea), "Center");

     this.defaultOut = System.out;
     this.defaultErr = System.err;

     System.setOut(new PrintStream(this));
     System.setErr(new PrintStream(this));

     ToolWindow window = toolWindowManager.registerToolWindow("DC", false, ToolWindowAnchor.BOTTOM);
     Content content = window.getContentManager().getFactory().createContent(panel, "Output", false);
     window.getContentManager().addContent(content);


     DefaultActionGroup defaultActionGroup = initToolbarActionGroup();

     ActionToolbar toolBar = ActionManager.getInstance().createActionToolbar("DebugConsole.Toolbar", (ActionGroup)defaultActionGroup, false);
     panel.add(toolBar.getComponent(), "West");
   }



   private DefaultActionGroup initToolbarActionGroup() {
     DefaultActionGroup actionGroup = new DefaultActionGroup();

     AnAction closeAction = new AnAction("Close", "Close ToolWindow", new ImageIcon(DebugConsolePlugin.class.getResource("/actions/cancel.png")))
       {
         public void actionPerformed(AnActionEvent e)
         {
           System.setOut(DebugConsolePlugin.this.defaultOut);
           System.setErr(DebugConsolePlugin.this.defaultErr);

           ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(DebugConsolePlugin.this.myProject);
           toolWindowManager.unregisterToolWindow("DC");
         }
       };

     AnAction clearAction = new AnAction("Clear", "Clear Ouput", new ImageIcon(DebugConsolePlugin.class.getResource("/actions/gc.png")))
       {
         public void actionPerformed(AnActionEvent e)
         {
           DebugConsolePlugin.this.textArea.setText("");
         }
       };


     actionGroup.add(closeAction);
     actionGroup.add(clearAction);

     return actionGroup;
   }




   public void projectClosed() {}



   public String getComponentName() {
     return "DC";
   }




   public void initComponent() {}




   public void disposeComponent() {}



   public void write(final int b) throws IOException {
     if (EventQueue.isDispatchThread()) {

       this.textArea.append(String.valueOf((char)b));
       this.textArea.setCaretPosition(this.textArea.getDocument().getLength());
     }
     else {

       EventQueue.invokeLater(new Runnable()
           {
             public void run()
             {
               DebugConsolePlugin.this.textArea.append(String.valueOf((char)b));
               DebugConsolePlugin.this.textArea.setCaretPosition(DebugConsolePlugin.this.textArea.getDocument().getLength());
             }
           });
     }
   }



   public void write(final byte[] b) throws IOException {
     if (EventQueue.isDispatchThread()) {

       this.textArea.append(new String(b));
       this.textArea.setCaretPosition(this.textArea.getDocument().getLength());
     }
     else {

       EventQueue.invokeLater(new Runnable()
           {
             public void run()
             {
               DebugConsolePlugin.this.textArea.append(new String(b));
               DebugConsolePlugin.this.textArea.setCaretPosition(DebugConsolePlugin.this.textArea.getDocument().getLength());
             }
           });
     }
   }




   public void write(final byte[] b, final int off, final int len) throws IOException {
     if (EventQueue.isDispatchThread()) {

       this.textArea.append(new String(b, off, len));
       this.textArea.setCaretPosition(this.textArea.getDocument().getLength());
     }
     else {

       EventQueue.invokeLater(new Runnable()
           {
             public void run()
             {
               DebugConsolePlugin.this.textArea.append(new String(b, off, len));
               DebugConsolePlugin.this.textArea.setCaretPosition(DebugConsolePlugin.this.textArea.getDocument().getLength());
             }
           });
     }
   }
 }


