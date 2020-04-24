package org.iitbact.cc.helper;

import org.iitbact.cc.exceptions.CovidControlException;

public interface CovidServiceSupplier<T> {
    T get() throws CovidControlException;
}
