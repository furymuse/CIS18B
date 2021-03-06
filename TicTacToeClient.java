//TicTacToeClient class part C

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

 public class TicTacToeClient extends Applet implements Runnable 
 {

    private TextArea display;
    private TextField field;
    private Panel board_one, board_2;
    private Square board[][], currentSquare;
    private TextField field2;
    private Socket link;
    private DataInputStream dataIn;
    private DataOutputStream output;
    private Thread outputThread;
    private char myMark;

    public void init()

    {
       setLayout( new BorderLayout() );
       display = new TextArea( 4, 30 );
       display.setEditable( false );
       add( display, BorderLayout.SOUTH );

       board_one = new Panel();
       board_one.setLayout( new GridLayout( 3, 3, 0, 0 ) );
       board = new Square[ 3 ][ 3 ];

       for ( int row = 0; row < board.length; row++ )
       for ( int col = 0; col < board[ row ].length; col++ ) 
       {
    	   board[ row ][ col ] = new Square( ' ', row * 3 + col );
       board[ row ][ col ].addMouseListener(new SquareListener(this, board[ row ][ col ] ) );
       board_one.add( board[ row ][ col ] );    

       }

       field = new TextField();
       field.setEditable( false );
       add( field, BorderLayout.NORTH );
       board_2 = new Panel();
       board_2.add( board_one );
       add( board_2, BorderLayout.CENTER );

 }
             
       public void start()     
       {     
    	   try 
	       {
		     link = new Socket( InetAddress.getLocalHost(), 5000 );
	         dataIn = new DataInputStream(link.getInputStream() );
	         output = new DataOutputStream(link.getOutputStream() );
	       }
	       catch ( IOException e ) 
	       {
	    	   e.printStackTrace();         
	       }
       		outputThread = new Thread( this );
       		outputThread.start();
       		
       }

       public void run()
       {
    	   try 
	       {
	          myMark = dataIn.readChar();
	          field.setText( "You're player: \\" + myMark + "\\" );
	       }
    	   catch ( IOException e ) 
	       {
	    	   e.printStackTrace(); 
	       }
             while ( true ) 
	       {
	    	   try 
	          {
	    		 String s = dataIn.readUTF();
	             processMessage( s );
	          }   
              catch ( IOException e ) 
    	   {
        	 e.printStackTrace();
            }
         }
       }             

        public void processMessage( String s )
        {

        if ( s.equals( "Move OK." ) ) 
        {
           display.append( "Valid move, please wait.\n" );
           currentSquare.setMark( myMark );
           currentSquare.repaint();
        }
         else if ( s.equals( "Bad move, try once more." ) ) 
        {
         display.append( s + "\n" );
        }
         else if ( s.equals( "Other player moved" ) ) 
        {
        	   try 
	           {
        		   int loc = dataIn.readInt();
        		   done:
	              for ( int row = 0; row < board.length; row++ )
	            	  for ( int col = 0;col < board[ row ].length; col++ )
			 
	                   	if ( row * 3 + col == loc ) 
	                    {
	                    	board[ row ][ col ].setMark(( myMark == 'X' ? 'O' : 'X' ) );
	                    	board[ row ][ col ].repaint();
	                        break done; 
	                    }
	                       
        		   display.append("Other player finished, your move.\n" );    
	            }
    
        	   catch ( IOException e ) 
        	   {
        		   e.printStackTrace(); 
               }     
        }
         else 
        {
        	 display.append( s + "\n" );
        }  

         }
      
        public void sendClickedSquare( int loc )
	     {  
        	try 
	        {
        		output.writeInt( loc );
	        }  
	
	        catch ( IOException ie ) 
        	{
	        	ie.printStackTrace();
	        }          
	     }
	        
        public void setCurrentSquare( Square s )
	       {
        	currentSquare = s;
           }	
}	
	
	    class Square extends Canvas 
	    {
	    	private char mark;
	    	private int location;
	    
	    	public Square( char MARK, int LOCATION)
	    	{
		        mark = MARK;
		        location = LOCATION;
		        setSize ( 35, 35 );
	    	}

		     public void setMark( char myChar ) { mark = myChar; }
		     public int getSquareLocation() { return location; }
		     public void paint( Graphics grph )
		     {
		    	grph.drawRect( 0, 0, 29, 29 );
		        grph.drawString( String.valueOf( mark ), 11, 20 );
		     }
}
     
  class SquareListener extends MouseAdapter 
  {
	 private TicTacToeClient applet;
     private Square mySquare;
     public SquareListener( TicTacToeClient tc, Square ss )
     {
    	applet = tc;
        mySquare = ss;
     }

     public void mouseReleased( MouseEvent e )
     {
        applet.setCurrentSquare( mySquare );
        applet.sendClickedSquare( mySquare.getSquareLocation() );
     }

  }










  



  



 

 


     

  





 



  



