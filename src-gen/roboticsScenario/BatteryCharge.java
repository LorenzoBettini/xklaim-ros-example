package roboticsScenario;

import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

/**
 * This process simulates the charge of the robot's battery
 */
@SuppressWarnings("all")
public class BatteryCharge extends KlavaProcess {
  private Locality position;
  
  public BatteryCharge(final Locality position) {
    super("roboticsScenario.BatteryCharge");
    this.position = position;
  }
  
  @Override
  public void executeProcess() {
    try {
      final double stationPosX = 5.0;
      final double stationPosY = 4.5;
      out(new Tuple(new Object[] {"charge station position", stationPosX, stationPosY}), this.self);
      while (true) {
        {
          in(new Tuple(new Object[] {"arrived at destination", stationPosX, stationPosY}), this.position);
          InputOutput.<String>println("Robot\'s under charge...");
          Integer currentBattery = null;
          Tuple _Tuple = new Tuple(new Object[] {"battery level", Integer.class});
          in(_Tuple, this.self);
          currentBattery = (Integer) _Tuple.getItem(1);
          Thread.sleep(3000);
          out(new Tuple(new Object[] {"battery level", 100}), this.self);
          out(new Tuple(new Object[] {"battery full"}), this.self);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
