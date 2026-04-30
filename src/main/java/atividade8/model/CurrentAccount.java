package atividade8.model;

import atividade8.exceptions.UnavailabreLimit;
import java.util.Date;

/*Autor: André Vinícius Barros Macambira
 * Nessa classe existem alguns get/set que estão faltando por não fazerem sentido existirem
 * pela lógica do programa.
 */
public class CurrentAccount {

    private long number;
    private Client owner;
    private double balance;
    private Date creationDate;

    public CurrentAccount() {
    }

    public CurrentAccount(long number, Client owner, Date creationDate) {
        this.number = number;
        this.owner = owner;
        this.creationDate = creationDate;
        balance = 0D;
    }

    public double getBalance() {
       return balance;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public long getNumber() {
        return number;
    }

    public Client getOwner() {
        return owner;
    }

    public void deposit(double amount){
        balance += amount;
    }

    public void withdraw(double amount){
        if(amount > balance){
            throw new UnavailabreLimit("Limite indisponível para essa operação!");
        }
        balance -= amount;
    }

    public void transfer(double amount){
        if(amount > balance){
            throw new UnavailabreLimit("Limite indisponível para essa operação!");
        }
        balance -= amount;
    }

    public String getStatement(){
        return
                "-------------------------------------------------------------\n" +
                        "Dono da conta: " + "\n" +
                        owner.toString() +
                        "Número da conta: " + number + "\n" +
                        "Saldo: " + String.format("%.2f", balance) + "\n" +
                        "Data de criação: " + creationDate + "\n" +
                "-------------------------------------------------------------\n";
    }
}
