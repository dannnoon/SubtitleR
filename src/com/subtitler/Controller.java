package com.subtitler;

import com.subtitler.data.Time;
import com.subtitler.util.subtitlemanager.SubtitleManager;
import com.subtitler.util.subtitlemanager.SubtitleManagerFactory;
import com.sun.istack.internal.Nullable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

import static com.subtitler.util.TextFieldUtil.addTextLimiter;

public class Controller {

    @FXML
    public Label doneLabel;
    @FXML
    public TextField hourText;
    @FXML
    public TextField minuteText;
    @FXML
    public TextField secondText;
    @FXML
    public TextField millisecondText;
    @FXML
    public Label originalTimeLabel;
    @FXML
    public Label originalTimeValue;
    @FXML
    public Button restoreTime;
    @FXML
    private Label fileNameLabel;
    @FXML
    private Label fileNameValue;
    @FXML
    private Label startTimeLabel;
    @FXML
    private Button pickFileButton;
    @FXML
    private Button saveFileButton;

    private SubtitleManager subtitleManager = null;

    @FXML
    private void initialize() {
        addTextLimiter(hourText, 2);
        addTextLimiter(minuteText, 2);
        addTextLimiter(secondText, 2);
        addTextLimiter(millisecondText, 3);

        initViewWithFile();

        pickFileButton.setOnAction(event -> {
            File chosenFile = chooseFile();

            if (chosenFile != null) {
                subtitleManager = SubtitleManagerFactory.create(chosenFile);
                initViewWithFile();
            }
        });

        saveFileButton.setOnAction(event -> {
            final Time time = getTime();
            changeTimeAction(time);
        });

        restoreTime.setOnAction(event -> changeTimeAction(subtitleManager.getOriginalStartTime()));
    }

    private void changeTimeAction(Time time) {
        saveFileButton.setDisable(true);
        restoreTime.setDisable(true);

        try {
            new Thread(() -> {
                subtitleManager.changeStartTime(time);
                Platform.runLater(() -> {
                    doneLabel.setVisible(true);
                    saveFileButton.setDisable(false);
                    initViewWithFile();
                });
                try {
                    Thread.sleep(800);
                    Platform.runLater(() -> doneLabel.setVisible(false));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private File chooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select subtitle file");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Subtitle file", "*.srt", "*.txt")
        );
        return chooser.showOpenDialog(fileNameLabel.getScene().getWindow());
    }

    private void initViewWithFile() {
        if (subtitleManager != null) {
            fileNameValue.setText(subtitleManager.getFile().getName());
            fileNameLabel.setDisable(false);
            saveFileButton.setDisable(false);
            startTimeLabel.setDisable(false);
            updateRestoreButton();
            initEnabledTimeInput();
        } else {
            fileNameValue.setText("");
            fileNameLabel.setDisable(true);
            saveFileButton.setDisable(true);
            originalTimeLabel.setDisable(true);
            originalTimeValue.setText("");
            startTimeLabel.setDisable(true);
            restoreTime.setVisible(false);
            initDisableTimeInput();
        }
    }

    private void updateRestoreButton() {
        restoreTime.setVisible(true);
        boolean disableRestoreButton = subtitleManager.getOriginalStartTime().equals(subtitleManager.getStartTime());
        restoreTime.setDisable(disableRestoreButton);
    }

    private void initEnabledTimeInput() {
        Time startTime = subtitleManager.getStartTime();

        hourText.setDisable(false);
        hourText.setText(String.format("%02d", startTime.getHour()));

        minuteText.setDisable(false);
        minuteText.setText(String.format("%02d", startTime.getMinute()));

        secondText.setDisable(false);
        secondText.setText(String.format("%02d", startTime.getSecond()));

        millisecondText.setDisable(false);
        millisecondText.setText(String.format("%03d", startTime.getMillisecond()));

        Time originalTime = subtitleManager.getOriginalStartTime();
        originalTimeLabel.setDisable(false);
        originalTimeValue.setText(String.format(
                "%02dh %02dm %02ds %03dms",
                originalTime.getHour(),
                originalTime.getMinute(),
                originalTime.getSecond(),
                originalTime.getMillisecond()
        ));
    }

    private void initDisableTimeInput() {
        hourText.setDisable(true);
        hourText.setText("00");

        minuteText.setDisable(true);
        minuteText.setText("00");

        secondText.setDisable(true);
        secondText.setText("00");

        millisecondText.setDisable(true);
        millisecondText.setText("000");
    }

    private Time getTime() {
        int hour = Integer.parseInt(hourText.getText());
        int minute = Integer.parseInt(minuteText.getText());
        int second = Integer.parseInt(secondText.getText());
        int millisecond = Integer.parseInt(millisecondText.getText());

        return new Time(hour, minute, second, millisecond);
    }
}
