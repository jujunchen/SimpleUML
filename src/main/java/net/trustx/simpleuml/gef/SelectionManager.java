 package net.trustx.simpleuml.gef;
 
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.HashSet;
 import java.util.Iterator;
 
 

 
 public class SelectionManager
 {
   private HashSet selectables = new HashSet();
 
 
 
   
   public boolean add(Selectable selectable) {
     boolean retVal = this.selectables.add(selectable);
     selectable.selectionChanged();
     return retVal;
   }
 
 
   
   public boolean addAll(Collection selectables) {
     boolean modified = false;
     for (Iterator iterator = selectables.iterator(); iterator.hasNext(); ) {
        Object selectableObject = iterator.next();
       if (selectableObject instanceof Figure && selectableObject instanceof Selectable) {
         Selectable selectable = (Selectable)selectableObject;
         this.selectables.add(selectable);
         selectable.selectionChanged();
         modified = true;
       } 
     }
     return modified;
   }
 
 
   
   public void clear() {
     HashSet oldSelectables = new HashSet(this.selectables);
     this.selectables.clear();
     for (Iterator<Selectable> iterator = oldSelectables.iterator(); iterator.hasNext(); ) {
       Selectable selectable = iterator.next();
       selectable.selectionChanged();
     } 
   }
 
 
   
   public boolean contains(Selectable selectable) {
     return this.selectables.contains(selectable);
   }
 
 
   
   public boolean containsAll(Collection selectables) {
     for (Iterator iterator = selectables.iterator(); iterator.hasNext(); ) {
      Object selectableObject = iterator.next();
       if (selectableObject instanceof Selectable && this.selectables.contains(selectableObject))
       {
         return true;
       }
     }
     return false;
   }
 
 
   
   public boolean isEmpty() {
     return this.selectables.isEmpty();
   }
 
 
   
   public boolean remove(Selectable selectable) {
     boolean retVal = this.selectables.remove(selectable);
     selectable.selectionChanged();
     return retVal;
   }
 
 
   
   public boolean removeAll(Collection<?> selectables) {
     boolean modified = this.selectables.removeAll(selectables);
     for (Iterator<?> iterator = selectables.iterator(); iterator.hasNext(); ) {
       
       Selectable selectable = (Selectable)iterator.next();
       selectable.selectionChanged();
     } 
     return modified;
   }
 
 
   
   public int size() {
     return this.selectables.size();
   }
 

   
   public boolean executeCommandOnSelection(SelectableCommand command) {
     command.preExecution();
     
     for (Iterator<?> iterator = (new ArrayList(this.selectables)).iterator(); iterator.hasNext(); ) {
       
       Selectable selectable = (Selectable)iterator.next();
       boolean success = command.executeCommand(selectable);
       if (!success) {
         
         command.postExecution();
         return false;
       } 
     } 
     
     command.postExecution();
     return true;
   }
 

   
   public static boolean executeCommandOnSelectable(SelectableCommand selectableCommand, Selectable selectable) {
     selectableCommand.preExecution();
     boolean success = selectableCommand.executeCommand(selectable);
     selectableCommand.postExecution();
     return success;
   }
 }


