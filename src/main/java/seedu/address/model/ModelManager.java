package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.food.Food;
import seedu.address.model.menu.MenuManager;
import seedu.address.model.menu.ReadOnlyMenuManager;
import seedu.address.model.order.OrderItem;
import seedu.address.model.order.OrderManager;
import seedu.address.model.order.ReadOnlyOrderManager;
import seedu.address.model.vendor.Vendor;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final List<MenuManager> menuManagers;
    private final OrderManager orderManager;

    private final UserPrefs userPrefs;
    // TODO: remove filteredvendors and orderitems
    private final FilteredList<Vendor> filteredVendors;
    private FilteredList<Food> filteredFoods;
    private FilteredList<OrderItem> filteredOrderItems;

    private boolean changeVendor = false;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.menuManagers = new ArrayList<>();
        this.orderManager = new OrderManager();

        this.userPrefs = new UserPrefs(userPrefs);
        this.filteredVendors = new FilteredList<>(this.addressBook.getVendorList());
    }

    /**
     * Initializes a ModelManager with the given addressBook, userPrefs, menuManager and orderManager.
     */
    public ModelManager(
            ReadOnlyAddressBook addressBook,
            ReadOnlyUserPrefs userPrefs,
            List<MenuManager> menuManagers,
            OrderManager orderManager
    ) {
        super();
        requireAllNonNull(addressBook, userPrefs, menuManagers, orderManager);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.menuManagers = menuManagers;
        this.orderManager = orderManager;
        filteredVendors = new FilteredList<>(this.addressBook.getVendorList());
    }

    /**
     * Initializes a ModelManager with the given addressBook, userPrefs, menuManager and orderManager.
     */
    public ModelManager(
            ReadOnlyAddressBook addressBook,
            ReadOnlyUserPrefs userPrefs,
            List<MenuManager> menuManagers,
            OrderManager orderManager, Vendor vendor
    ) {
        super();
        requireAllNonNull(addressBook, userPrefs, menuManagers, orderManager);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.menuManagers = menuManagers;
        this.orderManager = orderManager;
        this.filteredVendors = new FilteredList<>(this.addressBook.getVendorList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), new ArrayList<>(), new OrderManager());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    @Override
    public Path getMenuManagerFolderPath() {
        return userPrefs.getMenuManagerFolderPath();
    }

    @Override
    public void setMenuManagerFolderPath(Path menuManagerFolderPath) {
        requireNonNull(menuManagerFolderPath);
        userPrefs.setAddressBookFilePath(menuManagerFolderPath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasVendor(Vendor vendor) {
        requireNonNull(vendor);
        return addressBook.hasVendor(vendor);
    }

    @Override
    public void deleteVendor(Vendor target) {
        addressBook.removeVendor(target);
    }

    @Override
    public void addVendor(Vendor vendor) {
        addressBook.addVendor(vendor);
        updateFilteredVendorList(PREDICATE_SHOW_ALL_VENDORS);
    }

    @Override
    public void setVendor(Vendor target, Vendor editedVendor) {
        requireAllNonNull(target, editedVendor);

        addressBook.setVendor(target, editedVendor);
    }

    @Override
    public void selectVendor(int vendorIndex) {
        this.addressBook.selectVendor(vendorIndex);
    }

    @Override
    public void setVendorIndex(int vendorIndex) {
        this.addressBook.selectVendor(vendorIndex);
    }

    @Override
    public int getVendorIndex() {
        return this.addressBook.getVendorIndex();
    }

    //=========== MenuManager ================================================================================

    @Override
    public void setMenuManager(ReadOnlyMenuManager menuManager, int index) {
        this.menuManagers.get(index).resetData(menuManager);
    }

    @Override
    public ReadOnlyMenuManager getMenuManager(int index) {
        return menuManagers.get(index);
    }

    @Override
    public boolean hasFood(Food food, int index) {
        requireNonNull(food);
        return menuManagers.get(index).hasFood(food);
    }

    @Override
    public void deleteFood(Food target, int index) {
        menuManagers.get(index).removeFood(target);
    }

    @Override
    public void addFood(Food food, int index) {
    //        menuManagers.get(index).addFood(food);
    //        updateFilteredFoodList(PREDICATE_SHOW_ALL_FOODS);
    }

    @Override
    public void setFood(Food target, Food editedFood, int index) {
        requireAllNonNull(target, editedFood);
        menuManagers.get(index).setFood(target, editedFood);
    }

    //=========== OrderManager ================================================================================

    @Override
    public void setOrderManager(ReadOnlyOrderManager orderManager) {
        this.orderManager.resetData(orderManager);
    }

    @Override
    public ReadOnlyOrderManager getOrderManager() {
        return orderManager;
    }

    @Override
    public boolean hasOrderItem(OrderItem orderItem) {
        requireNonNull(orderItem);
        return orderManager.hasOrderItem(orderItem);
    }

    @Override
    public void removeOrderItem(OrderItem target) {
        orderManager.removeOrderItem(target);
    }

    @Override
    public void addOrderItem(OrderItem orderItem) {
        orderManager.addOrderItem(orderItem);
        updateFilteredOrderItemList(PREDICATE_SHOW_ALL_ORDERITEMS);
    }

    @Override
    public void setOrderItem(OrderItem target, OrderItem editedOrderItem) {
        requireAllNonNull(target, editedOrderItem);
        orderManager.setOrderItem(target, editedOrderItem);
    }

    @Override
    public int getOrderHistorySize() {
        return orderManager.getOrderHistorySize();
    }

    @Override
    public int getOrderItemQuantity(int index) {
        return orderManager.getQuantity(index);
    }

    @Override
    public void undoOrder() {
        orderManager.undoChanges();
    }

    @Override
    public void sortOrderItemByName() {
        orderManager.sortOrderItemByName();
    }

    @Override
    public void sortOrderItemByPrice() {
        orderManager.sortOrderItemByPrice();
    }

    //=========== Filtered Vendor List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Vendor} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Vendor> getFilteredVendorList() {
        return filteredVendors;
    }

    @Override
    public void updateFilteredVendorList(Predicate<Vendor> predicate) {
        requireNonNull(predicate);
        filteredVendors.setPredicate(predicate);
    }

    //=========== Filtered Food List Accessors =============================================================

    @Override
    public ObservableList<Food> getFilteredFoodList() {
        //todo the filteredFoods need to be changed and this whole class needs to be updated
        if (filteredFoods == null || changeVendor) {
            changeVendor = false;
            updateFilteredFoodList(PREDICATE_SHOW_ALL_FOODS);
        }
        return filteredFoods;
    }

    @Override
    public void clearOrder() {
        orderManager.setOrder(new ArrayList<>());
    }

    @Override
    public void resetOrder() {
        orderManager.resetOrder();
    }

    @Override
    public void updateVendor() {
        changeVendor = true;
    }

    @Override
    public void updateFilteredFoodList(Predicate<Food> predicate) {
        requireNonNull(predicate);
        filteredFoods = new FilteredList<>(menuManagers.get(getVendorIndex()).getFoodList());
        filteredFoods.setPredicate(predicate);
    }

    //=========== Filtered OrderItem List Accessors =============================================================

    @Override
    public ObservableList<OrderItem> getFilteredOrderItemList() {
        if (filteredOrderItems == null) {
            updateFilteredOrderItemList(PREDICATE_SHOW_ALL_ORDERITEMS);
        }
        return filteredOrderItems;
    }

    @Override
    public void updateFilteredOrderItemList(Predicate<OrderItem> predicateindex) {
        filteredOrderItems = new FilteredList<>(this.orderManager.getOrderItemList());
    }

    @Override
    public int getOrderSize() {
        return filteredOrderItems.size();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && userPrefs.equals(other.userPrefs)
                && filteredVendors.equals(other.filteredVendors);
    }

}
