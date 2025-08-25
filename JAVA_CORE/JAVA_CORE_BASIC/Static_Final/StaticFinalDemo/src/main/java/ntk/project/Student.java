package ntk.project;

//final Class
public final class Student {
    //static cho bien
    public static int age = 20;
    // hang so cho bien
    private final int year = 2003;
    // method static
    public static int getAge(int age) {
        return age;
    }

    public Student() {
    }

    // method hang so
    public final int getYear() {
        return year;
    }

    //class static
    static class test {
        public void method(){
            System.out.println("test static class");
        }
    }
}
