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


public class SecondHandController {
    public javafx.scene.control.TextField txt_name;

    @FXML
    Button AddProduct;
    public TextField Address;
    public TextField nameof;
    public TextField Manufacture;
    public TextField Picture;
    public TextField price;
    public ChoiceBox Type;
    public TextField yearMan;
    public TextArea desc;
    // public ChoiceBox Priority;
    public ImageView imageV;
    @FXML
    public void initialize()
    {
        Picture.setDisable(true);
        yearMan.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    yearMan.setText(newValue.replaceAll("[^\\d]", ""));
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
        String year = yearMan.getText();
        String Addr=Address.getText();
        String man=Manufacture.getText();
        String pic=Picture.getText();
        String pric=price.getText();
        String typ=Type.getValue().toString();
        //  String prio=Priority.getValue().toString();
        String id=nameof.getText();
        String des = desc.getText();
        if (Addr.equals("")||man.equals("")||pic.equals("")||pric.equals("")||id.equals("") || year.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cant finish Adding a Product");
            alert.setHeaderText("You must fill all of the fields for Adding Product");
            alert.showAndWait();
            return;
        }
        String name=Main.User.getValue().toString();
        name=name.substring(name.indexOf(" ")+1);
        int price=Integer.parseInt(pric);
        PackageControler.price+=price;
        PackageControler.pricebind.setValue(""+PackageControler.price);
        PackageControler.amountofproducts+=1;
        PackageControler.amount.setValue(""+PackageControler.amountofproducts);
        //String userName, String name, String picture, String price, String description, String Adress,String man,String type,String year
        SecondHand sh=new SecondHand(name,id,pic,pric,des,Addr,man,typ,year);
        PackageControler.ProductsToAdd.add(sh);
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
        imageV.setImage(image1);
      //  imageV.setRotate(imageV.getRotate() + 180);
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




