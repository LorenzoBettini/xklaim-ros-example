package roboticsScenario;

import com.google.common.base.Objects;
import java.util.Random;
import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class RobotController extends KlavaProcess {
  private Locality wheels;
  
  private Locality position;
  
  private Locality battery;
  
  private Locality obstacleAvoidance;
  
  public RobotController(final Locality wheels, final Locality position, final Locality battery, final Locality obstacleAvoidance) {
    super("roboticsScenario.RobotController");
    this.wheels = wheels;
    this.position = position;
    this.battery = battery;
    this.obstacleAvoidance = obstacleAvoidance;
  }
  
  @Override
  public void executeProcess() {
    try {
      final Random rand = new Random();
      out(new Tuple(new Object[] {"control step", "random walking"}), this.self);
      while (true) {
        {
          String stepType = null;
          Tuple _Tuple = new Tuple(new Object[] {"control step", String.class});
          read(_Tuple, this.self);
          stepType = (String) _Tuple.getItem(1);
          boolean _equals = Objects.equal(stepType, "random walking");
          if (_equals) {
            float _nextFloat = rand.nextFloat();
            float _multiply = (_nextFloat * 10);
            final float angularVelocity = (_multiply - 5);
            final double linearVelocity = 1.5;
            out(new Tuple(new Object[] {"velocity", linearVelocity, angularVelocity}), this.obstacleAvoidance);
            Thread.sleep(100);
          } else {
            boolean _equals_1 = Objects.equal(stepType, "low battery");
            if (_equals_1) {
              InputOutput.<String>println("low battery mode");
              Double stationX = null;
              Double stationY = null;
              Tuple _Tuple_1 = new Tuple(new Object[] {"charge station position", Double.class, Double.class});
              read(_Tuple_1, this.battery);
              stationX = (Double) _Tuple_1.getItem(1);
              stationY = (Double) _Tuple_1.getItem(2);
              out(new Tuple(new Object[] {"go to", stationX, stationY}), this.wheels);
              in(new Tuple(new Object[] {"battery charged"}), this.battery);
              in(new Tuple(new Object[] {"control step", "low battery"}), this.self);
              out(new Tuple(new Object[] {"control step", "random walking"}), this.self);
              InputOutput.<String>println("random walking mode");
            } else {
              boolean _equals_2 = Objects.equal(stepType, "victim detected");
              if (_equals_2) {
                out(new Tuple(new Object[] {"velocity", 0.0, 0.0}), this.wheels);
                Double x = null;
                Double y = null;
                Double anyTheta = null;
                Tuple _Tuple_2 = new Tuple(new Object[] {"position", Double.class, Double.class, Double.class});
                read(_Tuple_2, this.position);
                x = (Double) _Tuple_2.getItem(1);
                y = (Double) _Tuple_2.getItem(2);
                anyTheta = (Double) _Tuple_2.getItem(3);
                InputOutput.<String>println(String.format("victim detected at position: %f, %f", x, y));
                return;
              }
            }
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
