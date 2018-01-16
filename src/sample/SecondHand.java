package sample;

/**
 * Created by oran rozenbaum on 11/01/2018.
 */
public class SecondHand extends Product{
//public Product(String userName, String name, String picture, String price, String description, String Adress)

    //    public String PackageName;
    //public String Name;
    //public String Picture;
    //public String Price;
    //public String Description;
    //public String Address;
    public String Manufacture;
    public String ManYear;
    public String Type;

    public SecondHand() {
    }

    public String getType() {
        return Type;
    }

    public String getManYear() {

        return ManYear;
    }

    public String getManufacture() {

        return Manufacture;
    }

    public String getAddress() {
        return Address;
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

    public SecondHand(String userName, String name, String picture, String price, String description, String Adress,String man,String type,String year) {

        super(userName,name,picture,price,description,Adress);
        Manufacture =man;
        ManYear = year;
        Type=type;
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
    public String toShow() {
        return "Address : " + Address + "\nProduct type : " + getType()  + "\nProduct manufacture: " + getManufacture() + "\nYear of production :" + getManYear() + "\nPrice : " + getPrice() + "\n description" + Description;
    }
}




