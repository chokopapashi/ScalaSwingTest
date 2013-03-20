
import java.awt.Font

import scala.collection.immutable.ListMap
import scala.collection.mutable.Buffer
import scala.collection.JavaConversions.asScalaSet
import scala.swing.Component
import scala.swing.BorderPanel
import scala.swing.BoxPanel
import scala.swing.Button
import scala.swing.ComboBox
import scala.swing.Dimension
import scala.swing.FlowPanel
import scala.swing.GridBagPanel
import scala.swing.GridPanel
import scala.swing.Label
import scala.swing.MainFrame
import scala.swing.Orientation
import scala.swing.Panel
import scala.swing.SimpleSwingApplication
import scala.swing.event.SelectionChanged

import javax.swing.plaf.FontUIResource
import javax.swing.UIManager

import org.hirosezouen.hzutil.HZLog._

object PanelSample_2 extends SimpleSwingApplication {
    implicit val logger = getLogger(this.getClass.getName)

    val baseFont = new FontUIResource(Font.createFont(Font.TRUETYPE_FONT,
            getClass.getResourceAsStream("TakaoMincho.ttf")).deriveFont(14.0f))

    for(entry <- UIManager.getDefaults.entrySet)
        if(entry.getKey.toString.endsWith(".font"))
            UIManager.put(entry.getKey, baseFont)

    val boxPanel = new BoxPanel(Orientation.Vertical) {
        contents += new Label("AAA")
        contents += new Label("BBB")
        contents += new Button("CCC")
    }

    val borderPanel = new BorderPanel() {
        add(new Label("AAA"), BorderPanel.Position.North)
        add(new Label("BBB"), BorderPanel.Position.East)
        add(new Button("CCC"), BorderPanel.Position.South)
    }

    val flowPanel = new FlowPanel() {
        contents += new Label("AAA")
        contents += new Label("BBB")
        contents += new Button("CCC")
    }

    val gridPanel = new GridPanel( 3, 2 ) {
        contents += new Label( "AAA" )
        contents += new Label( "BBB" )
        contents += new Label( "CCC" )
        contents += new Label( "DDD" )
        contents += new Button( "EEE" )
        contents += new Button( "FFF" )
    }

    val gridBagPanel = new GridBagPanel() {
        layout += new Label( "AAA" ) -> ( 0, 0 )
        layout += new Label( "BBB" ) -> ( 1, 0 )
        layout += new Label( "CCC" ) -> ( 0, 1 )
        layout += new Label( "DDD" ) -> ( 1, 1 )

        val c = pair2Constraints( 0, 2 )
        c.gridwidth = 2
        layout += new Button( "EEE" ) -> c
    }

    val layoutedPanelMap = ListMap("BoxPanel"     -> boxPanel,
                                   "BorderPanel"  -> borderPanel,
                                   "BorderPanel"  -> borderPanel,
                                   "FlowPanel"    -> flowPanel,
                                   "GridPanel"    -> gridPanel,
                                   "GridBagPanel" -> gridBagPanel)

    val layoutedPanel = new Panel() with SelectionModel.SelectionChangeListener
    {
        contents.asInstanceOf[Buffer[Component]] += layoutedPanelMap(layoutedPanelMap.keySet.head)

        def selectionChanged(i: Int) {
            println(f"selectionChanged($i%d)")
            visible = false
            contents.asInstanceOf[Buffer[Component]].clear
            contents.asInstanceOf[Buffer[Component]] += layoutedPanelMap.toList(i)._2
            repaint
            visible = true
        }
    }

    val mainPanel = new BoxPanel(Orientation.Vertical)  {
        visible = false

        contents += new ComboBox(layoutedPanelMap.keys.toList) {
            listenTo(selection)

            reactions += {
                case e @ SelectionChanged(_) => {
//                    log_trace(s"$e")
                    println(s"$e")
                    selectionModel.changeSelection(selection.index)
                }
            }

        }
        contents += layoutedPanel
    }

    val selectionModel = SelectionModel(1)
    selectionModel.addSelectionChangeListener(layoutedPanel)

    def top = new MainFrame {
        title = "Window Title"
        minimumSize = new Dimension(300, 300)

        contents = mainPanel
        mainPanel.visible = true
    }
}

case class SelectionModel(var selected: Int) {
    import SelectionModel._

    var selectionChangeListenerSet = Set.empty[SelectionChangeListener]
    def addSelectionChangeListener(listner: SelectionChangeListener) {
        selectionChangeListenerSet += listner
    }

    def changeSelection(i: Int) = {
        selected = i
        selectionChangeListenerSet.foreach(_.selectionChanged(i))
    }
}

object SelectionModel {
    trait SelectionChangeListener {
        def selectionChanged(i: Int): Unit
    }
}

