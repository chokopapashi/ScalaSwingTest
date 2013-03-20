
import java.awt.Color

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Buffer
import scala.swing.Component
import scala.swing.Dimension
import scala.swing.Graphics2D
import scala.swing.MainFrame
import scala.swing.Panel
import scala.swing.SimpleSwingApplication

object GraphicSample extends SimpleSwingApplication {

    val column = 80
    val y_h = 127 + 100
    val y_l = 127 - 100

    val (p_x: Array[Int], p_y: Array[Int]) = {
        val xs = new Array[Int](column)
        val ys = ArrayBuffer.empty[Int]
        var v: Int = 0
        for(i <- 0 to (column-1)) {
            xs(i) = i * 10
/*
            ys += {
                if(((i/3) & 0x00000001) == 0) y_l
                else y_h
            }
*/
            ys += {
                if(((i) & 0x00000001) == 0) y_l
                else y_h
            }
        }

        (xs, ys.toArray)
    }

    (p_x zip p_y).foreach{case (x,y) => printf("(%03d,%03d)",x,y)}

    def top = new MainFrame {
        title = "Graphic Sample"
        minimumSize = new Dimension(300, 300)

        contents = new Panel {
            override def contents: Buffer[Component] = Buffer.empty
            override def paint(g: Graphics2D) = super.paint(g)
            override protected def paintComponent(g: Graphics2D) {
                g.setColor(Color.GREEN)
//                g.fillOval(0,0,size.width*2,size.height*2)
                g.drawPolyline(p_x, p_y, p_x.length)
            }
        }
    }
}
