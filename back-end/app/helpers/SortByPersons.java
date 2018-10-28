package helpers;

import models.RestaurantTable;

import java.util.Comparator;

public class SortByPersons implements Comparator<RestaurantTable> {
    public int compare(RestaurantTable a, RestaurantTable b)
    {
        return a.getPersons() - b.getPersons();
    }
}