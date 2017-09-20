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
  return ThumbImage("1","1",file.toString())
}
