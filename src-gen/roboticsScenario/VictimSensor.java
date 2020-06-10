package roboticsScenario;

import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;

@SuppressWarnings("all")
public class VictimSensor extends KlavaProcess {
  private Locality controller;
  
  private Locality position;
  
  public VictimSensor(final Locality controller, final Locality position) {
    super("roboticsScenario.VictimSensor");
    this.controller = controller;
    this.position = position;
  }
  
  @Override
  public void executeProcess() {
    final double victimX = 5.5;
    final double victimY = 7.0;
    final double distanceTolerance = 0.4;
    boolean victimFound = false;
    while ((!victimFound)) {
      {
        Double x = null;
        Double y = null;
        Double anyTheta = null;
        Tuple _Tuple = new Tuple(new Object[] {"position", Double.class, Double.class, Double.class});
        read(_Tuple, this.position);
        x = (Double) _Tuple.getItem(1);
        y = (Double) _Tuple.getItem(2);
        anyTheta = (Double) _Tuple.getItem(3);
        double _pow = Math.pow(((x).doubleValue() - victimX), 2);
        double _pow_1 = Math.pow(((y).doubleValue() - victimY), 2);
        double _plus = (_pow + _pow_1);
        final double distance = Math.sqrt(_plus);
        if ((distance <= distanceTolerance)) {
          victimFound = true;
          String stepType = null;
          Tuple _Tuple_1 = new Tuple(new Object[] {"control step", String.class});
          in(_Tuple_1, this.controller);
          stepType = (String) _Tuple_1.getItem(1);
          out(new Tuple(new Object[] {"control step", "victim detected"}), this.controller);
        }
      }
    }
  }
}
