 package net.trustx.simpleuml.components;

 import java.awt.BorderLayout;
 import java.awt.Dimension;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.Point;
 import java.awt.Rectangle;
 import java.awt.RenderingHints;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseMotionAdapter;
 import java.awt.geom.AffineTransform;
 import java.awt.image.BufferedImage;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;


 public class DiagramPreviewPanel
   extends JPanel
   implements ChangeListener
 {
   private Previewable previewable;
   private BufferedImage bufferedImage;
   private ImagePanel imagePanel;
   private boolean antialiasing;
   private boolean textAntialiasing;
   private double scaleFactor;
   private boolean fractionalMetrics;
   private int width;
   private int height;
   private JScrollPane scrollPane;
   private int minX;
   private int minY;
   private int maxX;
   private int maxY;
   private int previousWidth;
   private int previousHeight;
   private boolean cropImage;
   private boolean drawViewport;

   public DiagramPreviewPanel() {
     this((Previewable)null);
   }



   public DiagramPreviewPanel(Previewable previewable) {
     this.previewable = previewable;
     this.antialiasing = false;
     this.textAntialiasing = false;
     this.scaleFactor = 1.0D;
     this.fractionalMetrics = false;
     this.cropImage = true;


     this.bufferedImage = new BufferedImage(1, 1, 9);

     this.scaleFactor = 0.5D;
     setLayout(new BorderLayout());
     this.imagePanel = new ImagePanel();

     this.scrollPane = new JScrollPane(this.imagePanel);
     this.scrollPane.setMinimumSize(new Dimension(300, 300));
     this.scrollPane.setPreferredSize(new Dimension(300, 300));
     this.scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
     this.scrollPane.getVerticalScrollBar().setUnitIncrement(20);
     add(this.scrollPane, "Center");

     updateImage();
   }



   private void setGraphicsProperties(Graphics2D g2d) {
     AffineTransform at = AffineTransform.getScaleInstance(this.scaleFactor, this.scaleFactor);
     at.concatenate(AffineTransform.getTranslateInstance(-this.minX, -this.minY));
     g2d.setTransform(at);
     g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antialiasing ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, this.textAntialiasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
   }



   private void updateImage() {
     if (this.previewable == null || !this.previewable.isShowingOnScreen()) {
       return;
     }


     if (this.cropImage) {

       this.minX = this.previewable.getMinimumComponentX();
       this.minY = this.previewable.getMinimumComponentY();
       this.maxX = this.previewable.getMaximumComponentX();
       this.maxY = this.previewable.getMaximumComponentY();
     }
     else {

       this.minX = 0;
       this.minY = 0;
       this.maxX = this.previewable.getRealWidth();
       this.maxY = this.previewable.getRealHeight();
     }

     int origWidth = this.maxX - this.minX;
     int origHeight = this.maxY - this.minY;
     if (origWidth < 0)
     {
       origWidth = 1;
     }
     if (origHeight < 0)
     {
       origHeight = 1;
     }
     this.width = (int)Math.round(origWidth * this.scaleFactor);
     this.height = (int)Math.round(origHeight * this.scaleFactor);

     if (this.previousWidth != this.width || this.previousHeight != this.height) {

       try {

         if (this.width == 0 || this.height == 0) {
           return;
         }

         this.bufferedImage = new BufferedImage(this.width, this.height, 9);
         this.previousWidth = this.width;
         this.previousHeight = this.height;
       }
       catch (Throwable e) {

         e.printStackTrace();
         this.width = 1;
         this.height = 1;
         this.bufferedImage = new BufferedImage(this.width, this.height, 9);
         this.previousWidth = this.width;
         this.previousHeight = this.height;
         return;
       }
     }
     Graphics2D graphics2D = this.bufferedImage.createGraphics();
     setGraphicsProperties(graphics2D);
     this.previewable.printAllContents(graphics2D);

     this.imagePanel.revalidate();
     this.imagePanel.repaint();
     this.imagePanel.doLayout();

     this.scrollPane.revalidate();
     this.scrollPane.doLayout();
   }



   public BufferedImage getBufferedImage() {
     return this.bufferedImage;
   }



   public void setAntialiasing(boolean antialiasing) {
     this.antialiasing = antialiasing;
     updateImage();
   }



   public void setScaleFactor(double scaleFactor) {
     this.scaleFactor = scaleFactor;
     updateImage();
   }



   public void setDrawViewport(boolean drawViewport) {
     this.drawViewport = drawViewport;
     this.imagePanel.initListeners();
   }



   public void setScaleFactorToFit() {
     if (!isVisible()) {
       return;
     }


     if (this.previewable == null) {
       return;
     }


     if (!this.previewable.isReady()) {
       return;
     }


     if (this.cropImage) {

       this.minX = this.previewable.getMinimumComponentX();
       this.minY = this.previewable.getMinimumComponentY();
       this.maxX = this.previewable.getMaximumComponentX();
       this.maxY = this.previewable.getMaximumComponentY();
     }
     else {

       this.minX = 0;
       this.minY = 0;
       this.maxX = this.previewable.getRealWidth();
       this.maxY = this.previewable.getRealHeight();
     }

     double origWidth = (this.maxX - this.minX);
     double origHeight = (this.maxY - this.minY);

     double scaleWidth = (getWidth() - 2) / origWidth;
     double scaleHeight = (getHeight() - 1) / origHeight;

     if (scaleWidth < 1.0E-4D)
     {
       scaleWidth = 0.01D;
     }
     if (scaleHeight < 1.0E-4D)
     {
       scaleHeight = 0.01D;
     }

     this.scrollPane.setHorizontalScrollBarPolicy(31);
     this.scrollPane.setVerticalScrollBarPolicy(21);
     setScaleFactor(Math.min(scaleWidth, scaleHeight));
   }



   public void setTextAntialiasing(boolean textAntialiasing) {
     this.textAntialiasing = textAntialiasing;
     updateImage();
   }



   public void setCropImage(boolean cropImage) {
     this.cropImage = cropImage;
     updateImage();
   }



   public Previewable getPreviewable() {
     return this.previewable;
   }



   public void setPreviewable(Previewable previewable) {
     if (this.previewable != null)
     {
       this.previewable.getJViewport().removeChangeListener(this);
     }
     this.previewable = previewable;
     if (this.previewable != null)
     {
       this.previewable.getJViewport().addChangeListener(this);
     }
   }



   public void stateChanged(ChangeEvent e) {
     this.imagePanel.repaint();
   }



   public JScrollPane getScrollPane() {
     return this.scrollPane;
   }








   class ImagePanel
     extends JPanel
   {
     public void initListeners() {
       if (DiagramPreviewPanel.this.drawViewport && DiagramPreviewPanel.this.previewable != null) {

         addMouseListener(new MouseAdapter()
             {
               public void mouseClicked(MouseEvent e)
               {
                 ImagePanel.this.moveViewport(e);
               }



               public void mousePressed(MouseEvent e) {
                 ImagePanel.this.moveViewport(e);
               }
             });

         addMouseMotionListener(new MouseMotionAdapter()
             {
               public void mouseDragged(MouseEvent e)
               {
                 ImagePanel.this.moveViewport(e);
               }
             });
       }
     }



     private void moveViewport(MouseEvent e) {
       int newX = (int)Math.round(e.getX() / DiagramPreviewPanel.this.scaleFactor);
       int newY = (int)Math.round(e.getY() / DiagramPreviewPanel.this.scaleFactor);

       int eW = (DiagramPreviewPanel.this.previewable.getJViewport().getExtentSize()).width;
       int eH = (DiagramPreviewPanel.this.previewable.getJViewport().getExtentSize()).height;
       newX -= eW / 2;
       newY -= eH / 2;

       newX = (newX < 0) ? 0 : newX;
       newY = (newY < 0) ? 0 : newY;

       newX = (newX > DiagramPreviewPanel.this.previewable.getRealWidth() - eW) ? (DiagramPreviewPanel.this.previewable.getRealWidth() - eW) : newX;
       newY = (newY > DiagramPreviewPanel.this.previewable.getRealHeight() - eH) ? (DiagramPreviewPanel.this.previewable.getRealHeight() - eH) : newY;

       Point newPos = new Point(newX, newY);
       DiagramPreviewPanel.this.previewable.getJViewport().setViewPosition(newPos);
     }



     public Dimension getMinimumSize() {
       return new Dimension(DiagramPreviewPanel.this.width, DiagramPreviewPanel.this.height);
     }



     public Dimension getMaximumSize() {
       return new Dimension(DiagramPreviewPanel.this.width, DiagramPreviewPanel.this.height);
     }



     public Dimension getPreferredSize() {
       return new Dimension(DiagramPreviewPanel.this.width, DiagramPreviewPanel.this.height);
     }



     protected void paintComponent(Graphics g) {
       super.paintComponent(g);
       g.drawImage(DiagramPreviewPanel.this.bufferedImage, 0, 0, this);
       if (DiagramPreviewPanel.this.drawViewport && DiagramPreviewPanel.this.previewable != null) {

         Rectangle vRect = DiagramPreviewPanel.this.previewable.getJViewport().getViewRect();
         g.drawRect((int)Math.round(vRect.x * DiagramPreviewPanel.this.scaleFactor), (int)Math.round(vRect.y * DiagramPreviewPanel.this.scaleFactor), (int)Math.round(vRect.width * DiagramPreviewPanel.this.scaleFactor), (int)Math.round(vRect.height * DiagramPreviewPanel.this.scaleFactor));
       }
     }
   }
 }


