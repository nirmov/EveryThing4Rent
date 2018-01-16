package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class editSecondHandController {
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
    public int dif;
    public String packageName;
    @FXML
    public void initialize() throws SQLException {
        Picture.setDisable(true);
        nameof.setDisable(true);

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
        //stat.executeUpdate("create table SecondHand (Username, productName,ProductType,manufacturer,YearOfManufacture,address,picture,description,packageName,price);");
        String name=Main.User.getValue().toString();
        name=name.substring(name.indexOf(" ")+1);
        String sql = "select * from SecondHand WHERE productName = '" + EditProducts.choose + "' AND Username = '" + name + "'";
        ResultSet rs = Main.stat.executeQuery(sql);
        while (rs.next()) {
            Address.setText(rs.getString("address"));
            price.setText(rs.getString("price"));
            dif=Integer.parseInt(rs.getString("price"));
            Picture.setText(rs.getString("picture"));
            Type.setValue(rs.getString("ProductType"));
            yearMan.setText(rs.getString("YearOfManufacture"));
            Manufacture.setText(rs.getString("manufacturer"));
            nameof.setText(rs.getString("productName"));
            //Age.setText(""+rs.getInt("petAge"));
            desc.setText(rs.getString("description"));
            packageName=rs.getString("packageName");
        }
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
        dif=dif-price;
        String PackagePrice="";
        ResultSet rs1= Main.stat.executeQuery("SELECT * from Packages WHERE packageName = '"+packageName+"'");
        while (rs1.next()) {
            String type = rs1.getString("PackageType");
            PackagePrice = rs1.getString("price");
            if(!type.equals("Trade in")) {
                int PackagePrice1 = Integer.parseInt(PackagePrice) - dif;
                PackagePrice = "" + PackagePrice1;
            }
        }
        PreparedStatement statement1 = Main.conn.prepareStatement("UPDATE Packages SET price = ? WHERE PackageName = ?");
        statement1.setString(1,PackagePrice);
        statement1.setString(2,packageName);
        statement1.executeUpdate();
        PreparedStatement statement2 = Main.conn.prepareStatement("UPDATE SecondHand SET price = ? , picture = ? , ProductType = ? , manufacturer = ? , YearOfManufacture = ? ,  description = ? , address = ?  WHERE productName = ?");
        statement2.setString(1,pric);
        statement2.setString(2,pic);
        statement2.setString(3,typ);
        statement2.setString(4,man);
        statement2.setString(5,year);
        statement2.setString(6,des);
        statement2.setString(7,Addr);
        statement2.setString(8,id);
        statement2.executeUpdate();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Well Done");
        alert.setHeaderText("Your Product has been Updated");
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
        imageV.setRotate(imageV.getRotate() + 180);
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
}




