package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;


public class Controller {
    @FXML
    private TextField user;
    @FXML
    private TextField password;
    @FXML
    private TextField host;
    @FXML
    private Button quitbtn;
    @FXML
    private ListView<String> serverList = new ListView<>();
    @FXML
    private ListView<String> localList = new ListView<>();

    private Ftp f  = new Ftp();
    private final Stage primaryStage = new Stage();
    private ObservableList<String> file = FXCollections.observableArrayList();
    private ObservableList<String> local = FXCollections.observableArrayList();
    private ObservableList<String> state = FXCollections.observableArrayList();
    private String text;
    private String pw;
    private String address;
    private String path;
    private String currentItemSelected;



    @FXML
    public void initialize()
    {
        user.setText("root");
        password.setText("1234");
        host.setText("192.168.11.8");
        quitbtn.setDisable(true);
        goofier();
    }

    @FXML
    void connection()
    {
        text = user.getText();
        pw = password.getText();
        address = host.getText();

        if (text.isEmpty())
            return;

        try{
            f.openConnection(address);
            f.getMsgs() ;               // 受信スレッドの開始
            f.doLogin(text,pw);

        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        file = FXCollections.observableArrayList(f.doLs());
        serverList.setItems(file);

        location();
        quitbtn.setDisable(false);
    }

    @FXML
    void quit()
    {
        try{
            f.doQuit();
            f.closeConnection();

        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }


    public void goofier()
    {
        serverList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    //Use ListView's getSelected Item
                    final int selectedIdx = serverList.getSelectionModel()
                            .getSelectedIndex();
                    currentItemSelected = file.get(selectedIdx);
                    String selected = f.state.get(selectedIdx);
                    //use this to do whatever you want to. Open Link etc.

                    f.doCd(currentItemSelected,selected);
                    f.doLs();

                    directory();
                }
            }
        });
    }


    @FXML
    public void retr()
    {
        final int selectedIdx = serverList.getSelectionModel().getSelectedIndex();

        currentItemSelected = file.get(selectedIdx);

        if(f.state.get(selectedIdx).equals("f")) {
            f.doGet(currentItemSelected);
        }

        System.out.println("selectIdx: " + selectedIdx);
        System.out.println("item: " + currentItemSelected);

        location();
        directory();
    }


    @FXML
    void stor()
    {
        final int selectedIdx = localList.getSelectionModel().getSelectedIndex();

        currentItemSelected = local.get(selectedIdx);

        if(state.get(selectedIdx).equals("f")) {
            f.doPut(currentItemSelected);
        }

        System.out.println("selectIdx: " + selectedIdx);
        System.out.println("item: " + currentItemSelected);

        location();
        directory();
    }

    @FXML
    void add()
    {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);

        //Image image = new Image(getClass().getResourceAsStream("facebook_1.png"));
        Label label = new Label("File");
        TextField text = new TextField();
        Button button = new Button("Add");

        //label.setGraphic(new ImageView(image));
        //label.setTextFill(Color.web("#0076a3"));
        text.setMaxWidth(100);

        dialogVbox.getChildren().add(label);
        dialogVbox.getChildren().add(text);
        dialogVbox.getChildren().add(button);

        Scene dialogScene = new Scene(dialogVbox, 300, 300);
        dialog.setScene(dialogScene);
        dialog.show();

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String additional = text.getText();
                System.out.println(additional);

                f.doCreate(additional);
                f.doLs();

                directory();
                dialog.close();
            }
        });
    }

    @FXML
    void delete()
    {
        final int selectedIdx = serverList.getSelectionModel().getSelectedIndex();

        String itemToRemove = file.get(selectedIdx);


        if(f.state.get(selectedIdx).equals("f")) {
            f.doDel(itemToRemove);
        }

        if(f.state.get(selectedIdx).equals("d")) {
            f.doDir(itemToRemove);
        }


        System.out.println("selectIdx: " + selectedIdx);
        System.out.println("item: " + itemToRemove);
        file.remove(selectedIdx);

        directory();
    }


    public void location()
    {
        local.clear();
        state.clear();

        path = System.getProperty("user.dir");
        File dir = new File(path);
        File[] files = dir.listFiles();

        for (File f : files) {

            if(f.isDirectory()){
                local.add(f.getName());
                state.add("d");
            }

            if (f.isFile()){
                local.add(f.getName());
                state.add("f");
            }
        }

        for(String f:local)
            System.out.println(f);

        localList.getSelectionModel().clearSelection();
        localList.setItems(local);
    }


    public void directory()
    {
        serverList.getSelectionModel().clearSelection();
        file = FXCollections.observableArrayList(f.doLs());
        serverList.setItems(file);
    }
}
