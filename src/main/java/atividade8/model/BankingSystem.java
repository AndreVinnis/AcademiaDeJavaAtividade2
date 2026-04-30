package atividade8.model;

import atividade8.exceptions.UnavailabreLimit;

import java.util.Date;

/*Autor: André Vinícius Barros Macambira
 * Nessa classe existem alguns get/set que estão faltando por não fazerem sentido existirem
 * pela lógica do programa.
 */
public class BankingSystem {

    private String name;
    private long lastNumberAccount;

    public BankingSystem() {
    }

    public BankingSystem(String name) {
        this.name = name;
        lastNumberAccount = 1L;
    }

    public String getName() {
        return name;
    }

    public CurrentAccount createAccount(Client client){
        CurrentAccount newAccount = new CurrentAccount(lastNumberAccount, client, new Date());
        client.setAccount(newAccount);
        lastNumberAccount ++;
        System.out.println("Conta criada com sucesso.");
        return newAccount;
    }

    public void showAccountInformation(CurrentAccount account){
        System.out.println(account.getStatement());
    }

    public void realizeDeposit(CurrentAccount account, double amount){
        account.deposit(amount);
        System.out.printf("Deposito de %.2f$ realizado com sucesso.\n", amount);
        System.out.printf("Valor atual na conta final %d é de: %.2f$\n", account.getNumber(), account.getBalance());
    }

    public void realizeWithdraw(CurrentAccount account, double amount){
        try{
            account.withdraw(amount);
            System.out.printf("Saque de %.2f$ realizado com sucesso.\n", amount);
            System.out.printf("Valor atual na conta final %d é de: %.2f$\n", account.getNumber(), account.getBalance());
        }
        catch (UnavailabreLimit e){
            System.out.printf("Não foi possível realizar o saque de %.2f$ devido: ", amount);
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            System.out.printf("Não foi possível realizar o saque de %.2f$ devido: ", amount);
            System.out.println("Houve um erro inesperado no sistema.");
        }
    }

    public void realizeTransfer(CurrentAccount originAccount, CurrentAccount destinationAccount, double amount){
        try{
            originAccount.transfer(amount);
            destinationAccount.deposit(amount);
            System.out.printf("Transferência de %.2f$ realizado com sucesso.\n", amount);
            System.out.printf("Valor atual na conta de origem final %d é de: %.2f$\n", originAccount.getNumber(), originAccount.getBalance());
            System.out.printf("Valor atual na conta de destino final %d é de: %.2f$\n", destinationAccount.getNumber(), destinationAccount.getBalance());
        }
        catch (UnavailabreLimit e){
            System.out.printf("Não foi possível realizar a transferência de %.2f$ devido: ", amount);
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            System.out.printf("Não foi possível realizar a transferência de %.2f$ devido: ", amount);
            System.out.println("Houve um erro inesperado no sistema.");
        }
    }
}
