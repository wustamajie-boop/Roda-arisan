# ArisanKu — Proyek Android

Aplikasi Android sederhana berbahasa Indonesia dengan tema hijau. Fitur:
- Menambah dan menghapus peserta.
- Undian acak satu pemenang.
- Opsi mencegah peserta yang sudah menang terpilih kembali.
- Riwayat pemenang, nominal, kelompok, dan waktu undian.
- Penyimpanan lokal permanen di perangkat menggunakan SharedPreferences.

## Cara membuat APK

1. Pasang **Android Studio** di komputer: https://developer.android.com/studio
2. Ekstrak ZIP ini.
3. Android Studio → **Open** → pilih folder `ArisanKu` hasil ekstrak.
4. Tunggu Gradle Sync selesai. Bila diminta, pasang Android SDK Platform 35 dan JDK 17.
5. Untuk APK uji coba: **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
6. Setelah selesai, pilih **locate** pada notifikasi. APK biasanya berada di `app/build/outputs/apk/debug/app-debug.apk`.
7. Pindahkan APK ke HP Android, buka file, lalu izinkan pemasangan dari sumber tersebut bila diminta.

## Membuat APK rilis

Pilih **Build → Generate Signed Bundle / APK → APK**, lalu buat/pilih keystore. Simpan keystore dan kata sandinya dengan aman.

## Catatan

- Data disimpan di perangkat tempat aplikasi dipakai. Aplikasi belum memiliki sinkronisasi cloud atau akun bersama.
- Contoh peserta awal: Budi, Siti, Andi, Rina. Silakan hapus/tambah sesuai kebutuhan.
- Pengundian menggunakan pemilihan acak di perangkat, tanpa sertifikat audit atau verifikasi pihak ketiga.
- Package ID: `id.arisan.ku` · Versi: `1.0`.
