package moynes.imggen
import java.lang.ProcessBuilder
import java.lang.ProcessBuilder.Redirect
import java.lang.Process
import java.io.File

/*
c:\ffmpeg\bin\ffmpeg -i
"KT-Alerts.mov"
-ss 00:10:01.000 -vframes 1 out.png

c:\ffmpeg\bin\ffmpeg -i
"KT-Alerts.mov"
-vf fps=1/600 thumb%04d.png

*/
fun main(args: Array<String>){

  /*val processArgs = listOf("c:/ffmpeg/bin/ffmpeg",
  "-y",
  "-i",
  "\"KT-Alerts.mov\"",
  "-vf",
  "fps=1/600",
  "thumb%04d.png")*/

  //00:00:00.000 BLACK
  //00:10:00.000 1:18pm
  /*
  // 1/600 is every 10 minutes
  // ffmpeg will take the middle frame between the start time, and the interval time
  // so start time of 00:05:00 with end time of 00:15:00, image 0001 will be at 00:10:00
  */
  var processArgs = listOf("c:/ffmpeg/bin/ffmpeg",
  "-y",
  "-ss",
  "00:05:00.000",
  "-i",
  "\"KT-Alerts.mov\"",
  "-vf",
  "fps=1/600",
  "thumbs/thumb%04d.png")
  createAndExecute(processArgs)

  //grab the single frame at 00:00:00
  processArgs = listOf("c:/ffmpeg/bin/ffmpeg",
  "-y",
  "-ss",
  "00:00:00.000",
  "-i",
  "\"KT-Alerts.mov\"",
  "-vframes",
  "1",
  "thumbs/thumb0000.png")
  createAndExecute(processArgs)

  //start at 10 minutes, and every minute (1/60) for 10 minutes
  processArgs = listOf("c:/ffmpeg/bin/ffmpeg.exe",
  "-y",
  "-ss",
  "00:10:00.000",
  "-t",
  "00:10:00.000",
  "-i",
  "\"KT-Alerts.mov\"",
  "-vf",
  "fps=\"1/60\"",
  "\"thumbs/start_at_10_for_10_by_1_min_%04d.png\"")
  createAndExecute(processArgs)

  //start at 1 hour, and every minute (1/60) for 10 minutes
  processArgs = listOf("c:/ffmpeg/bin/ffmpeg.exe",
  "-y",
  "-ss",
  "01:00:00.000",
  "-t",
  "00:10:00.000",
  "-i",
  "\"KT-Alerts.mov\"",
  "-vf",
  "fps=\"1/60\"",
  "\"thumbs/start_at_60_for_10_by_1_min_%04d.png\"")

  /*val processArgs = listOf("c:/ffmpeg/bin/ffmpeg.exe",
  "-y",
  "-ss",
  "00:10:00.000",
  "-t",
  "00:10:00.000",
  "-i",
  "\"KT-Alerts.mov\"",
  "-vf",
  "fps=\"1/60\"",
  "\"start_at_10_for_10_by_1_min_%04d.png\"")*/
  createAndExecute(processArgs)
}
fun createAndExecute(processArgs: List<String>){

  val pb = ProcessBuilder(processArgs)
  pb.redirectOutput(Redirect.appendTo(File("output")))
  pb.redirectError(Redirect.appendTo(File("error")))
  println(pb.command())

  //TODO - wait for process to be complete
  val p = pb.start()
  //p.waitFor() - not returning
  while(p.isAlive()){
    println("isAlive: ${p.isAlive()} ")
    Thread.sleep(1000)
  }
}

fun createAndExecute(startTime: String, duration: String, period: String, outputPattern: String){
  val fps = if (period == "00:01:00"){
    "1/60"
  } else {
    "1/60"
  }

  val processArgs = listOf("c:/ffmpeg/bin/ffmpeg.exe",
    "-y", //overwrite existing files
    "-ss",
    startTime,
    "-t",
    duration,
    "-i",
    "\"KT-Alerts.mov\"",
    "-vf",
    "fps=\"$fps\"",
    "\"$outputPattern\"")
    createAndExecute(processArgs)
}
