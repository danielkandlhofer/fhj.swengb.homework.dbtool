package fhj.swengb.homework.dbtool

import java.net.URL
import java.util.ResourceBundle
import javafx.application.Application
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.{Initializable, FXML, FXMLLoader}
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import javafx.scene.{Scene, Parent}
import javafx.stage.Stage
import javafx.scene.control.TableView

import fhj.swengb.Person
import sun.plugin.javascript.navig.Anchor

import scala.collection.concurrent.RDCSS_Descriptor
import scala.util.control.NonFatal

/**
  * Created by Amar on 02.12.2015.
  */
object DbToolApp {

  def main(args: Array[String]) {
    Application.launch(classOf[DbToolApp], args: _*)
  }
}


class DbToolApp extends javafx.application.Application {


  //val Css = "/fhj/swengb/homework/dbtool/...
  val Fxml = "./testgui.fxml"


  val loader = new FXMLLoader(getClass.getResource(Fxml))

  override def start(stage: Stage): Unit =
    try {
      stage.setTitle("DbTool")
      loader.load[Parent]() // side effect
      val scene = new Scene(loader.getRoot[Parent]) //loads the default scene
      stage.setScene(scene)
      stage.setResizable(false) //window cannot be rescaled
      //stage.getScene.getStylesheets.add(Css)
      stage.show()
    } catch {
      case NonFatal(e) => e.printStackTrace()
    }
}



class DbToolController extends Initializable {

  @FXML var anchorpane: AnchorPane = _

  // Example for the Product Entity
  @FXML var tableview: TableView[Product] = _
  @FXML var c1: TableColumn[Product,Integer] = _
  @FXML var c2: TableColumn[Product,String] = _
  @FXML var c3: TableColumn[Product,Double] = _



  //create data for inserting in an observerList
  val data: ObservableList[Product] = FXCollections.observableArrayList(
    new Product(55,"hallo",33.0)
  )



  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    //Für die columns namen setzen
    c1.setCellValueFactory(new PropertyValueFactory[Product,Integer]("id"))
    c2.setCellValueFactory(new PropertyValueFactory[Product,String]("name"))
    c3.setCellValueFactory(new PropertyValueFactory[Product,Double]("price"))

    //inserting data from the observableArrayList
    tableview.setItems(data)
  }





}


