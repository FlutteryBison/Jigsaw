package delaunaytriangulation;

import java.awt.Point;
import java.util.Objects;

/**
 * Class representing an edge. The edge is directionless ie. p1 -> p2 is equal to p2 -> p1
 */
public class Edge {

    Point p1;
    Point p2;

    public Point getP1() {
        return this.p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return this.p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }
    

    public Edge(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean isEndPoint(Point p)
    {
        return p.equals(p1) || p.equals(p2);
    }

    

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Edge)) {
            return false;
        }
        Edge edge = (Edge) o;
        return (p1.equals(edge.p1) && p2.equals(edge.p2)) || (p1.equals(edge.p2) && p2.equals(edge.p1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2);
    }

    @Override
    public String toString() {
        return "{" +
            " p1='" + p1 + "'" +
            ", p2='" + p2 + "'" +
            "}";
    }
    

    

    
}
