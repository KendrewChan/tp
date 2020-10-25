package seedu.address.model.order;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.order.exceptions.DuplicateOrderItemException;
import seedu.address.model.order.exceptions.OrderItemNotFoundException;
import seedu.address.model.vendor.Vendor;

public class Order implements Iterable<OrderItem> {
    protected Vendor vendor;

    private final ObservableList<OrderItem> internalList = FXCollections.observableArrayList();
    private final ObservableList<OrderItem> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    /**
     * Returns true if the list contains an equivalent OrderItem as the given argument.
     */
    public boolean contains(OrderItem toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameOrderItemDescription);
    }

    /**
     * Returns a copy of the order. The orderItems are also copies.
     */
    public Order makeCopy() {
        Order newOrder = new Order();
        for (OrderItem orderItem: internalList) {
            newOrder.add(orderItem.makeCopy());
        }
        return newOrder;
    }

    /**
     * Adds OrderItem to the list.
     */
    public void add(OrderItem toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            int index = internalList.indexOf(toAdd);
            OrderItem existingItem = internalList.get(index);
            toAdd.setQuantity(toAdd.getQuantity() + existingItem.getQuantity());
            setOrderItem(existingItem, toAdd);
        } else {
            internalList.add(toAdd);
        }
    }

    public OrderItem getOrderItem(String name) {
        requireNonNull(name);
        for (OrderItem orderItem: internalList) {
            if (orderItem.getName().equals(name)) {
                return orderItem;
            }
        }
        throw new OrderItemNotFoundException();
    }

    /**
     * Replaces the OrderItem {@code target} in the list with {@code editedOrderItem}.
     * {@code target} must exist in the list.
     * The OrderItem identity of {@code editedOrderItem} must not be the same as another existing OrderItem in the list.
     */
    public void setOrderItem(OrderItem target, OrderItem editedOrderItem) {
        requireAllNonNull(target, editedOrderItem);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new OrderItemNotFoundException();
        }

        if (!target.isSameOrderItemDescription(editedOrderItem) && contains(editedOrderItem)) {
            throw new DuplicateOrderItemException();
        }

        internalList.set(index, editedOrderItem);
    }

    /**
     * Removes the equivalent OrderItem from the list.
     * The OrderItem must exist in the list.
     */
    public void remove(OrderItem toRemove) {
        requireNonNull(toRemove);
        if (!contains(toRemove)) {
            throw new OrderItemNotFoundException();
        }
        int index = internalList.indexOf(toRemove);

        OrderItem existingItem = internalList.get(index);
        int currQty = existingItem.getQuantity();
        int newQty = currQty - toRemove.getQuantity();

        if (!OrderItem.isValidQuantity(newQty)) {
            // Remove all
            internalList.remove(toRemove);
        } else {
            // Reduce
            OrderItem reduce = new OrderItem(toRemove, newQty);
            setOrderItem(existingItem, reduce);
        }
    }

    public void setOrder(Order replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        requireAllNonNull(orderItems);
        if (!orderItemsAreUnique(orderItems)) {
            throw new DuplicateOrderItemException();
        }
        internalList.setAll(orderItems);
    }

    public void sortOrderItemByName() {
        internalList.sort(Comparator.comparing(OrderItem::getName));
    }

    public void sortOrderItemByPrice() {
        internalList.sort(Comparator.comparing(OrderItem::getQuantity));
    }

    public double getTotal() {
        double total = 0;
        for (OrderItem orderItem: internalList) {
            total += orderItem.getQuantity() * orderItem.getPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (OrderItem orderItem: internalList) {
            text.append(orderItem.toString() + '\n');
        }
        return text.toString();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<OrderItem> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<OrderItem> iterator() {
        return internalList.iterator();
    }

    public int getQuantity(int index) {
        assert(index < internalList.size());
        return internalList.get(index).getQuantity();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Order // instanceof handles nulls
                && internalList.equals(((Order) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code orderItems} contains only unique orderItems.
     */
    private boolean orderItemsAreUnique(List<OrderItem> orderItems) {
        for (int i = 0; i < orderItems.size() - 1; i++) {
            for (int j = i + 1; j < orderItems.size(); j++) {
                if (orderItems.get(i).isSameOrderItemDescription(orderItems.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
