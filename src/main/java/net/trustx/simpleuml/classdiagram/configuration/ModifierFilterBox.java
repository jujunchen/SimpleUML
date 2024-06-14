 package net.trustx.simpleuml.classdiagram.configuration;

 import java.awt.BorderLayout;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.util.ArrayList;
 import java.util.Iterator;
 import javax.swing.Box;
 import javax.swing.DefaultListModel;
 import javax.swing.JButton;
 import javax.swing.JLabel;
 import javax.swing.JList;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.event.ListSelectionEvent;
 import javax.swing.event.ListSelectionListener;



 public class ModifierFilterBox
   extends JPanel
 {
   private JList filterList;
   private JButton addFilterButton;
   private JButton removeFilterButton;
   private DefaultListModel filterListModel;
   private ArrayList listenerList;

   public ModifierFilterBox(String borderTitle) {
     this.listenerList = new ArrayList();
     setLayout(new BorderLayout());

     this.filterListModel = new DefaultListModel();

     this.filterList = new JList(this.filterListModel);
     this.filterList.setCellRenderer(new ModifierListCellRenderer());
     this.filterList.setSelectionMode(0);
     this.filterList.setPrototypeCellValue("25");

     add(new JScrollPane(this.filterList), "Center");

     Box buttonBox = Box.createHorizontalBox();

     this.addFilterButton = new JButton("Add...");
     this.removeFilterButton = new JButton("Remove");
     this.removeFilterButton.setEnabled(false);

     buttonBox.add(Box.createHorizontalGlue());
     buttonBox.add(this.addFilterButton);
     buttonBox.add(Box.createHorizontalStrut(5));
     buttonBox.add(this.removeFilterButton);
     buttonBox.add(Box.createHorizontalGlue());
     add(buttonBox, "South");

     add(new JLabel(borderTitle), "North");
     this.filterList.addListSelectionListener(new ListSelectionListener()
         {
           public void valueChanged(ListSelectionEvent e)
           {
             if (ModifierFilterBox.this.filterList.getSelectedIndex() == -1) {

               ModifierFilterBox.this.removeFilterButton.setEnabled(false);
             }
             else {

               ModifierFilterBox.this.removeFilterButton.setEnabled(true);
             }
           }
         });

     this.removeFilterButton.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             int selectedIndex = ModifierFilterBox.this.filterList.getSelectedIndex();
             ModifierFilterBox.this.filterListModel.remove(selectedIndex);
             if (selectedIndex >= ModifierFilterBox.this.filterListModel.size() - 1)
             {
               selectedIndex = ModifierFilterBox.this.filterListModel.size() - 1;
             }
             if (selectedIndex > -1) {

               ModifierFilterBox.this.filterList.setSelectedIndex(selectedIndex);
               ModifierFilterBox.this.removeFilterButton.requestFocus();
             }
             ModifierFilterBox.this.notifyListeners();
           }
         });

     this.addFilterButton.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e)
           {
             ModifierChooserDialog modifierDialog = new ModifierChooserDialog(ModifierFilterBox.this);

             modifierDialog.setModal(true);
             modifierDialog.pack();
             modifierDialog.show();
             int mod = modifierDialog.getModifier();
             if (mod != -1 && !ModifierFilterBox.this.filterListModel.contains("" + mod)) {

               ModifierFilterBox.this.filterListModel.addElement("" + mod);
               ModifierFilterBox.this.filterList.setSelectedIndex(ModifierFilterBox.this.filterListModel.size() - 1);
               ModifierFilterBox.this.notifyListeners();
             }
           }
         });
   }





   public int[] getValues() {
     Object[] values = this.filterListModel.toArray();
     int[] result = new int[values.length];
     for (int i = 0; i < values.length; i++) {

       Object value = values[i];
       result[i] = Integer.parseInt(value.toString());
     }
     return result;
   }



   public void setValues(int[] values) {
     this.filterListModel.clear();
     for (int i = 0; i < values.length; i++)
     {
       this.filterListModel.addElement("" + values[i]);
     }
   }



   public void addActionListener(ActionListener actionListener) {
     this.listenerList.add(actionListener);
   }



   public void removeActionListener(ActionListener actionListener) {
     this.listenerList.remove(actionListener);
   }



   private void notifyListeners() {
     for (Iterator<ActionListener> iterator = this.listenerList.iterator(); iterator.hasNext(); ) {

       ActionListener actionListener = iterator.next();
       actionListener.actionPerformed(new ActionEvent(this, 0, ""));
     }
   }
 }


