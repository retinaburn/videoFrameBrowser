package moynes.JFX

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue

//images
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//event
import javafx.scene.input.MouseEvent

import moynes.glob.*
import moynes.imggen.createAndExecute


fun main(args: Array<String>){
  Application.launch(JFX::class.java, *args)
}

val images = listOf(ThumbImage("00:00:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0000.png"),
  ThumbImage("00:10:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0001.png"),
  ThumbImage("00:20:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0002.png"),
  ThumbImage("00:30:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0003.png"),
  ThumbImage("00:40:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0004.png"),
  ThumbImage("00:50:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0005.png"),
  ThumbImage("01:00:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0006.png"))

val SCREEN_WIDTH = 1200.0
val SCREEN_HEIGHT = 600.0

var grid = GridPane()

public class JFX : Application() {
  var sp = ScrollPane()
  var vb = VBox();

  var counter = 0

  override fun start(stage: Stage) {
    var box = HBox()
    stage.setTitle("Hello World")

    box.setPrefSize(100.0, 100.0)
    vb.setPrefSize(100.0, 100.0)

    grid.setHgap(0.0)
    grid.setVgap(0.0)

    var rowIndex = 0
    for(image in images){
      vb.getChildren().add(getImage(image))
      rowIndex++
    }

    box.getChildren().add(sp)
    VBox.setVgrow(sp, Priority.ALWAYS);
    stage.setScene(Scene(box, SCREEN_WIDTH, SCREEN_HEIGHT, Color.BLACK))

    val listener = object : ChangeListener<Number> {
      override fun changed(ob: ObservableValue<out Number>,
        old_val: Number,
        new_val: Number){

        }
    }

    sp.setPrefSize(300.0, 300.0);
    sp.setContent(vb);
    sp.vvalueProperty().addListener( listener );
    stage.show()
  }
}



fun getImage(image: ThumbImage): ImageView{
  val image1 = Image("file:${image.name}")
  val iv1 = ImageView()
  iv1.setImage(image1)
  iv1.setFitWidth(300.0)
  iv1.setPreserveRatio(true)
  iv1.setSmooth(true)
  iv1.setCache(true)

  iv1.addEventHandler(MouseEvent.MOUSE_CLICKED, MouseEventHandler(image));

  return iv1
}

var childImages = ArrayList<ImageView>()
class MouseEventHandler(val image: ThumbImage) : EventHandler<MouseEvent> {

  override fun handle(event: MouseEvent){
    event.consume()
    println("CLICKED ${image.name}")
    println(image)

    for(existingImage in childImages){
      grid.getChildren().remove(existingImage)
    }
    childImages.clear()

    val duration = when(image.duration){
      "VIDEO_LENGTH"  -> "00:10:00"
      "00:10:00.000"  -> "00:01:00"
      "00:01:00.000"  -> "00:00:01"
      else -> "00:00:01"
    }

    val period = when(duration) {
      "00:10:00"  -> "00:01:00"
      "00:01:00"  -> "00:00:01"
      else -> "UHOH"
    }

    //get strings for timestamps in generated filename
    val genStartTime = image.start.substring(0,8).replace(":","")
    val genDuration = duration.substring(0,8).replace(":","")
    val genPeriod = period.substring(0,8).replace(":","")

    //see if there are matching images already
    val imgarg = arrayOf("./thumbs/start_${genStartTime}_for_${genDuration}_by_${genPeriod}_*.png")
    print("Finding images for pattern: ${imgarg[0]}...")
    var newImages = glob(imgarg)
    if (newImages.isEmpty()){
      println(".not found. Generating.")
      createAndExecute(startTime=image.start,
        duration=duration,
        period=period,
        outputPattern="thumbs/start_${genStartTime}_for_${genDuration}_by_${genPeriod}_%04d.png")

      print("Finding generated images for pattern: ${imgarg[0]}...")
      newImages = glob(imgarg)
    }

    if(!newImages.isEmpty()){
      println(".found ${newImages}")
      val colStartIndex=2
      var rowIndex = 0
      var colIndex = 0
      for (image in newImages){
        val iv = getImage(image)
        childImages.add(iv)
        grid.add(iv, colIndex+colStartIndex, rowIndex)
        rowIndex++
        rowIndex=rowIndex % 4
        if (rowIndex == 0){
          colIndex++
          colIndex=colIndex % 4
        }
      }
    }
  }
}

/*
public class LineGrid : Pane{
  val canvas = Canvas()
  val dashLength = 5.0
  val horizontalX = 3.0

  constructor() {
    getChildren().add(canvas)
  }

  override protected fun layoutChildren(){
    val top = snappedTopInset().toInt()
    val right = snappedRightInset().toInt()
    val bottom = snappedBottomInset().toInt()
    val left = snappedLeftInset().toInt()
    val w = getWidth() - left - right
    val h = getHeight() - top - bottom

    canvas.setLayoutX(left.toDouble())
    canvas.setLayoutY(top.toDouble())

    if (w != canvas.getWidth() || h != canvas.getHeight()){
      canvas.setWidth(w)
      canvas.setHeight(h)
      val g = canvas.getGraphicsContext2D()
      g.clearRect(0.toDouble(), 0.toDouble(), w, h)

      //vertical lines
      g.setStroke(Color.DIMGRAY)
      g.setLineDashes(dashLength)
      for(i in 0..getWidth().toInt() step 30){
        g.moveTo(i.toDouble(), 0.toDouble())
        g.lineTo(i.toDouble(), getHeight() - (getHeight()%30))
        g.stroke()
      }

      //horizontal lines
      g.setLineDashes(dashLength)
      for(i in 0..getHeight().toInt() step 30){
        g.moveTo(horizontalX, i.toDouble())
        g.lineTo(getWidth() - (getWidth()%30), i.toDouble())
        g.stroke()
      }

    }
  }
}
*/
