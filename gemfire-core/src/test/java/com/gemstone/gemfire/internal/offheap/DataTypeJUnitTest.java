package com.gemstone.gemfire.internal.offheap;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.gemstone.gemfire.DataSerializer;
import com.gemstone.gemfire.distributed.internal.ReplyMessage;
import com.gemstone.gemfire.internal.DSCODE;
import com.gemstone.gemfire.internal.DataSerializableFixedID;
import com.gemstone.gemfire.internal.DataSerializableJUnitTest.DataSerializableImpl;
import com.gemstone.gemfire.internal.InternalDataSerializer;
import com.gemstone.gemfire.internal.admin.remote.ShutdownAllResponse;
import com.gemstone.gemfire.test.junit.categories.UnitTest;

/**
 * Tests the DataType support for off-heap MemoryInspector.
 * @author Kirk Lund
 */
@Category(UnitTest.class)
public class DataTypeJUnitTest {

  @Test
  public void testDataSerializableFixedIDByte() throws IOException {
    DataSerializableFixedID value = new ReplyMessage();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    InternalDataSerializer.writeDSFID(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("com.gemstone.gemfire.internal.DataSerializableFixedID:" + ReplyMessage.class.getName(), type);
  }
  @Test
  public void testDataSerializableFixedIDShort() throws IOException {
    DataSerializableFixedID value = new ShutdownAllResponse();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    InternalDataSerializer.writeDSFID(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("com.gemstone.gemfire.internal.DataSerializableFixedID:" + ShutdownAllResponse.class.getName(), type);
  }
  @Ignore
  @Test
  public void testDataSerializableFixedIDInt() throws IOException {
    // impl this if we ever have a Message class that can be instantiated in a unit test
  }
  @Test
  public void testNull() throws IOException {
    Object value = null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("null", type);
  }
  @Test
  public void testString() throws IOException {
    String value = "this is a string";
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.String", type);
  }
  @Test
  public void testNullString() throws IOException {
    String value = null;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeString(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.String", type);
  }
  @Test
  public void testClass() throws IOException {
    Class<?> value = String.class;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Class", type);
  }
  @Test
  public void testDate() throws IOException {
    Date value = new Date(); 
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out); // NOT writeDate
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.Date", type);
  }
  @Test
  public void testFile() throws IOException {
    File value = new File("tmp");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.io.File", type);
  }
  @Test
  public void testInetAddress() throws IOException {
    InetAddress value = InetAddress.getLocalHost();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.net.InetAddress", type);
  }
  @Test
  public void testCharacter() throws IOException {
    Character value = Character.valueOf('c');
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Character", type);
  }
  @Test
  public void testByte() throws IOException {
    Byte value = Byte.valueOf((byte)0);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Byte", type);
  }
  @Test
  public void testShort() throws IOException {
    Short value = Short.valueOf((short)1);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Short", type);
  }
  @Test
  public void testInteger() throws IOException {
    Integer value = Integer.valueOf(1);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Integer", type);
  }
  @Test
  public void testLong() throws IOException {
    Long value = Long.valueOf(1);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Long", type);
  }
  @Test
  public void testFloat() throws IOException {
    Float value = Float.valueOf((float)1.0);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Float", type);
  }
  @Test
  public void testDouble() throws IOException {
    Double value = Double.valueOf((double)1.0);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Double", type);
  }
  @Test
  public void testByteArray() throws IOException {
    byte[] value = new byte[10];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("byte[]", type);
  }
  @Test
  public void testByteArrays() throws IOException {
    byte[][] value = new byte[1][1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("byte[][]", type);
  }
  @Test
  public void testShortArray() throws IOException {
    short[] value = new short[1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("short[]", type);
  }
  @Test
  public void testStringArray() throws IOException {
    String[] value = new String[1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.String[]", type);
  }
  @Test
  public void testIntArray() throws IOException {
    int[] value = new int[1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("int[]", type);
  }
  @Test
  public void testFloatArray() throws IOException {
    float[] value = new float[1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("float[]", type);
  }
  @Test
  public void testLongArray() throws IOException {
    long[] value = new long[1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("long[]", type);
  }
  @Test
  public void testDoubleArray() throws IOException {
    double[] value = new double[1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("double[]", type);
  }
  @Test
  public void testBooleanArray() throws IOException {
    boolean[] value = new boolean[1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("boolean[]", type);
  }
  @Test
  public void testCharArray() throws IOException {
    char[] value = new char[1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("char[]", type);
  }
  @Test
  public void testObjectArray() throws IOException {
    Object[] value = new Object[1];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Object[]", type);
  }
  @Test
  public void testArrayList() throws IOException {
    ArrayList<Object> value = new ArrayList<Object>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.ArrayList", type);
  }
  @Test
  public void testLinkedList() throws IOException {
    LinkedList<Object> value = new LinkedList<Object>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.LinkedList", type);
  }
  @Test
  public void testHashSet() throws IOException {
    HashSet<Object> value = new HashSet<Object>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.HashSet", type);
  }
  @Test
  public void testLinkedHashSet() throws IOException {
    LinkedHashSet<Object> value = new LinkedHashSet<Object>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.LinkedHashSet", type);
  }
  @Test
  public void testHashMap() throws IOException {
    HashMap<Object,Object> value = new HashMap<Object,Object>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.HashMap", type);
  }
  @Test
  public void testIdentityHashMap() throws IOException {
    IdentityHashMap<Object,Object> value = new IdentityHashMap<Object,Object>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.IdentityHashMap", type);
  }
  @Test
  public void testHashtable() throws IOException {
    Hashtable<Object,Object> value = new Hashtable<Object,Object>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.Hashtable", type);
  }
  @Test
  public void testConcurrentHashMap() throws IOException { // java.io.Serializable (broken)
    ConcurrentHashMap<Object,Object> value = new ConcurrentHashMap<Object,Object>();
    value.put("key1", "value1");
    value.put("key2", "value2");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    //DataSerializer.writeConcurrentHashMap(value, out);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.io.Serializable:java.util.concurrent.ConcurrentHashMap", type);
  }
  @Test
  public void testProperties() throws IOException {
    Properties value = new Properties();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.Properties", type);
  }
  @Test
  public void testTimeUnit() throws IOException {
    final EnumSet<TimeUnit> optimizedTimeUnits = EnumSet.range(TimeUnit.NANOSECONDS, TimeUnit.SECONDS);
    for (TimeUnit v: TimeUnit.values()) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(baos);
      DataSerializer.writeObject(v, out);
      byte[] bytes = baos.toByteArray();
      String type = DataType.getDataType(bytes); // 4?
      if (optimizedTimeUnits.contains(v)) {
        assertEquals("for enum " + v, "java.util.concurrent.TimeUnit", type);
      } else {
        assertEquals("for enum " + v, "java.lang.Enum:java.util.concurrent.TimeUnit", type);
      }
    }
  }
  @Test
  public void testVector() throws IOException {
    Vector<Object> value = new Vector<Object>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.Vector", type);
  }
  @Test
  public void testStack() throws IOException {
    Stack<Object> value = new Stack<Object>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.Stack", type);
  }
  @Test
  public void testTreeMap() throws IOException {
    TreeMap<Object,Object> value = new TreeMap<Object,Object>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.TreeMap", type);
  }
  @Test
  public void testTreeSet() throws IOException {
    TreeSet<Object> value = new TreeSet<Object>();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.TreeSet", type);
  }
  @Test
  public void testBooleanType() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    out.writeByte(DSCODE.BOOLEAN_TYPE);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Boolean.class", type);
  }
  @Test
  public void testCharacterType() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    out.writeByte(DSCODE.CHARACTER_TYPE);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Character.class", type);
  }
  @Test
  public void testByteType() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    out.writeByte(DSCODE.BYTE_TYPE);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Byte.class", type);
  }
  @Test
  public void testShortType() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    out.writeByte(DSCODE.SHORT_TYPE);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Short.class", type);
  }
  @Test
  public void testIntegerType() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    out.writeByte(DSCODE.INTEGER_TYPE);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Integer.class", type);
  }
  @Test
  public void testLongType() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    out.writeByte(DSCODE.LONG_TYPE);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Long.class", type);
  }
  @Test
  public void testFloatType() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    out.writeByte(DSCODE.FLOAT_TYPE);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Float.class", type);
  }
  @Test
  public void testDoubleType() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    out.writeByte(DSCODE.DOUBLE_TYPE);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Double.class", type);
  }
  @Test
  public void testVoidType() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    out.writeByte(DSCODE.VOID_TYPE);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.lang.Void.class", type);
  }
  // TODO:USER_DATA_SERIALIZABLE
  // TODO:USER_DATA_SERIALIZABLE_2
  // TODO:USER_DATA_SERIALIZABLE_4
  @Test
  public void testDataSerializable() throws IOException {
    DataSerializableImpl value = new DataSerializableImpl(new Random());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("com.gemstone.gemfire.DataSerializable:" + DataSerializableImpl.class.getName(), type);
  }
  @Test
  public void testSerializable() throws IOException {
    SerializableClass value = new SerializableClass();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.io.Serializable:" + SerializableClass.class.getName(), type);
  }
  @SuppressWarnings("serial")
  public static class SerializableClass implements Serializable {
  }
  // TODO:PDX
  // TODO:PDX_ENUM
  // TODO:GEMFIRE_ENUM
  // TODO:PDX_INLINE_ENUM
  @Test
  public void testBigInteger() throws IOException {
    BigInteger value = BigInteger.ZERO;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.math.BigInteger", type);
  }
  @Test
  public void testBigDecimal() throws IOException {
    BigDecimal value = BigDecimal.ZERO;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.math.BigDecimal", type);
  }
  @Test
  public void testUUID() throws IOException {
    UUID value = new UUID(Long.MAX_VALUE, Long.MIN_VALUE);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    DataSerializer.writeObject(value, out);
    byte[] bytes = baos.toByteArray();
    String type = DataType.getDataType(bytes);
    assertEquals("java.util.UUID", type);
  }
}
