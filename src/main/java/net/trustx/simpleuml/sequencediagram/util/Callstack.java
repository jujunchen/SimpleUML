 package net.trustx.simpleuml.sequencediagram.util;

 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiMethod;
 import java.io.IOException;
 import java.io.PushbackReader;
 import java.io.StringReader;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Stack;
 import net.trustx.simpleuml.sequencediagram.model.Call;
 import net.trustx.simpleuml.sequencediagram.model.CallReturn;
 import net.trustx.simpleuml.sequencediagram.model.Link;
 import net.trustx.simpleuml.sequencediagram.model.MethodInfo;
 import net.trustx.simpleuml.sequencediagram.model.ObjectInfo;




















 public class Callstack
 {
   private Stack callStack = new Stack();
   private List linkList = new ArrayList();
   private List objList = new ArrayList();

   private int currentHorizontalSeq;

   private int currentVerticalSeq;

   private int currentSeq;
   public static final int OPEN_TOKENC = 91;
   public static final String OPEN_TOKEN = "[";
   public static final int CLOSE_TOKENC = 93;
   public static final String CLOSE_TOKEN = "]";
   private static final String DEFAULT_METHOD = "aMethod";

   public Callstack() {
     this.objList.add(new ObjectInfo("anObject", this.currentHorizontalSeq));
     this.callStack.push(new CallInfo(this.objList.get(0), "aMethod", this.currentVerticalSeq));
     this.currentHorizontalSeq = 1;
     this.currentVerticalSeq = 0;
   }





   public void parse(String s) throws IOException {
     parse(new PushbackReader(new StringReader(s)));
   }






   public void parse(PushbackReader r) throws IOException {
     while (true) {
       skipWhitespace(r);
       int c = r.read();
       if (c == -1) {
         break;
       }

       if (c == 91) {

         String objName = readIdent(r);
         String methodName = readIdent(r);
         addCall(objName, methodName); continue;
       }
       if (c == 93)
       {
         addReturn();
       }
     }




     setActive();
   }



   public List getLinks() {
     return this.linkList;
   }



   public Stack getCallStack() {
     return this.callStack;
   }



   public List getObjects() {
     return this.objList;
   }



   private void clearActive() {
     Object[] objs = this.objList.toArray();
     for (int i = 0; i < objs.length; i++) {
       ((ObjectInfo)objs[i]).setActive(false);
     }
   }


   public void setActive() {
     clearActive();
     if (this.callStack.size() > 0) {
       ((CallInfo)this.callStack.get(this.callStack.size() - 1)).getObj().setActive(true);
     }
   }



   public PsiMethod getActive() {
     Iterator<Link> links = this.linkList.iterator();
     Link l = null;
     PsiMethod ret = null;
     while (links.hasNext()) {


       l = links.next();
       if (l != null && l.getTo().isActive()) {

         ret = (PsiMethod)l.getPsiElement();
         break;
       }
     }
     return ret;
   }



   public boolean inCallStack(String objectName) {
     boolean ret = false;
     Iterator<CallInfo> it = this.callStack.iterator();
     while (it.hasNext() && !ret) {

       CallInfo callInfo = it.next();
       if (callInfo.getObj().getName().equals(objectName))
         ret = true;
     }
     return ret;
   }



   public boolean fileInCallStack(String parent) {
     Iterator<CallInfo> stack = this.callStack.iterator();
     boolean ret = false;
     while (stack.hasNext()) {

       CallInfo currentCall = stack.next();
       if (parent != null && currentCall != null && currentCall.getObj().getPsiClass() != null && currentCall.getObj().getPsiClass().getParent().toString().equals(parent)) {




         ret = true;
         break;
       }
     }
     return ret;
   }




   public void addCall(String calledObject, String calledMethod) {
     ObjectInfo objInf = new ObjectInfo(calledObject, this.currentHorizontalSeq);
     int i = this.objList.indexOf(objInf);
     if (i == -1) {

       this.currentHorizontalSeq++;
       this.objList.add(objInf);
     }
     else {

       objInf = this.objList.get(i);
     }
     CallInfo inf = new CallInfo(objInf, calledMethod, this.currentVerticalSeq);

     CallInfo currentInf = this.callStack.peek();
     Call call = new Call(currentInf.getObj(), inf.getObj(), inf.getMethod());

     this.linkList.add(call);
     call.setVerticalSeq(this.currentVerticalSeq++);

     this.callStack.push(inf);
   }



   public void addCall(PsiClass psiClass, PsiMethod psiMethod, PsiElement calledFrom) {
     ObjectInfo objInf = new ObjectInfo(psiClass, this.currentHorizontalSeq);
     int i = this.objList.indexOf(objInf);
     if (i == -1) {

       this.currentHorizontalSeq++;
       this.objList.add(objInf);
     }
     else {

       objInf = this.objList.get(i);
     }
     if (psiMethod != null) {

       CallInfo inf = new CallInfo(objInf, psiMethod.getName(), calledFrom, this.currentVerticalSeq);

       CallInfo currentInf = this.callStack.peek();
       Call call = new Call(currentInf.getObj(), inf.getObj(), psiMethod);

       this.linkList.add(call);
       call.setVerticalSeq(this.currentVerticalSeq++);

       this.callStack.push(inf);
     }
   }




   public int addReturn() {
     CallInfo callInfo = this.callStack.pop();
     int addReturn = -1;
     MethodInfo mi = new MethodInfo(callInfo.getObj(), callInfo.getMethod(), callInfo.getStartingVerticalSeq(), this.currentVerticalSeq);



     if (!this.callStack.isEmpty()) {

       CallInfo currentInf = this.callStack.peek();
       CallReturn call = new CallReturn(callInfo.getObj(), currentInf.getObj(), currentInf.getPsiLocation());
       this.linkList.add(call);
       addReturn = this.currentVerticalSeq;
       call.setVerticalSeq(this.currentVerticalSeq++);
     }
     return addReturn;
   }





   private String readIdent(PushbackReader r) throws IOException {
     skipWhitespace(r);
     String s = readNonWhitespace(r);
     skipWhitespace(r);

     return s;
   }



   private ObjectInfo findSeq(int seq) {
     for (int i = 0; i < this.objList.size(); i++) {

       ObjectInfo call = this.objList.get(i);
       if (findMethod(seq, call.getMethods()) != null) {
         return call;
       }
     }

     return null;
   }




   private CallInfo findFromStack(int seq) {
     for (int j = 0; j < this.callStack.size(); j++) {

       CallInfo call = this.callStack.get(j);
       if (call.getStartingSeq() == this.currentSeq && !call.getMethod().equals("aMethod"))
         return call;
     }
     return null;
   }



   private MethodInfo findMethod(int seq, List<MethodInfo> methods) {
     for (int j = 0; j < methods.size(); j++) {

       MethodInfo method = methods.get(j);
       if (method.getStartSeq() == seq || method.getEndSeq() == seq)
       {
         return method;
       }
     }
     return null;
   }









   private String stripTrailingCr(String command) {
     String t = command;
     if (command.indexOf('\n', command.length() - 1) > -1)
       t = command.substring(0, command.length() - 1);
     return t;
   }



   public String toString() {
     String workingString = "";
     this.currentSeq = 0;
     for (int i = 0; this.currentSeq < this.currentVerticalSeq && i < this.linkList.size(); i++) {

       String thisSeq = "";
       CallInfo csi = null;
       thisSeq = getCallInfo();

       if (thisSeq == null) {

         thisSeq = "";
         if ((csi = findFromStack(this.currentSeq)) != null) {

           thisSeq = "[" + csi.getObj().getName() + " " + csi.getMethod();
           this.currentSeq++;
         }
       }

       if (thisSeq.equals("") && i < this.linkList.size()) {

         i--;
         this.currentSeq++;
       }
       else if (thisSeq.equals("]")) {

         workingString = stripTrailingCr(workingString);
       }
       workingString = workingString + thisSeq + "\n";
     }

     return stripTrailingCr(workingString);
   }










   private String getCallInfo() {
     String currentString = null; ObjectInfo call;
     if ((call = findSeq(this.currentSeq)) != null) {

       MethodInfo method = findMethod(this.currentSeq, call.getMethods());
       if (method.getStartSeq() == this.currentSeq) {

         currentString = "[" + call.getName() + " " + method.getName();
       }
       else if (method.getEndSeq() == this.currentSeq) {
         currentString = "]";
       } else {
         currentString = "ERROR";
       }  this.currentSeq++;
     }
     return currentString;
   }





   private void skipWhitespace(PushbackReader r) throws IOException {
     int c = -1;
     while (Character.isWhitespace((char)(c = r.read())));



     if (c != -1) {
       r.unread(c);
     }
   }



   private String readNonWhitespace(PushbackReader r) throws IOException {
     int c = -1;
     StringBuffer sb = new StringBuffer();
     while ((c = r.read()) != -1) {

       if (c == 93)
         break;
       if (Character.isWhitespace((char)c)) {
         break;
       }
       sb.append((char)c);
     }
     if (c != -1)
       r.unread(c);
     return sb.toString();
   }
 }


