// This class handles everything related to files. It is specifically written to
// my database file format (that is, whenever it finds a "[" it will create a
// new table and whenever it finds a "]" it will finish adding records to that
// table). Because the writeToFile funstion deals with printing into a file, all
// of the testing regarding this function was done by eyetesting.
// Testing for Datafile is done in main class (DB)

import java.io.*;
import java.nio.file.*;
import java.util.*;


class DataFile{

  private File fileWrite, fileRead;
  private Table newTable, databaseTable;
  private String workingDirectory, read, write, tableName, keyTableName;
  private int numToken;
  private Database myDatabase;
  private List<String> keys, tableKeys;
  private boolean first;

  // start a file, clean it and create a path
  void run(String write){
    this.write = write;
    try {
    cleanFile();
     this.workingDirectory = System.getProperty("user.dir");
     this.fileWrite = new File(workingDirectory, this.write);
     this.fileWrite.createNewFile();
     } catch (IOException e) {
     e.printStackTrace();
     }
  }

  public void writeToFile(Database displayDatabase){

    this.myDatabase = new Database();
    this.myDatabase = displayDatabase;

    cleanFile();
    try (
      FileWriter fw = new FileWriter(this.fileWrite);
      BufferedWriter bw = new BufferedWriter(fw);
      PrintWriter out = new PrintWriter(bw)) {
        this.tableKeys = new ArrayList<String>(this.myDatabase.getKeys());
        for(int z = 0; z < tableKeys.size(); z++){
          setVariables(z);
          out.println(String.format("[ %s", keyTableName));
          printFields(out);
          printRecords(out);
          out.println("]"); }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // loof for a key "fields" and print it first
  private void printFields(PrintWriter out){
    for(int j = 0; j < numToken; j++){
      if(j == (numToken-1)){ out.println(databaseTable.selectRecord("fields")[j]); }
      else { out.print(databaseTable.selectRecord("fields")[j] + "/"); }
    }
  }

  // go ahead and print the records after the field in any order
  private void printRecords(PrintWriter out){
    for (String s : this.keys) { if(s != "fields"){
        for(int j = 0; j < numToken; j++){
          if(j == (numToken-1)){ out.print(databaseTable.selectRecord(s)[j]); }
          else { out.print(databaseTable.selectRecord(s)[j] + "/"); }
        } out.print("\n"); }
    }
  }

  // Initialize these variables depending on the tableKeys
  private void setVariables(int z){
    this.keyTableName = this.tableKeys.get(z);
    this.databaseTable = this.myDatabase.getTable(this.keyTableName);
    this.keys = new ArrayList<String>(this.databaseTable.getKeys());
    this.numToken = this.databaseTable.selectRecord("fields").length;
  }

  public Database scanDocument(String read){
    this.myDatabase = new Database();
    this.read = read;
    try {
      this.fileRead = new File(this.read);
      Scanner scanFile = new Scanner(this.fileRead);
      this.first = true;
      while (scanFile.hasNextLine()) {
        String line = scanFile.nextLine();
        if(line.contains("]")){
          this.first = true;
          this.myDatabase.addTable(this.tableName, this.newTable);
        } else if(line.contains("[")){
          line = line.replace("[ ", ""); line = line.replaceAll(" ", "");
          this.tableName  = line;
        } else {
          Scanner lineScan = new Scanner(line).useDelimiter("/");
          List<String> scannedRecord = new ArrayList<String>();
          while(lineScan.hasNext()){ scannedRecord.add(lineScan.next()); }
          lineScan.close();
          addMoreTables(scannedRecord);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return this.myDatabase;
	}

  // this creates a new table and to set the fields in place
  private void addMoreTables(List<String> scannedRecord){
    if(this.first == true){
      newTable = new Table(this.tableName, scannedRecord.toArray(new String[scannedRecord.size()]));
      this.first = false;
    }
    else{
      newTable.insertRecord(scannedRecord.toArray(new String[scannedRecord.size()]));
    }
  }

  // Empties the file in case it exists
  private void cleanFile(){
    try {
      Files.write(Paths.get(this.write), "".getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean doesItExists(String file){
    File f = new File(file);
    if(f.exists() && !f.isDirectory()) { return true; }
    return false;
  }

}
