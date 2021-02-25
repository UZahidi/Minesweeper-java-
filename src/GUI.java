import javax.swing. *;
import java.util. *;
import java.awt.*;  
import java.awt.event.*;


public class GUI extends JFrame 
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    Date startDate = new Date();

    int space = 5;
    public int mousex = -100;
    public int mousey = -100;

    public int emojix = 605;
    public int emojiy = 5;
    public int emoji_center_x = emojix + 35 + 7;
    public int emoji_center_y = emojiy + 35 + 30;

    public int flagx = 0;
    public int flagy = 0;



    boolean gameover = false;
    boolean gamewon = false;

    public int timex = 1100;
    public int timey = 5;
    public int sec = 0;
    public int sec1 = 0;    //Total time

    

    
    Queue <Integer> queue_i = new LinkedList<>();
    Queue <Integer> queue_j = new LinkedList<>();

    Random rand = new Random();
    boolean [][] mines = new boolean[16][9];
    int [][] neighbours = new int[16][9];
    boolean [][] clicked = new boolean [16][9];
    boolean [][] flag = new boolean [16][9];
    public GUI()
    {  
        this.setTitle("MineSweeper");
        this.setSize(1296, 829); //1286,829
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(true);
        for (int i = 0; i < 16; i++)
            {
                for(int j = 0; j < 9; j++)
                {
                    if(rand.nextInt(100)<20)
                    {
                        mines[i][j] = true;
                    }
                    else
                    {
                        mines[i][j] = false;
                    }
                    clicked[i][j] = false;
                    neighbours[i][j] = 0;
                }
            }
        Board board = new Board();
        this.setContentPane(board);

        Move move = new Move();
        this.addMouseMotionListener(move);
        populate_neighbours();

        Click click = new Click();
        this.addMouseListener(click);

    }

    public class Board extends JPanel
    {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public void paintComponent(Graphics gg)
        {
            gg.setColor(Color.DARK_GRAY);
            gg.fillRect(0, 0, 1280, 800);
            gg.setColor(Color.gray);

            for (int i = 0; i < 16; i++)
            {
                for(int j = 0; j < 9; j++)
                {
                     gg.setColor(Color.gray);
                    // if(mines[i][j] == true)      //CHECKING(SHOWING) FOR MINES
                    // {
                    //     gg.setColor(Color.CYAN);
                    // }
                    if(clicked[i][j] == true)       ////CHECKING(SHOWING) FOR CLICKED BOXES
                    {
                        gg.setColor(Color.WHITE);
                        if(mines[i][j] == true)
                        {
                            gg.setColor(Color.RED);
                        }
                    }
                    if((gameover == false) && (mousex >= space + i * 80) && (mousex <space + i * 80 + 80-2*space) && (mousey >=space + j * 80 + 80+26) && (mousey <  j * 80 + 80 + 26 +80-2*space))
                    {
                      gg.setColor(Color.lightGray);  
                    }
                    gg.fillRect(space + i * 80, space + j * 80 + 80 , 80-2*space, 80-2*space);

                    if(clicked[i][j] == true && mines[i][j]!= true && neighbours[i][j] != 0)
                    {
                        gg.setColor(Color.black);
                        if(neighbours[i][j] == 1)
                        {
                            gg.setColor(Color.blue);
                        }
                        else if(neighbours[i][j] == 2)
                        {
                            gg.setColor(Color.green);
                        }
                        else if(neighbours[i][j] == 3)
                        {
                            gg.setColor(Color.red);
                        }
                        else if(neighbours[i][j] == 4)
                        {
                            gg.setColor(new Color(0,0,128));
                        }
                        else if(neighbours[i][j] == 5)
                        {
                            gg.setColor(new Color(178,34,34));
                        }
                        else if(neighbours[i][j] == 6)
                        {
                            gg.setColor(new Color(72,209,204));
                        }
                        else if(neighbours[i][j] == 7)
                        {
                            
                            gg.setColor(Color.black);
                        }
                        else if(neighbours[i][j] == 8)
                        {
                            gg.setColor(Color.darkGray);
                        }
                        gg.setFont(new Font("Tahoma", Font.BOLD, 40));
                        gg.drawString(Integer.toString(neighbours[i][j]), i*80+27 , j*80+80+55);
                    }

                    if(clicked[i][j] == true && mines[i][j]== true) //if box contains mine
                    {
                        gg.setColor(Color.black);
                        gg.fillRect(i*80+10+20, j*80+80+20, 20, 40);
                        gg.fillRect(i*80+20, j*80+80+10+20, 40, 20);
                        gg.fillRect(i*80+5+20, j*80+80+5+20, 30, 30);
                        gg.fillRect(i*80+38, j*80+80+15, 4, 50);
                        gg.fillRect(i*80+15, j*80+80+38, 50, 4);
                        
                    }

                    if(flag[i][j] == true)
                    {
                        flagx = i*80;
                        flagy = j*80+80;
                        gg.setColor(Color.BLACK);
                        gg.fillRect(flagx + 32, flagy + 15, 5, 40);
                        gg.fillRect(flagx + 20, flagy + 50, 30, 10);
                        gg.setColor(Color.red);
                        gg.fillRect(flagx + 16, flagy + 15, 20, 15);
                        gg.setColor(Color.BLACK);
                        gg.drawRect(flagx + 16, flagy + 15, 20, 15);
                        gg.drawRect(flagx + 17, flagy + 16, 18, 13);

                    }

                }
            }
            //Emoji printing
            gg.setColor(Color.yellow);
            gg.fillOval(emojix, emojiy, 70, 70);
            
            gg.setColor(Color.BLACK);
            gg.fillOval(emojix + 20, emojiy + 20, 10, 10);
            gg.fillOval(emojix + 40, emojiy + 20, 10, 10);
            if(gameover == false)
            {
                gg.fillRect(emojix + 22, emojiy + 55, 25, 5);
                gg.fillRect(emojix + 17, emojiy + 50, 5, 5);
                gg.fillRect(emojix + 47, emojiy + 50, 5, 5);
                gg.fillRect(emojix + 12, emojiy + 35, 5, 15);
                gg.fillRect(emojix + 52, emojiy + 35, 5, 15);
                
                // gg.fillRect(emojix + 5, emojiy + 35, 5, 5);
                // gg.fillRect(emojix + 60, emojiy + 35, 5, 5);
            }
            else
            {
                gg.fillRect(emojix + 22, emojiy + 55, 25, 5);

            }


            //Timer Box Printing
            gg.setColor(Color.black);
            gg.fillRect(timex, timey, 140, 70);
            sec = (int)((new Date().getTime() - startDate.getTime()) / 1000);
            if(gameover == false && gamewon == false)
            {
                sec1 = sec;
                if(sec > 999)
                {
                    sec = 999;
                }
            // System.out.println(sec);
                gg.setColor(Color.white);
                gg.setFont(new Font("Tahoma", Font.PLAIN, 80));
                if( sec < 10)
                {
                    gg.drawString("00"+Integer.toString(sec), timex, timey+65);
                }
                else if( sec < 100)
                {
                    gg.drawString("0"+Integer.toString(sec), timex, timey+65);
                }
                else
                {
                    gg.drawString(Integer.toString(sec), timex, timey+65);

                }

            }
            else
            {
                if(sec1 > 999)
                {
                    sec1 = 999;
                }
            // System.out.println(sec);
                gg.setColor(Color.white);
                gg.setFont(new Font("Tahoma", Font.PLAIN, 80));
                if( sec1 < 10)
                {
                    gg.drawString("00"+Integer.toString(sec1), timex, timey+65);
                }
                else if( sec1 < 100)
                {
                    gg.drawString("0"+Integer.toString(sec1), timex, timey+65);
                }
                else
                {
                    gg.drawString(Integer.toString(sec1), timex, timey+65);

                }

            }
            
            //Flag Printing
            // gg.setColor(Color.BLACK);
            // gg.fillRect(flagx + 32, flagy + 15, 5, 40);
            // gg.fillRect(flagx + 20, flagy + 50, 30, 10);
            // gg.setColor(Color.red);
            // gg.fillRect(flagx + 16, flagy + 15, 20, 15);
            // gg.setColor(Color.BLACK);
            // gg.drawRect(flagx + 16, flagy + 15, 20, 15);
            // gg.drawRect(flagx + 17, flagy + 16, 18, 13);
        }
    }
    public class Move implements MouseMotionListener 
    {
        @Override
        public void mouseDragged(MouseEvent arg0)
        {

        }

        
        @Override
        public void mouseMoved(MouseEvent e)
        {
         //   System.out.println("aray mouse tou hill raha hai.");
           mousex = e.getX();
           mousey = e.getY();
           //System.out.println("x = " + mousex + ", y = " + mousey);
        }
    }
    
    public class Click implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            // int tempx = insideBoxX();
            // int tempy = insideBoxY();
            // if(tempx!= -1 && tempy != -1 && gameover == false)
            // {
            //     System.out.println("Mouse is in the box (" + tempx + "," + tempy  + ")" + "with neighbours = " + neighbours[tempx][tempy]);
            //     clicked[tempx][tempy] = true;
            //     if(neighbours[tempx][tempy] == 0)
            //     {
            //         empty_surrond_iterator(tempx, tempy);
            //     }
            //     if(neighbours[tempx][tempy] == 9)
            //     {
            //         gameover = true;
            //         show_bombs();
            //     }   
            // }
            // System.out.println("aray mouse tou click bhi karraha hai");
            // if(insideEmoji())
            // {
            //     reset();
            // }
            
        }

        
        @Override
        public void mouseEntered(MouseEvent arg0)
        {
            
        }
        @Override
        public void mouseExited(MouseEvent arg0)
        {

        }

        
        @Override
        public void mousePressed(MouseEvent e)
        {
            if(SwingUtilities.isLeftMouseButton(e))
            {
                int tempx = insideBoxX();
                int tempy = insideBoxY();
                if(tempx!= -1 && tempy != -1 && gameover == false && flag[tempx][tempy] == false)
                {
                    System.out.println("Mouse is in the box (" + tempx + "," + tempy  + ")" + "with neighbours = " + neighbours[tempx][tempy]);
                    clicked[tempx][tempy] = true;
                    if(neighbours[tempx][tempy] == 0)
                    {
                        empty_surrond_iterator(tempx, tempy);
                    }
                    if(neighbours[tempx][tempy] == 9)
                    {
                        gameover = true;
                        show_bombs();
                    }   
                }
                System.out.println("aray mouse tou click bhi karraha hai");
                if(insideEmoji())
                {
                    reset();
                }
            }
            else if(SwingUtilities.isRightMouseButton(e))
            {
                int tempx = insideBoxX();
                int tempy = insideBoxY();
                if(tempx!= -1 && tempy != -1 && gameover == false)
                {
                    System.out.println("Mouse is in the box (" + tempx + "," + tempy  + ")" + "with neighbours = " + neighbours[tempx][tempy]);
                    if(clicked[tempx][tempy] == false)
                    {
                        if(flag[tempx][tempy] == true )
                        {
                            flag[tempx][tempy] = false;
                            System.out.println("Mouse is in the box (" + tempx + "," + tempy  + ")" + "flag = false");
                    
                        }
                        else
                        {
                            flag[tempx][tempy] = true;
                            System.out.println("Mouse is in the box (" + tempx + "," + tempy  + ")" + "flag = true");
                        }
                    }
                       
                }
            }
            
        }

        @Override
        public void mouseReleased(MouseEvent arg0)
        {
            
        }
    
    
    }

    public boolean insideEmoji()
    {
        double dist = (Math.sqrt(Math.pow((mousex-emoji_center_x), 2) + Math.pow((mousey-emoji_center_y), 2) ));
        System.out.println("distance = " + dist);
        System.out.println("(" + mousex + ","+ mousey + ")");
        
        if(dist <= 35)
        {
            //System.out.println("distance = " + dist);
            return(true);
        }

        return(false);
    }

    public int insideBoxX()
    {
        for (int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                if((mousex >= space + i * 80) && (mousex <space + i * 80 + 80-2*space) && (mousey >=space + j * 80 + 80+26) && (mousey <  j * 80 + 80 + 26 +80-2*space))
                {
                    return(i);
                }
         
            }
        }
        return(-1);
    }
    
    public int insideBoxY()
    {
        for (int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                if((mousex >= space + i * 80) && (mousex <space + i * 80 + 80-2*space) && (mousey >=space + j * 80 + 80+26) && (mousey <  j * 80 + 80 + 26 +80-2*space))
                {
                    return(j);
                }
         
            }
        }
        return(-1);
    }
    public void populate_neighbours()
    {
        for (int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                if(mines[i][j] == true)
                {
                    neighbours[i][j] = 9;
                    bomb_surrond(i, j);
                }
            }
        }


        for (int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 16; j++)
            {
                System.out.print(neighbours[j][i]);
               
            }
            System.out.println("");
        }
    }
    public void show_bombs()
    {
        
        for (int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 9; j++)
            {
               if(neighbours[i][j] == 9)
               {
                   clicked[i][j] = true;
               }
               
            }
        }
    }
    public void bomb_surrond(int i , int j)
    {
        if(i+1 <= 15 && mines[i+1][j] == false)
        {
            neighbours[i+1][j]++;
        }
        if(i-1 >= 0 && mines[i-1][j] == false)
        {
            neighbours[i-1][j]++;
        }
        
        if(j-1 >= 0 && mines[i][j-1] == false)
        {
            neighbours[i][j-1]++;
        }
        
        if(j+1 <= 8 && mines[i][j+1] == false)
        {
            neighbours[i][j+1]++;
        }
        
        if(i-1 >= 0 && j-1 >= 0 && mines[i-1][j-1] == false)    //top left
        {
            neighbours[i-1][j-1]++;
        }
        
        if(i-1 >= 0 && j+1 <= 8 && mines[i-1][j+1] == false)    //bottom left
        {
            neighbours[i-1][j+1]++;
        }
        
        if(i+1 <= 15 && j-1 >= 0 && mines[i+1][j-1] == false)   //topright
        {
            neighbours[i+1][j-1]++;
        }
        
        if(i+1 <= 15 && j+1 <= 8 && mines[i+1][j+1] == false)   //bottom right
        {
            neighbours[i+1][j+1]++;
        }

    }
    
    public void empty_surrond_iterator(int i , int j)
    {
        
        System.out.println(i + " " + j);
        empty_surrond(i, j);
        while(!queue_i.isEmpty() && !queue_j.isEmpty())
        {
            i = queue_i.remove();
            j = queue_j.remove();
            System.out.println(i + " " + j);
            empty_surrond(i, j);
        }
    }


    public void empty_surrond(int i , int j)
    {
        if(i+1 <= 15 && mines[i+1][j] == false)
        {
            if(neighbours[i+1][j] == 0 &&  clicked[i+1][j] == false)
            {
                queue_i.add(i+1);
                queue_j.add(j);
            }
            
            clicked[i+1][j] = true;
        }
        if(i-1 >= 0 && mines[i-1][j] == false)
        {

            if(neighbours[i-1][j] == 0 && clicked[i-1][j] == false)
            {
                queue_i.add(i-1);
                queue_j.add(j);
            }
            clicked[i-1][j] = true;
        }
        
        if(j-1 >= 0 && mines[i][j-1] == false)
        {
            if(neighbours[i][j-1] == 0 && clicked[i][j-1] == false)
            {
                queue_i.add(i);
                queue_j.add(j-1);
            }
            
            clicked[i][j-1] = true;
        }
        
        if(j+1 <= 8 && mines[i][j+1] == false)
        {
            if(neighbours[i][j+1] == 0 &&    clicked[i][j+1] == false)
            {
                queue_i.add(i);
                queue_j.add(j+1);
            }
            clicked[i][j+1] = true;
         
        }
        
        if(i-1 >= 0 && j-1 >= 0 && mines[i-1][j-1] == false)    //top left
        {
            if(neighbours[i-1][j-1] == 0 && clicked[i-1][j-1] == false)
            {
                queue_i.add(i-1);
                queue_j.add(j-1);
            }
            clicked[i-1][j-1] = true;
            
        }
        
        if(i-1 >= 0 && j+1 <= 8 && mines[i-1][j+1] == false)    //bottom left
        {
            
            if(neighbours[i-1][j+1] == 0 && clicked[i-1][j+1] == false)
            {
                queue_i.add(i-1);
                queue_j.add(j+1);
            }
            clicked[i-1][j+1] = true;
        }
        
        if(i+1 <= 15 && j-1 >= 0 && mines[i+1][j-1] == false)   //topright
        {
            if(neighbours[i+1][j-1] == 0 && clicked[i+1][j-1] == false )
            {
                queue_i.add(i+1);
                queue_j.add(j-1);
                clicked[i+1][j-1] = true;
            }
            
            clicked[i+1][j-1] = true;
        }
        
        if(i+1 <= 15 && j+1 <= 8 && mines[i+1][j+1] == false)   //bottom right
        {
            clicked[i+1][j+1] = true;
            if(neighbours[i+1][j+1] == 0)
            {
                queue_i.add(i+1);
                queue_j.add(j+1);
            }
        }
    }

    public void reset()
    {
        startDate = new Date();
        for (int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                if(rand.nextInt(100)<20)
                {
                    mines[i][j] = true;
                }
                else
                {
                    mines[i][j] = false;
                }
                clicked[i][j] = false;
                neighbours[i][j] = 0;
                flag[i][j] = false;
            }
        }
        populate_neighbours();
        gameover = false;
        gamewon = false;


    }

    public boolean gamestop()
    {
        if(gameover == true)
        {
            return(true);
        }
        else
        {
            for (int i = 0; i < 16; i++)
            {
                for(int j = 0; j < 9; j++)
                {
                    if(neighbours[i][j] != 9 && clicked[i][j] == false)
                    {
                        return(false);
                    }
                }
            }
            gamewon = true;
            return(true);
            
        }

    }
    
    
}
