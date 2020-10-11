package student_player;

import java.util.*;

import boardgame.Move;

import Saboteur.SaboteurPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;

import Saboteur.cardClasses.*;
//import Saboteur.cardClasses.SaboteurCard;
//import Saboteur.cardClasses.SaboteurDestroy;
//import Saboteur.cardClasses.SaboteurMalus;
//import Saboteur.cardClasses.SaboteurMap;
//import Saboteur.cardClasses.SaboteurTile;

public class MyTools {
	
	private static final String[] tilesNoDeadEnd = {"0", "5", "5_flip", "6", "6_flip", "7_flip", "8", "9", "9_flip", "10"};
	private static final List<String> tilesNoDeadEndList = Arrays.asList(tilesNoDeadEnd);
	private static final String[] tilesBlocked = {"1", "2", "2_flip", "3", "3_flip", "4", "4_flip", "11", "11_flip","12", 
			"12_flip", "13", "14", "14_flip", "15"};
	private static final List<String> tilesBlockedList = Arrays.asList(tilesBlocked);
	
    public static double getSomething() {
        return Math.random();
    }
    
    public static boolean hasCard(String cardTypeName, ArrayList<SaboteurCard> myDeck) {
    	ArrayList<String> cardsList = getCardsNames(myDeck);
    	for (int i = 0; i < cardsList.size(); i++) {
    		String cardType = cardsList.get(i).split(":")[0];
    		if (cardType.contains(cardTypeName)) return true;   		
    	}
    	return false;
    }
    
    public static ArrayList<String> getCardsNames(ArrayList<SaboteurCard> myDeck){
    	ArrayList<String> cardsList = new ArrayList<String>();
    	for (int i = 0; i < myDeck.size(); i++) {
    		cardsList.add(i, myDeck.get(i).getName());
    	}
    	return cardsList;
    }
    
    public static int nextTileHidden(SaboteurBoardState boardState) {
    	for (int i = 0; i < 3; i++) {
    		SaboteurTile hiddenTile = boardState.getHiddenBoard()[boardState.hiddenPos[i][0]][boardState.hiddenPos[i][1]];
    		if(hiddenTile.getIdx() == "8") {
    			return i;
    		}
    	}  	
    	return 0;
    }
    
    public static int numTileHidden(SaboteurBoardState boardState) {
    	int num = 0;
    	for (int i = 0; i < 3; i++) {
    		SaboteurTile hiddenTile = boardState.getHiddenBoard()[boardState.hiddenPos[i][0]][boardState.hiddenPos[i][1]];
    		if(hiddenTile.getIdx() == "8") {
    			num++;
    		}
    	}  	
    	return num;
    }
    
    public static int Test1(SaboteurMove saboteurMove, SaboteurBoardState boardState, int[] goalXY) {
		int[] moveXY = {saboteurMove.getPosPlayed()[0], saboteurMove.getPosPlayed()[1]};
		SaboteurTile tile = (SaboteurTile) saboteurMove.getCardPlayed();
		String tileIdx = tile.getIdx();
		String[] tilesToLeft= {"6_flip", "7_flip", "8", "9_flip", "10"};
		String[] tilesToRight= {"5_flip", "6", "8", "9", "9_flip", "10"};
		String[] tilesToUp= {"5_flip", "6", "8", "9", "9_flip", "10"};
		String[] tilesToDown= {"5_flip", "6", "8", "9", "9_flip", "10"};
		List<String> tilesToLeftList = Arrays.asList(tilesToLeft);
		List<String> tilesToRightList = Arrays.asList(tilesToRight);
		List<String> tilesToUpList = Arrays.asList(tilesToUp);
		List<String> tilesToDownList = Arrays.asList(tilesToDown);

		
		//Tile is on the same line as the nugget's
		if (moveXY[0] == goalXY[0]) {
			if ((moveXY[1] < goalXY[1]) && !tilesToLeftList.contains(tileIdx)) {
				//System.out.print("Idx: " + tileIdx + "Goes to the left\n");
				return 2;
			}
			if ((moveXY[1] > goalXY[1]) && !tilesToRightList.contains(tileIdx)) {
				//System.out.print("Idx: " + tileIdx + "Goes to the right\n");
				return 2;
			}
		}
//		else if(moveXY[0] < goalXY[0]) {
//			
//			return -1;
//		}
//		else if(moveXY[0] > goalXY[0]) {
//			return -1;
//		}
//		else if(moveXY[1] == goalXY[1]) {
//			return -1;
//		}
//		else if(moveXY[1] < goalXY[1]) {
//			return -1;
//		}
//		else if(moveXY[1] > goalXY[1]) {
//			return -1;
//		}
		
		return 1;
	}
    
