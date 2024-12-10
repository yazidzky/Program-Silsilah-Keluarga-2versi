import java.util.Scanner;

public class Mainmenu {

    public static void main(String[] args) {
        // Scanner untuk input dari user
        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("===========================================");
            System.out.println("        Selamat datang di Program          ");
            System.out.println("       Silsilah Keluarga (Menu Utama)      ");
            System.out.println("===========================================");
            System.out.println("1. Menjalankan Program Silsilah Keluarga (Versi Basic)");
            System.out.println("2. Menjalankan Program Silsilah Keluarga (Versi Kompleks)");
            System.out.println("3. Keluar");
            System.out.println("===========================================");
            System.out.print("Masukkan pilihan (1/2/3): ");
            
            choice = scanner.nextInt();  

            switch (choice) {
                case 1:
                    System.out.println("Menjalankan FamilyManager...");
                    ProgramsilsilahKeluargaVersi1.main(args); 
                    break;
                case 2:
                    System.out.println("Menjalankan Programsilsilahkeluarga...");
                    ProgramsilsilahkeluargaVersi2.main(args);  
                    break;
                case 3:
                    System.out.println("Keluar dari program.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        } while (choice != 3);  // Menu akan terus muncul hingga memilih 3 untuk keluar
    }
}
