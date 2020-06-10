package roboticsScenario;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import org.eclipse.xtext.xbase.lib.Exceptions;
import roboticsScenario.LaserScan;
import ros.RosBridge;
import ros.RosListenDelegate;
import ros.SubscriptionRequestMsg;

@SuppressWarnings("all")
public class LaserSensor extends KlavaProcess {
  public LaserSensor() {
    super("roboticsScenario.LaserSensor");
  }
  
  @Override
  public void executeProcess() {
    final String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";
    final RosBridge bridge = new RosBridge();
    bridge.connect(rosbridgeWebsocketURI, true);
    out(new Tuple(new Object[] {"laser", 0.0, 0.0, 0.0, 0.0, 0.0}), this.self);
    final Locality myself = this.self;
    final RosListenDelegate _function = (JsonNode data, String stringRep) -> {
      try {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rosMsgNode = data.get("msg");
        LaserScan laserscan = mapper.<LaserScan>treeToValue(rosMsgNode, LaserScan.class);
        double range_center = laserscan.ranges[359];
        double range_left = laserscan.ranges[180];
        double range_right = laserscan.ranges[540];
        double range_left_last = laserscan.ranges[20];
        double range_right_last = laserscan.ranges[700];
        Double anyC = null;
        Double anyL = null;
        Double anyR = null;
        Double anyLL = null;
        Double anyRL = null;
        Tuple _Tuple = new Tuple(new Object[] {"laser", Double.class, Double.class, Double.class, Double.class, Double.class});
        in(_Tuple, myself);
        anyC = (Double) _Tuple.getItem(1);
        anyL = (Double) _Tuple.getItem(2);
        anyR = (Double) _Tuple.getItem(3);
        anyLL = (Double) _Tuple.getItem(4);
        anyRL = (Double) _Tuple.getItem(5);
        out(new Tuple(new Object[] {"laser", range_center, range_left, range_right, range_left_last, range_right_last}), myself);
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    bridge.subscribe(
      SubscriptionRequestMsg.generate("/scan").setType("sensor_msgs/LaserScan").setThrottleRate(Integer.valueOf(1)).setQueueLength(Integer.valueOf(1)), _function);
  }
}
