/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permintaan;

import fungsi.BackgroundMusic;
import fungsi.WarnaTable;
import fungsi.akses;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariDokter2;
import simrskhanza.DlgKabupaten;
import simrskhanza.DlgKecamatan;
import simrskhanza.DlgKelurahan;
import simrskhanza.DlgPenanggungJawab;
import simrskhanza.DlgPropinsi;

/**
 *
 * @author windiartonugroho
 */
public class DlgBookingPeriksa extends javax.swing.JFrame {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private String pilihtampil="";
    private String alarm="",nol_detik,detik,pengurutan="",tahun="",bulan="",posisitahun="",awalantahun="",awalanbulan="";
    private boolean aktif=false;
    private int nilai_detik,i=0,bookingbaru=0,p_kelurahan=0,p_kecamatan=0,p_kabupaten=0,p_propinsi=0,kuota=0;
    private BackgroundMusic music;
    private String[] arrSplit;
    private String kelurahan="",kecamatan="",kabupaten="",propinsi="",kdkel="",kdkec="",kdkab="",kdprop="",
                   aktifjadwal="",URUTNOREG="",status="",no_rawat="",umur="",sttsumur="";
    private DlgKabupaten kab=new DlgKabupaten(this,false);
    private DlgPropinsi prop=new DlgPropinsi(this,false);
    private DlgKecamatan kec=new DlgKecamatan(this,false);
    private DlgKelurahan kel=new DlgKelurahan(this,false);
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private DlgCariDokter2 dokter2=new DlgCariDokter2(null,false);
    private DlgPenanggungJawab penjab=new DlgPenanggungJawab(null,false);
    
    
    /**
     * Creates new form DlgBookingPeriksa
     */
    public DlgBookingPeriksa() {
        initComponents();
        
        Object[] row={"No.Booking","Tgl.Booking","Tgl.Periksa","Nama","Alamat","No.Telp","E-Mail","Kode Poli","Unit/Poli","Tambahan Pesan","Status"};
        tabMode=new DefaultTableModel(null,row){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };

        tbObat.setModel(tabMode);
        //tampil();
        //tbJabatan.setDefaultRenderer(Object.class, new WarnaTable(Scroll.getBackground(),Color.GREEN));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 11; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(100);
            }else if(i==1){
                column.setPreferredWidth(117);
            }else if(i==2){
                column.setPreferredWidth(65);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==4){
                column.setPreferredWidth(180);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(150);
            }else if(i==7){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==8){
                column.setPreferredWidth(100);
            }else if(i==9){
                column.setPreferredWidth(180);
            }else if(i==10){
                column.setPreferredWidth(80);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());
        
