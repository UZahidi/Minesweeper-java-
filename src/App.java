public class App implements Runnable {
    GUI gui = new GUI();
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        new Thread(new App()).start();
    }

    @Override
    public void run()
    {
        while(true)
        {
            gui.repaint();
            if(gui.gamestop())
            {
                if(gui.gameover == true)
                {
                    //System.out.println("GameOver, You Lost");
                }
                else if(gui.gamewon == true)
                {
                    //System.out.println("Congratulations, You won");
                }
            }
        }
    }
}
