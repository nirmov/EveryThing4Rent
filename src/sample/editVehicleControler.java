package sample;

import com.sun.jndi.cosnaming.IiopUrl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 1/12/2018.
 */
public class editVehicleControler {
    public TextField price;
    public TextField id;
    public TextArea discription;
    public TextField address;
    public TextField model;
    public ImageView vehicle_img;
    public TextField img_path;
    public TextField num_owners;
    public ChoiceBox manufacture;
    public String PackageName;
    public Button AddProdcut;
    int dif;
    public ChoiceBox manYear;
    public void initialize() throws SQLException {
        img_path.setDisable(true);
        id.setDisable(true);
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
        String name=Main.User.getValue().toString();
        name=name.substring(name.indexOf(" ")+1);
        String sql = "select * from Vehicle WHERE VehicleName = '" + EditProducts.choose + "' AND UserName = '" + name + "'";
        ResultSet rs = Main.stat.executeQuery(sql);
        while (rs.next()) {
            discription.setText(rs.getString("disc"));
            price.setText(rs.getString("price"));
            dif=Integer.parseInt(rs.getString("price"));
            address.setText(""+rs.getString("address"));
            img_path.setText(rs.getString("picture"));
            manufacture.setValue(rs.getString("ManuFa"));
            id.setText(rs.getString("VehicleName"));
            model.setText(rs.getString("model"));
            num_owners.setText(rs.getString("numOwn"));
            manYear.setValue(rs.getString("year"));
            PackageName=rs.getString("packageName");

        }
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

    public void submit_vehicle(ActionEvent actionEvent) throws SQLException {
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
            alert.setTitle("Cant finish Update Vehicle");
            alert.setHeaderText("You must fill all of the fields for Adding Vehicle");
            alert.showAndWait();
            return;
        }
        String name=Main.User.getValue().toString();
        name=name.substring(name.indexOf(" ")+1);
        int price=Integer.parseInt(pric);
        dif=dif-price;
        String PackagePrice="";
        ResultSet rs1= Main.stat.executeQuery("SELECT * from Packages WHERE PackageName = '"+PackageName+"'");
        while (rs1.next()) {
            String type=rs1.getString("PackageType");
            PackagePrice = rs1.getString("price");
            if(!type.equals("Trade in")) {
                int PackagePrice1 = Integer.parseInt(PackagePrice) - dif;
                PackagePrice = "" + PackagePrice1;
            }

        }
        PreparedStatement statement1 = Main.conn.prepareStatement("UPDATE Packages SET price = ? WHERE PackageName=?");
        statement1.setString(1,PackagePrice);
        statement1.setString(2,PackageName);
        statement1.executeUpdate();
        //VehicleName,price,address,ManuFa,model,year,picture,packageName,disc,numOwn
        PreparedStatement statement2 = Main.conn.prepareStatement("UPDATE Vehicle SET price = ?  , address = ? , ManuFa = ? , model = ? , year = ? , picture = ? , disc = ? , numOwn = ?  WHERE VehicleName=?");
        statement2.setString(1,pric);
        statement2.setString(2,addres);
        statement2.setString(3,ManuFac);
        statement2.setString(4,Model);
        statement2.setString(5,year);
        statement2.setString(6,pic);
        statement2.setString(7,disc);
        statement2.setString(8,numOwner);
        statement2.setString(9,VehicleName);
        statement2.executeUpdate();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Well Done");
        alert.setHeaderText("Your Product has been Updated");
        alert.showAndWait();
        Stage window = (Stage) AddProdcut.getScene().getWindow();
        window.close();
    }
}
