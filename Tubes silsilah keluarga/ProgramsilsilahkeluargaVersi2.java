import java.util.*;
import java.util.stream.Stream;

class Node implements Comparable<Node> {
    String name;
    String relation;
    int age;
    Node father;
    Node mother;
    Node spouse;
    List<Node> children;
    String gender;

    public List<Node> getSiblings() {
        Set<Node> siblings = new HashSet<>(); // Gunakan Set untuk menghindari duplikasi

        // Periksa saudara kandung dari pihak ayah
        if (this.father != null) {
            for (Node sibling : this.father.children) {
                if (sibling != this) {
                    siblings.add(sibling);
                }
            }
        }

        // Periksa saudara kandung dari pihak ibu
        if (this.mother != null) {
            for (Node sibling : this.mother.children) {
                if (sibling != this) {
                    siblings.add(sibling);
                }
            }
        }

        return new ArrayList<>(siblings); // Kembalikan dalam bentuk List jika diperlukan
    }

    public Node(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.father = null;
        this.mother = null;
        this.spouse = null;
        this.children = new ArrayList<>();
        this.relation = "Individu";
    }

    public void addChild(Node child) {
        this.children.add(child);
        this.children.sort(Comparator.comparingInt(c -> -c.age)); // Sort descending by age
    }

    @Override
    public int compareTo(Node other) {
        return other.age - this.age;
    }
}

class FamilyTree {
    private Map<String, Node> nodeMap;

    public FamilyTree() {
        this.nodeMap = new HashMap<>();
    }

    public void addIndividual(String name, int age, String gender) {
        if (!nodeMap.containsKey(name)) {
            nodeMap.put(name, new Node(name, age, gender));
        }
    }

    public void addParentRelationship(String father, String mother, List<Node> children) {
        Node fatherNode = nodeMap.getOrDefault(father, null);
        if (fatherNode == null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Masukkan umur ayah: ");
            int fatherAge = Integer.parseInt(scanner.nextLine());
            addIndividual(father, fatherAge, "L");
            fatherNode = nodeMap.get(father);
        }

        Node motherNode = nodeMap.getOrDefault(mother, null);
        if (motherNode == null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Masukkan umur ibu: ");
            int motherAge = Integer.parseInt(scanner.nextLine());
            addIndividual(mother, motherAge, "P");
            motherNode = nodeMap.get(mother);
        }

        fatherNode.spouse = motherNode;
        motherNode.spouse = fatherNode;

        for (Node childNode : children) {
            addIndividual(childNode.name, childNode.age, childNode.gender);
            Node existingChild = nodeMap.get(childNode.name);
            existingChild.father = fatherNode;
            existingChild.mother = motherNode;

            fatherNode.addChild(existingChild);
            motherNode.addChild(existingChild);
        }
    }

    //menu hapus individu
    public void deleteIndividual(String name) {
        Node nodeToDelete = nodeMap.get(name);
        if (nodeToDelete == null) {
            System.out.println("Individu " + name + " tidak ditemukan.");
            return;
        }

        // Hapus dari hubungan orang tua
        if (nodeToDelete.father != null) {
            nodeToDelete.father.children.remove(nodeToDelete);
        }
        if (nodeToDelete.mother != null) {
            nodeToDelete.mother.children.remove(nodeToDelete);
        }

        // Hapus hubungan pasangan
        if (nodeToDelete.spouse != null) {
            nodeToDelete.spouse.spouse = null;
        }

        // Hapus dari nodeMap
        nodeMap.remove(name);
        System.out.println("Individu " + name + " berhasil dihapus.");
    }

    //menu ganti nama 
    public void renameIndividual(String oldName, String newName) {
        Node node = nodeMap.get(oldName);
        if (node == null) {
            System.out.println("Individu " + oldName + " tidak ditemukan.");
            return;
        }

        // Update nama di node dan map
        node.name = newName;
        nodeMap.remove(oldName);
        nodeMap.put(newName, node);
        System.out.println("Individu " + oldName + " berhasil diganti menjadi " + newName + ".");
    }

    //menu cari dan tampilkan

