package roboticsScenario;

import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class LowBatterySensor extends KlavaProcess {
  private Locality controller;
  
  public LowBatterySensor(final Locality controller) {
    super("roboticsScenario.LowBatterySensor");
    this.controller = controller;
  }
  
  @Override
  public void executeProcess() {
    while (true) {
      {
        read(new Tuple(new Object[] {"battery level", 30}), this.self);
        String anyStep = null;
        Tuple _Tuple = new Tuple(new Object[] {"control step", String.class});
        in(_Tuple, this.controller);
        anyStep = (String) _Tuple.getItem(1);
        out(new Tuple(new Object[] {"control step", "low battery"}), this.controller);
        in(new Tuple(new Object[] {"battery full"}), this.self);
        out(new Tuple(new Object[] {"battery charged"}), this.self);
        InputOutput.<String>println("battery charged");
      }
    }
  }
}
