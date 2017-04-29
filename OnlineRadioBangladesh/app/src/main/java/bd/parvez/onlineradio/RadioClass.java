package bd.parvez.onlineradio;

/**
 * Created by Parvez on 6/4/2015.
 */
public class RadioClass {
    private String name;
    private String url;
    private int fav;
    private int id;


    public RadioClass(String name, String url, int fav, int id) {
        this.name = name;
        this.url = url;
        this.fav = fav;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getFav() {
        return fav;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
