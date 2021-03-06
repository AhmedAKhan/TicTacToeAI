import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

    Turn currentTurn;
    enum Turn{
        GameOver,
        AI,
        Player
    }

    public View(int width, int height){
        //gets rid of java's default positioning system so we have more control and can manually position everything using absolute positioning
        this.getContentPane().setLayout(null);
        this.setLayout(null);

        game = new JPanel();
        game.setSize(width, height);
        game.setLocation(0, 0);
        game.setLayout(null);

        cells = new JButton[3][3];

        turn = new TextField(" input the players turn here");
        turn.setLocation(this.getWidth()/2, heightOfButton * 4);
        turn.setSize(widthOfButton, heightOfButton);
        game.add(turn);

        resetButton = createButton("", widthOfButton + 40, heightOfButton * 4, widthOfButton, heightOfButton, game, "");
        resetButton.setText("Reset");
        game.add(resetButton);

        currentTurn = (Math.random() < 0.5)? Turn.AI:Turn.Player;
        turn.setText((currentTurn==Turn.AI)? "AI->X":"Player->O");
        Listener listener = new Listener();

        resetButton.addActionListener(listener);
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
//            newButton.setText("place button label here");
            newButton.setText("");
        }

        // this sets the buttons size and location on the screen
        newButton.setSize(width,height);
        newButton.setLocation(x, y);

        parent.add(newButton);//this adds the button to the parent
        return newButton;//returns the newly created button
    }

    private void resetBoard(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(cells[i][j] != null)
                    cells[i][j].setText("");
            }
        }
        currentTurn = (Math.random() < 0.5)? Turn.AI:Turn.Player;
        turn.setText((currentTurn==Turn.AI)? "X":"O");
    }
    private void doTurn(JButton buttonPressed){
        if(currentTurn == Turn.AI){
            doAITurn(buttonPressed);
            return;
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(cells[i][j] == buttonPressed){
                    if(!buttonPressed.getText().equals("")) return;
                    userPressed(cells[i][j]);
                }
            }
        }
    }

    private void doAITurn(JButton buttonPressed){
        int highestScore = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(cells[i][j] == buttonPressed){
                    if(!buttonPressed.getText().equals("")) continue;

                    int currentScore = findScoreAtTile(new Point(i,j));
                    cells[i][j].setText((currentTurn == Turn.AI)? "X":"O");
                    currentScore = currentScore - getHighestScore();
                    cells[i][j].setText("");//put cell back

                    if(highestScore < currentScore) highestScore = currentScore;
                }
            }
        }
    }
    private int getHighestScore(){
        int highestScore = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(!cells[i][j].getText().equals("")) continue;
                int currentScore = findScoreAtTile(new Point(i,j));
                if(highestScore < currentScore) highestScore = currentScore;
            }
        }
        return highestScore;
    }
    private int findScoreAtTile(Point point){
        int score = 0;
        for(Point vector : new Point[]{new Point(0,1), new Point(1,0), new Point(1,1), new Point(1,-1)}){
            int scoreOfCurrentDirection = findScoreForDirection(vector.x, vector.y, point);
            if(scoreOfCurrentDirection > score){
                score = scoreOfCurrentDirection;
            }
        }
        return score;
    }
    private int findScoreForDirection(int colIncrementor, int rowIncrementor, Point point){

        int currentLength = 0; // it starts of at one because if you put a piece down here it will have a sequence of itself

        String firstPointText = cells[point.x][ point.y].getText();
        for (int discFromPoint = 1; true; discFromPoint++) {
            Point currentPoint = new Point(point.x + colIncrementor * discFromPoint, point.y + rowIncrementor * discFromPoint);//get position of points
            if (!isInBounds(currentPoint.x, currentPoint.y)) break;
            if(firstPointText.equals(cells[currentPoint.x][currentPoint.y])){
                currentLength++;
            }
        }

        firstPointText = cells[point.x][ point.y].getText();
        for (int discFromPoint = 1; true; discFromPoint++) {
            Point currentPoint = new Point(point.x - colIncrementor * discFromPoint, point.y - rowIncrementor * discFromPoint);//get position of points
            if (!isInBounds(currentPoint.x, currentPoint.y)) break;
            if(firstPointText.equals(cells[currentPoint.x][currentPoint.y])){
                currentLength++;
            }
        }


        if(currentLength <= 1) return currentLength;
        return (int)Math.pow(2, currentLength);
    }
    private void userPressed(JButton buttonPressed){
        //this is the button that was pressed
        if(currentTurn == Turn.AI) {
            buttonPressed.setText("X");
            currentTurn = Turn.Player;
        } else if(currentTurn == Turn.Player){
            buttonPressed.setText("O");
            currentTurn = Turn.AI;

        }
        turn.setText((currentTurn==Turn.AI)? "X":"O");
    }
    public boolean isInBounds(int x, int y){
        return !(x < 0 || x > 2 || y < 0 || y > 2);
    }

    //creates a class called Listener that handles listener events such as actionPerformed, and handles all the mouse events which is called from the user input
    class Listener implements ActionListener, MouseInputListener {
        public Listener(){}//empty constructor

        //Purpose: when the user presses the main menu button it will call this function, and it will either go to the game stage or the custom game depending on what is clicked
        @Override public void actionPerformed(ActionEvent e) {
            if(e.getSource() == resetButton){
                resetBoard();
                return;
            }

            if(checkIfWon()) return;
            doTurn((JButton)e.getSource());
            if(checkIfWon()) JOptionPane.showMessageDialog(game, "The " + currentTurn + " has won");

        }//end function
        public boolean checkIfWon(){
            for(int i = 0; i < 3; i++){
                for(int j = 0; j<3; j++){
                    for(Point vector : new Point[]{ new Point(0,1), new Point(1,0), new Point(1,1), new Point(-1,1) }) {
                        if(checkWinInDirection(vector.x, vector.y, new Point(i,j))) return true;
                    }
                }
            }
            return false;
        }

        private boolean checkWinInDirection(int rowIncrementor, int colIncrementor, Point point){
            if(cells[point.x][point.y].getText().equals("")) return false;
            String firstPoint = cells[point.x][point.y].getText();//get position of points
            int discFromPoint;
            for (discFromPoint = 1; true; discFromPoint++) {
                Point currentPoint = new Point(point.x + colIncrementor * discFromPoint, point.y + rowIncrementor * discFromPoint);//get position of points
                if (!isInBounds(currentPoint.x, currentPoint.y)) break;
                if(!firstPoint.equals(cells[currentPoint.x][currentPoint.y].getText())) break;
            }
            return (discFromPoint ==3);
        }
        private Turn convertStringToType(String x){
            if(x.equals("X")) return Turn.AI;
            if(x.equals("O")) return Turn.Player;
            return Turn.GameOver;
        }

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
