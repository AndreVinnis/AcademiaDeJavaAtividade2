package atividade8;

import atividade8.model.BankingSystem;
import atividade8.model.Client;
import atividade8.model.CurrentAccount;

public class Main {

    public static void main(String[] args) {
        BankingSystem bankingSystem = new BankingSystem("Banco Imaginário");
        Client client1 = new Client("Andre", "Macambira", "12345678955");
        Client client2 = new Client("João", "Cruz", "98765432131");
        CurrentAccount account1, account2;

        System.out.println("Criação das contas:");
        System.out.println("-------------------------------------------------------------");
        account1 = bankingSystem.createAccount(client1);
        account2 = bankingSystem.createAccount(client2);
        System.out.println("-------------------------------------------------------------");
        System.out.println();

        System.out.println("Informações das contas:");
        bankingSystem.showAccountInformation(account1);
        bankingSystem.showAccountInformation(account2);

        System.out.println("Tentativas falhas:");
        System.out.println("-------------------------------------------------------------");
        bankingSystem.realizeWithdraw(account1, 10);
        System.out.println();
        bankingSystem.realizeTransfer(account2, account1, 50);
        System.out.println("-------------------------------------------------------------");
        System.out.println();

        System.out.println("Colocando dinheiro nas contas:");
        System.out.println("-------------------------------------------------------------");
        bankingSystem.realizeDeposit(account1, 100);
        System.out.println();
        bankingSystem.realizeDeposit(account2, 300);
        System.out.println("-------------------------------------------------------------");
        System.out.println();

        System.out.println("Realizando operações nas contas");
        System.out.println("-------------------------------------------------------------");
        bankingSystem.realizeWithdraw(account1, 30);
        System.out.println();
        bankingSystem.realizeTransfer(account2, account1, 80);
        System.out.println("-------------------------------------------------------------");
        System.out.println();

        System.out.println("Informações das contas ao final do programa:");
        bankingSystem.showAccountInformation(account1);
        bankingSystem.showAccountInformation(account2);
    }
}
