// A class which contains a HashMap to hold Tables and their respective names.

import java.util.*;

class Database{

  private HashMap<String, Table> database;

  Database(){
    database = new HashMap<>();
  }

  public void addTable(String tableName, Table inTable){
    this.database.put(tableName, inTable);
  }

  public void renameTable(String oldTableName, String newTableName){
    Table obj = this.database.get(oldTableName);
    this.database.remove(oldTableName);
    this.database.put(newTableName, obj);
  }

  public List<String> getKeys(){
    List<String> l = new ArrayList<String>(this.database.keySet());
    return l;
  }

  public Table getTable(String key){
    return this.database.get(key);
  }

  public void deleteTable(String key){
    this.database.remove(key);
  }

  public void testDatabase(){
    Database database = new Database();
    Table table1 = new Table("t1", "id", "name", "pet", "owner");
    Table table2 = new Table("t2", "username", "name", "address", "capital");
    Table table3 = new Table("t3", "owner", "car", "model", "plate");
    database.addTable("t1", table1);
    database.addTable("t2", table2);
    database.addTable("t3", table3);
    assert(database.getKeys().get(0) == "t1");
    assert(database.getKeys().get(1) == "t2");
    assert(database.getKeys().get(2) == "t3");
    assert(database.getTable("t1").selectRecord("fields")[0] == "id");
    assert(database.getTable("t1").selectRecord("fields")[1] == "name");
    assert(database.getTable("t1").selectRecord("fields")[2] == "pet");
    assert(database.getTable("t2").selectRecord("fields")[0] == "username");
    assert(database.getTable("t2").selectRecord("fields")[1] == "name");
    assert(database.getTable("t2").selectRecord("fields")[2] == "address");
    database.renameTable("t2", "newlyNamed");
    assert(database.getTable("newlyNamed").selectRecord("fields")[2] == "address");
    assert(database.getTable("t3").selectRecord("fields")[0] == "owner");
    assert(database.getTable("t3").selectRecord("fields")[1] == "car");
    assert(database.getTable("t3").selectRecord("fields")[2] == "model");
    database.renameTable("t3", "newlyNamed");
    assert(database.getTable("newlyNamed").selectRecord("fields")[2] == "model");
    database.deleteTable("t2");
    assert(database.getTable("t2") == null);
  }

}
