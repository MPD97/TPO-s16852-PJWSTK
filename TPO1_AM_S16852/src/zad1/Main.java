/**
 *
 *  @author Ambroziak Mateusz S16852

 *
 */

package zad1;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args) {
    String dirName = System.getProperty("user.home")+"/TPO1dir";
    String resultFileName = "TPO1res.txt";
    Futil.processDir(dirName, resultFileName);

  }
}
