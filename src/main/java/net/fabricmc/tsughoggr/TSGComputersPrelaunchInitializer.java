package net.fabricmc.tsughoggr;
import btw.block.model.BlockModel;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import btw.community.tsughoggr.computers.ComputersMod;

public class TSGComputersPrelaunchInitializer implements PreLaunchEntrypoint {
    /**
     * Runs the PreLaunch entrypoint to register BTW-Addon.
     * Don't initialize anything else here, use
     * the method Initialize() in the Addon.
     */
    @Override
    public void onPreLaunch() {
        ComputersMod.getInstance();
    }
}
