package bd.parvez.fragment;

import java.io.Serializable;

/**
 * Created by PARVEZ on 7/4/2015.
 */
public class ItemObject implements Serializable {
    private String name;
    private String details;

    public ItemObject(String name, String details) {
        this.name = name;
        this.details = details;
    }

    public String getName() {
        return name;
    }


    public String getDetails() {
        return details;
    }

}
