import java.util.Random;
import java.util.Scanner;

public class SubGame {
    public static Random GENERATOR;
    public static final Scanner SCAN = new Scanner(System.in);

    // Write auxiliary functions between the following lines


	// ~~~~~START THE GAME~~~~~~~~~

	//13 ROWS	: get&validate level
	public static int getLevel() {
		System.out.println("Please enter level:");
		int level = 0;
		boolean flag = false;
		while (flag == false) {
			level = SCAN.nextInt();
			if (level < 1 || level > 7) {
				flag = false;
				System.out.println("Error: Invalid Level!");
			} else
				flag = true;
		}
		return level;
	}

	//18 ROWS	: Print board
	public static void printBoard(char[][] A) {
		int lastCharAt = 0;
		for (int i = A.length-1; i >= 0; i--) {
			System.out.print(i + " |");
			boolean isEmptyRow=true;
			for (int k = A[i].length-1 ; k>=0&& isEmptyRow; k--) { //check last cell in row to print (not empty)
				if( A[i][k]!=0) {
					isEmptyRow=false;
					lastCharAt=k;
				}else lastCharAt = 0;
			}
			for (int j = 0; j <= lastCharAt&&!isEmptyRow; j++) { //print de facto
				System.out.print(" " + A[i][j]);
			}
			System.out.println("");
		}
		System.out.println("    - - - - - - - - - -");
		System.out.println("    0 1 2 3 4 5 6 7 8 9");
	}

	//4 ROWS	: Print players step
	public static void printStep(char[][] playerView,char [][]comp,boolean QUIT) {
		printBoard(playerView);
		System.out.println("The computer's following table:");
		printBoard(comp);
	}

	//11 ROWS	: Validate location coordinates
	public static boolean isValidLocation(char[][] A, int subLen, int a, int b, int x, int y) {
		if (a < 0 || a > 9 || b < 0 || b > 9 || x < -1 || x > 1 || y < -1 || y > 1 || ((x == 0) && (y == 0)))
			return false;
		else
			for (int i = 0; i < subLen; i++) {
				if ((a + i * x) > 9 || (b + i * y) > 9 || (a + i * x) < 0 || (b + i * y) < 0)
					return false; // exceeds board
				if (A[b + i * y][a + i * x] == '*')
					return false; // spot already taken
			}
		return true;
	}


	//21 ROWS	: Locate players subs
	public static char[][] locateSubs(char[][] A,int[][][] coordinats,int[] subs) {
		printBoard(A);
		boolean validation = false;
		for (int k = 0; k < subs.length; k++) { // iteration for every sub
			validation = false;
			while (validation == false) {
				System.out.println("Enter location for Battleship of size " + subs[k] + ":");
				int a = SCAN.nextInt();
				int b = SCAN.nextInt();
				int x = SCAN.nextInt();
				int y = SCAN.nextInt();
				if (isValidLocation(A, subs[k], a, b, x, y)) {
					for (int i = 0; i < subs[k]; i++) {
						A[b + i * y][a + i * x] = '*';
						coordinats[k][i][0]=a+i*x;	//save X coordinate for specific sub
						coordinats[k][i][1]=b+i*y;	//save Y coordinate for specific sub
					} validation = true;
				} else System.out.println("Error: Incorrect parameters! Please enter location for the Battleship size "+ subs[k] + " again:");
			} if (k<subs.length-1) printBoard(A);
		} System.out.println("All battleships have been located successfully!");
		return A;
	}


	//20 ROWS	: Locate computer's subs
	public static char[][] locateCompSubs(char[][] A,int[][][]coordinats, int[] subs) {
		boolean validation = false;
		for (int k = 0; k < subs.length; k++) { // iteration for every sub
			validation = false;
			while (validation == false) {
				int a = (int)(GENERATOR.nextDouble()*10); // rndm 0-9
				int b = (int)(GENERATOR.nextDouble()*10);// rndm 0-9
				int x = (int)(GENERATOR.nextDouble()*3) - 1; // rndm -1-1
				int y = (int)(GENERATOR.nextDouble()*3) - 1; // rndm -1-1
				if (isValidLocation(A, subs[k], a, b, x, y)) {
					for (int i = 0; i < subs[k]; i++) {
						A[b + i * y][a + i * x] = '*';
						coordinats[k][i][0]=a+i*x;	//save X coordinate for specific sub
						coordinats[k][i][1]=b+i*y;	//save Y coordinate for specific sub
					}
					validation = true;
				}
			}
		}
		return A;
	}

	//9 ROWS	:initiate coordinates 3-d arrays
	public static void initCoordinates (int[][][] A,int [][][] B) {	//initialize coordinates with value different then 0-9
		for (int i=0;i<A.length;i++) {
			for (int j=0;j<A[i].length;j++) {
				for(int k=0;k<A[i][j].length;k++) {
					A[i][j][k] = -12;	//number different from board, helps me later with finding attacked subs
					B[i][j][k] = -12;
				}
			}
		}
	}


	// ~~~~~~ START BATTLE ~~~

