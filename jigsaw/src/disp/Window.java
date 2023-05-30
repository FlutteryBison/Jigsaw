package disp;


import javax.swing.JFrame;

public class Window{

    private JFrame frame;

    public Window()
    {
        frame = new JFrame("Jigsaw");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        
        JigDisp jigDisp = new JigDisp(250, 250);
        frame.add(jigDisp);

        frame.setVisible(true);
        frame.pack();

    }


    public static void main(String[] args) {

        Window w = new Window();

        
    }

}
