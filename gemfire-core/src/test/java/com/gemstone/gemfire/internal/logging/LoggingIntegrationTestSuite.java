package com.gemstone.gemfire.internal.logging;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  DistributedSystemLogFileJUnitTest.class,
  LocatorLogFileJUnitTest.class,
  LogServiceIntegrationJUnitTest.class,
  MergeLogFilesJUnitTest.class,
})
public class LoggingIntegrationTestSuite {
}
