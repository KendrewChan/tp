package seedu.address.model.order;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.collections.ObservableList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameOrderItem comparison)
 */
public class OrderManager implements ReadOnlyOrderManager {

    private final Stack<Order> orderHistory; // head of orderHistory
    private final Order order;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        orderHistory = new Stack<>();
        order = new Order();
        saveChanges();
    }

    public OrderManager() {}

    /**
     * Creates a OrderManager using the OrderItems in the {@code toBeCopied}
     */
    public OrderManager(ReadOnlyOrderManager toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    private void saveChanges() {
        orderHistory.add(order.makeCopy());
    }

    /**
     * Undoes the last change to the order.
     */
    public void undoChanges() {
        assert(orderHistory.size() > 1);
        orderHistory.pop();
        order.setOrder(orderHistory.peek().makeCopy());
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the orderItem list with {@code orderItems}.
     * {@code orderItems} must not contain duplicate orderItems.
     */
    public void setOrder(List<OrderItem> orderItems) {
        this.order.setOrderItems(orderItems);
        saveChanges();
    }

    /**
     * Clears the contents of orderHistory.
     */
    public void resetOrder() {
        this.orderHistory.clear();
        this.order.setOrderItems(new ArrayList<>());
        saveChanges();
    }

    /**
     * Resets the existing data of this {@code OrderManager} with {@code newData}.
     */
    public void resetData(ReadOnlyOrderManager newData) {
        requireNonNull(newData);

        setOrder(newData.getOrderItemList());
    }

    //// orderItem-level operations

    /**
     * Returns true if a orderItem with the same identity as {@code orderItem} exists in the order manager.
     */
    public boolean hasOrderItem(OrderItem orderItem) {
        requireNonNull(orderItem);
        return order.contains(orderItem);
    }

    /**
     * Adds a orderItem to the address book.
     * The orderItem must not already exist in the address book.
     */
    public void addOrderItem(OrderItem f) {
        order.add(f);
        saveChanges();
    }


    /**
     * Replaces the given orderItem {@code target} in the list with {@code editedOrderItem}.
     * {@code target} must exist in the address book.
     * The orderItem identity of {@code editedOrderItem} must not be the same as another existing orderItem in the
     * address book.
     */
    public void setOrderItem(OrderItem target, OrderItem editedOrderItem) {
        requireNonNull(editedOrderItem);

        order.setOrderItem(target, editedOrderItem);
        saveChanges();
    }

    /**
     * Removes {@code key} from this {@code OrderManager}.
     * {@code key} must exist in the address book.
     */
    public void removeOrderItem(OrderItem key) {
        order.remove(key);
        saveChanges();
    }

    public int getOrderHistorySize() {
        return orderHistory.size();
    }

    public void sortOrderItemByName() {
        order.sortOrderItemByName();
    }

    public void sortOrderItemByPrice() {
        order.sortOrderItemByPrice();
    }

    //// util methods

    @Override
    public String toString() {
        return order.asUnmodifiableObservableList().size() + " orderItems";
        // TODO: refine later
    }

    @Override
    public ObservableList<OrderItem> getOrderItemList() {
        return order.asUnmodifiableObservableList();
    }

    public int getQuantity(int index) {
        return order.getQuantity(index);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OrderManager // instanceof handles nulls
                && order.equals(((OrderManager) other).order));
    }

    @Override
    public int hashCode() {
        return order.hashCode();
    }

}
