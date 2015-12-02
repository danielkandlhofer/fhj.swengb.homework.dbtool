package fhj.swengb.homework.dbtool

import java.sql.{ResultSet, Connection, Statement}

import scala.collection.mutable.ListBuffer

/**
  * Created by loete on 02.12.2015.
  */
object Order extends Db.DbEntity[Order] {

  val dropTableSql = "drop table if exists order"
  val createTableSql = "create table order (oid integer, cid integer, price double)"
  val insertSql = "insert into order (oid, cid, price) VALUES (?, ?, ?)"


  def reTable(stmt: Statement): Int = {
    stmt.executeUpdate(Order.dropTableSql)
    stmt.executeUpdate(Order.createTableSql)
  }

  def toDb(c: Connection)(i: Order): Int = {
    val pstmt = c.prepareStatement(insertSql)
    pstmt.setInt(1, i.oid)
    pstmt.setInt(2, i.cid)
    pstmt.setDouble(3, i.price)
    pstmt.executeUpdate()
  }

  def fromDb(rs: ResultSet): List[Order] = {
    val lb: ListBuffer[Order] = new ListBuffer[Order]()
    while (rs.next()) lb.append(Order(rs.getInt("oid"), rs.getInt("cid"), rs.getDouble("price")))
    lb.toList
  }

  def queryAll(con: Connection): ResultSet = query(con)("select * from order")

}

case class Order(oid:Int, cid: Int, price:Double) extends Db.DbEntity[Order] {

  def reTable(stmt: Statement): Int = 0

  def toDb(c: Connection)(t: Order): Int = 0

  def fromDb(rs: ResultSet): List[Order] = List()

  def dropTableSql: String = "drop table if exists order"

  def createTableSql: String = "create table order (oid integer, cid integer, price double)"

  def insertSql: String = "insert into order (oid,cid,price) VALUES (?, ?, ?)"

}