 package net.trustx.simpleuml.plugin.configuration;
 
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Font;
 import java.util.StringTokenizer;
 import org.jdom.Element;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ConfigurationUtils
 {
   public static Element getOptionElement(String name, String value) {
     Element element = new Element("option");
     element.setAttribute("name", name);
     element.setAttribute("value", value);
     return element;
   }
 
 
   
   public static Dimension getDimensionFromStringObject(Object value, Dimension defaultValue) {
     if (value == null)
     {
       return defaultValue;
     }
     
     String s = (String)value;
     int index = s.indexOf(',');
     if (index < 2)
     {
       return defaultValue;
     }
     String sW = s.substring(0, index);
     String sH = s.substring(index + 1, s.length());
 
     
     try {
       int w = Integer.parseInt(sW);
       int h = Integer.parseInt(sH);
       return new Dimension(w, h);
     }
     catch (NumberFormatException e) {
       
       return defaultValue;
     } 
   }
 
 
   
   public static long getLong(Object value, long defaultValue, long minValue, long maxValue) {
     if (value == null)
     {
       return defaultValue;
     }
 
     
     try {
       long parsedValue = Long.parseLong(value.toString());
       if (parsedValue < minValue)
       {
         return minValue;
       }
       if (parsedValue > maxValue)
       {
         return maxValue;
       }
 
       
       return parsedValue;
     
     }
     catch (NumberFormatException ex) {
       
       return defaultValue;
     } 
   }
 
 
 
   
   public static int getInt(Object value, int defaultValue) {
     if (value == null)
     {
       return defaultValue;
     }
 
     
     try {
       return Integer.parseInt(value.toString());
     }
     catch (NumberFormatException ex) {
       
       return defaultValue;
     } 
   }
 
 
   
   public static boolean getBooleanValue(Object value, boolean defaultValue) {
     if (value == null)
     {
       return defaultValue;
     }
     return "true".equalsIgnoreCase(value.toString());
   }
 
 
 
 
 
 
 
 
   
   public static int[] getArrayFromString(Object stringObject, int[] defaultValue) {
     if (stringObject == null)
     {
       return defaultValue;
     }
 
     
     try {
       StringTokenizer stok = new StringTokenizer(stringObject.toString(), ",");
       int size = stok.countTokens();
       int[] ints = new int[size];
       for (int i = 0; i < size; i++)
       {
         ints[i] = Integer.parseInt(stok.nextToken());
       }
       
       return ints;
     }
     catch (NumberFormatException e) {
       
       return defaultValue;
     } 
   }
 
 
   
   public static String getStringFromDimension(Dimension dim) {
     return dim.width + "," + dim.height;
   }
 
 
   
   public static String getStringFromArray(int[] ints) {
     StringBuffer sb = new StringBuffer();
     for (int i = 0; i < ints.length; i++) {
       
       int anInt = ints[i];
       sb.append(anInt);
       if (i + 1 < ints.length)
       {
         sb.append(",");
       }
     } 
     
     return sb.toString();
   }
 
 
   
   public static String getStringFromColor(Color color) {
     return "" + color.getRGB();
   }
 
   
   public static Color getColorFromStringObject(Object stringObject, Color defaultValue) {
     int rgb;
     if (stringObject == null)
     {
       return defaultValue;
     }
     
     String s = stringObject.toString();
 
     
     try {
       rgb = Integer.valueOf(s).intValue();
     }
     catch (NumberFormatException e) {
       
       return defaultValue;
     } 
     return new Color(rgb);
   }
 
 
   
   public static String getStringFromFont(Font font) {
     return font.getName() + "," + font.getStyle() + "," + font.getSize();
   }
 
 
 
 
 
 
 
 
   
   public static Font getFontFromStringObject(Object stringObject, Font defaultValue) {
     if (stringObject == null)
     {
       return defaultValue;
     }
 
     
     try {
       String s = stringObject.toString();
       String name = s.substring(0, s.indexOf(","));
       String style = s.substring(s.indexOf(",") + 1, s.lastIndexOf(","));
       String size = s.substring(s.lastIndexOf(",") + 1, s.length());
       return new Font(name, Integer.parseInt(style), Integer.parseInt(size));
     }
     catch (Throwable throwable) {
       
       return defaultValue;
     } 
   }
 }


