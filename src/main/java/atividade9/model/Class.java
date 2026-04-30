package atividade9.model;

import atividade9.enums.Status;

// Autor: André Vinícius Barros Macambira
public class Class {

    private Student[] students;
    private double higherAverage;
    private double lowerAverage;
    private double generalAverage;
    private int numApprovedStudents;
    private int numRetakes;
    private int numFailedStudents;


    public Class() {
    }

    public Class(Student[] students) {
        this.students = students;
        numApprovedStudents = 0;
        numRetakes = 0;
        numFailedStudents = 0;
    }

    public Student[] getStudents() {
        return students;
    }

    public void setStudents(Student[] students) {
        this.students = students;
    }

    public String generateReport(){
        processData();

        String report = "Relatório Individual:\n";
        report += "-------------------------------------------------------------\n";
        for (Student student : students) {
            student.checksStudentStatus();
            report += student.toString() + "\n";
        }
        report += "-------------------------------------------------------------\n";
        report += "\n";

        report += "Estatísticas da Turma:\n";
        report += "-------------------------------------------------------------\n";
        report += "Maior média: " + String.format("%.2f", higherAverage) + "\n";
        report += "Menor média: " + String.format("%.2f", lowerAverage) + "\n";
        report += "Média geral da turma: " + String.format("%.2f", generalAverage) + "\n";
        report += "-------------------------------------------------------------\n";
        report += "\n";

        report += "Distribuição de Resultados:\n";
        report += "-------------------------------------------------------------\n";
        report += "Aprovados: " + numApprovedStudents + "\n";
        report += "Recuperação: " + numRetakes + "\n";
        report += "Reprovados: " + numFailedStudents + "\n";
        report += "-------------------------------------------------------------\n";
        report += "\n";

        report += "Melhor(es) Aluno(s):\n";
        report += "-------------------------------------------------------------\n";
        report += returnStudentsWithHigherAverage() + "\n";
        report += "-------------------------------------------------------------\n";

        return report;
    }

    private void processData(){
        higherAverage = students[0].getAverage();
        lowerAverage = students[0].getAverage();
        double sumStudentsAvarage = 0;

        for (Student student : students) {
            higherAverage = (student.getAverage() > higherAverage) ? student.getAverage() : higherAverage;
            lowerAverage = (student.getAverage() < lowerAverage) ? student.getAverage() : lowerAverage;
            sumStudentsAvarage += student.getAverage();
            numApprovedStudents += (student.getStatus() == Status.APROVADO) ? 1 : 0;
            numRetakes += (student.getStatus() == Status.RECUPERAÇÃO) ? 1 : 0;
            numFailedStudents += (student.getStatus() == Status.REPROVADO) ? 1 : 0;

        }
        generalAverage = sumStudentsAvarage / students.length;
    }

    private String returnStudentsWithHigherAverage(){
        String result = "| ";
        for (Student student : students){
            result += (student.getAverage() == higherAverage) ? (student.getName() + " | ") : "";
        }
        return result;
    }
}
