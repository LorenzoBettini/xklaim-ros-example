package roboticsScenario;

import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class GoToActuator extends KlavaProcess {
  private Locality position;
  
  public GoToActuator(final Locality position) {
    super("roboticsScenario.GoToActuator");
    this.position = position;
  }
  
  @Override
  public void executeProcess() {
    try {
      final double distanceTolerance = 0.3;
      while (true) {
        {
          Double destinationX = null;
          Double destinationY = null;
          Tuple _Tuple = new Tuple(new Object[] {"go to", Double.class, Double.class});
          in(_Tuple, this.self);
          destinationX = (Double) _Tuple.getItem(1);
          destinationY = (Double) _Tuple.getItem(2);
          InputOutput.<String>println(String.format("go to: %f, %f", destinationX, destinationY));
          Double startingX = null;
          Double startingY = null;
          Double anyTheta = null;
          Tuple _Tuple_1 = new Tuple(new Object[] {"position", Double.class, Double.class, Double.class});
          read(_Tuple_1, this.position);
          startingX = (Double) _Tuple_1.getItem(1);
          startingY = (Double) _Tuple_1.getItem(2);
          anyTheta = (Double) _Tuple_1.getItem(3);
          double _minus = DoubleExtensions.operator_minus(startingX, destinationX);
          double _pow = Math.pow(_minus, 2);
          double _minus_1 = DoubleExtensions.operator_minus(startingY, destinationY);
          double _pow_1 = Math.pow(_minus_1, 2);
          double _plus = (_pow + _pow_1);
          double distance = Math.sqrt(_plus);
          while ((distance >= distanceTolerance)) {
            {
              double linearVelocity = 1.5;
              if ((distance < 1)) {
                double _linearVelocity = linearVelocity;
                linearVelocity = (_linearVelocity * distance);
              }
              Double x = null;
              Double y = null;
              Double theta = null;
              Tuple _Tuple_2 = new Tuple(new Object[] {"position", Double.class, Double.class, Double.class});
              read(_Tuple_2, this.position);
              x = (Double) _Tuple_2.getItem(1);
              y = (Double) _Tuple_2.getItem(2);
              theta = (Double) _Tuple_2.getItem(3);
              double _minus_2 = DoubleExtensions.operator_minus(x, destinationX);
              double _pow_2 = Math.pow(_minus_2, 2);
              double _minus_3 = DoubleExtensions.operator_minus(y, destinationY);
              double _pow_3 = Math.pow(_minus_3, 2);
              double _plus_1 = (_pow_2 + _pow_3);
              distance = Math.sqrt(_plus_1);
              double _minus_4 = DoubleExtensions.operator_minus(destinationY, y);
              double _minus_5 = DoubleExtensions.operator_minus(destinationX, x);
              final double steeringAngle = Math.atan2(_minus_4, _minus_5);
              final double angularVelocity = (1.0 * (steeringAngle - (theta).doubleValue()));
              out(new Tuple(new Object[] {"velocity", linearVelocity, angularVelocity}), this.self);
              Thread.sleep(100);
            }
          }
          out(new Tuple(new Object[] {"arrived at destination", destinationX, destinationY}), this.position);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
