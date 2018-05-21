/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demonmea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import net.sf.marineapi.nmea.event.AbstractSentenceListener;
import net.sf.marineapi.nmea.io.ExceptionListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.HDGSentence;
import net.sf.marineapi.nmea.sentence.MDASentence;
import net.sf.marineapi.nmea.sentence.MWVSentence;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.XDRSentence;
import net.sf.marineapi.nmea.util.Measurement;

/**
 *
 * @author jsoler
 */
public class Model {

    //implementa el patron singleton
    // esto asegura que solamente se va a crear una instancia de la clase model
    // y se podra acceder a ella desde cualquier clase del proyecto
    private static Model model;

    private Model() {}
    public static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }
    
    //===================================================================
    // CUIDADO, el objeto de la clase SentenceReader se ejecuta en un hilo
    // no se pueden modificar las propiedades de los objetos graficos desde
    // un metodo ejecutado en este hilo
    private SentenceReader reader;

    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty TWD = new SimpleDoubleProperty();
    public DoubleProperty TWDProperty() {
        return TWD;
    }
    
    // True Wind Speed -- intensidad de viento
    private final DoubleProperty TWS = new SimpleDoubleProperty();
    public DoubleProperty TWSProperty() {
        return TWS;
    }
    
    //Heading - compas magnetic
    private final DoubleProperty HDG = new SimpleDoubleProperty();
    public DoubleProperty HDGProperty() {
        return HDG;
    }
        
    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty TEMP = new SimpleDoubleProperty();
    public DoubleProperty TEMPProperty() {
        return TEMP;
    }
    
    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty AWA = new SimpleDoubleProperty();
    public DoubleProperty AWAProperty() {
        return AWA;
    }
        
    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty AWS = new SimpleDoubleProperty();
    public DoubleProperty AWSProperty() {
        return AWS;
    }
    
    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty PITCH = new SimpleDoubleProperty();
    public DoubleProperty PITCHProperty() {
        return PITCH;
    }
    
    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty ROLL = new SimpleDoubleProperty();
    public DoubleProperty ROLLProperty() {
        return ROLL;
    }
    
    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty LAT = new SimpleDoubleProperty();
    public DoubleProperty LATProperty() {
        return LAT;
    }
    
    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty LON = new SimpleDoubleProperty();
    public DoubleProperty LONProperty() {
        return LON;
    }
    
    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty COG = new SimpleDoubleProperty();
    public DoubleProperty COGProperty() {
        return COG;
    }
    
    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty SOG = new SimpleDoubleProperty();
    public DoubleProperty SOGProperty() {
        return SOG;
    }
    
    //====================================================================
    //anadir tantos sentenceListener como tipos de sentence queremos tratar
    class HDGSentenceListener extends AbstractSentenceListener<HDGSentence> {
    @Override
        public void sentenceRead(HDGSentence sentence) {
            HDG.set(sentence.getHeading());
        }
    };

    class MDASentenceListener extends AbstractSentenceListener<MDASentence> {
        @Override
        public void sentenceRead(MDASentence sentence) {
            TWD.set(sentence.getTrueWindDirection());
            TWS.set(sentence.getWindSpeedKnots());
            TEMP.set(sentence.getAirTemperature());
        }
    }
    
    class MWVSentenceListener extends AbstractSentenceListener<MWVSentence> {
        @Override
        public void sentenceRead(MWVSentence sentence) {
            AWA.set(sentence.getAngle());
            AWS.set(sentence.getSpeed());
        }
    }
    
    class XDRSentenceListener extends AbstractSentenceListener<XDRSentence> {
        @Override
        public void sentenceRead(XDRSentence sentence) {
            // anadimos el codigo necesario para guardar la información de la sentence 
            List<Measurement> measurements=sentence.getMeasurements();
            measurements.forEach((mm) -> {
                if (mm.getName().equals("PTCH")) PITCH.set(mm.getValue());
                else if (mm.getName().equals("ROLL")) ROLL.set(mm.getValue());
            });
        }
    }
    
    class RMCSentenceListener extends AbstractSentenceListener<RMCSentence> {
        @Override
        public void sentenceRead(RMCSentence sentence) {
            LAT.set(sentence.getPosition().getLatitude());
            LON.set(sentence.getPosition().getLongitude());
            COG.set(sentence.getCourse());
            SOG.set(sentence.getSpeed());
        }
    }
    
    //========================================================================================
    // anade todas las clases de que extiendan AbstractSentenceListener que necesites
    public void addSentenceReader(File file) throws FileNotFoundException {

        InputStream stream = new FileInputStream(file);
        if (reader != null) {  // esto ocurre si ya estamos leyendo un fichero
            reader.stop();
        }
        reader = new SentenceReader(stream);
     
        reader.setExceptionListener(e->{System.out.println(e.getMessage());});
        //creamos los SentenceListener para cada tipo de trama y los registramos
        HDGSentenceListener hdg = new HDGSentenceListener();
        reader.addSentenceListener(hdg);

        MDASentenceListener mda = new MDASentenceListener();
        reader.addSentenceListener(mda);
        
        MWVSentenceListener mwv = new MWVSentenceListener();
        reader.addSentenceListener(mwv);
        
        XDRSentenceListener xdr = new XDRSentenceListener();
        reader.addSentenceListener(xdr);
        
        RMCSentenceListener rmc = new RMCSentenceListener();
        reader.addSentenceListener(rmc);
        //===============================================================
        //anadimos un exceptionListener para que capture las tramas que no tienen parser
         reader.setExceptionListener(e->{System.out.println(e.getMessage());});
        //==================================================================
        //============= registra todos los sentenceListener que necesites
    
        // arrancamos el SentenceReader para que empieze a escuchar
        reader.start();
    }
}
