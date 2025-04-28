/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databases;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class koneksi {
    private Connection koneksi = null;

    public Connection connect() {
        try {
            // Memuat Driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Berhasil Koneksi ke Driver");

            // URL koneksi database
            String url = "jdbc:mysql://localhost:3306/kkpapotek"; // Gantilah dengan nama database yang sesuai
            koneksi = DriverManager.getConnection(url, "useraplikasi", "12345"); // Gantilah dengan username dan password yang sesuai

            System.out.println("Berhasil Koneksi Database");

        } catch (ClassNotFoundException ex) {
            System.out.println("Gagal Koneksi Driver: " + ex);
        } catch (SQLException ex) {
            System.out.println("Gagal Koneksi Database: " + ex);
        }

        return koneksi;
    }
}
