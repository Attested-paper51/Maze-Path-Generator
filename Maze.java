package Maze;

import java.io.*;
import BasicIO.*;                // for IO classes
import static BasicIO.Formats.*; // for field formats
import static java.lang.Math.*;  // for math constants and functions
/**
 * This class represents a program to find an automatic pathway from 'G' to 'H'
 * 'G' and 'H' are placed in random indices in each compilation
 * The premise is to find an automatic pathway using recursion
 * In the recursive method, out of the 24 available direction sequences, we choose to use 1:
 * Mine is: RIGHT, UP, LEFT, DOWN.
 * 
 * @author Muhammed Bilal
 *
 * @March 29, 2021
 * @version 1.0
 */
public class Maze
{
  //instance variables
  char [][] maze;  //declares "maze" as a 2-D array of char type.
  char h = 'H';  //declares "H" as of char type. - represents Hansel
  char g = 'G';  //declares "G" as of char type. - represents Gretel
  String l1, l2;
  int row, col;
  int x, y, a, b;
  private ASCIIOutputFile out, out2;  //to display into a .txt file
  private ASCIIDataFile in;  //to read from a .txt file
  
  //constructor
  public Maze()
  { 
    in = new ASCIIDataFile();  //selects .txt file to read from
    out = new ASCIIOutputFile("pathfinder.txt");  //selects .txt file to print on
    
    String line = in.readLine();  //reads the 1st line of chosen .txt file and stores it into a String variable "line"
    System.out.println(line);
    
    //column
    l1 = line.charAt(3) + "" + line.charAt(4);  //accesses the 1st and 2nd number in the String "line" - represents the column of "maze"
    col = Integer.parseInt(l1);  //converts l1 (column) into an integer var.
    System.out.println("Column: " + col);
    
    //row
    if(line.charAt(1) == ' ')  //if the row number is only 1 digit:
      l2 = line.charAt(0) + "";  // then access the 1st number in the String "line" - represents the row of "maze"
    else
      l2 = line.charAt(0) + "" + line.charAt(1);   //if the row number is 2 digits: then access the 1st AND 2nd numbers in the String "line"
    row = Integer.parseInt(l2);  //converts l2 (row) into an integer var.
    System.out.println("Row: " + row);
    
    maze = new char [row][col+1];  //l2 = row, l1 = column - both are generated based on .txt file being read
    printMaze();  //prints default maze but without the top numbers
    generateLocations();  //generates the random locations of Hansel and Gretel
    if(findPath(a, b))  //checks to see if a path is found
    {
      out2 = new ASCIIOutputFile("pathfinder.txt");  //selects .txt file to print on
      //If the path to H is found aka true is returned then print out the maze with its generated pathway
      for(int i=0; i<maze.length; i++)
      {
        for(int j=0; j<maze[i].length; j++)
        {
          if(i == a && j == b)
          {
            maze[a][b] = g; 
          }
          out2.writeC(maze[i][j]); 
        }
      }
      System.out.print("Found H!!!");
    }
    else
    {
      //If false is retunred then path is not found
      System.out.print("Path cannot be found"); 
    }
  } 
  
  //methods
  /* goes through the maze .txt file and inserts each character in each index of our array
   * Traverses in a row-major fashion
   * prints each index in a new .txt file
   * skips the first line
   */
  private void printMaze()
  {
    for(int i=0; i<maze.length; i++)
    {
      for(int j=0; j<maze[i].length; j++)
      {
        maze[i][j] = in.readC();
        int c = (int) maze[i][j];
        //System.out.println(" row:" + i + " col:" + j + " character: " + maze[i][j] + " ascii: " + c);
        out.writeC(maze[i][j]);
      }
    }
  }
  
