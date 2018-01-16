package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;

public class PetsController {
    public Button AddProduct;
    public TextField nameof;
    // public TextField petName;
    public TextField Age;
    public TextField Address;
    public TextField Picture;
    public TextField price;
    public ImageView imagePet;
    public ChoiceBox Type;
    public ChoiceBox Jender;
    public TextArea Description;
    // public ChoiceBox Priority;
    public ImageView imageV;
    @FXML
    public void initialize()
    {
        Picture.setDisable(true);
        Age.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    Age.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        price.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    price.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    public void finish() throws SQLException {

        int age=Integer.parseInt(Age.getText());
        String pic=Picture.getText();
        String pric=price.getText();
        String adress = Address.getText();
        String typ=Type.getValue().toString();
        String gen = Jender.getValue().toString();
        String description = Description.getText();
        String id=nameof.getText();

        String name=Main.User.getValue().toString();
        name=name.substring(name.indexOf(" ")+1);
        int price=Integer.parseInt(pric);
        PackageControler.price+=price;
        PackageControler.pricebind.setValue(""+PackageControler.price);
        PackageControler.amountofproducts+=1;
        PackageControler.amount.setValue(""+PackageControler.amountofproducts);
        //(String userName,  price, String type, String jender, String description)
        Pets Pet=new Pets(name,id,age,adress,pic,pric,typ,gen,description);
        PackageControler.ProductsToAdd.add(Pet);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Well Done");
        alert.setHeaderText("Your Product has been saved ,after you will finish to compose your package the product will be save in our database.");
        alert.showAndWait();
        Stage window = (Stage) AddProduct.getScene().getWindow();
        window.close();
    }
    public void UploadImage ()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("JPEG", "*.jpg");
        FileChooser.ExtensionFilter fileExtensions2 = new FileChooser.ExtensionFilter("JPEG", "*.png");
        fileChooser.getExtensionFilters().add(fileExtensions);
        fileChooser.getExtensionFilters().add(fileExtensions2);
        File selected_img = fileChooser.showOpenDialog(null);
        Picture.setText(selected_img.getPath());
        Image image1 = new Image("file:" + selected_img.getPath());
        imagePet.setImage(image1);
     //   imagePet.setRotate(imageV.getRotate() + 180);
    }
    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public void home(ActionEvent actionEvent) {
        Stage window = (Stage) AddProduct.getScene().getWindow();
        window.close();
    }
}
