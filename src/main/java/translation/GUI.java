package translation;

import translation.LanguageCodeConverter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.*;
import java.util.Arrays;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new JSONTranslator();
            LanguageCodeConverter languageConverter = new LanguageCodeConverter();

            JPanel countryPanel = new JPanel();
            JTextField countryField = new JTextField(10);
            countryField.setText("can");
            countryField.setEditable(false); // we only support the "can" country code for now
            countryPanel.add(new JLabel("Country:"));
            countryPanel.add(countryField);

            JPanel languagePanel = new JPanel();
            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String countryCode : translator.getLanguageCodes()) {
                languageComboBox.addItem(languageConverter.fromLanguageCode(countryCode));
            }
            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(languageComboBox);

            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Submit");
            buttonPanel.add(submit);

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);


            String[] items = new String[translator.getCountryCodes().size()];

            int i = 0;
            for(String countryCode : translator.getCountryCodes()) {
                items[i++] = countryCode; //use CountryCodeConverter here (convert to country)
            }

            JList list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            list.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    String language = languageComboBox.getSelectedItem().toString();
                    String languageCode = languageConverter.fromLanguage(language);
                    String country = (String) list.getSelectedValue();
                    String countryCode = ""; //convert here
                    String result = translator.translate(countryCode, languageCode);
                    resultLabel.setText(result);
                }
            });


            // adding listener for when the user clicks the submit button
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String language = languageConverter.fromLanguage(languageComboBox.getSelectedItem().toString());
                    String country = countryField.getText();

                    // for now, just using our simple translator, but
                    // we'll need to use the real JSON version later.
                    Translator translator = new JSONTranslator();

                    String result = translator.translate(country, language);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);

                }

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            frame.setLocationRelativeTo(null);
            frame.setSize(350, 200);
        });
    }
}
