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

  val dropTableSql = "drop table if exists person"
  val createTableSql = "create table person (githubUsername string, firstName string, secondName String, groupId integer)"
  val insertSql = s"insert into Product (productID,name,price) VALUES(?,?,?)"


  def reTable(stmt: Statement): Int = {
    stmt.executeUpdate(Product.dropTableSql)
    stmt.executeUpdate(Product.createTableSql)
  }

  def toDb(c: Connection)(p: Product): Int = {
    val pstmt = c.prepareStatement(insertSql)
    pstmt.setInt(1, p.productID)
    pstmt.setString(2, p.name)
    pstmt.setDouble(3, p.price)
    pstmt.executeUpdate()
  }

  def fromDb(rs: ResultSet): List[Product] = {
    val lb: ListBuffer[Product] = new ListBuffer[Product]()
    while (rs.next()) lb.append(Product(rs.getString("firstName"),
      rs.getString("secondName"),
      rs.getString("githubUsername"),
      rs.getInt("groupId")))
    lb.toList
  }

  def queryAll(con: Connection): ResultSet =
    query(con)("select * from person")

}


case class Product(productID: Int,name: String,price: Double) extends Db.DbEntity[Product] {

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
  def dropTableSql: String = "drop table product"

  /**
    * sql code for creating the entity backing table
    */
  def createTableSql: String

  /**
    * sql code for inserting an entity.
    */
  def insertSql(): String = ""


}



object DbTool {

  def main(args: Array[String]) {
    for {con <- Db.maybeConnection
         _ = Person.reTable(con.createStatement())
         _ = Students.sortedStudents.map(toDb(con)(_))
         s <- Person.fromDb(queryAll(con))} {
      println(s)
    }
  }

}
