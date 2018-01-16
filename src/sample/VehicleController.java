package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.File;

/**
 * Created by User on 1/11/2018.
 */
public class VehicleController {
    public TextField price;
    public TextField id;
    public TextArea discription;
    public TextField address;
    public TextField model;
    public ImageView vehicle_img;
    public TextField img_path;
    public TextField num_owners;
    public ChoiceBox manufacture;
    public Button AddProdcut;
    public ChoiceBox manYear;

    public void initialize()
    {
        img_path.setDisable(true);
        num_owners.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    num_owners.setText(newValue.replaceAll("[^\\d]", ""));
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
    public void upload_img(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("JPEG", "*.jpg");
        FileChooser.ExtensionFilter fileExtensions2 = new FileChooser.ExtensionFilter("JPEG", "*.png");
        fileChooser.getExtensionFilters().add(fileExtensions);
        fileChooser.getExtensionFilters().add(fileExtensions2);
        File selected_img = fileChooser.showOpenDialog(null);
        img_path.setText(selected_img.getPath());
        Image image1 = new Image("file:" + selected_img.getPath());
        vehicle_img.setImage(image1);
    }

    public void back(ActionEvent actionEvent) {
    }

    public void submit_vehicle(ActionEvent actionEvent) {
        String VehicleName=id.getText();
        String pric=price.getText();
        String addres=address.getText() ;
        String ManuFac=manufacture.getValue().toString();
        String Model=model.getText();
        String year=manYear.getValue().toString();
        String numOwner=num_owners.getText();
        String pic=img_path.getText();
        String disc=discription.getText();
        if (VehicleName==""||pric==""||addres==""||ManuFac==""||Model==""||year==""||numOwner==""||pic==""||disc=="")
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cant finish Adding Vehicle");
            alert.setHeaderText("You must fill all of the fields for Adding Vehicle");
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
        VehicleClass Vehicle=new VehicleClass(name,VehicleName,pric,addres,ManuFac,Model,year,numOwner,pic,disc);
        PackageControler.ProductsToAdd.add(Vehicle);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Well Done");
        alert.setHeaderText("Your Product has been saved ,after you will finish to compose your package the product will be save in our database.");
        alert.showAndWait();
        Stage window = (Stage) AddProdcut.getScene().getWindow();
        window.close();
    }

    public void home(ActionEvent actionEvent) {
        Stage window = (Stage) AddProdcut.getScene().getWindow();
        window.close();
    }
}
