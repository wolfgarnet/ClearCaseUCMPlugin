package net.praqma.hudson.mode.selectors;

import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.hudson.mode.BaselineSelector;

import java.util.List;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 15:05
 */
public class SingleStreamSelector extends BaselineSelector {

    @Override
    public List<Baseline> getValidBaselines() {
        return null;
    }
}
