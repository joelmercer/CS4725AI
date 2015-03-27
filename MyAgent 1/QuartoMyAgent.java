


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuartoMyAgent extends QuartoAgent {
	private static int level;
	private static int globalnextpiece = -1;
	private static long timer1, timelimit, f1, f2, f3, f4, f5, f6, f7, f8, f9;
	int c1=0, c2=0, c3=0, c4=0, c5=0, c6=0, c7=0;
	private static long timeleft, timeleft2;
    //Example AI
    public QuartoMyAgent(GameClient gameClient, String stateFileName) {
        // because super calls one of the super class constructors(you can overload constructors), you need to pass the parameters required.
        super(gameClient, stateFileName);
    }

    //MAIN METHOD
    public static void main(String[] args) {
        //start the server
        GameClient gameClient = new GameClient();

        String ip = "Localhost";
        String stateFileName = null;
        //IP must be specified
        /*if(args.length > 0) {
            ip = args[0];
        } else {
            System.out.println("No IP Specified");
            System.exit(0);
        }
        if (args.length > 1) {
            stateFileName = args[1];
        }
*/
        gameClient.connectToServer(ip, 4321);
        QuartoMyAgent quartoAgent = new QuartoMyAgent(gameClient, stateFileName);
        quartoAgent.play();

        gameClient.closeConnection();

    }


    /*
	 * This code will try to find a piece that the other player can't make a winning move off of
	 */
    @Override
    protected String pieceSelectionAlgorithm() {
        //some useful lines:
        //String BinaryString = String.format("%5s", Integer.toBinaryString(pieceID)).replace(' ', '0');

        this.startTimer();
        
        long starttime = System.currentTimeMillis();
        timelimit = (System.currentTimeMillis()) + (this.timeLimitForResponse - COMMUNICATION_DELAY);
        
        long timestart = System.currentTimeMillis();
        List<Integer> badpieces = new ArrayList<Integer>();
        List<Integer> goodpieces = new ArrayList<Integer>();
        
        boolean skip = false;
        
        for (int i = 0; i < this.quartoBoard.getNumberOfPieces(); i++) {
            skip = false;
            if (!this.quartoBoard.isPieceOnBoard(i)) {
                for (int row = 0; row < this.quartoBoard.getNumberOfRows(); row++) {
                    for (int col = 0; col < this.quartoBoard.getNumberOfColumns(); col++) {
                        if (!this.quartoBoard.isSpaceTaken(row, col)) {
                            QuartoBoard copyBoard = new QuartoBoard(this.quartoBoard);
                            copyBoard.insertPieceOnBoard(row, col, i);
                            if (copyBoard.checkRow(row) || copyBoard.checkColumn(col) || copyBoard.checkDiagonals()) {
                                skip = true;
                                badpieces.add(i);
                                break;
                            }
                        }
                    }
                    if (skip) {
                        break;
                    }

                }
                if (!skip) {
                	goodpieces.add(i);
                	break;
                }

            }
 

        }
        
        int[] piece = new int[2];
        QuartoBoard copyBoard = new QuartoBoard(this.quartoBoard);
        
        c1=0; c2=0; c3=0; c4=0; c5=0; c6=0; c7=0;
        timeleft=0;
        long finishtime = System.currentTimeMillis();
        long totaltime = finishtime - starttime;
        timelimit -= totaltime;
        
        //Call minimax
        piece = minimax(0, 2, copyBoard, -1, 0, 0, 0);
        globalnextpiece = piece[3];
        
        if(globalnextpiece == -1) {
        	if(!goodpieces.isEmpty())
        		globalnextpiece = goodpieces.get(0);
        	else {
        	globalnextpiece = this.quartoBoard.chooseRandomPieceNotPlayed(100);
        	System.out.println("DANG YO!!!!!!!!!!!!!!!!!!!!");
        	}
        }
        
        if(!badpieces.isEmpty()) {
        	
        boolean testpiece = true;
        int i = 0;
        while(testpiece) {
        	
        for (i = 0; i < (badpieces.size()); i++) {
        	
        	if(globalnextpiece==badpieces.get(i)) {
        		
        		if(!goodpieces.isEmpty())
            		globalnextpiece = goodpieces.get(0);
            	else
            		testpiece = false;
        		
        	break;
        	} 
        }
        
        if(i >= badpieces.size())
        	testpiece = false;
        
        }
        
        }
        
        
        
         finishtime = System.currentTimeMillis();
         totaltime = finishtime-starttime;
        long totaltimeleft = timelimit - finishtime;
        System.out.println("*********************************************Move LEVEL: " + piece[4]);
        System.out.println("*********************************************Piece Time Took: " + totaltime);
        System.out.println("*********************************************Piece Time Left: " + totaltimeleft);
        //System.out.println("*********************************************Piece Time taken off: " + timeleft);
        //System.out.println("*********************************************Piece Time total: " + (totaltime+timeleft));
        //System.out.println("*********************************************Piece Time could of did 1 move?: " + ((this.timeLimitForResponse - COMMUNICATION_DELAY) - ((totaltime+timeleft)+totaltime)));
       
      
        
        long timestop = System.currentTimeMillis();
         f1 = timestop - timestart;
       
        //System.out.println("PICKING A PIECES TOOK A LONG TIME! LIKE IT TOOK A WHOLE: " + timetotal);
        String BinaryString = String.format("%5s", Integer.toBinaryString(globalnextpiece)).replace(' ', '0');
        return BinaryString;
    }

    /*
     * Do Your work here
     * The server expects a move in the form of:   row,column
     */
    @Override
    protected String moveSelectionAlgorithm(int pieceID) {
 long starttime = System.currentTimeMillis();
 timelimit = (System.currentTimeMillis()) + (this.timeLimitForResponse - COMMUNICATION_DELAY);

        //If there is a winning move, take it
        for(int row = 0; row < this.quartoBoard.getNumberOfRows(); row++) {
            for(int column = 0; column < this.quartoBoard.getNumberOfColumns(); column++) {
                if(this.quartoBoard.getPieceOnPosition(row, column) == null) {
                    QuartoBoard copyBoard = new QuartoBoard(this.quartoBoard);

                    copyBoard.insertPieceOnBoard(row, column, pieceID);
                    if (copyBoard.checkRow(row) || copyBoard.checkColumn(column) || copyBoard.checkDiagonals()) {
                    	System.out.println("KICKED YOUR ASS BUDDY!!!!!");
                        return row + "," + column;
                    }

                }
            }
        }

        int[] move = new int[2];

        QuartoBoard copyBoard = new QuartoBoard(this.quartoBoard);
        
        c1=0; c2=0; c3=0; c4=0; c5=0; c6=0; c7=0;
        timeleft=0;
        long finishtime = System.currentTimeMillis();
        long totaltime = finishtime - starttime;
        timelimit -= totaltime;
        
        System.out.println("*********************************************Time limit: " + (timelimit-System.currentTimeMillis()));
        //CALL MINIMAX
        move = minimax(0, 1, copyBoard, pieceID, 0, 0, 0);
        
        
        finishtime = System.currentTimeMillis();
         totaltime = finishtime-starttime;
        long totaltimeleft = timelimit - finishtime;
        
        //System.out.println("********************************************* " + move[0] + " " + move[1]);
        globalnextpiece = move[3];
        System.out.println("*********************************************Move LEVEL: " + move[4]);
        System.out.println("*********************************************Move Time Took: " + totaltime);
        System.out.println("*********************************************Move Time Left: " + totaltimeleft);
        System.out.println("*********************************************Move Time taken off: " + (timeleft2 - starttime));
        //System.out.println("*********************************************Move Time total: " + (totaltime+timeleft));
        //System.out.println("*********************************************Move Time could of did 1 move?: " + ((this.timeLimitForResponse - COMMUNICATION_DELAY) - ((totaltime+timeleft)+totaltime)));
        

      //  System.out.println("*********************************************Time f1: " + f1);
       // System.out.println("*********************************************Time f2: " + f2);
       // System.out.println("*********************************************Time f3: " + f3);
       /// System.out.println("*********************************************Time f4: " + f4);
       // System.out.println("*********************************************Time f5: " + f5);
       // System.out.println("*********************************************Time f6: " + f6);
        System.out.flush();
        
      //  if (move[0] == -1 || move[1] == -1) {
       // QuartoBoard testBoard = new QuartoBoard(this.quartoBoard);
       // move = testBoard.chooseNextPositionNotPlayed();
       // }
   
        return move[0] + "," + move[1];
    }



  //  copyBoard.insertPieceOnBoard(row, column, pieceID);
    private int[] minimax(int time, int player, QuartoBoard copyBoard, int piece, int count, int lastrow, int lastcol) {
    	c1++;
    	//long timeleft = System.currentTimeMillis();
    	//System.out.println("*********************************************Time left: " + timeleft);
    	int bestscore;
    	int bestpiece = -1;
    	int currentscore;
    	int bestrow = -1;
    	int bestcol = -1;
    	List<int[]> nextmove = makemoves(copyBoard);
    	List<Integer> nextpiece = pickpiece(copyBoard, piece, player);
    	
    	if(player == 1) {
    		bestscore = Integer.MIN_VALUE;
    	} else {
    		bestscore = Integer.MAX_VALUE;
    	}
    	
    //	System.out.println("*********************************************Time current: " + timer1);
    	//System.out.println("*********************************************Time left: " + (timer1-(timelimit-(timeleft*count))));
    	timer1 = System.currentTimeMillis();
    	timeleft2 = (timelimit - ((timeleft)*(count)));
    	
    	if(timer1 >= timeleft2) {
        //if((nextmove.isEmpty() && nextpiece.isEmpty()) || timer1 >= timeleft2 ) {	

    		time++;
    		level++;
    		bestscore = expectedvalue(copyBoard, player, lastrow, lastcol);
    		
    	} else { 
    		//System.out.println("*********************************************nextpiece: " + nextpiece.size());
    		//System.out.println("*********************************************nextmove: " + nextmove.size());
    	for(int trypiece : nextpiece) {
    		for(int[] trymove : nextmove) {
    			
    			//try move
    			QuartoBoard copyBoardtry = new QuartoBoard(copyBoard);
    			copyBoardtry.insertPieceOnBoard(trymove[0], trymove[1], trypiece);
    			
    			if(player == 1) {
    				
    				piece = findnextpiece(copyBoardtry);
    				level++;
    				currentscore = minimax(time++, 2, copyBoardtry, piece, count++, trymove[0], trymove[1])[2];
    				
    				if(currentscore >= bestscore) {
    					bestscore = currentscore;
    					bestrow = trymove[0];
    					bestcol = trymove[1];
    					bestpiece = piece;
    				}
    				
    			} else {
    				
    				level++;
    				currentscore = minimax(time++, 1, copyBoardtry, trypiece, count++, trymove[0], trymove[1])[2];
    				
    				if(currentscore <= bestscore) {
    					bestscore = currentscore;
    					bestrow = trymove[0];
    					bestcol = trymove[1];
    				}
    			}
    			
    		}
    	}
    		
    	}
    	//System.out.println("############################################################################" + bestscore);
    return new int[] {bestrow, bestcol, bestscore, bestpiece, count};
    }
    
    private List<int[]> makemoves(QuartoBoard copyBoard){
    	long timestart = System.currentTimeMillis();
    	List<int[]> nextmoves = new ArrayList<int[]>();
    	c2++;
    	//If game is over, return empty list

    	  for(int row = 0; row < 5; row++) {
              for(int column = 0; column < 5; column++) {
                  if(copyBoard.getPieceOnPosition(row, column) == null) {
                	  nextmoves.add(new int[] {row, column});
            	  }
              }
          }
              
    	  long timestop = System.currentTimeMillis();
          timeleft+=(timestop-timestart);
          f3 = (timestop-timestart);
        //  System.out.println("Timeleft: " + timeleft);
    	return nextmoves;
    }
    
    private List<Integer> pickpiece(QuartoBoard copyBoard, int piece, int player){
    	long timestart = System.currentTimeMillis();
    	List<Integer> nextpiece = new ArrayList<Integer>();
    	c3++;
    	
    	if(player==1) {
    		nextpiece.clear();
    		nextpiece.add(piece);
    	} else {
    	
    	for (int i = 0; i < 32; i++) {
            if (!copyBoard.isPieceOnBoard(i)) {
            	nextpiece.add(i);
            }
        }
    	
    	}
  	  long timestop = System.currentTimeMillis();
      timeleft+=(timestop-timestart);
      f4 = (timestop-timestart);
      //System.out.println("Timeleft: " + timeleft);
    	return nextpiece;
    }
    
    private int expectedvalue(QuartoBoard copyBoard, int player, int lastrow, int lastcol){
    	long timestart = System.currentTimeMillis();
    	int value = 1;
    	c4++;
    	
    	int piece;
    	int rowvalue = 0;
		int colvalue = 0;
		int diavalue1 = 0;
		int diavalue2 = 0;
		
    	List<Integer> sol1 = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));
        int count1 = 0;
        List<Integer> sol2 = new ArrayList<Integer>(Arrays.asList(16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31));
        int count2 = 0;
        List<Integer> sol3 = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,16,17,18,19,20,21,22,23));
        int count3 = 0;
        List<Integer> sol4 = new ArrayList<Integer>(Arrays.asList(8,9,10,11,12,13,14,15,24,25,26,27,28,29,30,31));
        int count4 = 0;
        List<Integer> sol5 = new ArrayList<Integer>(Arrays.asList(0,1,2,3,16,17,18,19,8,9,10,11,24,25,26,27));
        int count5 = 0;
        List<Integer> sol6 = new ArrayList<Integer>(Arrays.asList(4,5,6,7,20,21,22,23,12,13,14,15,28,29,30,31));
        int count6 = 0;
        List<Integer> sol7 = new ArrayList<Integer>(Arrays.asList(0,1,16,17,8,9,24,25,4,5,20,21,12,13,28,29));
        int count7 = 0;
        List<Integer> sol8 = new ArrayList<Integer>(Arrays.asList(2,3,18,19,10,11,26,27,6,7,22,23,14,15,30,31));
        int count8 = 0;
        List<Integer> sol9 = new ArrayList<Integer>(Arrays.asList(0,16,8,24,4,20,12,28,2,18,10,26,6,22,14,30));
        int count9 = 0;
        List<Integer> sol10 = new ArrayList<Integer>(Arrays.asList(1,17,9,25,5,21,13,29,3,19,11,27,7,23,15,31));
        int count10 = 0;
        
    	//check row
		int rowcount = 0;
    	for(int i = 0; i<5; i++) {
    		
    		if(copyBoard.getPieceOnPosition(lastrow, i) != null) {
    		piece = copyBoard.getPieceOnPosition(lastrow, i).getPieceID();
    		rowcount++;
    		
    		if(sol1.contains(piece))
    			count1++;
    		if(sol2.contains(piece))
    			count2++;
    		if(sol3.contains(piece))
    			count3++;
    		if(sol4.contains(piece))
    			count4++;
    		if(sol5.contains(piece))
    			count5++;
    		if(sol6.contains(piece))
    			count6++;
    		if(sol7.contains(piece))
    			count7++;
    		if(sol8.contains(piece))
    			count8++;
    		if(sol9.contains(piece))
    			count9++;
    		if(sol10.contains(piece))
    			count10++;
    		}
    	}
    	//Find row value
    	if(rowcount>0) {
			if(rowcount==count1)
				rowvalue += count1;
			if(rowcount==count2)
				rowvalue += count2;
			if(rowcount==count3)
				rowvalue += count3;
			if(rowcount==count4)
				rowvalue += count4;
			if(rowcount==count5)
				rowvalue += count5;
			if(rowcount==count6)
				rowvalue += count6;
			if(rowcount==count7)
				rowvalue += count7;
			if(rowcount==count8)
				rowvalue += count8;
			if(rowcount==count9)
				rowvalue += count9;
			if(rowcount==count10)
				rowvalue += count10;
		}
    	
    	count1 = 0; 
    	count2 = 0; 
    	count3 = 0; 
    	count4 = 0; 
    	count5 = 0; 
    	count6 = 0; 
    	count7 = 0; 
    	count8 = 0; 
    	count9 = 0; 
    	count10 = 0; 
    
    	//check col
    	int colcount = 0;
    	for(int i = 0; i<5; i++) {
    		
    		if(copyBoard.getPieceOnPosition(i, lastcol) != null) {
    		colcount++;
    		piece = copyBoard.getPieceOnPosition(i, lastcol).getPieceID();
    		
    		if(sol1.contains(piece))
    			count1++;
    		if(sol2.contains(piece))
    			count2++;
    		if(sol3.contains(piece))
    			count3++;
    		if(sol4.contains(piece))
    			count4++;
    		if(sol5.contains(piece))
    			count5++;
    		if(sol6.contains(piece))
    			count6++;
    		if(sol7.contains(piece))
    			count7++;
    		if(sol8.contains(piece))
    			count8++;
    		if(sol9.contains(piece))
    			count9++;
    		if(sol10.contains(piece))
    			count10++;
    		}
    	}
    	//Find col value
    	if(colcount>0) {
			if(colcount==count1)
				colvalue += count1;
			if(colcount==count2)
				colvalue += count2;
			if(colcount==count3)
				colvalue += count3;
			if(colcount==count4)
				colvalue += count4;
			if(colcount==count5)
				colvalue += count5;
			if(colcount==count6)
				colvalue += count6;
			if(colcount==count7)
				colvalue += count7;
			if(colcount==count8)
				colvalue += count8;
			if(colcount==count9)
				colvalue += count9;
			if(colcount==count10)
				colvalue += count10;
		}
    	
    	count1 = 0; 
    	count2 = 0; 
    	count3 = 0; 
    	count4 = 0; 
    	count5 = 0; 
    	count6 = 0; 
    	count7 = 0; 
    	count8 = 0; 
    	count9 = 0; 
    	count10 = 0; 
    	
    	
    	
    	//check Diagonal 
    	int diacount1 = 0;
    	if(lastrow == lastcol) {		
    		
    		for(int i = 0; i<5; i++) {
    			
    			if(copyBoard.getPieceOnPosition(i, i) != null) {
    			
    			diacount1++;
        		piece = copyBoard.getPieceOnPosition(i, i).getPieceID();
        		
        		if(sol1.contains(piece))
        			count1++;
        		if(sol2.contains(piece))
        			count2++;
        		if(sol3.contains(piece))
        			count3++;
        		if(sol4.contains(piece))
        			count4++;
        		if(sol5.contains(piece))
        			count5++;
        		if(sol6.contains(piece))
        			count6++;
        		if(sol7.contains(piece))
        			count7++;
        		if(sol8.contains(piece))
        			count8++;
        		if(sol9.contains(piece))
        			count9++;
        		if(sol10.contains(piece))
        			count10++;
    			}
        	}
    	}
    	//Find dia value
    	if(diacount1>0) {
			if(diacount1==count1)
				diavalue1 += count1;
			if(diacount1==count2)
				diavalue1 += count2;
			if(diacount1==count3)
				diavalue1 += count3;
			if(diacount1==count4)
				diavalue1 += count4;
			if(diacount1==count5)
				diavalue1 += count5;
			if(diacount1==count6)
				diavalue1 += count6;
			if(diacount1==count7)
				diavalue1 += count7;
			if(diacount1==count8)
				diavalue1 += count8;
			if(diacount1==count9)
				diavalue1 += count9;
			if(diacount1==count10)
				diavalue1 += count10;
		}
    	
    	count1 = 0; 
    	count2 = 0; 
    	count3 = 0; 
    	count4 = 0; 
    	count5 = 0; 
    	count6 = 0; 
    	count7 = 0; 
    	count8 = 0; 
    	count9 = 0; 
    	count10 = 0; 
    		
    		int diacount2 = 0;
    		if((lastrow==4 && lastcol==0) || (lastrow==3 && lastcol==1) || (lastrow==1 && lastcol==3) || (lastrow==0 && lastcol==4) || (lastrow==2 && lastcol==2)) { 
    		int[] row = {4, 3, 2, 1, 0};
    		int[] col = {0, 1, 2, 3, 4};
    		
    		for(int i = 0; i<5; i++) {
    			
    			if(copyBoard.getPieceOnPosition(row[i], col[i]) != null) {
    				diacount2++;
        		piece = copyBoard.getPieceOnPosition(row[i], col[i]).getPieceID();
        		
        		if(sol1.contains(piece))
        			count1++;
        		if(sol2.contains(piece))
        			count2++;
        		if(sol3.contains(piece))
        			count3++;
        		if(sol4.contains(piece))
        			count4++;
        		if(sol5.contains(piece))
        			count5++;
        		if(sol6.contains(piece))
        			count6++;
        		if(sol7.contains(piece))
        			count7++;
        		if(sol8.contains(piece))
        			count8++;
        		if(sol9.contains(piece))
        			count9++;
        		if(sol10.contains(piece))
        			count10++;
    			}
        	}
    		
    	}
    	//Find dia value 2
    		if(diacount2>0) {
    			if(diacount2==count1)
    				diavalue2 += count1;
    			if(diacount2==count2)
    				diavalue2 += count2;
    			if(diacount2==count3)
    				diavalue2 += count3;
    			if(diacount2==count4)
    				diavalue2 += count4;
    			if(diacount2==count5)
    				diavalue2 += count5;
    			if(diacount2==count6)
    				diavalue2 += count6;
    			if(diacount2==count7)
    				diavalue2 += count7;
    			if(diacount2==count8)
    				diavalue2 += count8;
    			if(diacount2==count9)
    				diavalue2 += count9;
    			if(diacount2==count10)
    				diavalue2 += count10;
    		}
        	
	
    		

        value = rowvalue + colvalue + diavalue1 + diavalue2;
        
        if(player==2 && value!=0)
        	value *= -1;
        
    	
    	long timestop = System.currentTimeMillis();
    	timeleft += (timestop - timestart);
    	f5 = (timestop-timestart);
    	return value;
    }



