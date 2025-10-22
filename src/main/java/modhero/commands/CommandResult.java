package modhero.commands;

import modhero.data.modules.Module;

/**
 * Represents the result of executing a command.
 * Contains feedback for the user, optionally a relevant module,
 * and optionally the total number of modules affected.
 */
public class CommandResult {
    private final String feedbackToUser;
    private final Module relevantModule;
    private final int totalModules;

    /**
     * Creates a command result with only feedback.
     *
     * @param feedbackToUser the feedback message to display to the user
     */
    public CommandResult(String feedbackToUser) {
        this.feedbackToUser = feedbackToUser;
        this.relevantModule = null;
        this.totalModules = 0;
    }

    /**
     * Creates a command result with feedback and a relevant module.
     *
     * @param feedbackToUser the feedback message to display to the user
     * @param relevantModule the module relevant to this command
     */
    public CommandResult(String feedbackToUser, Module relevantModule) {
        this.feedbackToUser = feedbackToUser;
        this.relevantModule = relevantModule;
        this.totalModules = 0;
    }

    /**
     * Creates a command result with feedback, a relevant module,
     * and the total number of modules affected.
     *
     * @param feedbackToUser the feedback message to display to the user
     * @param relevantModule the module relevant to this command
     * @param totalModules the total number of modules affected
     */
    public CommandResult(String feedbackToUser, Module relevantModule, int totalModules) {
        this.feedbackToUser = feedbackToUser;
        this.relevantModule = relevantModule;
        this.totalModules = totalModules;
    }

    /** @return the feedback message to display to the user */
    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    /** @return the module relevant to this command, or {@code null} if none */
    public Module getrelevantModule() {
        return relevantModule;
    }

    /** @return the total number of modules affected by this command */
    public int getTotalModules() {
        return totalModules;
    }
}
