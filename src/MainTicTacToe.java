import javax.swing.*;

/**
 * Created by ahmed on 3/28/15.
 */
public class MainTicTacToe {

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                View theView = new View(600,600);
                theView.setVisible(true);
                theView.repaint();
            }
        });
    }

}
