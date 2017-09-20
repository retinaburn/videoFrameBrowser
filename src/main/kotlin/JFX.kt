package moynes.JFX

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;

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
//  ThumbImage("00:20:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0002.png"),
//  ThumbImage("00:30:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0003.png"),
//  ThumbImage("00:40:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0004.png"),
//  ThumbImage("00:50:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0005.png"),
  ThumbImage("01:00:00.000", "VIDEO_LENGTH", "00:10:00", "thumbs/thumb0006.png"))

val SCREEN_WIDTH = 1200.0
val SCREEN_HEIGHT = 600.0

var grid = GridPane()

public class JFX : Application() {
  var counter = 0

  override fun start(stage: Stage) {
    stage.setTitle("Hello World")

    var root = StackPane()

    grid.setHgap(0.0)
    grid.setVgap(0.0)

    var rowIndex = 0
    for(image in images){
      grid.add(getImage(image), 1, rowIndex)
      rowIndex++
    }

    root.getChildren().add(grid)
    stage.setScene(Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT, Color.BLACK))

    /*val lineGrid = LineGrid()
    lineGrid.getChildren().add(getImage("thumb0001.png"))
    lineGrid.getChildren().add(getImage("thumb0002.png"))
    lineGrid.getChildren().add(getImage("thumb0003.png"))

    stage.setScene(Scene(lineGrid, SCREEN_WIDTH, SCREEN_HEIGHT, Color.BLACK))*/
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

    val hhmm = image.start.substring(0,2)+image.start.substring(3,5)

    val duration = if (image.duration == "VIDEO_LENGTH") {
      "00:10:00"
    } else if (image.duration == "00:10:00"){
      "00:01:00"
    } else {
      "00:00:01"
    }

    val period = if (duration == "00:10:00"){
      "00:01:00"
    } else if ("00:01:00") {
      "00:00:01"
    }

    //see if there are matching images already
    val imgarg = arrayOf("./thumbs/start_at_${hhmm}_for_10_by_1_min*.png")
    println("Finding images for pattern: ${imgarg[0]}")
    var newImages = glob(imgarg)
    if (newImages.isEmpty()){
      createAndExecute(startTime=image.start, duration=duration, period=period, outputPattern="thumbs/start_at_${hhmm}_for_10_by_1_min_%04d.png")

      println("Finding images for pattern: ${imgarg[0]}")
      newImages = glob(imgarg)
    }

    if(!newImages.isEmpty()){
      println("Found ${newImages}")
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
