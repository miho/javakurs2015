/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.tutorial.jfxfractals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class MainFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        canvas = new FractCanvas();
        canvas.setWidth(100);
        canvas.setHeight(100);
        root.getChildren().add(canvas);
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        canvas.getaProperty().set(Double.parseDouble(aField.getText()));
        canvas.getbProperty().set(Double.parseDouble(bField.getText()));
        canvas.getcProperty().set(Double.parseDouble(cField.getText()));
        canvas.getIterationProperty().
                set(Long.parseLong(iterField.getText()));
        canvas.getScaleXProperty().set(Double.parseDouble(sxField.getText()));
        canvas.getScaleYProperty().set(Double.parseDouble(syField.getText()));

        aField.textProperty().addListener((ov) -> {
            canvas.getaProperty().set(Double.parseDouble(aField.getText()));
        });
        bField.textProperty().addListener((ov) -> {
            canvas.getbProperty().set(Double.parseDouble(bField.getText()));
        });
        cField.textProperty().addListener((ov) -> {
            canvas.getcProperty().set(Double.parseDouble(cField.getText()));
        });
        iterField.textProperty().addListener((ov) -> {
            canvas.getIterationProperty().
                    set(Long.parseLong(iterField.getText()));
        });
        sxField.textProperty().addListener((ov) -> {
            canvas.getScaleXProperty().set(Double.parseDouble(sxField.getText()));
        });
        syField.textProperty().addListener((ov) -> {
            canvas.getScaleYProperty().set(Double.parseDouble(syField.getText()));
        });

        if (aspectRatio.isSelected()) {
            sxField.textProperty().bindBidirectional(syField.textProperty());
        }

        aspectRatio.selectedProperty().addListener((ov) -> {
            if (aspectRatio.isSelected()) {
                sxField.textProperty().bindBidirectional(syField.textProperty());
            } else {
                sxField.textProperty().unbindBidirectional(syField.textProperty());
            }
        });

        txField.textProperty().addListener((ov) -> {
            canvas.getTranslateXProperty().set(Double.parseDouble(txField.getText()));
        });
        tyField.textProperty().addListener((ov) -> {
            canvas.getTranslateYProperty().set(Double.parseDouble(tyField.getText()));
        });

        canvas.getTranslateXProperty().addListener((ov) -> {
            txField.setText("" + canvas.getTranslateXProperty().get());
        });

        canvas.getTranslateYProperty().addListener((ov) -> {
            tyField.setText("" + canvas.getTranslateYProperty().get());
        });

        canvas.getScaleXProperty().addListener((ov) -> {
            sxField.setText("" + canvas.getScaleXProperty().get());
        });

        canvas.getScaleYProperty().addListener((ov) -> {
            syField.setText("" + canvas.getScaleYProperty().get());
        });

        canvas.getModuloProperty().bindBidirectional(moduloSlider.valueProperty());

        runBtn.setOnMouseClicked((evt) -> {
            canvas.redraw();
        });

        progressBar.progressProperty().bind(new DoubleBinding() {

            {
                super.bind(canvas.getCurrentIterationProperty());
            }

            @Override
            protected double computeValue() {

                double value = canvas.getCurrentIterationProperty().get()
                        / (double) (canvas.getIterationProperty().get());

                return value;
            }
        });

    }

    FractCanvas canvas;

    @FXML
    TitledPane mainPropertiesTab;

    @FXML
    AnchorPane root;
    @FXML
    Button runBtn;

    @FXML
    TextField aField;
    @FXML
    TextField bField;
    @FXML
    TextField cField;
    @FXML
    TextField iterField;
    @FXML
    TextField sxField;
    @FXML
    TextField syField;
    @FXML
    TextField txField;
    @FXML
    TextField tyField;

    @FXML
    CheckBox aspectRatio;

    @FXML
    Slider moduloSlider;

    @FXML
    ProgressBar progressBar;

    @FXML
    void onSaveImage(ActionEvent e) {
        File imgFile = selectSaveFile(null, "PNG Files (*.png)", "png", null);
        FractCanvas newCanvas = new FractCanvas(canvas);
        double factor = 2;
        newCanvas.getScaleXProperty().set(newCanvas.getScaleXProperty().get() * factor);
        newCanvas.getScaleYProperty().set(newCanvas.getScaleYProperty().get() * factor);
        newCanvas.setWidth(canvas.getWidth() * factor);
        newCanvas.setHeight(canvas.getHeight() * factor);
        newCanvas.draw(imgFile, (int) newCanvas.getWidth(), (int) canvas.getHeight());
    }

    public static File selectSaveFile(Window mainWindow, String description, String ending, File initDir) {
        FileChooser fileChooser = new FileChooser();
        if (initDir != null) {
            fileChooser.setInitialDirectory(initDir);
        }
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, "*." + ending);
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File file = fileChooser.showSaveDialog(mainWindow);

        if (!file.getName().toLowerCase().endsWith(ending)) {
            file = new File(file.getAbsolutePath() + "." + ending);
        }

        return file;
    }
}