    private String determineRelationForMale(Node node, Node target) {
        if (node == target) return "diri sendiri";
    
        if (node.children.contains(target)) {
            return "ayah";
        }
    
        if ((node.father != null && node.father == target) || (node.mother != null && node.mother == target)) {
            return "anak";
        }
    
        if (node.spouse != null && node.spouse == target) {
            return "pasangan";
        }
    
        if (node.children.stream().anyMatch(child -> child.spouse == target)) {
            return "menantu";
        }
    
        if (node.children.stream().anyMatch(child -> child.children.contains(target))) {
            return "kakek";
        }
    
        if (node.getSiblings().contains(target)) {
            return "saudara laki-laki";
        }
    
        if (node.getSiblings().stream().anyMatch(sibling -> sibling.spouse == target)) {
            return "saudara ipar laki-laki";
        }
    
        if (node.getSiblings().stream().flatMap(sibling -> sibling.children.stream()).anyMatch(c -> c == target)) {
            return "paman";
        }
    
        if (node.getSiblings().stream().flatMap(sibling -> sibling.spouse != null ? sibling.spouse.children.stream() : Stream.empty()).anyMatch(c -> c == target)) {
            return "paman";
        }
    
        if ((node.father != null && node.father.getSiblings().contains(target)) ||
            (node.mother != null && node.mother.getSiblings().contains(target))) {
            return "keponakan";
        }
    
        if ((node.father != null && node.father.getSiblings().stream()
                .flatMap(sibling -> sibling.children.stream()).anyMatch(c -> c == target)) ||
            (node.mother != null && node.mother.getSiblings().stream()
                .flatMap(sibling -> sibling.children.stream()).anyMatch(c -> c == target))) {
            return "sepupu";
        }
    
        return "keluarga";
    }
    
    private String determineRelationForFemale(Node node, Node target) {
        if (node == target) return "diri sendiri";
    
        if (node.children.contains(target)) {
            return "ibu";
        }
    
        if ((node.father != null && node.father == target) || (node.mother != null && node.mother == target)) {
            return "anak";
        }
    
        if (node.spouse != null && node.spouse == target) {
            return "pasangan";
        }
    
        if (node.children.stream().anyMatch(child -> child.spouse == target)) {
            return "menantu";
        }
    
        if (node.children.stream().anyMatch(child -> child.children.contains(target))) {
            return "nenek";
        }
    
        if (node.getSiblings().contains(target)) {
            return "saudara";
        }
    
        if (node.getSiblings().stream().anyMatch(sibling -> sibling.spouse == target)) {
            return "saudara ipar perempuan";
        }
    
        if (node.getSiblings().stream().flatMap(sibling -> sibling.children.stream()).anyMatch(c -> c == target)) {
            return "bibi";
        }
    
        if (node.getSiblings().stream().flatMap(sibling -> sibling.spouse != null ? sibling.spouse.children.stream() : Stream.empty()).anyMatch(c -> c == target)) {
            return "bibi";
        }
    
        if ((node.father != null && node.father.getSiblings().contains(target)) ||
            (node.mother != null && node.mother.getSiblings().contains(target))) {
            return "keponakan";
        }
    
        if ((node.father != null && node.father.getSiblings().stream()
                .flatMap(sibling -> sibling.children.stream()).anyMatch(c -> c == target)) ||
            (node.mother != null && node.mother.getSiblings().stream()
                .flatMap(sibling -> sibling.children.stream()).anyMatch(c -> c == target))) {
            return "sepupu";
        }
    
        return "keluarga";
    }
    
