package ntk.project;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Cac cach tao String
        String s1 = "S1";
        String s2 = new String("S1");
        char[] arr = {'J', 'a', 'v', 'a'};
        String s3 = new String(arr);
        byte[] b = {65, 66, 67}; // A B C trong ASCII
        String s4 = new String(b);
        String s7 = String.valueOf(123); // "123" // tro doi tuong trong pool
        String s8 = String.format("Xin chào %s", "Kiên");
        String s9 = String.join(" - ", "Java", "Spring", "Docker");
        // So sanh 2 chuoi co 2 loai
        // == so sanh dia chi 2 chuoi do
        // equal so sanh gia tri 2 chuoi do
        String a = "Kien";
        String a1 = new String("Kien");
        System.out.println("Checked: "+(a==a1));
        System.out.println("Checked: "+(a.equals(a1)));

    }
}