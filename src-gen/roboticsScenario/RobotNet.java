package roboticsScenario;

import klava.LogicalLocality;
import klava.PhysicalLocality;
import klava.topology.ClientNode;
import klava.topology.KlavaNodeCoordinator;
import klava.topology.LogicalNet;
import org.mikado.imc.common.IMCException;
import roboticsScenario.BatteryCharge;
import roboticsScenario.BatteryConsumption;
import roboticsScenario.CalculateOrientation;
import roboticsScenario.GoToActuator;
import roboticsScenario.LaserSensor;
import roboticsScenario.LowBatterySensor;
import roboticsScenario.PositionSensor;
import roboticsScenario.RobotController;
import roboticsScenario.VictimSensor;
import roboticsScenario.WheelsActuator;

@SuppressWarnings("all")
public class RobotNet extends LogicalNet {
  private static final LogicalLocality Position = new LogicalLocality("Position");
  
  private static final LogicalLocality Wheels = new LogicalLocality("Wheels");
  
  private static final LogicalLocality Controller = new LogicalLocality("Controller");
  
  private static final LogicalLocality Victim = new LogicalLocality("Victim");
  
  private static final LogicalLocality Battery = new LogicalLocality("Battery");
  
  private static final LogicalLocality ObstacleAvoidance = new LogicalLocality("ObstacleAvoidance");
  
  public static class Position extends ClientNode {
    private static class PositionProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        PositionSensor _positionSensor = new PositionSensor();
        eval(_positionSensor, this.self);
      }
    }
    
    public Position() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("Position"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new RobotNet.Position.PositionProcess());
    }
  }
  
  public static class Wheels extends ClientNode {
    private static class WheelsProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        WheelsActuator _wheelsActuator = new WheelsActuator();
        eval(_wheelsActuator, this.self);
        GoToActuator _goToActuator = new GoToActuator(RobotNet.Position);
        eval(_goToActuator, this.self);
      }
    }
    
    public Wheels() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("Wheels"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new RobotNet.Wheels.WheelsProcess());
    }
  }
  
  public static class Controller extends ClientNode {
    private static class ControllerProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        RobotController _robotController = new RobotController(RobotNet.Wheels, RobotNet.Position, RobotNet.Battery, RobotNet.ObstacleAvoidance);
        eval(_robotController, this.self);
      }
    }
    
    public Controller() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("Controller"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new RobotNet.Controller.ControllerProcess());
    }
  }
  
  public static class Victim extends ClientNode {
    private static class VictimProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        VictimSensor _victimSensor = new VictimSensor(RobotNet.Controller, RobotNet.Position);
        eval(_victimSensor, this.self);
      }
    }
    
    public Victim() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("Victim"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new RobotNet.Victim.VictimProcess());
    }
  }
  
  public static class Battery extends ClientNode {
    private static class BatteryProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        LowBatterySensor _lowBatterySensor = new LowBatterySensor(RobotNet.Controller);
        eval(_lowBatterySensor, this.self);
        BatteryConsumption _batteryConsumption = new BatteryConsumption();
        eval(_batteryConsumption, this.self);
        BatteryCharge _batteryCharge = new BatteryCharge(RobotNet.Position);
        eval(_batteryCharge, this.self);
      }
    }
    
    public Battery() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("Battery"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new RobotNet.Battery.BatteryProcess());
    }
  }
  
  public static class ObstacleAvoidance extends ClientNode {
    private static class ObstacleAvoidanceProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        LaserSensor _laserSensor = new LaserSensor();
        eval(_laserSensor, this.self);
        CalculateOrientation _calculateOrientation = new CalculateOrientation(RobotNet.Wheels);
        eval(_calculateOrientation, this.self);
      }
    }
    
    public ObstacleAvoidance() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("ObstacleAvoidance"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new RobotNet.ObstacleAvoidance.ObstacleAvoidanceProcess());
    }
  }
  
  public RobotNet() throws IMCException {
    super(new PhysicalLocality("localhost:9999"));
  }
  
  public void addNodes() throws IMCException {
    RobotNet.Position position = new RobotNet.Position();
    RobotNet.Wheels wheels = new RobotNet.Wheels();
    RobotNet.Controller controller = new RobotNet.Controller();
    RobotNet.Victim victim = new RobotNet.Victim();
    RobotNet.Battery battery = new RobotNet.Battery();
    RobotNet.ObstacleAvoidance obstacleAvoidance = new RobotNet.ObstacleAvoidance();
    position.addMainProcess();
    wheels.addMainProcess();
    controller.addMainProcess();
    victim.addMainProcess();
    battery.addMainProcess();
    obstacleAvoidance.addMainProcess();
  }
}
