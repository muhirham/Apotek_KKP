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
public class superadmin extends javax.swing.JFrame {
    private Connection conn = new koneksi().connect();
    private DefaultTableModel tabmode;
    private DefaultTableModel tabmodeKategori;



    /**
     * Creates new form test
     */
    public superadmin() {
        initComponents();
        pHome.setVisible(true);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pUser.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        
        datatableKategori();
        datatableUser();
        
        
        setLocationRelativeTo(null);
    }
    
    
     protected void datatableKategori(){
        Object[] Baris = {"Nomor", "Nama Kategori"};
        tabmodeKategori = new DefaultTableModel(null, Baris);
        jTablekategori.setModel(tabmodeKategori);
        String sql = "select * from kategori_Obat order by id_kategori asc";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while(hasil.next()){
                String id = hasil.getString("id_kategori");
                String nama_kategori = hasil.getString("nama_kategori");
                
                String[] data = {id,nama_kategori};
                tabmodeKategori.addRow(data);
            }
        }catch(Exception e){
            System.err.println("Error: "+e.getMessage());
        }
    }
     
     protected void tambahKategori() {
    String namaKategori = txtktgri.getText().trim();

    if (namaKategori.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Nama Kategori tidak boleh kosong");
        return;
    }

    String sql = "INSERT INTO kategori_Obat (nama_kategori) VALUES (?)";

    try (PreparedStatement stat = conn.prepareStatement(sql)) {
        stat.setString(1, namaKategori);
        stat.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        clearKategori();
        datatableKategori();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal menambahkan data: " + e.getMessage());
    }
}

    protected void editKategori() {
    String id = txtId.getText(); // Field ini non-editable, otomatis terisi dari tabel
    String namaKategori = txtktgri.getText().trim();

    if (id.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Pilih data dari tabel terlebih dahulu");
        return;
    }

    String sql = "UPDATE kategori_Obat SET nama_kategori=? WHERE id_kategori=?";

    try (PreparedStatement stat = conn.prepareStatement(sql)) {
        stat.setString(1, namaKategori);
        stat.setString(2, id);
        stat.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data berhasil diperbarui");
        clearKategori();
        datatableKategori();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal mengubah data: " + e.getMessage());
    }
}
    
    
    protected void hapusKategori() {
    String id = txtId.getText();

    if (id.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Pilih data dari tabel terlebih dahulu");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        String sql = "DELETE FROM kategori_Obat WHERE id_kategori=?";

        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, id);
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
            clearKategori();
            datatableKategori();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus data: " + e.getMessage());
        }
    }
}
    
        protected void dataterpilihKategori() {
            int bos = jTablekategori.getSelectedRow();
                String idKategori = tabmodeKategori.getValueAt(bos, 0).toString(); // Kolom pertama
                String namaKategori = tabmodeKategori.getValueAt(bos, 1).toString(); // Kolom kedua

                txtId.setText(idKategori);
                txtktgri.setText(namaKategori);
            }
        

        protected void cariKategori(){
        Object[] Baris = {"Nomor", "Nama Kategori"};
        tabmodeKategori = new DefaultTableModel(null, Baris);
        jTablekategori.setModel(tabmodeKategori);
        String keyword = txtcarikategori.getText();
        String sql = "select * from kategori_Obat where id_kategori like '%"+keyword+"%' or nama_kategori like '%"+keyword+"%' order by id_kategori asc";
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while(hasil.next()){
                String a = hasil.getString("id_kategori");
                String b = hasil.getString("nama_kategori");
                
                    String[] data = {a,b};
                tabmodeKategori.addRow(data);
            }
        }catch(Exception e){
        }
        }
    
        protected void datatableUser() {
        // Define the column names as per the previous array
        Object[] Baris = {"id","nama","no_identitas","alamat","no_induk_karyawan","no_telp","username","password","role","created_at"};
        tabmode = new DefaultTableModel(null, Baris);
        tableUser.setModel(tabmode);

        // SQL query to fetch the data from the 'suplier' table (ensure table name is correct)
        String sql = "SELECT * FROM users ORDER BY id ASC"; 
        try {
            // Create a Statement to execute the query
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);

            // Loop through the ResultSet and populate the table
            while (hasil.next()) {
                // Get the data for each row from the result set
                String id = hasil.getString("id");
                String nama = hasil.getString("nama");
                String no_id = hasil.getString("no_identitas");
                String alamat = hasil.getString("alamat");
                String NIK = hasil.getString("no_induk_karyawan");
                String no_tlp = hasil.getString("no_tlp");
                String user = hasil.getString("username");
                String pass = hasil.getString("password");
                String rol = hasil.getString("role");
                String date = hasil.getString("created_at");

                // Add the data to the table model
                String[] data = {id, nama,no_id,alamat,NIK,no_tlp,user,pass,rol,date};
                tabmode.addRow(data);
            }
        } catch (Exception e) {
            // Print any errors
            System.err.println("Error: " + e.getMessage());
        }
    }
        
    
     protected void dataterpilihUser() {        
        int bar = tableUser.getSelectedRow();  // Get the selected row index
        String Id = tabmode.getValueAt(bar, 0).toString();
        String name = tabmode.getValueAt(bar, 1).toString();
        String noIdent = tabmode.getValueAt(bar, 2).toString();
        String NIK = tabmode.getValueAt(bar, 4).toString();
        String no_Tlp = tabmode.getValueAt(bar, 5).toString();
        String user = tabmode.getValueAt(bar, 6).toString();
        String pass = tabmode.getValueAt(bar, 7).toString();
        String rol = tabmode.getValueAt(bar, 8).toString();
        String Almt = tabmode.getValueAt(bar, 3).toString();
        String date = tabmode.getValueAt(bar, 9).toString();

        // Set the retrieved data into respective input fields
        id.setText(Id);
        nama.setText(name);
        no_idnt.setText(noIdent);  // Assuming you have a field to store the address
        nik.setText(NIK);  // Assuming you have a field for the contact person
        no_telp.setText(no_Tlp);  // Assuming you have a field for account number
        username.setText(user);  // Assuming you have a field for phone number
        password.setText(pass);  // Assuming you have a field for the bank name
        rolle.setSelectedItem(rol);
        almt.setText(Almt);
        Date.setText(date);
    }
     

     
     protected void tambahUser() {
    // Ambil data dari field
    String namaUser = nama.getText();
    String noIdentitas = no_idnt.getText();
    String alamat = almt.getText();
    String nikUser = nik.getText();
    String noTlp = no_telp.getText();
    String user = username.getText();
    String pass = password.getText();
    String role = rolle.getSelectedItem().toString();
    

    String sql = "INSERT INTO users (nama, no_identitas, alamat, no_induk_karyawan, no_tlp, username, password, role) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try {
        Connection conn = new koneksi().connect(); // panggil koneksi
        PreparedStatement stat = conn.prepareStatement(sql);

        stat.setString(1, namaUser);
        stat.setString(2, noIdentitas);
        stat.setString(3, alamat);
        stat.setString(4, nikUser);
        stat.setString(5, noTlp);
        stat.setString(6, user);
        stat.setString(7, pass);
        stat.setString(8, role);        

        stat.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");

        // opsional: refresh tabel setelah tambah
        datatableUser(); // method untuk reload isi tabel

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal tambah data: " + e.getMessage());
        e.printStackTrace();
    }
}
    protected void editUser() {
    String idUser = id.getText(); // Ambil ID dari field
    if (idUser.isEmpty()) {
        JOptionPane.showMessageDialog(null, "ID Tidak Ditemukan.");
        return;
    }

    String sql = "UPDATE users SET nama=?, no_identitas=?, alamat=?, no_induk_karyawan=?, no_tlp=?, username=?, password=?, role=? WHERE id=?";

    try {
        conn.setAutoCommit(false);

        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, nama.getText());
            stat.setString(2, no_idnt.getText());
            stat.setString(3, almt.getText());
            stat.setString(4, nik.getText());
            stat.setString(5, no_telp.getText());
            stat.setString(6, username.getText());
            stat.setString(7, password.getText());
            stat.setString(8, rolle.getSelectedItem().toString());
            stat.setString(9, idUser);

            // Debug log
            System.out.println("Edit User - ID: " + idUser);

            stat.executeUpdate();
            conn.commit();

            JOptionPane.showMessageDialog(null, "Data User Berhasil Diubah");
            clearUser(); // Kosongkan field
            datatableUser(); // Refresh tabel

        } catch (SQLException e) {
            conn.rollback();
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah: " + e.getMessage());
        } finally {
            conn.setAutoCommit(true);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Kesalahan Koneksi: " + e.getMessage());
    }
}


    protected void hapusUser() {
    int konfirmasi = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
    if (konfirmasi == JOptionPane.YES_OPTION) {
        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, id.getText());
            stat.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data User Berhasil Dihapus");
            clearUser(); // Kosongkan field
            datatableUser(); // Refresh tabel
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus: " + e.getMessage());
        }
    }
}

