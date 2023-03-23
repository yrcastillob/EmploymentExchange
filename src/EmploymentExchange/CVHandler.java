package EmploymentExchange;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.support.ConnectionSource;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
        int selection = JOptionPane.showOptionDialog(null,
                "MENÚ PRINCIPAL \n\n Por favor, Seleccione las opciones: \n\n 1. Añadir hoja de vida.\n 2. Mostrar cédulas.\n 3. Buscar por cédula. \n 4. Buscar por nombre. \n 5. Ordenar lista por criterio.\n 6. Candidato con más experiencia. \n 7. Candidato más joven. \n 8. Contratar candidato. \n 9. Eliminar candidatos por experiencia.\n 10. Promedio edad candidatos.\n\n",
                "Bolsa de Empleo",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null,
                new Object[] {"1","2","3","4","5","6","7","8","9","10"},
                0);
        if (selection == 0){
            addCV();}
        else if (selection == 1){showCandidatesID();}
        else if (selection == 2){
            String nationalID = JOptionPane.showInputDialog("Por favor ingrese la cédula para buscar candidato:");
            searchByID(nationalID);
        }
        else if (selection == 3){
            String fullName = JOptionPane.showInputDialog("Por favor ingrese el nombre que desea buscar:");;
            searchByName(fullName);
        }
        else if (selection == 4) {sortList();}
        else if (selection == 5) {mostExperienced();}
        else if (selection == 6) {younger();}
        else if (selection == 7) {hire();}
        else if (selection == 8) {
            double workExperience = Double.parseDouble(JOptionPane.showInputDialog("Por favor ingrese el número de años de experiencia mínima que debe tener. \nCandidatos con experiencia inferior a la requerida serán eliminados.\n"));
            deleteCandidatesBcWorkExperience(workExperience);
        }
        else if (selection == 9){ageAverage();}

        int answer = JOptionPane.showOptionDialog(null,
                "¿Desea ir al menú o desea cerrar el programa?",
                "Ir al menú",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                new Object[] {"Ir al menu","Cerrar el programa"},
                0);
        if (answer == 0 ){
            menu();
        } else {
            connection.close();
        }
    }
    public static void addCV() throws Exception {
        Logger.setGlobalLogLevel(Level.OFF);
        JOptionPane.showMessageDialog(null,"A continuación se le pedirán los datos del candidato. Por favor ingréselos con cuidado para no cometer errores.","Instrucciones",JOptionPane.INFORMATION_MESSAGE);
        String fullName = JOptionPane.showInputDialog("Nombre completo: ");
        String nationalID = JOptionPane.showInputDialog("Cédula de ciudadanía: ");
        int age = Integer.parseInt(JOptionPane.showInputDialog("Edad: "));
        double workExperience = Double.parseDouble(JOptionPane.showInputDialog("Años de experiencia: "));
        String phoneNumber = JOptionPane.showInputDialog("Número de teléfono: ");
        String profession = JOptionPane.showInputDialog("Profesión: ");

        //Create the object with data entered
        CurriculumVitae candidate = listCV.queryForId(nationalID);

        if (candidate != null) {
            JOptionPane.showMessageDialog(null,"Ya existe un candidato con esa cédula","Error",JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,"Hoja de vida añadida","Operación exitosa",JOptionPane.INFORMATION_MESSAGE);
            CurriculumVitae curriculumVitae = new CurriculumVitae(nationalID, fullName, age, workExperience, profession, phoneNumber);
            listCV.create(curriculumVitae);
        }
    }

    public static void showCandidatesID() throws SQLException {
        Logger.setGlobalLogLevel(Level.OFF);

        String nationalIDs[] = new String[0];
        ArrayList<String> arrayListNationalIDs = new ArrayList<>(Arrays.asList(nationalIDs));
        String message;

        for (CurriculumVitae id : listCV) {
            message = ("NOMBRE:    " + id.getFullName() + "  --->  CÉDULA:    " + id.getNationalID());
            arrayListNationalIDs.add(message);
        }

        nationalIDs = arrayListNationalIDs.toArray(nationalIDs);

        String answer = (String) JOptionPane.showInputDialog(null,"Las cédulas de los candidatos son: ", "Lista cédulas", JOptionPane.INFORMATION_MESSAGE, null, nationalIDs,null);


    }

    public static CurriculumVitae searchByID(String nationalID) throws SQLException {
        Logger.setGlobalLogLevel(Level.OFF);
        System.out.println("");

        CurriculumVitae candidate = listCV.queryForId(nationalID);

        if (candidate == null) {
            JOptionPane.showMessageDialog(null,("No existe un candidato con cédula" +nationalID),"Candidato no encontrado",JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,("Los datos del candidato con cédula "+nationalID+" son:\n\n Nombre: "+candidate.getFullName()+"\n Edad: "+candidate.getAge()+" \n Profesión: "+candidate.getProfession()+"\n Años de experiencia: "+candidate.getWorkExperience()+"\n Número de teléfono: "+candidate.getPhoneNumber()),("Datos del candidato con cédula "+nationalID),JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null,("No existe un candidato con nombre" +name),"Candidato no encontrado",JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,("Los datos del candidato con nombre "+name+" son:\n\n Cédula: "+candidate.getNationalID()+"\n Edad: "+candidate.getAge()+" \n Profesión: "+candidate.getProfession()+"\n Años de experiencia: "+candidate.getWorkExperience()+"\n Número de teléfono: "+candidate.getPhoneNumber()),("Datos del candidato "+name),JOptionPane.INFORMATION_MESSAGE);
        }

        return candidate;
    }


    public static void sortList() throws SQLException {
        Logger.setGlobalLogLevel(Level.OFF);

        String CV[] = new String[0];
        ArrayList<String> arrayListCVs = new ArrayList<>(Arrays.asList(CV));
        String message,criteria = null;
        boolean order;

        int answer = JOptionPane.showOptionDialog(null,
                "Seleccione el criterio por el que desea organizar la lista.",
                "Organizar lista",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                new Object[] {"Años de experiencia","Edad","Profesión"},
                0);

        int orderAnswer = JOptionPane.showOptionDialog(null,
                "Seleccione el orden que quiere utilizar en la lista.",
                "Tipo de orden de lista",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                new Object[] {"Ascendente (menor a mayor o A...Z)","Descendente (mayor a menor o Z...A)"},
                0);

        if (orderAnswer == 0){
            order = true;
        } else {
            order = false;
        }

        if (answer == 0 ){
            for (CurriculumVitae cv : listCV.query(listCV.queryBuilder().orderBy("workExperience", order).prepare())){
                message = ("AÑOS DE EXPERIENCIA: "+cv.getWorkExperience()+" || Nombre: "+cv.getFullName()+" || Cédula: "+cv.getNationalID()+" || Edad: "+cv.getAge()+" || Profesión: "+cv.getProfession());
                arrayListCVs.add(message);
                criteria = "años de experiencia.";
            }
        } else if (answer == 1 ) {
            for (CurriculumVitae cv : listCV.query(listCV.queryBuilder().orderBy("age", order).prepare())) {
                message = ("EDAD: " + cv.getAge()+" || Nombre: "+cv.getFullName()+" || Cédula: "+cv.getNationalID()+" || Profesión: "+cv.getProfession()+" || Años de experiencia: " + cv.getWorkExperience());
                arrayListCVs.add(message);
                criteria = "edad.";
            }
        } else if (answer == 2){
            for (CurriculumVitae cv : listCV.query(listCV.queryBuilder().orderBy("profession", order).prepare())) {
                message = ("PROFESIÓN: " + cv.getProfession()+" || Nombre: "+cv.getFullName()+" || Cédula: "+cv.getNationalID()+" || Edad: "+cv.getAge()+" || Años de experiencia: " + cv.getWorkExperience());
                arrayListCVs.add(message);
                criteria = "profesión.";
            }
        }
        CV = arrayListCVs.toArray(CV);

        String showOrganizedlist = (String) JOptionPane.showInputDialog(null,("La lista organizada por "+criteria+"\n"), "Lista organizada por criterio", JOptionPane.INFORMATION_MESSAGE, null, CV,null);

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
            JOptionPane.showMessageDialog(null,("No hay candidatos para verificar."),"No hay cantidatos",JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,("El candidato con más experiencia es: \n\n"+ " Nombre: "+candidateCV.getFullName()+"\n Cédula de ciudadanía: "+candidateCV.getNationalID()+"\n Edad: "+candidateCV.getAge()+" \n Profesión: "+candidateCV.getProfession()+"\n Años de experiencia: "+candidateCV.getWorkExperience()+"\n Número de teléfono: "+candidateCV.getPhoneNumber()),("Candidato con más experiencia"),JOptionPane.INFORMATION_MESSAGE);
        }

        return candidateCV;
    }

    public static CurriculumVitae younger(){
        Logger.setGlobalLogLevel(Level.OFF);
        double age = 999999999;
        CurriculumVitae candidateCV = null;

        for (CurriculumVitae curriculumVitae : listCV){
            if (age > curriculumVitae.getAge()){
                age = curriculumVitae.getAge();
                candidateCV = curriculumVitae;
            }
        }

        if (candidateCV == null){
            JOptionPane.showMessageDialog(null,("No hay candidatos para verificar."),"No hay cantidatos",JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,("El candidato más joven es: \n\n"+ " Nombre: "+candidateCV.getFullName()+"\n Cédula de ciudadanía: "+candidateCV.getNationalID()+"\n Edad: "+candidateCV.getAge()+" \n Profesión: "+candidateCV.getProfession()+"\n Años de experiencia: "+candidateCV.getWorkExperience()+"\n Número de teléfono: "+candidateCV.getPhoneNumber()),("Candidato más joven"),JOptionPane.INFORMATION_MESSAGE);
        }

        return candidateCV;
    }

    public static void hire() throws SQLException {
        CurriculumVitae aspirant = null;

        int answer = JOptionPane.showOptionDialog(null,
                "Por favor, seleccione cómo desea seleccionar al candidato.",
                "Contratar candidato",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                new Object[] {"Por nombre","Por cédula"},
                0);
        if (answer == 0 ){
            aspirant = searchByName(JOptionPane.showInputDialog("Ingrese nombre del candidato."));
        } else {
            aspirant = searchByID(JOptionPane.showInputDialog("Ingrese cédula del candidato."));
        }

        if (aspirant == null){
            JOptionPane.showMessageDialog(null,("No existe candidato para contratar."),"No hay candidato",JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,("El candidato "+aspirant.getFullName()+" con cédula de ciudadanía "+aspirant.getNationalID()+" fue contratado, por lo tanto eliminado de la bolsa de empleo."),"Candidato contratado",JOptionPane.INFORMATION_MESSAGE);
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

        System.out.println();
        JOptionPane.showMessageDialog(null,(counter+" candidatos fueron eliminados porque tenían menos de "+workExperience+" años de experiencia."),"Candidatos eliminados por tener poca experiencia",JOptionPane.INFORMATION_MESSAGE);
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

        JOptionPane.showMessageDialog(null,("Habían "+counter+" candidatos. El promedio de edad de estos es de "+formattedAverage+" años."),"Promedio de edad de candidatos",JOptionPane.INFORMATION_MESSAGE);

        return average;
    }

}

