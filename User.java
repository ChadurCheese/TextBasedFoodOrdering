import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
// add guest login if nececesary

public class User {

    static Scanner sc;
    static Scanner input;

    public static void login() throws FileNotFoundException {
        sc = new Scanner(new File("src/resources/users.txt"));
        input = new Scanner(System.in);
        HashMap<String, String> userInfo = new HashMap<String, String>();

        // Read file and store in HashMap. Each line contains a username and password seperated by colin ex. "username:password"
        while (sc.hasNextLine()) {
            try {
                String[] line = sc.nextLine().split(":");
                userInfo.put(line[0], line[1]);
            }
            catch(ArrayIndexOutOfBoundsException e) {
                String[] line = sc.nextLine().split(":");
                userInfo.put(line[0], line[1]);
            }
        }
        
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();
        
        
        if (userInfo.containsKey(username) && userInfo.get(username).equals(password)) {
            System.out.println("Logged in, entering main menu...");
        }
        else {
            System.out.println("Invalid username or password");
            String userInput = "";
            //ask user if they want to try again or create an account
            while (!userInput.equals("1") && !userInput.equals("2")) {
                System.out.println("1. Try again");
                System.out.println("2. Create an account");
                userInput = input.nextLine();
            }
            if (userInput.equals("1")) {
                login();
            }
            else {
                signUp();
                login();
            }
        }  
    }    

    public static void signUp() {
        input = new Scanner(System.in);
        System.out.print("Create your username: ");
        String user = input.nextLine();
        System.out.print("Create your password: ");
        String pass = input.nextLine();
        

        try {
            File users = new File("src/resources/users.txt");
            PrintWriter pw = new PrintWriter(new FileWriter(users, true));
            // write to file in a new line
            pw.print("\n" + user + ":" + pass);
            pw.close();
            System.out.println("Account created, you can now login");
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}