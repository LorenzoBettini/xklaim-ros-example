package roboticsScenario;

import klava.Tuple;
import klava.topology.KlavaProcess;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

/**
 * This process simulates the consumption of the robot's battery
 */
@SuppressWarnings("all")
public class BatteryConsumption extends KlavaProcess {
  public BatteryConsumption() {
    super("roboticsScenario.BatteryConsumption");
  }
  
  @Override
  public void executeProcess() {
    try {
      int batteryLevel = 35;
      out(new Tuple(new Object[] {"battery level", batteryLevel}), this.self);
      while ((batteryLevel > 0)) {
        {
          Thread.sleep(1000);
          Integer currentBattery = null;
          Tuple _Tuple = new Tuple(new Object[] {"battery level", Integer.class});
          in(_Tuple, this.self);
          currentBattery = (Integer) _Tuple.getItem(1);
          batteryLevel = ((currentBattery).intValue() - 1);
          out(new Tuple(new Object[] {"battery level", batteryLevel}), this.self);
        }
      }
      InputOutput.<String>println("The robot\'s battery is empty!");
      System.exit(0);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
