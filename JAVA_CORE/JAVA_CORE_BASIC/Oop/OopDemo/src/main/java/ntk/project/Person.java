package ntk.project;

// đây là 1 class
public abstract class Person {
    // access modifiel
    private String name;
    private int age;

    public void getInfoPerson() {
        System.out.println("Tên của bạn là: "+name+ " bạn có số tuổi là: "+age);
    }


    // METHOD FINAL
    public final int getTestFinal() {
        return 3;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void getTestExtend() {
        System.out.println("TestExtend");
    }

    public abstract void getKien();

}