	//22 ROWS	: Player's Attack
	public static boolean playerAttack (char[][] compBoard,char[][]playerBoard,char[][]playerView,int[][][]compSubs,int[] subs) {
		boolean QUIT = false, isLegal = false;
		int x=0, y=0 ;
		System.out.println("It's your turn!");
		System.out.println("Enter coordinates for attack:");
		while (isLegal==false&&QUIT==false) {
			y = SCAN.nextInt();
			x = SCAN.nextInt();
			if(x<0||y<0) {
				QUIT=true;
				System.out.println("Error: Invalid input!");
			}
			else if ((x>9||y>9||playerView[x][y]=='V'||playerView[x][y]=='X')) {
				System.out.println("Error: Incorrect parameters! Please enter coordinates for attack again:");		
			} else isLegal=true;
		} if (QUIT==false) {		
			if (compBoard[x][y]=='*') {
				playerView[x][y] = 'V';
				bullsEye(compSubs,x,y);
				QUIT = checkIfDestroyd(compSubs,subs,"player");
			} else playerView[x][y] = 'X';
		} return QUIT;
	}

	//21 ROWS	: Computer's Attack
	public static boolean compAttack (char[][] compBoard,char[][]playerBoard,char[][]playerView,int[][][]playerSubs,int[] subs,int level) {
		boolean QUIT = false;
		for (int l=0;l<level&&QUIT==false;l++) {
			int x=0 ,y=0 ;
			boolean isLegal = false;
			while (isLegal==false) {
				y = (int)(GENERATOR.nextDouble()*10); // rndm 0-9
				x =(int)(GENERATOR.nextDouble()*10); // rndm 0-9
				if ((x>9||y>9||playerBoard[x][y]=='V'||playerBoard[x][y]=='X')) {
				}
				else isLegal=true;
			}
			if (playerBoard[x][y]=='*') {
				playerBoard[x][y] = 'V';
				bullsEye(playerSubs,x,y);
				QUIT = checkIfDestroyd(playerSubs,subs,"comp");
			}
			else playerBoard[x][y] = 'X';
		}
		if (QUIT==false) printStep (playerView,playerBoard,QUIT);
		return QUIT;
	}


	//20 ROWS	: Check if and which sub is destroyd & end game if all destroyd
	public static boolean checkIfDestroyd (int[][][]A,int[] subs,String currentPlayer) {
		for (int i=0;i<A.length;i++) {
			boolean nextSub = false;
			for (int j=0;j<A[i].length&&!nextSub;j++) {
				if (A[i][j][0]!=-12) nextSub=true;		//its ok to check only X coordinate
			}
			if (nextSub==false&&subs[i]!=0) {
				if (currentPlayer.equals("player")) System.out.println("The computer's battleship of size "+ subs[i] + " has been drowned!");
				else if (  currentPlayer.equals("comp")) System.out.println("Your battleship of size "+ subs[i] + " has been drowned!");
				subs[i]=0;		//destroyd sub --> size becomes 0 (then will not check next time)
			}
		} int s=0;
		while (s<subs.length) {		//check all subs destroyd (game end)
			if (subs[s]!=0) return false;
			s++;	
			if (currentPlayer.equals("player")&&s==subs.length) System.out.println("Congratulations! You are the winner of this great battle!");
			else if (  currentPlayer.equals("comp")&&s==subs.length) System.out.println("GAME OVER! You lost...");
			if (s==subs.length) return true;
		}
		return true;
	}

	//8 ROWS	: check if the attack was successful (if sub is hit)
	public static void bullsEye (int[][][] coordinates,int y,int x) {	//if hit sub - change coordinates (to -12)
		for (int i=0;i<coordinates.length;i++) {
			for (int j=0;j<coordinates[i].length;j++) {
				if (coordinates[i][j][0]==x)
					if (coordinates[i][j][1]==y)
						coordinates[i][j][0] = -12;			
			}
		}
	}



	// ############### MAIN ######################

	//21 ROWS	: Main.
	public static void main(String[] args) {
		System.out.println("Enter seed:");
		GENERATOR = new Random(SCAN.nextInt());
		// Write the main function code between the following lines
		// -------------------------------------------------------
		System.out.println("Welcome To The Great Battleship Console Game!");
		boolean QUIT = false;
		int level = getLevel();
		int[] subs = { 2, 3, 3, 4, 5 }; // types of subs
		char[][] compBoard = new char[10][10];
		char[][] playerBoard = new char[10][10];
		char[][] playerView = new char[10][10];
		int [][][] compSubs = new int [5][5][2]; // 2 - [sub index][sub length(max)][x,y]
		int [][][] playerSubs= new int [5][5][2]; //2 - [sub index][sub length(max)][x,y]
		initCoordinates(compSubs,playerSubs);
		locateSubs(playerBoard,playerSubs,subs);
		printStep (playerView,playerBoard,QUIT);
		locateCompSubs(compBoard,compSubs,subs);
		while (QUIT==false) {		
			QUIT = playerAttack(compBoard,playerBoard,playerView,compSubs,subs);
			if (QUIT ==false) QUIT = compAttack(compBoard,playerBoard,playerView,playerSubs,subs,level);
		}
		// -------------------------------------------------------
	}
}


