package com.gemstone.gemfire.internal.offheap;

import com.gemstone.gemfire.Statistics;

/**
 * Null implementation of OffHeapMemoryStats for testing.
 *  
 * @author Kirk Lund
 */
public class NullOffHeapMemoryStats implements OffHeapMemoryStats {

  public void incFreeMemory(long value) {
  }
  public void incMaxMemory(long value) {
  }
  public void incUsedMemory(long value) {
  }
  public void incSlabSize(long value) {
  }
  public void incObjects(int value) {
  }

  public long getFreeMemory() {
    return 0;
  }
  public long getMaxMemory() {
    return 0;
  }
  public long getUsedMemory() {
    return 0;
  }
  public long getSlabSize() {
    return 0;
  }
  public int getObjects() {
    return 0;
  }
  @Override
  public void incReads() {
  }
  @Override
  public long getReads() {
    return 0;
  }
  @Override
  public int getCompactions() {
    return 0;
  }
  @Override
  public void setFragments(long value) {
  }
  @Override
  public long getFragments() {
    return 0;
  }
  @Override
  public void setLargestFragment(int value) {
  }
  @Override
  public int getLargestFragment() {
    return 0;
  }
  @Override
  public long startCompaction() {
    return 0;
  }
  @Override
  public void endCompaction(long start) {
  }
  @Override
  public void setFragmentation(int value) {
  }
  @Override
  public int getFragmentation() {
    return 0;
  }
  @Override
  public Statistics getStats() {
    return null;
  }
  @Override
  public long getCompactionTime() {
    return 0;
  }
  @Override
  public void close() {
  }
  @Override
  public void initialize(OffHeapMemoryStats stats) {
    stats.close();
  }     
}
