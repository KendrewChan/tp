package seedu.address.model.menu;

import javafx.collections.ObservableList;
import seedu.address.model.food.Food;

/**
 * Unmodifiable view of a menu
 */
public interface ReadOnlyMenuManager {

    /**
     * Returns an unmodifiable view of the foods list.
     * This list will not contain any duplicate foods.
     */
    ObservableList<Food> getFoodList();

}