    private String determineRelation(Node node, Node target, boolean isTarget) {
        // Tentukan relasi dengan individu target berdasarkan gender node
        String relation = node.gender.equalsIgnoreCase("L") 
            ? determineRelationForMale(node, target) 
            : determineRelationForFemale(node, target);
    
        // Validasi bahwa gender asli masing-masing tidak berubah sesuai input awal
        String nodeGender = node.gender.equalsIgnoreCase("L") ? "laki-laki" : "perempuan";
        String targetGender = target.gender.equalsIgnoreCase("L") ? "laki-laki" : "perempuan";
    
        // Pastikan logika pencarian relasi tidak memodifikasi gender asli
        if (!nodeGender.equalsIgnoreCase(targetGender)) {
            return relation + ", jenis kelamin: " + nodeGender ;
        }
    
        // Jika gender sama, tetap kembalikan dengan relasi sesuai logika
        return relation + ", jenis kelamin : " + targetGender;
    }
    

    
    
public void displayTree(String currentName, String prefix, boolean isLast, Node target, Set<Node> visited) {
    Node node = nodeMap.get(currentName.toLowerCase());
    if (node == null || visited.contains(node)) return; // Hindari loop

    // Tandai node yang sudah dikunjungi
    visited.add(node);

    // Tentukan relasi dengan individu target
    String relation = determineRelation(node, target, node == target);
    String partnerInfo = (node.spouse != null)
            ? " - " + node.spouse.name + " (" + determineRelation(node.spouse, target, false) + ", " + node.spouse.age + " tahun)"
            : "";

    System.out.println(prefix + (isLast ? "└── " : "├── ") + node.name + " (" + relation + ", " + node.age + " tahun)" + partnerInfo);

    prefix += isLast ? "    " : "│   ";

    // Pisahkan anak berdasarkan gender
    List<Node> femaleChildren = new ArrayList<>();
    List<Node> maleChildren = new ArrayList<>();
    for (Node child : node.children) {
        if (child.gender.equalsIgnoreCase("P")) {
            femaleChildren.add(child);
        } else {
            maleChildren.add(child);
        }
    }

    // Urutkan berdasarkan usia menurun
    femaleChildren.sort(Comparator.comparingInt(child -> -child.age));
    maleChildren.sort(Comparator.comparingInt(child -> -child.age));

    // Gabungkan list anak perempuan (kiri) dan laki-laki (kanan)
    List<Node> sortedChildren = new ArrayList<>();
    sortedChildren.addAll(femaleChildren);
    sortedChildren.addAll(maleChildren);

    // Tampilkan anak-anak
    for (int i = 0; i < sortedChildren.size(); i++) {
        displayTree(sortedChildren.get(i).name, prefix, i == sortedChildren.size() - 1, target, visited);
    }
}

// Contoh hasil pencarian
public void searchAndDisplay(String searchName) {
    Node target = nodeMap.get(searchName.toLowerCase());
    if (target == null) {
        System.out.println("Individu " + searchName + " tidak ditemukan.");
        return;
    }

    Node root = findRoot();

    System.out.println("\nStruktur Silsilah Keluarga untuk: " + searchName);

    // Inisialisasi set untuk node yang sudah dikunjungi
    Set<Node> visited = new HashSet<>();
    displayTree(root.name, "", true, target, visited);
}

// Fungsi untuk menemukan root pohon keluarga
private Node findRoot() {
    Node root = null;
    for (Node node : nodeMap.values()) {
        if (node.father == null && node.mother == null) {
            if (root == null || node.age > root.age) {
                root = node;
            }
        }
    }
    return root;
}

//menu jalur terpendek
public List<String> findShortestRelationshipPath(String startName, String endName) {
    Node startNode = nodeMap.get(startName.toLowerCase());
    Node endNode = nodeMap.get(endName.toLowerCase());

    if (startNode == null || endNode == null) {
        System.out.println("Salah satu individu tidak ditemukan.");
        return null;
    }

    Map<Node, Node> parentMap = new HashMap<>();
    Queue<Node> queue = new LinkedList<>();
    Set<Node> visited = new HashSet<>();

    queue.add(startNode);
    visited.add(startNode);

    while (!queue.isEmpty()) {
        Node current = queue.poll();

        if (current == endNode) {
            List<String> path = new LinkedList<>();
            while (current != null) {
                path.add(0, current.name);
                current = parentMap.get(current);
            }
            return path;
        }

        // Tambahkan tetangga (hubungan keluarga)
        addNeighborToQueue(current.father, current, queue, visited, parentMap);
        addNeighborToQueue(current.mother, current, queue, visited, parentMap);
        addNeighborToQueue(current.spouse, current, queue, visited, parentMap);
        for (Node child : current.children) {
            addNeighborToQueue(child, current, queue, visited, parentMap);
        }
        for (Node sibling : current.getSiblings()) {
            addNeighborToQueue(sibling, current, queue, visited, parentMap);
        }
    }

    System.out.println("Tidak ada jalur hubungan antara " + startName + " dan " + endName);
    return null;
}

private void addNeighborToQueue(Node neighbor, Node current, Queue<Node> queue, Set<Node> visited, Map<Node, Node> parentMap) {
    if (neighbor != null && !visited.contains(neighbor)) {
        queue.add(neighbor);
        visited.add(neighbor);
        parentMap.put(neighbor, current);
    }
}

public void displayShortestPath(String startName, String endName) {
    List<String> path = findShortestRelationshipPath(startName, endName);

    if (path == null || path.isEmpty()) {
        System.out.println("Tidak ada jalur hubungan antara " + startName + " dan " + endName);
        return;
    }

    Node startNode = nodeMap.get(startName.toLowerCase());
    Node endNode = nodeMap.get(endName.toLowerCase());

    if (startNode == null || endNode == null) {
        System.out.println("Individu " + startName + " atau " + endName + " tidak ditemukan.");
        return;
    }

    System.out.println("\nJalur Terpendek untuk Hubungan dari " + startName + " ke " + endName + ":");
    displayPathAsTree(path, "", true, 0, endNode);

    // Tentukan hubungan akhir antara individu awal dan tujuan
    String finalRelation = determineRelation(startNode, endNode, false);
    System.out.println("\nHubungan antara " + startName + " ke " + endName + " adalah: " + finalRelation);
}

private void displayPathAsTree(List<String> path, String prefix, boolean isLast, int index, Node target) {
    if (index >= path.size()) return;

    String currentName = path.get(index);
    Node currentNode = nodeMap.get(currentName.toLowerCase());

    if (currentNode == null) return;

    String relation = (index > 0) ? determineRelation(currentNode, nodeMap.get(path.get(index - 1).toLowerCase()), false) : "diri sendiri";

    // Tampilkan nama individu beserta keterangannya
    System.out.println(prefix + (isLast ? "└── " : "├── ") + currentNode.name + " (" + relation + ", " + currentNode.age + " tahun)");

    prefix += isLast ? "    " : "│   ";

    // Tampilkan individu berikutnya dalam jalur
    displayPathAsTree(path, prefix, true, index + 1, target);
}


//menu pernikahan
    public void addMarriage(String individualName, String spouseName, int spouseAge, String spouseGender) {
        addIndividual(spouseName, spouseAge, spouseGender);

        Node individual = nodeMap.get(individualName);
        Node spouse = nodeMap.get(spouseName);

        if (individual == null || spouse == null) {
            System.out.println("Pernikahan gagal: salah satu individu tidak ditemukan.");
            return;
        }

        individual.spouse = spouse;
        spouse.spouse = individual;

        System.out.println(individualName + " menikahi " + spouseName);
    }
}