    public static double distToTarget(SaboteurBoardState boardState, SaboteurMove move, int[] target) {
    	ArrayList<Double> dist = new ArrayList<Double>();
    	
    	int[] current_pos = move.getPosPlayed();
    	int[][] intboard = boardState.getHiddenIntBoard_corrected();
    	SaboteurCard card = move.getCardPlayed();
    	
    	if(card instanceof SaboteurTile) {
    		SaboteurTile t = (SaboteurTile)card;
    		int[][] originTileInt = t.getPath();
    		int[][] tansTileInt = corrdTranspose(originTileInt);
    		
			for(int i=1;i<3;i++) {
				for(int j=0;j<3;j++) {
					int x = current_pos[0]*3 + i;
    				int y = current_pos[1]*3 + j;
    				
    				int[][] coord = {{x-1,y},{x+1,y},{x,y-1},{x,y+1}};
    				boolean[] b = inCardCoord(current_pos,coord);
    				
    				if(i!=1 && j!=1 && tansTileInt[i][j]==1) {
    					if(intboard[x-1][y]==-1 && !b[0]) {
    						double d = Math.abs((x-1-target[0]*3-1))+Math.abs((y-target[1]*3-1));
    						dist.add(d);
    					}
    					else if(intboard[x+1][y]==-1 && !b[1]) {
    						double d = Math.abs((x+1-target[0]*3-1))+Math.abs((y-target[1]*3-1));
    						dist.add(d);
    					}
    					else if(intboard[x][y-1]==-1 && !b[2]) {
    						double d = Math.abs((x-target[0]*3-1))+Math.abs((y-1-target[1]*3-1));
    						dist.add(d);
    					}
    					else if(intboard[x][y+1]==-1 && !b[3]) {
    						double d = Math.abs((x-target[0]*3-1))+Math.abs((y+1-target[1]*3-1));
    						dist.add(d);
    					}
    				}
				}
				
			}
    		
    	}
    	double d_min = 1000;
    	for(int i=0;i<dist.size();i++) {
    		if(d_min>dist.get(i)) {
    			d_min = dist.get(i);
    		}
    	}
    	return d_min;
    	//return Collections.min(dist);
    }
    
    private static int[][] corrdTranspose(int[][] origin){
    	int[][] trans = origin;
    	
    	for(int i=0;i<3;i++) {
    		for(int j=0;j<3;j++) {
    			trans[i][j] = origin[j][2-i];
    		}
    	}
    	return trans;
    }
    
    private static boolean[] inCardCoord(int[] card_pos, int[][] pos) {
    	boolean[] b = {true,true,true,true};
    	for(int i=0;i<4;i++) {
    		int x = pos[i][0];
    		int y = pos[i][1];
    		b[i] = x>=card_pos[0]*3 && x<=card_pos[0]*3+2 && y>=card_pos[1]*3 && y<=card_pos[1]*3+2;
    	}
    	//boolean w1 = x>=card_pos[0] && x<=card_pos[0]+2 && y>=card_pos[0] && y<=card_pos[1]+2;
    	
    	return b;
    }
    
    public static int isPathConnected(SaboteurBoardState boardState, SaboteurMove move) {

		ArrayList<int[]> originTargets = new ArrayList<>();
        originTargets.add(new int[]{boardState.originPos,boardState.originPos}); //the starting points
        int[] targetPos = {move.getPosPlayed()[0], move.getPosPlayed()[1]};
        if (cardPath(originTargets, targetPos, true, boardState)) {
        	ArrayList<int[]> originTargets2 = new ArrayList<>();
            //the starting points
            originTargets2.add(new int[]{boardState.originPos*3+1, boardState.originPos*3+1});
            originTargets2.add(new int[]{boardState.originPos*3+1, boardState.originPos*3+2});
            originTargets2.add(new int[]{boardState.originPos*3+1, boardState.originPos*3});
            originTargets2.add(new int[]{boardState.originPos*3, boardState.originPos*3+1});
            originTargets2.add(new int[]{boardState.originPos*3+2, boardState.originPos*3+1});
            //get the target position in 0-1 coordinate
            int[] targetPos2 = {targetPos[0]*3+1, targetPos[1]*3+2};
            int[] targetPos3 = {targetPos[0]*3+2, targetPos[1]*3+1};
            int[] targetPos4 = {targetPos[0]*3+1, targetPos[1]*3};
            int[] targetPos5 = {targetPos[0]*3, targetPos[1]*3+1};
            if (cardPath(originTargets2, targetPos2, false, boardState) ||
            		cardPath(originTargets2, targetPos3, false, boardState) ||
            		cardPath(originTargets2, targetPos4, false, boardState) ||
            		cardPath(originTargets2, targetPos5, false, boardState)) {
                return 1;
            }

        }
			
		return 2;	
	}
    
