package watermark

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.system.exitProcess

class WatermarkedImage {
    lateinit var mainImageName: BufferedImage
    lateinit var waterImageName: BufferedImage
    lateinit var transColors: List<String>
    var waterTransPercentage = 0
    lateinit var singleParams: List<String>
    var finalImageName = ""
}

fun combineImages(transparency: Int, watermarkImage: Color, mainImage: Color): Color {
    return Color(
        (transparency * watermarkImage.red + (100 - transparency) * mainImage.red) / 100,
        (transparency * watermarkImage.green + (100 - transparency) * mainImage.green) / 100,
        (transparency * watermarkImage.blue + (100 - transparency) * mainImage.blue) / 100
    )
}
fun checkSize(firstFileName: BufferedImage, secondFileName: BufferedImage) {
    if(firstFileName.height < secondFileName.height || firstFileName.width < secondFileName.width) {
        println("The watermark's dimensions are larger.")
        exitProcess(0)
    }
}
fun readFileType (finalImageName: String): String {
    val fileType = if (finalImageName.contains("jpg")) {
        "jpg"
    } else if (finalImageName.contains("png")) {
        "png"
    } else {
        println("The output file extension isn't \"jpg\" or \"png\".")
        exitProcess(0)
    }
    return fileType
}
fun alphaChannelUse(firstFileName: BufferedImage, watermark: BufferedImage) {
    if (watermark.transparency == 3) {
        println("Do you want to use the watermark's Alpha channel?")
        if (readln() == "yes") {
            val s = WatermarkedImage()
            s.mainImageName = firstFileName
            s.waterImageName = watermark
            s.waterTransPercentage = getWaterTransparency()
            when (getPosMethod()) {
                "single" -> {
                    s.singleParams = getSinglePos(s.mainImageName, s.waterImageName)
                    s.finalImageName = getFinalImageName()
                    createAlphaSingleWatermark(s)
                }
                "grid" -> {
                    s.finalImageName = getFinalImageName()
                    createAlphaGridWatermark(s)
                }
                else -> {
                    println("wrong Input")
                    exitProcess(0)
                }
            }
        }
    }
}
fun getPosMethod(): String {
    println("Choose the position method (single, grid):")
    val method = readln()
    if (method != "single" && method != "grid") {
        println("The position method input is invalid.")
        exitProcess(0)
    }
    return method
}
fun getSinglePos(image: BufferedImage, watermark: BufferedImage): List<String> {
    val diffX = image.width - watermark.width
    val diffY = image.height - watermark.height
    println("Input the watermark position ([x 0-$diffX] [y 0-$diffY]):")
    val input = readln().split(" ")
    if (input.size != 2) {
        println("The position input is invalid.")
        exitProcess(0)
    } else {
        try {
            val posA = input[0].toInt()
            val posB = input[1].toInt()
            if (posA !in 0..diffX || posB !in 0..diffY) {
                println("The position input is out of range.")
                exitProcess(0)
            }
        } catch (e: NumberFormatException) {
            println("The position input is invalid.")
            exitProcess(0)
        }
    }
    return input
}
fun getMainImage(): BufferedImage {
    println("Input the image filename:")
    val fileName = readln()
    try {
        val fileImage: BufferedImage = ImageIO.read(File(fileName))
        if (fileImage.colorModel.numColorComponents != 3) {
            println("The number of image color components isn't 3.")
            exitProcess(0)
        }
        if (fileImage.colorModel.pixelSize != 24 && fileImage.colorModel.pixelSize != 32) {
            println("The image isn't 24 or 32-bit.")
            exitProcess(0)
        }
        return fileImage
    } catch (e: IllegalArgumentException) {
        println("Null Input detected.")
        exitProcess(0)
    } catch (i: IOException) {
        println("The file $fileName doesn't exist.")
        exitProcess(0)
    }
}
fun getWaterImage(): BufferedImage {
    println("Input the watermark image filename:")
    val fileName = readln()
    try {
        val fileImage: BufferedImage = ImageIO.read(File(fileName))
        if (fileImage.colorModel.numColorComponents != 3) {
            println("The number of Watermark color components isn't 3.")
            exitProcess(0)
        }
        if (fileImage.colorModel.pixelSize != 24 && fileImage.colorModel.pixelSize != 32) {
            println("The Watermark isn't 24 or 32-bit.")
            exitProcess(0)
        }
        return fileImage
    } catch (e: IllegalArgumentException) {
        println("Null Input detected.")
        exitProcess(0)
    } catch (i: IOException) {
        println("The file $fileName doesn't exist.")
        exitProcess(0)
    }
}
fun getWaterTransparency(): Int {
    println("Input the watermark transparency percentage (Integer 0-100):")
    val waterTransparency = readln().toInt()
    try {
        if (waterTransparency !in 1..99) {
            println("The transparency percentage is out of range.")
            exitProcess(0)
        }
        return waterTransparency
    } catch (e: NumberFormatException) {
        println("The transparency percentage isn't an integer number.")
        exitProcess(0)
    }
}
fun getTransColor(): List<String> {
    val invalidText = "The transparency color input is invalid."
    println("Input a transparency color ([Red] [Green] [Blue]):")
    val input = readln().split(" ")
    try {
        if (input.size != 3) {
            println(invalidText)
            exitProcess(0)
        }
        for (i in 0..2) {
            if (input[i].toInt() !in 0..255) {
                println(invalidText)
                exitProcess(0)
            }
        }
    } catch (e: Exception) {
        println(invalidText)
        exitProcess(0)
    }
    return input
}
fun getFinalImageName(): String {
    println("Input the output image filename (jpg or png extension):")
    return readln()
}
fun createSingleWatermark(wi: WatermarkedImage) {
    val outputImage = buildOutputImage(wi.mainImageName.width, wi.mainImageName.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until wi.mainImageName.width) {
        for (y in 0 until wi.mainImageName.height) {
            val imagePixel = Color(wi.mainImageName.getRGB(x, y))
            val color = Color(imagePixel.red, imagePixel.green, imagePixel.blue)
            outputImage.setRGB(x, y, color.rgb)
        }
    }
    for (x in wi.singleParams[0].toInt() until wi.waterImageName.width + wi.singleParams[0].toInt()) {
        for (y in wi.singleParams[1].toInt() until wi.waterImageName.height + wi.singleParams[1].toInt()) {
            val imagePixel = Color(wi.mainImageName.getRGB(x, y))
            val watermarkPixel = if (x >= wi.waterImageName.width + wi.singleParams[0].toInt() ||
                y >= wi.waterImageName.height + wi.singleParams[1].toInt()) {
                imagePixel
            } else {
                Color(wi.waterImageName.getRGB(x - wi.singleParams[0].toInt(), y - wi.singleParams[1].toInt()))
            }
            val color = if(watermarkPixel == Color(wi.transColors[0].toInt(), wi.transColors[1].toInt(),
                    wi.transColors[2].toInt())) {
                Color(wi.mainImageName.getRGB(x, y))
            } else {
                combineImages(wi.waterTransPercentage, watermarkPixel, imagePixel)
            }
            outputImage.setRGB(x, y, color.rgb)
        }
    }
    writeFile(wi.finalImageName, outputImage, readFileType(wi.finalImageName))
}
fun createGridWatermark(wi: WatermarkedImage) {
    val outputImage = buildOutputImage(wi.mainImageName.width, wi.mainImageName.height, BufferedImage.TYPE_INT_RGB)
    var waterCountX = 0
    for (x in 0 until wi.mainImageName.width) {
        if (waterCountX >= wi.waterImageName.width) waterCountX = 0
        var waterCountY = 0
        for (y in 0 until wi.mainImageName.height) {
            if (waterCountY >= wi.waterImageName.height) waterCountY = 0
            val imagePixel = Color(wi.mainImageName.getRGB(x, y))
            val watermarkPixel = Color(wi.waterImageName.getRGB(waterCountX, waterCountY))
            val color = if(watermarkPixel == Color(wi.transColors[0].toInt(), wi.transColors[1].toInt(),
                    wi.transColors[2].toInt())) {
                Color(wi.mainImageName.getRGB(x, y))
            } else {
                combineImages(wi.waterTransPercentage, watermarkPixel, imagePixel)
            }
            outputImage.setRGB(x, y, color.rgb)
            waterCountY++
        }
        waterCountX++
    }
    writeFile(wi.finalImageName, outputImage, readFileType(wi.finalImageName))
}
fun createAlphaGridWatermark(wi: WatermarkedImage) {
    val outputImage = BufferedImage(wi.mainImageName.width, wi.mainImageName.height, BufferedImage.TYPE_INT_RGB)
    var waterCountX = 0
    for (x in 0 until wi.mainImageName.width) {
        if (waterCountX >= wi.waterImageName.width) waterCountX = 0
        var waterCountY = 0
        for (y in 0 until wi.mainImageName.height) {
            if (waterCountY >= wi.waterImageName.height) waterCountY = 0
            val imagePixel = Color(wi.mainImageName.getRGB(x, y))
            var watermarkPixel = Color(wi.waterImageName.getRGB(waterCountX, waterCountY), true)
            if (watermarkPixel.alpha == 0) {
                watermarkPixel = Color(wi.mainImageName.getRGB(x, y))
            }
            val color = combineImages(wi.waterTransPercentage, watermarkPixel, imagePixel)
            outputImage.setRGB(x, y, color.rgb)
            waterCountY++
        }
        waterCountX++
    }
    writeFile(wi.finalImageName, outputImage, readFileType(wi.finalImageName))
}
fun createWatermark(wi: WatermarkedImage) {
    val outputImage = buildOutputImage(wi.mainImageName.width, wi.mainImageName.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until wi.mainImageName.width) {
        for (y in 0 until wi.mainImageName.height) {
            val imagePixel = Color(wi.mainImageName.getRGB(x, y))
            val watermarkPixel = Color(wi.waterImageName.getRGB(x, y))
            val color = combineImages(wi.waterTransPercentage, watermarkPixel, imagePixel)
            outputImage.setRGB(x, y, color.rgb)
        }
    }
    writeFile(wi.finalImageName, outputImage, readFileType(wi.finalImageName))
}
fun createColoredWatermark(wi: WatermarkedImage) {
    val outputImage = buildOutputImage(wi.mainImageName.width, wi.mainImageName.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until wi.mainImageName.width) {
        for (y in 0 until wi.mainImageName.height) {
            val imagePixel = Color(wi.mainImageName.getRGB(x, y))
            val watermarkPixel = Color(wi.waterImageName.getRGB(x, y))
            val color = if(watermarkPixel == Color(wi.transColors[0].toInt(), wi.transColors[1].toInt(),
                    wi.transColors[2].toInt())) {
                Color(wi.mainImageName.getRGB(x, y))
            } else {
                combineImages(wi.waterTransPercentage, watermarkPixel, imagePixel)
            }
            outputImage.setRGB(x, y, color.rgb)
        }
    }
    writeFile(wi.finalImageName, outputImage, readFileType(wi.finalImageName))
}
fun createAlphaSingleWatermark(wi: WatermarkedImage) {
    val outputImage = buildOutputImage(wi.mainImageName.width, wi.mainImageName.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until wi.mainImageName.width) {
        for (y in 0 until wi.mainImageName.height) {
            val imagePixel = Color(wi.mainImageName.getRGB(x, y))
            val color = Color(imagePixel.red, imagePixel.green, imagePixel.blue)
            outputImage.setRGB(x, y, color.rgb)
        }
    }
    for (x in wi.singleParams[0].toInt() until wi.waterImageName.width + wi.singleParams[0].toInt()) {
        for (y in wi.singleParams[1].toInt() until wi.waterImageName.height + wi.singleParams[1].toInt()) {
            val imagePixel = Color(wi.mainImageName.getRGB(x, y))
            var watermarkPixel = if (x >= wi.waterImageName.width + wi.singleParams[0].toInt() ||
                y >= wi.waterImageName.height + wi.singleParams[1].toInt()) {
                imagePixel
            } else {
                Color(wi.waterImageName.getRGB(x - wi.singleParams[0].toInt(), y - wi.singleParams[1].toInt()), true)
            }
            if (watermarkPixel.alpha == 0) {
                watermarkPixel = Color(wi.mainImageName.getRGB(x, y))
            }
            val color = combineImages(wi.waterTransPercentage, watermarkPixel, imagePixel)
            outputImage.setRGB(x, y, color.rgb)
        }
    }
    writeFile(wi.finalImageName, outputImage, readFileType(wi.finalImageName))
}
fun writeFile(finalImageName: String, outputImage: BufferedImage, fileType: String) {
    val outputFile = File(finalImageName)
    ImageIO.write(outputImage, fileType, outputFile)
    println("The watermarked image $finalImageName has been created.")
    exitProcess(0)
}
fun buildOutputImage(width: Int, height: Int, typeIntRgb: Int): BufferedImage {
    return BufferedImage(width, height, typeIntRgb)
}
fun main() {
    val w = WatermarkedImage()
    w.mainImageName = getMainImage()
    w.waterImageName = getWaterImage()
    checkSize(w.mainImageName, w.waterImageName)
    alphaChannelUse(w.mainImageName, w.waterImageName)
    println("Do you want to set a transparency color?")
    if (readln() == "yes") {
        w.transColors = getTransColor()
        w.waterTransPercentage = getWaterTransparency()
        when (getPosMethod()) {
            "single" -> {
                w.singleParams = getSinglePos(w.mainImageName, w.waterImageName)
                w.finalImageName = getFinalImageName()
                createSingleWatermark(w)
            }
            "grid" -> {
                w.finalImageName = getFinalImageName()
                createGridWatermark(w)
            }
            else -> {
                println("wrong Input")
                exitProcess(0)
            }
        }
        w.finalImageName = getFinalImageName()
        createColoredWatermark(w)
    }
    w.waterTransPercentage = getWaterTransparency()
    w.finalImageName = getFinalImageName()
    createWatermark(w)
}