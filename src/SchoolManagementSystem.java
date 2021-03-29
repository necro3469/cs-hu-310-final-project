import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This application will keep track of things like what classes are offered by
 * the school, and which students are registered for those classes and provide
 * basic reporting. This application interacts with a database to store and
 * retrieve data.
 */
public class SchoolManagementSystem {

	static Database database;
	static String bars = "--------------------------------------------------------------------------------";
	
    public static void getAllClassesByInstructor(String first_name, String last_name) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = database.getDatabaseConnection();
        	sqlStatement = connection.createStatement();
        	ResultSet rs = sqlStatement.executeQuery("SELECT instructors.first_name, instructors.last_name, academic_titles.title, classes.code, classes.name AS class_name, terms.name "
        			+ "FROM instructors "
        			+ "LEFT JOIN academic_titles ON academic_titles.academic_title_id = instructors.academic_title_id "
        			+ "LEFT JOIN class_sections ON class_sections.instructor_id = instructors.instructor_id "
        			+ "LEFT JOIN classes ON classes.class_id = class_sections.class_id "
        			+ "LEFT JOIN terms ON class_sections.term_id = terms.term_id "
        			+ "WHERE instructors.first_name = " + "\"" + first_name + "\"" + " AND instructors.last_name = " + "\"" + last_name + "\"");
        	System.out.println("First Name | Last Name | Title | Code | Name | Term");
    		System.out.println(bars);
        	while(rs.next()) {
        		String firstName = rs.getString("first_name");
        		String lastName = rs.getString("last_name");
        		String title = rs.getString("title");
        		String code = rs.getString("code");
        		String name = rs.getString("class_name");
        		String term = rs.getString("name");
        		System.out.println(firstName + " | " + lastName + " | " + title + " | " + code + " | " + name + " | " + term);
        	}
        	rs.close();
        	sqlStatement.close();
        	connection.close();
            //throw new SQLException(); // REMOVE THIS (this is just to force it to compile)  
        } catch (SQLException sqlException) {
            System.out.println("Failed to get class sections");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public static void submitGrade(String studentId, String classSectionID, String grade) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = database.getDatabaseConnection();
        	sqlStatement = connection.createStatement();
        	sqlStatement.execute("UPDATE class_registrations SET class_registrations.grade_id = (SELECT grade_id FROM grades WHERE grades.letter_grade = \"" + grade + "\") "
        			+ "WHERE class_registrations.student_id = " + studentId + " AND class_registrations.class_section_id = " + classSectionID);
        	ResultSet rs = sqlStatement.executeQuery("SELECT classes.class_id, classes.code, classes.name, classes.maximum_students\n" 
        			+ "FROM class_registrations "
        			+ "LEFT JOIN class_sections ON class_sections.class_section_id = class_registrations.class_section_id "
        			+ "LEFT JOIN classes ON classes.class_id = class_sections.class_id "
        			+ "WHERE class_registrations.class_section_id = " + classSectionID + " AND class_registrations.student_id = " + studentId);
        	System.out.println("Grade has been submitted!");
        	System.out.println("Class ID | Code | Name | Maximum_stuidents");
    		System.out.println(bars);
        	while(rs.next()) {
        		int classId = rs.getInt("class_id");
        		String code = rs.getString("code");
        		String name = rs.getString("name");
        		int max = rs.getInt("maximum_students");
        		System.out.println(classId + " | " + code + " | " + name + " | " + max);
        	}
        	rs.close();
        	sqlStatement.close();
        	connection.close();
            //throw new SQLException(); // REMOVE THIS (this is just to force it to compile)
        } catch (SQLException sqlException) {
            System.out.println("Failed to submit grade");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void registerStudent(String studentId, String classSectionID) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = database.getDatabaseConnection();
        	sqlStatement = connection.createStatement();
        	sqlStatement.execute("INSERT INTO class_registrations (student_id, class_section_id) "
        			+ "VALUES (" + studentId + ", " + classSectionID + ")");
        	ResultSet rs = sqlStatement.executeQuery("SELECT class_registration_id FROM class_registrations WHERE student_id = \"" + studentId + "\" AND class_section_id = \"" + classSectionID + "\"");
        	System.out.println("Class Registration ID | Student ID | Class Section ID");
    		System.out.println(bars);
        	while(rs.next()) {
        		int classRegistrationId = rs.getInt("class_registration_id");
        		System.out.println(classRegistrationId + " | " + studentId + " | " + classSectionID);
        	}
        	rs.close();
        	sqlStatement.close();
        	connection.close();
            //throw new SQLException(); // REMOVE THIS (this is just to force it to compile)
        } catch (SQLException sqlException) {
            System.out.println("Failed to register student");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void deleteStudent(String studentId) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = database.getDatabaseConnection();
        	sqlStatement = connection.createStatement();
        	sqlStatement.execute("DELETE FROM students WHERE student_id = " + studentId);
        	System.out.println("Student ID | First Name | Last Name | Birthdate");
    		System.out.println(bars);
    		System.out.println("student with id: " + studentId + " was deleted");
        	sqlStatement.close();
        	connection.close();
            //throw new SQLException(); // REMOVE THIS (this is just to force it to compile)
        } catch (SQLException sqlException) {
            System.out.println("Failed to delete student");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }


    public static void createNewStudent(String first_name, String last_name, String birthdate) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = database.getDatabaseConnection();
        	sqlStatement = connection.createStatement();
        	sqlStatement.execute("INSERT INTO students (first_name, last_name, birthdate) "
        			+ "VALUES (\"" + first_name + "\", \"" + last_name + "\", \" " + birthdate + "\")");
        	ResultSet rs = sqlStatement.executeQuery("SELECT student_id FROM students WHERE first_name = \"" + first_name + "\" AND last_name = \"" + last_name + "\"");
        	System.out.println("Student ID | First Name | Last Name | Birthdate");
    		System.out.println(bars);
        	while(rs.next()) {
        		int studentId = rs.getInt("student_id");
        		System.out.println(studentId + " | " + first_name + " | " + last_name + " | " + birthdate);
        	}
        	rs.close();
        	sqlStatement.close();
        	connection.close();
            //throw new SQLException(); // REMOVE THIS (this is just to force it to compile)
        } catch (SQLException sqlException) {
            System.out.println("Failed to create student");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public static void listAllClassRegistrations() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = database.getDatabaseConnection();
        	sqlStatement = connection.createStatement();
        	ResultSet rs = sqlStatement.executeQuery("SELECT class_registrations.student_id, class_registrations.class_section_id, students.first_name, "
        			+ "students.last_name, classes.code, classes.name, terms.name AS term_name, grades.letter_grade "
        			+ "FROM class_registrations "
        			+ "LEFT JOIN students ON students.student_id = class_registrations.student_id "
        			+ "LEFT JOIN class_sections ON class_sections.class_section_id = class_registrations.class_section_id "
        			+ "LEFT JOIN classes ON classes.class_id = class_sections.class_id "
        			+ "LEFT JOIN terms ON terms.term_id = class_sections.term_id "
        			+ "LEFT JOIN grades ON grades.grade_id = class_registrations.grade_id");
        	System.out.println("Student ID | class_section_id | First Name | Last Name | Code | Name | Term | Letter Grade");
    		System.out.println(bars);
        	while(rs.next()) {
        		int studentId = rs.getInt("student_id");
        		int classSectionId = rs.getInt("class_section_id");
        		String firstName = rs.getString("first_name");
        		String lastName = rs.getString("last_name");
        		String code = rs.getString("code");
        		String name = rs.getString("name");
        		String term = rs.getString("term_name");
        		String letterGrade = rs.getString("letter_grade");
        		System.out.println(studentId + " | " + classSectionId + " | " +  firstName + " | " + lastName + " | " + code + " | " + name + " | " + term + " | " + letterGrade);
        	}
        	rs.close();
        	sqlStatement.close();
        	connection.close();
            //throw new SQLException(); // REMOVE THIS (this is just to force it to compile)
        } catch (SQLException sqlException) {
            System.out.println("Failed to get class sections");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void listAllClassSections() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = database.getDatabaseConnection();
        	sqlStatement = connection.createStatement();
        	ResultSet rs = sqlStatement.executeQuery("SELECT class_sections.class_section_id, classes.code, classes.name, terms.name AS term_name FROM class_sections LEFT JOIN classes ON classes.class_id = class_sections.class_id LEFT JOIN terms ON terms.term_id = class_sections.term_id");
        	System.out.println("Class Section ID | Code | Name | term");
    		System.out.println(bars);
        	while(rs.next()) {
        		int classSectionId = rs.getInt("class_section_id");
        		String code = rs.getString("code");
        		String name = rs.getString("name");
        		String term = rs.getString("term_name");
        		System.out.println(classSectionId + " | " + code + " | " + name + " | " + term);
        	}
        	rs.close();
        	sqlStatement.close();
        	connection.close();
            //throw new SQLException(); // REMOVE THIS (this is just to force it to compile)
        } catch (SQLException sqlException) {
            System.out.println("Failed to get class sections");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void listAllClasses() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = database.getDatabaseConnection();
        	sqlStatement = connection.createStatement();
        	ResultSet rs = sqlStatement.executeQuery("SELECT * FROM classes");
        	System.out.println("Class ID | Code | Name | Description");
    		System.out.println(bars);
        	while(rs.next()) {
        		int classId = rs.getInt("class_id");
        		String code = rs.getString("code");
        		String name = rs.getString("name");
        		String description = rs.getString("description");
        		System.out.println(classId + " | " + code + " | " + name + " | " + description);
        	}
        	rs.close();
        	sqlStatement.close();
        	connection.close();
            //throw new SQLException(); // REMOVE THIS (this is just to force it to compile)
        } catch (SQLException sqlException) {
            System.out.println("Failed to get students");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }


    public static void listAllStudents() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
        	connection = database.getDatabaseConnection();
        	sqlStatement = connection.createStatement();
        	ResultSet rs = sqlStatement.executeQuery("SELECT * FROM students");
        	System.out.println("Student ID | First Name | Last Name | Birthdate");
    		System.out.println(bars);
        	while(rs.next()) {
        		int studentId = rs.getInt("student_id");
        		String firstName = rs.getString("first_name");
        		String lastName = rs.getString("last_name");
        		String birthDate = rs.getString("birthdate");
        		System.out.println(studentId + " | " + firstName + " | " + lastName + " | " + birthDate);
        	}
        	rs.close();
        	sqlStatement.close();
        	connection.close();
           //throw new SQLException(); // REMOVE THIS (this is just to force it to compile)
        } catch (SQLException sqlException) {
            System.out.println("Failed to get students");
            System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }


	/***
     * Splits a string up by spaces. Spaces are ignored when wrapped in quotes.
     *
     * @param command - School Management System cli command
     * @return splits a string by spaces.
     */
    public static List<String> parseArguments(String command) {
        List<String> commandArguments = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (m.find()) commandArguments.add(m.group(1).replace("\"", ""));
        return commandArguments;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the School Management System");
        System.out.println("-".repeat(80));

        Scanner scan = new Scanner(System.in);
        String command = "";

        do {
            System.out.print("Command: ");
            command = scan.nextLine();
            ;
            List<String> commandArguments = parseArguments(command);
            command = commandArguments.get(0);
            commandArguments.remove(0);

            if (command.equals("help")) {
                System.out.println("-".repeat(38) + "Help" + "-".repeat(38));
                System.out.println("test connection \n\tTests the database connection");

                System.out.println("list students \n\tlists all the students");
                System.out.println("list classes \n\tlists all the classes");
                System.out.println("list class_sections \n\tlists all the class_sections");
                System.out.println("list class_registrations \n\tlists all the class_registrations");
                System.out.println("list instructor <first_name> <last_name>\n\tlists all the classes taught by that instructor");


                System.out.println("delete student <studentId> \n\tdeletes the student");
                System.out.println("create student <first_name> <last_name> <birthdate> \n\tcreates a student");
                System.out.println("register student <student_id> <class_section_id>\n\tregisters the student to the class section");

                System.out.println("submit grade <studentId> <class_section_id> <letter_grade> \n\tcreates a student");
                System.out.println("help \n\tlists help information");
                System.out.println("quit \n\tExits the program");
            } else if (command.equals("test") && commandArguments.get(0).equals("connection")) {
                Database.testConnection();
            } else if (command.equals("list")) {
                if (commandArguments.get(0).equals("students")) listAllStudents();
                if (commandArguments.get(0).equals("classes")) listAllClasses();
                if (commandArguments.get(0).equals("class_sections")) listAllClassSections();
                if (commandArguments.get(0).equals("class_registrations")) listAllClassRegistrations();

                if (commandArguments.get(0).equals("instructor")) {
                    getAllClassesByInstructor(commandArguments.get(1), commandArguments.get(2));
                }
            } else if (command.equals("create")) {
                if (commandArguments.get(0).equals("student")) {
                    createNewStudent(commandArguments.get(1), commandArguments.get(2), commandArguments.get(3));
                }
            } else if (command.equals("register")) {
                if (commandArguments.get(0).equals("student")) {
                    registerStudent(commandArguments.get(1), commandArguments.get(2));
                }
            } else if (command.equals("submit")) {
                if (commandArguments.get(0).equals("grade")) {
                    submitGrade(commandArguments.get(1), commandArguments.get(2), commandArguments.get(3));
                }
            } else if (command.equals("delete")) {
                if (commandArguments.get(0).equals("student")) {
                    deleteStudent(commandArguments.get(1));
                }
            } else if (!(command.equals("quit") || command.equals("exit"))) {
                System.out.println(command);
                System.out.println("Command not found. Enter 'help' for list of commands");
            }
            System.out.println("-".repeat(80));
        } while (!(command.equals("quit") || command.equals("exit")));
        System.out.println("Bye!");
    }
}

