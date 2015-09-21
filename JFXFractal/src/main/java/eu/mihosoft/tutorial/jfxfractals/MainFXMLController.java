/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.tutorial.jfxfractals;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

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

        runBtn.setOnMouseClicked((evt) -> {
            canvas.redraw();
        });

    }

    FractCanvas canvas;

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

    private double initialMouseX;
    private double initialMouseY;
    private double initialTx;
    private double initialTy;

    public FractCanvas() {
        aProperty.addListener(ov -> draw());
        bProperty.addListener(ov -> draw());
        cProperty.addListener(ov -> draw());
        scaleXProperty.addListener(ov -> draw());
        scaleYProperty.addListener(ov -> draw());
        translateXProperty.addListener(ov -> draw());
        translateYProperty.addListener(ov -> draw());
        iterationProperty.addListener(ov -> draw());

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

    public void redraw() {
        draw();
    }

    @Override
    protected void draw() {

        if (t != null) {
            t.interrupt();
        }

        if (timer != null) {
            timer.stop();
        }

        if (getWidth() < 1 || getHeight() < 1) {
            return;
        }

        WritableImage img = new WritableImage((int) (getWidth() * 2), (int) (getHeight()) * 2);

        GraphicsContext g2 = getGraphicsContext2D();

        g2.setFill(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                g2.drawImage(img, 0, 0, getWidth(), getHeight());
            }
        };
        timer.start();

        Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};

        t = new Thread(() -> {

            Random r = new Random(1);
            double a = getaProperty().get(),
                    b = getbProperty().get(),
                    c = getcProperty().get(), x = 0, y = 0;
            for (long i = 0; i < getIterationProperty().get(); i++) {

                if (t.isInterrupted()) {
                    break;
                }

                double imgScaleX = img.getWidth() / getWidth();
                double imgScaleY = img.getHeight() / getHeight();

                double sx = scaleXProperty.get();
                double sy = scaleYProperty.get();

                double tx = translateXProperty.get();
                double ty = translateYProperty.get();

                final double finalX = tx * sx * imgScaleX + x * sx + img.getWidth() * 0.5;
                final double finalY = ty * sy * imgScaleY + y * sy + img.getHeight() * 0.5;
                final long finalI = i;

                if (finalX >= 0 && finalX < img.getWidth() && finalY >= 0 && finalY < img.getHeight()) {
                    Color color = colors[(int) (finalI % colors.length)];

//                    Color color = new Color(
//                        0.3 * (i % 255) / 255.0,
//                        0.8*(i % 255) / 255.0,
//                        0.3 * (i % 255) / 255.0, 1.0);
                    img.getPixelWriter().setColor((int) finalX, (int) finalY, color);
                }

//                g2.setFill(colors[finalI % colors.length]);
//
////                g2.setFill(new Color(
////                        1.0 * (i % 255) / 255.0,
////                        (i % 255) / 255.0,
////                        1.0 * (i % 255) / 255.0, 1.0));
//                g2.fillRect(
//                        finalX * 10 + getWidth() * 0.5,
//                        finalY * 10 + getHeight() * 0.5, 0.25, 0.25);
                double xx = y - Math.signum(x) * Math.sqrt(Math.abs(b * x - c));
                double yy = a - x;
                x = xx * (1.0d + r.nextDouble() * 0.00000001d);
                y = yy * (1.0d + r.nextDouble() * 0.00000001d);
            }
        }
        );

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
