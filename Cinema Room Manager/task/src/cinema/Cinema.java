package cinema;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

class CinemaStats {
    static int numberOfPurchasedTickets = 0;
    static double percentagePurchased = 0.00;
    static int currentIncome = 0;
    static int totalSeats = 0;
    static int totalIncome = 0;
}

public class Cinema {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Write your code here
        System.out.println("Enter the number of rows: ");
        int rows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row: ");
        int columns = scanner.nextInt();
        String[][] cinema = buildCinema(rows, columns);

        // Need a loop for Menu Interaction:
        boolean exitOptionPressed = false;
        boolean violationsDetected = true;

        while (!exitOptionPressed) {
            displayMenuSelection();
            int input = scanner.nextInt();
            switch (input) {
                case 1:
                    System.out.println("Cinema: ");
                    displayCinema(cinema);
                    break;
                case 2:
                    while(violationsDetected) {
                        // Prompt for Row Number:
                        System.out.println("Enter a row number:");
                        int rowNumber = scanner.nextInt();
                        // Prompt for Seat Number (Column):
                        System.out.println("Enter a seat number in that row:");
                        int seatNumber = scanner.nextInt();
                        boolean outOfBoundsCheck = checkOutOfBoundsSelection(rowNumber, seatNumber, rows, columns);
                        boolean seatAlreadyBought = true;
                        if (outOfBoundsCheck) {
                            System.out.println("Wrong input!");
                        }
                        if (!outOfBoundsCheck) {
                            seatAlreadyBought = checkIfBought(rowNumber, seatNumber, cinema);
                            if (seatAlreadyBought) {
                                System.out.println("That ticket has already been purchased!");
                            }
                        }
                        if(!outOfBoundsCheck && !seatAlreadyBought){
                            purchaseSeat(rowNumber, seatNumber, cinema, rows, columns);
                            violationsDetected = false;
                        }
                    }
                    violationsDetected = true;
                    break;
                case 3:
                    showStatistics();
                    break;
                case 0:
                    exitOptionPressed = true;
                    break;
                default:
            }
        }
    }

    /* ===== DISPLAY METHODS ===== */
     
    public static void displayCinema(String[][] cinema) {
        for (String[] arr : cinema) {
            for (String value : arr) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    // The statistics for current theater:
    private static void showStatistics() {
        DecimalFormat df = new DecimalFormat("##0.00");
        System.out.println("Number of purchased tickets: " + CinemaStats.numberOfPurchasedTickets);
        System.out.println("Percentage: " + df.format(round(CinemaStats.percentagePurchased * 100.00, 2)) + "%");
        System.out.println("Current income: $" + CinemaStats.currentIncome);
        System.out.println("Total income: $" + CinemaStats.totalIncome);
    }

    // The User Interface:
    private static void displayMenuSelection() {
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
    }

    public static int checkAndReturnSeatPrice(int rowNumber, int rows, int columns) {

        int ticketPrice = 0;
        int totalSeats = countSeats(rows, columns);

        if (totalSeats <= 60) {
            ticketPrice = 10;
        } else {
            if (rowNumber <= Math.floor((double) rows / (double) 2)) {
                ticketPrice = 10;
            }
            if (rowNumber > Math.floor((double) rows / (double) 2)) {
                ticketPrice = 8;
            }
        }
        return ticketPrice;
    }

    // Purchasing and updating Cinema Stats global values:
    public static void purchaseSeat(int rowNumber, int seatNumber, String[][] cinema, int cinemaRows, int cinemaColumns) {
        cinema[rowNumber][seatNumber] = "B";
        int seatPrice = checkAndReturnSeatPrice(rowNumber, cinemaRows, cinemaColumns);
        CinemaStats.numberOfPurchasedTickets++;
        CinemaStats.currentIncome += seatPrice;
        CinemaStats.percentagePurchased = (double) CinemaStats.numberOfPurchasedTickets / (double) CinemaStats.totalSeats;
        System.out.println("Ticket price: $" + seatPrice);
    }

    // Exception Handling Method for already Purchased seat:
    public static boolean checkIfBought(int rowNumber, int seatNumber, String[][] cinema) {
        return cinema[rowNumber][seatNumber].equals("B");
    }

    // Exception Handling method for choosing an incorrect seat:
    public static boolean checkOutOfBoundsSelection(int rowNumber, int seatNumber, int builtCinemaRows, int builtCinemaCols) {
        return rowNumber < 0 || seatNumber < 0 || rowNumber > builtCinemaRows || seatNumber > builtCinemaCols;
    }

    // For counting all the seats
    public static int countSeats(int rows, int columns) {
        return rows * columns;
    }

    // This is to calculate and determine the total income
    public static int calculateTotalIncome(int rows, int columns) {
        // Sold:
        int totalSeats = rows * columns;
        int totalIncome;

        if (totalSeats < 60) {
            int ticketPrice = 10;
            totalIncome = ticketPrice * totalSeats;
        } else {
            int frontTicketPrice = 10;
            int backTicketPrice = 8;
            int frontRows = (rows / 2) * columns;
            int backRows = ((rows - (rows / 2)) * columns);
            totalIncome = ((frontRows * frontTicketPrice) + (backRows * backTicketPrice));
        }
        return totalIncome;
    }


    // This is for building the initial cinema. Used once.
    public static String[][] buildCinema(int rows, int columns) {

        String[][] cinema = new String[rows + 1][columns + 1];

        int rowCounter = 1;
        int columnCounter = 1;

        for (int r = 0; r < cinema.length; r++) {
            for (int c = 0; c < cinema[r].length; c++) {
                if (r == 0 && c == 0) {
                    cinema[0][0] = " ";
                } else if (r == 0 && c > 0) {
                    cinema[r][c] = String.valueOf(rowCounter);
                    rowCounter++;
                } else if (r > 0 && c == 0) {
                    cinema[r][c] = String.valueOf(columnCounter);
                    columnCounter++;
                } else {
                    cinema[r][c] = "S";
                }
            }
        }

        CinemaStats.totalSeats = countSeats(rows, columns);
        CinemaStats.totalIncome = calculateTotalIncome(rows, columns);

        return cinema;
    }

    // This is for displaying the cinema we have built using buildCinema() method.


    public static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
