package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undoes the last change to the order.";

    public static final String MESSAGE_UNDO_SUCCESS = "Successfully undone last change.";
    public static final String MESSAGE_UNDO_EMPTY = "No changes left to undo.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (model.getOrderHistorySize() <= 1) {
            return new CommandResult(MESSAGE_UNDO_EMPTY);
        }
        model.undoOrder();
        return new CommandResult(MESSAGE_UNDO_SUCCESS);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
