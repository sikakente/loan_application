package org.kente.loans;

public class EmployeeAuthenticator {

    private final static String ADMIN_PASSWORD = "Employ33adm1n";

    public static boolean authenticate(String password){
        return password.equals(ADMIN_PASSWORD);
    }
}
