package uk.nhs.digital.ps.migrator.misc;

import org.springframework.boot.ExitCodeGenerator;

public class MigratorExitCodeGenerator implements ExitCodeGenerator {

    boolean isRunFailed = false;

    @Override
    public int getExitCode() {
        return isRunFailed ? 1 : 0;
    }

    public void markRunAsFailed() {
        isRunFailed = true;
    }

    public boolean isRunFailed() {
        return isRunFailed;
    }
}