protected int findnextpiece(QuartoBoard copyBoard) {
    //some useful lines:
    //String BinaryString = String.format("%5s", Integer.toBinaryString(pieceID)).replace(' ', '0');
	//long timestart2 = System.currentTimeMillis();
	
    this.startTimer();
    long timestart = System.currentTimeMillis();
    c5++;
    List<Integer> badpieces = new ArrayList<Integer>();
    List<Integer> goodpieces = new ArrayList<Integer>();

    boolean skip = false;
    
    for (int i = 0; i < this.quartoBoard.getNumberOfPieces(); i++) {
        skip = false;
        if (!copyBoard.isPieceOnBoard(i)) {
            for (int row = 0; row < copyBoard.getNumberOfRows(); row++) {
                for (int col = 0; col < copyBoard.getNumberOfColumns(); col++) {
                    if (!copyBoard.isSpaceTaken(row, col)) {
                        QuartoBoard copyBoard2 = new QuartoBoard(copyBoard);
                        copyBoard2.insertPieceOnBoard(row, col, i);
                        if (copyBoard2.checkRow(row) || copyBoard2.checkColumn(col) || copyBoard2.checkDiagonals()) {
                            skip = true;
                            badpieces.add(i);
                            break;
                        }
                    }
                }
                if (skip) {
                    break;
                }

            }
            if (!skip) {
            	goodpieces.add(i);
            }

        }


    }


    //if we don't find a piece in the above code just grab the first random piece
    int pieceId;// = copyBoard.chooseRandomPieceNotPlayed(100);
   // String BinaryString = String.format("%5s", Integer.toBinaryString(pieceId)).replace(' ', '0');

    int[] sol1 = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    List<Integer> count1 = new ArrayList<Integer>();
    int[] sol2 = {16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    List<Integer> count2 = new ArrayList<Integer>();
    int[] sol3 = {0,1,2,3,4,5,6,7,16,17,18,19,20,21,22,23};
    List<Integer> count3 = new ArrayList<Integer>();
    int[] sol4 = {8,9,10,11,12,13,14,15,24,25,26,27,28,29,30,31};
    List<Integer> count4 = new ArrayList<Integer>();
    int[] sol5 = {0,1,2,3,16,17,18,19,8,9,10,11,24,25,26,27};
    List<Integer> count5 = new ArrayList<Integer>();
    int[] sol6 = {4,5,6,7,20,21,22,23,12,13,14,15,28,29,30,31};
    List<Integer> count6 = new ArrayList<Integer>();
    int[] sol7 = {0,1,16,17,8,9,24,25,4,5,20,21,12,13,28,29};
    List<Integer> count7 = new ArrayList<Integer>();
    int[] sol8 = {2,3,18,19,10,11,26,27,6,7,22,23,14,15,30,31};
    List<Integer> count8 = new ArrayList<Integer>();
    int[] sol9 = {0,16,8,24,4,20,12,28,2,18,10,26,6,22,14,30};
    List<Integer> count9 = new ArrayList<Integer>();
    int[] sol10 = {1,17,9,25,5,21,13,29,3,19,11,27,7,23,15,31};
    List<Integer> count10 = new ArrayList<Integer>();
        
    //System.out.println("*********************************************************sol2 " + (!copyBoard.isPieceOnBoard(sol1[0])));
    //System.out.println("*********************************************************sol2 " + (!copyBoard.isPieceOnBoard(sol2[0])));
    
    for(int i = 0; i<16; i++) {
    	
    	if(!copyBoard.isPieceOnBoard((sol1[i]))) 
    		count1.add(sol1[i]);
    	if(!copyBoard.isPieceOnBoard(sol2[i])) 
    		count2.add(sol2[i]);
    	if(!copyBoard.isPieceOnBoard(sol3[i]))
    		count3.add(sol3[i]);
    	if(!copyBoard.isPieceOnBoard(sol4[i]))
    		count4.add(sol4[i]);
    	if(!copyBoard.isPieceOnBoard(sol5[i]))
    		count5.add(sol5[i]);
    	if(!copyBoard.isPieceOnBoard(sol6[i]))
    		count6.add(sol6[i]);
    	if(!copyBoard.isPieceOnBoard(sol7[i]))
    		count7.add(sol7[i]);
    	if(!copyBoard.isPieceOnBoard(sol8[i]))
    		count8.add(sol8[i]);
    	if(!copyBoard.isPieceOnBoard(sol9[i]))
    		count9.add(sol9[i]);
    	if(!copyBoard.isPieceOnBoard(sol10[i]))
    		count10.add(sol10[i]);
    
    }
    
    int highest;
    highest = Math.max(count1.size(), count2.size()); 
    //System.out.println("********************************************************* " + count2.size());
    highest = Math.max(highest, count3.size());
    highest = Math.max(highest, count4.size());
    highest = Math.max(highest, count5.size());
    highest = Math.max(highest, count6.size());
    highest = Math.max(highest, count7.size());
    highest = Math.max(highest, count8.size());
    highest = Math.max(highest, count9.size());
    highest = Math.max(highest, count10.size());
    
    //System.out.println("********************************************************* " + highest);
    
    
    
    if(highest==count1.size())
    	pieceId = count1.get(0);
    else if(highest==count2.size())
    	pieceId = count2.get(0);
    else if(highest==count3.size())
    	pieceId = count3.get(0);
    else if(highest==count4.size())
    	pieceId = count4.get(0);
    else if(highest==count5.size())
    	pieceId = count5.get(0);
    else if(highest==count6.size())
    	pieceId = count6.get(0);
    else if(highest==count7.size())
    	pieceId = count7.get(0);
    else if(highest==count8.size())
    	pieceId = count8.get(0);
    else if(highest==count9.size())
    	pieceId = count9.get(0);
    else 
    	pieceId = count10.get(0);

    if(!badpieces.isEmpty()) {
    	
        boolean testpiece = true;
        int i = 0;
        
        while(testpiece) {
        	//System.out.println("loop");
        
        for (i = 0; i < (badpieces.size()); i++) {
        	
        	if(pieceId==badpieces.get(i)) {
        		
        		if(!goodpieces.isEmpty()) {
        			pieceId = goodpieces.get(0);
        		} else {
        			testpiece = false;
        			break;
        		}
        		
        		}
        		
        	} 
        
        
        if(i >= (badpieces.size()))
        	testpiece = false;
        
        }
    }
        
    
    long timestop = System.currentTimeMillis();
	timeleft += (timestop - timestart);
	f6 = (timestop-timestart);
	//System.out.println("Timeleft: " + timeleft);
    return pieceId;
}

}
