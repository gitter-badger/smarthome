package org.eclipse.smarthome.config.discovery.inbox;

import org.eclipse.smarthome.config.discovery.DiscoveryResultFlag;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;


/**
 * The {@link InboxFilterCriteria} specifies the filter for {@link Inbox} <i>GET</i> requests.
 * <p>
 * The according property is filtered in the {@link Inbox} if it's <i>NEITHER</i> {@code null}
 * <i>NOR</i> empty. All specified properties are filtered with an <i>AND</i> operator.
 *
 * @author Michael Grammling - Initial Contribution
 *
 * @see Inbox
 */
public final class InboxFilterCriteria {

    private final String bindingId;
    private final ThingTypeUID thingTypeUID;
    private final ThingUID thingUID;
    private final DiscoveryResultFlag flag;

    /**
     * Creates a new instance of this class with the specified parameters.
     *
     * @param bindingId the binding ID to be filtered (could be null or empty)
     * @param flag the discovery result flag to be filtered (could be null)
     */
    public InboxFilterCriteria(String bindingId, DiscoveryResultFlag flag) {
        this.bindingId = bindingId;
        this.thingTypeUID = null;
        this.thingUID = null;
        this.flag = flag;
    }

    /**
     * Creates a new instance of this class with the specified parameters.
     * 
     * @param thingTypeUID
     *            the Thing type to be filtered (could be null or empty)
     * @param flag
     *            the discovery result flag to be filtered (could be null)
     */
    public InboxFilterCriteria(ThingTypeUID thingTypeUID, DiscoveryResultFlag flag) {
        this.bindingId = null;
        this.thingTypeUID = thingTypeUID;
        this.thingUID = null;
        this.flag = flag;
    }

    /**
     * Creates a new instance of this class with the specified parameters.
     * 
     * @param thingUID
     *            the Thing ID to be filtered (could be null or empty)
     * @param flag
     *            the discovery result flag to be filtered (could be null)
     */
    public InboxFilterCriteria(ThingUID thingUID, DiscoveryResultFlag flag) {
        this.bindingId = null;
        this.thingTypeUID = null;
        this.thingUID = thingUID;
        this.flag = flag;
    }

    /**
     * Returns the binding ID to be filtered.
     *
     * @return the binding ID to be filtered (could be null or empty)
     */
    public String getBindingId() {
        return this.bindingId;
    }


    /**
     * Returns the unique {@code Thing} type to be filtered.
     *
     * @return the unique Thing type to be filtered (could be null or empty)
     */
    public ThingTypeUID getThingTypeUID() {
        return this.thingTypeUID;
    }

    /**
     * Returns the unique {@code Thing} ID to be filtered.
     *
     * @return the unique Thing ID to be filtered (could be null or empty)
     */
    public ThingUID getThingUID() {
        return this.thingUID;
    }


    /**
     * Return the {@link DiscoveryResultFlag} to be filtered.
     *
     * @return the discovery result flag to be filtered (could be null)
     */
    public DiscoveryResultFlag getFlag() {
        return this.flag;
    }

}