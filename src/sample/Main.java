package sample;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class Main extends Application {
    public static Boolean LogIn =false;
    public static HashSet<String> Usersnames;
    public static HashSet<String> Emails;
    public static HashMap<String,String> userNameToMale;
    public static HashSet<String> packagesNames;
    Stage window;
    Scene s1;
    Scene s2;
    Stage Login;
    static  Connection conn;
    static Statement stat;
    static int PackageId;
    static int vehicleId;
    static int realEstateId;
    static int PetsId;
    static int secondHandId;

    static StringProperty User=new SimpleStringProperty("Guest");
    static BooleanProperty logOutBool= new SimpleBooleanProperty(false);
    static BooleanProperty SignInBool= new SimpleBooleanProperty(true);
    static BooleanProperty ProductsBool= new SimpleBooleanProperty(false);
    @FXML
    public TitledPane Acordion;
    public  Label UserLab;
    public Button logOut;
    public Button  LoginButton;
    public static void SetLogIn()
    {
        LogIn=false;
        logOutBool.setValue(false);
        SignInBool.setValue(true);
        ProductsBool.setValue(false);
    }

    public static String getUserName() {
        String name=User.getValue().toString();
        name=name.substring(name.indexOf(" ")+1);
        return name;
    }

    public  void LogOut()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Comfirmation Log Out");
        alert.setHeaderText("Log Out");
        alert.setContentText("For loging out please press ok");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            setUser("Hello Guest");
            SetLogIn();
        }
    }
    public static void setUser(String name)
    {
       User.setValue(name);
       LogIn=true;
       logOutBool.setValue(true);
        SignInBool.setValue(false);
        ProductsBool.setValue(true);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        stat = conn.createStatement();
     //   stat.executeUpdate("drop table if exists people;");
      //  stat.executeUpdate("create table people (Username, password,FirstName,LastName,EmailAddress,phone,picture,date);");
       //   stat.executeUpdate("drop table if exists RealEstate;");
       //  stat.executeUpdate("create table RealEstate (Username, id,price,address,amountofpeople,picture,Priority,type,packageName);");
      //  stat.executeUpdate("drop table if exists Packages;");
      //  stat.executeUpdate("create table Packages (PackageName,Username,price,area,type,amountOfProducts,PackageType);");
      //  stat.executeUpdate("drop table if exists Inventation;");
      // stat.executeUpdate("create table Inventation (Username,StartDate,EndDate,PackageName,status,price);");
      //  String UserName,String VehicleName,String pric,String adres,String ManuFa,String model,String year,String numOwn,String pic,String disc
        //  stat.executeUpdate("drop table if exists Vehicle;");
       //  stat.executeUpdate("create table Vehicle (UserName, VehicleName,price,address,ManuFa,model,year,picture,packageName,disc,numOwn);");
        //  stat.executeUpdate("drop table if exists Pets;");
        //  stat.executeUpdate("create table Pets (Username, petName,petAge,address,picture,type,jender,description,packageName,price);");
       // stat.executeUpdate("drop table if exists SecondHand;");
       // stat.executeUpdate("create table SecondHand (Username, productName,ProductType,manufacturer,YearOfManufacture,address,picture,description,packageName,price);");
        //    stat.executeUpdate("drop table if exists Inventation;");
        //  stat.executeUpdate("create table Inventation (Username,StartDate,EndDate,PackageName,status);");
         //   stat.executeUpdate("drop table if exists TradeIn;");
         // stat.executeUpdate("create table TradeIn (Username1Id,Username2Id,StartDate,EndDate,Package1Id,Package2Id,status);");
        conn.setAutoCommit(true);
        Usersnames=new HashSet<>();
        CalculateId();
        Emails=new HashSet<>();
        userNameToMale=new HashMap<>();
        packagesNames=new HashSet<>();
        ResultSet rs = Main.stat.executeQuery("select * from people;");
        while (rs.next()) {
            Usersnames.add(rs.getString("Username"));
            Emails.add(rs.getString("EmailAddress"));
            userNameToMale.put(rs.getString("Username"),rs.getString("EmailAddress"));
        }

        ResultSet rs1 = Main.stat.executeQuery("SELECT * from Packages " );
        while (rs1.next()) {
            packagesNames.add(rs1.getString("PackageName"));
        }

        window=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene s1 = new Scene(root, 720,400);
        primaryStage.setTitle("Product Renter");
        s1.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        primaryStage.setScene(s1 );
        primaryStage.show();
    }

    public void Login() throws IOException {
            UserLab.textProperty().bind(User);
            Acordion.visibleProperty().bind(ProductsBool);
            logOut.visibleProperty().bind(logOutBool);
            LoginButton.visibleProperty().bind(SignInBool);
            Stage Login = new Stage();
            Parent root2 = FXMLLoader.load(getClass().getResource("Login.fxml"));
            s2 = new Scene(root2, 720, 400);
            s2.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
            Login.setScene(s2);
            Login.show();
    }
    public void update() throws IOException, SQLException {
        int count=getAmountOfProducts();
        if (count>=1) {
            Stage update = new Stage();
            Parent root2 = FXMLLoader.load(getClass().getResource("EditProducts.fxml"));
            Scene s = new Scene(root2, 720, 400);
            s.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
            update.setScene(s);
            update.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Eror");
            alert.setHeaderText("Only users that have at least 1 package can manage packages");
            alert.showAndWait();
        }
    }
    public static int getAmountOfProducts() throws SQLException {
        String name=Main.User.getValue().toString();
        name=name.substring(name.indexOf(" ")+1);
        String sql1="select DISTINCT PackageName from Packages WHERE Username = '"+name+"'";
        ResultSet rs1 = stat.executeQuery(sql1);
        int count=0;
        while (rs1.next()) {
            count++; break;
        }
        return  count;
    }
    public static void SendMail(String emailaddress , String messageToSent, String subject)
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("everything4Rents@gmail.com","12345678n");
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("everything4Rents@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailaddress));
            message.setSubject(subject);
            message.setText(messageToSent);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void AddPackgage() throws IOException {
        Stage AdPackage = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("AddPackage.fxml"));
        Scene s = new Scene(root2, 720, 400);
        s.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
        Acordion.setExpanded(false);
        AdPackage.setScene(s);
        AdPackage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    public void Search(ActionEvent actionEvent) throws IOException {
        Stage Search = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("search.fxml"));
        Scene s = new Scene(root2, 720, 400);
        s.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
        Search.setScene(s);
        Search.show();
    }

    public static List<String> getPackOfUser() throws SQLException {
        List<String> toReturn =new ArrayList<>();
        String name=Main.User.getValue().toString();
        name=name.substring(name.indexOf(" ")+1);
        String sql1="select DISTINCT PackageName from Packages WHERE Username = '"+name+"'";
        ResultSet rs1 = stat.executeQuery(sql1);
        while (rs1.next()) {
            toReturn.add(rs1.getString("PackageName"));
        }
        return  toReturn;
    }
    public static void CalculateId() throws SQLException {
        ResultSet rs = Main.stat.executeQuery("select count(*) from Packages;");
        while (rs.next()) {
            PackageId= rs.getInt(1);
        }
        ResultSet rs4 = Main.stat.executeQuery("select count(*) from RealEstate;");
        while (rs4.next()) {
            realEstateId= rs4.getInt(1);
        }
        ResultSet rs1 = Main.stat.executeQuery("select count(*) from Pets;");
        while (rs1.next()) {
            PetsId= rs1.getInt(1);
        }
        ResultSet rs2 = Main.stat.executeQuery("select count(*) from SecondHand;");
        while (rs2.next()) {
            secondHandId= rs2.getInt(1);
        }
        ResultSet rs3 = Main.stat.executeQuery("select count(*) from Vehicle;");
        while (rs3.next()) {
            vehicleId= rs3.getInt(1);
        }

    }
}
