package EmploymentExchange;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Scanner;

public class CVHandler {
    // Connect with the database
    static String url = "jdbc:h2:file:./data/database/employmentExchange";
    static ConnectionSource connection;

    static {
        try {
            connection = new JdbcConnectionSource(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Get access to the list of Objects=>Table (DAO)
    // First element is class and the second is the type of the primary key
    static Dao<CurriculumVitae, String> listCV;

    static {
        try {
            listCV = DaoManager.createDao(connection, CurriculumVitae.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Scanner keyboard = new Scanner(System.in);

    public CVHandler() throws SQLException {
    }

    public static void main(String[] args) throws Exception {
        menu();
        connection.close();
    }

    public static void menu() throws Exception {
        System.out.println("**************************");
        System.out.println("MENÚ");
        System.out.println("Por favor, ingrese un número para seleccionar la opción de lo que desea hacer:");
        System.out.println("1. Añadir una hoja de vida.");
        System.out.println("2. Mostrar cédula de los aspirantes.");
        System.out.println("3. Buscar aspirante por cédula.");
        System.out.println("4. Buscar aspirante por nombre.");
        System.out.println("5. Ordenar la lista de acuerdo a criterio.");
        System.out.println("6. Ver candidato con más experiencia.");
        System.out.println("7. Ver candidato más joven.");
        System.out.println("8. Contratar candidato.");
        System.out.println("9. Eliminar candidatos con experiencia de trabajo menor a años específicados.");
        System.out.println("10. Mostrar el promedio de edad de los aspirantes.");
        int selection = keyboard.nextInt();
        switch (selection){
            case 1:
                addCV();
                break;
            case 2:
                showCandidatesID();
                break;
            case 3:
                System.out.println("");
                System.out.println("**************************");
                System.out.println("Por favor ingrese la cédula para buscar candidato:");
                keyboard.nextLine();
                String nationalID = keyboard.nextLine();
                searchByID(nationalID);
                break;
            case 4:
                System.out.println("");
                System.out.println("**************************");
                System.out.println("Ingrese el nombre que desea buscar");
                keyboard.nextLine();
                String fullName = keyboard.nextLine();
                searchByName(fullName);
                break;
            case 5:
                sortList();
                break;
            case 6:
                mostExperienced();
                break;
            case 7:
                younger();
                break;
            case 8:
                hire();
                break;
            case 9:
                System.out.println("");
                System.out.println("**************************");
                System.out.println("Por favor ingrese la experiencia mínima que debe tener. Candidatos con experiencia inferior a la requerida serán eliminados.");
                keyboard.nextLine();
                double workExperience = Double.parseDouble(keyboard.nextLine());
                deleteCandidatesBcWorkExperience(workExperience);
                break;
            case 10:
                ageAverage();
                break;

        }
        System.out.println("");
        System.out.println("**************************");
        System.out.println("¿Desea ir al menú o desea cerrar el programa?");
        System.out.println("1. Ir al menú.");
        System.out.println("2. Cerrar el programa.");
        int answer = keyboard.nextInt();
        if (answer == 1 ){
            menu();
        } else {
            connection.close();
        }
    }
    public static void addCV() throws Exception {
        Logger.setGlobalLogLevel(Level.OFF);
        System.out.println("");
        System.out.println("**************************");


        System.out.println("Ingrese los siguientes datos del aspirante. ");
        System.out.println("Nombre completo: ");
        keyboard.nextLine();
        String fullName = keyboard.nextLine();

        System.out.println("Cédula de ciudadanía: ");
        String nationalID = keyboard.nextLine();

        System.out.println("Edad: ");
        int age = Integer.parseInt(keyboard.nextLine());

        System.out.println("Años de experiencia: ");
        double workExperience = Double.parseDouble(keyboard.nextLine());

        System.out.println("Número de teléfono: ");
        String phoneNumber = keyboard.nextLine();

        System.out.println("Profesión: ");
        String profession = keyboard.nextLine();

        //Create the object with data entered
        CurriculumVitae curriculumVitae = new CurriculumVitae(nationalID, fullName, age, workExperience, profession, phoneNumber);
        listCV.create(curriculumVitae);


        System.out.println("");
    }

    public static void showCandidatesID() {
        Logger.setGlobalLogLevel(Level.OFF);
        System.out.println("");
        System.out.println("**************************");

        System.out.println("Las cédulas de los aspirantes son: ");
        System.out.println("");
        int counter = 1;
        for (CurriculumVitae id : listCV) {
            System.out.println("Aspirante " + counter + ". " + id.getNationalID());
            counter = counter+1;
        }
        System.out.println("");
    }

    public static CurriculumVitae searchByID(String nationalID) throws SQLException {
        Logger.setGlobalLogLevel(Level.OFF);
        System.out.println("");

        CurriculumVitae candidate = listCV.queryForId(nationalID);

        if (candidate == null) {
            System.out.println("No existe un candidato con cédula "+nationalID);
        } else {
            System.out.println("Los datos del candidato con cédula "+nationalID+" son:");
            System.out.println("");
            System.out.println("Edad: "+candidate.getAge());
            System.out.println("Profesión: "+candidate.getProfession());
            System.out.println("Años de experiencia: "+candidate.getWorkExperience());
            System.out.println("Número de teléfono: "+candidate.getPhoneNumber());
        }

        return candidate;

    }

    public static CurriculumVitae searchByName(String name) throws SQLException {
        Logger.setGlobalLogLevel(Level.OFF);
        ;

        CurriculumVitae candidate = null;

        for (CurriculumVitae curriculumVitae : listCV){
            if (curriculumVitae.getFullName().equalsIgnoreCase(name)){
                candidate = curriculumVitae;
            }
        }

        if (candidate == null) {
            System.out.println("No existe un candidato con nombre "+name);
        } else {
            System.out.println("Los datos del candidato con nombre "+name+" son:");
            System.out.println("");
            System.out.println("Edad: "+candidate.getAge());
            System.out.println("Profesión: "+candidate.getProfession());
            System.out.println("Años de experiencia: "+candidate.getWorkExperience());
            System.out.println("Número de teléfono: "+candidate.getPhoneNumber());
        }

        return candidate;
    }


    public static void sortList() throws SQLException {
        Logger.setGlobalLogLevel(Level.OFF);

        System.out.println("");
        System.out.println("**************************");
        System.out.println("Ingrese el número del criterio por el que desea organizar la lista:");
        System.out.println("1 - Por años de experiencia.");
        System.out.println("2 - Por edad.");
        System.out.println("3 - Por profesión.");
        keyboard.nextLine();
        String criteria = keyboard.nextLine();


        switch (criteria) {
            case "1":
                for (CurriculumVitae cv : listCV.query(listCV.queryBuilder().orderBy("workExperience", false).prepare())){
                    System.out.println("");
                    System.out.println("**************************");
                    System.out.println("AÑOS DE EXPERIENCIA: "+cv.getWorkExperience());
                    System.out.println("Nombre: "+cv.getFullName());
                    System.out.println("Edad: "+cv.getAge());
                    System.out.println("Profesión: "+cv.getProfession());
                }
                break;
            case "2":
                for (CurriculumVitae cv : listCV.query(listCV.queryBuilder().orderBy("age", true).prepare())) {
                    System.out.println("");
                    System.out.println("**************************");
                    System.out.println("EDAD: " + cv.getAge());
                    System.out.println("Nombre: " + cv.getFullName());
                    System.out.println("Años de experiencia: " + cv.getWorkExperience());
                    System.out.println("Profesión: " + cv.getProfession());
                }
                break;
            case "3":
                for (CurriculumVitae cv : listCV.query(listCV.queryBuilder().orderBy("profession", true).prepare())) {
                    System.out.println("");
                    System.out.println("**************************");
                    System.out.println("PROFESIÓN: " + cv.getProfession());
                    System.out.println("Nombre: " + cv.getFullName());
                    System.out.println("Edad: " + cv.getAge());
                    System.out.println("Años de experiencia: " + cv.getWorkExperience());
                }
                break;
        }
    }

    public static CurriculumVitae mostExperienced(){
        Logger.setGlobalLogLevel(Level.OFF);
        double workExperience = 0;
        CurriculumVitae candidateCV = null;

        for (CurriculumVitae curriculumVitae : listCV){
            if (curriculumVitae.getWorkExperience()> workExperience){
                candidateCV = curriculumVitae;
            }
        }

        if (candidateCV == null){
            System.out.println("No hay candidatos para verificar.");
        } else {
            System.out.println("");
            System.out.println("**************************");
            System.out.println("El candidato con más experiencia es: ");
            System.out.println("Nombre: "+candidateCV.getFullName());
            System.out.println("Edad: "+candidateCV.getAge());
            System.out.println("Años de experiencia: "+candidateCV.getWorkExperience());
            System.out.println("Profesión: "+candidateCV.getProfession());
            System.out.println("Teléfono: "+candidateCV.getPhoneNumber());
        }

        return candidateCV;
    }

    public static CurriculumVitae younger(){
        Logger.setGlobalLogLevel(Level.OFF);
        double age = 9999;
        CurriculumVitae candidateCV = null;

        for (CurriculumVitae curriculumVitae : listCV){
            if (age > curriculumVitae.getAge()){
                age = curriculumVitae.getAge();
                candidateCV = curriculumVitae;
            }
        }

        if (candidateCV == null){
            System.out.println("No hay candidatos para verificar.");
        } else {
            System.out.println("");
            System.out.println("**************************");
            System.out.println("El candidato más joven es: ");
            System.out.println("Nombre: "+candidateCV.getFullName());
            System.out.println("Edad: "+candidateCV.getAge());
            System.out.println("Años de experiencia: "+candidateCV.getWorkExperience());
            System.out.println("Profesión: "+candidateCV.getProfession());
            System.out.println("Teléfono: "+candidateCV.getPhoneNumber());
        }

        return candidateCV;
    }

    public static void hire() throws SQLException {
        System.out.println("");
        System.out.println("**************************");
        System.out.println("Seleccione 1 o 2 dependiendo de cómo desea seleccionar al candidato: ");
        System.out.println("1 Por nombre.");
        System.out.println("2 Por cédula.");
        keyboard.nextLine();

        String busqueda = keyboard.nextLine();
        CurriculumVitae aspirant = null;

        switch (busqueda){
            case "1":
                System.out.println("Ingrese el nombre del candidato:");
                aspirant = searchByName(keyboard.nextLine());
                break;
            case "2":
                System.out.println("Ingrese la cédula del candidato:");
                aspirant = searchByID(keyboard.nextLine());
                break;
        }

        System.out.println("");
        if (aspirant == null){
            System.out.println("No existe ese candidato para contratar");
        } else {
            System.out.println("El candidato "+aspirant.getFullName()+" con cédula de ciudadanía "+aspirant.getNationalID()+" fue contratado, por lo tanto eliminado de la bolsa de empleo.");
            listCV.delete(aspirant);
        }

    }

    public static void deleteCandidatesBcWorkExperience(double workExperience) throws SQLException {
        Logger.setGlobalLogLevel(Level.OFF);
        int counter = 0;

        for (CurriculumVitae curriculumVitae : listCV){
            if (curriculumVitae.getWorkExperience()<workExperience){
                listCV.delete(curriculumVitae);
                counter = counter + 1;
            }
        }

        System.out.println(counter+" candidatos fueron eliminados porque tenían menos de "+workExperience+" años de experiencia.");
    }

    public static double ageAverage() {
        Logger.setGlobalLogLevel(Level.OFF);
        int counter = 0;
        int totalAges = 0;
        double average = 0;

        for (CurriculumVitae curriculumVitae : listCV){
            totalAges = totalAges + curriculumVitae.getAge();
            counter = counter + 1;
        }

        average = (double)totalAges/counter;
        String formattedAverage = String.format("%.2f", average);

        System.out.println("Habían "+counter+" candidatos. El promedio de edad de estos es de "+formattedAverage+" años.");

        return average;
    }


}

