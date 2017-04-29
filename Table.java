// A class for storing Records with their respective keys. Each record contains
// a single primary key (which is the first column of the record). Therefore,
// in order to ensure a unique key, it is adviced to the user to begin the
//first column with an id field.
// Fields are embedded in the hash map, their key is "fields".

import java.util.*;

class Table{

  private HashMap<String, Record> table = new HashMap<>();
  private Record record;
  private String tableName;


  Table(String tableName, String... fields){
    this.tableName = tableName;
    this.record = new Record(fields);
    this.table.put("fields", this.record);
  }

  public String getTableName(){
    return this.tableName;
  }

  public void insertRecord(String... record){
    this.record = new Record(record);
    this.table.put(this.record.getKey(), this.record);
  }

  // Selects specific Records
  public String[] selectRecord(String key){
    if(this.table.get(key) == null){
      return null;
    } else{
        return this.table.get(key).getRecord();
      }
  }

  public void deleteRecord(String... key){
    for(int i = 0; i < key.length; i++){
      this.table.remove(key[i]);
    }
  }

  public String updateRecord(String key, String... record){
    if(record.length == selectRecord("fields").length){
      deleteRecord(key);
      insertRecord(record);
    }
    return "Your record exceed the field size";
  }

  public HashMap mapTable(){
    return this.table;
  }

  public List<String> getKeys(){
    List<String> l = new ArrayList<String>(this.table.keySet());
    return l;
  }

  public void testTable(){

    Table t0 = new Table("t0", "id", "name", "animal", "owner");
    assert(t0.selectRecord("fields")[0] == "id");
    t0.insertRecord("1", "Winston", "dog", "elf123");
    assert(t0.selectRecord("1")[1] == "Winston");
    assert(t0.selectRecord("1")[2] == "dog");
    t0.insertRecord("2", "Wang", "dog", "elf123");
    t0.insertRecord("3", "Fluffy", "dog", "elf123");
    t0.deleteRecord("1");
    assert(t0.selectRecord("1") == null);
    t0.updateRecord("1", "1", "Poo", "dog", "elf123");
    assert(t0.selectRecord("1")[1] == "Poo");
    t0.insertRecord("2", "Poo", "dog", "elf123");
    assert(t0.selectRecord("2")[2] == "dog");
    assert(t0.getTableName() == "t0");
    assert(t0.updateRecord("1", "1", "Poo", "dog", "elf123", "extra").equals(
      "Your record exceed the field size"));
    t0.deleteRecord("1", "2", "3");
    assert(t0.selectRecord("1") == null);
    assert(t0.selectRecord("2") == null);
    assert(t0.selectRecord("3") == null);

    Table t1 = new Table("t1", "last", "first", "address");
    t1.insertRecord("zea", "seth", "lalala address");
    assert(t1.selectRecord("zea")[1] == "seth");
    assert(t1.getTableName() == "t1");
    t1.deleteRecord("1");
    assert(t1.selectRecord("1") == null);


    Table t2 = new Table("t2", "name", "pet", "owner");
    t2.insertRecord("primo", "dog", "seth");
    assert(t2.selectRecord("primo")[0]) == "primo";
    assert(t2.getTableName() == "t2");

  }

}
