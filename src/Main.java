import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class Main implements ActionListener{
    private final int windowWidth = 300;
    private final int windowHeight = 310;
    private int semitoneChange = 0;
    private boolean changeKeys = false;
    private JCheckBox keyChangeCheck;
    private String[] keyOptions =
            {"[SELECT]", "A", "A#/B♭", "B", "C", "C#/D♭", "D", "D#/E♭", "E", "F", "F#/G♭", "G", "G#/A♭"};
    private JComboBox currKeyComboBox, newKeyComboBox;
    private JLabel currKeyLabel, newKeyLabel, directionLabel, semitoneLabel, errorLabel;
    private JRadioButton upRadioButton, downRadioButton;
    private JButton grabButton;
    private JTextField urlField;
    JFrame window;
    JFileChooser fileChooser;

    public static void main(String[] args)
    {
        Main newWindow = new Main();
    }

    public Main() {
        window = new JFrame("Easy Audio Grab");

        //URL Text Field Label
        JLabel urlLabel = new JLabel("YouTube URL:");
        urlLabel.setBounds(10, 10, 100, 20);
        window.add(urlLabel);

        //URL Text Field
        urlField = new JTextField();
        urlField.setBounds(10, 30, 265, 30);
        window.add(urlField);

        //Checkbox for key change
        keyChangeCheck = new JCheckBox();
        keyChangeCheck.setSelected(false);
        keyChangeCheck.setBounds(6,80, 20, 20);
        keyChangeCheck.addActionListener(this);
        window.add(keyChangeCheck);

        //Change Keys Label
        JLabel changeKeysLabel = new JLabel("Change Keys");
        changeKeysLabel.setBounds(30, 80, 100, 20);
        window.add(changeKeysLabel);

        // Current Key Label
        currKeyLabel = new JLabel("Current Key:");
        currKeyLabel.setBounds(10, 110, 100, 20);
        currKeyLabel.setForeground(Color.GRAY);
        window.add(currKeyLabel);

        // Current Key DropDown
        currKeyComboBox = new JComboBox(keyOptions);
        currKeyComboBox.setBounds(100, 110, 100, 20);
        currKeyComboBox.addActionListener(this);
        currKeyComboBox.setEnabled(false);
        window.add(currKeyComboBox);

        // New Key Label
        newKeyLabel = new JLabel("New Key:");
        newKeyLabel.setBounds(10, 140, 100, 20);
        newKeyLabel.setForeground(Color.GRAY);
        window.add(newKeyLabel);

        // New Key DropDown
        newKeyComboBox = new JComboBox(keyOptions);
        newKeyComboBox.setBounds(100, 140, 100, 20);
        newKeyComboBox.addActionListener(this);
        newKeyComboBox.setEnabled(false);
        window.add(newKeyComboBox);

        // Direction label
        directionLabel = new JLabel("Direction:");
        directionLabel.setBounds(10, 170, 95, 20);
        directionLabel.setForeground(Color.GRAY);
        window.add(directionLabel);

        // Radio Buttons
        upRadioButton = new JRadioButton("Up", true);
        upRadioButton.setBounds(95, 170, 50, 20);
        upRadioButton.addActionListener(this);
        upRadioButton.setEnabled(false);
        downRadioButton = new JRadioButton("Down", true);
        downRadioButton.setBounds(150, 170, 80, 20);
        downRadioButton.addActionListener(this);
        downRadioButton.setEnabled(false);
        ButtonGroup directionGroup = new ButtonGroup();
        directionGroup.add(upRadioButton);
        directionGroup.add(downRadioButton);
        window.add(upRadioButton);
        window.add(downRadioButton);

        // Semitones label
        semitoneLabel = new JLabel("Semitones (Half-steps):  -");
        semitoneLabel.setBounds(10, 200, 200, 20);
        semitoneLabel.setForeground(Color.GRAY);
        window.add(semitoneLabel);

        // Error Label
        errorLabel = new JLabel("ERROR: ", JLabel.CENTER);
        errorLabel.setBounds(10, 220, 265, 20);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
        window.add(errorLabel);

        //Grab Audio Button
        grabButton = new JButton("Grab Audio");
        grabButton.setBounds(90,240,100,20);
        grabButton.addActionListener(this);
        window.add(grabButton);

        //Set Window Configurations
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int windowXPos = Math.round((float)gd.getDisplayMode().getWidth() / 2) - Math.round((float)windowWidth / 2);
        int windowYPos = Math.round((float)gd.getDisplayMode().getHeight() / 2)- Math.round((float)windowHeight / 2);
        System.out.println(windowXPos);
        window.setSize(windowWidth,windowHeight);
        window.setLocation(windowXPos, windowYPos);
        window.setLayout(null);
        window.setResizable(false);
        window.setVisible(true);
    }

    public String CalculateSemitones (String currKey, String newKey, boolean moveUp)
    {
        String[] keys = {"A", "A#/B♭", "B", "C", "C#/D♭", "D", "D#/E♭", "E", "F", "F#/G♭", "G", "G#/A♭"};
        int startIndex = -1;

        //Get Starting index
        for (int i = 0; i < keys.length; i++)
        {
            if (currKey.equals(keys[i]))
            {
                startIndex = i;
                break;
            }
        }

        //Return ERROR if starting index couldn't be found
        if (startIndex == -1)
        {
            return "ERROR1";
        }

        if (moveUp)
        {
            boolean repeat = false;
            int counter = 0;
            for (int i = startIndex; i < keys.length; i++)
            {
                if (i == startIndex && repeat)
                    return "ERROR2"; //Return ERROR if semitones cant be calculated
                else
                    repeat = true;
                if (keys[i].equals(newKey))
                    return "" + counter;
                if (i >= keys.length - 1) //Check for loop to beginning
                    i = -1;
                counter++;
            }
        }
        else
        {
            boolean repeat = false;
            int counter = 0;
            for (int i = startIndex; i > -1; i--)
            {
                if (i == startIndex && repeat)
                    return "ERROR3"; //Return ERROR if semitones cant be calculated
                else
                    repeat = true;
                if (keys[i].equals(newKey))
                    return "" + counter;
                if (i <= 0) //Check for loop to end
                    i = keys.length;
                counter++;
            }
        }
        return "ERROR4";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Enable KeyChange related elements
        if (e.getSource() == keyChangeCheck) {
            if (keyChangeCheck.isSelected())
            {
                currKeyComboBox.setEnabled(true);
                newKeyComboBox.setEnabled(true);
                upRadioButton.setEnabled(true);
                downRadioButton.setEnabled(true);
                currKeyLabel.setForeground(Color.BLACK);
                newKeyLabel.setForeground(Color.BLACK);
                directionLabel.setForeground(Color.BLACK);
                semitoneLabel.setForeground(Color.BLACK);
            }
            else
            {
                currKeyComboBox.setEnabled(false);
                newKeyComboBox.setEnabled(false);
                upRadioButton.setEnabled(false);
                downRadioButton.setEnabled(false);
                currKeyLabel.setForeground(Color.GRAY);
                newKeyLabel.setForeground(Color.GRAY);
                directionLabel.setForeground(Color.GRAY);
                semitoneLabel.setForeground(Color.GRAY);
            }
        }

        //Check if semitones need to be calculated
        if (e.getSource() == currKeyComboBox || e.getSource() == newKeyComboBox ||
                e.getSource() == upRadioButton || e.getSource() == downRadioButton)
        {
            String semitoneValue = "";

            // Return if both keys aren't selected
            if (currKeyComboBox.getSelectedItem().toString().equals(keyOptions[0]) ||
                    newKeyComboBox.getSelectedItem().toString().equals(keyOptions[0]))
            {
                semitoneLabel.setText("Semitones (Half-steps):  -");
                return;
            }

            //Calculate Semitones
            semitoneValue = CalculateSemitones(currKeyComboBox.getSelectedItem().toString(),
                    newKeyComboBox.getSelectedItem().toString(), upRadioButton.isSelected());

            //Set semitone variable values
            try
            {
                semitoneChange = Integer.parseInt(semitoneValue);
            }
            catch (Exception intExcept)
            {
                System.out.println("ERROR: INVALID SEMITONE VALUE");
                semitoneChange = 1;
            }
            if (downRadioButton.isSelected())
                semitoneChange *= -1;
            semitoneLabel.setText("Semitones (Half-steps):  " + semitoneValue);
        }

        //If Grab Audio Button is pressed
        if (e.getSource() == grabButton)
        {
            //Error if there's no URL
            if (urlField.getText().equals(""))
            {
                errorLabel.setText("ERROR: Enter a YouTube URL");
                errorLabel.setVisible(true);
                return;
            }

            //Error if keys aren't selected when they're supposed to be
            if (keyChangeCheck.isSelected() && currKeyComboBox.getSelectedItem().toString().equals(keyOptions[0]) ||
                    keyChangeCheck.isSelected() && newKeyComboBox.getSelectedItem().toString().equals(keyOptions[0]))
            {
                errorLabel.setText("ERROR: Ensure both keys are selected.");
                errorLabel.setVisible(true);
                return;
            }

            try
            {
                String url = urlField.getText();
                //Setup download command
                ProcessBuilder builder  =
                        new ProcessBuilder("cmd.exe", "/c", "cd \"" +
                                System.getProperty("user.dir") + "\\src\" && " +
                                "youtube-dl.exe -f bestaudio " + url);
                builder.redirectErrorStream(true); //Get cmd output lines
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader((p.getInputStream())));
                String line = "";
                String fileName = "";
                while (true) //Loop through cmd output lines
                {
                    line = r.readLine();
                    if (line != null && line.startsWith("[download] Destination:")) //Get filename
                        fileName = line.substring(24);
                    else if (line != null && line.contains("has already been downloaded")) //Get filename if file exists
                        fileName = line.substring(11).replace(" has already been downloaded", "");
                    if (line == null)
                        break;
                    System.out.println(line);
                }
                System.out.println("FileName: \"" + fileName + "\""); //Print filename

                //Get audioName
                String cleanFilename = fileName;
                cleanFilename = fileName.replaceAll(" ", "-");
                String audioName = "";
                for (int i = cleanFilename.length() - 1; i > -1; i--)
                {
                    if (cleanFilename.charAt(i) == '.')
                    {
                        audioName = cleanFilename.substring(0, i) + ".wav";
                        break;
                    }
                }

                //Check if out exists
                File checkOut = new File(System.getProperty("user.dir") + "\\src\\out.wav");
                if (checkOut.exists())
                    checkOut.delete();
                File checkOut2 = new File(System.getProperty("user.dir") + "\\src\\out2.wav");
                if (checkOut2.exists())
                    checkOut2.delete();

                // Convert audio process - output to out.mp3
                ProcessBuilder builderConvert = new ProcessBuilder("cmd.exe", "/c", "cd \"" +
                        System.getProperty("user.dir") + "\\src\\ffmpeg\\bin\" && ffmpeg.exe -i \"..\\..\\" +
                        fileName + "\" \"..\\..\\out.wav\"");
                builderConvert.redirectErrorStream(true);
                Process pConvert = builderConvert.start();
                BufferedReader rConvert = new BufferedReader(new InputStreamReader((pConvert.getInputStream())));
                line = "";
                while (true) //Loop through output lines
                {
                    line = rConvert.readLine();
                    if (line == null)
                        break;
                    System.out.println(line);
                }
                System.out.println("END CONVERSION");

                //Delete non-mp3 file
                File toDelete = new File(System.getProperty("user.dir") + "\\src\\" + fileName);
                toDelete.delete();

                //Change audio pitch if necessary
                if (keyChangeCheck.isSelected())
                {
                    // Change pitch
                    ProcessBuilder builderPitch  =
                            new ProcessBuilder("cmd.exe", "/c", "cd \"" +
                                    System.getProperty("user.dir") + "\\src\" && " +
                                    "soundstretch.exe out.wav out2.wav -pitch=" + semitoneChange);
                    builderPitch.redirectErrorStream(true);
                    Process pPitch = builderPitch.start();
                    BufferedReader rPitch = new BufferedReader(new InputStreamReader((pPitch.getInputStream())));
                    line = "";
                    while (true) //Loop through output lines
                    {
                        line = rPitch.readLine();
                        if (line == null)
                            break;
                        System.out.println(line);
                    }
                    System.out.println("END PITCH CHANGE");

                    //Delete out.wav file
                    File outDelete = new File(System.getProperty("user.dir") + "\\src\\out.wav");
                    outDelete.delete();

                    // Rename out2.wav to out.wav
                    try {
                        Path source = Paths.get(System.getProperty("user.dir") + "\\src\\out2.wav");
                        Path target = Paths.get(System.getProperty("user.dir") + "\\src\\out.wav");
                        Files.move(source, target);
                    }
                    catch (IOException ioExcept)
                    {
                        ioExcept.printStackTrace();
                    }

                    System.out.println("Successful file modifications");
                }

                //File chooser
                fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Audio File");
                File defFile = new File(fileChooser.getFileSystemView().getDefaultDirectory().getAbsolutePath()
                        + "\\" + audioName);
                System.out.println(defFile.getAbsolutePath().toString());
                fileChooser.setSelectedFile(defFile);
                int userSelection = fileChooser.showSaveDialog(window);

                if (userSelection == JFileChooser.APPROVE_OPTION)
                {
                    File fileToSave = fileChooser.getSelectedFile();
                    Path source = Paths.get(System.getProperty("user.dir") + "\\src\\out.wav");
                    String saveFilePath = fileToSave.getAbsolutePath().toString();
                    if (!saveFilePath.substring(saveFilePath.length() - 4).toLowerCase().equals(".wav"))
                    {
                        fileToSave = new File(saveFilePath += ".wav");
                    }
                    Path target = Paths.get(fileToSave.getAbsolutePath());
                    Files.move(source, target);
                }
            }
            catch (Exception except)
            {
                throw new RuntimeException(except);
            }

            errorLabel.setVisible(false);
        }
    }
}