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

import jig.Jigsaw;
import jig.piece.Piece.PieceLook;
public class JigDisp extends JPanel implements MouseInputListener{

    private int width;
    private int height;

    private Jigsaw jigsaw;

    //variables to transform between jigsaw size and screen size
    private double wScale = 1, hScale = 1;
    private double xTranslate = 0, yTranslate = 0;

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

        //the scale between the size of the jigsaw and the size of the window.
        //Only give the jigsaw 80% of the space to leave a border around the edge and space for solving.
        //Translate to keep leave the spare 20% as a border.
        wScale = (width*0.8)/jigsaw.getWidth();
        hScale = (height*0.8)/jigsaw.getHeight();
        xTranslate = width*0.1;
        yTranslate = height*0.1;
        


        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        
    }


    

    @Override
    protected void paintComponent(Graphics g){

        
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(0));
        g2.translate(xTranslate, yTranslate);
        g2.scale(wScale, hScale);

        
        PieceLook[] pieceLooks = jigsaw.getSortedPieceLooks();

        //Draw in reverse order so the most recently moved pieces are on top.
        for(int i=jigsaw.getNumPieces()-1; i>=0;i--){
            
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
         Point2D.Double p = new Point2D.Double(((e.getPoint().x - xTranslate) / wScale) , (e.getPoint().y - yTranslate)/hScale);
        int ID = jigsaw.getTopPieceIDAt(p);

        if(ID>=0)
        {
            activePiece = ID;
            lastPoint = e.getPoint();
        }
    }




    @Override
    public void mouseReleased(MouseEvent e) {
        jigsaw.dropPiece(activePiece);
        activePiece = -1;
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
