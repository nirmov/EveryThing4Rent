package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 12/28/2017.
 */
public class LoginController {
    @FXML
    public Button confirm;
    public TextField UserName;
    public PasswordField Pass;
    public void Log() throws SQLException {
        String User=UserName.getText();
        String pas=Pass.getText();
        String name="";
        ResultSet rs = Main.stat.executeQuery("select * from people;");
        boolean b=false;
        while (rs.next()) {
            if (rs.getString("Username").equals(User)&&rs.getString("password").equals(pas)) {
                b = true;
                name=rs.getString("Username");
            }
        }
        if (b)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Succsecfull");
            alert.setHeaderText("Welcome back "+User);
            alert.showAndWait();
            Stage window = (Stage) confirm.getScene().getWindow();
            Main.setUser("Hello "+name);
            window.close();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cant finish Login");
            alert.setHeaderText("Please check userNameOrPassword");
            alert.showAndWait();
        }
    }
    public void home()
    {
        Stage window = (Stage) confirm.getScene().getWindow();
        window.close();
    }
    public void SignUp() throws IOException {
        Stage signUpStage=new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Scene s3 = new Scene(root2, 720,400);
        s3.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
        signUpStage.setScene(s3);

        signUpStage.show();
        Stage window = (Stage) confirm.getScene().getWindow();
        window.close();
    }
    public void forgotPass() throws SQLException {
        String Name=UserName.getText();
        String email= new String("");
        String PassWord=new String("");
        String firstName = new String("");
        if (Name!=null&&Main.Usersnames.contains(Name))
        {

            ResultSet rs = Main.stat.executeQuery("select * from people;");
            while (rs.next()) {
                if (rs.getString("Username").equals(Name)) {
                    email=rs.getString("EmailAddress");
                    PassWord=rs.getString("password");
                    firstName=rs.getString("FirstName");
                }
            }
            final String mail=email;
            final String first=firstName;
            final String Pass=PassWord;
            Thread thread = new Thread(new Runnable()
            {
                public void run()
                {
                    Main.SendMail(mail,first+" your password is \n"+Pass,"Password recovery");
                }
            });
            thread.start();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Recovery");
            alert.setHeaderText("Your Password has been sent to your email address ");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Recovery");
            alert.setHeaderText("Username is not in the system , please enter valid UserName at the TextField");
            alert.showAndWait();
        }
    }
}
