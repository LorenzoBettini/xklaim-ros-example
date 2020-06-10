package roboticsScenario;

import com.fasterxml.jackson.databind.JsonNode;
import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import ros.RosBridge;
import ros.RosListenDelegate;
import ros.SubscriptionRequestMsg;

@SuppressWarnings("all")
public class PositionSensor extends KlavaProcess {
  public PositionSensor() {
    super("roboticsScenario.PositionSensor");
  }
  
  @Override
  public void executeProcess() {
    final String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";
    final RosBridge bridge = new RosBridge();
    bridge.connect(rosbridgeWebsocketURI, true);
    out(new Tuple(new Object[] {"position", 5.0, 4.5, 0.0}), this.self);
    final Locality myself = this.self;
    final RosListenDelegate _function = (JsonNode data, String stringRep) -> {
      final double x = data.get("msg").get("pose").get("pose").get("position").get("x").asDouble();
      final double y = data.get("msg").get("pose").get("pose").get("position").get("y").asDouble();
      final double qx = data.get("msg").get("pose").get("pose").get("orientation").get("x").asDouble();
      final double qy = data.get("msg").get("pose").get("pose").get("orientation").get("y").asDouble();
      final double qz = data.get("msg").get("pose").get("pose").get("orientation").get("z").asDouble();
      final double qw = data.get("msg").get("pose").get("pose").get("orientation").get("w").asDouble();
      final double siny_cosp = (2 * ((qw * qz) + (qx * qy)));
      final double cosy_cosp = (1 - (2 * ((qy * qy) + (qz * qz))));
      final double theta = Math.atan2(siny_cosp, cosy_cosp);
      Double anyX = null;
      Double anyY = null;
      Double anyTheta = null;
      Tuple _Tuple = new Tuple(new Object[] {"position", Double.class, Double.class, Double.class});
      in(_Tuple, myself);
      anyX = (Double) _Tuple.getItem(1);
      anyY = (Double) _Tuple.getItem(2);
      anyTheta = (Double) _Tuple.getItem(3);
      out(new Tuple(new Object[] {"position", x, y, theta}), myself);
    };
    bridge.subscribe(
      SubscriptionRequestMsg.generate("/robot_base_velocity_controller/odom").setType("nav_msgs/Odometry").setThrottleRate(Integer.valueOf(1)).setQueueLength(Integer.valueOf(1)), _function);
  }
}
