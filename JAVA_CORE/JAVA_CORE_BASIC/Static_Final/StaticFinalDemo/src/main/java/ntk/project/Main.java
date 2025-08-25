package ntk.project;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // bien static
        System.out.println("Biến static: "+Student.age);
        // method static
        System.out.println("Method static: "+Student.getAge(Student.age));
        // class static
        new Student.test().method();
    }
}