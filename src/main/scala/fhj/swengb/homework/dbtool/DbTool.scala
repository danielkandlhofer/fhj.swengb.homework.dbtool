package fhj.swengb.homework.dbtool

import java.sql.{Connection, DriverManager, ResultSet, Statement}

import fhj.swengb.Person._
import fhj.swengb.{Person, Students}

import scala.collection.mutable.ListBuffer
import scala.util.Try

/**
  * Example to connect to a database.
  *
  * Initializes the database, inserts example data and reads it again.
  *
  */
object Db {

  /**
    * A marker interface for datastructures which should be persisted to a jdbc database.
    *
    * @tparam T the type to be persisted / loaded
    */
  trait DbEntity[T] {

    /**
      * Recreates the table this entity is stored in
      *
      * @param stmt
      * @return
      */
    def reTable(stmt: Statement): Int

    /**
      * Saves given type to the database.
      *
      * @param c
      * @param t
      * @return
      */
    def toDb(c: Connection)(t: T): Int

    /**
      * Given the resultset, it fetches its rows and converts them into instances of T
      *
      * @param rs
      * @return
      */
    def fromDb(rs: ResultSet): List[T]

    /**
      * Queries the database
      *
      * @param con
      * @param query
      * @return
      */
    def query(con: Connection)(query: String): ResultSet = {
      con.createStatement().executeQuery(query)
    }

    /**
      * Sql code necessary to execute a drop table on the backing sql table
      *
      * @return
      */
    def dropTableSql: String

    /**
      * sql code for creating the entity backing table
      */
    def createTableSql: String

    /**
      * sql code for inserting an entity.
      */
    def insertSql: String

  }

  lazy val maybeConnection: Try[Connection] =
    Try(DriverManager.getConnection("jdbc:sqlite::memory:"))

}
object Product extends Db.DbEntity[Product] {

  val dropTableSql = "drop table if exists product"
  val createTableSql = "create table product (id integer, bezeichnung string, price double)"
  val insertSql = "insert into product(id, bezeichnung, price) VALUES (? ? ?)"


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
      rs.getDouble("price")))
    return lb.toList
  }

  def queryAll(con: Connection): ResultSet =
    query(con)("select * from product")

}

case class Product(id: Int, name: String, price: Double) extends Db.DbEntity[Product] {

   def reTable(stmt: Statement): Int = ???
   def createTableSql: String = ???
   def dropTableSql: String = ???
   def toDb(c: Connection)(t: Product): Int = ???
   def fromDb(rs: ResultSet): List[Product] = ???
   def insertSql(): String = ???
}


object DbTool {

  def main(args: Array[String]) {
    for {con <- Db.maybeConnection
         _ = Product.reTable(con.createStatement())
         //_ = Students.sortedStudents.map(toDb(con)(_))
         s <- Product.fromDb(Product.queryAll(con))} {
      println(s)
    }
  }

}
