import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class Game {
    private Scanner scanner;
    private Random random;
    private Display display;
    private static Words words = new Words();
    private String word;
    private boolean[] letterIsGuessed;
    private ArrayList<Character> incorrectGuesses;
    private ArrayList<Character> correctGuesses;    
    private boolean gameover;
    private boolean playAgain;
    private int bodyparts;
    private long runtime;

    public Game() {
        this.display = new Display();
        this.scanner = new Scanner(System.in);
        this.random = new Random();        
        this.playAgain = true;
    }

    public void play() {
        while(playAgain) {            
            initialize();
            start();
            run();
            playAgain = end();            
        }
    }

    private void initialize() {
        words.initData();
        this.display.resetDisplay();
        this.gameover = false;
        int rng = random.nextInt(words.getWordCount());
        this.word = words.getWord(rng);
        initializeGuessBoolean(this.word);
        this.incorrectGuesses = new ArrayList<Character>();
        this.correctGuesses = new ArrayList<Character>();
        this.bodyparts = 0;
    }

    private void initializeGuessBoolean(String word) {
        this.letterIsGuessed = new boolean[word.length()];
        for (int i = 0; i < word.length(); i++) {
            this.letterIsGuessed[i] = false;
        }        
    }

    private void start() {
        System.out.println("-".repeat(50));
        System.out.println("Welcome to Hungman!");
        System.out.println("Enter a letter into the console to start guessing the word.");
        System.out.println("Be careful: once you've guessed seven incorrect letters, you'll be hung!");
        System.out.println("-".repeat(50));
        this.runtime = System.nanoTime();
    }

    private void run() {
        while(!gameover) {            
            display.draw(this.bodyparts, this.word, this.letterIsGuessed, this.incorrectGuesses);
            char inputChar = promptUser();
            checkNewLetter(inputChar);
            checkLose();
            if (!gameover) checkWin();
        }
    }

    private boolean end() {
        System.out.print("\nEnter \"y\" or \"yes\" to play again: ");
        String input = scanner.next().toLowerCase();
        scanner.nextLine();        
        if (input.equals("y") || input.equals("yes")) {            
            return true;
        }
        return false;
    }

    private char promptUser() {        
        while(true) {
            System.out.print("\nEnter a letter: ");
            String input = scanner.next();
            scanner.nextLine();
            char inputChar = input.charAt(0);
            boolean guessed = checkPreviousLetters(inputChar);
            if (guessed) {
                System.out.println("Letter already guessed!");
                continue;
            }
            return inputChar;            
        }
    }

    private void checkWin() {        
        boolean wordIsGuessed = true;
        for (int i = 0; i < this.letterIsGuessed.length; i++) {
            if (this.letterIsGuessed[i] == false) wordIsGuessed = false;
        }
        if (wordIsGuessed) {
            display.draw(this.bodyparts, this.word, this.letterIsGuessed, this.incorrectGuesses);
            this.runtime = System.nanoTime() - runtime;
            runtime /= Math.pow(10, 9);
            System.out.println("\nYou guessed the word in " + runtime + " seconds!");
            this.gameover = true;
        }
    }

    private void checkLose() {
        if (this.bodyparts == 7) {
            display.draw(this.bodyparts, this.word, this.letterIsGuessed, this.incorrectGuesses);
            System.out.println("\nYou're hung!");
            this.gameover = true;            
        }
    }

    private boolean checkPreviousLetters(char letter) {
        return (this.correctGuesses.contains(letter) || this.incorrectGuesses.contains(letter));
    }

    private void checkNewLetter(char letter) {
        boolean correctGuess = false;
        for (int i = 0; i < this.word.length(); i++) {
            if (this.word.charAt(i) == letter) {
                this.letterIsGuessed[i] = true;
                this.correctGuesses.add(letter);
                correctGuess = true;
            }
        }
        if (!correctGuess) {
            this.bodyparts++;
            this.incorrectGuesses.add(letter);
        }
    }
}
