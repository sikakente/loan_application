package org.kente.loans;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Loans {

    private List<Loan> loans;

    private class LoanStats {
        private double total;
        private double average;
        private double malePercentage;
        private double femalePercentage;

        public LoanStats(double total, double average, double malePercentage, double femalePercentage) {
            this.total = total;
            this.average = average;
            this.malePercentage = malePercentage;
            this.femalePercentage = femalePercentage;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public double getAverage() {
            return average;
        }

        public void setAverage(double average) {
            this.average = average;
        }

        public double getMalePercentage() {
            return malePercentage;
        }

        public void setMalePercentage(double malePercentage) {
            this.malePercentage = malePercentage;
        }

        public double getFemalePercentage() {
            return femalePercentage;
        }

        public void setFemalePercentage(double femalePercentage) {
            this.femalePercentage = femalePercentage;
        }
    }

    public Loans(List<Loan> loans) {
        this.loans = loans;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    /**
     * List all loans of a given branch
     * @param branchCode
     */
    public void listBranchLoans(int branchCode){
        //filter all loans by branchcode
        System.out.format("These are the loans of branch %d: %n", branchCode);
        loans.stream()
                .filter(loan -> loan.getBranchCode() == branchCode)
                .forEach(loan -> {
                    Customer customer = loan.getCustomer();
                    System.out.format("Loan %s for customer %s %s %s %n",
                            loan.getId(), customer.getFirstName(), customer.getMiddleName(), customer.getLastName());
                });
    }

    /**
     * Gets a loan given a loandId
     * @param loanId String
     * @return Loan
     */
    public Loan getLoanByLoanId(String loanId) throws Exception {
        return loans.stream()
                .filter(loan -> loan.getId().equals(loanId))
                .limit(1)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Loan not found"));
    }

    public void listAllLoanDetails(){
        loans.forEach(System.out::println);
    }

    public void listLoanDetailsByLoanId(String loanId){
        loans.stream()
                .filter(loan -> loan.getId().equals(loanId))
                .forEach(System.out::println);
    }

    public void showLoanSummaryByBranch(){
        //Group by branch
        Map<Integer, Loans.LoanStats> loansByBranch = loans.stream()
                .collect(Collectors.groupingBy(Loan::getBranchCode,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            double total = list.stream()
                                    .map(Loan::getAmount)
                                    .mapToDouble(Double::doubleValue)
                                    .sum();

                            double average = list.stream()
                                    .map(Loan::getAmount)
                                    .mapToDouble(Double::doubleValue)
                                    .average()
                                    .orElseGet(() -> 0.0);

                            long maleCustomers = list.stream()
                                    .filter(loan -> loan.getCustomer().getGender().equals(Gender.M))
                                    .count();

                            long femaleCustomers = list.size() - maleCustomers;

                            double percentageOfMaleCustomers = maleCustomers / (list.size() * 1.0);
                            double percentageOfFemaleCustomers = femaleCustomers / (list.size() * 1.0);

                            return new Loans.LoanStats(total, average,
                                    percentageOfMaleCustomers, percentageOfFemaleCustomers);
                        })));

        loansByBranch.forEach((branchCode, loanStats) ->
                System.out.format("Branch %6d | Average: %15.2f | Total Loan Amount: %15.2f | %%Males: %3.2f | %%Females: %3.2f %n",
                        branchCode, loanStats.getAverage(), loanStats.getTotal(),
                loanStats.getMalePercentage()*100, loanStats.getFemalePercentage()*100));

    }

    public void showCurrentMonthBirthdays(){
        LocalDate today = LocalDate.now();
        Month currentMonth = today.getMonth();
        System.out.format("The customers born in %s are:%n", currentMonth.toString());
        loans.stream()
                .filter(loan -> loan.getCustomer().getDateOfBirth().getMonth().equals(currentMonth))
                .forEach(loan -> {
                    Customer customer = loan.getCustomer();
                    System.out.format("Name: %15s, %s, %15s  | Birthday: %s%n",
                            customer.getFirstName(), customer.getMiddleName(), customer.getLastName(),
                            customer.getDateOfBirth().toString());
                });
    }


}
