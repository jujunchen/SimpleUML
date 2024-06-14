 package net.trustx.simpleuml.sequencediagram.model;

 import com.intellij.psi.PsiClass;
 import java.util.ArrayList;
 import java.util.List;
 import net.trustx.simpleuml.sequencediagram.util.PsiClassUtil;























 public class ObjectInfo
 {
   private String name;
   private int seq;
   private List methods = new ArrayList();

   private boolean active;
   private PsiClass psiClass;

   public ObjectInfo(String name, int seq) {
     this.name = name;
     this.seq = seq;
   }


   public ObjectInfo(PsiClass psiId, int seq) {
     this(PsiClassUtil.getClassName(psiId), seq);
     this.psiClass = psiId;
   }


   public boolean isActive() {
     return this.active;
   }


   public void setActive(boolean a) {
     this.active = a;
   }


   public PsiClass getPsiClass() {
     return this.psiClass;
   }


   public void addMethod(MethodInfo mi) {
     int possible = -1;
     for (int i = 0; i < this.methods.size(); i++) {

       MethodInfo otherMethod = (MethodInfo) this.methods.get(i);
       if (otherMethod.getStartSeq() > mi.getStartSeq()) {

         possible = i;
         break;
       }
     }
     if (possible == -1) {
       this.methods.add(mi);
     } else {
       this.methods.add(possible, mi);
     }
   }

   public List getMethods() {
     return this.methods;
   }


   public String getName() {
     return this.name;
   }


   public int getSeq() {
     return this.seq;
   }


   public String toString() {
     return "Object " + this.name + " seq " + this.seq;
   }


   public int hashCode() {
     return this.name.hashCode();
   }


   public boolean equals(Object o) {
     return this.name.equals(((ObjectInfo)o).name);
   }
 }