  /*
   * do while loop: 1st executes command then asks if condition is true
   * 1) Chooses random locations for both Gretel and Hansel
   * 2) Asks if BOTH Gretel and Hansel are NOT at walls
   * - 3) If 2) is true, then asks if BOTH are not at the same location
   *    - 4) If 3) is true then prints both H and G at their determined locations
   * - 5) If 2) is NOT true then goes to while loop to check condition and repeats 1)
   */
  private void generateLocations()
  {
    int largeX = row - 1;
    int smallX = 0;
    int largeY = col - 2;
    int smallY = 0;
    do
    {
      x = (int)((largeX - smallX + 1) * Math.random() + smallX);
      y = (int)((largeY - smallY + 1) * Math.random() + smallY);
      a = (int)((largeX - smallX + 1) * Math.random() + smallX);
      b = (int)((largeY - smallY + 1) * Math.random() + smallY);
      
      System.out.println("Hansel: (" + x + "," + y + ")");
      System.out.println("Gretel: (" + a + "," + b + ")");
      if(maze[x][y] != '#' && maze[a][b] != '#')
      {
        if(x != a | y != b)
        {
          in = new ASCIIDataFile();  //selects .txt file to read from
          out = new ASCIIOutputFile("pathfinder.txt");  //selects .txt file to read from
          for(int i=0; i<maze.length; i++)
          {
            for(int j=0; j<maze[i].length; j++)
            {
              maze[i][j] = in.readC();
              if(i == x && j == y)
              {
                maze[x][y] = h;
                out.writeC(maze[x][y]);
              }
              else if(i == a && j == b)
              {
                maze[a][b] = g;
                out.writeC(maze[a][b]);
              }
              else
              {
                out.writeC(maze[i][j]); 
              }
            }
          }
          System.out.println("(" + x + "," + y + ") contains " + maze[x][y]);
          System.out.println("(" + a + "," + b + ") contains " + maze[a][b]);
          break;
        }
      }
    }
    while(maze[x][y] == '#' || maze[a][b] == '#');
  }
  
  /**
   * Finds the path from the G to the H, recursively.
   * parameter r: the current row (vertical) of G
   * parameter c: the current column (horizontal) of G
   * returns a boolean depending on the outcome
   * if returns true then path is found
   * if returns false then path is not found
   */
  private boolean findPath (int r, int c) 
  {
    
    //Base Case 1
    if(maze[r][c] == h)
    {
      System.out.println("H is found at: " + "(" + r + "," + c + ")");
      return true; 
    }
    
    //Base Case 2,3,4
    //'#' || 'A' || '.' - All 3 characters are being treated as walls
    else if(maze[r][c] == '#' || maze[r][c] == 'A' || maze[r][c] == '.')
    {
      //System.out.println("hit a wall at: (" + r + "," + c + ")" );
      return false;
    }
    
    //leaving my footprint - so that I can't be able to move backwards, therefore creating an infinite traversal - stackoverflow error
    maze[r][c] = 'A';
    
    //Check Right - calls findPath to the right - to check if it is a wall or not
    if(findPath(r, c+1))
    {
      maze[r][c] = '>';
      return true;
    }
    
    //Check Up - calls findPath to the above - to check if it is a wall or not
    if(findPath(r-1, c))
    {
      maze[r][c] = '^';
      return true;
    }
    
    //Check Left - calls findPath to the left - to check if it is a wall or not
    if(findPath(r, c-1))
    {
      maze[r][c] = '<';
      return true;
    }
    
    //Check Down - calls findPath below - to check if it is a wall or not
    if(findPath(r+1, c))
    {
      maze[r][c] = 'v';
      return true;
    }
    
    /*If maze[r][c] is surrounded by walls on all sides
     *ie: All the if-statements above are skipped, because they were not true
     *Then insert a '.' at maze[r][c] - '.' is to be treated as wall as seen from the Base Case 2,3,4*/ 
    maze[r][c] = '.';
    return false;
  }
  
  public static void main(String[] args) {
    Maze m = new Maze(); 
  }  
}//COSC1P03