package ca.pethappy.pethappy.android.api.page;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
    public List<T> content;
    public Pageable pageable;
    public boolean last;
    public int totalPages;
    public int totalElements;
    public boolean first;
    public int number;
    public Sort sort;
    public int numberOfElements;
    public int size;
    public boolean empty;

    public Page() {
        content = new ArrayList<>();
        empty = true;
    }
}
