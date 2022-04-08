import java.io.*;
import java.util.Scanner;

public class Questioner {
    static Scanner myReader = null;

    static {
        try {
            File file = new File("trading-info.txt");
            myReader = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static double win = Double.parseDouble(myReader.nextLine());
    static double loss = Double.parseDouble(myReader.nextLine());
    static double spent = Double.parseDouble(myReader.nextLine());
    static double percentT = Double.parseDouble(myReader.nextLine());
    static double sold = Double.parseDouble(myReader.nextLine());
    static double resultT = Double.parseDouble(myReader.nextLine());
    static double money = Double.parseDouble(myReader.nextLine());
    static double feeT = Double.parseDouble(myReader.nextLine());
    static String currency = myReader.nextLine();
    static Boolean saveFile = Boolean.parseBoolean(myReader.nextLine());

    static Integer Menu(int exitLine) {
        do {
            try {
                Scanner scanner = new Scanner(System.in);
                int ans = Integer.parseInt(scanner.nextLine());
                if (ans == 0 || ans == exitLine) return exitLine;
                else if (ans > 0 && ans < exitLine) return ans;
                else throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("Type numbers between 1 and " + exitLine + "!");
            }
        } while (true);
    }

    static Double Double(String Question) {
        do {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println(Question);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Type numbers!");
            }
        } while (true);
    }

    static String String(String Question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(Question);
        return scanner.nextLine();
    }

    static void fileWriter(String filename, String text, String type) {
        if (type.equals("buffer")) {
            try {
                FileWriter writer = new FileWriter(filename);
                BufferedWriter buffer = new BufferedWriter(writer);
                buffer.write(text);
                buffer.close();
            } catch (IOException e) {
                System.out.println("File not found.");
            }
        } else if (type.equals("writer")) {
            try {
                FileWriter myWriter = new FileWriter(filename);
                myWriter.write(text);
                myWriter.close();
            } catch (IOException e) {
                System.out.println("File not found.");
            }
        }
    }

    static void fileReader(String filename) {
        File file = new File(filename);
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    static Integer firstStart() {
        File tradingInfo = new File("trading-info.txt");
        if (!tradingInfo.exists() || Questioner.money == 0) {
            Questioner.fileWriter("trading-info.txt", ("0\n0\n0\n0\n0\n0\n0\n0\nUSD\ntrue"), "writer");
            return 1;
        }
        return 0;
    }
}