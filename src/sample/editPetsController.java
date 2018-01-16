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

public class editPetsController {
    public Button AddProduct;
    public TextField nameof;
    // public TextField petName;
    public TextField Age;
    public TextField Address;
    public TextField Picture;
    public TextField price;
    public ChoiceBox Type;
    public ChoiceBox Jender;
    public TextArea Description;
    public int dif;
    public String packageName;
    // public ChoiceBox Priority;
    public ImageView imageV;
    @FXML
    public void initialize() throws SQLException {
        Picture.setDisable(true);
        nameof.setDisable(true);
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
        String name=Main.User.getValue().toString();
        name=name.substring(name.indexOf(" ")+1);
        String sql = "select * from Pets WHERE petName = '" + EditProducts.choose + "' AND Username = '" + name + "'";
        ResultSet rs = Main.stat.executeQuery(sql);
        while (rs.next()) {
            Address.setText(rs.getString("address"));
            price.setText(rs.getString("price"));
            dif=Integer.parseInt(rs.getString("price"));
            Picture.setText(rs.getString("picture"));
            Type.setValue(rs.getString("type"));
            Jender.setValue(rs.getString("jender"));
            nameof.setText(rs.getString("petName"));
            Age.setText(""+rs.getInt("petAge"));
            Description.setText(rs.getString("description"));
            packageName=rs.getString("packageName");
        }

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
        PreparedStatement statement1 = Main.conn.prepareStatement("UPDATE Packages SET price = ? WHERE PackageName=?");
        statement1.setString(1,PackagePrice);
        statement1.setString(2,packageName);
        statement1.executeUpdate();
        //(Username, petName,petAge,address,picture,type,jender,description,packageName,price);
        PreparedStatement statement2 = Main.conn.prepareStatement("UPDATE Pets SET price = ? , petAge=? , picture=? , type=? , jender=? , description=? , address=?  WHERE petName=?");
        //(String userName,  price, String type, String jender, String description)
        statement2.setString(1,pric);
        statement2.setInt(2,age);
        statement2.setString(3,pic);
        statement2.setString(4,typ);
        statement2.setString(5,gen);
        statement2.setString(6,description);
        statement2.setString(7,adress);
        statement2.setString(8,id);
        statement2.executeUpdate();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Well Done");
        alert.setHeaderText("Your Product has been update!");
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
