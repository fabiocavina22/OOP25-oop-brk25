package it.unibo.breakout.model.api;

import java.util.List;
import it.unibo.breakout.model.impl.PowerUpImpl;

/**
 * Read-only view of the power up manager, exposing the active capsules for rendering.
 */
public interface PowerUpView {

    /**
     * Returns the currently active power up capsules.
     *
     * @return the list of active capsules
     */
    List<PowerUpImpl> getActivePowerUp();
}
