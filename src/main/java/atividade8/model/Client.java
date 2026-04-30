package atividade8.model;

/*Autor: André Vinícius Barros Macambira
* Nessa classe existem alguns get/set que estão faltando por não fazerem sentido existirem
* pela lógica do programa.
*/
public class Client {

    private String name;
    private String lastName;
    private String cpf;
    private CurrentAccount account;

    public Client() {
    }

    public Client(String name, String lastName, String cpf) {
        this.name = name;
        this.lastName = lastName;
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CurrentAccount getAccount() {
        return account;
    }

    public void setAccount(CurrentAccount account) {
        this.account = account;
    }

    @Override
    public String toString(){
        return
                "Nome: " + name + " " + lastName + "\n" +
                "CPF: " + cpf + "\n";
    }
}
