import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;



public class Highscore {
	
	
	public void writeHighScore(long score) throws IOException {
		File file = new File("HighScore.txt");
		FileWriter fw = new FileWriter(file, false);
		PrintWriter output = new PrintWriter(fw);
		
		output.print(score);
	}
	
	
	public long readsFile() throws IOException {
		File file = new File("HighScore.txt");
		Scanner inputfile = new Scanner(file);
		
		String line = inputfile.nextLine();
		long highScore = Long.parseLong(line);
		return highScore;
		
	}
	
}
