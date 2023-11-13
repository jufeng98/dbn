package com.dbn.data.model.sortable;

import com.dbn.data.sorting.SortingState;
import com.dbn.data.model.DataModelState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SortableDataModelState extends DataModelState {
    protected SortingState sortingState = new SortingState();
}
