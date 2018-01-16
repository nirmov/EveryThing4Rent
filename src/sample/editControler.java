package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 12/28/2017.
 */
public class editControler  {
    public ChoiceBox type;
 //   public ChoiceBox Priority;
    public Button Update;
    public TextField Address;
    public TextField AmountOfPeople;
    public TextField Picture;
    public TextField price;
    public TextField id;
    int dif;

    @FXML
    public void initialize() throws SQLException {
        AmountOfPeople.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    AmountOfPeople.setText(newValue.replaceAll("[^\\d]", ""));
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
        String sql = "select * from RealEstate WHERE id = '" + EditProducts.choose + "' AND Username = '" + name + "'";
        ResultSet rs = Main.stat.executeQuery(sql);
        while (rs.next()) {
            Address.setText(rs.getString("address"));
            price.setText(rs.getString("price"));
            dif=Integer.parseInt(rs.getString("price"));
            AmountOfPeople.setText(""+rs.getInt("amountofpeople"));
            Picture.setText(rs.getString("picture"));
            type.setValue(rs.getString("type"));
            id.setText(rs.getString("id"));
        }
    }
    public void home()
    {
        Stage window = (Stage) Update.getScene().getWindow();
        window.close();
    }
    public void UploadImage()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("JPEG", "*.jpg");
        FileChooser.ExtensionFilter fileExtensions2 = new FileChooser.ExtensionFilter("JPEG", "*.png");
        fileChooser.getExtensionFilters().add(fileExtensions);
        fileChooser.getExtensionFilters().add(fileExtensions2);
        File selected_img = fileChooser.showOpenDialog(null);
        Picture.setText(selected_img.getPath());
    }
    public void finish() throws SQLException {
        String Addr=Address.getText();
        String AmountOfPeo=AmountOfPeople.getText();
        String pic=Picture.getText();
        String pric=price.getText();
        if (Addr.equals("")||AmountOfPeo.equals("")||pic.equals("")||pric.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cant finish Updating a Product");
            alert.setHeaderText("You must fill all of the fields for Adding Product");
            alert.showAndWait();
            return;
        }
        int price=Integer.parseInt(pric);
        dif=dif-price;
        String username=Main.User.toString();
        String nameOf=id.getText();
       // String pri=Priority.getValue().toString();
        String ty=type.getValue().toString();
        String name=Main.User.getValue().toString();
        name=name.substring(name.indexOf(" ")+1);
        String packageName="";
        ResultSet rs = Main.stat.executeQuery("SELECT * from RealEstate WHERE id = '"+nameOf+"' AND Username = '"+name+"'");
        while (rs.next()) {
            packageName=rs.getString("packageName");
        }
        String PackagePrice="";
        ResultSet rs1= Main.stat.executeQuery("SELECT * from Packages WHERE PackageName = '"+packageName+"'");
        while (rs1.next()) {
            String type=rs1.getString("PackageType");
            PackagePrice = rs1.getString("price");
            if(!type.equals("Trade in")) {
                int PackagePrice1 = Integer.parseInt(PackagePrice) - dif;
                PackagePrice = "" + PackagePrice1;
            }

        }
        PreparedStatement statement = Main.conn.prepareStatement("DELETE from RealEstate WHERE id = ? AND Username = ?");
        statement.setString(1,nameOf);
        statement.setString(2,name);
        statement.executeUpdate();
        PreparedStatement statement1 = Main.conn.prepareStatement("UPDATE Packages SET price = ? WHERE PackageName=?");
        statement1.setString(1,PackagePrice);
        statement1.setString(2,packageName);
        statement1.executeUpdate();
        PreparedStatement prep = Main.conn.prepareStatement(
                "insert into RealEstate values (?, ?,?, ?,?, ?,?,?,?);");
        prep.setString(1, name );
        prep.setString(2, nameOf);
        prep.setString(3, pric);
        prep.setString(4, Addr);
        prep.setInt(5, Integer.parseInt( AmountOfPeo));
        prep.setString(6, pic);
        prep.setString(7,"");
        prep.setString(8, ty);
        prep.setString(9, packageName);
        prep.addBatch();
        prep.executeBatch();
        Stage window = (Stage) Update.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Well Done");
        alert.setHeaderText("Your Product has been Updated");
        alert.showAndWait();
        window.close();
    }
}
