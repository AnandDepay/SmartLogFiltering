package com.anand.file;

import org.jdesktop.swingx.calendar.SingleDaySelectionModel;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class SmartLogFilter extends JXDatePicker {
    private JSpinner timeSpinner;
    private JPanel timePanel;
    private DateFormat timeFormat;
    private static JTextField textField;
    private static ReadAllFiles readFilesObj = null;

    public SmartLogFilter() {
        super();
        getMonthView().setSelectionModel(new SingleDaySelectionModel());
    }

    public SmartLogFilter( Date d ) {
        this();
        setDate(d);
    }

    public void commitEdit() throws ParseException {
        commitTime();
        super.commitEdit();
    }

    public void cancelEdit() {
        super.cancelEdit();
        setTimeSpinners();
    }

    @Override
    public JPanel getLinkPanel() {
        super.getLinkPanel();
        if( timePanel == null ) {
            timePanel = createTimePanel();
            timePanel.setSize(new Dimension(60,40));
        }
        setTimeSpinners();
        return timePanel;
    }

    private JPanel createTimePanel() {
        JPanel newPanel = new JPanel();
        newPanel.setSize(new Dimension(300, 60));
//        newPanel.setLayout(new FlowLayout());
        //newPanel.add(panelOriginal);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        timeSpinner = new JSpinner(dateModel);
        if( timeFormat == null ) timeFormat = DateFormat.getTimeInstance( DateFormat.SHORT );
        updateTextFieldFormat();
        newPanel.add(new JLabel( "Time:" ) );
        newPanel.add(timeSpinner);
//        newPanel.setBackground(Color.GREEN);
        return newPanel;
    }

    private void updateTextFieldFormat() {
        if( timeSpinner == null ) return;
        JFormattedTextField tf = ((JSpinner.DefaultEditor) timeSpinner.getEditor()).getTextField();
        DefaultFormatterFactory factory = (DefaultFormatterFactory) tf.getFormatterFactory();
        DateFormatter formatter = (DateFormatter) factory.getDefaultFormatter();
        // Change the date format to only show the hours
        formatter.setFormat( timeFormat );
    }

    private void commitTime() {
        Date date = getDate();
        if (date != null) {
            Date time = (Date) timeSpinner.getValue();
            GregorianCalendar timeCalendar = new GregorianCalendar();
            timeCalendar.setTime( time );

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get( Calendar.HOUR_OF_DAY ) );
            calendar.set(Calendar.MINUTE, timeCalendar.get( Calendar.MINUTE ) );
            calendar.set(Calendar.SECOND, timeCalendar.get( Calendar.SECOND ));
            calendar.set(Calendar.MILLISECOND, 0);

            Date newDate = calendar.getTime();
            setDate(newDate);
        }

    }

    private void setTimeSpinners() {
        Date date = getDate();
        if (date != null) {
            timeSpinner.setValue( date );
        }
    }

    public DateFormat getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(DateFormat timeFormat) {
        this.timeFormat = timeFormat;
        updateTextFieldFormat();
    }

    public static void main(String[] args) {
        Date date = new Date();
        JFrame frame = new JFrame();
        
        frame.setTitle("Smart Log Filter");
        frame.setBounds(100, 100, 700, 500);
        frame.setPreferredSize(new Dimension(700,400));
        frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final SmartLogFilter dateTimePicker = new SmartLogFilter();
        dateTimePicker.setFormats( DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.MEDIUM ) );
        dateTimePicker.setTimeFormat( DateFormat.getTimeInstance( DateFormat.MEDIUM ) );

        dateTimePicker.setDate(date);
        
        JLabel lblTimestamp = new JLabel("Select Timestamp:");
        lblTimestamp.setBounds(65, 55, 200, 14);
        frame.getContentPane().add(lblTimestamp);
        
        dateTimePicker.setBounds(180, 45, 150, 30);
        frame.getContentPane().add(dateTimePicker);

        JLabel lblInterval = new JLabel("Enter Interval:");
        lblInterval.setBounds(65, 120, 200, 14);
        frame.getContentPane().add(lblInterval);
        
        textField = new JTextField();
        textField.setBounds(180, 112, 50, 30);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        
        JLabel lblException = new JLabel("Exception Count:");
        lblException.setBounds(65, 185, 200, 14);
        frame.getContentPane().add(lblException);
        
        final JRadioButton radioButton = new JRadioButton("");
        radioButton.setSelected(true);
        radioButton.setBounds(180, 179, 50, 30);
        frame.getContentPane().add(radioButton);
        
        JButton btnSubmit = new JButton("Create File");
        
        btnSubmit.setBounds(165, 250, 120, 50);
        frame.getContentPane().add(btnSubmit);
        
        JButton btnClear = new JButton("Reset");
        
        btnClear.setBounds(365, 250, 120, 50);
        frame.getContentPane().add(btnClear);
        
        frame.pack();
        frame.setVisible(true);
        
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(textField.getText().isEmpty())
                    JOptionPane.showMessageDialog(null, "Data Missing");
                else {
                JOptionPane.showMessageDialog(null, "Data Submitted");
                try {
                	readFilesObj = new ReadAllFiles();
						readFilesObj.processDateTime(dateTimePicker, Integer.parseInt(textField.getText()), radioButton.isSelected());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                }
            }
        });
        
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textField.setText(null);
                radioButton.setSelected(false);
                dateTimePicker.setDate(new Date());
            }
        });
    }
}