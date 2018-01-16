package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by User on 12/28/2017.
 */

public class EditProducts {
    public ChoiceBox PackageList;
    public ChoiceBox ProductList;
    static String choose;
    static String packageChoose;
    public Button Home;
    public HashMap<String,String> productToType;
    @FXML
    public void initialize() throws SQLException {
        productToType=new HashMap<>();
        String name=Main.User.getValue().toString();
        choose="";
        name=name.substring(name.indexOf(" ")+1);

        String sql1 = "select DISTINCT PackageName from Packages WHERE Username = '" + name + "'";
        ResultSet rs1 = Main.stat.executeQuery(sql1);
        while (rs1.next()) {
            PackageList.getItems().add(rs1.getString("PackageName"));
        }

    }
    public  void ProductChoose() throws SQLException {
        ProductList.getItems().clear();
        String PackageName=PackageList.getValue().toString();
        String name=Main.User.getValue().toString();
        name=name.substring(name.indexOf(" ")+1);
        String sql="select DISTINCT id from RealEstate WHERE Username = '"+name+"' And packageName = '"+PackageName+"'" ;
        ResultSet rs = Main.stat.executeQuery(sql);
        while (rs.next()) {
            String idr=rs.getString("id");
            ProductList.getItems().add(rs.getString("id"));
            productToType.put(idr,"Edit.fxml");
        }
        String sql1="select DISTINCT VehicleName from Vehicle WHERE UserName = '"+name+"' And packageName = '"+PackageName+"'" ;
        ResultSet rs1 = Main.stat.executeQuery(sql1);
        while (rs1.next()) {
            ProductList.getItems().add(rs1.getString("VehicleName"));
            productToType.put(rs1.getString("VehicleName"),"editVehicle.fxml");
        }
        String sql2="select DISTINCT petName from Pets WHERE Username = '"+name+"' And packageName = '"+PackageName+"'" ;
        ResultSet rs2 = Main.stat.executeQuery(sql2);
        while (rs2.next()) {
            ProductList.getItems().add(rs2.getString("petName"));
            productToType.put(rs2.getString("petName"),"editPets.fxml");
        }
        String sql3="select DISTINCT productName from SecondHand WHERE Username = '"+name+"' And packageName = '"+PackageName+"'" ;
        ResultSet rs3 = Main.stat.executeQuery(sql3);
        while (rs3.next()) {
            ProductList.getItems().add(rs3.getString("productName"));
            productToType.put(rs3.getString("productName"),"editSecondHand.fxml");
        }
    }
    public void home()
    {
        packageChoose=null;
        Stage window = (Stage) Home.getScene().getWindow();
        window.close();
    }
    public void done()
    {
        packageChoose=null;
        Stage window = (Stage) Home.getScene().getWindow();
        window.close();
    }
    public void edit() throws SQLException, IOException {
        try {
            choose = ProductList.getValue().toString();
            Stage Login = new Stage();
            Parent root2 = FXMLLoader.load(getClass().getResource(productToType.get(choose)));
            Scene s2 = new Scene(root2, 720, 400);
            s2.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
            Login.setScene(s2);
            Login.show();
            home();
        }
        catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Eror");
            alert.setHeaderText("Please chose product before press Edit button");
            alert.showAndWait();
        }
    }
    public void DeleteProductFromPuc() throws SQLException {
        try {
            String choose = ProductList.getValue().toString();
            String name = Main.User.getValue().toString();
            name = name.substring(name.indexOf(" ") + 1);
            String packageName = "";
            int ProductPric = 0;
            if (productToType.get(choose).contains("Edit")) {
                ResultSet rs = Main.stat.executeQuery("SELECT * from RealEstate WHERE id = '" + choose + "' AND Username = '" + name + "'");
                while (rs.next()) {
                    packageName = rs.getString("packageName");
                    ProductPric = Integer.parseInt(rs.getString("price"));
                }
            }
            else if (productToType.get(choose).contains("Vehicle"))
            {
                ResultSet rs = Main.stat.executeQuery("SELECT * from Vehicle WHERE VehicleName = '" + choose + "' AND UserName = '" + name + "'");
                while (rs.next()) {
                    packageName = rs.getString("packageName");
                    ProductPric = Integer.parseInt(rs.getString("price"));
                }
            }
            else if(productToType.get(choose).contains("Pets"))
            {
                ResultSet rs = Main.stat.executeQuery("SELECT * from Pets WHERE petName = '" + choose + "' AND Username = '" + name + "'");
                while (rs.next()) {
                    packageName = rs.getString("packageName");
                    ProductPric = Integer.parseInt(rs.getString("price"));
                }
            }
            else if(productToType.get(choose).contains("SecondHand"))
            {
                ResultSet rs = Main.stat.executeQuery("SELECT * from SecondHand WHERE productName = '" + choose + "' AND Username = '" + name + "'");
                while (rs.next()) {
                    packageName = rs.getString("packageName");
                    ProductPric = Integer.parseInt(rs.getString("price"));
                }
            }
            int count=0;
            count=getAmountOfInvetation(packageName);
            if (count!=0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eror");
                alert.setHeaderText("Can Not Finish Deleting a product , There is invitation on this product. ");
                alert.showAndWait();
                return;
            }
            String PackagePrice = "";
            ResultSet rs1 = Main.stat.executeQuery("SELECT * from Packages WHERE PackageName = '" + packageName + "'");
            int amountOfprod = 0;
            while (rs1.next()) {
                String type=rs1.getString("PackageType");
                PackagePrice = rs1.getString("price");
                if(!type.equals("Trade in")) {
                    int PackagePrice1 = Integer.parseInt(PackagePrice) - ProductPric;
                    PackagePrice = "" + PackagePrice1;
                }
                amountOfprod = Integer.parseInt(rs1.getString("amountOfProducts"));
            }
            if (amountOfprod == 1) {

                PreparedStatement statement = Main.conn.prepareStatement("DELETE from Packages WHERE PackageName = ?");
                statement.setString(1, packageName);
                statement.executeUpdate();
                PackageList.getItems().remove(packageName);
            } else {
                PreparedStatement statement1 = Main.conn.prepareStatement("UPDATE Packages SET price = ?, amountOfProducts=? WHERE PackageName=?");
                statement1.setString(1, PackagePrice);
                statement1.setInt(2,  amountOfprod);
                statement1.setString(3, packageName);
                statement1.executeUpdate();
            }
            PreparedStatement statement = Main.conn.prepareStatement("DELETE from RealEstate WHERE id = ? AND Username = ?");
            statement.setString(1, choose);
            statement.setString(2, name);
            statement.executeUpdate();
            PreparedStatement statement1 = Main.conn.prepareStatement("DELETE from Vehicle WHERE VehicleName = ? AND UserName = ?");
            statement1.setString(1, choose);
            statement1.setString(2, name);
            statement1.executeUpdate();
            PreparedStatement statement2 = Main.conn.prepareStatement("DELETE from Pets WHERE petName = ? AND Username = ?");
            statement2.setString(1, choose);
            statement2.setString(2, name);
            statement2.executeUpdate();
            PreparedStatement statement3 = Main.conn.prepareStatement("DELETE from SecondHand WHERE productName = ? AND Username = ?");
            statement3.setString(1, choose);
            statement3.setString(2, name);
            statement3.executeUpdate();
            ProductList.getItems().remove(choose);
        }
        catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Eror");
            alert.setHeaderText("Please chose product before press delete Product button");
            alert.showAndWait();
        }

    }
    public void DeletePackage() throws SQLException {
        try {
            String packageName = PackageList.getValue().toString();
            int count=0;
            count=getAmountOfInvetation(packageName);
            if (count!=0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eror");
                alert.setHeaderText("Can Not Finish Deleting a package , There is invitation on this package. ");
                alert.showAndWait();
                return;
            }
            PreparedStatement statement = Main.conn.prepareStatement("DELETE from Packages WHERE PackageName = ?");
            statement.setString(1, packageName);
            statement.executeUpdate();
            PreparedStatement statement1 = Main.conn.prepareStatement("DELETE from RealEstate WHERE packageName = ?");
            statement1.setString(1, packageName);
            statement1.executeUpdate();
            PreparedStatement statement2 = Main.conn.prepareStatement("DELETE from Vehicle WHERE packageName = ?");
            statement2.setString(1, packageName);
            statement2.executeUpdate();
            PreparedStatement statement3 = Main.conn.prepareStatement("DELETE from Pets WHERE packageName = ?");
            statement3.setString(1, packageName);
            statement3.executeUpdate();
            PreparedStatement statement4 = Main.conn.prepareStatement("DELETE from SecondHand WHERE packageName = ?");
            statement4.setString(1, packageName);
            statement4.executeUpdate();
            PackageList.getItems().remove(packageName);
            ProductList.getItems().clear();
        }
        catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Eror");
            alert.setHeaderText("Please chose Package before press Delete Package button");
            alert.showAndWait();
        }
        //PackageList.getItems().removeAll();
    }
    public int getAmountOfInvetation(String packageName) throws SQLException {
        int count=0;
        String sql = "select count(*) from Inventation WHERE PackageName = '" + packageName+ "' ";
        ResultSet rs = Main.stat.executeQuery(sql);
        while (rs.next())
        {
            count=rs.getInt(1);
        }
        return count;
    }
    public void editPack() throws IOException {
        packageChoose=PackageList.getValue().toString();
        Stage Login = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("EditPackageOption.fxml"));
        Scene s2 = new Scene(root2, 720, 400);
        s2.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
        Login.setScene(s2);
        Login.show();
    }
}