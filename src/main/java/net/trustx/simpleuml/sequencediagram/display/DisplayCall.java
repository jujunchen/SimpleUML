 package net.trustx.simpleuml.sequencediagram.display;






















 public class DisplayCall
   extends DisplayLink
 {
   DisplayCall(int callNumber, String name, DisplayObjectInfo from, DisplayObjectInfo to, int seq, String comment, boolean showComment) {
     super(callNumber, name, from, to, seq, comment, showComment);
     from.addCall(this);
   }
 }


