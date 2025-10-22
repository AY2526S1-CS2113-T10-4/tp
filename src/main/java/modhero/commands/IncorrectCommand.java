package modhero.commands;

public class IncorrectCommand extends Command {
    private String errorMessage;

    public IncorrectCommand(String errorMessage) {
        assert errorMessage != null && !errorMessage.isEmpty() : "Error message must not be null or empty";
        this.errorMessage = errorMessage;
    }

    @Override
    public CommandResult execute() {
        return new CommandResult(errorMessage);
    }
}
