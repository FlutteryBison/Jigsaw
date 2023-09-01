package disp;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Random;

import jig.Jigsaw;
import jig.piece.Piece.PieceLook;
public class JigDisp extends JPanel implements MouseInputListener{

    private int width;
    private int height;

    private Jigsaw jigsaw;

    private Color[] colors;

    private Random rand = new Random();

    private double wScale = 1, hScale = 1;

    private int activePiece = -1;

    private Point lastPoint;

    



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

        jigsaw = new Jigsaw(25, 25, 20);

        
        wScale = width/jigsaw.getWidth();
        hScale = height/jigsaw.getHeight();

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        
        
        


        colors = new Color[jigsaw.getNumPieces()];

        for(int i=0; i<jigsaw.getNumPieces(); i++){

            colors[i] = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
        
    }


    

    @Override
    protected void paintComponent(Graphics g){

        
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(0));
        g2.scale(wScale, hScale);

        
        

        PieceLook[] pieceLooks = jigsaw.getSortedPieceLooks();

        for(int i=0; i<jigsaw.getNumPieces();i++){
            
            g2.setColor(pieceLooks[i].getCol());
            g2.fill(pieceLooks[i].getShape());
            g2.setColor(Color.BLACK);
            g2.draw(pieceLooks[i].getShape());
            
        }


    }

    




    @Override
    public void mouseClicked(MouseEvent e) {
        
    }




    @Override
    public void mousePressed(MouseEvent e) {
        int ID = jigsaw.getTopPieceIDAt(new Point2D.Double(e.getPoint().x / wScale, e.getPoint().y/hScale));

        if(ID>=0)
        {
            activePiece = ID;
            lastPoint = e.getPoint();
        }
    }




    @Override
    public void mouseReleased(MouseEvent e) {
        activePiece = -1;
        jigsaw.dropPiece();
        this.revalidate();
        this.repaint();
    }




    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }




    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }




    @Override
    public void mouseDragged(MouseEvent e) {
        if(activePiece != -1)
        {
            Point2D.Double dist = new Point2D.Double((e.getPoint().x - lastPoint.x )/ wScale, (e.getPoint().y-lastPoint.y)/hScale);
            jigsaw.movePiece(activePiece, dist);
            lastPoint = e.getPoint();
            this.revalidate();
            this.repaint();
        }
        
    }




    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
    }


    
}
