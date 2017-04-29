package bd.com.parvez.httprequestdata.services;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import bd.com.parvez.httprequestdata.model.Item;

public class XMLParser {

    public static Item[] parseFeed(String content) {

        try {

            boolean inItemTag = false;
            String currentTagName = "";
            Item currentItem = null;
            List<Item> itemList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("item")) {
                            inItemTag = true;
                            currentItem = new Item();
                            itemList.add(currentItem);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            inItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (inItemTag && currentItem != null) {
                            try {
                                switch (currentTagName) {
                                    case "id":
                                        currentItem.setId(Integer.parseInt(text));
                                        break;
                                    case "name":
                                        currentItem.setName(text);
                                        break;
                                    case "cell_phone":
                                        currentItem.setCellPhone(text);
                                        break;
                                    case "blood_group":
                                        currentItem.setBloodGroup(text);
                                        break;
                                    case "image":
                                        currentItem.setImage(text);
                                    default:
                                        break;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }

                eventType = parser.next();

            } // end while loop

            Item[] dataItems = new Item[itemList.size()];
            return itemList.toArray(dataItems);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}