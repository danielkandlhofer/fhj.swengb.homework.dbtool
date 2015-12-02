package fhj.swengb.homework.dbtool

import java.net.URL
import java.sql.{Connection, ResultSet, Statement}

import scala.collection.mutable.ListBuffer

object Product extends Db.DbEntity[Product] {
  // Bei DB
  val dropTableSql = "drop table if exists product"
  val createTableSql = "create table product (ID int, productName string, productDescription String, productPrice double)"
  val insertSql = "insert into product (ID int, productName string, productDescription String, productPrice double VALUES (?, ?, ?, ?)"


  def reTable(stmt: Statement): Int = {
    stmt.executeUpdate(Product.dropTableSql)
    stmt.executeUpdate(Product.createTableSql)
  }

  // referenziert auf das trait Product und nicht object Product
  def toDb(c: Connection)(p: Product): Int = {
    val pstmt = c.prepareStatement(insertSql)
    pstmt.setInt(1, p.ID)
    pstmt.setString(2, p.productName)
    pstmt.setString(3, p.productDescription)
    pstmt.setDouble(4, p.productPrice)
    pstmt.executeUpdate()
  }

  def fromDb(rs: ResultSet): List[Product] = {
    val lb: ListBuffer[Product] = new ListBuffer[Product]()
    while (rs.next()) lb.append(Product(rs.getInt("ID"), rs.getString("productName"), rs.getString("productDescription"), rs.getDouble("productPrice")))
    lb.toList
  }



  def queryAll(con: Connection): ResultSet =
    query(con)("select * from product")

}

/*
/**
 * Created by lad on 24.09.15.
 */
sealed trait Product {

  def ID: Int

  def productName: String

  def productDescription: String

  def productPrice: Double

  def normalize(in: String): String = {
    val mapping =
      Map("ä" -> "ae",
        "ö" -> "oe",
        "ü" -> "ue",
        "ß" -> "ss")
    mapping.foldLeft(in) { case ((s, (a, b))) => s.replace(a, b) }
  }

  /*
  def normalize2(input: String): String = {
    val output = Normalizer.normalize(input, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    pattern.matcher(output).replaceAll("")
  } */

/*
  def userId: String = {
    val fst = firstName(0).toLower.toString
    normalize(fst + secondName.toLowerCase)
  }
*/

  /*
  val gitHubHome: String = s"https://github.com/$githubUsername/"

  @deprecated("remove", "now")
  val tutorialName: String = "fhj.swengb.assignments.tutorial"
  val tutorialURL: URL = new URL(gitHubHome + tutorialName)

  def mkHome: String = s" - $longName : [$githubUsername]($gitHubHome)"

  def gitHubUser: GitHub.User = {
    import GitHub.GithubUserProtocol._
    import GitHub._
    import spray.json._

    val webserviceString: String = scala.io.Source.fromURL(new URL(s"https://api.github.com/users/$githubUsername?client_id=083fd3dd1f81ff332025&client_secret=6450ef529f167ab41cf263a82a05fd1ce2084724")).mkString
    webserviceString.parseJson.convertTo[User]
  }
*/

}
*/

case class Product(ID: Int,
                   productName: String,
                   productDescription: String,
                   productPrice: Double) extends Db.DbEntity[Product] {
  def reTable(stmt: Statement): Int = ???
  def createTableSql: String = ???
  def dropTableSql: String = ???
  def toDb(c: Connection)(t: Product): Int = ???
  def fromDb(rs: ResultSet): List[Product] = ???
  def insertSql(): String = ???

}
/*
case class Student(firstName: String,
                   secondName: String,
                   githubUsername: String,
                   groupId: Int) extends Person
*/