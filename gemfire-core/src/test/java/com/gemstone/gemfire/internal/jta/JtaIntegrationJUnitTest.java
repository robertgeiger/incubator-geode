package com.gemstone.gemfire.internal.jta;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionFactory;
import com.gemstone.gemfire.cache.RegionShortcut;
import com.gemstone.gemfire.distributed.internal.InternalDistributedSystem;
import com.gemstone.gemfire.internal.logging.LogService;
import com.gemstone.gemfire.test.junit.categories.IntegrationTest;

/**
 * Moved some non-DUnit tests over from com/gemstone/gemfire/internal/jta/dunit/JTADUnitTest
 * 
 * @author Kirk Lund
 */
@Category(IntegrationTest.class)
public class JtaIntegrationJUnitTest {

  private static final Logger logger = LogService.getLogger();
  
  @After
  public void tearDown() {
    InternalDistributedSystem ids = InternalDistributedSystem.getAnyInstance();
    if (ids != null) {
      ids.disconnect();
    }
  }
  
  @Test
  public void testBug43987() {
    //InternalDistributedSystem ds = getSystem(); // ties us in to the DS owned by DistributedTestCase.
    CacheFactory cf = new CacheFactory().set("mcast-port", "0");//(ds.getProperties());
    Cache cache = cf.create(); // should just reuse the singleton DS owned by DistributedTestCase.
    RegionFactory<String, String> rf = cache.createRegionFactory(RegionShortcut.REPLICATE);
    Region<String, String> r = rf.create("JTA_reg");
    r.put("key", "value");
    cache.close();
    cache = cf.create();
    RegionFactory<String, String> rf1 = cache.createRegionFactory(RegionShortcut.REPLICATE);
    Region<String, String> r1 = rf1.create("JTA_reg");
    r1.put("key1", "value");
  }
  
  @Test
  public void testBug46169() throws Exception {
    String tableName = CacheUtils.init("CacheTest");
    assertFalse(tableName == null || tableName.equals(""));
    logger.debug("Table name: " + tableName);

    logger.debug("init for bug46169 Successful!");
    Cache cache = CacheUtils.getCache();
    
    TransactionManager xmanager = (TransactionManager) cache.getJNDIContext().lookup("java:/TransactionManager");
    assertNotNull(xmanager);
    
    Transaction trans = xmanager.suspend();
    assertNull(trans);

    try {
      logger.debug("Destroying table: " + tableName);
      CacheUtils.destroyTable(tableName);
      logger.debug("Closing cache...");
      logger.debug("destroyTable for bug46169 Successful!");
    } finally {
      CacheUtils.closeCache();
    }
  }

  @Test
  public void testBug46192() throws Exception {
    String tableName = CacheUtils.init("CacheTest");
    assertFalse(tableName == null || tableName.equals(""));
    logger.debug("Table name: " + tableName);
    
    logger.debug("init for bug46192 Successful!");
    Cache cache = CacheUtils.getCache();
    
    TransactionManager xmanager = (TransactionManager) cache.getJNDIContext().lookup("java:/TransactionManager");
    assertNotNull(xmanager);
    
    try {
      xmanager.rollback();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException expected) {
      // passed
    }
    
    try {
      xmanager.commit();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException expected) {
      // passed
    }

    try {
      logger.debug("Destroying table: " + tableName);
      CacheUtils.destroyTable(tableName);
      logger.debug("Closing cache...");
      logger.debug("destroyTable for bug46192 Successful!");
    } finally {
      CacheUtils.closeCache();
    }
  }
}
