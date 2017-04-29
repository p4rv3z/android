package bd.com.parvez.httprequestdata.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ParveZ on 4/14/2017.
 */

public class Item implements Parcelable {

    private int id;
    private String name;
    private String cellPhone;
    private String bloodGroup;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cellPhone='" + cellPhone + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.cellPhone);
        dest.writeString(this.bloodGroup);
        dest.writeString(this.image);
    }

    public Item() {
    }

    protected Item(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.cellPhone = in.readString();
        this.bloodGroup = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
