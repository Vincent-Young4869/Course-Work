package student_player;

import boardgame.*;
import Saboteur.SaboteurPlayer;
import Saboteur.cardClasses.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.lang.*;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;

/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260764629");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public SaboteurMove chooseMove(SaboteurBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
//        MyTools.getSomething();

        // Is random the best you can do?
        //Move myMove = boardState.getRandomMove();
    	//boardState = this.createBoard().getBoardState();
		
		// evaluate the state
		int[][] hidden_pos = boardState.hiddenPos;
		int origin = boardState.originPos;
	   
	    int[] target = hidden_pos[1]; 
	    boolean found_nugget = false;
	    
	    for (int i = 0; i < 3; i++) {
    		SaboteurTile hiddenTile = boardState.getHiddenBoard()[boardState.hiddenPos[i][0]][boardState.hiddenPos[i][1]];
    		if(hiddenTile.getIdx().equals("nugget")) {
    			found_nugget = true;
    			target = hidden_pos[i];
    		}
    	} 
	    
	    //System.out.println("the hidden pos: "+revealed_hiddens[0]+", "+revealed_hiddens[1]+", "+revealed_hiddens[2]);
	    int hiddenNum = MyTools.numTileHidden(boardState);
	    
	    ArrayList<SaboteurCard> hand = boardState.getCurrentPlayerCards();
	    boolean isBlocked = false;
        if(boardState.getNbMalus(boardState.getTurnPlayer())>0){
            isBlocked = true;
        }
        
        int BonusNum = 0;
        for(SaboteurCard card: hand) {
        	if(card instanceof SaboteurBonus) {
        		BonusNum++;
        	}
        }
        
        boolean found_target = found_nugget || hiddenNum<=1;
        if (found_target == true && hiddenNum == 1 && !found_nugget){
         found_nugget= true;
         target = hidden_pos[MyTools.nextTileHidden(boardState)];
        }
        
        System.out.println("the current target is: "+target[0]+","+target[1]);
        
	    //Choese move:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        
        if (MyTools.hasCard("Malus", hand) && boardState.getTurnNumber()>6) {
        	return new SaboteurMove(new SaboteurMalus(), 0, 0, boardState.getTurnPlayer());
        }
        
        if(isBlocked) {
    		if (MyTools.hasCard("Bonus", hand)){
        		//System.out.println("has a bonnus card");
        		return new SaboteurMove (new SaboteurBonus(), 0, 0, boardState.getTurnPlayer());	
        	} 
        	else if((hiddenNum>=2 && !found_nugget) && hand.contains(new SaboteurMap())) {
        		int h = MyTools.nextTileHidden(boardState);
        		return new SaboteurMove(new SaboteurMap(),hidden_pos[h][0],hidden_pos[h][1],boardState.getTurnPlayer());
        	}
        	else {
        		ArrayList<SaboteurMove> myLegalMoves = boardState.getAllLegalMoves();
        		for (int i = 0; i < myLegalMoves.size(); i++) {
        			SaboteurMove move = myLegalMoves.get(i);
        			if(move.getCardPlayed() instanceof SaboteurDestroy){
        				int w = MyTools.blockedTilesToDestroy(boardState, move);
        				if(w==1) {return move;}
        			}
        		}
        		
        		found_target = found_nugget || hiddenNum<=1;
        		for(SaboteurCard cardToDrop: hand) {
        			if(MyTools.blockedDrop(cardToDrop, found_target,BonusNum)) {
        				int idx = hand.indexOf(cardToDrop);
        				return new SaboteurMove(new SaboteurDrop(),idx,0,boardState.getTurnPlayer());
        			}
        		}
        		return boardState.getRandomMove();
        	}
        }
        
        //final double D = (origin-target[0])*(origin-target[0]) + (origin-target[1])*(origin-target[1]);
        final double D = 3*Math.abs((origin-target[0])) + 3*Math.abs((origin-target[1]));
        MoveNode myMove = null;
        
        for(SaboteurMove move: boardState.getAllLegalMoves()) {
        	System.out.println(move.toPrettyString());
        	
        	double score = 1000.0;
        	SaboteurCard card = move.getCardPlayed();
        	
        	int[] pos = move.getPosPlayed();
    		if(pos[0]<origin){ continue;}
    		double dist = 3*Math.abs((pos[0]-target[0])) + 3*Math.abs((pos[1]-target[1]));
        	//double dist = (pos[0]-target[0])*(pos[0]-target[0]) + (pos[1]-target[1])*(pos[1]-target[1]);
        	
        	
        	if((hiddenNum>=2 && !found_nugget) && card instanceof SaboteurMap) {
        		int h = MyTools.nextTileHidden(boardState);
        		SaboteurMove m = new SaboteurMove(new SaboteurMap(),hidden_pos[h][0],hidden_pos[h][1],boardState.getTurnPlayer());
        		myMove = new MoveNode(-100*D,m);
        		//score = 100*D;
        	}
		    else if(card instanceof SaboteurMalus) {
		    	score = 100;
			    //score = 2*D - 0.1*(D-dist);
		    }else if(card instanceof SaboteurDestroy) {
		    	int weight = MyTools.blockedTilesToDestroy(boardState, move);
		    	score = weight*dist*1.5;
			    //score = weight*(D-dist)*0.5;
		    }
		    else if(card instanceof SaboteurDrop) {
		    	found_target = found_nugget || hiddenNum<=1;
		    	double weight = MyTools.CardToDrop(boardState, move, found_target,BonusNum);
		    	int turns = boardState.getTurnNumber();
		    	if(turns>10) {
		    		score = 1.5* weight*dist;
		    	}else {
		    		score = weight*dist;
		    	}
		    	
		    }
		    else if(card instanceof SaboteurTile){
		    	double d = MyTools.distToTarget(boardState, move, target);
		    	if(d!=1000) {
		    		dist = d;
		    	}
		    	//System.out.println("the distance to current target:"+dist);
		    	
        		String idx = ((SaboteurTile) card).getIdx();
        		int test1 = MyTools.Test1(move,boardState,pos);
		    	int test2 = MyTools.isPathConnected(boardState, move);
		    	
    		    if(idx.equalsIgnoreCase("0") || idx.equalsIgnoreCase("5") || idx.equalsIgnoreCase("5_flip") || idx.equalsIgnoreCase("6") || idx.equalsIgnoreCase("6_flip") || idx.equalsIgnoreCase("7") || idx.equalsIgnoreCase("7_flip") || idx.equalsIgnoreCase("8") || idx.equalsIgnoreCase("9") || idx.equalsIgnoreCase("9_flip") || idx.equalsIgnoreCase("10")){
    		    	if(test1==1 && test2==1) {
    		    		score = 0.6*dist;
    		    		//score = 0.5*(test1*0.25+test2*0.75)*dist;
    		    	}else {
    		    		score = 1.7*dist;
    		    	}
			 	}else {
			 		score = 3*D;
			 	}
        	}
        	
        	if(myMove==null || myMove.value > score) {
        		myMove = new MoveNode(score,move);
        	}
        	//System.out.println("current iteration's move is "+ myMove.move.toPrettyString()+"with score = "+myMove.value);

        }
        
        return myMove.move;
    	
    }