class FractCanvas extends ResizableCanvas {

    private Thread t = null;
    private AnimationTimer timer;

    private final DoubleProperty aProperty = new SimpleDoubleProperty();
    private final DoubleProperty bProperty = new SimpleDoubleProperty();
    private final DoubleProperty cProperty = new SimpleDoubleProperty();

    private final DoubleProperty scaleXProperty = new SimpleDoubleProperty(1.0);
    private final DoubleProperty scaleYProperty = new SimpleDoubleProperty(1.0);

    private final DoubleProperty translateXProperty = new SimpleDoubleProperty(0.0);
    private final DoubleProperty translateYProperty = new SimpleDoubleProperty(0.0);

    private final LongProperty iterationProperty = new SimpleLongProperty();

    private final LongProperty currentIterationProperty = new SimpleLongProperty();

    private final IntegerProperty moduloProperty = new SimpleIntegerProperty(4);

    private double initialMouseX;
    private double initialMouseY;
    private double initialTx;
    private double initialTy;

    private final AtomicLong currentIteration = new AtomicLong();

    public FractCanvas() {
        aProperty.addListener(ov -> draw());
        bProperty.addListener(ov -> draw());
        cProperty.addListener(ov -> draw());
        scaleXProperty.addListener(ov -> draw());
        scaleYProperty.addListener(ov -> draw());
        translateXProperty.addListener(ov -> draw());
        translateYProperty.addListener(ov -> draw());
        iterationProperty.addListener(ov -> draw());

        moduloProperty.addListener((ov) -> draw());

        setOnMousePressed((evt) -> {

            initialTx = translateXProperty.get();
            initialTy = translateYProperty.get();

            initialMouseX = evt.getX();
            initialMouseY = evt.getY();

        });

        setOnMouseDragged((evt) -> {

            double dX = evt.getX() - initialMouseX;
            double dY = evt.getY() - initialMouseY;

            dX /= scaleXProperty.get();
            dY /= scaleYProperty.get();

            translateXProperty.setValue(dX + initialTx);
            translateYProperty.setValue(dY + initialTy);
        });

        setOnZoom((evt) -> {

            double ds = evt.getZoomFactor();

            scaleXProperty.setValue(scaleXProperty.get() * ds);
            scaleYProperty.setValue(scaleXProperty.get() * ds);

        });
    }

    public FractCanvas(FractCanvas other) {
        aProperty.set(other.aProperty.get());
        bProperty.set(other.bProperty.get());
        cProperty.set(other.cProperty.get());
        translateXProperty.set(other.translateXProperty.get());
        translateYProperty.set(other.translateYProperty.get());
        scaleXProperty.set(other.scaleXProperty.get());
        scaleYProperty.set(other.scaleYProperty.get());
        iterationProperty.set(other.iterationProperty.get());
        moduloProperty.set(other.moduloProperty.get());
    }

    public void redraw() {
        draw();
    }

    @Override
    protected void draw() {
        draw(null, 0, 0);
    }

    protected void draw(final File imgFile, final int w, final int h) {

        currentIterationProperty.set(0);

        if (t != null) {
            t.interrupt();
        }

        if (timer != null) {
            timer.stop();
        }

        if (getWidth() < 1 || getHeight() < 1) {
            return;
        }

        WritableImage img;

        if (imgFile != null) {
            img = new WritableImage(
                    (int) getWidth() * 2, (int) getHeight() * 2);
        } else {
            img = new WritableImage(
                    (int) (getWidth() * 2), (int) (getHeight()) * 2);
        }

        PixelWriter pw = img.getPixelWriter();
//        
//        
//        for (int imgY = 0; imgY < img.getHeight(); imgY++) {
//            for (int imgX = 0; imgX < img.getWidth(); imgX++) {
//                pw.setColor(imgX, imgY, Color.BLACK);
//            }
//        }

        GraphicsContext g2 = getGraphicsContext2D();

        g2.setFill(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                g2.drawImage(img, 0, 0, getWidth(), getHeight());
                currentIterationProperty.set(currentIteration.get());
            }
        };

