package roboticsScenario;

import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;

@SuppressWarnings("all")
public class CalculateOrientation extends KlavaProcess {
  private Locality wheels;
  
  public CalculateOrientation(final Locality wheels) {
    super("roboticsScenario.CalculateOrientation");
    this.wheels = wheels;
  }
  
  @Override
  public void executeProcess() {
    final double normal_vel = 0.9;
    final double normal_angel_turn = 2.57284;
    final int last_ob_vel = 0;
    final double last_ob_angel = 0.21845;
    final double half_angel = 1.57284;
    while (true) {
      {
        Double x = null;
        Double z = null;
        Tuple _Tuple = new Tuple(new Object[] {"velocity", Double.class, Double.class});
        in(_Tuple, this.self);
        x = (Double) _Tuple.getItem(1);
        z = (Double) _Tuple.getItem(2);
        Double range_center = null;
        Double range_left = null;
        Double range_right = null;
        Double range_left_last = null;
        Double range_right_last = null;
        Tuple _Tuple_1 = new Tuple(new Object[] {"laser", Double.class, Double.class, Double.class, Double.class, Double.class});
        read(_Tuple_1, this.self);
        range_center = (Double) _Tuple_1.getItem(1);
        range_left = (Double) _Tuple_1.getItem(2);
        range_right = (Double) _Tuple_1.getItem(3);
        range_left_last = (Double) _Tuple_1.getItem(4);
        range_right_last = (Double) _Tuple_1.getItem(5);
        double linearx = 0.0;
        double angularz = 0.0;
        if (((((((range_center).doubleValue() == 0.0) && ((range_right).doubleValue() == 0.0)) && ((range_left).doubleValue() == 0.0)) && ((range_right_last).doubleValue() == 0.0)) && ((range_left_last).doubleValue() == 0.0))) {
          out(new Tuple(new Object[] {"velocity", x, z}), this.wheels);
        } else {
          if (((range_center).doubleValue() == 0.0)) {
            linearx = normal_vel;
            if ((((range_left_last).doubleValue() != 0.0) && ((range_left_last).doubleValue() < 0.2))) {
              linearx = normal_vel;
              angularz = normal_angel_turn;
            } else {
              if ((((range_right_last).doubleValue() != 0.0) && ((range_right_last).doubleValue() < 0.2))) {
                linearx = normal_vel;
                angularz = (-normal_angel_turn);
              }
            }
          } else {
            if ((((range_left_last).doubleValue() == 0.0) || ((range_left).doubleValue() == 0.0))) {
              linearx = 0;
              angularz = (-normal_angel_turn);
            } else {
              if ((((range_right_last).doubleValue() == 0.0) || ((range_right).doubleValue() == 0.0))) {
                linearx = 0;
                angularz = normal_angel_turn;
              } else {
                boolean _greaterThan = (range_left.compareTo(range_right) > 0);
                if (_greaterThan) {
                  linearx = 0;
                  angularz = (-normal_angel_turn);
                } else {
                  linearx = normal_vel;
                  angularz = normal_angel_turn;
                }
              }
            }
          }
          out(new Tuple(new Object[] {"velocity", linearx, angularz}), this.wheels);
        }
      }
    }
  }
}
