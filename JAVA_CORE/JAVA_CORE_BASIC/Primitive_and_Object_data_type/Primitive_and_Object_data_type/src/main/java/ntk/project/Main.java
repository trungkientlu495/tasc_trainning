package ntk.project;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Kieu du lieu nguyen thuy
        int a = 5;
        float b = 5.0f;
        // Kieu du lieu doi tuong
        String s = "Kien";
        Integer d = 5;
        // Chuyen doi giua 2 kieu du lieu
        System.out.println("Chuyển đổi 2 kiểu dữ liệu");
        Integer c = a;
        System.out.println("Chuyển từ primitive về Object: "+c);
        int e = d;
        System.out.println("Chuyển đổi từ Object về Primitive: "+e);
        //So sanh 2 kieu du lieu
        // So sanh giua 2 kieu du lieu deu la nguyen thuy
        System.out.println("SO SÁNH 2 BIẾN ĐỀU THUỘC PRIMITIVE: "+(a==e));
        // So sanh giua 2 kieu du lieu deu la object
        // neu dung ==
        // luu y neu Integer d =5 nó thuộc -128 đến 127 thì đều lay tu 1 dia chi
        // nên sẽ trả về true
        // còn nêu khơởitaojo 1 doi tuong new thi se la false
        System.out.println("SO SÁNH 2 BIẾN ĐỀU THUỘC Object: "+(c==d));
        // neu dung equal
        System.out.println("SO SÁNH 2 BIẾN ĐỀU THUỘC Object: "+(c.equals(d)));
        // gia tri khi khoi tao hai bien
        Student student = new Student();
        System.out.println("Biến khởi tạo của String là: "+student.getName());
        System.out.println("Biến khởi tạo của int là: "+student.getAge());
        System.out.println("Biến khởi tạo của boolean là: "+student.isGender());
        System.out.println("Biến khởi tạo của char là: "+student.getCharFirstName());



    }
}