 package net.trustx.simpleuml.sequencediagram.model;

 import com.intellij.openapi.project.Project;
 import com.intellij.psi.JavaPsiFacade;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiMethod;
 import com.intellij.psi.PsiParameter;
 import com.intellij.psi.search.GlobalSearchScope;
 import java.beans.PropertyChangeListener;
 import java.io.BufferedReader;
 import java.io.BufferedWriter;
 import java.io.File;
 import java.io.FileReader;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.io.PrintWriter;
 import java.io.StringReader;
 import java.util.Iterator;
 import java.util.List;
 import javax.swing.event.EventListenerList;
 import javax.swing.event.SwingPropertyChangeSupport;
 import net.trustx.simpleuml.components.DiagramSaver;
 import net.trustx.simpleuml.sequencediagram.display.GotoClassAction;
 import net.trustx.simpleuml.sequencediagram.util.CallInfo;
 import net.trustx.simpleuml.sequencediagram.util.Callstack;
 import net.trustx.simpleuml.sequencediagram.util.PsiClassUtil;
 import org.jdom.Content;
 import org.jdom.Element;














 public class Model
 {
   private String sequenceString = " ";

   private SwingPropertyChangeSupport changeSupport;

   private File file;
   private boolean modified;
   private DiagramSaver saveDocSaver = new SaveDocAction(this);

   private EventListenerList listenerList = new EventListenerList();


   private Callstack persistentStack;


   private GotoClassAction gotoAction;


   public Model(Project project) {
     this.changeSupport = new SwingPropertyChangeSupport(this);
     this.gotoAction = new GotoClassAction(this, project);
   }





   public void addPropertyChangeListener(String propName, PropertyChangeListener listener) {
     this.changeSupport.addPropertyChangeListener(propName, listener);
   }




   void removePropertyChangeListener(String propName, PropertyChangeListener listener) {
     this.changeSupport.removePropertyChangeListener(propName, listener);
   }



   boolean loadNew() {
     setFile(null);
     internalSetText("", this);
     setModified(false);
     return true;
   }




   boolean readFromFile(File f) {
     try {
       StringBuffer sb = new StringBuffer(1024);
       BufferedReader br = new BufferedReader(new FileReader(f));
       String s = null;
       while ((s = br.readLine()) != null) {

         sb.append(s);
         sb.append("\n");
       }
       br.close();
       setFile(f);
       internalSetText(sb.toString(), this);
       setModified(false);
       return true;
     }
     catch (IOException ioe) {

       ioe.printStackTrace();
       return false;
     }
   }




   boolean writeToFile(File f) {
     try {
       PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));

       BufferedReader br = new BufferedReader(new StringReader(getText()));

       String s = null;
       while ((s = br.readLine()) != null)
       {
         out.println(s);
       }
       out.close();
       setFile(f);
       setModified(false);
       return true;
     }
     catch (IOException ioe) {

       ioe.printStackTrace();
       return false;
     }
   }



   boolean isModified() {
     return this.modified;
   }



   void setModified(boolean modified) {
     boolean oldModified = this.modified;
     this.modified = modified;

     if (modified != oldModified) {
       this.changeSupport.firePropertyChange("modified", oldModified, modified);
     }
   }




   private void setFile(File f) {
     File oldFile = this.file;
     this.file = f;
     this.changeSupport.firePropertyChange("file", oldFile, this.file);
   }





   public String getText() {
     return this.sequenceString;
   }






   public void addCall(PsiClass className, PsiMethod methodName, PsiMethod selectedLocation, String selectedFile) {
     Callstack currentStack = getCurrentCallstack();
     if (this.persistentStack != null && selectedLocation != null && selectedLocation.getParent() != null) {



       PsiMethod activeMethod = this.persistentStack.getActive();
       boolean addReturns = this.persistentStack.inCallStack(((PsiClass)selectedLocation.getParent()).getName());






       while (addReturns && activeMethod != null && !PsiClassUtil.equal((PsiClass)activeMethod.getParent(), (PsiClass)selectedLocation.getParent())) {



         this.persistentStack.addReturn();
         currentStack.addReturn();
         this.persistentStack.setActive();
         activeMethod = this.persistentStack.getActive();
       }
     }

     currentStack.addCall(className, methodName, (PsiElement)selectedLocation);
     updatePersistentStack(currentStack);
     currentStack.setActive();
     setText(currentStack.toString(), Model.class);
   }



   private void updatePersistentStack(Callstack currentStack) {
     if (this.persistentStack == null) {
       this.persistentStack = currentStack;
     } else {
       this.persistentStack = mergeStack(currentStack, this.persistentStack.getLinks(), this.persistentStack.getCallStack());
     }
   }




   private Callstack getCurrentCallstack() {
     Callstack currentStack = new Callstack();

     try {
       if (this.sequenceString.trim().length() != 0) {
         currentStack.parse(this.sequenceString);
       }
     } catch (IOException e) {

       e.printStackTrace();
     }
     return currentStack;
   }












   private synchronized Callstack mergeStack(Callstack currentStack, List sourceLinks, List sourceStack) {
     mergeCompleteCalls(sourceLinks, currentStack);
     mergeIncompleteCalls(sourceStack, currentStack);

     return currentStack;
   }










   private void mergeCompleteCalls(List sourceLinks, Callstack currentStack) {
     Iterator<Link> it = sourceLinks.iterator();
     while (it.hasNext()) {

       Link currentLink = it.next();
       for (int i = 0; i < currentStack.getLinks().size(); i++) {

         Link checkLink = (Link) currentStack.getLinks().get(i);
         if (currentLink.getName().equals(checkLink.getName()) && currentLink.getTo().getName().equals(checkLink.getTo().getName())) {


           checkLink.setPsiElement(currentLink.getPsiElement());
           checkLink.setCallComment(currentLink.getCallComment());
           checkLink.setCommentsOnDiagram(currentLink.getCommentOnDiagram());
         }
       }
     }
   }











   private void mergeIncompleteCalls(List sourceStack, Callstack currentStack) {
     Iterator<CallInfo> it = sourceStack.iterator();
     int newStackPosition = 0;
     while (it.hasNext()) {

       CallInfo info = it.next();
       CallInfo newInfo = null;
       if (currentStack.getCallStack().size() > newStackPosition) {
         newInfo = (CallInfo) currentStack.getCallStack().get(newStackPosition);


         if (methodsEqual(info.getMethod(), newInfo.getMethod())) {


           newInfo.setPsiLocation(info.getPsiLocation());

         }
         else {

           if (sourceStack.size() > currentStack.getCallStack().size()) {
             continue;
           }


           if (sourceStack.size() < currentStack.getCallStack().size() && methodsEqual(info.getMethod(), (newInfo = (CallInfo) currentStack.getCallStack().get(newStackPosition + 1)).getMethod())) {



             newInfo.setPsiLocation(info.getPsiLocation());
             newStackPosition++;
           }
         }
         newStackPosition++;
       }
     }
   }









   private boolean methodWithin(String m1, String m2) {
     if (m1 == null) return false;

     int startParen = m1.indexOf("(");
     return (startParen > 1 && m1.substring(0, startParen - 1).equals(m2));
   }











   private boolean methodsEqual(String m1, String m2) {
     if (m1.equals(m2))
       return true;
     if (methodWithin(m1, m2) || methodWithin(m2, m1))
     {
       return true; }
     return false;
   }



   public void addReturn() {
     int starts = 0;
     int ends = 0;
     for (int i = 0; i < this.sequenceString.length(); i++) {

       if (this.sequenceString.charAt(i) == '[') {
         starts++;
       } else if (this.sequenceString.charAt(i) == ']') {
         ends++;
       }
     }  if (starts > ends) {

       Callstack currentCallStack = getCurrentCallstack();
       if (this.persistentStack != null) {

         this.persistentStack = mergeStack(currentCallStack, this.persistentStack.getLinks(), this.persistentStack.getCallStack());



         int sequence = this.persistentStack.addReturn();
         if (sequence >= 0) {
           this.gotoAction.fire(sequence);
         }
         setText(this.sequenceString + "]", Model.class);
       }
     }
   }



   public void setText(String s, Object setter) {
     internalSetText(s, setter);
     setModified(true);
     if (setter != Model.class) {

       Callstack currentStack = getCurrentCallstack();
       updatePersistentStack(currentStack);
     }
   }




   private void internalSetText(String s, Object setter) {
     this.sequenceString = s;
     fireModelTextEvent(s, setter);
   }



   File getFile() {
     return this.file;
   }








   public void addModelTextListener(ModelTextListener l) {
     this.listenerList.add(ModelTextListener.class, l);
   }



   public void removeModelTextListener(ModelTextListener l) {
     this.listenerList.remove(ModelTextListener.class, l);
   }



   public void refresh(Object requestor) {
     fireModelTextEvent(getText(), requestor);
   }



   private synchronized void fireModelTextEvent(String s, Object setter) {
     ModelTextEvent mte = new ModelTextEvent(setter, s);
     Object[] listeners = this.listenerList.getListenerList();
     for (int i = listeners.length - 2; i >= 0; i -= 2) {

       if (listeners[i] == ModelTextListener.class) {
         ((ModelTextListener)listeners[i + 1]).modelTextChanged(mte);
       }
     }
   }


   public DiagramSaver getSaveDocAction() {
     return this.saveDocSaver;
   }



   public PsiElement getPsiElement(int sequence) {
     PsiElement psId = null;
     if (this.persistentStack == null)
       return null;
     if (sequence < this.persistentStack.getLinks().size()) {

       Link l = (Link) this.persistentStack.getLinks().get(sequence);
       psId = l.getPsiElement();
     }
     return psId;
   }



   public Element getLinksElement() {
     Element links = new Element("Links");
     if (this.persistentStack != null) {

       Iterator<Link> it = this.persistentStack.getLinks().iterator();
       while (it.hasNext()) {

         Link link = it.next();
         Element linkElement = link.toElement();
         if (linkElement != null)
           links.addContent(linkElement);
       }
     }
     return links;
   }



   public Link getLink(int sequence) {
     Link l = null;
     if (this.persistentStack.getLinks().size() > sequence)
       l = (Link) this.persistentStack.getLinks().get(sequence);
     return l;
   }



   private void updateLinkData(Project project, Element child) {
     Iterator<Link> it = this.persistentStack.getLinks().iterator();
     while (it.hasNext()) {

       Link l = it.next();
       if (child.getChildren().size() > 0) {

         Element linkData = getLinkData(child, Integer.toString(l.getVerticalSeq()));
         PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(linkData.getAttribute("Class").getValue(), GlobalSearchScope.allScope(project));
         if (psiClass == null)
           continue;
         String methodName = linkData.getAttribute("Method").getValue();
         l.setPsiElement(findPsiElement(psiClass.getAllMethods(), methodName, getMethodArgs(linkData.getContent())));


         if (linkData.getChildTextTrim("CallComment") == null) {

           l.setCommentsOnDiagram(false);

           continue;
         }
         Element call = linkData.getChild("CallComment");
         l.setCallComment(call.getTextTrim());
         l.setCommentsOnDiagram(call.getAttributeValue(SaveDocAction.SHOW_ON_DIAGRAM).equals("true"));
       }
     }
   }




   private Element getMethodArgs(List<Content> methodInfo) {
     Element methodArgs = null;
     for (int k = 0; k < methodInfo.size(); k++) {

       if (methodInfo.get(k) instanceof Element) {

         methodArgs = (Element) methodInfo.get(k);
         if (methodArgs.getName().equals("Arguments"))
           break;
       }
     }
     return methodArgs;
   }



   private Element getLinkData(Element child, String linksSequence) {
     int i = 0;
     Element linkData = child.getChildren().get(i);

     while (!linkData.getAttribute("Id").getValue().equals(linksSequence) && i + 1 < child.getChildren().size()) {

       i++;
       linkData = child.getChildren().get(i);
     }
     return linkData;
   }



   private boolean checkParam(int number, List<Element> savedParams, String type) {
     String lookingForType = ((Element)savedParams.get(number)).getAttributeValue("Type");
     return type.equals(lookingForType);
   }



   private PsiElement findPsiElement(PsiMethod[] psiMethods, String methodName, Element methodArgs) {
     int j = 0;
     boolean found = false;
     while (psiMethods.length > j) {

       String method = psiMethods[j].getName();

       if (!(found = method.equals(methodName))) {
         j++;
         continue;
       }
       if (psiMethods[j] != null && (psiMethods[j].getParameterList().getParameters()).length == methodArgs.getChildren().size()) {



         List savedParams = methodArgs.getChildren();
         PsiParameter[] methodParams = psiMethods[j].getParameterList().getParameters();
         boolean match = false;
         for (int b = 0; b < savedParams.size();) {

           if (checkParam(b, savedParams, methodParams[b].getType().getCanonicalText())) {
             match = true;

             b++;
           }
         }
         if (match || methodParams.length == 0) {

           found = true;

           break;
         }

         j++;

         continue;
       }
       j++;
     }
     return (found && j < psiMethods.length) ? (PsiElement)psiMethods[j] : null;
   }



   public void load(Project project, String def, Element psiData) {
     this.sequenceString = def;
     this.persistentStack = getCurrentCallstack();
     updateLinkData(project, psiData);

     setText(def, this);
   }



   public Callstack getCallstack() {
     return this.persistentStack;
   }



   public void expunge() {
     List list = this.persistentStack.getLinks();
     int deletes = 0;
     for (int i = 0; i < list.size(); i++) {

       Object obj = list.get(i);
       Link link = (Link)obj;

       if (link.isDeleted() || deletes > 0) {

         if (obj instanceof Call) {

           deletes++;
           for (int index = 0; index < link.getTo().getMethods().size(); index++) {

             if (((MethodInfo)link.getTo().getMethods().get(index)).getName().equals(link.name)) {

               link.getTo().getMethods().remove(index);

               break;
             }
           }
         } else {
           deletes--;
         }  list.remove(i);
         i--;
       }
     }

     internalSetText(this.persistentStack.toString(), Model.class);
   }
 }


