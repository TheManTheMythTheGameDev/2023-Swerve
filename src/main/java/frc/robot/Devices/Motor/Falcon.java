package frc.robot.Devices.Motor;

import com.ctre.phoenixpro.configs.CurrentLimitsConfigs;
import com.ctre.phoenixpro.hardware.TalonFX;

import frc.robot.Devices.AnyMotor;

/**
 * The Falcon class extends the AnyMotor abstract class to provide an interface
 * to control a Talon FX motor controller (also known as a Falcon 500).
 */
public class Falcon extends AnyMotor {
    private TalonFX falcon; // The Talon FX motor controller object.
    double stallVolt;       // The voltage at which the motor is considered to be stalling.

    final int id;           // Unique identifier for the motor controller.

    /**
     * Retrieves the ID of the motor controller.
     * @return The CAN ID of the motor controller.
     */
    public int getID() {
        return id;
    }

    /**
     * Sets the current limit for the motor.
     * @param amps The maximum current in Amperes.
     */
    public void setCurrentLimit(int amps) {
        var config = falcon.getConfigurator();
        var currentConfig = new CurrentLimitsConfigs();
        currentConfig.SupplyCurrentLimit = amps;
        config.apply(currentConfig);
    }

    /**
     * Constructor for the Falcon motor controller.
     * @param deviceNumber The CAN ID for the motor controller.
     * @param isReversed Indicates whether the motor output should be reversed.
     * @param isStallable Indicates whether the motor should have stall voltage applied.
     */
    public Falcon(int deviceNumber, boolean isReversed, boolean isStallable) {
        super(isReversed);

        this.id = deviceNumber;

        this.falcon = new TalonFX(deviceNumber);
        falcon.setInverted(false);
        this.stallVolt = isStallable ? 3 : 0; // Set stall voltage if the motor is stallable.
    }

    /**
     * Overloaded constructor for the Falcon motor controller without stallable parameter.
     * @param deviceNumber The CAN ID for the motor controller.
     * @param isReversed Indicates whether the motor output should be reversed.
     */
    public Falcon(int deviceNumber, boolean isReversed) {
        this(deviceNumber, isReversed, false);
    }

    /**
     * Sets the voltage output of the motor, taking into account stall voltage.
     * @param volts The desired voltage.
     */
    protected void uSetVoltage(double volts) {
        double fac = (volts > 0) ? 1 : -1; // Determine the direction of the voltage.
        if (Math.abs(volts) < stallVolt / 2) {
            falcon.setVoltage(0); // If voltage is below half stall, turn off motor.
            return;
        } else if (Math.abs(volts) < stallVolt) {
            falcon.setVoltage(stallVolt * fac); // Apply stall voltage if voltage is below stall level.
            return;
        }

        falcon.setVoltage(volts); // Apply the full voltage if above stall level.
    }

    /**
     * Retrieves the number of revolutions from the motor's integrated sensor.
     * @return The position of the encoder in revolutions.
     */
    protected double uGetRevs() {
        return falcon.getPosition().getValue();
    }

    /**
     * Stops the motor immediately by cutting power.
     */
    public void stop() {
        falcon.stopMotor();
    }
}
