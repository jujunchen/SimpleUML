 package net.trustx.simpleuml.sequencediagram.display;

 import java.awt.BorderLayout;
 import java.awt.Font;
 import java.net.URL;
 import java.util.ResourceBundle;
 import javax.swing.Action;
 import javax.swing.ImageIcon;
 import javax.swing.JEditorPane;
 import javax.swing.JPanel;
 import javax.swing.event.DocumentEvent;
 import javax.swing.event.DocumentListener;
 import javax.swing.text.BadLocationException;
 import javax.swing.text.DefaultEditorKit;
 import javax.swing.text.Document;
 import net.trustx.simpleuml.sequencediagram.model.Model;
 import net.trustx.simpleuml.sequencediagram.model.ModelTextEvent;
 import net.trustx.simpleuml.sequencediagram.model.ModelTextListener;




















 public class Editor
   extends JPanel
   implements DocumentListener, ModelTextListener
 {
   private Model model;
   private JEditorPane editPane = new JEditorPane();

   private Action cutAction;

   private Action copyAction;

   private Action pasteAction;

   private ResourceBundle bundle;

   private boolean ignoreChange;
   private boolean documentChanged;
   private long lastChangeTime;

   public Editor(Model model) {
     this.model = model;
     setLayout(new BorderLayout());
     this.editPane.setFont(new Font("Monospaced", 0, this.editPane.getFont().getSize() + 1));


     add(this.editPane, "Center");

     this.bundle = ResourceBundle.getBundle("net.trustx.simpleuml.sequencediagram.components.Sequence");


     this.editPane.setText(model.getText());
     this.editPane.getDocument().addDocumentListener(this);
     (new Thread(new ChangeNotifier())).start();
   }



   public synchronized Action getCutAction() {
     if (this.cutAction == null) {

       this.cutAction = new DefaultEditorKit.CutAction();
       initAction(this.cutAction, "CutAction");
     }
     return this.cutAction;
   }



   public synchronized Action getPasteAction() {
     if (this.pasteAction == null) {

       this.pasteAction = new DefaultEditorKit.PasteAction();
       initAction(this.pasteAction, "PasteAction");
     }
     return this.pasteAction;
   }



   public synchronized Action getCopyAction() {
     if (this.copyAction == null) {

       this.copyAction = new DefaultEditorKit.CopyAction();
       initAction(this.copyAction, "CopyAction");
     }
     return this.copyAction;
   }



   private void initAction(Action act, String resourcePrefix) {
     act.putValue("Name", getResource(resourcePrefix, "name"));

     act.putValue("ShortDescription", getResource(resourcePrefix, "shortDesc"));

     act.putValue("SmallIcon", getIcon(resourcePrefix, "icon"));
   }




   String getResource(String resourcePrefix, String key) {
     return this.bundle.getString(resourcePrefix + "." + key);
   }



   ImageIcon getIcon(String resourcePrefix, String key) {
     URL iconURL = ClassLoader.getSystemResource(getResource(resourcePrefix, key));

     return new ImageIcon(iconURL, key);
   }



   public void modelTextChanged(ModelTextEvent mte) {
     if (mte.getSource() == this) {
       return;
     }

     Document doc = this.editPane.getDocument();

     try {
       this.ignoreChange = true;
       doc.remove(0, doc.getLength());
       doc.insertString(0, mte.getText(), null);
     }
     catch (BadLocationException ble) {

       ble.printStackTrace();
     }
     finally {

       this.ignoreChange = false;
     }
   }



   private void documentChanged(DocumentEvent e) {
     if (this.ignoreChange)
       return;
     this.documentChanged = true;
     this.lastChangeTime = System.currentTimeMillis();
   }



   public void changedUpdate(DocumentEvent e) {
     documentChanged(e);
   }



   public void insertUpdate(DocumentEvent e) {
     documentChanged(e);
   }



   public void removeUpdate(DocumentEvent e) {
     documentChanged(e);
   }


   private class ChangeNotifier
     implements Runnable
   {
     private ChangeNotifier() {}


     public void run() {
       while (true) {
         synchronized (Editor.this) {

           if (Editor.this.documentChanged)
           {
             if (System.currentTimeMillis() - Editor.this.lastChangeTime > 500L) {

               try {

                 Document doc = Editor.this.editPane.getDocument();
                 String s = doc.getText(0, doc.getLength());
                 Editor.this.model.setText(s, Editor.class);
                 Editor.this.documentChanged = false;
               }
               catch (BadLocationException ble) {

                 ble.printStackTrace();
               }
             }
           }
         }

         try {
           Thread.sleep(250L);
         }
         catch (InterruptedException ie) {}
       }
     }
   }
 }


