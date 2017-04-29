import java.util.*;

class Record{

  private String[] record;
  private int sizeOfRecord;

  Record(String... record){
    this.sizeOfRecord = record.length;
    this.record = new String[sizeOfRecord];
    setRecord(record);
  }

  private void setRecord(String... record){
    this.record = new String[sizeOfRecord];
    for(int i = 0; i < sizeOfRecord; i++){
      this.record[i] = record[i];
    }
  }

  public String[] getRecord(){
    return this.record;
  }

  public String getKey(){
    return this.record[0];
  }

  public void testRecords(){
    Record record = new Record("1", "Fido", "dog", "ab123");
    assert(record.getRecord()[0] == "1");
    assert(record.getRecord()[1] == "Fido");
    assert(record.getRecord()[2] == "dog");
    assert(record.getRecord()[3] == "ab123");
    assert(record.getKey()== "1");

    Record record2 = new Record("2", "Wanda", "fish", "ef789");
    String[] theRecord2 = record2.getRecord();
    assert(theRecord2[0] == "2");
    assert(theRecord2[1] == "Wanda");
    assert(theRecord2[2] == "fish");
    assert(theRecord2[3] == "ef789");
    assert(record2.getKey()== "2");


  }

}
