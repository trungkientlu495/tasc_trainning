package ntk.project;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        ShapeFactory factory1 = new CircleFactory();
        Shape circle = factory1.createShape();
        circle.draw(); // Output: Drawing Circle

        ShapeFactory factory2 = new RectangleFactory();
        Shape rectangle = factory2.createShape();
        rectangle.draw(); // Output: Drawing Rectangle
    }
}