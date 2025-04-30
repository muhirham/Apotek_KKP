/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import databases.koneksi;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author user
 */
public class admin extends javax.swing.JFrame {
    private Connection conn = new koneksi().connect();
    private DefaultTableModel tabmode;
    private Map<String, Integer> kategoriMap = new HashMap<>();
    private Map<String, Integer> suplierMap = new HashMap<>();

    

    /**
     * Creates new form admin
     */
    public admin() {
        initComponents();
        pHome.setVisible(true);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pSuplayer.setVisible(false);
        
        if (txtnamaobat.getText().isEmpty()) {
            txtnamaobat.setText("Kode Obat");  // Menampilkan placeholder jika field kosong
            txtnamaobat.setForeground(Color.GRAY);  // Mengatur warna placeholder menjadi abu-abu
        }
        
        datatableSublayer();
        datatableObat();
        cbKategori();
        cbSuplayer();
        
        setSize(947, 650);
        
         setLocationRelativeTo(null);  // Center the JFrame

        // Make the JFrame visible
        setVisible(true);
    }
    
    protected void datatableSublayer() {
        // Define the column names as per the previous array
        Object[] Baris = {"Nomor", "Nama Perusahaan", "Alamat Perusahaan", "No Kontak", "No Telpon", "Nama Bank", "No Rekening"};
        tabmode = new DefaultTableModel(null, Baris);
        jTablesuplayer.setModel(tabmode);

        // SQL query to fetch the data from the 'suplier' table (ensure table name is correct)
        String sql = "SELECT * FROM suplier ORDER BY id_suplayer ASC"; 
        try {
            // Create a Statement to execute the query
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);

            // Loop through the ResultSet and populate the table
            while (hasil.next()) {
                // Get the data for each row from the result set
                String id = hasil.getString("id_suplayer");
                String nama_perusahaan = hasil.getString("nama_perusahaan");
                String alamat = hasil.getString("alamat");
                String contact_person = hasil.getString("contact_person");
                String no_tlp = hasil.getString("no_tlp");
                String nama_bank = hasil.getString("nama_bank");
                String no_rekening = hasil.getString("no_rekening");

                // Add the data to the table model
                String[] data = {id, nama_perusahaan, alamat, contact_person, no_tlp, nama_bank, no_rekening};
                tabmode.addRow(data);
            }
        } catch (Exception e) {
            // Print any errors
            System.err.println("Error: " + e.getMessage());
        }
    }
    protected void datatableObat() {
        // Set up table column names
        Object[] columns = {"Kode Obat", "Nama Obat", "Harga Jual", "Harga Beli", "Stok", "Kemasan", "Kategori", "Suplier", "Golongan", "No Registrasi BPOM"};
        DefaultTableModel tabmode = new DefaultTableModel(null, columns);
        tbObat.setModel(tabmode);

        // SQL query
        String sql = "SELECT obat.kode_obat, obat.nama_obat, obat.harga_jual, obat.harga_beli, obat.stok, obat.kemasan, "
                    + "kategori_obat.nama_kategori, suplier.nama_perusahaan, obat.golongan, obat.no_registrasi_bpom "
                    + "FROM obat "
                    + "JOIN kategori_obat ON obat.id_kategori = kategori_obat.id_kategori "
                    + "JOIN suplier ON obat.id_suplayer = suplier.id_suplayer "
                    + "ORDER BY obat.kode_obat";

        try {
            // Membuat Statement dan mengeksekusi query
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);

            // Mengambil data dan menambahkannya ke dalam JTable
            while (hasil.next()) {
                String kode_obat = hasil.getString("kode_obat");
                String nama_obat = hasil.getString("nama_obat");
                String harga_jual = hasil.getString("harga_jual");
                String harga_beli = hasil.getString("harga_beli");
                String stok = hasil.getString("stok");
                String kemasan = hasil.getString("kemasan");
                String kategori = hasil.getString("nama_kategori");  // mengambil nama kategori
                String suplier = hasil.getString("nama_perusahaan");  // mengambil nama suplier
                String golongan = hasil.getString("golongan");
                String no_bpom = hasil.getString("no_registrasi_bpom");

                // Menambahkan data ke dalam tabel
                String[] data = {kode_obat, nama_obat, harga_jual, harga_beli, stok, kemasan, kategori, suplier, golongan, no_bpom};
                tabmode.addRow(data);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());  // Mencetak error SQL
        }
    }
    
   protected void cbKategori(){
    try {
        String sql = "SELECT * FROM kategori_obat ORDER BY id_kategori ASC";
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);

        cbkategori.removeAllItems();
        kategoriMap.clear(); // reset mapping

        while (hasil.next()) {
            int idKategori = hasil.getInt("id_kategori");
            String namaKategori = hasil.getString("nama_kategori");

            cbkategori.addItem(namaKategori);
            kategoriMap.put(namaKategori, idKategori);
        }
    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
    }
}

    // Membuat ComboBox untuk suplier
    private void cbSuplayer() {
    try {
        String sql = "SELECT * FROM suplier ORDER BY id_suplayer ASC";
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);

        cbsuplayer.removeAllItems();
        suplierMap.clear(); // reset mapping

        while (hasil.next()) {
            int idSuplier = hasil.getInt("id_suplayer");
            String namaSuplier = hasil.getString("nama_perusahaan");

            cbsuplayer.addItem(namaSuplier);
            suplierMap.put(namaSuplier, idSuplier);
        }
    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
    }
}


    protected void clearSuplayer() {
        // Clear all the text fields
        txtid.setText(null);
        txtnamaperusahaan.setText(null);
        txtalamat.setText(null);
        txtnokontak.setText(null);
        txtnotlp.setText(null);
        txtnorekening.setText(null);
        txtnamabank.setText(null); // Clears the image label
    }

    
    protected void dataterpilihSuplayer() {        
        int bar = jTablesuplayer.getSelectedRow();  // Get the selected row index
        String id = tabmode.getValueAt(bar, 0).toString();
        String nama_perusahaan = tabmode.getValueAt(bar, 1).toString();
        String alamat = tabmode.getValueAt(bar, 2).toString();
        String contact_person = tabmode.getValueAt(bar, 3).toString();
        String no_tlp = tabmode.getValueAt(bar, 4).toString();
        String nama_bank = tabmode.getValueAt(bar, 5).toString();
        String no_rekening = tabmode.getValueAt(bar, 6).toString();  // Column 6 for the bank name

        // Set the retrieved data into respective input fields
        txtid.setText(id);
        txtnamaperusahaan.setText(nama_perusahaan);
        txtalamat.setText(alamat);  // Assuming you have a field to store the address
        txtnokontak.setText(contact_person);  // Assuming you have a field for the contact person
        txtnotlp.setText(no_tlp);  // Assuming you have a field for account number
        txtnamabank.setText(nama_bank);  // Assuming you have a field for phone number
        txtnorekening.setText(no_rekening);  // Assuming you have a field for the bank name
    }
    
    protected void tambahSuplayer() {
        // Make sure fields are not empty
        if (txtnamaperusahaan.getText().isEmpty() || txtalamat.getText().isEmpty() || txtnokontak.getText().isEmpty() || 
            txtnotlp.getText().isEmpty() || txtnorekening.getText().isEmpty() || txtnamabank.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
            return;
        }

        String sql = "INSERT INTO suplier (nama_perusahaan, alamat, contact_person, no_tlp, no_rekening, nama_bank) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, txtnamaperusahaan.getText());
            stat.setString(2, txtalamat.getText());
            stat.setString(3, txtnokontak.getText());
            stat.setString(4, txtnotlp.getText());
            stat.setString(5, txtnorekening.getText());
            stat.setString(6, txtnamabank.getText());

            // Execute the SQL statement to insert data into the database
            stat.executeUpdate();

            // Show success message
            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan");

            // Clear the input fields and refresh the table
            clearSuplayer();
            datatableSublayer();
        } catch (SQLException e) {
            // Print the error for debugging
            System.out.println("Error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Data Gagal Ditambahkan");
        }
    }
    
    protected void editSuplayer() {
        // Ensure the 'id' is retrieved and available for the update query
        String id = txtid.getText(); // Assuming you already have a field for 'id'

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "ID Tidak Ditemukan.");
            return; // Exit if ID is not found or provided
        }

        String sql = "UPDATE suplier SET nama_perusahaan=?, alamat=?, contact_person=?, no_tlp=?, nama_bank=?, no_rekening=? WHERE id_suplayer=?";

        try {
            // Nonaktifkan autocommit untuk mengelola transaksi manual
            conn.setAutoCommit(false);

            try (PreparedStatement stat = conn.prepareStatement(sql)) {
                // Set the parameters for the update query
                stat.setString(1, txtnamaperusahaan.getText());
                stat.setString(2, txtalamat.getText());
                stat.setString(3, txtnokontak.getText());
                stat.setString(4, txtnotlp.getText());
                stat.setString(5, txtnamabank.getText());
                stat.setString(6, txtnorekening.getText());
                stat.setString(7, id); // Set the ID for the WHERE clause

                // Tampilkan nilai parameter sebelum eksekusi untuk debugging
                System.out.println("Nama Perusahaan: " + txtnamaperusahaan.getText());
                System.out.println("Alamat Perusahaan: " + txtalamat.getText());
                System.out.println("No Kontak: " + txtnokontak.getText());
                System.out.println("No Telepon: " + txtnotlp.getText());
                System.out.println("Nama Bank: " + txtnamabank.getText());
                System.out.println("No Rekening: " + txtnorekening.getText());
                System.out.println("ID: " + id);

                // Eksekusi update query
                stat.executeUpdate();

                // Commit transaksi
                conn.commit();

                // Show success message
                JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
                clearSuplayer();
                datatableSublayer(); // Refresh the table
            } catch (SQLException e) {
                // If there is an error, rollback the transaction
                conn.rollback();
                System.out.println("Error during update: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Data Gagal Diubah: " + e.getMessage());
            } finally {
                // Set autocommit back to true
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah: " + e.getMessage());
        }
    }
    
    protected void deleteSuplayer(){
        int delete = JOptionPane.showConfirmDialog(null,"hapus", 
                "Konfirmasi Dialog",JOptionPane.YES_NO_OPTION);
        if(delete==0){
            String sql="delete from suplier where id_suplayer='"+txtid.getText()+"'";
            try {
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
                clearSuplayer();
                datatableSublayer();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Data Gagal Dihapus");
            }
        }
    }
    
    protected void cariSuplayer(){
        Object[] Baris = {"Nomor", "Nama Perusahaan", "Alamat Perusahaan", "No Kontak", "No Telpon", "Nama Bank", "No Rekening"};
        tabmode = new DefaultTableModel(null, Baris);
        jTablesuplayer.setModel(tabmode);
        String keyword = txtcariSuplayer.getText();
        String sql = "select * from suplier where id_suplayer like '%"+keyword+"%' or nama_perusahaan like '%"+keyword+"%' order by id_suplayer asc";
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while(hasil.next()){
                String a = hasil.getString("id_suplayer");
                String b = hasil.getString("nama_perusahaan");
                String c = hasil.getString("alamat");
                String d = hasil.getString("contact_person");
                String e = hasil.getString("no_tlp");
                String f = hasil.getString("nama_bank");
                String g = hasil.getString("no_rekening");

                String[] data = {a,b,c,d,e,f,g};
                tabmode.addRow(data);
            }
        }catch(Exception e){
        }
    }


   private void tambahObat() {
    try {
        String kodeObat = txtkodeobat.getText();
        String namaObat = txtnamaobat.getText();
        int hargaJual = Integer.parseInt(txthargajual.getText());
        int hargaBeli = Integer.parseInt(txthargabeli.getText());
        int stok = Integer.parseInt(txtstok.getText());
        String kemasan = txtkemasan.getText();
        String golongan = cbgolongan.getSelectedItem().toString();
        String noBpom = txtnoregisbpom.getText();

        String selectedKategori = cbkategori.getSelectedItem().toString();
        String selectedSuplier = cbsuplayer.getSelectedItem().toString();

        int idKategori = kategoriMap.get(selectedKategori);
        int idSuplier = suplierMap.get(selectedSuplier);

        String sql = "INSERT INTO obat (kode_obat, nama_obat, harga_jual, harga_beli, stok, kemasan, id_kategori, id_suplayer, golongan, no_registrasi_bpom) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, kodeObat);
        pst.setString(2, namaObat);
        pst.setInt(3, hargaJual);
        pst.setInt(4, hargaBeli);
        pst.setInt(5, stok);
        pst.setString(6, kemasan);
        pst.setInt(7, idKategori);
        pst.setInt(8, idSuplier);
        pst.setString(9, golongan);
        pst.setString(10, noBpom);

        pst.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data obat berhasil disimpan.");
        datatableObat(); // refresh tabel
    } catch (SQLException | NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Error menyimpan data: " + e.getMessage());
    }
}





    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel21 = new javax.swing.JLabel();
        pn_navbar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pn_sidebar = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jtestKategori = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jtestSuplayer = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jtestSuplayer3 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jtest2 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jtest3 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jtestSuplayer1 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jtestSuplayer2 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jtestSuplayer4 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jtestSuplayer5 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        pHome = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        pSuplayer = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablesuplayer = new javax.swing.JTable();
        txtcariSuplayer = new javax.swing.JTextField();
        txtnamaperusahaan = new javax.swing.JTextField();
        txtnokontak = new javax.swing.JTextField();
        txtnamabank = new javax.swing.JTextField();
        txtnotlp = new javax.swing.JTextField();
        txtnorekening = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtalamat = new javax.swing.JTextArea();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        txtid = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        pKategori = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        pRfobat = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        pPenjualan = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        pPembelian = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        pObat = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbObat = new javax.swing.JTable();
        txtnamaobat = new javax.swing.JTextField();
        txtkemasan = new javax.swing.JTextField();
        txthargajual = new javax.swing.JTextField();
        txthargabeli = new javax.swing.JTextField();
        txtstok = new javax.swing.JTextField();
        txtnoregisbpom = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        cbkategori = new javax.swing.JComboBox<>();
        cbsuplayer = new javax.swing.JComboBox<>();
        cbgolongan = new javax.swing.JComboBox<>();
        jLabel49 = new javax.swing.JLabel();
        txtkodeobat = new javax.swing.JTextField();

        jLabel21.setText("jLabel21");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(null);

        pn_navbar.setBackground(new java.awt.Color(0, 0, 0));
        pn_navbar.setForeground(new java.awt.Color(204, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo_apotek-removebg-preview.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-exit-40.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pn_navbarLayout = new javax.swing.GroupLayout(pn_navbar);
        pn_navbar.setLayout(pn_navbarLayout);
        pn_navbarLayout.setHorizontalGroup(
            pn_navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_navbarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 692, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        pn_navbarLayout.setVerticalGroup(
            pn_navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_navbarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pn_navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_navbarLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(pn_navbar);
        pn_navbar.setBounds(0, 0, 947, 60);

        pn_sidebar.setBackground(new java.awt.Color(255, 255, 255));
        pn_sidebar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 153, 255));
        jLabel3.setText("Master Data");
        pn_sidebar.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 102, 139, -1));

        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 153, 255));
        jLabel4.setText("Dashboard");
        pn_sidebar.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 24, 139, -1));

        jLabel5.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 153, 255));
        jLabel5.setText("Data ");
        pn_sidebar.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 139, -1));

        jLabel6.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 153, 255));
        jLabel6.setText("Laporan");
        pn_sidebar.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 139, -1));

        jtestKategori.setBackground(new java.awt.Color(255, 255, 255));
        jtestKategori.setForeground(new java.awt.Color(204, 204, 204));
        jtestKategori.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtestKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtestKategoriMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtestKategoriMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtestKategoriMouseExited(evt);
            }
        });
        jtestKategori.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-categories-30.png"))); // NOI18N
        jtestKategori.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel30.setText("Kategori Obat");
        jtestKategori.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        pn_sidebar.add(jtestKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 170, 30));

        jtestSuplayer.setBackground(new java.awt.Color(255, 255, 255));
        jtestSuplayer.setForeground(new java.awt.Color(204, 204, 204));
        jtestSuplayer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtestSuplayer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtestSuplayerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtestSuplayerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtestSuplayerMouseExited(evt);
            }
        });
        jtestSuplayer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-buy-30.png"))); // NOI18N
        jtestSuplayer.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel32.setText("Pembelian Obat");
        jtestSuplayer.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 90, 30));

        jtestSuplayer3.setBackground(new java.awt.Color(255, 255, 255));
        jtestSuplayer3.setForeground(new java.awt.Color(204, 204, 204));
        jtestSuplayer3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtestSuplayer3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtestSuplayer3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtestSuplayer3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtestSuplayer3MouseExited(evt);
            }
        });
        jtestSuplayer3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-sell-30.png"))); // NOI18N
        jtestSuplayer3.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel42.setText("Penjualan Obat");
        jtestSuplayer3.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        jtestSuplayer.add(jtestSuplayer3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 170, 30));

        pn_sidebar.add(jtestSuplayer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 170, 30));

        jtest2.setBackground(new java.awt.Color(255, 255, 255));
        jtest2.setForeground(new java.awt.Color(204, 204, 204));
        jtest2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtest2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtest2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtest2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtest2MouseExited(evt);
            }
        });
        jtest2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-home-30.png"))); // NOI18N
        jtest2.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel34.setText("Home");
        jtest2.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        pn_sidebar.add(jtest2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 170, 30));

        jtest3.setBackground(new java.awt.Color(255, 255, 255));
        jtest3.setForeground(new java.awt.Color(204, 204, 204));
        jtest3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtest3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtest3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtest3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtest3MouseExited(evt);
            }
        });
        jtest3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-form-30.png"))); // NOI18N
        jtest3.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel36.setText("RF Obat");
        jtest3.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        pn_sidebar.add(jtest3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 170, 30));

        jtestSuplayer1.setBackground(new java.awt.Color(255, 255, 255));
        jtestSuplayer1.setForeground(new java.awt.Color(204, 204, 204));
        jtestSuplayer1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtestSuplayer1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtestSuplayer1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtestSuplayer1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtestSuplayer1MouseExited(evt);
            }
        });
        jtestSuplayer1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-users-30.png"))); // NOI18N
        jtestSuplayer1.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel38.setText("Data suplayar");
        jtestSuplayer1.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        pn_sidebar.add(jtestSuplayer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 170, 30));

        jtestSuplayer2.setBackground(new java.awt.Color(255, 255, 255));
        jtestSuplayer2.setForeground(new java.awt.Color(204, 204, 204));
        jtestSuplayer2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtestSuplayer2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtestSuplayer2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtestSuplayer2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtestSuplayer2MouseExited(evt);
            }
        });
        jtestSuplayer2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-medicines-30.png"))); // NOI18N
        jtestSuplayer2.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel40.setText("Data Obat");
        jtestSuplayer2.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        pn_sidebar.add(jtestSuplayer2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 170, 30));

        jtestSuplayer4.setBackground(new java.awt.Color(255, 255, 255));
        jtestSuplayer4.setForeground(new java.awt.Color(204, 204, 204));
        jtestSuplayer4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtestSuplayer4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtestSuplayer4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtestSuplayer4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtestSuplayer4MouseExited(evt);
            }
        });
        jtestSuplayer4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-sell-30.png"))); // NOI18N
        jtestSuplayer4.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel44.setText("Penjualan Obat");
        jtestSuplayer4.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        jtestSuplayer5.setBackground(new java.awt.Color(255, 255, 255));
        jtestSuplayer5.setForeground(new java.awt.Color(204, 204, 204));
        jtestSuplayer5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtestSuplayer5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtestSuplayer5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtestSuplayer5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtestSuplayer5MouseExited(evt);
            }
        });
        jtestSuplayer5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-sell-30.png"))); // NOI18N
        jtestSuplayer5.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel46.setText("Penjualan Obat");
        jtestSuplayer5.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        jtestSuplayer4.add(jtestSuplayer5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 170, 30));

        pn_sidebar.add(jtestSuplayer4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 170, 30));

        getContentPane().add(pn_sidebar);
        pn_sidebar.setBounds(0, 66, 175, 621);

        pHome.setBackground(new java.awt.Color(153, 153, 255));

        jLabel12.setText("Home Dashboard");

        javax.swing.GroupLayout pHomeLayout = new javax.swing.GroupLayout(pHome);
        pHome.setLayout(pHomeLayout);
        pHomeLayout.setHorizontalGroup(
            pHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(645, Short.MAX_VALUE))
        );
        pHomeLayout.setVerticalGroup(
            pHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(605, Short.MAX_VALUE))
        );

        getContentPane().add(pHome);
        pHome.setBounds(181, 66, 760, 627);

        pSuplayer.setBackground(new java.awt.Color(0, 255, 204));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setText("DATA SUPLAYER");

        jTablesuplayer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama Perusahaan", "Alamat", "No Kontak", "No Tlp", "No Rekening", "Nama Bank"
            }
        ));
        jTablesuplayer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablesuplayerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTablesuplayer);

        txtcariSuplayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcariSuplayerActionPerformed(evt);
            }
        });
        txtcariSuplayer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcariSuplayerKeyPressed(evt);
            }
        });

        txtnamaperusahaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnamaperusahaanActionPerformed(evt);
            }
        });

        txtnokontak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnokontakActionPerformed(evt);
            }
        });

        txtnamabank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnamabankActionPerformed(evt);
            }
        });

        txtnotlp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnotlpActionPerformed(evt);
            }
        });

        txtnorekening.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnorekeningActionPerformed(evt);
            }
        });

        txtalamat.setColumns(20);
        txtalamat.setRows(5);
        jScrollPane3.setViewportView(txtalamat);

        jLabel22.setText("Nama Perusahaan");

        jLabel23.setText("Alamat Perusahaan");

        jLabel24.setText("No kontak");

        jLabel25.setText("No Telepon");

        jLabel26.setText("Nama Bank");

        jLabel27.setText("No Rekening");

        jButton2.setText("Save");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setText("Edit");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jButton5.setText("Delete");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        txtid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidActionPerformed(evt);
            }
        });

        jLabel28.setText("Nomor");

        jButton9.setText("Cetak");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton9MouseClicked(evt);
            }
        });
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pSuplayerLayout = new javax.swing.GroupLayout(pSuplayer);
        pSuplayer.setLayout(pSuplayerLayout);
        pSuplayerLayout.setHorizontalGroup(
            pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSuplayerLayout.createSequentialGroup()
                .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pSuplayerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 384, Short.MAX_VALUE)
                        .addComponent(txtcariSuplayer, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pSuplayerLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 723, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(pSuplayerLayout.createSequentialGroup()
                .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pSuplayerLayout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pSuplayerLayout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pSuplayerLayout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtnamaperusahaan, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pSuplayerLayout.createSequentialGroup()
                                .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel26)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel27))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pSuplayerLayout.createSequentialGroup()
                                            .addComponent(txtnamabank, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel24))
                                        .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pSuplayerLayout.createSequentialGroup()
                                        .addComponent(txtnorekening, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtnokontak, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pSuplayerLayout.createSequentialGroup()
                                                .addComponent(jLabel25)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtnotlp, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pSuplayerLayout.setVerticalGroup(
            pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSuplayerLayout.createSequentialGroup()
                .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pSuplayerLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel17)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pSuplayerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtcariSuplayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addGap(15, 15, 15)
                .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnamaperusahaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnokontak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnamabank, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnotlp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnorekening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pSuplayerLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pSuplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel23))
                .addContainerGap(92, Short.MAX_VALUE))
        );

        getContentPane().add(pSuplayer);
        pSuplayer.setBounds(179, 63, 770, 630);

        pKategori.setBackground(new java.awt.Color(204, 255, 204));

        jLabel15.setText("KATEGORI OBAT");

        javax.swing.GroupLayout pKategoriLayout = new javax.swing.GroupLayout(pKategori);
        pKategori.setLayout(pKategoriLayout);
        pKategoriLayout.setHorizontalGroup(
            pKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pKategoriLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel15)
                .addContainerGap(653, Short.MAX_VALUE))
        );
        pKategoriLayout.setVerticalGroup(
            pKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pKategoriLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel15)
                .addContainerGap(597, Short.MAX_VALUE))
        );

        getContentPane().add(pKategori);
        pKategori.setBounds(181, 66, 761, 627);

        pRfobat.setBackground(new java.awt.Color(255, 255, 102));

        jLabel8.setText("RF Obat");

        javax.swing.GroupLayout pRfobatLayout = new javax.swing.GroupLayout(pRfobat);
        pRfobat.setLayout(pRfobatLayout);
        pRfobatLayout.setHorizontalGroup(
            pRfobatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRfobatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(645, Short.MAX_VALUE))
        );
        pRfobatLayout.setVerticalGroup(
            pRfobatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRfobatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(608, Short.MAX_VALUE))
        );

        getContentPane().add(pRfobat);
        pRfobat.setBounds(181, 63, 760, 630);

        pPenjualan.setBackground(new java.awt.Color(153, 153, 255));

        jLabel9.setText("Penjualan Obat");

        javax.swing.GroupLayout pPenjualanLayout = new javax.swing.GroupLayout(pPenjualan);
        pPenjualan.setLayout(pPenjualanLayout);
        pPenjualanLayout.setHorizontalGroup(
            pPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPenjualanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(645, Short.MAX_VALUE))
        );
        pPenjualanLayout.setVerticalGroup(
            pPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPenjualanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(605, Short.MAX_VALUE))
        );

        getContentPane().add(pPenjualan);
        pPenjualan.setBounds(181, 66, 760, 627);

        pPembelian.setBackground(new java.awt.Color(153, 153, 255));

        jLabel11.setText("Pembelian Obat");

        javax.swing.GroupLayout pPembelianLayout = new javax.swing.GroupLayout(pPembelian);
        pPembelian.setLayout(pPembelianLayout);
        pPembelianLayout.setHorizontalGroup(
            pPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPembelianLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(645, Short.MAX_VALUE))
        );
        pPembelianLayout.setVerticalGroup(
            pPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPembelianLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(605, Short.MAX_VALUE))
        );

        getContentPane().add(pPembelian);
        pPembelian.setBounds(181, 66, 760, 627);

        pObat.setBackground(new java.awt.Color(51, 51, 51));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Data Obat");

        tbObat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode_Obat", "Nama Obat", "Harga Jual", "Harga Beli", "Stok", "Kemasan", "Kategori Obat", "Suplayer", "Golongan", "No Regis BPOM"
            }
        ));
        jScrollPane2.setViewportView(tbObat);

        txtnamaobat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtnamaobatFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtnamaobatFocusLost(evt);
            }
        });

        txtkemasan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtkemasanFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtkemasanFocusLost(evt);
            }
        });

        txthargajual.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txthargajualFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txthargajualFocusLost(evt);
            }
        });
        txthargajual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthargajualActionPerformed(evt);
            }
        });

        txthargabeli.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txthargabeliFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txthargabeliFocusLost(evt);
            }
        });

        txtstok.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtstokFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtstokFocusLost(evt);
            }
        });

        txtnoregisbpom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtnoregisbpomFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtnoregisbpomFocusLost(evt);
            }
        });

        jLabel7.setText("Nama Obat");

        jLabel13.setText("Harga Jual");

        jLabel14.setText("Harga Beli");

        jLabel16.setText("Stok");

        jLabel18.setText("Kemasan");

        jLabel19.setText("Kategori Obat");

        jLabel20.setText("Suplayer");

        jLabel47.setText("Golongan");

        jLabel48.setText("No Regis BPOM");

        jButton4.setText("Save");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setText("Edit");

        jButton7.setText("Hapus");

        jButton8.setText("Cetak");

        cbkategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbkategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbkategoriActionPerformed(evt);
            }
        });

        cbsuplayer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbgolongan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Keras", "Ringan", "B Aja" }));
        cbgolongan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbgolonganActionPerformed(evt);
            }
        });

        jLabel49.setText("Kode Obat");

        txtkodeobat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtkodeobatFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtkodeobatFocusLost(evt);
            }
        });

        javax.swing.GroupLayout pObatLayout = new javax.swing.GroupLayout(pObat);
        pObat.setLayout(pObatLayout);
        pObatLayout.setHorizontalGroup(
            pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pObatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pObatLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(645, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pObatLayout.createSequentialGroup()
                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pObatLayout.createSequentialGroup()
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtnoregisbpom, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(151, 151, 151))
                            .addGroup(pObatLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pObatLayout.createSequentialGroup()
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txthargajual, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pObatLayout.createSequentialGroup()
                                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(70, 70, 70)
                                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(pObatLayout.createSequentialGroup()
                                                .addComponent(jLabel16)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txtstok, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(pObatLayout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(10, 10, 10)
                                                .addComponent(txtnamaobat, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(pObatLayout.createSequentialGroup()
                                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txthargabeli, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(pObatLayout.createSequentialGroup()
                                        .addComponent(jLabel49)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtkodeobat, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pObatLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pObatLayout.createSequentialGroup()
                                                .addComponent(jLabel18)
                                                .addGap(51, 51, 51))
                                            .addGroup(pObatLayout.createSequentialGroup()
                                                .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtkemasan, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                            .addComponent(cbkategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cbsuplayer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cbgolongan, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(pObatLayout.createSequentialGroup()
                                        .addGap(59, 59, 59)
                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(16, 16, 16))
                    .addGroup(pObatLayout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(6, 6, 6))))
        );
        pObatLayout.setVerticalGroup(
            pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pObatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addGap(22, 22, 22)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtkodeobat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49))
                .addGap(18, 18, 18)
                .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pObatLayout.createSequentialGroup()
                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtnamaobat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txthargajual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txthargabeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtstok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)))
                    .addGroup(pObatLayout.createSequentialGroup()
                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtkemasan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbkategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addGap(18, 18, 18)
                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(cbsuplayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(cbgolongan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnoregisbpom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48))
                .addGap(37, 37, 37)
                .addGroup(pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(94, Short.MAX_VALUE))
        );

        getContentPane().add(pObat);
        pObat.setBounds(181, 66, 760, 627);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtcariSuplayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcariSuplayerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcariSuplayerActionPerformed

    private void txtnamaperusahaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnamaperusahaanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnamaperusahaanActionPerformed

    private void txtnokontakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnokontakActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnokontakActionPerformed

    private void txtnamabankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnamabankActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnamabankActionPerformed

    private void txtnotlpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnotlpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnotlpActionPerformed

    private void txtnorekeningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnorekeningActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnorekeningActionPerformed

    private void txtidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidActionPerformed

    private void jTablesuplayerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablesuplayerMouseClicked
        dataterpilihSuplayer();                // TODO add your handling code here:
    }//GEN-LAST:event_jTablesuplayerMouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        tambahSuplayer();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        editSuplayer();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        deleteSuplayer();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        int option = JOptionPane.showConfirmDialog(this, 
        "Apakah Anda yakin ingin keluar?", 
        "Konfirmasi Keluar", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jtestSuplayer4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer4MouseExited
        jtestSuplayer4.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer4MouseExited

    private void jtestSuplayer4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer4MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtestSuplayer4.setBorder(border);        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer4MouseEntered

    private void jtestSuplayer4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer4MouseClicked
        pHome.setVisible(false);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(true);
        pPembelian.setVisible(false);
        pSuplayer.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer4MouseClicked

    private void jtestSuplayer5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer5MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer5MouseExited

    private void jtestSuplayer5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer5MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer5MouseEntered

    private void jtestSuplayer5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer5MouseClicked
        pHome.setVisible(false);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pSuplayer.setVisible(true);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer5MouseClicked

    private void jtestSuplayer2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer2MouseExited
        jtestSuplayer2.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer2MouseExited

    private void jtestSuplayer2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer2MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtestSuplayer2.setBorder(border);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer2MouseEntered

    private void jtestSuplayer2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer2MouseClicked
        pHome.setVisible(false);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(true);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pSuplayer.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer2MouseClicked

    private void jtestSuplayer1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer1MouseExited
        jtestSuplayer1.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer1MouseExited

    private void jtestSuplayer1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer1MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtestSuplayer1.setBorder(border);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer1MouseEntered

    private void jtestSuplayer1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer1MouseClicked
        pHome.setVisible(false);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pSuplayer.setVisible(true);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer1MouseClicked

    private void jtest3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest3MouseExited
        jtest3.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtest3MouseExited

    private void jtest3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest3MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtest3.setBorder(border);// TODO add your handling code here:
    }//GEN-LAST:event_jtest3MouseEntered

    private void jtest3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest3MouseClicked
        pHome.setVisible(false);
        pKategori.setVisible(false);
        pRfobat.setVisible(true);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pSuplayer.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtest3MouseClicked

    private void jtest2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest2MouseExited
        jtest2.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtest2MouseExited

    private void jtest2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest2MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtest2.setBorder(border);// TODO add your handling code here:
    }//GEN-LAST:event_jtest2MouseEntered

    private void jtest2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest2MouseClicked
        pHome.setVisible(true);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pSuplayer.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtest2MouseClicked

    private void jtestSuplayerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayerMouseExited
        jtestSuplayer.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayerMouseExited

    private void jtestSuplayerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayerMouseEntered

        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtestSuplayer.setBorder(border);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayerMouseEntered

    private void jtestSuplayerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayerMouseClicked
        pKategori.setVisible(false);
        pHome.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(true);
        pSuplayer.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayerMouseClicked

    private void jtestSuplayer3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer3MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer3MouseExited

    private void jtestSuplayer3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer3MouseEntered

    private void jtestSuplayer3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer3MouseClicked
        pHome.setVisible(false);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pSuplayer.setVisible(true);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer3MouseClicked

    private void jtestKategoriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestKategoriMouseExited
        jtestKategori.setBorder(null);        // TODO add your handling code here:
    }//GEN-LAST:event_jtestKategoriMouseExited

    private void jtestKategoriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestKategoriMouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtestKategori.setBorder(border);        // TODO add your handling code here:
    }//GEN-LAST:event_jtestKategoriMouseEntered

    private void jtestKategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestKategoriMouseClicked
        pKategori.setVisible(true);
        pHome.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pSuplayer.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtestKategoriMouseClicked

    private void txtnamaobatFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtnamaobatFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnamaobatFocusGained

    private void txtnamaobatFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtnamaobatFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnamaobatFocusLost

    private void txtkemasanFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtkemasanFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtkemasanFocusGained

    private void txtkemasanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtkemasanFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtkemasanFocusLost

    private void txthargajualFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txthargajualFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txthargajualFocusGained

    private void txthargajualFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txthargajualFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txthargajualFocusLost

    private void txthargabeliFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txthargabeliFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txthargabeliFocusGained

    private void txthargabeliFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txthargabeliFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txthargabeliFocusLost

    private void txtstokFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtstokFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtstokFocusGained

    private void txtstokFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtstokFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtstokFocusLost

    private void txtnoregisbpomFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtnoregisbpomFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnoregisbpomFocusGained

    private void txtnoregisbpomFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtnoregisbpomFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnoregisbpomFocusLost

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        tambahObat();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cbkategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbkategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbkategoriActionPerformed

    private void txthargajualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthargajualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthargajualActionPerformed

    private void jButton9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9MouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try{
            String reportPath = "src/view/dataSuplayer.jasper";
            HashMap<String, Object> parameters = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(reportPath,parameters, conn);
            JasperViewer viewer = new JasperViewer(print,false);
            viewer.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void txtcariSuplayerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariSuplayerKeyPressed
        cariSuplayer();        // TODO add your handling code here:
    }//GEN-LAST:event_txtcariSuplayerKeyPressed

    private void cbgolonganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbgolonganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbgolonganActionPerformed

    private void txtkodeobatFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtkodeobatFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtkodeobatFocusGained

    private void txtkodeobatFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtkodeobatFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtkodeobatFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbgolongan;
    private javax.swing.JComboBox<String> cbkategori;
    private javax.swing.JComboBox<String> cbsuplayer;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTablesuplayer;
    private javax.swing.JPanel jtest2;
    private javax.swing.JPanel jtest3;
    private javax.swing.JPanel jtestKategori;
    private javax.swing.JPanel jtestSuplayer;
    private javax.swing.JPanel jtestSuplayer1;
    private javax.swing.JPanel jtestSuplayer2;
    private javax.swing.JPanel jtestSuplayer3;
    private javax.swing.JPanel jtestSuplayer4;
    private javax.swing.JPanel jtestSuplayer5;
    private javax.swing.JPanel pHome;
    private javax.swing.JPanel pKategori;
    private javax.swing.JPanel pObat;
    private javax.swing.JPanel pPembelian;
    private javax.swing.JPanel pPenjualan;
    private javax.swing.JPanel pRfobat;
    private javax.swing.JPanel pSuplayer;
    private javax.swing.JPanel pn_navbar;
    private javax.swing.JPanel pn_sidebar;
    private javax.swing.JTable tbObat;
    private javax.swing.JTextArea txtalamat;
    private javax.swing.JTextField txtcariSuplayer;
    private javax.swing.JTextField txthargabeli;
    private javax.swing.JTextField txthargajual;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtkemasan;
    private javax.swing.JTextField txtkodeobat;
    private javax.swing.JTextField txtnamabank;
    private javax.swing.JTextField txtnamaobat;
    private javax.swing.JTextField txtnamaperusahaan;
    private javax.swing.JTextField txtnokontak;
    private javax.swing.JTextField txtnoregisbpom;
    private javax.swing.JTextField txtnorekening;
    private javax.swing.JTextField txtnotlp;
    private javax.swing.JTextField txtstok;
    // End of variables declaration//GEN-END:variables

    private void loadData() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
