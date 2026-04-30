package atividade9.model;

import atividade9.enums.Status;

import java.util.Arrays;

/*Autor: André Vinícius Barros Macambira
 * Nessa classe existem alguns get/set que estão faltando por não fazerem sentido existirem
 * pela lógica do programa.
 */
public class Student {

    private String name;
    private int[] grades;
    private double average;
    private Status status;

    public Student() {
    }

    public Student(String name, int[] grades) {
        this.name = name;
        this.grades = grades;
    }

    public double getAverage() {
        calculateAverage();
        return average;
    }

    public int[] getGrades() {
        return grades;
    }

    public void setGrades(int[] grades) {
        this.grades = grades;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        checksStudentStatus();
        return status;
    }

    public void calculateAverage(){
        average = (double) (grades[0] + grades[1] + grades[2]) / 3;
    }

    public void checksStudentStatus(){
        calculateAverage();
        status = (average >= 50) ? ( (average >= 70)? Status.APROVADO : Status.RECUPERAÇÃO ) : Status.REPROVADO;
    }

    @Override
    public String toString(){
        return name + " | " + "Notas: " + Arrays.toString(grades) + " | " + "Média: " + String.format("%.2f", average) + " | " + status;
    }
}
