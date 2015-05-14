package io.zarda.elnerd.src;

/**
 * Created by Ahmed Atef on 11/05/15.
 */
public interface Waitable {
    /**
     * This method will be called anytime, so its content should be independent of any current
     * state.
     *
     * @param response
     */
    void receiveResponse(Object response);
}
