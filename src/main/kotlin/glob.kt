package moynes.glob

import java.io.File
import java.nio.file.PathMatcher
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths

public data class ThumbImage(val time: String, val interval: String, val name: String);

fun glob(args: Array<String>): List<ThumbImage>? {
  if (args.isEmpty()){
    return null
  }
  for (arg in args){
    var images = ArrayList<ThumbImage>()
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
  return null
}

fun body(file: File): ThumbImage{
  val searchString = "start_at_"
  //get the HHMM timestamp from the filename into separate elements
  val hhmmIndex = file.toString().indexOf(searchString)+searchString.length
  val hh = file.toString().substring(hhmmIndex, hhmmIndex+2)
  val mm = file.toString().substring(hhmmIndex+2, hhmmIndex+2+2)
  return ThumbImage("$hh:$mm:00.000","00:01:00.000",file.toString())
}
