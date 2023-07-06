package todolist;
import java.sql.*;
import java.util.Scanner;


class ToDo {
    private int id;
    private String todo;
    private String kategori;
    private Date tanggal;
    private String status;
    
    public ToDo(int id, String todo, String kategori, Date tanggal, String status){
        this.id = id;
        this.todo = todo;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.status = status;
    }
    
     public int getId() {
        return id;
    }
     
      public String getTodo() {
        return todo;
    }
      
       public String getKategori() {
        return kategori;
    }
       
      public Date getTanggal() {
        return tanggal;
    }
        
      public String getStatus() {
        return status;
    }
      
}
public class ToDoList {
    // Konfigurasi koneksi database
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/uasprakpboandika";
    static final String USER = "username_database";
    static final String PASS = "password_database";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            // Mengatur koneksi ke dalam databasenya
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // Loop utama program
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("|========= TODO LIST ========|");
                System.out.println("|    1. Tampilkan To-Do List |");
                System.out.println("|    2. Tambahkan To-Do      |");
                System.out.println("|    3. Hapus To-Do          |");
                System.out.println("|    4. Keluar               |");
                System.out.print(  "|Pilih :                     |");
                System.out.println("|============================|");
                
                int menu = scanner.nextInt();
                scanner.nextLine(); // Membersihkan newline

                switch (menu) {
                    case 1:
                        displayToDoList(stmt);
                        break;
                    case 2:
                        addToDoItem(stmt, scanner);
                        break;
                    case 3:
                        deleteToDoItem(stmt, scanner);
                        break;
                    case 4:
                        exit = true;
                        break;
                    default:
                        System.out.println("Menu yang Anda pilih tidak valid.");
                }

                System.out.println();
            }

            // Menutup koneksi
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    // Menampilkan daftar todo
    private static void displayToDoList(Statement stmt) throws SQLException {
        String sql = "SELECT * FROM todolist";
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("===== DAFTAR TO-DO =====");
        while (rs.next()) {
            int id = rs.getInt("id");
            String todo = rs.getString("todo");
            String kategori = rs.getString("kategori");
            Date tanggal = rs.getDate("tanggal");
            String status = rs.getString("status");

            System.out.println("ID: " + id);
            System.out.println("To-Do: " + todo);
            System.out.println("Kategori: " + kategori);
            System.out.println("Tanggal: " + tanggal);
            System.out.println("Status: " + status);
            System.out.println();
        }

        rs.close();
    }

    // Menambahkan todo yang baru
    private static void addToDoItem(Statement stmt, Scanner scanner) throws SQLException {
        System.out.println("===== TAMBAHKAN TO-DO =====");
        System.out.print("Masukkan To-Do: ");
        String todo = scanner.nextLine();

        System.out.print("Masukkan Kategori: ");
        String kategori = scanner.nextLine();

        System.out.print("Masukkan Tanggal (YYYY-MM-DD): ");
        String tanggal = scanner.nextLine();

        System.out.print("Masukkan Status: ");
        String status = scanner.nextLine();

        String sql = "INSERT INTO todolist (todo, kategori, tanggal, status) VALUES " +
                "('" + todo + "', '" + kategori + "', '" + tanggal + "', '" + status + "')";

        stmt.executeUpdate(sql);
        System.out.println("To-Do berhasil ditambahkan.");
    }

    // Menghapus to-do
    private static void deleteToDoItem(Statement stmt, Scanner scanner) throws SQLException {
        System.out.println("===== HAPUS TO-DO =====");
        System.out.print("Masukkan ID To-Do yang akan dihapus: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Membersihkan newline

        String sql = "DELETE FROM todolist WHERE id=" + id;

        stmt.executeUpdate(sql);
        System.out.println("To-Do berhasil dihapus.");
    }
}