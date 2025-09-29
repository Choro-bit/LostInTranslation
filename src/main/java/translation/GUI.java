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
            CountryCodeConverter countryConverter = new CountryCodeConverter();

            JPanel countryPanel = new JPanel();

            JPanel languagePanel = new JPanel();
            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String countryCode : translator.getLanguageCodes()) {
                languageComboBox.addItem(languageConverter.fromLanguageCode(countryCode));
            }
            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(languageComboBox);

            JPanel buttonPanel = new JPanel();

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);


            String[] items = new String[countryConverter.getNumCountries()];

            int i = 0;
            for(String countryCode : translator.getCountryCodes()) {
                items[i++] = countryConverter.fromCountryCode(countryCode);
            }

            JList list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(list);
            countryPanel.add(scrollPane);

            list.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    String language = languageComboBox.getSelectedItem().toString();
                    String languageCode = languageConverter.fromLanguage(language);
                    String country = (String) list.getSelectedValue();
                    String countryCode = countryConverter.fromCountry(country);
                    String result = translator.translate(countryCode, languageCode);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);
                }
            });

            languageComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String language = languageComboBox.getSelectedItem().toString();
                        String languageCode = languageConverter.fromLanguage(language);
                        String country = (String) list.getSelectedValue();
                        String countryCode = countryConverter.fromCountry(country);
                        String result = translator.translate(countryCode, languageCode);
                        if (result == null) {
                            result = "no translation found!";
                        }
                        resultLabel.setText(result);
                    }
                }
            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);
            mainPanel.add(countryPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

            frame.setLocationRelativeTo(null);
            frame.setSize(450, 300);
        });
    }
}
