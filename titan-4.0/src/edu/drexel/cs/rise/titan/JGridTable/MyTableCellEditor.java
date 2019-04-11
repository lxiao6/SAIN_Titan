package edu.drexel.cs.rise.titan.JGridTable;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;

public class MyTableCellEditor
       extends DefaultTableCellRenderer {
  public Component getTableCellRendererComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int row,
                                                 int column) {
    //Component c = new UnderlinedJLabel("" + value);
	//  Component c = new JTableCell(value.toString());
    // Only for specific cell
	this.setText(value.toString());
    setFont(new Font("Arial", Font.ITALIC, 12));
    setForeground(Color.blue);
    
    return this;
  }
}



class UnderlinedJLabel extends JLabel
{
   public UnderlinedJLabel() {
   }
  
   public UnderlinedJLabel(Icon image) {
      super(image);
   }
  
   public UnderlinedJLabel(Icon image, int horizontalAlignment) {
      super(image, horizontalAlignment);
   }
  
   public UnderlinedJLabel(String text) {
      super(text);
   }
  
   public UnderlinedJLabel(String text, Icon icon, int horizontalAlignment) {
      super(text, icon, horizontalAlignment);
   }
  
   public UnderlinedJLabel(String text, int horizontalAlignment) {
      super(text, horizontalAlignment);
   }
  
   public void paint(Graphics g) {
      super.paint(g);
      underline(g);
   }
  
   protected void underline(Graphics g) {
      Insets insets = getInsets();
      FontMetrics fm = g.getFontMetrics();
      Rectangle textR = new Rectangle();
      Rectangle viewR = new Rectangle(
                                  insets.left,
                                  insets.top,
                                  getWidth() - (insets.right + insets.left),
                                  getHeight() - (insets.bottom + insets.top));
   
      // compute and return location of the icons origin,
      // the location of the text baseline, and a possibly clipped
      // version of the compound label string.  Locations are computed
      // relative to the viewR rectangle.
      String text = SwingUtilities.layoutCompoundLabel(
                         this,                        // this JLabel
                         fm,                          // current FontMetrics
                         getText(),                   // text
                         getIcon(),                   // icon
                         getVerticalAlignment(),     
                         getHorizontalAlignment(),
                         getVerticalTextPosition(),
                         getHorizontalTextPosition(),
                         viewR,                      
                         new Rectangle(),             // don't care about icon rectangle
                         textR,                       // resulting text locations
                         getText() == null ? 0 : ((Integer)UIManager.get("Button.textIconGap")).intValue());
  
      // draw line
      int textShiftOffset = ((Integer) UIManager.get("Button.textShiftOffset")).intValue();
      g.fillRect(textR.x +
                 textShiftOffset - 4,
                 textR.y + fm.getAscent() + textShiftOffset + 2,
                 textR.width,
                 1);
   }
}