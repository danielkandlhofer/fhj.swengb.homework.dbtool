package fhj.swengb.homework.dbtool

import java.sql.{ResultSet, Connection, Statement}

import scala.collection.mutable.ListBuffer

/**
  * Created by loete on 02.12.2015.
  */
object OrderPos extends Db.DbEntity[OrderPos] {

  val dropTableSql = "drop table if exists orderpos"
  val createTableSql = "create table orderpos (oid integer, pid integer, amount integer, price double)"
  val insertSql = "insert into orderpos (oid, pid, amount, price) VALUES (?, ?, ?, ?)"


  def reTable(stmt: Statement): Int = {
    stmt.executeUpdate(OrderPos.dropTableSql)
    stmt.executeUpdate(OrderPos.createTableSql)
  }

  def toDb(c: Connection)(i: OrderPos): Int = {
    val pstmt = c.prepareStatement(insertSql)
    pstmt.setInt(1, i.oid)
    pstmt.setInt(2, i.pid)
    pstmt.setInt(3, i.amount)
    pstmt.setDouble(4, i.price)
    pstmt.executeUpdate()
  }

  def fromDb(rs: ResultSet): List[OrderPos] = {
    val lb: ListBuffer[OrderPos] = new ListBuffer[OrderPos]()
    while (rs.next()) lb.append(OrderPos(rs.getInt("oid"), rs.getInt("pid"), rs.getInt("amount"), rs.getDouble("price")))
    lb.toList
  }

  def queryAll(con: Connection): ResultSet = query(con)("select * from orderpos")

}

case class OrderPos(oid:Int, pid: Int, amount:Int, price:Double) extends Db.DbEntity[OrderPos] {

  def reTable(stmt: Statement): Int = 0

  def toDb(c: Connection)(t: OrderPos): Int = 0

  def fromDb(rs: ResultSet): List[OrderPos] = List()

  def dropTableSql: String = "drop table if exists orderpos"

  def createTableSql: String = "create table orderpos (oid integer, pid integer, amount integer, price double)"

  def insertSql: String = "insert into orderpos (oid,pid, amount,price) VALUES (?, ?, ?, ?)"

}
