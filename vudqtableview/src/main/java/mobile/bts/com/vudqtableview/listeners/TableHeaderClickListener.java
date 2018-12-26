package mobile.bts.com.vudqtableview.listeners;

/**
 * Listener interface to listen for clicks on table headers of a {@link mobile.bts.com.customtable.SortableTableView}.
 *
 * @author ISchwarz
 */
public interface TableHeaderClickListener {

    /**
     * This method is called of a table header was clicked.
     *
     * @param columnIndex The index of the column that was clicked.
     */
    void onHeaderClicked(final int columnIndex);

}
