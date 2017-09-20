package moynes.glob

import java.io.File
import java.nio.file.PathMatcher
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths

public data class ThumbImage(val start: String, val duration: String, val period: String, val name: String);

fun glob(args: Array<String>): List<ThumbImage> {
  var images = ArrayList<ThumbImage>()
  if (args.isEmpty()){
    return images
  }
  for (arg in args){

    val file = File(arg)

    if (file.exists()){
      images.add(body(file))
    } else {
      val ds = Files.newDirectoryStream(file.getParentFile().toPath(), file.getName())


      for(path in ds){
        //body(file)
        images.add(body(path.toFile()))
        //images.add(body())
      }
    }
    return images
  }
  return images
}

fun body(file: File): ThumbImage{
  var searchString = "start_at_"
  //get the HHMM timestamp from the filename into separate elements
  val hhmmIndex = file.toString().indexOf(searchString)+searchString.length
  val hh = file.toString().substring(hhmmIndex, hhmmIndex+2)
  var mm = file.toString().substring(hhmmIndex+2, hhmmIndex+2+2)
  //get the number of the thumbnail to find the actual start time
  searchString = "_min_"
  val thumbIndex = file.toString().indexOf(searchString)+searchString.length
  val thumbVal = file.toString().substring(thumbIndex, thumbIndex+4)
  mm = String.format("%02d",(mm.toInt()+thumbVal.toInt()-1))
  return ThumbImage("$hh:$mm:00.000","00:01:00.000","00:01:00.000",file.toString())
}
