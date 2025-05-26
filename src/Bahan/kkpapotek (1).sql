-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 25 Bulan Mei 2025 pada 00.35
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kkpapotek`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `kategori_obat`
--

CREATE TABLE `kategori_obat` (
  `id_kategori` int(11) NOT NULL,
  `nama_kategori` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `kategori_obat`
--

INSERT INTO `kategori_obat` (`id_kategori`, `nama_kategori`) VALUES
(1, 'Antibiotik'),
(2, 'Analgesik'),
(3, 'Vitamin'),
(5, 'Tolak Angin');

-- --------------------------------------------------------

--
-- Struktur dari tabel `obat`
--

CREATE TABLE `obat` (
  `kode_obat` varchar(20) NOT NULL,
  `nama_obat` varchar(100) DEFAULT NULL,
  `harga_jual` int(30) DEFAULT NULL,
  `harga_beli` int(30) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL,
  `kemasan` varchar(50) DEFAULT NULL,
  `id_kategori` int(11) DEFAULT NULL,
  `id_suplayer` int(11) DEFAULT NULL,
  `golongan` varchar(50) DEFAULT NULL,
  `no_registrasi_bpom` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `obat`
--

INSERT INTO `obat` (`kode_obat`, `nama_obat`, `harga_jual`, `harga_beli`, `stok`, `kemasan`, `id_kategori`, `id_suplayer`, `golongan`, `no_registrasi_bpom`) VALUES
('OBT001', 'Paracetamol', 5000, 3000, 100, 'Tablet', 2, 1, 'Bebas', 'BPOM123456'),
('OBT002', 'Amoxicillin', 10000, 6000, 60, 'Kapsul', 1, 2, 'Keras', 'BPOM654321'),
('OBT003', 'Vitamin C', 7000, 4000, 200, 'Tablet', 3, 3, 'Bebas', 'BPOM789012'),
('OBT004', 'Bodrex', 6000, 3000, 100, 'Tablet', 2, 2, 'B Aja', 'BPOM990088'),
('OBT005', 'jamu', 8000, 5000, 80, 'Botol', 3, 3, 'B Aja', 'BPOM0099883');

-- --------------------------------------------------------

--
-- Struktur dari tabel `rf_obat`
--

CREATE TABLE `rf_obat` (
  `id_rf` varchar(10) NOT NULL,
  `tanggal_rf` date DEFAULT NULL,
  `deskripsi` text DEFAULT NULL,
  `nama_suplayer` varchar(100) DEFAULT NULL,
  `kode_obat` varchar(10) DEFAULT NULL,
  `nama_obat` varchar(100) DEFAULT NULL,
  `harga_beli` int(11) DEFAULT NULL,
  `jumlah_pesanan` int(11) DEFAULT NULL,
  `total_harga` int(11) DEFAULT NULL,
  `status` enum('pending','acc','tolak') DEFAULT 'pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `rf_obat`
--

INSERT INTO `rf_obat` (`id_rf`, `tanggal_rf`, `deskripsi`, `nama_suplayer`, `kode_obat`, `nama_obat`, `harga_beli`, `jumlah_pesanan`, `total_harga`, `status`) VALUES
('RF0001', '2025-05-28', 'Permintaan oleh admin', 'PT Farma Prima', 'OBT003', 'Vitamin C', 4000, 80, 320000, 'pending');

-- --------------------------------------------------------

--
-- Struktur dari tabel `suplier`
--

CREATE TABLE `suplier` (
  `id_suplayer` int(11) NOT NULL,
  `nama_perusahaan` varchar(100) DEFAULT NULL,
  `alamat` varchar(200) DEFAULT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `no_tlp` varchar(20) DEFAULT NULL,
  `nama_bank` varchar(50) DEFAULT NULL,
  `no_rekening` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `suplier`
--

INSERT INTO `suplier` (`id_suplayer`, `nama_perusahaan`, `alamat`, `contact_person`, `no_tlp`, `nama_bank`, `no_rekening`) VALUES
(1, 'PT Sehat Selalu', 'Jl. Kesehatan No.10 Jakarta', 'Andi', '08123456789', 'BCA', '1234567890'),
(2, 'CV Obat Abadi', 'Jl. Medika No.23 Bandung', 'Budi', '08234567890', 'Mandiri', '0987654321'),
(3, 'PT Farma Prima', 'Jl. Apotek No.88 Surabaya', 'Citra', '08345678901', 'BRI', '1122334455');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `no_identitas` varchar(50) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `no_induk_karyawan` varchar(50) DEFAULT NULL,
  `no_tlp` varchar(20) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('kasir','admin','superadmin') DEFAULT 'superadmin',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `nama`, `no_identitas`, `alamat`, `no_induk_karyawan`, `no_tlp`, `username`, `password`, `role`, `created_at`) VALUES
(1, 'Andrea', '327103110001', 'Jl. Merdeka No. 10', 'EMP001', '081234567890', 'admin', 'admin123', 'admin', '2025-04-30 09:12:51'),
(2, 'Khonsa', '327103110002', 'Jl. Mawar No. 5', 'EMP002', '082134567891', 'kasir', 'kasir123', 'kasir', '2025-04-30 09:12:51'),
(5, 'muhamad irham', '112341', 'Depok', 'AA1244', '085717625', 'iam', 'irham', 'superadmin', '2025-04-30 13:18:58');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `kategori_obat`
--
ALTER TABLE `kategori_obat`
  ADD PRIMARY KEY (`id_kategori`);

--
-- Indeks untuk tabel `obat`
--
ALTER TABLE `obat`
  ADD PRIMARY KEY (`kode_obat`),
  ADD KEY `id_kategori` (`id_kategori`),
  ADD KEY `id_suplayer` (`id_suplayer`);

--
-- Indeks untuk tabel `rf_obat`
--
ALTER TABLE `rf_obat`
  ADD PRIMARY KEY (`id_rf`);

--
-- Indeks untuk tabel `suplier`
--
ALTER TABLE `suplier`
  ADD PRIMARY KEY (`id_suplayer`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `kategori_obat`
--
ALTER TABLE `kategori_obat`
  MODIFY `id_kategori` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `suplier`
--
ALTER TABLE `suplier`
  MODIFY `id_suplayer` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `obat`
--
ALTER TABLE `obat`
  ADD CONSTRAINT `obat_ibfk_1` FOREIGN KEY (`id_kategori`) REFERENCES `kategori_obat` (`id_kategori`),
  ADD CONSTRAINT `obat_ibfk_2` FOREIGN KEY (`id_suplayer`) REFERENCES `suplier` (`id_suplayer`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
