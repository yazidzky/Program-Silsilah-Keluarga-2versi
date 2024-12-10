import java.util.*;

class FamilyMember {
    String name;
    String role;
    int id;

    // Konstruktor untuk membuat anggota keluarga dengan nama, peran, dan ID unik
    public FamilyMember(String name, String role, int id) {
        this.name = name;
        this.role = role;
        this.id = id;
    }
}

class TreeNode {
    FamilyMember member;
    TreeNode left, right;

    public TreeNode(FamilyMember member) {
        this.member = member;
        this.left = null;
        this.right = null;
    }
}

class FamilyManager {
    private final List<FamilyMember> familyList = new ArrayList<>(); // Daftar anggota keluarga
    private final Map<Integer, FamilyMember> familyHashMap = new HashMap<>(); // HashMap untuk penyimpanan berdasarkan ID
    private final Map<String, List<String>> familyGraph = new HashMap<>(); // Graf hubungan antar anggota keluarga
    private TreeNode root = null; // Root dari Binary Search Tree (BST)
    private int idCounter = 1; // Penghitung ID unik untuk setiap anggota keluarga

    // Fitur 1: Menambah Anggota Keluarga
    public void addMember(String name, String role) {
        int id = idCounter++; // Menghasilkan ID unik untuk anggota baru
        FamilyMember member = new FamilyMember(name, role, id);
        familyList.add(member); // Menambahkan anggota ke daftar
        familyHashMap.put(id, member); // Menambahkan anggota ke HashMap dengan ID sebagai kunci
        addMemberToBST(member); // Tambahkan anggota ke dalam pohon keluarga (BST)
        System.out.println("Anggota keluarga berhasil ditambahkan: " + name + " (ID: " + id + ")");
    }

    // Fitur 2: Mencari Anggota Keluarga Berdasarkan Nama (Binary Search)
    public void searchMemberByName(String name) {
        familyList.sort(Comparator.comparing(member -> member.name)); // Mengurutkan anggota berdasarkan nama
        int index = Collections.binarySearch(familyList, new FamilyMember(name, "", 0), Comparator.comparing(member -> member.name));
        if (index >= 0) {
            FamilyMember found = familyList.get(index);
            System.out.println("Anggota ditemukan: " + found.name + " (ID: " + found.id + ", Role: " + found.role + ")");
        } else {
            System.out.println("Anggota tidak ditemukan.");
        }
    }

    // Fitur 3: Mencari Anggota Keluarga Berdasarkan ID
    public void searchMemberById(int id) {
        FamilyMember member = familyHashMap.get(id); // Mencari anggota berdasarkan ID
        if (member != null) {
            System.out.println("Anggota ditemukan: " + member.name + " (Role: " + member.role + ")");
        } else {
            System.out.println("Anggota tidak ditemukan.");
        }
    }

    // Fitur 4: Pengurutan Anggota Keluarga Berdasarkan Nama (Bubble Sort atau Quick Sort)
    public void sortMembers(String algorithm) {
        switch (algorithm.toLowerCase()) {
            case "bubble" -> bubbleSort(); // Menjalankan Bubble Sort
            case "quick" -> quickSort(0, familyList.size() - 1); // Menjalankan Quick Sort
            default -> System.out.println("Algoritma pengurutan tidak valid.");
        }
        displayAllMembers(); // Menampilkan anggota keluarga setelah diurutkan
    }

    // Bubble Sort untuk mengurutkan anggota keluarga berdasarkan nama
    private void bubbleSort() {
        for (int i = 0; i < familyList.size() - 1; i++) {
            for (int j = 0; j < familyList.size() - i - 1; j++) {
                if (familyList.get(j).name.compareTo(familyList.get(j + 1).name) > 0) {
                    Collections.swap(familyList, j, j + 1); // Menukar posisi anggota
                }
            }
        }
    }

    // Quick Sort untuk mengurutkan anggota keluarga berdasarkan nama
    private void quickSort(int low, int high) {
        if (low < high) {
            int pi = partition(low, high); // Membagi daftar
            quickSort(low, pi - 1); // Mengurutkan bagian kiri
            quickSort(pi + 1, high); // Mengurutkan bagian kanan
        }
    }

