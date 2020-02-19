import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

public class Mexico {
	static Scanner in = new Scanner(System.in);
	static Random rand = new Random();

	// max function for two numbers
	public static int max1(int a, int b) {
		if (a > b) {
			return a;
		}
		return b;
	}

	// min function for two numbers
	public static int min1(int a, int b) {
		if (a < b) {
			return a;
		}
		return b;
	}

	// min function for three numbers
	public static int min1(int a, int b, int c) {
		return min1(c, min1(a, b));
	}

	// max function for three numbers
	public static int max1(int a, int b, int c) {
		return max1(c, max1(a, b));
	}

	// evaluates dice roles into score
	public static int score(int a, int b) {
		return max1(a, b) * 10 + min1(a, b);
	}

	// roles for the player who decides max number of roles
	public static int[] rollp1(int p) {
		char choice = 'Y';
		//a and b are dice rolls
		int a = 0;
		int b = 0;
		//c is the number of rolls so far
		int c = 0;
		while (choice == 'Y' || choice == 'y' && c < 3) {
			c++;
			a = dice();
			b = dice();
			System.out.printf("Player %1s\n", p);
			System.out.printf("Your dice roll is %1d and %1d\n", a, b);
			System.out.printf("Your score is %1d\n", score(a, b));
			System.out.println("Would you like to roll again? Y for yes, N for no.");
			choice = in.nextLine().charAt(0);
		}
		//return number of rolls and p1s roll value
		int[] ans = new int[2];
		ans[0] = score(a, b);
		ans[1] = c;
		return ans;
	}

	// rolls for other players
	public static int roll(int n, int p) {
		char choice = 'Y';
		//a b are dice values
		int a = 0;
		int b = 0;
		int numOfRolls = 0;
		while ((choice != 'N' || choice != 'n') && numOfRolls < n) {
			numOfRolls++;
			a = dice();
			b = dice();
			System.out.printf("Roll's Left: %1d\n", n - numOfRolls);
			System.out.printf("Player %1s\n", p);
			System.out.printf("Your dice roll is %1d and %1d\n", a, b);
			System.out.printf("Your score is %1d\n", score(a, b));
			System.out.println("Would you like to roll again? Y for yes, N for no.");
			choice = in.nextLine().charAt(0);
		}
		return score(a, b);
	}

	public static int scoreChange(int a) {
		//eval score if special case
		if ((a == 11) || (a == 22) || (a == 33) || (a == 44) || (a == 55) || (a == 66)) {
			return a + 100;
		} else if (a == 21) {
			return a + 200;
		} else {
			return a;
		}
	}
	// a, b, c, are different players. It first adjust score using score change then returns the player that won
	public static int eval(int a, int b, int c) {
		a = scoreChange(a);
		b = scoreChange(b);
		c = scoreChange(c);
		if (a == min1(a, b, c)) {
			return 1;
		} else if (b == min1(a, b, c)) {
			return 2;
		} else if (c == min1(a, b, c)) {
			return 3;
		}
		return 0;
	}
	//dice helper funtion
	public static int dice() {
		return rand.nextInt(6) + 1;
	}
	//initializer
	public static void main(String[] args) {
		//setups lives
		int players = 3;
		int p1 = 0;
		int p2 = 0;
		int p3 = 0;
		int rolls = 0;
		int p1Lives = 6;
		int p2Lives = 6;
		int p3Lives = 6;
		int[] temp;
		int loser = 1;
		boolean isP1Alive = true;
		//while game is still going
		while (players > 1) {
			rolls = 0;
			//case if player one goes first
			if (loser == 1) {
				if (p1Lives > 0) {
					temp = rollp1(1);
					rolls = temp[1];
					p1 = temp[0];
				} else if (p2Lives > 0) {
					temp = rollp1(2);
					rolls = temp[1];
					p2 = temp[0];
					isP1Alive = false;
					p1 = 999;
				}
				if (p2Lives > 0 && isP1Alive == true) {
					p2 = roll(rolls, 2);
				} else {
					p2 = 999;
				}
				if (p3Lives > 0) {
					p3 = roll(rolls, 3);
				} else {
					p3 = 999;
				}
				//case if p2 goes first
			} else if (loser == 2) {
				if (p1Lives > 0) {
					temp = rollp1(2);
					rolls = temp[1];
					p2 = temp[0];
				} else if (p1Lives > 0) {
					temp = rollp1(1);
					rolls = temp[1];
					p1 = temp[0];
					isP1Alive = false;
					p2 = 999;
				}
				if (p2Lives > 0 && isP1Alive == true) {
					p3 = roll(rolls, 3);
				} else {
					p3 = 999;
				}
				if (p1Lives > 0) {
					p1 = roll(rolls, 1);
				} else {
					p1 = 999;
				}
				//case if p3 goes first
			} else if (loser == 3) {
				if (p3Lives > 0) {
					temp = rollp1(3);
					rolls = temp[1];
					p3 = temp[0];
				} else if (p1Lives > 0) {
					temp = rollp1(1);
					rolls = temp[1];
					p1 = temp[0];
					isP1Alive = false;
					p3 = 999;
				}
				if (p2Lives > 0 && isP1Alive == true) {
					p1 = roll(rolls, 1);
				} else {
					p1 = 999;
				}
				if (p2Lives > 0) {
					p2 = roll(rolls, 2);
				} else {
					p2 = 999;
				}
			}
			//figures who loser is 
			loser = eval(p1, p2, p3);
			System.out.printf("\nPlayer %1s loses\n", loser);
			//adjusts lives accordingly
			if (loser == 1) {
				p1Lives--;
			} else if (loser == 2) {
				p2Lives--;
			} else if (loser == 3) {
				p3Lives--;
			}
			//if player hits zero lives remove fromg ame
			if (p1Lives == 0) {
				players--;
				p1Lives--;
			} else if (p2Lives == 0) {
				players--;
				p2Lives--;
			} else if (p3Lives == 0) {
				players--;
				p3Lives--;
			}
			//pritn score
			System.out.printf("Lives\n");
			System.out.printf("Player 1: %1d\n", p1Lives);
			System.out.printf("Player 2: %1d\n", p2Lives);
			System.out.printf("Player 3: %1d\n\n", p3Lives);
		}
		//once game is over explain rseults
		System.out.println("GAME OVER");
		if (1(p1Lives, p2Lives, p3Lives) == p1Lives) {
			System.out.printf("Winner is Player 1");
		} else if (max1(p1Lives, p2Lives, p3Lives) == p2Lives) {
			System.out.printf("Winner is Player 2");
		} else if (max1(p1Lives, p2Lives, p3Lives) == p3Lives) {
			System.out.printf("Winner is Player 3");
		}

	}

}
