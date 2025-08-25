package ntk.project;

public class Student extends Person implements Utils {
    private String msv;

    public Student(String name, int age) {
        super(name, age);
    }


    // override
    @Override
    public void getInfoPerson() {
        super.getInfoPerson();
    }

    // overrload
    public int getTest() {
        return 1;
    }

    //overload
    public int getTest(int a) {
        return 2;
    }

    public String getMsv() {
        return msv;
    }

    public void setMsv(String msv) {
        this.msv = msv;
    }

    @Override
    public void getKien() {

    }
}
