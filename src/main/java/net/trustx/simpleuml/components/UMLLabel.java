 package net.trustx.simpleuml.components;
 
 import com.intellij.psi.PsiElement;
 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.Point;
 import java.awt.RenderingHints;
 import java.awt.event.MouseEvent;
 import java.awt.font.FontRenderContext;
 import java.awt.geom.Rectangle2D;
 import javax.swing.BorderFactory;
 import javax.swing.Icon;
 import javax.swing.JComponent;
 import javax.swing.JLabel;
 import javax.swing.JToolTip;
 import javax.swing.ToolTipManager;
 
 public class UMLLabel
   extends JLabel
 {
   private boolean underscored;
   private JToolTip toolTip;
   private PsiElement psiElement;
   
   public UMLLabel(String text, Icon icon, int horizontalAlignment) {
     super(text, icon, horizontalAlignment);
     init();
   }
 
 
   
   public UMLLabel(String text, int horizontalAlignment) {
     super(text, horizontalAlignment);
     init();
   }
 
 
   
   public UMLLabel(String text) {
     super(text);
     init();
   }
 
 
   
   public UMLLabel(Icon image, int horizontalAlignment) {
     super(image, horizontalAlignment);
     init();
   }
 
 
   
   public UMLLabel(Icon image) {
     super(image);
     init();
   }
 
 
   
   public UMLLabel() {
     init();
   }
 
 
   
   private void init() {
     setForeground(Color.BLACK);
   }
 
 
   
   public void setUnderscored(boolean underscored) {
     this.underscored = underscored;
   }
 
 
   
   private Dimension calcSize() {
     FontRenderContext frc = new FontRenderContext(null, false, true);
     Rectangle2D r2d = getFont().getStringBounds(getText(), frc);
     return new Dimension((r2d.getBounds()).width + 1, (r2d.getBounds()).height + 1);
   }
 
 
   
   public Dimension getMaximumSize() {
     return calcSize();
   }
 
 
   
   public Dimension getMinimumSize() {
     return calcSize();
   }
 
 
   
   public Dimension getPreferredSize() {
     return calcSize();
   }
 
 
   
   public void paint(Graphics g) {
     ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
     super.paint(g);
   }
 
 
   
   public void paintAll(Graphics g) {
     ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
     super.paintAll(g);
   }
 
 
   
   protected void paintBorder(Graphics g) {
     ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
     super.paintBorder(g);
   }
 
 
   
   protected void paintChildren(Graphics g) {
     ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
     super.paintChildren(g);
   }
 
 
   
   public void paintComponents(Graphics g) {
     ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
     super.paintComponents(g);
   }
 
 
   
   protected void paintComponent(Graphics g) {
     ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
     super.paintComponent(g);
     if (this.underscored)
     {
       g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
     }
   }
 
 
   
   public void setToolTipComponent(JComponent toolTipComponent) {
     this.toolTip = new JToolTip();
     this.toolTip.setLayout(new BorderLayout());
     this.toolTip.add(toolTipComponent, "Center");
     this.toolTip.setBorder(BorderFactory.createLineBorder(Color.BLACK));
     
     Dimension dim = toolTipComponent.getPreferredSize();
     dim.width += 4;
     dim.height += 4;
     toolTipComponent.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
     this.toolTip.setPreferredSize(dim);
     ToolTipManager.sharedInstance().registerComponent(this);
   }
 
 
   
   public String getToolTipText() {
     if (this.toolTip == null)
     {
       return super.getToolTipText();
     }
     return " ";
   }
 
 
   
   public Point getToolTipLocation(MouseEvent event) {
     if (this.toolTip == null)
     {
       return super.getToolTipLocation(event);
     }
     return new Point(0, -1);
   }
 
 
   
   public JToolTip createToolTip() {
     if (this.toolTip == null)
     {
       return super.createToolTip();
     }
 
     
     return this.toolTip;
   }
 
 
 
   
   public PsiElement getPsiElement() {
     return this.psiElement;
   }
 
 
   
   public void setPsiElement(PsiElement psiElement) {
     this.psiElement = psiElement;
   }
 }


