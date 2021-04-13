import javax.swing.*;

public class Main extends JFrame {
    public Main() {
        setTitle("Змейка");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(370,380);
        setLocation(400,200);
        add(new GameField());
        setVisible(true);
        setResizable(false);
    }
    public static void main(String[] args) {
        Main mw = new Main();
    }
}