//-------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------
    
    public static final int WIN_VALUE = Integer.MAX_VALUE-1;
	public static final int LOSE_VALUE = Integer.MIN_VALUE+1;
	
	public double evaluateBoardState(SaboteurBoardState boardState, SaboteurMove move) {
		// NOT IMPLEMENTED YET
		/*
		 * int[] pos = move.getPosPlayed();
		 * SaboteurCard card = move.getCardPlayed();
		 * int[][] hidden_pos = boardState.hiddenPos;
		 * check if the current player has revealed the gold card:
		 *   if found: target_pos = int[i]
		 *   else (not found): target_pos = int[1]; //the middle one by default
		 * function = distance between cur_pos and target pos.
		 * 
		 */
		
    	double score = 0;
		
		if (boardState.getWinner() == player_id) {
			return WIN_VALUE;
		}
		else if (boardState.getWinner() == 1 - player_id) {
			return LOSE_VALUE;
		}
		else if (boardState.getWinner() == Board.DRAW) {
			return 0;
		}
		else {
		// evaluate the state
		int[] pos = move.getPosPlayed();
		int[][] hidden_pos = boardState.hiddenPos;
		int origin = boardState.originPos;
	   
	    int[] target = hidden_pos[1]; 
	    boolean found_nugget = false;
	    int[] revealed_hiddens = {0,0,0};
	    
	    SaboteurTile[][] tiles = boardState.getBoardForDisplay();
	    for(int h=0;h<3;h++) {
		    if(tiles[hidden_pos[h][0]][hidden_pos[h][1]].getIdx()=="nugget") {
			    target[0] = hidden_pos[h][0];
			    target[1] = hidden_pos[h][1];
			    found_nugget = true;
			    revealed_hiddens[h] = 1;
	        }else if(tiles[hidden_pos[h][0]][hidden_pos[h][1]].getIdx()=="hidden1" || tiles[hidden_pos[h][0]][hidden_pos[h][1]].getIdx()=="hidden2") {
			    revealed_hiddens[h] = 1;
		    }
	    }
	   
	    if(!found_nugget) {
		    for(int h=0;h<3;h++) {
			    if(revealed_hiddens[h]==0) {
				    target = hidden_pos[h];
			    }
		    } 
	    }
	   
	    int dist = (pos[0]-target[0])^2 + (pos[1]-target[1])^2;
	    int D = (origin-target[0])^2 + (origin-target[1])^2;
	    
	    System.out.println("target pos:"+target);
	    System.out.println("distance: "+dist);
	    System.out.println("current card:"+move.getCardPlayed().getName());
	    
	   //ArrayList<SaboteurCard> cards =  boardState.getCurrentPlayerCards();
//	    if(move==null){
//	    	score = 0.5*D;
//	    }else
	    if(!found_nugget && move.getCardPlayed() instanceof SaboteurMap) {
		    score = 0.5*D;
	    }else if(found_nugget && move.getCardPlayed() instanceof SaboteurMap) {
		    score = 0.4*D;
	    }else if(move.getCardPlayed() instanceof SaboteurMalus) {
		    score = 5*D - 0.1*(D-dist);
	    }else if(move.getCardPlayed() instanceof SaboteurDestroy) {
		    score = 0.3*(D-dist);
	    }else if(move.getCardPlayed() instanceof SaboteurDrop) {
		    // if current state is malus (cannot tile) 
	    	score = 0.6*D;
	    	//else
		    score = 0.2*dist;
	    }
	    else{ // move is a tile card
		    //if the tile is line/cross/interection...
	    	//SaboteurTile t = move.getCardPlayed();
		    if(true){
		    	score = (D-dist); // - 0.2 * D * 1
		 	}else {
		 		score = 0.1*(D-dist);
		 	}
	 		   
	 	}
	   
	//   for (int i=0; i<boardState.BOARD_SIZE; i++) {
	// 	    for (int j=0; j<boardState.BOARD_SIZE; j++) {
	// 	     board[i][j] = new Node(i, j, boardState.getPieceAt(i,j));
	// 	    }
	//   } 
	//   double playerScore = evaluatePlayerState(board, myPiece);
	//   
	//   double opponentScore = evaluatePlayerState(board, opponentPiece);
	//   
	//   score = playerScore - opponentScore;
	   
	    return score; 
		}
	}
	
    public class MoveNode {
    	public double value;
    	public SaboteurMove move;
    	public MoveNode(double v, SaboteurMove m) {
    		value = v;
    		move = m;
    	}
    }
    
    public class StateEval{
    	public SaboteurMove move;
    	public SaboteurBoardState board;
    	public double value;
    	
    	public StateEval (SaboteurMove m, SaboteurBoardState b) {
    		this.move = m;
    		this.board = b;
    		this.value = evaluateBoardState(b,m);
    	}
    }
    
    public MoveNode MiniMax(int depth, int maxDepth, double alpha, double beta, SaboteurBoardState boardState, SaboteurMove pre_move, int player_id) {
    	
    	boolean isMyTurn = boardState.getTurnPlayer() == player_id ? true : false;
    	
    	if (depth == maxDepth) return new MoveNode(evaluateBoardState(boardState,pre_move), pre_move);
    	
    	PriorityQueue<StateEval> queue;
    	Comparator<StateEval> comparator;
    	//-------------------------------------------------------------------------------------------
    	// >>
//    	if (pre_move != null && depth == 0) {
//    		comparator = new Comparator<StateEval>() {
//    			// it is a priority queue, but we want the biggest on the top 
//				@Override
//				public int compare(StateEval o1, StateEval o2) {
//					//neg if o1 < o2, pos if o1 > o2
//					if (o2.move == pre_move) return LOSE_VALUE+1;
//					else if (o1.move == pre_move) return WIN_VALUE-1;
//					else return (int)(o2.value - o1.value);
//				}
//    		};
//    	}
//    	else 
    	if (isMyTurn) {
    		comparator = new Comparator<StateEval>() {
    			// it is a priority queue, but we want the biggest on the top 
				@Override
				public int compare(StateEval o1, StateEval o2) {
					//neg if o1 < o2, pos if o1 > o2
					return (int)(o2.value - o1.value);
				}
    		};
    	}
    	else {
    		comparator = new Comparator<StateEval>() {
    			// it is a priority queue, but we want the biggest on the top 
				@Override
				public int compare(StateEval o1, StateEval o2) {
					//neg if o1 < o2, pos if o1 > o2
					return (int)(o1.value - o2.value);
				}
    		};
    	}
    	// >>
    	//-------------------------------------------------------------------------------------------
    	
    	
    	MoveNode optMove = null;
    	ArrayList<SaboteurMove> options = boardState.getAllLegalMoves();
    	
    	if (isMyTurn) {
    		queue = new PriorityQueue<StateEval>(10,comparator);
    		
    		for (SaboteurMove move : options) {
    			//apply this move and get a new boardstate
    			SaboteurBoardState newState = (SaboteurBoardState)boardState.clone();
    			newState.processMove(move);
    			queue.add(new StateEval(move, newState));
    		}
    		while(!queue.isEmpty()) {
    			StateEval s = queue.remove();
    			
    			SaboteurBoardState newState = s.board;
    			SaboteurMove move = s.move;
    			
//    			if (newState.getWinner() == player_id) {
//    				return new MoveNode(WIN_VALUE, move);
//    			}
    			
    			MoveNode result = MiniMax(depth+1, maxDepth, alpha, beta, newState, move, 0);
    			
    			// if the best value so far is null we assign the first return value to it
    			if (optMove == null || (result.value > optMove.value)) {
    				optMove = result;
    				optMove.move = move;
    			}
    			
    			//alpha = Math.max(alpha, result.value);
    			
//    			if (alpha < result.value) {
//    				//update the best move
//    				alpha = result.value;
//    				optMove = result;
//    			}
//    			
//    			if (alpha >= beta) {	// pruning
//    				//max_prun++;
//    				optMove.value = beta;
//    				optMove.move = null;
//    				return optMove;
//    			}
    		}
    		return optMove;
    		
    	}else { // not my turn
    		
    		//sort the moves based on the evaluation function (min first)
    		//min
    		queue = new PriorityQueue<StateEval> (10, comparator);
    		//max
    		//add to the queue and then process the queue instead
    		for (SaboteurMove move : options) {
    			//apply this move and get a new boardstate
    			SaboteurBoardState newState = (SaboteurBoardState)boardState.clone();
    			try {
    				boardState.processMove(move);
    			}catch(IllegalArgumentException e) {
    				
    			}
    			queue.add(new StateEval(move, boardState));
    		}
    		int count = 0;
    		while (!queue.isEmpty()) {
    			//apply this move and get a new boardstate
    			StateEval s = queue.remove();
    			//System.out.println("Min step " + count +  ": " + s.value );
    			count++;
    			SaboteurBoardState newState = s.board;
    			SaboteurMove move = s.move;
    			
    			if (newState.getWinner() == 1 - player_id) {
    				return new MoveNode(LOSE_VALUE, move);
    			}
    			
    			MoveNode result = MiniMax(depth+1, maxDepth, alpha, beta, newState, move,1);    			
    			
    			if (optMove == null || (result.value < optMove.value)) {
    				optMove = result;
    				optMove.move = move;
    			}
    			
    			//alpha = Math.max(alpha, result.value);
    			
    			if (beta > result.value) {
    				//update the best move
    				beta = result.value;
    				optMove = result;
    			}
    			
    			if (alpha >= beta) {
    				//min_prun++;
    				optMove.value = alpha;
    				optMove.move = null;
    				return optMove;
    			}
    			
    		}
    		return optMove;
    	}
    			
    }
}