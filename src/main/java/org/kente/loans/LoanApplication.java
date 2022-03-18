package org.kente.loans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LoanApplication {

    private static final String COMMA_DELIMITER = ",";

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        showLoginMenu();
        int loginMenuOption = scanner.nextInt();

        switch (loginMenuOption) {
            case 1:

                if(login()){
                    List<Loan> listOfLoans = readLoansFromCSV();
                    Loans loans = new Loans(readLoansFromCSV());
                    printMainMenu();
                    int mainMenuOption = scanner.nextInt();
                    handleSelectedMenuOption(mainMenuOption, loans);
                }
                break;
            case 2:
                System.exit(0);
                break;
            default:
                System.out.println("Please select a valid option");
                break;
        }
    }

    private static void showLoginMenu(){
        System.out.println("+---------------------------------------+");
        System.out.println("|  1. Login                             |");
        System.out.println("|  2. Exit                              |");
        System.out.println("+---------------------------------------+");
        System.out.println();
        System.out.println("Please, enter your option:");
    }

    private static void handleSelectedMenuOption(int option, Loans loans){
        switch (option){
            case 1:
                loans.listAllLoanDetails();
                break;
            case 2:
                getAndListLoansByBranchCode(loans);
                break;
            case 3:
                queryLoanByLoanId(loans);
                break;
            case 4:
                loans.showLoanSummaryByBranch();
                break;
            case 5:
                loans.showCurrentMonthBirthdays();
                break;
            case 0:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid Option");
                break;
        }
    }

    private static void getAndListLoansByBranchCode(Loans loans){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the branch code you wish to display:");
        int branchCode = scanner.nextInt();
        loans.listBranchLoans(branchCode);
    }

    private static void queryLoanByLoanId(Loans loans){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the loan ID you want to query:");
        System.out.println();
        String loanId = scanner.next();
        try {
            Loan loan = loans.getLoanByLoanId(loanId);
            PasscodeAuthenticator passcodeAuthenticator = new PasscodeAuthenticator();
            List<Integer> passCodePositions = passcodeAuthenticator.generatePasscodeAuthPositions(loan.getPasscode());
            HashMap<Integer, String> userPasscodeInput = new HashMap<>();
            passCodePositions.forEach(position -> {
                System.out.format("Enter character %d of the loan pass code: %n", position);
                String enteredPasscode = scanner.next();
                userPasscodeInput.put(position, enteredPasscode);
            });
            System.out.println("");
            if (passcodeAuthenticator.authenticate(userPasscodeInput, loan.getPasscode())){
                System.out.println("Pass code accepted!");
                System.out.println();
                loan.prettyPrint();
            }else{
                System.out.println("Invalid pass code entered");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean login(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("+---------------------------------------+");
        System.out.println("Enter your employee password:");
        String password = scanner.next();
        if(EmployeeAuthenticator.authenticate(password)){
            System.out.println("Employee authenticated successfully");
            return true;
        }else{
            System.out.println("Authentication failed");
        }
        return false;
    }

    private static void printMainMenu(){
        System.out.println();
        System.out.println("+---------------------------------------------+");
        System.out.println("|  MAIN MENU                                  |");
        System.out.println("+---------------------------------------------+");
        System.out.println("|  1. List all loan objects                   |");
        System.out.println("|  2. Display loans by branch code            |");
        System.out.println("|  3. Query loan by ID                        |");
        System.out.println("|  4. Display summary by branch               |");
        System.out.println("|  5. Show this month's birthdays             |");
        System.out.println("|  0. Exit                                    |");
        System.out.println("+---------------------------------------------+");
    }

    private static List<Loan> readLoansFromCSV() {
        List<Loan> loans = new ArrayList<Loan>();
        String fileName = "src/main/customermortgage.csv";
        BufferedReader reader = null;
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(fileName));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy");

            while((line = reader.readLine()) != null){
                String[] values = line.split(COMMA_DELIMITER);
                String loanId = values[0];
                String firstName = values[3];
                String middleName = values[2];
                String lastName = values[1];
                Integer branchCode = Integer.parseInt(values[4]);
                Gender gender = values[5].length() == 1 ? Gender.valueOf(values[5]) : Gender.U;
                LocalDate dateOfBirth = LocalDate.parse(values[6].replaceFirst("^0+(?!$)", ""), formatter);
                Double amount = Double.parseDouble(values[7]);
                String phoneNumber = values[8];
                String passcode = values[9];
                Customer customer = new Customer(firstName, middleName, lastName, gender, dateOfBirth, phoneNumber);
                loans.add(new Loan(loanId, customer, passcode, branchCode, amount));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.format("Loaded %d loans%n", loans.size());
        System.out.println();

        return loans;
    }
}
