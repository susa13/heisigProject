import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Integer.parseInt;

public class heisigProject extends JFrame implements ActionListener{
    private JButton random, remove, reset, answer;
    private JTextField wordDisplay, textBox1, textBox2, answerBox;
    private JLabel label;
    private int start, end;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> kanji = new ArrayList<>();
    private int counter = 0, current = 0;

    public heisigProject() {
        super();
        setTitle("heisigProject");
        setSize(600,145);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        random = new JButton("Random");
        remove = new JButton("Remove");
        reset = new JButton("Reset");
        answer = new JButton("Answer");
        label = new JLabel("Enter the interval to study");
        wordDisplay = new JTextField(15);
        textBox1 = new JTextField(4);
        textBox2 = new JTextField(4);
        answerBox = new JTextField(4);
        random.addActionListener(this);
        remove.addActionListener(this);
        reset.addActionListener(this);
        answer.addActionListener(this);
        load(); //loads file for the first time
        setLayout(new BorderLayout());
        JPanel north = new JPanel();
        north.add(label);
        north.add(textBox1);
        north.add(textBox2);
        north.add(wordDisplay);
        add(north, BorderLayout.NORTH);
        answerBox.setFont(new Font(answer.getFont().getName(),answer.getFont().getStyle(),48));
        JPanel center = new JPanel();
        center.add(random);
        center.add(remove);
        center.add(reset);
        center.add(answer);
        center.add(answerBox);
        add(center, BorderLayout.CENTER);
        setVisible(true);
        wordDisplay.setEditable(false);
        answerBox.setEditable(false);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == reset)
            load();
        else if(e.getSource() == answer) {
            if(!wordDisplay.getText().equals("All Cleared") && !wordDisplay.getText().equals(""))
                answerBox.setText(kanji.get(current));
        }
        else if(wordDisplay.getText().equals("All Cleared") || wordDisplay.getText().equals("Error")) {}
        else if(e.getSource() == remove) {
            if(wordDisplay.getText().equals("")) {}
            else {
                if(counter != list.size()) {
                    list.set(list.indexOf(wordDisplay.getText()), "removed");
                    counter++;
                    if (counter == list.size())
                        wordDisplay.setText("All Cleared");
                }
            }
        }
        try {
            int start = parseInt(textBox1.getText());
            int end = parseInt(textBox2.getText());
            if(end < start || end > list.size())
                wordDisplay.setText("Error");
            else if(allRemoved(start, end))
                wordDisplay.setText("All Cleared");
            else if(e.getSource() == answer) {}
            else {
                setRandom(start, end);
                answerBox.setText("");
            }
        } catch (Exception er) {
            if((!wordDisplay.getText().equals("") || e.getSource() == random) && e.getSource() != reset && e.getSource() != answer) {
                setRandom(1, list.size());
                answerBox.setText("");
            }
        }
    }
    public void setRandom(int start, int end) {
        int randomNum = ThreadLocalRandom.current().nextInt(start, end + 1);
        if(list.get(randomNum - 1).equals("removed"))
            if(!allRemoved(start, end))
                setRandom(start, end);
            else {}
        else {
            wordDisplay.setText(list.get(randomNum - 1));
            current = randomNum-1;
        }
    }
    public void load() {
        list.clear();
        try {
            Scanner scan = new Scanner(new File("heisig.txt")); //".\\src\\heisig.txt"
            while(scan.hasNextLine()) {
                list.add(scan.nextLine());
            }
            scan = new Scanner(new File("kanji.txt"), "utf-8");
            while(scan.hasNextLine()) {
                String temp = scan.nextLine();
                try {
                    kanji.add(temp.substring(temp.indexOf(" ")));
                } catch(Exception e) {
                    kanji.add(temp);
                }
            }
            scan.close();
        } catch(Exception er) {
            er.printStackTrace();
        }
        counter = 0;
    }
    public boolean allRemoved(int start, int end) {
        for(int i = start; i <= end; i++) {
            if(!list.get(i - 1).equals("removed"))
                return false;
        }
        return true;
    }
    public static void main(String[] args) {
        new heisigProject();
    }
}
