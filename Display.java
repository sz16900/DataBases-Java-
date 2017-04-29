// A class that serves the function of Inputting/Outputting data to the user
// and other classes in the program. This is the "brains" of the program.
// All other function all delegated accordingly

import java.io.*;
import java.util.*;

class Display{

  private PrintStream out;
  private Console console;
  private Table newTable, databaseTable;
  private String tableName, keyTableName, fileName, line;
  private int numToken;
  private Database myDatabase;
  private ArrayList<String> keys, tableKeys;
  private DataFile datafile;

  Display(){
    this.console = System.console();
    this.out = System.out;
    this.myDatabase = new Database();
    this.datafile = new DataFile();
    this.fileName = null;
  }

  public void runDisplay(){
    welcome();
    while(!"done".equals(this.line)){
      this.tableKeys = new ArrayList<String>(this.myDatabase.getKeys());
      this.line = console.readLine("> ");
      if(this.line.contains("create")) { create_table(); }
      if(this.line.contains("load")){ load_database(); }
      if(this.line.contains("remove")){ remove_table(); }
      if(this.line.contains("show tables")){ display_keys(); }
      if(this.line.contains("display")){ display_table(); }
      if(this.line.contains("update")){ update_table(); }
      if(this.line.contains("create database")){ create_database(); }
    }
    if(this.fileName == null) {out.println("\nGoodbye!\n");}
    else{
      this.datafile.run(this.fileName);
      this.datafile.writeToFile(this.myDatabase);
      out.println("\nGoodbye!\n");
    }
  }

  private void welcome(){
    out.println("\nWelcome to SethDB 2017.\n\n");
    out.println("Your commands are:\n");
    out.println("COMMANDS:           EXAMPLES:");
    out.println("------------------------------");
    out.println("create         ---> create t1");
    out.println("load           ---> load test.txt");
    out.println("remove         ---> remove cars");
    out.println("show tables");
    out.println("display        ---> display cars");
    out.println("update         ---> update cars");
    out.println("create database --> create database newdatabase.txt");
    out.println("done\n\n");
    out.println("**TIPS**\n- Records are separated by \"/\". I.e: id/name/age");
    out.print("- Record ids are the first element in the Record. I.e: name ");
    out.println("is the key from the record above");
    out.println("- If you create a table with the same name, it overwrites.");
    out.println("- Nothing is saved into the file until \"done\" is typed.\n\n");
  }

  private void create_database(){
    String line = this.line.replace("create database", "");
    line = line.replaceAll(" ", "");
    if(!line.contains(".txt")){
      out.println("I cant create a " + line + " file.");
    }
    else if(this.datafile.doesItExists(line)){
      out.println("This file already exists.\n");
    } else{
        this.datafile = new DataFile();
        this.myDatabase = new Database();
        this.fileName = line;
        this.datafile.run(this.fileName);
      }
  }

  private void create_table(){
    this.line = this.line.replace("create", "");
    String tableName = line.replaceAll(" ", "");
    if(tableName.equals("")){ out.println("Not a suitable table name.");}
    else{
      out.println("\nEnter fields --> id(int)/name/age/height\n");
      this.line = console.readLine("(> ");
      String[] fields = this.line.split("/");
      this.newTable = new Table(tableName, fields);
      this.myDatabase.addTable(tableName, this.newTable);
      addRecord(tableName);
    }
  }

  private void load_database(){
    String line = this.line.replace("load", "");
    line = line.replaceAll(" ", "");
    this.fileName = line;
    if(this.datafile.doesItExists(line)){
      this.myDatabase = this.datafile.scanDocument(line);
      out.println("\n" + line + " loaded!\n");
    }else{
      out.println("File doesnt exist.\n");
      this.line = "";
      }
  }

  private void display_table(){
    Table table = fetchTable();
    if(table == null){
      out.println("\ntable does not exist.\n"); return ; }
    this.numToken = table.selectRecord("fields").length;
      out.println(String.format("\n\nTABLE NAME:  %s", table.getTableName() ));
      out.println(borders());
      for(int j = 0; j < numToken; j++){
        out.print(String.format("| %-15s", table.selectRecord("fields")[j]));
      }
      out.println("| \n" + borders());
      this.keys = new ArrayList<String>(table.getKeys());
      printRecords(this.keys, table);
        out.println(borders());
  }

