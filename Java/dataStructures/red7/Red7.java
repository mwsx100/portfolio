package red7;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.beans.*; //property change stuff
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class Red7 extends JPanel implements MouseListener{
    private ArrayList<Red7Player> players;
    private ArrayList<Red7Card> deck;
    String currentRule;
    int clickedHand;
    boolean clickedTab;
    boolean played, drawRuleEnabled;
    
    public Red7(JFrame frame) {
        super(new GridLayout(1, 1));
        
        //building the deck
        String[] colors = {"RED","ORANGE","YELLOW","GREEN","BLUE","INDIGO","VIOLET"};
        
        ArrayList<Red7Card> deck = new ArrayList();
        for(int i=0; i<7; i++)
            for(int j=1; j<=7; j++)
                deck.add(new Red7Card(colors[i],j));
        
        Collections.shuffle(deck);
        
        
        //Add content to the window.
        
        NameDialog customDialog = new NameDialog(frame);
        customDialog.pack();
        customDialog.setLocationRelativeTo(frame);
        customDialog.setVisible(true);
        
        String[] s = customDialog.getValidatedText();//{"Player 1","Player 2","Player 3","Player 4"};
        drawRuleEnabled = customDialog.isDrawRule();
        
        ArrayList<Red7Player> players = new ArrayList();
        for(int i=0; i<s.length; i++)
            if(s[i]!=null && !s[i].isEmpty()){
                Red7Player player = new Red7Player(s[i]);
                for(int j=0; j<7; j++)
                    player.dealCard(deck.remove(0));
                player.dealTableau(deck.remove(0));
                players.add(player);
            }
        if(players.size()<2) System.exit(1);
        
        
        //Setting player order
        Red7Player currentPlayer = players.remove(0);
        while(!isWinning("RED",currentPlayer,players)){
            players.add(currentPlayer);
            currentPlayer = players.remove(0);
        }
        //current player is the winning at the beginning, one more cycle to be next player
        players.add(currentPlayer);
        
        this.players = players;
        this.deck = deck;
        
        this.setPreferredSize(new Dimension(900,650));
        
        this.addMouseListener(this);
        clickedHand=-1;
        clickedTab=played=false;
        currentRule="RED";
    }
    
    public void paint(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(0,0,getWidth(),getHeight());
        Font myFont = new Font("SansSerif",Font.BOLD,25);
        for(int i=0; i<players.size(); i++){
            players.get(i).drawTableau(g,20,getHeight()-(i+1)*150,400,100);
            g.setColor(Color.BLACK);
            g.drawRect(10,getHeight()-(i+1)*150-30,420,140);
            g.setFont(myFont);
            g.drawString(players.get(i).getName()+"'s Tableau:",20,getHeight()-(i+1)*150-5);
        }
        g.drawRect(440,getHeight()-180,420,140);
        players.get(0).drawHand(g,450,getHeight()-140,400,100);
        g.setColor(Color.BLACK);
        g.setFont(myFont);
        g.drawString(players.get(0).getName()+"'s Hand:",450,getHeight()-155);
        
        
        g.drawString("Current Rule:",440,340);
        setColor(g, currentRule);
        g.fillRect(440,360,400,70);
        
        myFont = new Font("SansSerif",Font.BOLD,15);
        g.setFont(myFont);
        setColor(g, "R");
        g.fillRect(440,10,400,40);
        setColor(g, "W");
        g.drawString("Red-Highest Card Wins",450,30);
        setColor(g, "O");
        g.fillRect(440,50,400,40);
        setColor(g, "W");
        g.drawString("Orange-Most of One Number Wins",450,70);
        setColor(g, "Y");
        g.fillRect(440,90,400,40);
        setColor(g, "W");
        g.drawString("Yellow-Most of One Color Wins",450,110);
        setColor(g, "G");
        g.fillRect(440,130,400,40);
        setColor(g, "W");
        g.drawString("Green-Most Even Cards Wins",450,150);
        setColor(g, "B");
        g.fillRect(440,170,400,40);
        setColor(g, "W");
        g.drawString("Blue-Most Different Colors Wins",450,190);
        setColor(g, "I");
        g.fillRect(440,210,400,40);
        setColor(g, "W");
        g.drawString("Indigo-Most Cards In A Row Wins",450,230);
        setColor(g, "V");
        g.fillRect(440,250,400,40);
        setColor(g, "W");
        g.drawString("Violet-Most Cards Below 4 Wins",450,270);
    }
    
    public boolean checkInRule(int x, int y){
        if(x>440 && x<840 && y>10 && y<430)
            return true;
        return false;
    }
    public boolean checkInTab(int x, int y){
        if(x>10 && x<430 && y>getHeight()-180 && y<getHeight()-40)
            return true;
        return false;
    }
    public void checkIfEmpty(){
        Red7Player currentPlayer = players.get(0);
        while(currentPlayer.isEmpty() && players.size()!=1){
            JOptionPane.showMessageDialog(this, currentPlayer.getName()+" has no cards to play...","Alert",JOptionPane.ERROR_MESSAGE);
            players.remove(0);
            if(players.size()==1){
                JOptionPane.showMessageDialog(this, players.get(0).getName()+" WON!","Huzzah!",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
   	public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {
        int nhand = players.get(0).checkClickedHand(e.getX(),e.getY());
        boolean inTab = checkInTab(e.getX(), e.getY());
        boolean inRule = checkInRule(e.getX(), e.getY());
        if(nhand==clickedHand){
            players.get(0).clearHandClick();
            nhand=-1;
        }
        
        if(clickedHand!=-1 && inTab && !played){
            //play a card
            players.get(0).playCard(clickedHand);
            
            played=true;
        }
        if(clickedHand!=-1 && inRule){
            //discard a card, change player
            Red7Player currentPlayer = players.remove(0);
            Red7Card rc = currentPlayer.discardCard(clickedHand);
            if(drawRuleEnabled){
                int number = rc.getNumber(); //number on discarded card
                if(currentPlayer.getTableauSize()<number && deck.size()>0){
                   currentPlayer.dealCard(deck.remove(0));
                }
            }
            if(!Red7.isWinning(rc.getColor(),currentPlayer,players)){
                JOptionPane.showMessageDialog(this, currentPlayer.getName()+" is not winning with the discarded rule","alert",JOptionPane.ERROR_MESSAGE);
            } else {
                players.add(currentPlayer);
                currentRule = rc.getColor();
            }
            checkIfEmpty();
            played = inTab = false;
            nhand=-1;
        }
        if(played && inRule && clickedHand==-1){ // not discarding, keeping rule
            Red7Player currentPlayer = players.remove(0);
            if(!Red7.isWinning(currentRule,currentPlayer,players)){
                JOptionPane.showMessageDialog(this, currentPlayer.getName()+" is not winning with the current rule","alert",JOptionPane.ERROR_MESSAGE);
            } else
                players.add(currentPlayer);
            checkIfEmpty();
            played = inTab = false;
            nhand=-1;
        }
        clickedHand = nhand;
        clickedTab = inTab;
        
        if(players.size()==1){
            JOptionPane.showMessageDialog(this, players.get(0).getName()+" WON!","Huzzah!",JOptionPane.ERROR_MESSAGE);
            
        }
        
        repaint();
   	}

    /**************************************************************************************************
     
     Red7Player Inner Class
     
     **************************************************************************************************/
    
    class Red7Player{
        private Red7CardSet hand;
        private Red7CardSet tableau;
        private String name;
        private int[] handDim;
        private int[] tabDim;
        private int clickedHand, clickedTab;
        
        public Red7Player(String name){
            this.name=name;
            hand = new Red7CardSet();
            tableau = new Red7CardSet();
            handDim=new int[4];
            tabDim=new int[4];
            clickedTab=clickedHand=-1;
        }
        public String getName(){return name;}
        
        public int getHandSize(){
            return hand.getSize();
        }
        public int getTableauSize(){
            return tableau.getSize();
        }
        public void dealCard(Red7Card card){
            hand.add(card);
        }
        
        public Red7Card getCard(char letter){
            return hand.get((int)(letter-'A'));
        }
        public void dealTableau(Red7Card card){
            tableau.add(card);
        }
        
        public void playCard(int ind){
            Red7Card cardToPlay = hand.get(ind);
            if(cardToPlay==null) return;
            hand.remove(cardToPlay);
            tableau.add(cardToPlay);
        }
        public void playCard(Red7Card cardToPlay){
            if(cardToPlay==null) return;
            hand.remove(cardToPlay);
            tableau.add(cardToPlay);
        }
        
        public Red7Card discardCard(int ind){
            Red7Card cardToPlay = hand.get(ind);
            if(cardToPlay==null) return null;
            hand.remove(cardToPlay);
            return cardToPlay;
        }
        public Red7Card discardCard(Red7Card cardToPlay){
            if(cardToPlay==null) return null;
            hand.remove(cardToPlay);
            return cardToPlay;
        }
        public boolean isWinning(String color,Red7Player other){
            return tableau.compareRule(other.tableau, color)>0;
        }
        
        public boolean isEmpty(){
            return hand.getSize()==0;
        }
        public void clearHandClick(){
            clickedHand=-1;
        }
        public int checkClickedHand(int x, int y){
            if(y-handDim[1] < 0 || y-handDim[1] > handDim[3]) clickedHand=-1;
            else{
                clickedHand = (x-handDim[0])/handDim[2];
                if(clickedHand<0 || clickedHand>=hand.getSize()) clickedHand=-1;
            }
            return clickedHand;
        }
        public int checkClickedTab(int x, int y){
            if(y-tabDim[1] < 0 || y-tabDim[1] > tabDim[3]) clickedTab=-1;
            else{
                clickedTab = (x-tabDim[0])/tabDim[2];
                if(clickedTab<0 || clickedTab>=tableau.getSize()) clickedTab=-1;
            }
            return clickedTab;
        }
        public void drawHand(Graphics g, int x, int y, int width, int height){
            drawSet(g,x,y,width,height,hand);
        }
        public void drawTableau(Graphics g, int x, int y, int width, int height){
            drawSet(g,x,y,width,height,tableau);
        }
        public void drawSet(Graphics g, int x, int sy, int width, int height, Red7CardSet set){
            boolean isHand = set==hand;
            String shand = set.toString();
            if(shand.isEmpty()) return;
            String[] cards = shand.split(",");
            
            int cwidth = width/cards.length;
            if(cards.length==0) cwidth=100;//prevents division by 0
            
            int twidth = (int)(0.714*height);
            if(twidth<cwidth) cwidth=twidth;
            int cheight = (int)(cwidth*1.4);
            
            if(isHand){
                handDim[0] = x;
                handDim[1] = sy;
                handDim[2] = cwidth;
                handDim[3] = cheight;
            }else {
                tabDim[0] = x;
                tabDim[1] = sy;
                tabDim[2] = cwidth;
                tabDim[3] = cheight;
            }
            
            Font myFont = new Font("SansSerif",Font.BOLD,cheight/8);
            Font myFont2 = new Font("SansSerif",Font.BOLD,cheight/3);
            g.setFont(myFont);
            FontMetrics fm = g.getFontMetrics();
            g.setFont(myFont2);
            FontMetrics fm2 = g.getFontMetrics();
            Graphics2D graphics2 = (Graphics2D) g;
            int outerBorder = cwidth/16;
            int innerBorder = cwidth/11;
            
            for(int i=0; i<cards.length; i++){
                String ccolor = cards[i].substring(1).trim().toUpperCase();
                char num = cards[i].charAt(0);
                twidth = fm2.stringWidth(num+"");
                int clrWidth = fm.stringWidth(ccolor);
                int y = sy;
                
                if(isHand){
                    if(clickedHand==i)
                        y = sy-10;
                } else {
                    if(clickedTab==i)
                        y = sy-10;
                }
                
                setColor(g,ccolor);
                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(x+cwidth*(i), y, cwidth, cheight, 10, 10);
                graphics2.fill(roundedRectangle);
                setColor(g,"W");
                graphics2.setStroke(new BasicStroke(3));
                
                //white rectangles
                roundedRectangle = new RoundRectangle2D.Float(x+cwidth*(i)+outerBorder, y+outerBorder, cwidth-2*outerBorder, cheight-2*outerBorder, 10, 10);
                graphics2.draw(roundedRectangle);
                
                roundedRectangle = new RoundRectangle2D.Float(x+cwidth*(i)+innerBorder, y+innerBorder, twidth+10, fm2.getHeight()+outerBorder, 10, 10);
                graphics2.fill(roundedRectangle);
                
                roundedRectangle = new RoundRectangle2D.Float(x+cwidth*(i+1)-innerBorder-clrWidth-10, y+cheight-innerBorder-fm.getHeight()-outerBorder, clrWidth+10, fm.getHeight()+outerBorder, 10, 10);
                graphics2.fill(roundedRectangle);
                
                graphics2.setStroke(new BasicStroke(2));
                
                g.setColor(Color.BLACK);
                g.setFont(myFont2);
                g.drawString(num+"",x+cwidth*i+innerBorder+5,y+fm2.getHeight());
                g.setFont(myFont);
                g.drawString(ccolor,x+cwidth*(i+1)-innerBorder-clrWidth-5,y+cheight-innerBorder-outerBorder);
                roundedRectangle = new RoundRectangle2D.Float(x+cwidth*(i), y, cwidth, cheight, 10, 10);
                graphics2.draw(roundedRectangle);
                graphics2.setStroke(new BasicStroke(1));
            }
        }
        
    }
    
    
    /**************************************************************************************************
     
     Custom Dialog Inner Class
     
     **************************************************************************************************/
    
    class NameDialog extends JDialog implements ActionListener, PropertyChangeListener {
        private String typedText1,typedText2,typedText3,typedText4;
        private JTextField textField1,textField2,textField3,textField4;
        JCheckBox checkButton;
        
        private String magicWord;
        private JOptionPane optionPane;
        
        private String btnString1 = "Enter";
        private String btnString2 = "Cancel";
        
        private boolean drawRule;
        
        public boolean isDrawRule(){
            return drawRule;
        }
        
        public String[] getValidatedText() {
            String[] playerNames  = {typedText1,typedText2,typedText3,typedText4};
            return playerNames;
        }
        
        public NameDialog(Frame aFrame) {
            super(aFrame, true);
            drawRule=false;
            
            setTitle("Player Names");
            
            textField1 = new JTextField(10);
            textField2 = new JTextField(10);
            textField3 = new JTextField(10);
            textField4 = new JTextField(10);
            
            //Create an array of the text and components to be displayed.
            String msgString1 = "Enter each player's name:";
            String msgString2 = "(Leave blank if < 4)";
            checkButton = new JCheckBox("Enable Draw when Discard # > amount in Tableau");
            checkButton.setSelected(false);
            Object[] array = {msgString1, msgString2, textField1, textField2, textField3, textField4,checkButton};
            
            Object[] options = {btnString1, btnString2};
            
            optionPane = new JOptionPane(array,
                                         JOptionPane.QUESTION_MESSAGE,
                                         JOptionPane.YES_NO_OPTION,
                                         null,
                                         options,
                                         options[0]);
            setContentPane(optionPane);
            
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent ce) {
                    textField1.requestFocusInWindow();
                }
            });
            
            textField1.addActionListener(this);
            textField2.addActionListener(this);
            textField3.addActionListener(this);
            textField4.addActionListener(this);
            
            optionPane.addPropertyChangeListener(this);
        }
        
        public void actionPerformed(ActionEvent e) {
            optionPane.setValue(btnString1);
        }
        
        public void propertyChange(PropertyChangeEvent e) {
            String prop = e.getPropertyName();
            
            if (isVisible() && (e.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
                    JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
                    Object value = optionPane.getValue();
                    if (value == JOptionPane.UNINITIALIZED_VALUE) return;
                    optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
                    if (btnString1.equals(value)) { //Enter Pressed
                        typedText1 = textField1.getText();
                        typedText2 = textField2.getText();
                        typedText3 = textField3.getText();
                        typedText4 = textField4.getText();
                        drawRule = checkButton.isSelected();
                        
                        clearAndHide();
                    } else { //user closed dialog or clicked cancel
                        typedText1 = "";
                        typedText2 = "";
                        typedText3 = "";
                        typedText4 = "";
                        drawRule = false;
                        clearAndHide();
                    }
                }
        }
        
        public void clearAndHide() {
            textField1.setText(null);
            textField2.setText(null);
            textField3.setText(null);
            textField4.setText(null);
            setVisible(false);
        }
    }
    
    /**************************************************************************************************
     
     Static Helper & Frame Construction Methods
     
     **************************************************************************************************/
    public static void setColor(Graphics g, String color){
        switch(color.toUpperCase()){
            case("R"):
            case "RED": g.setColor(new Color(220,43,46)); break;
            case("O"):
            case "ORANGE": g.setColor(new Color(230,122,48)); break;
            case("Y"):
            case "YELLOW": g.setColor(new Color(238,201,71)); break;
            case("G"):
            case "GREEN": g.setColor(new Color(63,188,102)); break;
            case("B"):
            case "BLUE": g.setColor(new Color(62,168,252)); break;
            case("I"):
            case "INDIGO": g.setColor(new Color(25,71,218)); break;
            case("V"):
            case "VIOLET": g.setColor(new Color(73,0,164)); break;
            case("W"):
            case "WHITE": g.setColor(Color.WHITE); break;
            default:
                g.setColor(Color.BLACK);
        }
    }
    
    public static boolean isWinning(String color, Red7Player current, ArrayList<Red7Player> players){
        boolean isWinning=true;
        
        for(int i=0; i<players.size(); i++){
            Red7Player next = players.remove(0);
            if(!current.isWinning(color,next)) isWinning=false;
            players.add(next);
        }
        return isWinning;
    }
    
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Red 7");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.add(new Red7(frame), BorderLayout.CENTER);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}