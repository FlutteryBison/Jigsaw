package disp;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;

import jig.Jigsaw;
public class JigDisp extends JPanel{

    private int width;
    private int height;

    private Jigsaw jigsaw;

    private Color[] colors;

    private Random rand = new Random();

    private Polygon[] shapes;


    /**
     * Creates a new JPanel that displays the jigsaw
     * @param width     //Width of the JPanel
     * @param height    //height of the JPanel
     */
    public JigDisp(int width, int height)
    {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width,height));

        jigsaw = new Jigsaw(10, 10, 4);

        shapes = jigsaw.getPiecePolygons(width, height);

        colors = new Color[shapes.length];

        for(int i=0; i<shapes.length; i++){

            colors[i] = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
        
    }


    

    @Override
    protected void paintComponent(Graphics g){

        super.paintComponents(g);

        for(int i=0; i<shapes.length;i++){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(colors[i]);
            g2.fillPolygon(shapes[i]);
        }


    }   
    
}
