public class HelloJava {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Binance Master");
            if (Questioner.firstStart() == 1) settings();
            System.out.println("Money - " + (String.format("%.2f", Questioner.money)) + "\n");
            System.out.println("1.Trading\n2.Trading History\n3.Strategy info\n4.Settings\n5.Exit");
            int menu = Questioner.Menu(5);
            if (menu == 1) trading();
            else if (menu == 2) tradingHistory();
            else if (menu == 3) {
                try {
                    strategyInfo();
                } catch (Exception e) {
                    System.out.println("Please restart the program");
                }
                break;
            }
            else if (menu == 4) settings();
            else if (menu == 5) break;
        }
    }

    private static void trading() {
        while (true) {
            System.out.println("""
                    1.Spot trading 0.1%/0.1%
                    2.Spot trading 0.075%/0.075% BNB25%Off
                    3.Futures trading USDT 0.02%/0.04%
                    4.Futures trading USDT 0.018%/0.036% BNB10%Off
                    5.Futures trading BUSD -0.01/0.023
                    6.Futures trading BUSD -0.01%/0.0207% BNB10%Off
                    7.Custom fees
                    8.Menu""");
            int choice = Questioner.Menu(8);
            if (choice == 8) break;
            choice--;
            double[][] fees = {{0.1, 0.1, 0.002}, {0.075, 0.075, 0.0015}, {0.02, 0.04, 0.0006}, {0.018, 0.036, 0.00054}, {-0.01, 0.023, 0.00013}, {-0.01, 0.0207, 0.00011}, {0, 0, 0}};
            while (true) {
                if (choice == 6 && (fees[6][0] == 0 || fees[6][1] == 0)) {
                    fees[6][0] = Questioner.Double("What is your brokers maker fee? [%]");
                    fees[6][1] = Questioner.Double("What is your brokers taker fee? [%]");
                }
                double buyPrice, buyFor, sellPrice, minPrice, percent, result, fee, soldFor, maker = fees[choice][0], taker = fees[choice][1];
                buyPrice = Questioner.Double("Buy price: ");
                if (buyPrice == 0) break;
                buyFor = Questioner.Double("Spent: ");
                if (buyFor > Questioner.money) {
                    System.out.println("You don't have enough money.");
                    settings();
                    break;
                }
                if (buyFor == 0) break;
                minPrice = buyPrice + (buyPrice * fees[choice][2]);
                if (choice != 6) System.out.printf("Minimal sell: %.3f%n", minPrice);
                sellPrice = Questioner.Double("Sell price: ");
                if (sellPrice == 0) break;
                fee = buyPrice * (maker / 100) + sellPrice * (taker / 100);
                percent = ((sellPrice - buyPrice - fee) / buyPrice) * 100;
                result = percent / 100 * buyFor;
                boolean positive = percent >= 0;
                String word = (positive) ? "Profit:" : "Loss:";
                String word1 = (positive) ? "+" : "";
                System.out.printf("%s %.3f %% %nResult: %s%.2f %s\n", word, percent, word1, result, Questioner.currency);
                if (Questioner.saveFile) {
                    Questioner.fileWriter("trading-history.txt", ((Questioner.String("What pair are u trading? ")) + "\t" + buyPrice + " " + Questioner.currency + "\t" + buyFor + Questioner.currency + "\t" + sellPrice + " " + Questioner.currency + "\t" + word1 + String.format("%.2f", percent) + "%\t" + word1 + String.format("%.2f", result) + " " + Questioner.currency + "\t" + fees[choice][0] + "%/" + fees[choice][1] + "%" + "\t" + String.format("%.3f", fee) + " " + Questioner.currency + "\n"), "buffer");
                    Questioner.spent += buyFor;
                    Questioner.percentT += percent;
                    soldFor = buyFor + result;
                    Questioner.sold += soldFor;
                    Questioner.resultT += result;
                    Questioner.money += result;
                    Questioner.feeT += fee;
                    if (positive) Questioner.win++;
                    else Questioner.loss++;
                    String text = Questioner.win + "\n" + Questioner.loss + "\n" + Questioner.spent + "\n" + Questioner.percentT + "\n" + Questioner.sold + "\n" + Questioner.resultT + "\n" + Questioner.money + "\n" + Questioner.feeT + "\n" + Questioner.currency + "\n" + Questioner.saveFile;
                    Questioner.fileWriter("trading-info.txt", text, "writer");
                }
            }
        }
    }

    private static void tradingHistory() {
        while (true) {
            System.out.println("  Pair    Buy        Spent    Sell      Percent    Result    Fee %            Fee");
            Questioner.fileReader("trading-history.txt");
            System.out.println("""
                    1.Delete history
                    2.Menu""");
            int menu = Questioner.Menu(2);
            if (menu == 1) Questioner.fileWriter("trading-history.txt", "", "writer");
            else if (menu == 2) break;
        }
    }

    private static void strategyInfo() throws Exception {
        while (true) {
            System.out.println("Trades done: " + (Questioner.win + Questioner.loss));
            System.out.print("Win rate: ");
            if ((Questioner.win + Questioner.loss) > 0) {
                System.out.println((Questioner.win / (Questioner.win + Questioner.loss) * 100));
            } else System.out.println();
            System.out.println("Total volume: " + (String.format("%.2f", (Questioner.spent + Questioner.sold))) + " " + Questioner.currency);
            System.out.println("Total percentage: " + (String.format("%.2f", Questioner.percentT)) + " %");
            System.out.println("Total Fee: " + (String.format("%.2f", Questioner.feeT)) + " " + Questioner.currency);
            System.out.println("Money result: " + (String.format("%.2f", Questioner.resultT)) + " " + Questioner.currency);
            System.out.println("You have: " + (String.format("%.2f", Questioner.money)) + " " + Questioner.currency);
            System.out.println("""            
                    1.Reset default settings and delete strategy info
                    2.Menu""");
            int menu = Questioner.Menu(2);
            if (menu == 1) {
                Questioner.fileWriter("trading-info.txt", ("0\n0\n0\n0\n0\n0\n0\n0\nUSD\ntrue"), "writer");
                throw new Exception();
            }
            else if (menu == 2) break;
        }
    }

    private static void settings() {
        while (true) {
            System.out.println("Choose between: 1-4");
            System.out.println("1.Money - " + (String.format("%.2f", Questioner.money)));
            System.out.println("2.Currency - " + Questioner.currency);
            System.out.println("3.Save to history - " + Questioner.saveFile);
            System.out.println("4.Menu");
            int menu = Questioner.Menu(4);
            if (menu == 1) Questioner.money = Questioner.Double("How much money you have? ");
            else if (menu == 2) Questioner.currency = Questioner.String("What will be your default currency? ");
            else if (menu == 3) Questioner.saveFile = !Questioner.saveFile;
            else if (menu == 4) break;
            Questioner.fileWriter("trading-info.txt", (Questioner.win + "\n" + Questioner.loss + "\n" + Questioner.spent + "\n" + Questioner.percentT + "\n" + Questioner.sold + "\n" + Questioner.resultT + "\n" + Questioner.money + "\n" + Questioner.feeT + "\n" + Questioner.currency + "\n" + Questioner.saveFile), "writer");
        }
    }
}