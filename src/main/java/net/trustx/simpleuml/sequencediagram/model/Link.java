 package net.trustx.simpleuml.sequencediagram.model;

 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiMethod;
 import com.intellij.psi.PsiParameter;
 import com.intellij.psi.PsiReferenceList;
 import com.intellij.psi.PsiType;
 import com.intellij.psi.javadoc.PsiDocComment;
 import com.intellij.psi.javadoc.PsiDocTag;
 import org.jdom.Element;

















 public class Link
 {
   protected ObjectInfo from;
   protected ObjectInfo to;
   protected String name;
   protected int seq = -1;


   private PsiElement psiId;


   private String callComment;


   private boolean callOnDiagram;

   protected boolean deleted;


   public Link(ObjectInfo from, ObjectInfo to, String name) {
     this.from = from;
     this.to = to;
     this.name = name;
   }








   private String removeCrLf(String s) {
     String str = s;
     if (s == null)
       return "";
     int cr = 0;
     if ((cr = s.indexOf('\n')) >= 0)
     {
       str = s.substring(0, cr) + removeCrLf(s.substring(cr + 1));
     }
     return str.trim();
   }








   private String getComment(PsiMethod method) {
     PsiDocComment docComment = method.getDocComment();
     if (docComment == null) return "";

     PsiElement[] comments = docComment.getDescriptionElements();

     String comment = "";
     comment = method.getName();
     for (int i = 0; i < comments.length; i++)
     {
       comment = comment + " " + removeCrLf(comments[i].getText());
     }
     return comment;
   }







   public Element toElement() {
     Element link = null;
     if (this.psiId != null && this.psiId instanceof com.intellij.psi.PsiJavaToken) {

       PsiElement e = this.psiId;
       while (e != null) {

         if (e instanceof PsiMethod) {

           this.psiId = e;

           break;
         }
         e = e.getParent();
       }
     }
     if (this.psiId != null && this.psiId instanceof PsiMethod && this.psiId.getParent() != null) {

       PsiClass psiClass = (PsiClass)this.psiId.getParent();
       if (psiClass.getQualifiedName() != null) {

         PsiMethod psiMethod = (PsiMethod)this.psiId;
         link = new Element("Link");
         CreateLinkElement(link, psiClass, psiMethod);
         AddLinkComments(psiMethod, link);
         AddLinkReturn(psiMethod, link);
         AddLinkParams(psiMethod, link);
         AddLinkThrows(psiMethod, link);
         AddLinkCallComments(link);
       }
     }
     return link;
   }



   private void AddLinkCallComments(Element link) {
     Element callComments = new Element("CallComment");
     callComments.setAttribute(SaveDocAction.SHOW_ON_DIAGRAM, Boolean.valueOf(this.callOnDiagram).toString());
     callComments.addContent(this.callComment);
     link.addContent(callComments);
   }



   private void AddLinkThrows(PsiMethod psiMethod, Element link) {
     if (psiMethod == null)
       return;
     PsiReferenceList throwsList = psiMethod.getThrowsList();
     if ((throwsList.getChildren()).length <= 0) {
       return;
     }
     for (int i = 0; i < (throwsList.getChildren()).length; i++) {

       PsiElement e = throwsList.getChildren()[i];
       if (e.getText() != null && e.getText().trim().length() > 0 && !e.getText().trim().equals(",") && !e.getText().trim().equals("throws")) {





         Element throwsElement = new Element("Throws");
         throwsElement.setAttribute("Name", e.getText());
         throwsElement.setAttribute("Description", getComments(psiMethod, e.getText()));
         link.addContent(throwsElement);
         PsiElement lastChild = throwsList.getLastChild();
         if (lastChild != null)
         {
           lastChild.getText();
         }
       }
     }
   }



   private void AddLinkParams(PsiMethod psiMethod, Element link) {
     PsiParameter[] params = psiMethod.getParameterList().getParameters();
     Element args = new Element("Arguments");
     link.addContent(args);
     for (int i = 0; i < params.length; i++) {

       Element paramElement = new Element("Param");
       PsiParameter param = params[i];
       paramElement.setAttribute("Sequence", "" + i);
       paramElement.setAttribute("Name", param.getName());
       paramElement.setAttribute("Type", param.getType().getCanonicalText());
       paramElement.setAttribute("Description", getComments(psiMethod, param.getName()));
       args.addContent(paramElement);
     }
   }



   private void AddLinkReturn(PsiMethod psiMethod, Element link) {
     Element returnType = new Element("Return");
     PsiType methodReturnType = psiMethod.getReturnType();
     if (methodReturnType != null) {

       returnType.addContent(methodReturnType.getCanonicalText());
       returnType.setAttribute("Description", getCommentsOfType("return", psiMethod));
     }
     link.addContent(returnType);
   }



   private void AddLinkComments(PsiMethod psiMethod, Element link) {
     Element comment = new Element("Comment");
     comment.addContent(getComment(psiMethod));
     link.addContent(comment);
   }



   private void CreateLinkElement(Element link, PsiClass psiClass, PsiMethod psiMethod) {
     link.setAttribute("Id", Integer.toString(this.seq));
     link.setAttribute("Class", psiClass.getQualifiedName());
     link.setAttribute("Method", psiMethod.getName());
     if (this instanceof CallReturn) {
       link.setAttribute("Return", "true");
     } else {
       link.setAttribute("Return", "false");
     }
   }








   private String getComments(PsiMethod psiMethod, String name) {
     PsiDocComment docComment = psiMethod.getDocComment();
     if (docComment != null) {

       PsiDocTag[] elements = docComment.getTags();
       for (int i = 0; i < elements.length; i++) {

         if (elements[i] != null && elements[i].getValueElement() != null && elements[i].getValueElement().getText() != null && elements[i].getValueElement().getText().equals(name)) {




           String paramComment = "";
           for (int j = 1; j < (elements[i].getDataElements()).length; j++)
             paramComment = paramComment + elements[i].getDataElements()[j].getText() + " ";
           return paramComment;
         }
       }
     }
     return "";
   }









   private String getCommentsOfType(String type, PsiMethod psiMethod) {
     PsiDocComment docComment = psiMethod.getDocComment();
     if (docComment != null) {

       PsiDocTag[] elements = docComment.getTags();
       for (int i = 0; i < elements.length; i++) {

         String name = elements[i].getName();
         if (name != null && name.equals(type)) {

           String paramComment = "";
           for (int j = 0; j < (elements[i].getDataElements()).length; j++)
             paramComment = paramComment + elements[i].getDataElements()[j].getText() + " ";
           return paramComment;
         }
       }
     }
     return "";
   }



   public String getName() {
     int startMethodSig = 0;
     String name = this.name;
     if ((startMethodSig = name.indexOf('(')) > 0)
       name = this.name.substring(0, startMethodSig - 1);
     return name;
   }



   public PsiElement getPsiElement() {
     return this.psiId;
   }



   public void setPsiElement(PsiElement id) {
     this.psiId = id;
   }



   public void setVerticalSeq(int y) {
     this.seq = y;
   }



   public int getVerticalSeq() {
     return this.seq;
   }



   public ObjectInfo getFrom() {
     return this.from;
   }



   public ObjectInfo getTo() {
     return this.to;
   }



   public String toString() {
     return "Link from " + this.from + " to " + this.to;
   }



   public String getCallComment() {
     return this.callComment;
   }



   public boolean getCommentOnDiagram() {
     return this.callOnDiagram;
   }



   public void setCommentsOnDiagram(boolean b) {
     this.callOnDiagram = b;
   }



   public void setCallComment(String c) {
     this.callComment = c;
   }



   public boolean isDeleted() {
     return this.deleted;
   }

   public void delete() {}
 }


