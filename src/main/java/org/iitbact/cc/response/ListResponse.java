package org.iitbact.cc.response;

import lombok.Data;
import org.iitbact.cc.beans.BaseBean;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListResponse<T> implements BaseBean<List<T>> {
    private List<T> list = new ArrayList<>();
    private int pageNo;
    private boolean hasMore;

    @Override
    public void setEntity(List<T> list) {
        this.list = list;
    }
}
