package sample;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class Pets extends Product {

    public String petName;
    public int petAge;
    public String Type;
    public String Jender;
    // public ChoiceBox Priority;
    // Product(String userName, String name, String picture, String price, String description, String Adress)
    //Username,nameof,picture,price,descroption,address

    public Pets(String userName, String petName, int petAge, String address, String picture, String price, String type, String jender, String description) {
        super(userName,petName,picture,price,description,address);
        this.petName = petName;
        this.petAge = petAge;
        Type = type;
        Jender = jender;
    }

    public String getPetName() {
        return petName;
    }

    public int getPetAge() {
        return petAge;
    }

    public String getType() {
        return Type;
    }
    public String toShow()
    {
        return "Address : "+Address+"\nPet type : "+Type+"\nPrice : "+getPrice()+"\nAge of pet: "+this.petAge + "\nJender :"+Jender+"\n description : "+Description;
    }
    public String getJender() {
        return Jender;
    }
}
