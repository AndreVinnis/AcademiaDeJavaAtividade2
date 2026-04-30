package atividade9;

import atividade9.model.SchoolSystem;
import java.util.Scanner;

// André Vinícius Barros Macambira
public class Main {

    public static void main(String[] args) {
        Scanner scannerStrings = new Scanner(System.in);
        Scanner scannerNumbers = new Scanner(System.in);
        SchoolSystem schoolSystem = new SchoolSystem();
        schoolSystem.createClass(scannerStrings, scannerNumbers);
        schoolSystem.showClassReport();
        scannerStrings.close();
        scannerNumbers.close();
    }
}
