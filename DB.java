import java.io.*;
import java.util.*;

class DB {

  private PrintStream out;
  private String read, write;

  void run(){
    Display run = new Display();
    run.runDisplay();
  }

  void test(){
    Record record = new Record("fields", "id", "name", "pet", "owner");
    record.testRecords();
    Table table = new Table("t1", "id", "name", "pet", "owner");
    table.testTable();

    // This is to test whether a file exists and whether another is created
    DataFile newFile = new DataFile();
    assert(newFile.doesItExists("t1.txt") == false);
    assert(newFile.doesItExists("test.txt") == true);

    // This is to test the integrty of the Database
    Database database = new Database();
    database.testDatabase();
    System.out.println("\nAll test passed!\n");

  }

  public static void main(String[] args) {
    boolean testing = false;
    assert(testing = true);
    DB program = new DB();
    if (testing) program.test();
    else if(!testing) {
      program.run();
    }
    else {
      System.err.println("Use:");
      System.err.println("  java -ea DB     for testing or");
      System.exit(1);
    }
  }

}
