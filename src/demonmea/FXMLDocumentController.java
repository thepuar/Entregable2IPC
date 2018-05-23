/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demonmea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.sf.marineapi.nmea.event.AbstractSentenceListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.HDGSentence;
import net.sf.marineapi.nmea.sentence.MDASentence;
import net.sf.marineapi.nmea.sentence.MWVSentence;
import net.sf.marineapi.nmea.util.Position;

/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button button;
    @FXML
    private Label ficheroLabel;
    @FXML
    private Label hdgLabel;
    @FXML
    private Label hdgLabel1;
    @FXML
    private Label twdLabel;
    @FXML
    private Label twdLabel1;
    @FXML
    private Label twsLabel;
    @FXML
    private Label tempLabel;
    @FXML
    private Label awaLabel;
    @FXML
    private Label awaLabel1;
    @FXML
    private Label awsLabel;
    @FXML
    private Label pitchLabel;
    @FXML
    private Label rollLabel;
    @FXML
    private Label latLabel;
    @FXML
    private Label lonLabel;
    @FXML
    private Label cogLabel;
    @FXML
    private Label cogLabel1;
    @FXML
    private Label sogLabel;
    @FXML
    private LineChart<String, Number> lineChartTWD;
    @FXML
    private NumberAxis numberAxisTWD;
    @FXML
    private CategoryAxis categoryAxisTWD;
    @FXML
    private LineChart<String, Number> lineChartTWS;
    @FXML
    private NumberAxis numberAxisTWS;
    @FXML
    private CategoryAxis categoryAxisTWS;
    @FXML
    private PieChart chartROLL;
    @FXML
    private PieChart chartPITCH;
    @FXML
    private PieChart chartHDG;
    @FXML
    private PieChart chartTWD;
    @FXML
    private PieChart chartAWA;
    @FXML
    private PieChart chartCOG;

    private Model model;
    ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();
    XYChart.Series seriesTWD = new XYChart.Series();
    XYChart.Series seriesTWS = new XYChart.Series();
    Integer iTWD, iTWS;
    @FXML
    private Button btnSunMoon;
    @FXML
    private Slider sliderTWD;
    @FXML
    private Slider sliderTWS;

    private boolean boolday;
    @FXML
    private VBox vboxmain;

    int sizeTWS = 0;
    int sizeTWD = 0;
    int crecimiento = 60;
    List<Double> arrayTWS = new ArrayList<>();
    List<Double> arrayTWD = new ArrayList<>();
    @FXML
    private Label labelHDG;
    @FXML
    private Label labelTWS;
    @FXML
    private Label labelTWD;
    @FXML
    private Label labelTEMP;
    @FXML
    private Label labelAWA;
    @FXML
    private Label labelAWS;
    @FXML
    private Label labelLAT;
    @FXML
    private Label labelCOG;
    @FXML
    private Label labelLONG;
    @FXML
    private Label labelSOG;
    @FXML
    private Label labelModo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //<editor-fold defaultstate="collapsed" desc="Color ">
        ficheroLabel.getStyleClass().add("labelTitle");
        hdgLabel.getStyleClass().add("labelTitle");
        hdgLabel1.getStyleClass().add("labelTitle");
        twdLabel.getStyleClass().add("labelTitle");
        twdLabel1.getStyleClass().add("labelTitle");
        twsLabel.getStyleClass().add("labelTitle");
        tempLabel.getStyleClass().add("labelTitle");
        awaLabel.getStyleClass().add("labelTitle");
        awaLabel1.getStyleClass().add("labelTitle");
        awsLabel.getStyleClass().add("labelTitle");
        pitchLabel.getStyleClass().add("labelTitle");
        rollLabel.getStyleClass().add("labelTitle");
        latLabel.getStyleClass().add("labelTitle");
        lonLabel.getStyleClass().add("labelTitle");
        cogLabel.getStyleClass().add("labelTitle");
        cogLabel1.getStyleClass().add("labelTitle");
        sogLabel.getStyleClass().add("labelTitle");
        labelSOG.getStyleClass().add("labelTitle");
        labelHDG.getStyleClass().add("labelTitle");
        labelTWS.getStyleClass().add("labelTitle");
        labelTWD.getStyleClass().add("labelTitle");
        labelTEMP.getStyleClass().add("labelTitle");
        labelAWA.getStyleClass().add("labelTitle");
        labelAWS.getStyleClass().add("labelTitle");
        labelLAT.getStyleClass().add("labelTitle");
        labelCOG.getStyleClass().add("labelTitle");
        labelLONG.getStyleClass().add("labelTitle");
        labelModo.getStyleClass().add("labelTitle");
        

