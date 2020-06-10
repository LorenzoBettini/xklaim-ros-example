package roboticsScenario;

import klava.Tuple;
import klava.topology.KlavaProcess;
import ros.Publisher;
import ros.RosBridge;
import ros.msgs.geometry_msgs.Twist;

@SuppressWarnings("all")
public class WheelsActuator extends KlavaProcess {
  public WheelsActuator() {
    super("roboticsScenario.WheelsActuator");
  }
  
  @Override
  public void executeProcess() {
    final String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";
    final RosBridge bridge = new RosBridge();
    bridge.connect(rosbridgeWebsocketURI, true);
    final Publisher pub = new Publisher("/robot_base_velocity_controller/cmd_vel", "geometry_msgs/Twist", bridge);
    while (true) {
      {
        Double x = null;
        Double z = null;
        Tuple _Tuple = new Tuple(new Object[] {"velocity", Double.class, Double.class});
        in(_Tuple, this.self);
        x = (Double) _Tuple.getItem(1);
        z = (Double) _Tuple.getItem(2);
        final Twist twistMsg = new Twist();
        twistMsg.linear.x = (x).doubleValue();
        twistMsg.angular.z = (z).doubleValue();
        pub.publish(twistMsg);
      }
    }
  }
}
