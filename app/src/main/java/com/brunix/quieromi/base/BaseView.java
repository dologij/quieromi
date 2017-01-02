package com.brunix.quieromi.base;

import android.support.annotation.StringRes;

/**
 * All views should extend from this interface. Create a baseclass with common logic if needed.
 */
public interface BaseView {

    /**
     * A generic approach on showing messages to the user
     *
     * @param messageResourceId
     * @param callback
     */
    void showMessage(
            @StringRes int messageResourceId,
            MessageCallback callback);

    /**
     * Callback for handling message show is complete
     */
    interface MessageCallback {

        void done();

    }

}
