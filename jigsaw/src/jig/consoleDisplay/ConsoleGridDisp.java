package jig.consoleDisplay;

public class ConsoleGridDisp {
    public static void dispGrid(int[][] grid)
    {
        System.console().flush();
        int width = grid.length;
        int height = grid[0].length;

        int boxSize = 5;
        if(boxSize%2 !=1)
        {
            boxSize++;
        }

        String cellBorderTop = " ";
        String rowBorderTop = "";
        String rowBorderMiddle = "";
        String gap = "";

        for(int i=0; i< boxSize; i++)
        {
            cellBorderTop = cellBorderTop.concat("-");
            if(i==boxSize/2){
                gap = gap.concat("#");
            }else{
                gap = gap.concat(" ");
            }
        }
        for(int i=0; i< width; i++)
        {
            rowBorderTop = rowBorderTop.concat(cellBorderTop);
            rowBorderMiddle = rowBorderMiddle.concat("|"+gap);
            
        }
        rowBorderMiddle = rowBorderMiddle.concat("|");



        for(int y=0; y<height; y++)
        {
            String rowStr = rowBorderMiddle;
            for(int x=0; x<width; x++)
            {
                String id = Integer.toString(grid[x][y]);
                String regex = "#";
                if(id.length()>1)
                {
                    regex = " #";
                }
                //replace hashes with actuall values
                rowStr = rowStr.replaceFirst(regex, Integer.toString(grid[x][y]));
            }
            System.out.println(rowBorderTop);
            System.out.println(rowStr);
        }
        System.out.println(rowBorderTop);
    }
}