//</editor-fold>

        Double aux = Double.parseDouble(sliderTWS.getValue() + "");
        sizeTWS = aux.intValue() * crecimiento;
        aux = Double.parseDouble(sliderTWD.getValue() + "");
        sizeTWD = aux.intValue() * crecimiento;
        boolday = true;
        iTWD = 0;
        iTWS = 0;
        numberAxisTWD.setVisible(false);
        numberAxisTWS.setVisible(false);
        loadChartAngle(chartHDG, hdgLabel);
        loadChartAngle(chartTWD, twdLabel);
        loadChartAngle(chartAWA, awaLabel);
        loadChartAngle(chartCOG, cogLabel);
        loadChartAngle(chartROLL, rollLabel);
        loadChartAngle(chartPITCH, pitchLabel);
        Platform.runLater(() -> {
            lineChartTWD.getData().addAll(seriesTWD);
        });
        Platform.runLater(() -> {
            lineChartTWS.getData().addAll(seriesTWS);
        });

        model = Model.getInstance();

        // anadimos un listener para que cuando cambie el valor en el modelo 
        //se actualice su valor en su correspondiente representacion grafica
        model.HDGProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c) + "º";
            Platform.runLater(() -> {
                hdgLabel.setText(dat);
                hdgLabel1.setText(dat);
            });
        });

        model.TWDProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c) + "º";
            Platform.runLater(() -> {
                twdLabel.setText(dat);
                twdLabel1.setText(dat);
                arrayTWD.add(c.doubleValue());
                if (seriesTWD.getData().size() == sizeTWD) {
                    seriesTWD.getData().remove(0);
                }
                seriesTWD.getData().add(new XYChart.Data(iTWD + "", c));
                iTWD += 1;
            });
        });

        model.TWSProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c) + "Kn";
            Platform.runLater(() -> {
                twsLabel.setText(dat);
                arrayTWS.add(c.doubleValue());
                if (seriesTWS.getData().size() == sizeTWS) {
                    seriesTWS.getData().remove(0);
                }
                seriesTWS.getData().add(new XYChart.Data(iTWS + "", c));
                iTWS += 1;
            });
        });

        sliderTWS.valueProperty().addListener((arg, oldVal, newVal) -> {
            iTWS = 0;
            sizeTWS = newVal.intValue() * crecimiento;
            seriesTWS.getData().clear();
            int min = Math.min(sizeTWS, arrayTWS.size());
            for (int i = arrayTWS.size() - min; i < arrayTWS.size(); i++) {
                seriesTWS.getData().add(new XYChart.Data(iTWS++ + "", arrayTWS.get(i)));
            }
        });

        sliderTWD.valueProperty().addListener((arg, oldVal, newVal) -> {
            iTWD = 0;
            sizeTWD = newVal.intValue() * crecimiento;
            seriesTWD.getData().clear();
            int min = Math.min(sizeTWD, arrayTWD.size());
            for (int i = arrayTWD.size() - min; i < arrayTWD.size(); i++) {
                seriesTWD.getData().add(new XYChart.Data(iTWD++ + "", arrayTWD.get(i)));
            }
        });

        model.TEMPProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c) + "º";
            Platform.runLater(() -> {
                tempLabel.setText(dat);
            });
        });

        model.AWAProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c) + "º";
            Platform.runLater(() -> {
                awaLabel.setText(dat);
                awaLabel1.setText(dat);
            });
        });

        model.AWSProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c) + "Kn";
            Platform.runLater(() -> {
                awsLabel.setText(dat);
            });
        });

        model.PITCHProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c) + "º";
            Platform.runLater(() -> {
                pitchLabel.setText(dat);
            });
        });

        model.ROLLProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c) + "º";
            Platform.runLater(() -> {
                rollLabel.setText(dat);
            });
        });

        model.LATProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c);
            Platform.runLater(() -> {
                latLabel.setText(((String)String.format("%.5f", Double.parseDouble(dat)) + "º").replace(",", "."));
            });
        });

        model.LONProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c);
            Platform.runLater(() -> {
                lonLabel.setText(((String)String.format("%.5f", Double.parseDouble(dat)) + "º").replace(",", "."));
            });
        });

        model.COGProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c);
            Platform.runLater(() -> {
                String conversor = String.format("%.2f", Double.parseDouble(dat)) + "º";
                conversor = conversor.replace(",", ".");
                cogLabel.setText(conversor);
                cogLabel1.setText(conversor);
            });
        });

        model.SOGProperty().addListener((a, b, c) -> {
            String dat = String.valueOf(c);
            Platform.runLater(() -> {
                sogLabel.setText(((String)String.format("%.2f", Double.parseDouble(dat)) + "Kn").replace(",", "."));
            });
        });
    }

    public void loadChartAngle(PieChart chartPie, Label label) {
        chartPie.getData().add(new PieChart.Data("", 50));
        chartPie.getData().add(new PieChart.Data("", 50));
        chartPie.getData().get(0).getNode().setStyle("-fx-pie-color: aliceBlue;");
        chartPie.getData().get(1).getNode().setStyle("-fx-pie-color: cornflowerBlue;");
        chartPie.setStartAngle(180);
        Platform.runLater(() -> {
            chartPie.startAngleProperty().bind(Bindings.createDoubleBinding(() -> {
                double editValue;
                try {
                    String cadena = label.textProperty().getValue();
                    cadena = cadena.substring(0, cadena.length() - 1);
                    editValue = Double.valueOf(cadena);
                } catch (NumberFormatException e) {
                    editValue = 0;
                }
                return 180 - editValue;
            }, label.textProperty()));
            chartPie.legendVisibleProperty().setValue(Boolean.FALSE);
        });
    }

    @FXML
    private void cargarFichero() throws FileNotFoundException {
        FileChooser ficheroChooser = new FileChooser();
        ficheroChooser.setSelectedExtensionFilter(new ExtensionFilter("ficheros NMEA", "*.NMEA"));
        ficheroChooser.setTitle("fichero datos NMEA");

        File ficheroNMEA = ficheroChooser.showOpenDialog(ficheroLabel.getScene().getWindow());
        if (ficheroNMEA != null) {
            // NO se comprueba que se trata de un fichero de datos NMEA
            // esto es una demos
            ficheroLabel.setText("Fichero: " + ficheroNMEA.getName());

            model.addSentenceReader(ficheroNMEA);
        }
    }

    @FXML
    private void changeStyle(ActionEvent event) {

        if (boolday) {
            //Es de dia -> Cambiar a noche
            boolday = false;
            labelModo.setText("Modo Noche");
            try {
                vboxmain.getStylesheets().clear();
                vboxmain.getStylesheets().add("resources/noche.css");
                vboxmain.getStyleClass().add("main");
                for (String cadena : vboxmain.getStyleClass()) {
                    System.out.println("Este es el class: " + cadena);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            boolday = true;
            labelModo.setText("Modo Dia");
            try {
                vboxmain.getStylesheets().clear();
                vboxmain.getStylesheets().add("resources/style.css");
                vboxmain.getStyleClass().add("main");
                for (String cadena : vboxmain.getStyleClass()) {
                    System.out.println("Este es el class: " + cadena);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
