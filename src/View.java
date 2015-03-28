import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Created by ahmed on 3/28/15.
 */
public class View extends JFrame{

    private JButton[][] cells;

    final int widthOfButton = 200;
    final int heightOfButton = 50;

    private JPanel game;

    private JButton resetButton;
    private TextField turn;

    public View(int width, int height){
        //gets rid of java's default positioning system so we have more control and can manually position everything using absolute positioning
        this.getContentPane().setLayout(null);
        this.setLayout(null);

        game = new JPanel();
        game.setSize(width, height);
        game.setLocation(0,0);
        game.setLayout(null);

        cells = new JButton[3][3];
        turn = new TextField(" input the players turn here");
        turn.setLocation(widthOfButton, heightOfButton * 4);
        turn.setSize(widthOfButton, heightOfButton);
        game.add(turn);

        Listener listener = new Listener();

        for(int i =0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                JButton currentButton = createButton("", i * widthOfButton, j * heightOfButton, game, "");
                currentButton.addActionListener(listener);

                cells[i][j] = currentButton;
                game.add(cells[i][j]);
            }
        }

        this.add(game);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//sets the default close operation
        this.setSize(width, height);//sets the size of the screen
    }

    // this class creates a button and positions it at the location (x,y) with the width and height of the input. adds the button to the parent and sets its image to the filename equal to name in the images folder
    public JButton createButton(String name, int x, int y, JPanel parent, String pressedName){ return createButton(name, x,y, widthOfButton, heightOfButton, parent, pressedName); }
    public JButton createButton(String name, int x, int y, int width, int height, JPanel parent, String pressedName) {
        JButton newButton = new JButton(new ImageIcon(name)); // creates the button, with the image name that is given
        newButton.setPressedIcon(new ImageIcon(pressedName)); // makes a new button

        //if the image name that you have given does not exist then use the default image,
        // if the image given exists then remove the default button graphics
        if(newButton.getIcon().toString().length() > 2){
            //removes the default button graphics
            newButton.setOpaque(false);
            newButton.setContentAreaFilled(false);
            newButton.setBorderPainted(false);
            newButton.setFocusPainted(false);
        }else{
            //inform the user that the image can not be found
            System.out.println("go an empty location for image picture, using the default button");
            newButton.setText("place button label here");
        }

        // this sets the buttons size and location on the screen
        newButton.setSize(width,height);
        newButton.setLocation(x, y);

        parent.add(newButton);//this adds the button to the parent
        return newButton;//returns the newly created button
    }

    //creates a class called Listener that handles listener events such as actionPerformed, and handles all the mouse events which is called from the user input
    class Listener implements ActionListener, MouseInputListener {
        public Listener(){}//empty constructor

        //Purpose: when the user presses the main menu button it will call this function, and it will either go to the game stage or the custom game depending on what is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("aasdsad");
        }//end function

        //Purpose: this function will be called when the user presses a button, it will be responsible for handling the outcome of the button press.
        @Override
        public void mouseClicked(MouseEvent e) {}
        //all these functions were mandatory because if we do not have them then this class would be implementing an interface it does not have functions to
        @Override public void mouseReleased(MouseEvent e) {}//unused
        @Override public void mousePressed(MouseEvent e) {} //unused
        @Override public void mouseEntered(MouseEvent e) {}//unused
        @Override public void mouseExited(MouseEvent e) {}//unused
        @Override public void mouseDragged(MouseEvent e) {}//unused
        @Override public void mouseMoved(MouseEvent e) {}//unused
    }//end calculate listener class


    public static void main(String[] args){

    }

}
