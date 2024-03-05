import java.sql.*;
import java.text.SimpleDateFormat;



public class PostgreSQLJDBCConnection {
    // declare the connection obj
    private static Connection conn = null;


    public static void main(String[] args) {

        // JDBC & Database credentials
        String url = "jdbc:postgresql://localhost:5432/University";
        String user = "postgres";
        String password = "111111";

        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Connect to the database
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to PostgreSQL successfully!");
                System.out.println();

                // Retrieves and displays all records from the students table
                 getAllStudents();


                // Inserts a new student record into the students table.
//                addStudent("Jack","Dong","jack.dong@example.com","2024-03-04");


                // Updates the email address for a student with the specified student_id.
//                updateStudentEmail(7, "111111111@example.com");


                // Deletes the record of the student with the specified student_id.
//                deleteStudent(7);


            } else {
                System.out.println("Failed to establish connection.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    // Close the connection
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }


    // 1. Retrieves and displays all records from the students table.
    private static void getAllStudents() throws SQLException {
        // Create statement
        Statement stmt = conn.createStatement();
        // write the right sql language
        String SQL = "SELECT * FROM students";
        // Execute SQL query
        ResultSet rs = stmt.executeQuery(SQL); // Process the result

        // format the output
        String format = "%-12s %-12s %-12s %-25s %-15s%n";
        System.out.printf(format, "student_id", "first_name", "last_name", "email", "enrollment_date");

        // Execute SQL query
        while (rs.next()) {
            // get each column
            int student_id = rs.getInt("student_id");
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            String email = rs.getString("email");
            // convert date to string
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String enrollment_date = formatter.format(rs.getDate("enrollment_date"));

            // print the result
            System.out.printf(format, student_id, first_name, last_name, email, enrollment_date);
        }
        // Close resources
        rs.close();
        stmt.close();
    }


    // 2. Inserts a new student record into the students table.
    private static void addStudent(String first_name, String last_name, String email, String enrollment_date) throws Exception {
        // create sql
        String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?,?)";
        // create PreparedStatement obj
        PreparedStatement pstmt = conn.prepareStatement(insertSQL);

        // set value
        pstmt.setString(1, first_name);
        pstmt.setString(2, last_name);
        pstmt.setString(3, email);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = formatter.parse(enrollment_date);
        Date sqlDate = new Date(utilDate.getTime());
        pstmt.setDate(4, sqlDate);

        // execute
        int rowsInserted = pstmt.executeUpdate();
        System.out.println(rowsInserted + " rows inserted !");
    }

    // 3. Updates the email address for a student with the specified student_id.
    private static void updateStudentEmail(int student_id, String new_email) throws SQLException {
        String updateSQL = "UPDATE students SET email = ? WHERE student_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(updateSQL);

        pstmt.setString(1, new_email);
        pstmt.setInt(2, student_id);

        int rowsUpdated = pstmt.executeUpdate();
        System.out.println(rowsUpdated + " rows updated !");
    }

    // 4. Deletes the record of the student with the specified student_id.
    private static void deleteStudent(int student_id) throws SQLException {
        String deleteSQL = "DELETE FROM students WHERE student_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(deleteSQL);

        pstmt.setInt(1, student_id);

        int rowsDeleted = pstmt.executeUpdate();
        System.out.println(rowsDeleted + " rows deleted !");
    }
}
