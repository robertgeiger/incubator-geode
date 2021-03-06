package com.gemstone.gemfire.internal.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.Logger;

import com.gemstone.gemfire.internal.logging.LogService;
import com.gemstone.gemfire.internal.util.StopWatch;

/**
 * Reads the InputStream per-byte instead of per-line. Uses BufferedReader.ready() 
 * to ensure that calls to read() will not block. Uses continueReadingMillis to
 * continue reading after the Process terminates in order to fully read the last
 * of that Process' output (such as a stack trace).
 * 
 * @author Kirk Lund
 * @since 8.2
 */
public final class NonBlockingProcessStreamReader extends ProcessStreamReader {
  private static final Logger logger = LogService.getLogger();

  /** millis to continue reading after Process terminates in order to fully read the last of its output */
  private final long continueReadingMillis;
  
  protected NonBlockingProcessStreamReader(final Builder builder) {
    super(builder);
    continueReadingMillis = builder.continueReadingMillis;
  }
  
  @Override
  public void run() {
    final boolean isDebugEnabled = logger.isDebugEnabled();
    if (isDebugEnabled) {
      logger.debug("Running {}", this);
    }
    StopWatch continueReading = new StopWatch();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuilder sb = new StringBuilder(); 
      boolean ready = false;
      int ch = 0;
      while (ch != -1) {
        while ((ready = reader.ready()) && (ch = reader.read()) != -1) {
          sb.append((char)ch);
          if ((char)ch == '\n') {
            this.inputListener.notifyInputLine(sb.toString());
            sb = new StringBuilder();
          }
        }
        if (!ready) {
          if (!ProcessUtils.isProcessAlive(process)) {
            if (!continueReading.isRunning()) {
              continueReading.start();
            } else if (continueReading.elapsedTimeMillis() > continueReadingMillis) {
              return;
            }
          }
          Thread.sleep(10);
        }
      }
    } catch (IOException e) {
      if (isDebugEnabled) {
        logger.debug("Failure reading from buffered input stream: {}", e.getMessage(), e);
      }
    } catch (InterruptedException e) {
      if (isDebugEnabled) {
        logger.debug("Interrupted reading from buffered input stream: {}", e.getMessage(), e);
      }
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
        if (isDebugEnabled) {
          logger.debug("Failure closing buffered input stream reader: {}", e.getMessage(), e);
        }
      }
      if (isDebugEnabled) {
        logger.debug("Terminating {}", this);
      }
    }
  }
}
