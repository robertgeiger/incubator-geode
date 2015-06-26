package com.gemstone.gemfire.internal.redis;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

/**
 * This class is a wrapper for the any Regions that need to store a 
 * byte[]. The only data this an instance will store is a byte[]
 * for the data but it is also serializable and comparable so it is able to be used
 * in querying. The hash code and to string variant are created lazily
 * 
 * @author Vitaliy Gavrilov
 *
 */
public class ByteArrayWrapper implements DataSerializable, Comparable<ByteArrayWrapper> {
  /**
   * Generated serialVerionUID
   */
  private static final long serialVersionUID = 9066391742266642992L;

  /**
   * The data portion of ValueWrapper
   */
  private byte[] value;

  /**
   * Hash of {@link #value}, this value is cached for performance
   */
  private transient int hashCode;

  private transient String toString;

  /**
   * Empty constructor for serialization
   */
  public ByteArrayWrapper() {
  }

  /**
   * Default constructor constructs a ValueWrapper
   * and initialize the {@link #value}
   * 
   * @param value
   */
  public ByteArrayWrapper(byte[] value) {
    this.value = value;
    this.hashCode = Arrays.hashCode(value);
  }

  @Override
  public void toData(DataOutput out) throws IOException {
    DataSerializer.writeByteArray(value, out);
  }

  @Override
  public void fromData(DataInput in) throws IOException, ClassNotFoundException {
    this.value = DataSerializer.readByteArray(in);;
    this.hashCode = Arrays.hashCode(this.value);
  }

  @Override
  public String toString() {
    if (toString == null)
      toString = Coder.bytesToString(this.value);
    return toString;
  }

  public byte[] toBytes() {
    return this.value;
  }

  public void setBytes(byte[] bytes) {
    this.value = bytes;
    this.toString = null;
    this.hashCode = Arrays.hashCode(bytes);
  }

  /**
   * Getter for the length of the {@link #value} array
   * @return The length of the value array
   */
  public int length() {
    return value.length;
  }

  /**
   * Hash code for byte[] wrapped by this object,
   * the actual hashcode is determined by Arrays.hashCode(byte[])
   */
  @Override
  public int hashCode() {
    return this.hashCode;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof ByteArrayWrapper)
      return Arrays.equals(value, ((ByteArrayWrapper) other).value);
    else if (other instanceof String) {
      return Arrays.equals(value, Coder.stringToBytes((String) other));
    }
    return false;
  }

  /**
   * This is a byte to byte comparator, it is not lexicographical but purely compares
   * byte values
   */
  @Override
  public int compareTo(ByteArrayWrapper other) {
    return arrayCmp(value, other.value);

  }

  /**
   * Private helper method to compare two byte arrays, A.compareTo(B). The comparison
   * is basically numerical, for each byte index, the byte representing the greater
   * value will be the greater
   * 
   * @param A byte[]
   * @param B byte[]
   * @return 1 if A > B, -1 if B > A, 0 if A == B
   */
  private int arrayCmp(byte[] A, byte[] B) {
    if (A == B)
      return 0;
    if (A == null) {
      return -1;
    } else if (B == null) {
      return 1;
    }

    int len = Math.min(A.length, B.length);

    for (int i = 0; i < len; i++) {
      byte a = A[i];
      byte b = B[i];
      int diff = a - b;
      if (diff > 0)
        return 1;
      else if (diff < 0)
        return -1;
    }

    if (A.length > B.length)
      return 1;
    else if (B.length > A.length)
      return -1;

    return 0;
  }

}