package atividade9.model;

import atividade9.exceptions.IllegalGradeException;
import atividade9.exceptions.IllegalNameException;

import java.util.Optional;
import java.util.Scanner;

// Autor: André Vinícius Barros Macambira
public class SchoolSystem {

    private Class classes;

    public SchoolSystem() {
    }

    public void createClass(Scanner scannerStrings, Scanner scannerNumbers){
        int numStudents;

        System.out.print("Digite o número de alunos nessa turma: ");
        numStudents = scannerNumbers.nextInt();
        Student[] students = new Student[numStudents];

        for(int i = 0; i < numStudents; i++){
            String name = null;
            int studentGrades[] = new int[3];
            boolean legalName;
            boolean legalGrade;

            do {
               try{
                   System.out.printf("Digite as informações do %d aluno:\n", i+1);
                   System.out.print("Nome: ");
                   name = scannerStrings.nextLine();
                   validateName(name);
                   legalName = true;
               }
               catch (IllegalNameException e){
                   System.out.println(e.getMessage());
                   legalName = false;
               }
            } while (!legalName);

            for(int j = 0; j < studentGrades.length; j ++){
                do {
                    try{
                        System.out.printf("Nota %d: ", j+1);
                        studentGrades[j] = scannerNumbers.nextInt();
                        validateGrade(studentGrades[j]);
                        legalGrade = true;
                    }
                    catch (IllegalGradeException e){
                        System.out.println(e.getMessage());
                        legalGrade = false;
                    }
                } while (!legalGrade);
            }

            Student student = new Student(name, studentGrades);
            students[i] = student;
            System.out.println();
        }

        classes = new Class(students);
    }

    public void showClassReport(){
        System.out.println(classes.generateReport());
    }

    private void validateName(String name) throws IllegalNameException {
        /*
         * Essa solução não é a mais correta. Porém, pela limitação de não poder usar um if e
         * utilizar um operador ternário aqui também não resolve o problema de forma adequada,
         * a solução que encontrei foi de utilizar o switch dessa forma somente para lançar a exceção.
         * Pois, de qualquer modo, eu precisaria validar a palavra para lançar ou não a exceção.
         */
        boolean legalName = name.length() >= 3;
        switch (String.valueOf(legalName)){
            case "false":
                throw new IllegalNameException("O nome precisa ter pelo menos 3 caracteres!");
            default:
                break;
        }
    }

    private void validateGrade(double grade) throws IllegalNameException {
        /*
         * Essa solução não é a mais correta. Porém, pela limitação de não poder usar um if e
         * utilizar um operador ternário aqui também não resolve o problema de forma adequada,
         * a solução que encontrei foi de utilizar o switch dessa forma somente para lançar a exceção.
         * Pois, de qualquer modo, eu precisaria validar a nota para lançar ou não a exceção.
         */
        boolean legalGrade = (grade >= 0 && grade <= 100);
        switch (String.valueOf(legalGrade)){
            case "false":
                throw new IllegalGradeException("A nota inserida precisa ser pelo menos 0 e no máximo 100!");
            default:
                break;
        }
    }
}