        penjab.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(penjab.getTable().getSelectedRow()!= -1){
                    kdpnj.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),1).toString());
                    nmpnj.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),2).toString());
                }  
                kdpnj.requestFocus();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        penjab.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    penjab.dispose();
                }  
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        prop.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(prop.getTable().getSelectedRow()!= -1){
                    Propinsi.setText(prop.getTable().getValueAt(prop.getTable().getSelectedRow(),0).toString());
                    kdprop=prop.getTable().getValueAt(prop.getTable().getSelectedRow(),1).toString();
                    Propinsi.requestFocus();
                }     
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        kab.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(kab.getTable().getSelectedRow()!= -1){
                    Kabupaten.setText(kab.getTable().getValueAt(kab.getTable().getSelectedRow(),0).toString());
                    kdkab=kab.getTable().getValueAt(kab.getTable().getSelectedRow(),1).toString();
                    Kabupaten.requestFocus();
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        kec.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(kec.getTable().getSelectedRow()!= -1){
                    Kecamatan.setText(kec.getTable().getValueAt(kec.getTable().getSelectedRow(),0).toString());
                    kdkec=kec.getTable().getValueAt(kec.getTable().getSelectedRow(),1).toString();
                    Kecamatan.requestFocus();
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        kel.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(kel.getTable().getSelectedRow()!= -1){
                    Kelurahan.setText(kel.getTable().getValueAt(kel.getTable().getSelectedRow(),0).toString());
                    kdkel=kel.getTable().getValueAt(kel.getTable().getSelectedRow(),1).toString();
                    Kelurahan.requestFocus();
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {;}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){                    
                    KdDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    NmDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                    isNomer();
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        dokter2.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {;}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter2.getTable().getSelectedRow()!= -1){                    
                    KdDokter.setText(dokter2.getTable().getValueAt(dokter2.getTable().getSelectedRow(),0).toString());
                    NmDokter.setText(dokter2.getTable().getValueAt(dokter2.getTable().getSelectedRow(),1).toString());
                    if(aktifjadwal.equals("aktif")){
                        kuota=Integer.parseInt(dokter2.getTable().getValueAt(dokter2.getTable().getSelectedRow(),13).toString());
                    }
                    isNomer();                        
                }      
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        try {
            aktifjadwal=koneksiDB.JADWALDOKTERDIREGISTRASI();
            URUTNOREG=koneksiDB.URUTNOREG();
        } catch (Exception ex) {
            aktifjadwal="";
            URUTNOREG="";
        }
        
        try {
            alarm=koneksiDB.ALARMBOOKINGPERIKSA();
        } catch (Exception e) {
            alarm="no";
            System.out.println("E : "+e);
        }
        
        try {
            ps=koneksi.prepareStatement("select * from set_alamat_pasien");
            try {
                rs=ps.executeQuery();
                while(rs.next()){
                    Kelurahan.setEditable(rs.getBoolean("kelurahan"));
                    Kecamatan.setEditable(rs.getBoolean("kecamatan"));                    
                    Kabupaten.setEditable(rs.getBoolean("kabupaten"));                    
                    Propinsi.setEditable(rs.getBoolean("propinsi"));
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            
            ps=koneksi.prepareStatement("select * from set_kelengkapan_data_pasien");
            try {
                rs=ps.executeQuery();
                while(rs.next()){
                    kelurahan=rs.getString("kelurahan");
                    p_kelurahan=rs.getInt("p_kelurahan");
                    kecamatan=rs.getString("kecamatan");
                    p_kecamatan=rs.getInt("p_kecamatan");
                    kabupaten=rs.getString("kabupaten");
                    p_kabupaten=rs.getInt("p_kabupaten");
                    propinsi=rs.getString("propinsi");
                    p_propinsi=rs.getInt("p_propinsi");
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            
            ps=koneksi.prepareStatement("select * from set_urut_no_rkm_medis");
            try {
                rs=ps.executeQuery();
                while(rs.next()){
                    pengurutan=rs.getString("urutan");
                    tahun=rs.getString("tahun");
                    bulan=rs.getString("bulan");
                    posisitahun=rs.getString("posisi_tahun_bulan");
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        } 
        
        if(alarm.equals("yes")){
            jam();
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        WindowBalas = new javax.swing.JDialog();
        internalFrame5 = new widget.InternalFrame();
        panelGlass6 = new widget.panelisi();
        BtnSimpan4 = new widget.Button();
        BtnCloseIn4 = new widget.Button();
        scrollPane2 = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        NoBooking = new widget.TextBox();
        jLabel18 = new widget.Label();
        Alamat = new widget.TextBox();
        jLabel15 = new widget.Label();
        jLabel14 = new widget.Label();
        NoTelp = new widget.TextBox();
        jLabel17 = new widget.Label();
        Email = new widget.TextBox();
        ChkRM = new widget.CekBox();
        TNo = new widget.TextBox();
        NamaPasien = new widget.TextBox();
        jLabel3 = new widget.Label();
        jLabel13 = new widget.Label();
        jLabel53 = new widget.Label();
        jLabel19 = new widget.Label();
        Kelurahan = new widget.TextBox();
        BtnKelurahan = new widget.Button();
        BtnKecamatan = new widget.Button();
        Kecamatan = new widget.TextBox();
        Kabupaten = new widget.TextBox();
        BtnKabupaten = new widget.Button();
        Propinsi = new widget.TextBox();
        BtnPropinsi = new widget.Button();
        scrollPane1 = new widget.ScrollPane();
        TambahanPesan = new widget.TextArea();
        jLabel54 = new widget.Label();
        jLabel16 = new widget.Label();
        TanggalBooking = new widget.TextBox();
        TanggalPeriksa = new widget.TextBox();
        jLabel20 = new widget.Label();
        jLabel21 = new widget.Label();
        NmPoli = new widget.TextBox();
        KdPoli = new widget.TextBox();
        jLabel55 = new widget.Label();
        jLabel9 = new widget.Label();
        KdDokter = new widget.TextBox();
        NmDokter = new widget.TextBox();
        BtnDokter = new widget.Button();
        jLabel23 = new widget.Label();
        kdpnj = new widget.TextBox();
        nmpnj = new widget.TextBox();
        btnPenjab = new widget.Button();
        jLabel24 = new widget.Label();
        NoReg = new widget.TextBox();
        Kuota = new widget.TextBox();
        jLabel26 = new widget.Label();
        Kuota1 = new widget.TextBox();
        jLabel27 = new widget.Label();
        jLabel28 = new widget.Label();
        Kuota2 = new widget.TextBox();
        jLabel29 = new widget.Label();
        LabelStatus = new widget.Label();
        jLabel56 = new widget.Label();
        NoRm = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        LCount1 = new widget.Label();
        BtnHapus = new widget.Button();
        BtnJawab = new widget.Button();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();
        panelCari = new widget.panelisi();
        R1 = new widget.RadioButton();
        R2 = new widget.RadioButton();
        DTPCari1 = new widget.Tanggal();
        jLabel22 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        R3 = new widget.RadioButton();
        DTPCari3 = new widget.Tanggal();
        jLabel25 = new widget.Label();
        DTPCari4 = new widget.Tanggal();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();

        WindowBalas.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowBalas.setUndecorated(true);
        WindowBalas.setResizable(false);

        internalFrame5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pengecekan Data Pasien & Ketersediaan Kuota Periksa ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame5.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass6.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan4.setMnemonic('S');
        BtnSimpan4.setText("Simpan");
        BtnSimpan4.setToolTipText("Alt+S");
        BtnSimpan4.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpan4ActionPerformed(evt);
            }
        });
        panelGlass6.add(BtnSimpan4);

        BtnCloseIn4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnCloseIn4.setMnemonic('U');
        BtnCloseIn4.setText("Tutup");
        BtnCloseIn4.setToolTipText("Alt+U");
        BtnCloseIn4.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnCloseIn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseIn4ActionPerformed(evt);
            }
        });
        panelGlass6.add(BtnCloseIn4);

        internalFrame5.add(panelGlass6, java.awt.BorderLayout.PAGE_END);

        scrollPane2.setPreferredSize(new java.awt.Dimension(1093, 138));

        FormInput.setBorder(null);
        FormInput.setPreferredSize(new java.awt.Dimension(775, 168));
        FormInput.setLayout(null);

        NoBooking.setEditable(false);
        NoBooking.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoBookingKeyPressed(evt);
            }
        });
        FormInput.add(NoBooking);
        NoBooking.setBounds(554, 20, 210, 23);

        jLabel18.setText("No.Booking :");
        FormInput.add(jLabel18);
        jLabel18.setBounds(470, 20, 80, 23);

        Alamat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlamatKeyPressed(evt);
            }
        });
        FormInput.add(Alamat);
        Alamat.setBounds(120, 80, 349, 23);

        jLabel15.setText("Alamat Pasien :");
        FormInput.add(jLabel15);
        jLabel15.setBounds(0, 80, 116, 23);

        jLabel14.setText("No.HP/Telp :");
        FormInput.add(jLabel14);
        jLabel14.setBounds(470, 50, 80, 23);

        NoTelp.setEditable(false);
        NoTelp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoTelpKeyPressed(evt);
            }
        });
        FormInput.add(NoTelp);
        NoTelp.setBounds(554, 50, 210, 23);

        jLabel17.setText("E-Mail :");
        FormInput.add(jLabel17);
        jLabel17.setBounds(470, 80, 80, 23);

        Email.setEditable(false);
        Email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EmailKeyPressed(evt);
            }
        });
        FormInput.add(Email);
        Email.setBounds(554, 80, 210, 23);

        ChkRM.setBorder(null);
        ChkRM.setSelected(true);
        ChkRM.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkRM.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkRM.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkRM.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChkRMItemStateChanged(evt);
            }
        });
        FormInput.add(ChkRM);
        ChkRM.setBounds(280, 20, 23, 23);

        TNo.setEditable(false);
        TNo.setBackground(new java.awt.Color(245, 250, 240));
        TNo.setHighlighter(null);
        TNo.setOpaque(true);
        TNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoKeyPressed(evt);
            }
        });
        FormInput.add(TNo);
        TNo.setBounds(120, 20, 160, 23);

        NamaPasien.setEditable(false);
        NamaPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NamaPasienKeyPressed(evt);
            }
        });
        FormInput.add(NamaPasien);
        NamaPasien.setBounds(120, 50, 270, 23);

        jLabel3.setText("No.Rekam Medis :");
        FormInput.add(jLabel3);
        jLabel3.setBounds(0, 20, 116, 23);

        jLabel13.setText("Nama Pasien :");
        FormInput.add(jLabel13);
        jLabel13.setBounds(0, 50, 116, 23);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setText("A. DATA PASIEN :");
        FormInput.add(jLabel53);
        jLabel53.setBounds(10, 0, 180, 23);

        jLabel19.setText("Pesan :");
        FormInput.add(jLabel19);
        jLabel19.setBounds(470, 110, 80, 23);

        Kelurahan.setText("KELURAHAN");
        Kelurahan.setHighlighter(null);
        Kelurahan.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                KelurahanMouseMoved(evt);
            }
        });
        Kelurahan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                KelurahanMouseExited(evt);
            }
        });
        Kelurahan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KelurahanKeyPressed(evt);
            }
        });
        FormInput.add(Kelurahan);
        Kelurahan.setBounds(120, 110, 142, 23);

        BtnKelurahan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnKelurahan.setMnemonic('2');
        BtnKelurahan.setToolTipText("ALt+2");
        BtnKelurahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKelurahanActionPerformed(evt);
            }
        });
        FormInput.add(BtnKelurahan);
        BtnKelurahan.setBounds(265, 110, 28, 23);

        BtnKecamatan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnKecamatan.setMnemonic('3');
        BtnKecamatan.setToolTipText("ALt+3");
        BtnKecamatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKecamatanActionPerformed(evt);
            }
        });
        FormInput.add(BtnKecamatan);
        BtnKecamatan.setBounds(441, 110, 28, 23);

        Kecamatan.setText("KECAMATAN");
        Kecamatan.setHighlighter(null);
        Kecamatan.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                KecamatanMouseMoved(evt);
            }
        });
        Kecamatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                KecamatanMouseExited(evt);
            }
        });
        Kecamatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KecamatanKeyPressed(evt);
            }
        });
        FormInput.add(Kecamatan);
        Kecamatan.setBounds(296, 110, 142, 23);

        Kabupaten.setText("KABUPATEN");
        Kabupaten.setHighlighter(null);
        Kabupaten.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                KabupatenMouseMoved(evt);
            }
        });
        Kabupaten.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                KabupatenMouseExited(evt);
            }
        });
        Kabupaten.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KabupatenKeyPressed(evt);
            }
        });
        FormInput.add(Kabupaten);
        Kabupaten.setBounds(120, 140, 142, 23);

        BtnKabupaten.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnKabupaten.setMnemonic('4');
        BtnKabupaten.setToolTipText("ALt+4");
        BtnKabupaten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKabupatenActionPerformed(evt);
            }
        });
        FormInput.add(BtnKabupaten);
        BtnKabupaten.setBounds(265, 140, 28, 23);

        Propinsi.setText("PROPINSI");
        Propinsi.setHighlighter(null);
        Propinsi.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                PropinsiMouseMoved(evt);
            }
        });
        Propinsi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PropinsiMouseExited(evt);
            }
        });
        Propinsi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PropinsiKeyPressed(evt);
            }
        });
        FormInput.add(Propinsi);
        Propinsi.setBounds(296, 140, 142, 23);

        BtnPropinsi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPropinsi.setMnemonic('4');
        BtnPropinsi.setToolTipText("ALt+4");
        BtnPropinsi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPropinsiActionPerformed(evt);
            }
        });
        FormInput.add(BtnPropinsi);
        BtnPropinsi.setBounds(441, 140, 28, 23);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TambahanPesan.setEditable(false);
        TambahanPesan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TambahanPesan.setColumns(20);
        TambahanPesan.setRows(5);
        TambahanPesan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TambahanPesanKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(TambahanPesan);

        FormInput.add(scrollPane1);
        scrollPane1.setBounds(554, 110, 210, 42);

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel54.setText("B. DATA BOOKING :");
        FormInput.add(jLabel54);
        jLabel54.setBounds(10, 170, 180, 23);

        jLabel16.setText("Tanggal Booking :");
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 190, 116, 23);

        TanggalBooking.setEditable(false);
        TanggalBooking.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalBookingKeyPressed(evt);
            }
        });
        FormInput.add(TanggalBooking);
        TanggalBooking.setBounds(120, 190, 140, 23);

        TanggalPeriksa.setEditable(false);
        TanggalPeriksa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalPeriksaKeyPressed(evt);
            }
        });
        FormInput.add(TanggalPeriksa);
        TanggalPeriksa.setBounds(360, 190, 80, 23);

        jLabel20.setText("Poli Dituju :");
        jLabel20.setPreferredSize(new java.awt.Dimension(74, 14));
        FormInput.add(jLabel20);
        jLabel20.setBounds(442, 190, 70, 23);

        jLabel21.setText("Tanggal Periksa :");
        jLabel21.setPreferredSize(new java.awt.Dimension(74, 14));
        FormInput.add(jLabel21);
        jLabel21.setBounds(260, 190, 96, 23);

        NmPoli.setEditable(false);
        NmPoli.setHighlighter(null);
        NmPoli.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                NmPoliMouseMoved(evt);
            }
        });
        NmPoli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                NmPoliMouseExited(evt);
            }
        });
        NmPoli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NmPoliKeyPressed(evt);
            }
        });
        FormInput.add(NmPoli);
        NmPoli.setBounds(579, 190, 185, 23);

        KdPoli.setEditable(false);
        KdPoli.setHighlighter(null);
        KdPoli.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                KdPoliMouseMoved(evt);
            }
        });
        KdPoli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                KdPoliMouseExited(evt);
            }
        });
        KdPoli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPoliKeyPressed(evt);
            }
        });
        FormInput.add(KdPoli);
        KdPoli.setBounds(516, 190, 60, 23);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel55.setText("C. PENCARIAN JADWAl DOKTER & KUOTA TERSEDIA :");
        FormInput.add(jLabel55);
        jLabel55.setBounds(10, 220, 300, 23);

        jLabel9.setText("Dokter :");
        FormInput.add(jLabel9);
        jLabel9.setBounds(0, 240, 116, 23);

        KdDokter.setEditable(false);
        KdDokter.setHighlighter(null);
        FormInput.add(KdDokter);
        KdDokter.setBounds(120, 240, 100, 23);

        NmDokter.setEditable(false);
        NmDokter.setHighlighter(null);
        FormInput.add(NmDokter);
        NmDokter.setBounds(223, 240, 190, 23);

        BtnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter.setMnemonic('X');
        BtnDokter.setToolTipText("Alt+X");
        BtnDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDokterActionPerformed(evt);
            }
        });
        BtnDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDokterKeyPressed(evt);
            }
        });
        FormInput.add(BtnDokter);
        BtnDokter.setBounds(416, 240, 28, 23);

        jLabel23.setText("Cara Bayar :");
        FormInput.add(jLabel23);
        jLabel23.setBounds(0, 270, 116, 23);

        kdpnj.setEditable(false);
        kdpnj.setHighlighter(null);
        kdpnj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kdpnjActionPerformed(evt);
            }
        });
        FormInput.add(kdpnj);
        kdpnj.setBounds(120, 270, 70, 23);

        nmpnj.setEditable(false);
        FormInput.add(nmpnj);
        nmpnj.setBounds(193, 270, 220, 23);

        btnPenjab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPenjab.setMnemonic('2');
        btnPenjab.setToolTipText("ALt+2");
        btnPenjab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenjabActionPerformed(evt);
            }
        });
        btnPenjab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPenjabKeyPressed(evt);
            }
        });
        FormInput.add(btnPenjab);
        btnPenjab.setBounds(416, 270, 28, 23);

        jLabel24.setText("No.Reg :");
        FormInput.add(jLabel24);
        jLabel24.setBounds(630, 270, 70, 23);

        NoReg.setHighlighter(null);
        NoReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRegKeyPressed(evt);
            }
        });
        FormInput.add(NoReg);
        NoReg.setBounds(704, 270, 60, 23);

        Kuota.setEditable(false);
        Kuota.setHighlighter(null);
        Kuota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KuotaKeyPressed(evt);
            }
        });
        FormInput.add(Kuota);
        Kuota.setBounds(549, 240, 60, 23);

        jLabel26.setText("Kuota :");
        FormInput.add(jLabel26);
        jLabel26.setBounds(465, 240, 80, 23);

        Kuota1.setEditable(false);
        Kuota1.setHighlighter(null);
        Kuota1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Kuota1KeyPressed(evt);
            }
        });
        FormInput.add(Kuota1);
        Kuota1.setBounds(704, 240, 60, 23);

        jLabel27.setText("Pendaftar :");
        FormInput.add(jLabel27);
        jLabel27.setBounds(630, 240, 70, 23);

        jLabel28.setText("Sisa Kuota :");
        FormInput.add(jLabel28);
        jLabel28.setBounds(465, 270, 80, 23);

        Kuota2.setEditable(false);
        Kuota2.setHighlighter(null);
        Kuota2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Kuota2KeyPressed(evt);
            }
        });
        FormInput.add(Kuota2);
        Kuota2.setBounds(549, 270, 60, 23);

        jLabel29.setText("Status :");
        FormInput.add(jLabel29);
        jLabel29.setBounds(0, 300, 116, 23);

        LabelStatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LabelStatus.setText("status");
        FormInput.add(LabelStatus);
        LabelStatus.setBounds(120, 300, 640, 23);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel56.setText("D. BALASAN KE PASIEN :");
        FormInput.add(jLabel56);
        jLabel56.setBounds(10, 330, 300, 23);

        scrollPane2.setViewportView(FormInput);

        internalFrame5.add(scrollPane2, java.awt.BorderLayout.CENTER);

        WindowBalas.getContentPane().add(internalFrame5, java.awt.BorderLayout.CENTER);

        NoRm.setHighlighter(null);
        NoRm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRmKeyPressed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("::[ Booking Online Periksa ]::");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setOpaque(true);

        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setPreferredSize(new java.awt.Dimension(55, 55));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel6.setText("Key Word :");
        jLabel6.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass8.add(jLabel6);

        TCari.setPreferredSize(new java.awt.Dimension(205, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass8.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnAll);

        LCount1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount1.setPreferredSize(new java.awt.Dimension(20, 23));
        panelGlass8.add(LCount1);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnJawab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/file-edit-16x16.png"))); // NOI18N
        BtnJawab.setMnemonic('J');
        BtnJawab.setText("Jawab");
        BtnJawab.setToolTipText("Alt+J");
        BtnJawab.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnJawab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnJawabActionPerformed(evt);
            }
        });
        BtnJawab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnJawabKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnJawab);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelCari.setPreferredSize(new java.awt.Dimension(44, 43));
        panelCari.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 9));

        R1.setBackground(new java.awt.Color(240, 250, 230));
        R1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R1);
        R1.setSelected(true);
        R1.setText("Belum Dibalas");
        R1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelCari.add(R1);

        R2.setBackground(new java.awt.Color(240, 250, 230));
        R2.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R2);
        R2.setText("Booking :");
        R2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R2.setPreferredSize(new java.awt.Dimension(75, 23));
        panelCari.add(R2);

        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-05-2020" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        DTPCari1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari1ItemStateChanged(evt);
            }
        });
        DTPCari1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari1KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari1);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("s.d");
        jLabel22.setPreferredSize(new java.awt.Dimension(25, 23));
        panelCari.add(jLabel22);

        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-05-2020" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        DTPCari2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari2KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari2);

        R3.setBackground(new java.awt.Color(240, 250, 230));
        R3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R3);
        R3.setText("Periksa :");
        R3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R3.setPreferredSize(new java.awt.Dimension(75, 23));
        panelCari.add(R3);

        DTPCari3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-05-2020" }));
        DTPCari3.setDisplayFormat("dd-MM-yyyy");
        DTPCari3.setOpaque(false);
        DTPCari3.setPreferredSize(new java.awt.Dimension(90, 23));
        DTPCari3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari3ItemStateChanged(evt);
            }
        });
        DTPCari3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari3KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari3);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("s.d");
        jLabel25.setPreferredSize(new java.awt.Dimension(25, 23));
        panelCari.add(jLabel25);

        DTPCari4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-05-2020" }));
        DTPCari4.setDisplayFormat("dd-MM-yyyy");
        DTPCari4.setOpaque(false);
        DTPCari4.setPreferredSize(new java.awt.Dimension(90, 23));
        DTPCari4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari4ItemStateChanged(evt);
            }
        });
        DTPCari4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari4KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari4);

        jLabel7.setText("Record :");
        jLabel7.setPreferredSize(new java.awt.Dimension(55, 23));
        panelCari.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LCount.setText("0");
        LCount.setPreferredSize(new java.awt.Dimension(45, 23));
        panelCari.add(LCount);

        jPanel3.add(panelCari, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        
    }//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        
    }//GEN-LAST:event_tbObatKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            TCari.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Map<String, Object> param = new HashMap<>();    
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select logo from setting")); 
            pilihtampil="";
            if(R1.isSelected()==true){
                pilihtampil="booking_periksa.status='Belum Dibalas'";
            }else if(R2.isSelected()==true){
                pilihtampil="booking_periksa.tanggal_booking between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' ";
            }else if(R3.isSelected()==true){
                pilihtampil="booking_periksa.tanggal between '"+Valid.SetTgl(DTPCari3.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari4.getSelectedItem()+"")+"' ";
            }
            
            Valid.MyReportqry("rptBookingPeriksa.jasper","report","::[ Data Pasien Booking Periksa ]::",
                    "select booking_periksa.no_booking,booking_periksa.tanggal,booking_periksa.nama,booking_periksa.alamat,booking_periksa.no_telp,booking_periksa.email,booking_periksa.kd_poli,poliklinik.nm_poli,"+
                    "booking_periksa.tambahan_pesan,booking_periksa.status,booking_periksa.tanggal_booking from booking_periksa inner join poliklinik on booking_periksa.kd_poli=poliklinik.kd_poli "+
                    "where "+pilihtampil+(TCari.getText().trim().equals("")?"":" and (booking_periksa.no_booking like ? or booking_periksa.nama like ? or booking_periksa.alamat like ? or booking_periksa.no_telp like ? or "+
                    "booking_periksa.email like ? or poliklinik.nm_poli like ? or booking_periksa.tambahan_pesan like ? or booking_periksa.status like ?)")+" order by booking_periksa.no_booking",param);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        
    }//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        
    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        kab.dispose();
        kec.dispose();
        prop.dispose();
        kel.dispose();
        WindowBalas.dispose();
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        
    }//GEN-LAST:event_BtnKeluarKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        
    }//GEN-LAST:event_BtnCariKeyPressed

    private void DTPCari1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari1ItemStateChanged
        R2.setSelected(true);
    }//GEN-LAST:event_DTPCari1ItemStateChanged

    private void DTPCari1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari1KeyPressed

    }//GEN-LAST:event_DTPCari1KeyPressed

    private void DTPCari2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari2KeyPressed
        R2.setSelected(true);
    }//GEN-LAST:event_DTPCari2KeyPressed

    private void DTPCari3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari3ItemStateChanged
        R3.setSelected(true);
    }//GEN-LAST:event_DTPCari3ItemStateChanged

    private void DTPCari3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DTPCari3KeyPressed

    private void DTPCari4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari4ItemStateChanged
        R3.setSelected(true);
    }//GEN-LAST:event_DTPCari4ItemStateChanged

    private void DTPCari4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DTPCari4KeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        aktif=true;
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void BtnJawabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnJawabActionPerformed
        if(tbObat.getSelectedRow()>-1){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));    
            getData();
            autoNomor();
            if(aktifjadwal.equals("aktif")){
                if(akses.getkode().equals("Admin Utama")){

                }
            }
                    
            WindowBalas.setSize(internalFrame1.getWidth()-10, internalFrame1.getHeight()-10);
            WindowBalas.setLocationRelativeTo(internalFrame1);
            WindowBalas.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        } 
    }//GEN-LAST:event_BtnJawabActionPerformed

    private void BtnJawabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnJawabKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnJawabKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        aktif=false;
        WindowBalas.dispose();
    }//GEN-LAST:event_formWindowClosed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Sequel.meghapus("booking_periksa","no_booking",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            tampil();
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnJawab);
        }
    }//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnCloseIn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseIn4ActionPerformed
        kab.dispose();
        kec.dispose();
        prop.dispose();
        kel.dispose();
        WindowBalas.dispose();
    }//GEN-LAST:event_BtnCloseIn4ActionPerformed

    private void BtnSimpan4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpan4ActionPerformed
        
    }//GEN-LAST:event_BtnSimpan4ActionPerformed

    private void NamaPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NamaPasienKeyPressed
        
    }//GEN-LAST:event_NamaPasienKeyPressed

    private void TNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoKeyPressed
        /*if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            TTmp.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            KabupatenPj.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_DOWN){
            kddokter.requestFocus();
        }*/
    }//GEN-LAST:event_TNoKeyPressed

    private void ChkRMItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChkRMItemStateChanged
        if(ChkRM.isSelected()==true){
            TNo.setEditable(false);
            TNo.setBackground(new Color(245,250,240));
            autoNomor();
        }else if(ChkRM.isSelected()==false){
            TNo.setEditable(true);
            TNo.setBackground(new Color(250,255,245));
        }
    }//GEN-LAST:event_ChkRMItemStateChanged

    private void NoTelpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoTelpKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoTelpKeyPressed

    private void AlamatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlamatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_AlamatKeyPressed

    private void EmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EmailKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailKeyPressed

    private void NoBookingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoBookingKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoBookingKeyPressed

    private void KelurahanMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_KelurahanMouseMoved
        if(Kelurahan.getText().equals("KELURAHAN")){
            Kelurahan.setText("");
        }
    }//GEN-LAST:event_KelurahanMouseMoved

    private void KelurahanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_KelurahanMouseExited
        if(Kelurahan.getText().equals("")){
            Kelurahan.setText("KELURAHAN");
        }
    }//GEN-LAST:event_KelurahanMouseExited

    private void KelurahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KelurahanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(Kelurahan.getText().equals("")){
                Kelurahan.setText("KELURAHAN");
            }
            if(Kecamatan.getText().equals("KECAMATAN")){
                Kecamatan.setText("");
            }
            Kecamatan.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            if(Kelurahan.getText().equals("")){
                Kelurahan.setText("KELURAHAN");
            }
            if(Alamat.getText().equals("ALAMAT")){
                Alamat.setText("");
            }
            Alamat.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnKelurahanActionPerformed(null);
        }
    }//GEN-LAST:event_KelurahanKeyPressed

    private void BtnKelurahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKelurahanActionPerformed
        kel.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        kel.setLocationRelativeTo(internalFrame1);
        kel.setVisible(true);
    }//GEN-LAST:event_BtnKelurahanActionPerformed

    private void BtnKecamatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKecamatanActionPerformed
        kec.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        kec.setLocationRelativeTo(internalFrame1);
        kec.setVisible(true);
    }//GEN-LAST:event_BtnKecamatanActionPerformed

    private void KecamatanMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_KecamatanMouseMoved
        if(Kecamatan.getText().equals("KECAMATAN")){
            Kecamatan.setText("");
        }
    }//GEN-LAST:event_KecamatanMouseMoved

    private void KecamatanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_KecamatanMouseExited
        if(Kecamatan.getText().equals("")){
            Kecamatan.setText("KECAMATAN");
        }
    }//GEN-LAST:event_KecamatanMouseExited

    private void KecamatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KecamatanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(Kecamatan.getText().equals("")){
                Kecamatan.setText("KECAMATAN");
            }
            if(Kabupaten.getText().equals("KABUPATEN")){
                Kabupaten.setText("");
            }
            Kabupaten.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            if(Kecamatan.getText().equals("")){
                Kecamatan.setText("KECAMATAN");
            }
            if(Kelurahan.getText().equals("KELURAHAN")){
                Kelurahan.setText("");
            }
            Kelurahan.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnKecamatanActionPerformed(null);
        }
    }//GEN-LAST:event_KecamatanKeyPressed

    private void KabupatenMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_KabupatenMouseMoved
        if(Kabupaten.getText().equals("KABUPATEN")){
            Kabupaten.setText("");
        }
    }//GEN-LAST:event_KabupatenMouseMoved

    private void KabupatenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_KabupatenMouseExited
        if(Kabupaten.getText().equals("")){
            Kabupaten.setText("KABUPATEN");
        }
    }//GEN-LAST:event_KabupatenMouseExited

    private void KabupatenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KabupatenKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(Kabupaten.getText().equals("")){
                Kabupaten.setText("KABUPATEN");
            }
            if(Propinsi.getText().equals("PROPINSI")){
                Propinsi.setText("");
            }
            Propinsi.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            if(Kabupaten.getText().equals("")){
                Kabupaten.setText("KABUPATEN");
            }
            if(Kecamatan.getText().equals("KECAMATAN")){
                Kecamatan.setText("");
            }
            Kecamatan.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnKabupatenActionPerformed(null);
        }
    }//GEN-LAST:event_KabupatenKeyPressed

    private void BtnKabupatenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKabupatenActionPerformed
        kab.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        kab.setLocationRelativeTo(internalFrame1);
        kab.setVisible(true);
    }//GEN-LAST:event_BtnKabupatenActionPerformed

    private void PropinsiMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PropinsiMouseMoved
        if(Propinsi.getText().equals("PROPINSI")){
            Propinsi.setText("");
        }
    }//GEN-LAST:event_PropinsiMouseMoved

    private void PropinsiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PropinsiMouseExited
        if(Propinsi.getText().equals("")){
            Propinsi.setText("PROPINSI");
        }
    }//GEN-LAST:event_PropinsiMouseExited

    private void PropinsiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PropinsiKeyPressed
        /*if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(Propinsi.getText().equals("")){
                Propinsi.setText("PROPINSI");
            }
            if(AlamatPj.getText().equals("ALAMAT")){
                AlamatPj.setText("");
            }
            AlamatPj.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            if(Propinsi.getText().equals("")){
                Propinsi.setText("PROPINSI");
            }
            if(Kabupaten.getText().equals("KABUPATEN")){
                Kabupaten.setText("");
            }
            Kabupaten.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnPropinsiActionPerformed(null);
        }*/
    }//GEN-LAST:event_PropinsiKeyPressed

    private void BtnPropinsiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPropinsiActionPerformed
        prop.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        prop.setLocationRelativeTo(internalFrame1);
        prop.setVisible(true);
    }//GEN-LAST:event_BtnPropinsiActionPerformed

    private void TambahanPesanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TambahanPesanKeyPressed
        //Valid.pindah(evt,BMI,RiwayatPenyakit);
    }//GEN-LAST:event_TambahanPesanKeyPressed

    private void TanggalBookingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalBookingKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TanggalBookingKeyPressed

    private void TanggalPeriksaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalPeriksaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TanggalPeriksaKeyPressed

    private void NmPoliMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NmPoliMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_NmPoliMouseMoved

    private void NmPoliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NmPoliMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_NmPoliMouseExited

    private void NmPoliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NmPoliKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NmPoliKeyPressed

    private void KdPoliMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_KdPoliMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_KdPoliMouseMoved

    private void KdPoliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_KdPoliMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_KdPoliMouseExited

    private void KdPoliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPoliKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdPoliKeyPressed

    private void NoRmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRmKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRmKeyPressed

    private void BtnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterActionPerformed
        if(aktifjadwal.equals("aktif")){
            if(akses.getkode().equals("Admin Utama")){
                dokter.isCek();
                dokter.TCari.requestFocus();
                dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
                dokter.setLocationRelativeTo(internalFrame1);
                dokter.setVisible(true);
            }else{
                dokter2.setPoli(NmPoli.getText());
                dokter2.isCek();
                dokter2.SetHari(Valid.SetTgl2(TanggalPeriksa.getText()));
                dokter2.tampil();
                dokter2.TCari.requestFocus();
                dokter2.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
                dokter2.setLocationRelativeTo(internalFrame1);
                dokter2.setVisible(true);
            }
        }else{
            dokter.isCek();
            dokter.TCari.requestFocus();
            dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            dokter.setLocationRelativeTo(internalFrame1);
            dokter.setVisible(true);
        }
    }//GEN-LAST:event_BtnDokterActionPerformed

    private void BtnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDokterKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnDokterActionPerformed(null);
        }else{
            //Valid.pindah(evt,BtnPasien,BtnPoli);
        }
    }//GEN-LAST:event_BtnDokterKeyPressed

    private void btnPenjabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenjabActionPerformed
        penjab.onCari();
        penjab.isCek();
        penjab.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        penjab.setLocationRelativeTo(internalFrame1);
        penjab.setVisible(true);
    }//GEN-LAST:event_btnPenjabActionPerformed

    private void btnPenjabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPenjabKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            btnPenjabActionPerformed(null);
        }else{
            //Valid.pindah(evt,BtnPoli,TanggalBooking);
        }
    }//GEN-LAST:event_btnPenjabKeyPressed

    private void NoRegKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRegKeyPressed
        //Valid.pindah(evt,TanggalPeriksa,BtnSimpan);
    }//GEN-LAST:event_NoRegKeyPressed

    private void KuotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KuotaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KuotaKeyPressed

    private void kdpnjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kdpnjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kdpnjActionPerformed

    private void Kuota1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Kuota1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Kuota1KeyPressed

    private void Kuota2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Kuota2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Kuota2KeyPressed

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
            java.util.logging.Logger.getLogger(DlgBookingPeriksa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DlgBookingPeriksa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DlgBookingPeriksa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DlgBookingPeriksa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DlgBookingPeriksa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.TextBox Alamat;
    private widget.Button BtnAll;
    private widget.Button BtnCari;
    private widget.Button BtnCloseIn4;
    private widget.Button BtnDokter;
    private widget.Button BtnHapus;
    private widget.Button BtnJawab;
    private widget.Button BtnKabupaten;
    private widget.Button BtnKecamatan;
    private widget.Button BtnKeluar;
    private widget.Button BtnKelurahan;
    private widget.Button BtnPrint;
    private widget.Button BtnPropinsi;
    private widget.Button BtnSimpan4;
    private widget.CekBox ChkRM;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.Tanggal DTPCari3;
    private widget.Tanggal DTPCari4;
    private widget.TextBox Email;
    private widget.PanelBiasa FormInput;
    private widget.TextBox Kabupaten;
    private widget.TextBox KdDokter;
    private widget.TextBox KdPoli;
    private widget.TextBox Kecamatan;
    private widget.TextBox Kelurahan;
    private widget.TextBox Kuota;
    private widget.TextBox Kuota1;
    private widget.TextBox Kuota2;
    private widget.Label LCount;
    private widget.Label LCount1;
    private widget.Label LabelStatus;
    private widget.TextBox NamaPasien;
    private widget.TextBox NmDokter;
    private widget.TextBox NmPoli;
    private widget.TextBox NoBooking;
    private widget.TextBox NoReg;
    private widget.TextBox NoRm;
    private widget.TextBox NoTelp;
    private widget.TextBox Propinsi;
    private widget.RadioButton R1;
    private widget.RadioButton R2;
    private widget.RadioButton R3;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNo;
    private widget.TextArea TambahanPesan;
    private widget.TextBox TanggalBooking;
    private widget.TextBox TanggalPeriksa;
    private javax.swing.JDialog WindowBalas;
    private widget.Button btnPenjab;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame5;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel3;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel3;
    private widget.TextBox kdpnj;
    private widget.TextBox nmpnj;
    private widget.panelisi panelCari;
    private widget.panelisi panelGlass6;
    private widget.panelisi panelGlass8;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables
    
    private void tampil(){
        Valid.tabelKosong(tabMode);
        try{
            pilihtampil="";
            if(R1.isSelected()==true){
                pilihtampil="booking_periksa.status='Belum Dibalas'";
            }else if(R2.isSelected()==true){
                pilihtampil="booking_periksa.tanggal_booking between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' ";
            }else if(R3.isSelected()==true){
                pilihtampil="booking_periksa.tanggal between '"+Valid.SetTgl(DTPCari3.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari4.getSelectedItem()+"")+"' ";
            }
            
            ps=koneksi.prepareStatement(
                    "select booking_periksa.no_booking,booking_periksa.tanggal,booking_periksa.nama,booking_periksa.alamat,booking_periksa.no_telp,booking_periksa.email,booking_periksa.kd_poli,poliklinik.nm_poli,"+
                    "booking_periksa.tambahan_pesan,booking_periksa.status,booking_periksa.tanggal_booking from booking_periksa inner join poliklinik on booking_periksa.kd_poli=poliklinik.kd_poli "+
                    "where "+pilihtampil+(TCari.getText().trim().equals("")?"":" and (booking_periksa.no_booking like ? or booking_periksa.nama like ? or booking_periksa.alamat like ? or booking_periksa.no_telp like ? or "+
                    "booking_periksa.email like ? or poliklinik.nm_poli like ? or booking_periksa.tambahan_pesan like ? or booking_periksa.status like ?)")+" order by booking_periksa.no_booking");
            try {
                if(!TCari.getText().trim().equals("")){
                    ps.setString(1,"%"+TCari.getText().trim()+"%");
                    ps.setString(2,"%"+TCari.getText().trim()+"%");
                    ps.setString(3,"%"+TCari.getText().trim()+"%");
                    ps.setString(4,"%"+TCari.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText().trim()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,"%"+TCari.getText().trim()+"%");
                    ps.setString(8,"%"+TCari.getText().trim()+"%");
                }
                
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_booking"),rs.getString("tanggal_booking"),rs.getString("tanggal"),rs.getString("nama"),rs.getString("alamat"),rs.getString("no_telp"),
                        rs.getString("email"),rs.getString("kd_poli"),rs.getString("nm_poli"),rs.getString("tambahan_pesan"),rs.getString("status")
                    });
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                
                if(ps!=null){
                    ps.close();
                }
            } 
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }

    private void jam(){
        ActionListener taskPerformer = (ActionEvent e) -> {
            if(aktif==true){
                nol_detik = "";
                Date now = Calendar.getInstance().getTime();
                nilai_detik = now.getSeconds();
                if (nilai_detik <= 9) {
                    nol_detik = "0";
                }

                detik = nol_detik + Integer.toString(nilai_detik);
                if(detik.equals("05")){
                    bookingbaru=Sequel.cariInteger("select count(*) from booking_periksa where status='Belum Dibalas'");
                    if(bookingbaru>0){
                        try {
                            music = new BackgroundMusic("./suara/alarm.mp3");
                            music.start();
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        
                        i=JOptionPane.showConfirmDialog(null, "Ada booking periksa baru yang belum dibalas, apa mau ditampilkan????","Konfirmasi",JOptionPane.YES_NO_OPTION);
                        if(i==JOptionPane.YES_OPTION){
                            R1.setSelected(true);
                            TCari.setText("");
                            tampil();
                        }
                    }
                }
            }                
        };
        new Timer(1000, taskPerformer).start();
    }
    
    private void getData(){
        if(tbObat.getSelectedRow()!= -1){
            NoBooking.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            TanggalBooking.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TanggalPeriksa.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            NamaPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            arrSplit = tbObat.getValueAt(tbObat.getSelectedRow(),4).toString().toUpperCase().split(",");
            try {
                if(!arrSplit[0].equals("")){
                    Alamat.setText(arrSplit[0]);
                }
            } catch (Exception e) {
                Alamat.setText("-");
            }
            
            try {
                if(!arrSplit[1].equals("")){
                    Kelurahan.setText(arrSplit[1].replaceFirst(" ",""));
                }
            } catch (Exception e) {
                Kelurahan.setText("-");
            }
            
            try {
                if(!arrSplit[2].equals("")){
                    Kecamatan.setText(arrSplit[2].replaceFirst(" ",""));
                }
            } catch (Exception e) {
                Kecamatan.setText("-");
            }
            
            try {
                if(!arrSplit[3].equals("")){
                    Kabupaten.setText(arrSplit[3].replaceFirst(" ",""));
                }
            } catch (Exception e) {
                Kabupaten.setText("-");
            }
            
            try {
                if(!arrSplit[4].equals("")){
                    Propinsi.setText(arrSplit[4].replaceFirst(" ",""));
                }
            } catch (Exception e) {
                Propinsi.setText("-");
            }
            
            NoTelp.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            Email.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            KdPoli.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            NmPoli.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            TambahanPesan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
        }
    }
    
    public void isCek(){
        BtnHapus.setEnabled(akses.getbooking_periksa());
        BtnPrint.setEnabled(akses.getbooking_periksa());
    }
    
    private void isNomer(){
        switch (URUTNOREG) {
            case "poli":
                Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_poli='"+KdPoli.getText()+"' and tanggal_periksa='"+TanggalPeriksa.getText()+"'","",3,NoReg);
                break;
            case "dokter":
                Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='"+KdDokter.getText()+"' and tanggal_periksa='"+TanggalPeriksa.getText()+"'","",3,NoReg);
                break;
            case "dokter + poli":             
                Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='"+KdDokter.getText()+"' and kd_poli='"+KdPoli.getText()+"' and tanggal_periksa='"+TanggalPeriksa.getText()+"'","",3,NoReg);
                break;
            default:
                Valid.autoNomer3("select ifnull(MAX(CONVERT(no_reg,signed)),0) from booking_registrasi where kd_dokter='"+KdDokter.getText()+"' and tanggal_periksa='"+TanggalPeriksa.getText()+"'","",3,NoReg);
                break;
        }
    }
    
    private void autoNomor() {        
        if(ChkRM.isSelected()==true){
            if(tahun.equals("Yes")){
                awalantahun=TanggalPeriksa.getText().substring(6,10);
            }else{
                awalantahun="";
            }

            if(bulan.equals("Yes")){
                awalanbulan=TanggalPeriksa.getText().substring(3,5);
            }else{
                awalanbulan="";
            }

            if(posisitahun.equals("Depan")){
                switch (pengurutan) {
                    case "Straight":
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(no_rkm_medis,6),signed)),0) from set_no_rkm_medis","",6,NoRm);
                        break;
                    case "Terminal":
                        Valid.autoNomer4("select ifnull(MAX(CONVERT(CONCAT(SUBSTRING(RIGHT(no_rkm_medis,6),5,2),SUBSTRING(RIGHT(no_rkm_medis,6),3,2),SUBSTRING(RIGHT(no_rkm_medis,6),1,2)),signed)),0) from set_no_rkm_medis","",6,NoRm);
                        break;
                    case "Middle":
                        Valid.autoNomer5("select ifnull(MAX(CONVERT(CONCAT(SUBSTRING(RIGHT(no_rkm_medis,6),3,2),SUBSTRING(RIGHT(no_rkm_medis,6),1,2),SUBSTRING(RIGHT(no_rkm_medis,6),5,2)),signed)),0) from set_no_rkm_medis","",6,NoRm);
                        break;
                }
            }else if(posisitahun.equals("Belakang")){
                switch (pengurutan) {
                    case "Straight":
                        Valid.autoNomer3("select ifnull(MAX(CONVERT(LEFT(no_rkm_medis,6),signed)),0) from set_no_rkm_medis","",6,NoRm);
                        break;
                    case "Terminal":
                        Valid.autoNomer4("select ifnull(MAX(CONVERT(CONCAT(SUBSTRING(LEFT(no_rkm_medis,6),5,2),SUBSTRING(LEFT(no_rkm_medis,6),3,2),SUBSTRING(LEFT(no_rkm_medis,6),1,2)),signed)),0) from set_no_rkm_medis","",6,NoRm);
                        break;
                    case "Middle":
                        Valid.autoNomer5("select ifnull(MAX(CONVERT(CONCAT(SUBSTRING(LEFT(no_rkm_medis,6),3,2),SUBSTRING(LEFT(no_rkm_medis,6),1,2),SUBSTRING(LEFT(no_rkm_medis,6),5,2)),signed)),0) from set_no_rkm_medis","",6,NoRm);
                        break;
                }            
            }

            if(posisitahun.equals("Depan")){
                TNo.setText(awalantahun+awalanbulan+NoRm.getText());
            }else if(posisitahun.equals("Belakang")){
                if(!(awalanbulan+awalantahun).equals("")){
                    TNo.setText(NoRm.getText()+"-"+awalanbulan+awalantahun);
                }else{
                    TNo.setText(NoRm.getText());
                }            
            }
        }
    }
}