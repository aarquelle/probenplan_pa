package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.ui.cli.CancelCommandException;

public abstract class AbstractCommand {
    private final String name;
    private final String description;

    public AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public AbstractCommand(String name) {
        this(name, "");
    }

    public abstract void execute(String[] args) throws BusinessException;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
