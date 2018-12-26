package mobile.bts.com.vudqtableview.listeners;


import mobile.bts.com.vudqtableview.SortingStatus;

/**
 * Definition of a listener that is notified when the has changed.
 */
public interface SortingStatusChangeListener {

    /**
     * Callback method that is called when the { SortingStatus} has changed.
     *
     * @param newSortingStatus The new { SortingStatus}.
     */
    void onSortingStatusChanged(final SortingStatus newSortingStatus);
}