protected void cariUser(){
    Object[] Baris = {"ID", "Nama", "No. Identitas", "Alamat", "No. Induk Karyawan", "No. Tlp", "Username", "Password", "Role", "Created At"};
    tabmode = new DefaultTableModel(null, Baris);
    tableUser.setModel(tabmode);

    String keyword = searchUser.getText(); // Ganti dengan nama JTextField pencarian milikmu

    String sql = "SELECT * FROM users WHERE " +
                 "id LIKE '%" + keyword + "%' OR " +
                 "nama LIKE '%" + keyword + "%' OR " +
                 "no_induk_karyawan LIKE '%" + keyword + "%' " +
                 "ORDER BY id ASC";
    try {
        Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);

        while (hasil.next()) {
            String id = hasil.getString("id");
            String nama = hasil.getString("nama");
            String noId = hasil.getString("no_identitas");
            String alamat = hasil.getString("alamat");
            String nik = hasil.getString("no_induk_karyawan");
            String noTlp = hasil.getString("no_tlp");
            String username = hasil.getString("username");
            String password = hasil.getString("password");
            String role = hasil.getString("role");
            String createdAt = hasil.getString("created_at");

            String[] data = {id, nama, noId, alamat, nik, noTlp, username, password, role, createdAt};
            tabmode.addRow(data);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mencari: " + e.getMessage());
    }
}

        protected void clearUser() {
        id.setText(null);
        nama.setText(null);
        no_idnt.setText(null);  // Assuming you have a field to store the address
        nik.setText(null);  // Assuming you have a field for the contact person
        no_telp.setText(null);  // Assuming you have a field for account number
        username.setText(null);  // Assuming you have a field for phone number
        password.setText(null);  // Assuming you have a field for the bank name
        rolle.setSelectedItem(null);
        almt.setText(null);
        Date.setText(null);// Jika Anda punya field tanggal (created_at), kosongkan juga
}
        
        protected void clearKategori() {
        txtId.setText(null); // ID sebaiknya auto dari database
        txtktgri.setText(null);
    }


     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        pn_navbar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pn_sidebar = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jtest2 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jtestKategori = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jtest3 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jtestKategori1 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jtest4 = new javax.swing.JPanel();
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
        jtestSuplayer = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jtestSuplayer3 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jtestSuplayer6 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        pHome = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        pKategori = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablekategori = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtktgri = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        txtcarikategori = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        pUser = new javax.swing.JPanel();
        id = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        nama = new javax.swing.JTextField();
        a = new javax.swing.JLabel();
        no_idnt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        alamat = new javax.swing.JLabel();
        nik = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        no_telp = new javax.swing.JTextField();
        no = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        rolle = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableUser = new javax.swing.JTable();
        Date = new javax.swing.JTextField();
        jf = new javax.swing.JLabel();
        save = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        jf1 = new javax.swing.JLabel();
        searchUser = new javax.swing.JTextField();
        almt = new javax.swing.JTextField();
        pRfobat = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        pObat = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        pPenjualan = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        pPembelian = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(0, 0));

        pn_navbar.setBackground(new java.awt.Color(0, 0, 0));
        pn_navbar.setForeground(new java.awt.Color(204, 255, 255));
        pn_navbar.setMaximumSize(new java.awt.Dimension(1280, 800));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo_apotek-removebg-preview.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-exit-40.png"))); // NOI18N
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 679, Short.MAX_VALUE)
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

        pn_sidebar.setBackground(new java.awt.Color(255, 255, 255));
        pn_sidebar.setMaximumSize(new java.awt.Dimension(1280, 800));
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
        pn_sidebar.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 202, 139, -1));

        jLabel6.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 153, 255));
        jLabel6.setText("Laporan");
        pn_sidebar.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 304, 139, -1));

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

        jtestKategori1.setBackground(new java.awt.Color(255, 255, 255));
        jtestKategori1.setForeground(new java.awt.Color(204, 204, 204));
        jtestKategori1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtestKategori1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtestKategori1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtestKategori1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtestKategori1MouseExited(evt);
            }
        });
        jtestKategori1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-categories-30.png"))); // NOI18N
        jtestKategori1.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel32.setText("Kategori Obat");
        jtestKategori1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        pn_sidebar.add(jtestKategori1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 170, 30));

        jtest4.setBackground(new java.awt.Color(255, 255, 255));
        jtest4.setForeground(new java.awt.Color(204, 204, 204));
        jtest4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtest4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtest4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtest4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtest4MouseExited(evt);
            }
        });
        jtest4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-form-30.png"))); // NOI18N
        jtest4.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel38.setText("RF Obat");
        jtest4.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        pn_sidebar.add(jtest4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 170, 30));

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

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-users-30.png"))); // NOI18N
        jtestSuplayer2.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel40.setText("Data User");
        jtestSuplayer2.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        pn_sidebar.add(jtestSuplayer2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 170, 30));

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

        pn_sidebar.add(jtestSuplayer4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, 170, 30));

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

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-buy-30.png"))); // NOI18N
        jtestSuplayer.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel42.setText("Pembelian Obat");
        jtestSuplayer.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 90, 30));

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

        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-sell-30.png"))); // NOI18N
        jtestSuplayer3.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel48.setText("Penjualan Obat");
        jtestSuplayer3.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        jtestSuplayer.add(jtestSuplayer3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 170, 30));

        pn_sidebar.add(jtestSuplayer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 170, 30));

        jtestSuplayer6.setBackground(new java.awt.Color(255, 255, 255));
        jtestSuplayer6.setForeground(new java.awt.Color(204, 204, 204));
        jtestSuplayer6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jtestSuplayer6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtestSuplayer6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtestSuplayer6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jtestSuplayer6MouseExited(evt);
            }
        });
        jtestSuplayer6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-medicines-30.png"))); // NOI18N
        jtestSuplayer6.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 30));

        jLabel50.setText("Data Obat");
        jtestSuplayer6.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 80, 30));

        pn_sidebar.add(jtestSuplayer6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 170, 30));

        pHome.setBackground(new java.awt.Color(255, 0, 102));
        pHome.setMaximumSize(new java.awt.Dimension(1280, 800));

        jLabel14.setText("DASHBOARD SUPER ADMIN");

        javax.swing.GroupLayout pHomeLayout = new javax.swing.GroupLayout(pHome);
        pHome.setLayout(pHomeLayout);
        pHomeLayout.setHorizontalGroup(
            pHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addContainerGap(598, Short.MAX_VALUE))
        );
        pHomeLayout.setVerticalGroup(
            pHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pHomeLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel14)
                .addContainerGap(536, Short.MAX_VALUE))
        );

        pKategori.setBackground(new java.awt.Color(102, 204, 255));
        pKategori.setMaximumSize(new java.awt.Dimension(1280, 800));
        pKategori.setPreferredSize(new java.awt.Dimension(752, 572));

        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("KATEGORI");

        jTablekategori.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "No", "Nama Kategori"
            }
        ));
        jTablekategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablekategoriMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTablekategori);

        jButton1.setText("Cetak brok");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("ID");

        txtId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtId.setToolTipText("");

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Nama Kategori");

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Edit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Delete");
        jButton4.setToolTipText("");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        txtcarikategori.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcarikategoriKeyReleased(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Cari Kategori");

        javax.swing.GroupLayout pKategoriLayout = new javax.swing.GroupLayout(pKategori);
        pKategori.setLayout(pKategoriLayout);
        pKategoriLayout.setHorizontalGroup(
            pKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pKategoriLayout.createSequentialGroup()
                .addGroup(pKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pKategoriLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jButton1))
                    .addGroup(pKategoriLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pKategoriLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtktgri, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(187, Short.MAX_VALUE))
            .addGroup(pKategoriLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel15)
                .addGap(292, 292, 292)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtcarikategori)
                .addGap(39, 39, 39))
        );
        pKategoriLayout.setVerticalGroup(
            pKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pKategoriLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtcarikategori, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(pKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pKategoriLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jButton1)))
                .addGap(55, 55, 55)
                .addGroup(pKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtktgri, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58)
                .addGroup(pKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(197, Short.MAX_VALUE))
        );

        pUser.setBackground(new java.awt.Color(0, 153, 153));
        pUser.setMaximumSize(new java.awt.Dimension(1280, 800));
        pUser.setPreferredSize(new java.awt.Dimension(748, 531));

        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel8.setText("ID");

        nama.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        a.setText("Nama");

        no_idnt.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel10.setText("No Identitas");

        jLabel11.setText("Create User");

        alamat.setText("No Induk Karyawan");

        nik.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel13.setText("Password");

        jLabel17.setText("Role");

        jLabel22.setText("Alamat");

        jLabel21.setText("Username");

        no_telp.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        no.setText("No Telp");

        rolle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "kasir", "superadmin", " " }));

        tableUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Nama", "No Identitas", "No Induk Karyawan", "No Telp", "Username", "Password", "Role", "Alamat", "Date"
            }
        ));
        tableUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableUserMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableUser);

        Date.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jf.setText("Date");

        save.setText("Save");
        save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveMouseClicked(evt);
            }
        });

        edit.setText("Edit");
        edit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editMouseClicked(evt);
            }
        });
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        jf1.setText("Search");

        searchUser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchUserActionPerformed(evt);
            }
        });
        searchUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchUserKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pUserLayout = new javax.swing.GroupLayout(pUser);
        pUser.setLayout(pUserLayout);
        pUserLayout.setHorizontalGroup(
            pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pUserLayout.createSequentialGroup()
                .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pUserLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 749, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pUserLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(a, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(no, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alamat, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(no_idnt)
                            .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(nik, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(no_telp, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pUserLayout.createSequentialGroup()
                                .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rolle, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pUserLayout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(almt))
                            .addGroup(pUserLayout.createSequentialGroup()
                                .addComponent(jf, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Date, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pUserLayout.createSequentialGroup()
                                .addComponent(jf1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchUser, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(23, 23, 23)))
                .addGap(51, 51, 51))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pUserLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(324, 324, 324))
            .addGroup(pUserLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pUserLayout.setVerticalGroup(
            pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pUserLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pUserLayout.createSequentialGroup()
                        .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(a, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(no_idnt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alamat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nik, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(no, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(no_telp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pUserLayout.createSequentialGroup()
                        .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pUserLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rolle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pUserLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(almt, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jf, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Date, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jf1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        pRfobat.setBackground(new java.awt.Color(51, 51, 255));
        pRfobat.setMaximumSize(new java.awt.Dimension(1280, 800));

        jLabel16.setText("Request Form Obat");

        javax.swing.GroupLayout pRfobatLayout = new javax.swing.GroupLayout(pRfobat);
        pRfobat.setLayout(pRfobatLayout);
        pRfobatLayout.setHorizontalGroup(
            pRfobatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRfobatLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel16)
                .addContainerGap(627, Short.MAX_VALUE))
        );
        pRfobatLayout.setVerticalGroup(
            pRfobatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRfobatLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel16)
                .addContainerGap(537, Short.MAX_VALUE))
        );

        pObat.setBackground(new java.awt.Color(102, 0, 51));
        pObat.setMaximumSize(new java.awt.Dimension(1280, 800));

        jLabel18.setText("Form Obat");

        javax.swing.GroupLayout pObatLayout = new javax.swing.GroupLayout(pObat);
        pObat.setLayout(pObatLayout);
        pObatLayout.setHorizontalGroup(
            pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pObatLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel18)
                .addContainerGap(668, Short.MAX_VALUE))
        );
        pObatLayout.setVerticalGroup(
            pObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pObatLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel18)
                .addContainerGap(540, Short.MAX_VALUE))
        );

        pPenjualan.setBackground(new java.awt.Color(255, 0, 255));
        pPenjualan.setMaximumSize(new java.awt.Dimension(1280, 800));

        jLabel19.setText("Form Penjualan Obat");

        javax.swing.GroupLayout pPenjualanLayout = new javax.swing.GroupLayout(pPenjualan);
        pPenjualan.setLayout(pPenjualanLayout);
        pPenjualanLayout.setHorizontalGroup(
            pPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPenjualanLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel19)
                .addContainerGap(616, Short.MAX_VALUE))
        );
        pPenjualanLayout.setVerticalGroup(
            pPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPenjualanLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel19)
                .addContainerGap(541, Short.MAX_VALUE))
        );

        pPembelian.setBackground(new java.awt.Color(255, 204, 204));
        pPembelian.setMaximumSize(new java.awt.Dimension(1280, 800));

        jLabel20.setText("Form Pembelian Obat");

        javax.swing.GroupLayout pPembelianLayout = new javax.swing.GroupLayout(pPembelian);
        pPembelian.setLayout(pPembelianLayout);
        pPembelianLayout.setHorizontalGroup(
            pPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPembelianLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel20)
                .addContainerGap(612, Short.MAX_VALUE))
        );
        pPembelianLayout.setVerticalGroup(
            pPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPembelianLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel20)
                .addContainerGap(541, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pn_navbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pn_sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 182, Short.MAX_VALUE)
                    .addComponent(pKategori, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 183, Short.MAX_VALUE)
                    .addComponent(pRfobat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 186, Short.MAX_VALUE)
                    .addComponent(pUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 187, Short.MAX_VALUE)
                    .addComponent(pObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 184, Short.MAX_VALUE)
                    .addComponent(pPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 184, Short.MAX_VALUE)
                    .addComponent(pPembelian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pn_navbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pn_sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(70, Short.MAX_VALUE)
                    .addComponent(pKategori, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(69, Short.MAX_VALUE)
                    .addComponent(pRfobat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(67, 67, 67)
                    .addComponent(pUser, javax.swing.GroupLayout.PREFERRED_SIZE, 575, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(66, 66, 66)
                    .addComponent(pObat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(65, 65, 65)
                    .addComponent(pPenjualan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(65, 65, 65)
                    .addComponent(pPembelian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try{
            String reportPath = "src/view/testReport.jasper";
            HashMap<String, Object> parameters = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(reportPath,parameters, conn);
            JasperViewer viewer = new JasperViewer(print,false);
            viewer.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jtest2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest2MouseClicked
        pHome.setVisible(true);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pUser.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtest2MouseClicked

    private void jtest2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest2MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtest2.setBorder(border);// TODO add your handling code here:
    }//GEN-LAST:event_jtest2MouseEntered

    private void jtest2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest2MouseExited
        jtest2.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtest2MouseExited

    private void jtestKategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestKategoriMouseClicked
        pKategori.setVisible(true);
        pHome.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pUser.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtestKategoriMouseClicked

    private void jtestKategoriMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestKategoriMouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtestKategori.setBorder(border);        // TODO add your handling code here:
    }//GEN-LAST:event_jtestKategoriMouseEntered

    private void jtestKategoriMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestKategoriMouseExited
        jtestKategori.setBorder(null);        // TODO add your handling code here:
    }//GEN-LAST:event_jtestKategoriMouseExited

    private void jtest3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest3MouseClicked
        pHome.setVisible(false);
        pKategori.setVisible(false);
        pRfobat.setVisible(true);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pUser.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtest3MouseClicked

    private void jtest3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest3MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtest3.setBorder(border);// TODO add your handling code here:
    }//GEN-LAST:event_jtest3MouseEntered

    private void jtest3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest3MouseExited
        jtest3.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtest3MouseExited

    private void jtestKategori1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestKategori1MouseClicked
        pKategori.setVisible(true);
        pHome.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pUser.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtestKategori1MouseClicked

    private void jtestKategori1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestKategori1MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtestKategori.setBorder(border);        // TODO add your handling code here:
    }//GEN-LAST:event_jtestKategori1MouseEntered

    private void jtestKategori1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestKategori1MouseExited
        jtestKategori.setBorder(null);        // TODO add your handling code here:
    }//GEN-LAST:event_jtestKategori1MouseExited

    private void jtest4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest4MouseClicked
        pHome.setVisible(false);
        pKategori.setVisible(false);
        pRfobat.setVisible(true);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pUser.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtest4MouseClicked

    private void jtest4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest4MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtest3.setBorder(border);// TODO add your handling code here:
    }//GEN-LAST:event_jtest4MouseEntered

    private void jtest4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtest4MouseExited
        jtest3.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtest4MouseExited

    private void jtestSuplayer2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer2MouseClicked
        pKategori.setVisible(false);
        pHome.setVisible(false);
        pRfobat.setVisible(false);
        pUser.setVisible(true);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate(); 
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer2MouseClicked

    private void jtestSuplayer2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer2MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtestSuplayer2.setBorder(border);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer2MouseEntered

    private void jtestSuplayer2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer2MouseExited
        jtestSuplayer2.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer2MouseExited

    private void jtestSuplayer5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer5MouseClicked
        pHome.setVisible(false);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pUser.setVisible(true);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer5MouseClicked

    private void jtestSuplayer5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer5MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer5MouseEntered

    private void jtestSuplayer5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer5MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer5MouseExited

    private void jtestSuplayer4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer4MouseClicked
        pHome.setVisible(false);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(true);
        pPembelian.setVisible(false);
        pUser.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer4MouseClicked

    private void jtestSuplayer4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer4MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtestSuplayer4.setBorder(border);        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer4MouseEntered

    private void jtestSuplayer4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer4MouseExited
        jtestSuplayer4.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer4MouseExited

    private void jtestSuplayer3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer3MouseClicked
        pHome.setVisible(false);
        pKategori.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pUser.setVisible(true);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer3MouseClicked

    private void jtestSuplayer3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer3MouseEntered

    private void jtestSuplayer3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer3MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer3MouseExited

    private void jtestSuplayerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayerMouseClicked
        pKategori.setVisible(false);
        pHome.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(false);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(true);
        pUser.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayerMouseClicked

    private void jtestSuplayerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayerMouseEntered

        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtestSuplayer.setBorder(border);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayerMouseEntered

    private void jtestSuplayerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayerMouseExited
        jtestSuplayer.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayerMouseExited

    private void jtestSuplayer6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer6MouseClicked
        pKategori.setVisible(false);
        pHome.setVisible(false);
        pRfobat.setVisible(false);
        pObat.setVisible(true);
        pPenjualan.setVisible(false);
        pPembelian.setVisible(false);
        pUser.setVisible(false);

        // Refresh tampilan setelah perubahan
        revalidate();
        repaint();        // TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer6MouseClicked

    private void jtestSuplayer6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer6MouseEntered
        Border border = BorderFactory.createLineBorder(new Color(102, 102, 102), 3); // Warna grey dengan ketebalan 3px
        jtestSuplayer6.setBorder(border);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer6MouseEntered

    private void jtestSuplayer6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtestSuplayer6MouseExited
        jtestSuplayer6.setBorder(null);// TODO add your handling code here:
    }//GEN-LAST:event_jtestSuplayer6MouseExited

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        int option = JOptionPane.showConfirmDialog(this, 
        "Apakah Anda yakin ingin keluar?", 
        "Konfirmasi Keluar", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            dispose();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

    private void tableUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableUserMouseClicked
        // TODO add your handling code here:
        dataterpilihUser();
    }//GEN-LAST:event_tableUserMouseClicked

    private void saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseClicked
        // TODO add your handling code here:
        tambahUser();
    }//GEN-LAST:event_saveMouseClicked

    private void editMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_editMouseClicked

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        // TODO add your handling code here:
        editUser();
    }//GEN-LAST:event_editActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        // TODO add your handling code here:
        hapusUser();
    }//GEN-LAST:event_deleteActionPerformed

    private void searchUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchUserActionPerformed

    private void searchUserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchUserKeyPressed
        // TODO add your handling code here:
        cariUser();
    }//GEN-LAST:event_searchUserKeyPressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        hapusKategori();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        tambahKategori();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        editKategori();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTablekategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablekategoriMouseClicked
        // TODO add your handling code here:
        dataterpilihKategori();
    }//GEN-LAST:event_jTablekategoriMouseClicked

    private void txtcarikategoriKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcarikategoriKeyReleased
        // TODO add your handling code here:
        cariKategori();
    }//GEN-LAST:event_txtcarikategoriKeyReleased

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
            java.util.logging.Logger.getLogger(superadmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(superadmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(superadmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(superadmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new superadmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Date;
    private javax.swing.JLabel a;
    private javax.swing.JLabel alamat;
    private javax.swing.JTextField almt;
    private javax.swing.JButton delete;
    private javax.swing.JButton edit;
    private javax.swing.JTextField id;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
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
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTablekategori;
    private javax.swing.JLabel jf;
    private javax.swing.JLabel jf1;
    private javax.swing.JPanel jtest2;
    private javax.swing.JPanel jtest3;
    private javax.swing.JPanel jtest4;
    private javax.swing.JPanel jtestKategori;
    private javax.swing.JPanel jtestKategori1;
    private javax.swing.JPanel jtestSuplayer;
    private javax.swing.JPanel jtestSuplayer2;
    private javax.swing.JPanel jtestSuplayer3;
    private javax.swing.JPanel jtestSuplayer4;
    private javax.swing.JPanel jtestSuplayer5;
    private javax.swing.JPanel jtestSuplayer6;
    private javax.swing.JTextField nama;
    private javax.swing.JTextField nik;
    private javax.swing.JLabel no;
    private javax.swing.JTextField no_idnt;
    private javax.swing.JTextField no_telp;
    private javax.swing.JPanel pHome;
    private javax.swing.JPanel pKategori;
    private javax.swing.JPanel pObat;
    private javax.swing.JPanel pPembelian;
    private javax.swing.JPanel pPenjualan;
    private javax.swing.JPanel pRfobat;
    private javax.swing.JPanel pUser;
    private javax.swing.JPasswordField password;
    private javax.swing.JPanel pn_navbar;
    private javax.swing.JPanel pn_sidebar;
    private javax.swing.JComboBox<String> rolle;
    private javax.swing.JButton save;
    private javax.swing.JTextField searchUser;
    private javax.swing.JTable tableUser;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtcarikategori;
    private javax.swing.JTextField txtktgri;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
