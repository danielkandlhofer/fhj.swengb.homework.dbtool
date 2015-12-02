package fhj.swengb.homework.dbtool

import java.sql.{Connection, DriverManager, ResultSet, Statement}

import fhj.swengb.Product._

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

case class Products(id: Int, name: String, price: Double) extends Db.DbEntity[Products] {

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
    * @param p
    * @return
    */
  def toDb(c: Connection)(p: Products): Int

  /**
    * Given the resultset, it fetches its rows and converts them into instances of T
    *
    * @param rs
    * @return
    */
  def fromDb(rs: ResultSet): List[Products]

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


def reTable(stmt: Statement): Int = 0

  def toDb(c: Connection)(t: Products): Int = 0

  def fromDb(rs: ResultSet): List[Products] = List()

  def dropTableSql: String = "drop table Products"

  def createTableSql: String = "create table Products (...)"

  def insertSql: String = "insert ..."

}

object DbTool {

  def main(args: Array[String]) {
    for {con <- Db.maybeConnection
         _ = Products.reTable(con.createStatement())
         _ = Products.sortedStudents.map(toDb(con)(_))
         s <- Products.fromDb(queryAll(con))} {
      println(s)
    }
  }

}