    private static boolean cardPath(ArrayList<int[]> originTargets,int[] targetPos,Boolean usingCard,
			SaboteurBoardState boardState){

        // the search algorithm, usingCard indicate weither we search a path of cards (true) or a path of ones (aka tunnel)(false).
        ArrayList<int[]> queue = new ArrayList<>(); //will store the current neighboring tile. Composed of position (int[]).
        ArrayList<int[]> visited = new ArrayList<int[]>(); //will store the visited tile with an Hash table where the key is the position the board.
        visited.add(targetPos);
        if(usingCard) addUnvisitedNeighborToQueue(targetPos,queue,visited,boardState.BOARD_SIZE,usingCard, boardState);
        else addUnvisitedNeighborToQueue(targetPos,queue,visited,boardState.BOARD_SIZE*3,usingCard, boardState);
        while(queue.size()>0){
            int[] visitingPos = queue.remove(0);
            if(containsIntArray(originTargets,visitingPos)){
                return true;
            }
            visited.add(visitingPos);
            if(usingCard) addUnvisitedNeighborToQueue(visitingPos,queue,visited,boardState.BOARD_SIZE,usingCard, boardState);
            else addUnvisitedNeighborToQueue(visitingPos,queue,visited,boardState.BOARD_SIZE*3,usingCard, boardState);
        }
        return false;
    }
    
    private static void addUnvisitedNeighborToQueue(int[] pos,ArrayList<int[]> queue, ArrayList<int[]> visited,
    		int maxSize,boolean usingCard, SaboteurBoardState boardState){
        int[][] moves = {{0, -1},{0, 1},{1, 0},{-1, 0}};
        int i = pos[0];
        int j = pos[1];
        for (int m = 0; m < 4; m++) {
            if (0 <= i+moves[m][0] && i+moves[m][0] < maxSize && 0 <= j+moves[m][1] && j+moves[m][1] < maxSize) { //if the hypothetical neighbor is still inside the board
                int[] neighborPos = new int[]{i+moves[m][0],j+moves[m][1]};
                if(!containsIntArray(visited,neighborPos)){
                    if(usingCard && boardState.getHiddenBoard()[neighborPos[0]][neighborPos[1]]!=null) queue.add(neighborPos);
                    else if(!usingCard && boardState.getHiddenIntBoard()[neighborPos[0]][neighborPos[1]]==1) queue.add(neighborPos);
                }
            }
        }
    }
    
    private static boolean containsIntArray(ArrayList<int[]> a,int[] o){ //the .equals used in Arraylist.contains is not working between arrays..
        if (o == null) {
            for (int i = 0; i < a.size(); i++) {
                if (a.get(i) == null)
                    return true;
            }
        } else {
            for (int i = 0; i < a.size(); i++) {
                if (Arrays.equals(o, a.get(i)))
                    return true;
            }
        }
        return false;
    }

    public static int blockedTilesToDestroy(SaboteurBoardState boardState, SaboteurMove move) {
	
		int[] posMov = move.getPosPlayed();
		SaboteurTile tileToDestroy = boardState.getHiddenBoard()[posMov[0]][posMov[1]];
		if (tilesBlockedList.contains(tileToDestroy.getIdx()) && move.getPosPlayed()[0] > 5) {
			return 1;
		}
	
		return 2;
    }
    
    public static double CardToDrop(SaboteurBoardState boardState, SaboteurMove move, boolean found_nugget, int BonusNum) {
    	
		int[] posMov = move.getPosPlayed();
		ArrayList<SaboteurCard> cards = boardState.getCurrentPlayerCards();
		SaboteurCard cardToDrop = cards.get(posMov[0]);
		
		if(cardToDrop instanceof SaboteurTile) {
			SaboteurTile tileToDrop = (SaboteurTile)cardToDrop;
			if (tilesBlockedList.contains(tileToDrop.getIdx())) {
				return 1;
			}
		}
		else if(cardToDrop instanceof SaboteurMap && found_nugget) {
			return 1;
		}
		else if(cardToDrop instanceof SaboteurBonus && BonusNum>2) {
			return 1;
		}
		
		return 1.5;
			
    }
    
    public static boolean blockedDrop(SaboteurCard cardToDrop, boolean found_nugget, int BonusNum) {
    	if(cardToDrop instanceof SaboteurTile) {
			SaboteurTile tileToDrop = (SaboteurTile)cardToDrop;
			if (tilesBlockedList.contains(tileToDrop.getIdx())) {
				return true;
			}
		}
    	else if(cardToDrop instanceof SaboteurMap && found_nugget) {
			return true;
		}
    	else if(cardToDrop instanceof SaboteurBonus && BonusNum>2) {
			return true;
		}
    	
    	return false;
    }
 
    
}