 package net.trustx.simpleuml.sequencediagram.model;

 import net.trustx.simpleuml.components.DiagramSaver;
 import org.jdom.Document;
 import org.jdom.Element;












 public class SaveDocAction
   implements DiagramSaver
 {
   Model model;
   public static final String DEFINITION = "Definition";
   public static final String LINKS = "Links";
   public static final String LINK = "Link";
   public static final String CLASSNAME = "Class";
   public static final String METHODNAME = "Method";
   public static final String METHOD_COMMENT = "Comment";
   public static final String CALL_COMMENT = "CallComment";
   public static final String METHOD_RETURN = "Return";
   public static final String RETURN_DESC = "Description";
   public static final String RETURNCALL = "Return";
   public static final String METHOD_ARGS = "Arguments";
   public static final String METHOD_PARAM = "Param";
   public static final String METHOD_THROWS = "Throws";
   public static final String PARAM_SEQ = "Sequence";
   public static final String PARAM_NAME = "Name";
   public static final String PARAM_TYPE = "Type";
   public static final String PARAM_DESC = "Description";
   public static final String EXCEPTION_NAME = "Name";
   public static final String SEQUENCE = "Id";
   public static String SHOW_ON_DIAGRAM = "ShowOnDiagram";








   public SaveDocAction(Model model) {
     this.model = model;
   }








   public Document saveToDocument() {
     Element root = new Element("Sequencediagram");
     Element def = new Element("Definition");
     if (this.model.getText() != null) {

       def.addContent(this.model.getText());
       root.addContent(def);
       root.addContent(this.model.getLinksElement());
     }
     return new Document(root);
   }
 }