        timer.start();

        Color[] colors = {
            Color.BLUE,
            Color.RED,
            Color.GREEN,
            Color.YELLOW,
            Color.WHITE,
            Color.ORANGE,
            Color.VIOLET,
            Color.CADETBLUE,
            Color.BISQUE,
            Color.YELLOWGREEN,
            Color.DARKTURQUOISE,
            Color.DARKRED,
            Color.DEEPPINK,
            Color.GOLDENROD
        };

        t = new Thread(() -> {

            Random r = new Random(1);
            double a = getaProperty().get(),
                    b = getbProperty().get(),
                    c = getcProperty().get(), x = 0, y = 0;

            for (long i = 0; i < getIterationProperty().get(); i++) {

                if (t.isInterrupted()) {
                    break;
                }

                currentIteration.set(i);

                double imgScaleX = img.getWidth() / getWidth();
                double imgScaleY = img.getHeight() / getHeight();

                double sx = scaleXProperty.get();
                double sy = scaleYProperty.get();

                double tx = translateXProperty.get();
                double ty = translateYProperty.get();

                final double finalX = tx * sx * imgScaleX
                        + x * sx + img.getWidth() * 0.5;
                final double finalY = ty * sy * imgScaleY
                        + y * sy + img.getHeight() * 0.5;
                final long finalI = i;

                if (finalX >= 0 && finalX < img.getWidth()
                        && finalY >= 0 && finalY < img.getHeight()) {

                    Color color;

                    if (moduloProperty.get() > colors.length
                            || moduloProperty.get() == 0) {
                        color = Color.WHITE;
                    } else {
                        color = colors[(int) (finalI % moduloProperty.get())];
                    }

                    pw.setColor(
                            (int) finalX, (int) finalY, color);
                }

                double xx = y - Math.signum(x) * Math.sqrt(Math.abs(b * x - c));
                double yy = a - x;
                x = xx * (1.0d + r.nextDouble() * 0.00000001d);
                y = yy * (1.0d + r.nextDouble() * 0.00000001d);
            } // end for

            if (imgFile != null) {

                Platform.runLater(() -> {

                    WritableImage wImg = snapshot(null, null);
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(wImg, null),
                                "png", imgFile);
                    } catch (IOException ex) {
                        Logger.getLogger(MainFXMLController.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }
                });

            }
        });

        t.start();
    }

    /**
     * @return the aProperty
     */
    public DoubleProperty getaProperty() {
        return aProperty;
    }

    /**
     * @return the bProperty
     */
    public DoubleProperty getbProperty() {
        return bProperty;
    }

    /**
     * @return the cProperty
     */
    public DoubleProperty getcProperty() {
        return cProperty;
    }

    /**
     * @return the iterationProperty
     */
    public LongProperty getIterationProperty() {
        return iterationProperty;
    }

    /**
     * @return the scaleXProperty
     */
    public DoubleProperty getScaleXProperty() {
        return scaleXProperty;
    }

    /**
     * @return the scaleYProperty
     */
    public DoubleProperty getScaleYProperty() {
        return scaleYProperty;
    }

    /**
     * @return the translateXProperty
     */
    public DoubleProperty getTranslateXProperty() {
        return translateXProperty;
    }

    /**
     * @return the translateYProperty
     */
    public DoubleProperty getTranslateYProperty() {
        return translateYProperty;
    }

    /**
     * @return the moduloProperty
     */
    public IntegerProperty getModuloProperty() {
        return moduloProperty;
    }

    /**
     * @return the currentIterationProperty
     */
    public LongProperty getCurrentIterationProperty() {
        return currentIterationProperty;
    }

}

class ResizableCanvas extends Canvas {

    public ResizableCanvas() {
        widthProperty().addListener(ov -> draw());
        heightProperty().addListener(ov -> draw());
    }

    protected void draw() {

        double width = getWidth();
        double height = getHeight();

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        gc.setStroke(Color.RED);
        gc.strokeLine(0, 0, width, height);
        gc.strokeLine(0, height, width, 0);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}
