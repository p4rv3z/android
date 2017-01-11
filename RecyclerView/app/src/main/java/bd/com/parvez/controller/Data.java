package bd.com.parvez.controller;

import java.util.ArrayList;
import java.util.List;

import bd.com.parvez.model.Item;
import bd.com.parvez.recyclerview.R;

/**
 * Created by ParveZ on 1/11/2017.
 */

public class Data {
    private static final String[] name = {"Arfan", "Sudip", "Rajdip", "Papan", "Sourav", "Akash", "Jahid",
            "Rabbier", "Tuhin"};
    private static final String[] email = {"arfan@gmail.com", "sudip@gmail.com", "Rajdip@gmail.com",
            "Papan@gmail.com", "Sourav@gmail.com", "Akash@gmail.com", "Jahid@gmail.com",
            "Rabbier@gmail.com", "Tuhin@gmail.com"};
    private static final int[] images = {R.drawable.arfan, R.drawable.sudip, R.drawable.rajdip, R.drawable.papan,
            R.drawable.sourav, R.drawable.akash, R.drawable.jahid, R.drawable.rabbier, R.drawable.tuhin};

    /**
     * Create a list of Item
     * @return all Item data
     */
    public static List<Item> getAllData() {
        //Create List of Item
        List<Item> data = new ArrayList<>();
        for (int j = 0;j<3;j++)//for increse item for better test
        for (int i = 0; i < name.length; i++) {
            Item item = new Item();
            item.setName(name[i]);
            item.setEmail(email[i].toLowerCase());
            item.setImage(images[i]);
            data.add(item);
        }
        return data;
    }
}
