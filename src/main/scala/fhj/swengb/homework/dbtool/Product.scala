/**
  * Created by Daniel on 27.11.2015.
  */
package fhj.swengb

import java.net.URL
import java.sql.{Connection, ResultSet, Statement}
/*
import fhj.swengb.homework.dbtool.DbEntity
import fhj.swengb.Db.DbEntity
import fhj.swengb.GitHub
import fhj.swengb.GitHub.User
import fhj.swengb.Person._
*/
import scala.collection.mutable.ListBuffer


object Product extends Db.DbEntity[Product] {

  val dropTableSql = "drop table if exists product"
  val createTableSql = "create table product (id string, name string, price integer)"
  val insertSql = "insert into product (id, name, price) VALUES (?, ?, ?, ?)"


  def reTable(stmt: Statement): Int = {
    stmt.executeUpdate(Product.dropTableSql)
    stmt.executeUpdate(Product.createTableSql)
  }

  def toDb(c: Connection)(p: Product): Int = {
    val pstmt = c.prepareStatement(insertSql)
    pstmt.setInt(1, p.id)
    pstmt.setString(2, p.name)
    pstmt.setDouble(3, p.price)
    pstmt.executeUpdate()
  }

  def fromDb(rs: ResultSet): List[Product] = {
    val lb: ListBuffer[Product] = new ListBuffer[Product]()
    while (rs.next()) lb.append(Product(rs.getInt("id"),
      rs.getString("name"),
      rs.getDouble("price"),
    lb.toList
  }

  def queryAll(con: Connection): ResultSet =
    query(con)("select * from product")
}

/**
  * Created by lad on 24.09.15.
  */
sealed trait Product {

  def id: Int

  def name: String

  def price: Double

  def longName = s"$name"

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


  def userId: String = {
    val fst = name(0).toLower.toString
    normalize(fst.toLowerCase)
  }