public class ProgramsilsilahkeluargaVersi2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FamilyTree familyTree = new FamilyTree();

        System.out.println("=== Program Silsilah Keluarga ===");
        System.out.println("1. Tambahkan Hubungan Orang Tua dan Anak");
        System.out.println("2. Cari dan Tampilkan Struktur Silsilah");
        System.out.println("3. Tambahkan Pernikahan (Pasangan)");
        System.out.println("4. Hapus Individu");
        System.out.println("5. Ganti Nama Individu");
        System.out.println("6. Cari Jalur Terpendek untuk Hubungan");
        System.out.println("0. Keluar");

        while (true) {
            System.out.print("\nPilih opsi: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Masukkan angka sesuai pilihan menu.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Masukkan nama ayah: ");
                    String father = scanner.nextLine();
                    System.out.print("Masukkan nama ibu: ");
                    String mother = scanner.nextLine();

                    System.out.print("Masukkan jumlah anak: ");
                    int numChildren = Integer.parseInt(scanner.nextLine());

                    List<Node> children = new ArrayList<>();
                    for (int i = 0; i < numChildren; i++) {
                        System.out.print("Masukkan nama anak ke-" + (i + 1) + ": ");
                        String childName = scanner.nextLine();
                        System.out.print("Masukkan umur anak ke-" + (i + 1) + ": ");
                        int childAge = Integer.parseInt(scanner.nextLine());
                        System.out.print("Masukkan jenis kelamin anak ke-" + (i + 1) + " (Laki-laki/Perempuan): ");
                        String gender = scanner.nextLine();
                        children.add(new Node(childName, childAge, gender));
                    }

                    familyTree.addParentRelationship(father, mother, children);
                    break;

                case 2:
                    System.out.print("Masukkan nama individu yang ingin dicari: ");
                    String searchName = scanner.nextLine();
                    familyTree.searchAndDisplay(searchName);
                    break;

                case 3:
                    System.out.print("Masukkan nama individu yang ingin menikah: ");
                    String name = scanner.nextLine();
                    System.out.print("Masukkan nama pasangan: ");
                    String spouseName = scanner.nextLine();
                    System.out.print("Masukkan umur pasangan: ");
                    int spouseAge = Integer.parseInt(scanner.nextLine());
                    System.out.print("Masukkan jenis kelamin pasangan (Laki-laki/Perempuan): ");
                    String spouseGender = scanner.nextLine();

                    familyTree.addMarriage(name, spouseName, spouseAge, spouseGender);
                    break;

                case 4:
                    System.out.print("Masukkan nama individu yang ingin dihapus: ");
                    String nameToDelete = scanner.nextLine();
                    familyTree.deleteIndividual(nameToDelete);
                    break;

                case 5:
                    System.out.print("Masukkan nama individu yang ingin diganti: ");
                    String oldName = scanner.nextLine();
                    System.out.print("Masukkan nama baru: ");
                    String newName = scanner.nextLine();
                    familyTree.renameIndividual(oldName, newName);
                    break;

                case 6:
                    System.out.print("Masukkan nama individu awal: ");
                    String startName = scanner.nextLine();
                    System.out.print("Masukkan nama individu tujuan: ");
                    String endName = scanner.nextLine();
                    familyTree.displayShortestPath(startName, endName);
                    break;                

                case 0:
                    System.out.println("Keluar dari program.");
                    return;

                default:
                    System.out.println("Pilihan tidak valid. Masukkan angka sesuai pilihan menu.");
            }
        }
    }
}
