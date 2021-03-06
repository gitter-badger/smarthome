package org.eclipse.smarthome.core.common.registry;

/**
 * The {@link ManagedProvider} is a specific {@link Provider} that enables to
 * add, remove and update elements at runtime.
 * 
 * @author Dennis Nobel - Initial contribution
 * 
 * @param <E>
 *            type of the element
 * @param <K>
 *            type of the element key
 */
public interface ManagedProvider<E, K> extends Provider<E> {

    /**
     * Adds an element.
     * 
     * @param element
     *            element to be added
     */
    void add(E element);

    /**
     * Removes an element and returns the removed element.
     * 
     * @param key
     *            key of the element that should be removed
     * @return element that was removed, or null if no element with the given
     *         key exists
     */
    E remove(K key);

    /**
     * Updates an element.
     * 
     * @param element
     *            element to be updated
     * @return returns the old element or null if no element with the same key
     *         exists
     */
    E update(E element);

}