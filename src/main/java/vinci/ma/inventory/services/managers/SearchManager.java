package vinci.ma.inventory.services.managers;

import java.util.List;
import java.util.Map;

public interface SearchManager {
    Map<String, List<?>> search(String query);
}
