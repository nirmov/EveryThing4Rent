package sample;

/**
 * Created by User on 1/9/2018.
 */
public class Product {
    public String UserName;
    public String PackageName;
    public String Name;
    public String Picture;
    public String Price;
    public String Description;
    public String Address;
    public Product() {
    }

    public String getUserName() {
        return UserName;
    }

    public String getPackageName() {
        return PackageName;
    }

    public String getName() {
        return Name;
    }

    public void setUserName(String userName) {

        UserName = userName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public void setName(String name) {
        Name = name;
    }

    public Product(String userName, String name, String picture, String price, String description,String address) {
        UserName = userName;
        Picture = picture;
        Name = name;
        Price=price;
        Address=address;
        Description= description;
    }
    public String toShow()
    {
        return "I am A product";
    }

    public String getPicture() {
        return Picture;
    }

    public String getPrice() {
        return Price;
    }

    public String getDescription() {
        return Description;
    }
}