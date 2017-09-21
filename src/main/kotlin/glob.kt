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
  var searchString = "start_"
  var searchStringEnd = "_"
  val startTimestamp = getTimestamp(file.toString(), searchString, searchStringEnd)
  //get the HHMM timestamp from the filename into separate elements
  var (hh,mm,ss) = getTimestampComponents(startTimestamp)


  //get the number of the thumbnail to find the actual start time
  searchString = "_"
  val thumbIndex = file.toString().lastIndexOf(searchString)+1
  val thumbVal = file.toString().substring(thumbIndex, thumbIndex+4)

  //get the duration
  searchString = "_for_"
  searchStringEnd = "_"
  val durationTimestamp = getTimestamp(file.toString(), searchString, searchStringEnd)
  val (durationHH, durationMM, durationSS) = getTimestampComponents(durationTimestamp)

  //get the period
  searchString = "_by_"
  searchStringEnd = "_"
  val periodTimestamp = getTimestamp(file.toString(), searchString, searchStringEnd)
  val (periodHH,periodMM,periodSS) = getTimestampComponents(periodTimestamp)

  //if the period is by minute, then the thumb count ()"0001.png") is a minute
  //and we add it to the start time minutes to get it accurate
  //if the period is by second, then the thumb count is a second
  //and we add it to the start time seconds to get it accurate
  if(periodTimestamp == "000100"){
    mm = String.format("%02d",(mm.toInt()+thumbVal.toInt()-1))
  } else if (periodTimestamp == "000001"){
    ss = String.format("%02d",(ss.toInt()+thumbVal.toInt()-1))
  }

  return ThumbImage("$hh:$mm:$ss.000",
    "$durationHH:$durationMM:$durationSS.000",
    "$periodHH:$periodMM:$periodSS.000",file.toString())
}

fun getTimestamp(key: String, searchStringStart: String, searchStringEnd: String): String{
  val searchStringStartIndex = key.indexOf(searchStringStart)+searchStringStart.length
  val searchStringEndIndex = key.indexOf(searchStringEnd, searchStringStartIndex)
  return key.substring(searchStringStartIndex, searchStringEndIndex)
}

fun getTimestampComponents(key: String): Triple<String, String, String>{
  val hh = key.substring(0,2)
  val mm = key.substring(2,4)
  val ss = key.substring(4,6)
  return Triple(hh, mm, ss)
}
