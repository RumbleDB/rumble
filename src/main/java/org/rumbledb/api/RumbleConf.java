package org.rumbledb.api;

/**
 * The configuration of Rumble.
 * 
 * For the moment, it is only possible to configure the results size cap (which is forwarded to Spark's collect-item-limit).
 * 
 * @author Ghislain Fourny, Stefan Irimescu, Can Berker Cikis
 *
 */
public class RumbleConf {
    private int _resultsSizeCap;

    public RumbleConf() {
        _resultsSizeCap = 100;
    }

    /**
     * Sets the number of Items that should be collected in case of a forced materialization. This applies in particular to a local use of the ItemIterator.
     * @param cap the maximum number of Items to collect.
     */
    void setResultsSizeCap(int cap)
    {
        _resultsSizeCap = cap;
    }

    /**
     * Gets the configured number of Items that should be collected in case of a forced materialization. This applies in particular to a local use of the ItemIterator.
     * @return the current number of Items to collect.
     */
    int getResultsSizeCap()
    {
        return _resultsSizeCap;
    }

}
