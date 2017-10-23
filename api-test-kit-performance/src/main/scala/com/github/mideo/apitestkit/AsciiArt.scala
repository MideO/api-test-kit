package com.github.mideo.apitestkit

import java.awt.image.BufferedImage
import java.awt.{Font, Graphics2D, RenderingHints}
import java.util.concurrent.TimeUnit

object AsciiArt {
  def draw(text: String, height: Int = 30, width: Int = 100, symbol: String = "$", size: Int = 14): Unit = {
    lazy val img: BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    lazy val g = img.getGraphics
    lazy val graphics = g.asInstanceOf[Graphics2D]

    g.setFont(new Font(null, Font.ITALIC, size))
    graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
    graphics.drawString(text, 10, 20)
    draw(img, height, width, symbol)
    TimeUnit.MILLISECONDS.sleep(500)
  }

  private def draw(img: BufferedImage, height: Int, width: Int, symbol: String): Unit = {
    val WhiteSpaceLong = -16777216
    try {
      println("\n \n")
      for (y <- 0 until height) {
        val sb = new StringBuilder
        for (x <- 0 until width) {
          img.getRGB(x, y) match {
            case WhiteSpaceLong => sb.append(" ")
            case _ => sb.append(symbol)
          }

        }
        if (!sb.toString().trim.isEmpty) {
          println(sb)
        }
      }
      println("\n \n")
    }
    catch {
      case _: Exception => println("Failed to print ascii")
    }

  }

}