    private int partition(int low, int high) {
        String pivot = familyList.get(high).name; // Menentukan pivot
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (familyList.get(j).name.compareTo(pivot) < 0) {
                i++;
                Collections.swap(familyList, i, j); // Menukar jika elemen lebih kecil dari pivot
            }
        }
        Collections.swap(familyList, i + 1, high); // Menempatkan pivot pada posisi yang benar
        return i + 1;
    }

    // Fitur 5: Menampilkan Semua Anggota Keluarga
    public void displayAllMembers() {
        System.out.println("Daftar Anggota Keluarga:");
        for (FamilyMember member : familyList) {
            System.out.println(member.name + " (ID: " + member.id + ", Role: " + member.role + ")");
        }
    }

    // Fitur 6: Menambah Hubungan Antar Anggota Keluarga dalam Graf
    public void addRelationship(String name1, String name2) {
        familyGraph.computeIfAbsent(name1, k -> new ArrayList<>()).add(name2); // Menambah hubungan antara dua anggota
        familyGraph.computeIfAbsent(name2, k -> new ArrayList<>()).add(name1);
        System.out.println("Hubungan keluarga berhasil ditambahkan antara " + name1 + " dan " + name2);
    }

    // Fitur 7: Menampilkan Graf Hubungan Keluarga
    public void displayFamilyGraph() {
        if (familyGraph.isEmpty()) {
            System.out.println("Graf hubungan keluarga kosong.");
        } else {
            System.out.println("\nGraf Hubungan Keluarga:");
            for (Map.Entry<String, List<String>> entry : familyGraph.entrySet()) {
                String member = entry.getKey();
                List<String> relatives = entry.getValue();
                System.out.println(member + " -> " + String.join(", ", relatives));
            }
        }
    }

    // Fitur 8: Menjelajahi Graf Hubungan Keluarga dengan DFS
    public void exploreGraphDFS(String start) {
        if (!familyGraph.containsKey(start)) {
            System.out.println("Anggota keluarga tidak ditemukan di dalam graf.");
            return;
        }
        Set<String> visited = new HashSet<>(); // Set untuk melacak anggota yang sudah dikunjungi
        System.out.println("DFS dari " + start + ":");
        dfs(start, visited); // Menjalankan DFS dari anggota yang diberikan
    }

    private void dfs(String current, Set<String> visited) {
        if (!visited.contains(current)) {
            System.out.print(current + " "); // Menampilkan anggota yang dikunjungi
            visited.add(current); // Tandai anggota sebagai dikunjungi
            for (String neighbor : familyGraph.getOrDefault(current, Collections.emptyList())) {
                dfs(neighbor, visited); // Melakukan DFS pada tetangga yang belum dikunjungi
            }
        }
    }

    // Fitur 9: Menambah Anggota Keluarga ke dalam Binary Search Tree
    public void addMemberToBST(FamilyMember member) {
        root = insert(root, member); // Memasukkan anggota keluarga ke dalam BST
    }

    private TreeNode insert(TreeNode root, FamilyMember member) {
        if (root == null) {
            root = new TreeNode(member);
            return root;
        }

        if (member.name.compareTo(root.member.name) < 0) {
            root.left = insert(root.left, member);
        } else if (member.name.compareTo(root.member.name) > 0) {
            root.right = insert(root.right, member);
        }

        return root;
    }

    // Menampilkan Binary Search Tree
    public void displayFamilyTree() {
        System.out.println("\nPohon Keluarga (Binary Search Tree):");
        printTree(root, "", true); // Menampilkan pohon dengan cara traversing
    }

    private void printTree(TreeNode node, String indent, boolean last) {
        if (node != null) {
            System.out.println(indent + (last ? "└── " : "├── ") + node.member.name);
            printTree(node.left, indent + (last ? "    " : "│   "), false);
            printTree(node.right, indent + (last ? "    " : "│   "), true);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FamilyManager familyManager = new FamilyManager();
        boolean running = true;

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Tambah Anggota Keluarga");
            System.out.println("2. Cari Anggota Keluarga");
            System.out.println("3. Pengurutan Anggota Keluarga");
            System.out.println("4. Menampilkan Pohon Keluarga (BST)");
            System.out.println("5. Menambah Hubungan Keluarga");
            System.out.println("6. Menampilkan Graf Hubungan Keluarga");
            System.out.println("7. Menjelajahi Graf Hubungan Keluarga (DFS)");
            System.out.println("8. Keluar");
            System.out.print("Pilih Menu: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Untuk mengonsumsi newline setelah input angka

            switch (choice) {
                case 1:
                    System.out.print("Nama Anggota Keluarga: ");
                    String name = scanner.nextLine();
                    System.out.print("Peran Anggota (misalnya: Ayah, Ibu, Anak): ");
                    String role = scanner.nextLine();
                    familyManager.addMember(name, role);
                    break;
                case 2:
                    System.out.print("Cari berdasarkan Nama (1) atau ID (2): ");
                    int searchChoice = scanner.nextInt();
                    scanner.nextLine(); // Konsumsi newline
                    if (searchChoice == 1) {
                        System.out.print("Nama Anggota: ");
                        String searchName = scanner.nextLine();
                        familyManager.searchMemberByName(searchName);
                    } else if (searchChoice == 2) {
                        System.out.print("ID Anggota: ");
                        int id = scanner.nextInt();
                        familyManager.searchMemberById(id);
                    }
                    break;
                case 3:
                    System.out.print("Pilih algoritma pengurutan (bubble/quick): ");
                    String algorithm = scanner.nextLine();
                    familyManager.sortMembers(algorithm);
                    break;
                case 4:
                    familyManager.displayFamilyTree();
                    break;
                case 5:
                    System.out.print("Nama Anggota 1: ");
                    String name1 = scanner.nextLine();
                    System.out.print("Nama Anggota 2: ");
                    String name2 = scanner.nextLine();
                    familyManager.addRelationship(name1, name2);
                    break;
                case 6:
                    familyManager.displayFamilyGraph();
                    break;
                case 7:
                    System.out.print("Nama Anggota untuk DFS: ");
                    String startName = scanner.nextLine();
                    familyManager.exploreGraphDFS(startName);
                    break;
                case 8:
                    running = false;
                    System.out.println("Keluar ke halaman utama.");
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }

        scanner.close();
    }
}
