package ntk.project;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Student s1 = new Student("S1", 1);
        //S1 la mot instance cua class Student
        // check class con ke thua method final cua class cha
        System.out.println("TestFinal: "+s1.getTestFinal());
        // test 1 astract va 1 inteface co method cung ten
        // dang test truong hop method abstract co body
        // no se uu tien method cua abtract class co body hon
        s1.getTestExtend();

        // truong hop method cua abstract class la method khong body
        // no se override 1 lan neu cung kieu du lieu tra ve
        // neu khac kieu du lieu tra ve thi no se conflict
    }
}