  private Table fetchTable(){
    String line = this.line.replace("display", "");
    line = line.replaceAll(" ", "");
    return this.myDatabase.getTable(line);
  }

  private void printRecords(ArrayList<String> keys, Table table){
    for (String s : keys) {
      if(s != "fields"){
        for(int j = 0; j < numToken; j++){
          if(table.selectRecord("fields")[j].contains("(int)")){
            out.print(String.format("| %15s", table.selectRecord(s)[j]));
          }
          else{
            out.print(String.format("| %-15s", table.selectRecord(s)[j]));
          }
        } out.println("|");
      }
    }
  }

  private void remove_table(){
    String line = this.line.replace("remove", "");
    line = line.replaceAll(" ", "");
    if(this.tableKeys.contains(line)){
      this.myDatabase.deleteTable(line);
      out.println("removed " + this.line);
    } else { out.println("Table " + line + " does not exist");}
  }

  private void display_keys(){
    this.tableKeys = new ArrayList<String>(this.myDatabase.getKeys());
    this.numToken = 1;
    out.println(borders());
    for(int z = 0; z < this.tableKeys.size(); z++){
      out.print(String.format("| %-15s", this.tableKeys.get(z)));
      out.println("|");
    }
    out.println(borders());
  }

  private void update_table(){
    out.println("\nYour commands are:");
    out.println("add record\nremove record");
    out.println("rename   ---> rename cats\n");
    out.println("** adding a record with an existing id overrides it **");
    out.println("** using the records key is a good way to update it **\n\n");
    String line = this.line.replace("update", "");
    line = line.replaceAll(" ", "");
    if(this.tableKeys.contains(line)){
      this.line = console.readLine("( "+ line + " )> ");
      if(this.line.contains("add record")){
        while (!"finish record".contains(this.line)){ addRecord(line); }
      }
      if(this.line.contains("remove record")){ removeRecord(this.line); }
      if(this.line.contains("rename")){ rename_table(line, this.line); }
    } else { out.println("Table " + line + " does not exist");}
  }

  private void rename_table(String tName, String line){
    line = this.line.replace("rename", "");
    String newName = line.replaceAll(" ", "");
    this.myDatabase.renameTable(tName, newName);
    out.println(tName + " renamed to " + newName + "\n");
  }

  private void removeRecord(String line){
    String table = line;
    line = this.line.replace("remove record", "");
    line = line.replaceAll(" ", "");
    this.myDatabase.getTable(table).deleteRecord(line);
    out.println("Record " + line + " removed.\n");
  }

  private void addRecord(String line){
    String[] myFields = this.myDatabase.getTable(line).selectRecord("fields");
    out.println("\nYour commands are:\n finish record\n");
    while (!"finish record".equals(this.line)){
      this.line = console.readLine("(enter record)> ");
      if(!"finish record".equals(this.line)){
        // add an empty space if fields are not met
        String[] record = new String[myFields.length];
        types(record, myFields, line);
      }
    }
  }

  private void types(String[] record, String[] myFields, String line){
    for(int i = 0; i < record.length; i++){
      if(i < this.line.split("/").length){
        String x = this.line.split("/")[i];
          //so it doesnt update the fields by accident
          if(x.contains("fields")){ return; }
          if(myFields[i].contains("(int)")){
            if(x.matches("[0-9]+")){ record[i] = x; }
            else { out.println("\n**You are not inserting the correct type**\n");
            return ;
          }
          } else{ record[i] = x; }
      } else { record[i] = " "; }
    }
    this.myDatabase.getTable(line).insertRecord(record);
  }

  private void setVariables(int z){
    this.keyTableName = this.tableKeys.get(z);
    this.databaseTable = this.myDatabase.getTable(this.keyTableName);
    this.keys = new ArrayList<String>(this.databaseTable.getKeys());
    this.numToken = this.databaseTable.selectRecord("fields").length;
  }

  private StringBuilder borders(){
    StringBuilder str = new StringBuilder("-");
    for(int z = 1; z <= 17*numToken; z++){ str.append("-"); }
    return str;
  }

}
