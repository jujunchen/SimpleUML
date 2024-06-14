 package net.trustx.simpleuml.components;
 
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.DialogWrapper;
 import com.intellij.openapi.ui.Messages;
 import java.awt.BorderLayout;
 import java.awt.Dimension;
 import java.util.StringTokenizer;
 import javax.swing.Action;
 import javax.swing.Box;
 import javax.swing.JComponent;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.JTextArea;
 

 public class InternalErrorDialog
   extends DialogWrapper
 {
   private JPanel centerPanel;
   private JTextArea exceptionTextArea;
   
   public InternalErrorDialog(Project project, String title, String message, Throwable[] throwables) {
     super(project, false);
     
     this.centerPanel = new JPanel(new BorderLayout(5, 20));
     
     Box labelBox = Box.createVerticalBox();
     
     StringTokenizer tokenizer = new StringTokenizer(message, "\n");
     while (tokenizer.hasMoreElements()) {
       
       String s = (String)tokenizer.nextElement();
       labelBox.add(new JLabel(s));
     } 
     
     JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
     headerPanel.add(labelBox, "Center");
     headerPanel.add(new JLabel(Messages.getErrorIcon()), "West");
     
     this.centerPanel.add(headerPanel, "North");
     
     this.exceptionTextArea = new JTextArea("");
     
     for (int i = 0; i < throwables.length; i++) {
       
       Throwable throwable = throwables[i];
       String exceptionText = getExceptionText(throwable, new StringBuffer()).toString();
       this.exceptionTextArea.append(exceptionText);
       this.exceptionTextArea.append("\n");
     } 
     
     this.exceptionTextArea.setCaretPosition(0);
     this.exceptionTextArea.setEditable(false);
     
     JScrollPane scrollPane = new JScrollPane(this.exceptionTextArea);
     scrollPane.setPreferredSize(new Dimension(500, 300));
     this.centerPanel.add(scrollPane, "Center");
     
     setTitle(title);
     
     init();
   }
 
 
   
   public InternalErrorDialog(Project project, String title, String message, Throwable throwable) {
     this(project, title, message, new Throwable[] { throwable });
   }
 
 
   
   private StringBuffer getExceptionText(Throwable throwable, StringBuffer stringBuffer) {
     stringBuffer.append(throwable).append('\n');
     StackTraceElement[] trace = throwable.getStackTrace();
     for (int i = 0; i < trace.length; i++)
     {
       stringBuffer.append("\tat ").append(trace[i]).append('\n');
     }
     
     Throwable cause = throwable.getCause();
     if (cause != null)
     {
       getExceptionText(cause, stringBuffer);
     }
     
     return stringBuffer;
   }
 
 
   
   protected JComponent createCenterPanel() {
     return this.centerPanel;
   }
 
 
   
   protected Action[] createActions() {
     setOKButtonText("Ok");
     return new Action[] { getOKAction() };
   }
 }


