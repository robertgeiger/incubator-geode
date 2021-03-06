package demo;

import io.pivotal.gemfire.spark.connector.GemFireConnectionConf;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import java.util.*;

import static io.pivotal.gemfire.spark.connector.javaapi.GemFireJavaUtil.*;

/**
 * This Spark application demonstrates how to save a RDD to GemFire using GemFire Spark 
 * Connector with Java.
 * <p/>
 * In order to run it, you will need to start GemFire cluster, and create the following region
 * with GFSH:
 * <pre>
 * gfsh> create region --name=str_str_region --type=REPLICATE \
 *         --key-constraint=java.lang.String --value-constraint=java.lang.String
 * </pre>
 * 
 * Once you compile and package the demo, the jar file basic-demos_2.10-0.5.0.jar
 * should be generated under gemfire-spark-demos/basic-demos/target/scala-2.10/. 
 * Then run the following command to start a Spark job:
 * <pre>
 *   <path to spark>/bin/spark-submit --master=local[2] --class demo.PairRDDSaveJavaDemo \
 *       <path to>/basic-demos_2.10-0.5.0.jar <locator host>:<port>
 * </pre>
 * 
 * Verify the data was saved to GemFire with GFSH:
 * <pre>gfsh> query --query="select * from /str_str_region.entrySet"  </pre>
 */
public class PairRDDSaveJavaDemo {

  public static void main(String[] argv) {

    if (argv.length != 1) {
      System.err.printf("Usage: PairRDDSaveJavaDemo <locators>\n");
      return;
    }

    SparkConf conf = new SparkConf().setAppName("PairRDDSaveJavaDemo");
    conf.set(GemFireLocatorPropKey, argv[0]);
    JavaSparkContext sc = new JavaSparkContext(conf);
    GemFireConnectionConf connConf = GemFireConnectionConf.apply(conf);

    List<Tuple2<String, String>> data = new ArrayList<>();
    data.add(new Tuple2<>("7", "seven"));
    data.add(new Tuple2<>("8", "eight"));
    data.add(new Tuple2<>("9", "nine"));

    List<Tuple2<String, String>> data2 = new ArrayList<Tuple2<String, String>>();
    data2.add(new Tuple2<>("11", "eleven"));
    data2.add(new Tuple2<>("12", "twelve"));
    data2.add(new Tuple2<>("13", "thirteen"));

    // method 1: generate JavaPairRDD directly
    JavaPairRDD<String, String> rdd1 =  sc.parallelizePairs(data);
    javaFunctions(rdd1).saveToGemfire("str_str_region", connConf);

    // method 2: convert JavaRDD<Tuple2<K,V>> to JavaPairRDD<K, V>
    JavaRDD<Tuple2<String, String>> rdd2 =  sc.parallelize(data2);
    javaFunctions(toJavaPairRDD(rdd2)).saveToGemfire("str_str_region", connConf);
       
    sc.stop();
  }

}
