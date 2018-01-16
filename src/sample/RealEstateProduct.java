package sample;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Created by User on 12/29/2017.
 */
public class RealEstateProduct extends Product {
    public String AmountOfPeople;
    public String Type;
    public String Priority;
    public RealEstateProduct(String address,String Username, String nameof, String amountOfPeople, String picture, String price, String type, String priority, String descroption ) {
        super(Username,nameof,picture,price,descroption,address);
        AmountOfPeople = amountOfPeople;
        Picture = picture;
        Type = type;
        Priority = priority;
        UserName=Username;
    }

    @Override
    public void setPackageName(String packageName) {
        super.setPackageName(packageName);
    }
    public String getProcudtName()
    {
        return  super.getName();
    }
    public String toShow()
    {
        return "Address : "+Address+"\nAmount of people : "+AmountOfPeople+"\nPrice : "+getPrice()+"\nProduct type: "+Type;
    }
}